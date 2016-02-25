/*
 Copyright (c) 2011, 2012, 2013, 2014 The Regents of the University of
 Sydney. All Rights Reserved.  Redistribution and use in
 source and binary forms, with or without modification, are permitted
 provided that the following conditions are met:

    * Redistributions of source code must retain the above
      copyright notice, this list of conditions and the following
      two paragraphs of disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      two paragraphs of disclaimer in the documentation and/or other materials
      provided with the distribution.
    * Neither the name of the Regents nor the names of its contributors
      may be used to endorse or promote products derived from this
      software without specific prior written permission.

 IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 REGENTS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF
 ANY, PROVIDED HEREUNDER IS PROVIDED "AS IS". REGENTS HAS NO OBLIGATION
 TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 MODIFICATIONS.
*/

package LAD

import Chisel._

class MSDFAddModule extends Module {
  val io = new Bundle {
    val a = UInt(INPUT, 2)
    val b = UInt(INPUT, 2)
    val start = Bool(INPUT)
    val c = UInt(OUTPUT, 2)
  }
  io.c := Reg(next=MSDFAdd(Reg(next=io.a), Reg(next=io.b), Reg(next=io.start)))
}


class MSDFLMSModule extends Module {
    val io = new Bundle {
        val x = Vec.fill(2){UInt(INPUT, 2)}
        val inW = Vec.fill(2){UInt(INPUT, 2)}
        val y = UInt(INPUT, 2)
        val stepSize = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val ybar = UInt(OUTPUT, 2)
        val outErr = UInt(OUTPUT, 2)
        val outStep = UInt(OUTPUT, 2)
        val outW = Vec.fill(2){UInt(OUTPUT, 2)}
    }
    val threeDelay = ShiftRegister(io.start, 3)
    val dotProduct = (io.x, io.inW).zipped.map((ai, bi) => MSDFMul(ai, bi, io.start)).reduce((r, c) => MSDFAdd(r, c, threeDelay)) // 5 Delay

    io.ybar := dotProduct

    val yDelay = UInt(width=2)
    yDelay := ShiftRegister(io.y, 5)
    val fiveDelay = ShiftRegister(io.start, 5)
    val err = MSDFSub(yDelay, dotProduct, fiveDelay) // 2 Delay (7 Total)
    io.outErr := err

    val stepDelay = UInt(width=2)
    stepDelay := ShiftRegister(io.stepSize, 7)
    val sevenDelay = ShiftRegister(io.start, 7)
    val step = MSDFMul(stepDelay, err, sevenDelay) // 3 Delay (10 Total)
    io.outStep := step

    val tenDelay = ShiftRegister(io.start, 10)
    val xDelay = Vec.fill(2){UInt(width=2)}
    xDelay := ShiftRegister(io.x, 10)
    val wDelay = Vec.fill(2){UInt(width=2)}
    wDelay := ShiftRegister(io.inW, 13)
    val thirteenDelay = ShiftRegister(io.start, 13)
    val wUpdate = (wDelay, xDelay.map(x1 => MSDFMul(x1, step, tenDelay))).zipped.map((w1, x1) => MSDFAdd(w1, x1, thirteenDelay)) // 5 Delay (15 Total)

    io.outW := wUpdate
}

class MSDFMulModule extends Module {
    val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
    }
    
    io.c := Reg(next=MSDFMul(Reg(next=io.a), Reg(next=io.b), Reg(next=io.start)))
}



object Top {
  def main(args: Array[String]): Unit = {
    val theArgs = Array("--backend", "v", "--genHarness")
    chiselMain(theArgs, () => Module(new MSDFAddModule()))
  }
}
