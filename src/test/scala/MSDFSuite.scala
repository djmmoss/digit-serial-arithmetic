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
import org.junit.Assert._
import org.junit.Test
import org.junit.Ignore


import Chisel._

class MSDFSuite extends TestSuite {

  val trials = 10
  val r = scala.util.Random
  val totalWidth = 16

  val digit = 4
  val n = totalWidth/digit
  
  @Test def testMSDFAddMul() {
    class MSDFAddMulTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      val addRes = MSDFAdd(io.a, io.b, io.start)
      val mulStart = ShiftRegister(io.start, 2)
      io.c := MSDFMul(addRes, addRes, mulStart)
    }

    class MSDFAddMulTests(c : MSDFAddMulTest) extends Tester(c) {
      for (i <- 0 until trials) {
        val dA = r.nextDouble()/4
        val dB = r.nextDouble()/4
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        // Delay = 5 Mul + Add
        for (j <- 0 until a.length + 5) {
          val inA = if(j < a.length) a(j) else 0
          val inB = if(j < b.length) b(j) else 0
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          poke(c.io.b, SignedDigit.toSignedDigit(inB))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          if (j >= 5)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        
        val aAddB = SignedDigit.signedToDouble(a)+SignedDigit.signedToDouble(b)
        val expectedRes = aAddB*aAddB
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString  + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }

    launchCppTester((c : MSDFAddMulTest) => new MSDFAddMulTests(c))
  }

  @Test def testMSDFMulAdd() {
    class MSDFMulAddTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      val mulRes = MSDFMul(io.a, io.b, io.start)
      val addStart = ShiftRegister(io.start, 3)
      io.c := MSDFAdd(mulRes, mulRes, addStart)

    }

    class MSDFMulAddTests(c : MSDFMulAddTest) extends Tester(c) {
      for (i <- 0 until trials) {
        val dA = r.nextDouble()/2
        val dB = r.nextDouble()/2
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        // Delay = 5 Mul + Add
        for (j <- 0 until a.length + 5) {
          val inA = if(j < a.length) a(j) else 0
          val inB = if(j < b.length) b(j) else 0
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          poke(c.io.b, SignedDigit.toSignedDigit(inB))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          if (j >= 5)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        
        val aMulB = SignedDigit.signedToDouble(a)*SignedDigit.signedToDouble(b)
        val expectedRes = aMulB+aMulB
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString  + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }

    launchCppTester((c : MSDFMulAddTest) => new MSDFMulAddTests(c))
  }

  @Test def testMSDFDotProduct() {
    class MSDFDotProductTest extends Module {
      val io = new Bundle {
        val a = Vec.fill(4){UInt(INPUT, 2)}
        val b = Vec.fill(4){UInt(INPUT, 2)}
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }

      io.c := MSDFDotProduct(io.a, io.b, io.start)
    }

    class MSDFDotProductTests(c : MSDFDotProductTest) extends Tester(c) {

      for (i <- 0 until trials) {
        val dA = List.fill(4){r.nextDouble()/4}
        val dB = List.fill(4){r.nextDouble()/4}
        val a = dA.map(in => SignedDigit.doubleToSigned(in, 8))
        val b = dB.map(in => SignedDigit.doubleToSigned(in, 8))
        val res = new ArrayBuffer[Int]
        for (j <- 0 until a(0).length + 7) {
          for (k <- 0 until 4) {
            val inA = if(j < a(k).length) a(k)(j) else 0
            val inB = if(j < b(k).length) b(k)(j) else 0
            poke(c.io.a(k), SignedDigit.toSignedDigit(inA))
            poke(c.io.b(k), SignedDigit.toSignedDigit(inB))
          }
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          peek(c.io.c)
          if (j >= 7)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = (a.map(x => SignedDigit.signedToDouble(x)), b.map(x => SignedDigit.signedToDouble(x))).zipped.map(_*_).reduce(_+_)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -7)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFDotProductTest) => new MSDFDotProductTests(c))
  }

  @Test def testMSDFDistance() {
    class MSDFDistanceTest extends Module {
      val io = new Bundle {
        val a = Vec.fill(4){UInt(INPUT, 2)}
        val b = Vec.fill(4){UInt(INPUT, 2)}
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }

      io.c := MSDFDistance(io.a, io.b, io.start)
    }

    class MSDFDistanceTests(c : MSDFDistanceTest) extends Tester(c) {

      for (i <- 0 until trials) {
        val dA = List.fill(4){r.nextDouble()/2}
        val dB = List.fill(4){r.nextDouble()/8}
        val a = dA.map(in => SignedDigit.doubleToSigned(in, 8))
        val b = dB.map(in => SignedDigit.doubleToSigned(in, 8))
        val res = new ArrayBuffer[Int]
        for (j <- 0 until a(0).length + MSDFDistance.reportDelay(4)) {
          for (k <- 0 until 4) {
            val inA = if(j < a(k).length) a(k)(j) else 0
            val inB = if(j < b(k).length) b(k)(j) else 0
            poke(c.io.a(k), SignedDigit.toSignedDigit(inA))
            poke(c.io.b(k), SignedDigit.toSignedDigit(inB))
          }
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          peek(c.io.c)
          if (j >= MSDFDistance.reportDelay(4))
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = (a.map(x => SignedDigit.signedToDouble(x)), b.map(x => SignedDigit.signedToDouble(x))).zipped.map(_-_).map(x => x*x).reduce(_+_)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -7)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFDistanceTest) => new MSDFDistanceTests(c))
  }

  @Test def testMSDFL2Norm() {
    class MSDFL2NormTest extends Module {
      val io = new Bundle {
        val a = Vec.fill(8){UInt(INPUT, 2)}
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }

      io.c := MSDFL2Norm(io.a, io.start)
    }

    class MSDFL2NormTests(c : MSDFL2NormTest) extends Tester(c) {

      for (i <- 0 until trials) {
        val dA = List.fill(8){r.nextDouble()/8}
        val a = dA.map(in => SignedDigit.doubleToSigned(in, 8))
        val res = new ArrayBuffer[Int]
        for (j <- 0 until a(0).length + 9) {
          for (k <- 0 until 8) {
            val inA = if(j < a(k).length) a(k)(j) else 0
            poke(c.io.a(k), SignedDigit.toSignedDigit(inA))
          }
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          peek(c.io.c)
          if (j >= 9)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = scala.math.sqrt(a.map(x => SignedDigit.signedToDouble(x)).map(x => x*x).reduce(_+_))
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -7)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFL2NormTest) => new MSDFL2NormTests(c))
  }

  @Test def testMSDFAdderTree() {
    class MSDFAdderTreeTest extends Module {
      val io = new Bundle {
        val a = Vec.fill(8){UInt(INPUT, 2)}
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }

      io.c := MSDFAdderTree(io.a, io.start)
    }

    class MSDFAdderTreeTests(c : MSDFAdderTreeTest) extends Tester(c) {

      for (i <- 0 until trials) {
        val dA = List.fill(8){r.nextDouble()/8}
        val a = dA.map(in => SignedDigit.doubleToSigned(in, 8))
        val res = new ArrayBuffer[Int]
        for (j <- 0 until a(0).length + 6) {
          for (k <- 0 until 8) {
            val inA = if(j < a(k).length) a(k)(j) else 0
            poke(c.io.a(k), SignedDigit.toSignedDigit(inA))
          }
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          peek(c.io.c)
          if (j >= 6)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = a.map(x => SignedDigit.signedToDouble(x)).reduce(_+_)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -7)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFAdderTreeTest) => new MSDFAdderTreeTests(c))
  }

  @Test def testMSDFLiteral() {
    class MSDFLiteralTest extends Module {
      val io = new Bundle {
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFLiteral(UInt("b10011001"))
    }

    class MSDFLiteralTests(c : MSDFLiteralTest) extends Tester(c) {
      for (i <- 0 until trials) {
        val res = List(2, 1, 2, 1)
        for (j <- 0 until 4) {
          expect(c.io.c, res(j))
          step(1)
        }
      }
    }
    launchCppTester((c : MSDFLiteralTest) => new MSDFLiteralTests(c))
  }

  @Test def testMSDFLiteralInitial() {
    class MSDFLiteralInitialTest extends Module {
      val io = new Bundle {
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFLiteral(UInt("b10011001"), 1)
    }

    class MSDFLiteralInitialTests(c : MSDFLiteralInitialTest) extends Tester(c) {
      for (i <- 0 until trials) {
        val res = List(1, 2, 1, 2)
        for (j <- 0 until 4) {
          expect(c.io.c, res(j))
          step(1)
        }
      }
    }
    launchCppTester((c : MSDFLiteralInitialTest) => new MSDFLiteralInitialTests(c))
  }


  @Test def testMSDFRegister() {
    class MSDFRegisterTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val update = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      val wVec = MSDFRegister(UInt("b0010011000011000"))
      val w = MSDFRegister(wVec)
      when (io.update) { 
        w := io.a 
        wVec(UInt(0)) := UInt(1)
      }
      io.c := w
    }

    class MSDFRegisterTests(c : MSDFRegisterTest) extends Tester(c) {
      val res1 = List(0, 2, 1, 2, 0, 1, 2, 0)
      for (j <- 0 until 8) {
        poke(c.io.a, 1)
        if (j == 3) poke(c.io.update, 1) else poke(c.io.update, 0)
        expect(c.io.c, res1(j))
        step(1)
      }
      val res2 = List(1, 2, 1, 1, 0, 1, 2, 0)
      for (j <- 0 until 8) {
        poke(c.io.a, 1)
        poke(c.io.update, 0)
        expect(c.io.c, res2(j))
        step(1)
      }
    }
    launchCppTester((c : MSDFRegisterTest) => new MSDFRegisterTests(c))
  }

  @Test def testMSDFAddAccum() {
    class MSDFAddAccumTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      val wVec = MSDFRegister(UInt("b0000000000000000"))
      val w = MSDFRegister(wVec)
      val addRes = MSDFAdd(io.a, w, io.start)
      val nextAddStart = nextStart(io.start, 2)
      val wCounter = updateCounter(nextAddStart, wVec.length)
      wVec(wCounter) := addRes
      io.c := addRes
    }

    class MSDFAddAccumTests(c : MSDFAddAccumTest) extends Tester(c) {
      var dB = 0.0
      for (i <- 0 until 5) {
        val dA = r.nextDouble()/8
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        for (j <- 0 until 8 + 2) {
          val inA = if(j < a.length) a(j) else 0
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          if (j >= 2)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = SignedDigit.signedToDouble(a)+SignedDigit.signedToDouble(b)
        val dRes = SignedDigit.signedToDouble(res.toList)
        dB = dRes
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "(" + (dA+dB).toString + ")" + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFAddAccumTest) => new MSDFAddAccumTests(c))
  }

  @Test def testMSDFLMSForward() {
    class MSDFLMSTest extends Module {
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

    class MSDFLMSTests(c : MSDFLMSTest) extends Tester(c) {

      def compare(expectedRes : Double, dRes : Double) {
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }

      def update(x : List[Double], y : Double, w : List[Double], stepSize : Double) = {
        val yBar = (x, w).zipped.map(_*_).reduce(_+_)
        val err = y - yBar
        val step = stepSize*err
        val wUpdate = (x.map(step*_), w).zipped.map(_+_)
        (yBar, wUpdate, err, step)
      }

      val digitSize = 16
      val ybarDelay = 5
      val errDelay = 7
      val stepDelay = 10
      val wUpdateDelay = 15

      val stepSize : Double = 0.125

      for (i <- 0 until trials) {

        val dX : List[Double] = List.fill(2){r.nextDouble()/8}
        val dW : List[Double] = List.fill(2){r.nextDouble()/8}
        val dY : Double = r.nextDouble()/8
        val x = dX.map(in => SignedDigit.doubleToSigned(in, digitSize))
        val w = dW.map(in => SignedDigit.doubleToSigned(in, digitSize))
        val y = SignedDigit.doubleToSigned(dY, digitSize)
        val stepS = SignedDigit.doubleToSigned(stepSize, digitSize)

        val wRes = List.fill(2){new ArrayBuffer[Int]}
        val yRes = new ArrayBuffer[Int]
        val errRes = new ArrayBuffer[Int]
        val stepRes = new ArrayBuffer[Int]

        for (j <- 0 until digitSize + wUpdateDelay) {
          // X & W Input
          for (k <- 0 until 2) {
           val inX = if(j < digitSize) x(k)(j) else 0
           val inW = if(j < digitSize) w(k)(j) else 0
           poke(c.io.x(k), SignedDigit.toSignedDigit(inX)) 
           poke(c.io.inW(k), SignedDigit.toSignedDigit(inW)) 
          }

          // Y Input
          val inY = if(j < digitSize) y(j) else 0
          poke(c.io.y, SignedDigit.toSignedDigit(inY))

          // Step Input
          val inStep = if(j < digitSize) stepS(j) else 0
          poke(c.io.stepSize, SignedDigit.toSignedDigit(inStep))

          // Start Input
          val start = if (j == 0) 1 else 0
          poke(c.io.start, start)


          if ((j >= ybarDelay) & (j < ybarDelay+digitSize))
            yRes ++= SignedDigit.fromSignedDigit(peek(c.io.ybar).toInt)

          if ((j >= errDelay) & (j < errDelay+digitSize))
            errRes ++= SignedDigit.fromSignedDigit(peek(c.io.outErr).toInt)

          if ((j >= stepDelay) & (j < stepDelay+digitSize))
            stepRes ++= SignedDigit.fromSignedDigit(peek(c.io.outStep).toInt)

          if ((j >= wUpdateDelay) & (j < wUpdateDelay+digitSize)) {
            for (k <- 0 until 2) {
              wRes(k) ++= SignedDigit.fromSignedDigit(peek(c.io.outW(k)).toInt)
            }
          }

          step(1)
        }

        // Expected Results
        val (yBar, wUpdate, lmsErr, lmsStep) = update(dX, dY, dW, stepSize)

        // yBar
        println("yBar:")
        compare(yBar, SignedDigit.signedToDouble(yRes.toList))

        // lmsErr
        println("lmsErr:")
        compare(lmsErr, SignedDigit.signedToDouble(errRes.toList))

        // lmsStep
        println("lmsStep:")
        compare(lmsStep, SignedDigit.signedToDouble(stepRes.toList))

        // w
        for (j <- 0 until 2) {
          println("w(" + j.toString + "): ")
          compare(wUpdate(j), SignedDigit.signedToDouble(wRes(j).toList))
        }
      }
    }
    launchCppTester((c : MSDFLMSTest) => new MSDFLMSTests(c))
  }

  @Test def testMSDFLMS() {
    class MSDFLMSTest extends Module {
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

    class MSDFLMSTests(c : MSDFLMSTest) extends Tester(c) {

      def compare(expectedRes : Double, dRes : Double, count : Int) {
        if (count != 0) {
          val err = scala.math.abs(expectedRes - dRes)
          val correct = if (err > scala.math.pow(2, -8)) false else true
          expect(correct, "Expected: " + expectedRes.toString + "\tGot: " + dRes.toString + "\tError: " + err.toString)
        }
      }

      def update(x : List[Double], y : Double, w : List[Double], stepSize : Double) = {
        val yBar = (x, w).zipped.map(_*_).reduce(_+_)
        val err = y - yBar
        val step = stepSize*err
        val wUpdate = (x.map(step*_), w).zipped.map(_+_)
        (yBar, wUpdate, err, step)
      }

      val digitSize = 16
      val ybarDelay = 5
      val errDelay = 7
      val stepDelay = 10
      val wUpdateDelay = 15

      val stepSize : Double = 0.125

      val yBarRes = new scala.collection.mutable.Queue[Double]
      val wUpdateRes = new scala.collection.mutable.Queue[List[Double]]
      val lmsErrRes = new scala.collection.mutable.Queue[Double]
      val lmsStepRes = new scala.collection.mutable.Queue[Double]

      val yQ = new scala.collection.mutable.Queue[Double]
      val xQ = new scala.collection.mutable.Queue[List[Double]]

      // Counters
      var yBarCount = digitSize - ybarDelay
      var errCount = digitSize - errDelay
      var stepCount = digitSize - stepDelay 
      var wUpdateCount = digitSize - wUpdateDelay
      var compareCount = 0

      var updateW : Boolean = false
      var updateY : Boolean = false
      var updateErr : Boolean = false
      var updateStep : Boolean = false

      val w = List.fill(2){ArrayBuffer.fill(digitSize){0}}
      var prevW = List.fill(2){ArrayBuffer.fill(digitSize){0}}
      var yRes = new ArrayBuffer[Int]
      var errRes = new ArrayBuffer[Int]
      var stepRes = new ArrayBuffer[Int]

      for (i <- 0 until trials) {


        val dX : List[Double] = List.fill(2){r.nextDouble()/8}
        val dY : Double = dX.reduce(_*_)
        val x = dX.map(in => SignedDigit.doubleToSigned(in, digitSize))
        xQ.enqueue(x.map(in => SignedDigit.signedToDouble(in)))
        val y = SignedDigit.doubleToSigned(dY, digitSize)
        yQ.enqueue(SignedDigit.signedToDouble(y))
        val stepS = SignedDigit.doubleToSigned(stepSize, digitSize)


        // Expected Results
        if (i != 0) {
          val (yBar, wUpdate, lmsErr, lmsStep) = update(xQ.dequeue(), yQ.dequeue(), prevW.map(w1 => SignedDigit.signedToDouble(w1.toList)), stepSize)
          yBarRes.enqueue(yBar)
          wUpdateRes.enqueue(wUpdate)
          lmsErrRes.enqueue(lmsErr)
          lmsStepRes.enqueue(lmsStep)
        }

        for (j <- 0 until digitSize) {
          // X & W Input
          for (k <- 0 until 2) {
           val inX = if(j < digitSize) x(k)(j) else 0
           val inW = if(j < digitSize) w(k)(j) else 0
           poke(c.io.x(k), SignedDigit.toSignedDigit(inX)) 
           poke(c.io.inW(k), SignedDigit.toSignedDigit(inW)) 
          }

          // Y Input
          val inY = if(j < digitSize) y(j) else 0
          poke(c.io.y, SignedDigit.toSignedDigit(inY))

          // Step Input
          val inStep = if(j < digitSize) stepS(j) else 0
          poke(c.io.stepSize, SignedDigit.toSignedDigit(inStep))

          // Start Input
          val start = if (j == 0) 1 else 0
          poke(c.io.start, start)

          if (updateY) {
            yRes ++= SignedDigit.fromSignedDigit(peek(c.io.ybar).toInt)
          }

          if (updateErr) {
            errRes ++= SignedDigit.fromSignedDigit(peek(c.io.outErr).toInt)
          }

          if (updateStep){
            stepRes ++= SignedDigit.fromSignedDigit(peek(c.io.outStep).toInt)
          }

          if (updateW) {
            for (k <- 0 until 2) {
              w(k)(wUpdateCount) = SignedDigit.fromSignedDigit(peek(c.io.outW(k)).toInt)(0)
            }
          }

         yBarCount = if(yBarCount == digitSize -1) 0 else yBarCount + 1
         errCount = if(errCount == digitSize -1) 0 else errCount + 1
         stepCount = if(stepCount == digitSize -1) 0 else stepCount + 1
         wUpdateCount = if(wUpdateCount == digitSize -1) 0 else wUpdateCount + 1
         if(wUpdateCount == digitSize -1) {
          updateW = true
          prevW = w
        }
         if(yBarCount == digitSize -1) updateY = true
         if(errCount == digitSize -1) updateErr = true
         if(stepCount == digitSize -1) updateStep = true

         if (i != 0) {
           if (yBarCount == 0) {
            // yBar
            println("yBar:")
            compare(yBarRes.dequeue(), SignedDigit.signedToDouble(yRes.toList), compareCount)
            yRes = new ArrayBuffer[Int]
           }
           if (errCount == 0) {
            // lmsErr
            println("lmsErr:")
            compare(lmsErrRes.dequeue(), SignedDigit.signedToDouble(errRes.toList), compareCount)
            errRes = new ArrayBuffer[Int]
           }

           if (stepCount == 0) {
            // lmsStep
            println("lmsStep:")
            compare(lmsStepRes.dequeue(), SignedDigit.signedToDouble(stepRes.toList), compareCount)
            stepRes = new ArrayBuffer[Int]
           }

           if (wUpdateCount == 0) {
            // w
            val wRes = wUpdateRes.dequeue()
            for (j <- 0 until 2) {
              println("w(" + j.toString + "): ")
              compare(wRes(j), SignedDigit.signedToDouble(w(j).toList), compareCount)
            }
            compareCount += 1
           }
          }

          step(1)
        }


      }
    }
    launchCppTester((c : MSDFLMSTest) => new MSDFLMSTests(c))
  }
}