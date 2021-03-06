/*
 Copyright (c) 2011, 2012, 2013, 2014 The University of Sydney.
 All Rights Reserved.  Redistribution and use in source and 
 binary forms, with or without modification, are permitted
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

package Chisel

import Node._
import ChiselError._

object FullAdder {
  def apply(a : UInt, b : UInt, cin : UInt) : (UInt, UInt) = {
    val FAMod = Module(new FullAdderModule())
    FAMod.io.a := a
    FAMod.io.b := b
    FAMod.io.cin := cin
    (FAMod.io.o, FAMod.io.cout)
  }
}

class FullAdderModule extends Module {
    val io = new Bundle {
        val a = UInt(INPUT, 1)
        val b = UInt(INPUT, 1)
        val cin = UInt(INPUT, 1)
        val o = UInt(OUTPUT, 1)
        val cout = UInt(OUTPUT, 1)
    }
    io.o := (io.a ^ io. b) ^ io.cin
    io.cout := (io.a & io.b) | (io.a & io.cin) | (io.b & io.cin)
}

object CarrySaveAdder {
  def apply(a : UInt, b : UInt, c : UInt, cin : UInt) : (UInt, UInt) = {
    val vc = Vec.fill(b.getWidth() + 1){UInt(width=1)}
    vc(0) := cin
    val vs = Vec.fill(b.getWidth()){UInt(width=1)}
    for (i <- 0 until b.getWidth()) {
      val (r, cs) = FullAdder(a(i), b(i), c(i))
      vs(i) := r
      vc(i + 1) := cs
    }
    (vs.toBits.toUInt, vc.toBits.toUInt)
  }

  def apply(a : UInt, b : UInt) : (UInt, UInt) = CarrySaveAdder(a, b, UInt(0), UInt(0))
  def apply(a : UInt, b : UInt, c : UInt) : (UInt, UInt) = CarrySaveAdder(a, b, c, UInt(0))
}

object RippleCarryAdder {
  def apply(a : UInt, b : UInt, cin : UInt) : (UInt, UInt) = {
    val cout = Vec.fill(b.getWidth() + 1){UInt(width=1)}
    cout(0) := cin
    val res = Vec.fill(b.getWidth()){UInt(width=1)}
    for (i <- 0 until b.getWidth()) {
      val (r, c) = FullAdder(a(i), b(i), cout(i))
      res(i) := r
      cout(i+1) := c
    }
    (res.toBits.toUInt, cout(b.getWidth()))
  }
}

object fourToTwoAdder {
  def apply(a : UInt, b : UInt, c : UInt, d : UInt, cin1 : UInt, cin2 : UInt) : (UInt, UInt) =  {
    val fTTA = Module(new fourToTwoAdderModule(a.getWidth()))
    fTTA.io.a := a
    fTTA.io.b := b
    fTTA.io.c := c
    fTTA.io.d := d
    fTTA.io.cin1 := cin1
    fTTA.io.cin2 := cin2
    (fTTA.io.o, fTTA.io.cout)
  }

  def apply(a : UInt, b : UInt, c : UInt, d : UInt, cin : UInt) : (UInt, UInt) = fourToTwoAdder(a, b, c, d, cin, UInt(0))
  def apply(a : UInt, b : UInt, c : UInt, d : UInt) : (UInt, UInt) = fourToTwoAdder(a, b, c, d, UInt(0), UInt(0))
}

class fourToTwoAdderModule(inWidth : Int) extends Module {
    val io = new Bundle {
        val a = UInt(INPUT, inWidth)
        val b = UInt(INPUT, inWidth)
        val c = UInt(INPUT, inWidth)
        val d = UInt(INPUT, inWidth)
        val cin1 = UInt(INPUT, 1)
        val cin2 = UInt(INPUT, 1)
        val o = UInt(OUTPUT, inWidth)
        val cout = UInt(OUTPUT, inWidth)
    }
    
    val vs = Vec.fill(inWidth){UInt(width=1)}
    val vc = Vec.fill(inWidth + 1){UInt(width=1)}
    vc(0) := io.cin1

    val cT = Vec.fill(inWidth + 1){UInt(width=1)}
    val sT = Vec.fill(inWidth){UInt(width=1)}
    cT(0) := io.cin2

    // Two Layers of Adders
    for (i <- 0 until inWidth) {
      val (s0, c0) = FullAdder(io.a(i), io.b(i), io.c(i))
      val (s1, c1) = FullAdder(s0, io.d(i), cT(i))
      cT(i+1) := c0
      vs(i) := s1
      vc(i+1) := c1
    }
    val vcF = Vec.fill(inWidth){UInt(width=1)}
    for (i <- 0 until inWidth) {
      vcF(i) := vc(i)
    }

    io.o := vs.toBits.toUInt
    io.cout := vcF.toBits.toUInt


}

object threeToTwoAdder {
  def apply(a : UInt, b : UInt, c : UInt, cin : UInt) : (UInt, UInt) =  {
    val vc = Vec.fill(b.getWidth() + 1){UInt(width=1)}
    vc(0) := cin
    val vs = Vec.fill(b.getWidth()){UInt(width=1)}
    for (i <- 0 until b.getWidth()) {
      val (r, cs) = FullAdder(a(i), b(i), c(i))
      vs(i) := r
      vc(i + 1) := cs
    }

    val vcF = Vec.fill(a.getWidth()){UInt(width=1)}
    for (i <- 0 until a.getWidth()) {
      vcF(i) := vc(i)
    }

    (vs.toBits.toUInt, vcF.toBits.toUInt)
  }

  def apply(a : UInt, b : UInt, c : UInt) : (UInt, UInt) = fourToTwoAdder(a, b, c, UInt(0))
}
