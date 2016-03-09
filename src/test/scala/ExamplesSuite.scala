
/*
 Copyright (c) 2011, 2012, 2013, 2014 The Regents of the University of
 California (Regents). All Rights Reserved.  Redistribution and use in
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

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import java.io.File
import org.junit.Assert._
import org.junit.Test
import org.junit.Ignore


import Chisel._

class ExamplesSuite extends TestSuite {

    def toFixed(x : Int, fracWidth : Int) : BigInt = toFixed(x.toDouble, fracWidth)
    def toFixed(x : Float, fracWidth : Int) : BigInt = toFixed(x.toDouble, fracWidth)
    def toFixed(x : Double, fracWidth : Int) : BigInt = BigInt(scala.math.round(x*scala.math.pow(2, fracWidth)))
    def toDouble(x : BigInt, fracWidth : Int) : Double = x.toDouble/scala.math.pow(2, fracWidth)

    val r = new scala.util.Random
    val testDir = new File("test-outputs")
    val trialLength = 1000
    val totalWidth = 64
    val fracWidth = 32


    @Test def testFixedFIR() {
        val numTaps = 16
        val taps = List.fill(numTaps){r.nextDouble()}

        class FixedFIRTest(c : FixedFIR) extends Tester(c) {

            var aRegs : List[Double] = List.fill(numTaps){0.0}

            for (i <- 0 until trialLength){
                val expected = (taps, aRegs).zipped.map((tap, aReg) => tap*aReg).reduce(_+_)
                val aVal = r.nextDouble()
                val a = Fixed(aVal, c.totalWidth, c.fracWidth).litValue()
                aRegs = aVal :: aRegs.dropRight(1)
                poke(c.io.a, a)
                val res = toDouble(peek(c.io.out), c.fracWidth)
                val err = scala.math.abs(scala.math.abs(expected) - scala.math.abs(res))
                val correct = if (err > scala.math.pow(2, -c.fracWidth + 4)) false else true
                val errMessage = "Expected: %4.4f\tGot: %4.4f\tError: %4.6f".format(expected, res, err)
                expect(correct, errMessage)
                step(1)
            }
        }


        //launchCppTester((c : MSDFMulAddTest(taps, totalWidth, fracWidth)) => new MSDFMulAddTests(c))
        chiselMainTest(Array("--genHarness", "--compile", "--test", "--backend", "c", "--targetDir", testDir.getPath.toString()), () => {
            Module(new FixedFIR(taps, totalWidth, fracWidth))
        }) { c => new FixedFIRTest(c) }
    }


}
