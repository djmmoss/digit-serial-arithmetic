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

class MSDFPrimitivesSuite extends TestSuite {

  val trials = 10
  val r = scala.util.Random
  val totalWidth = 16

  val doTrace = false
  val digit = 4
  val n = totalWidth/digit

 @Test def testMSDFMulSEL() {
    class MSDFMulSELTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 3)
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFMul.SEL(io.a)
    }

    class MSDFMulSELTests(c : MSDFMulSELTest) extends Tester(c, isTrace=doTrace) {
      val sel = List(
        UInt("b000").litValue(), 
        UInt("b001").litValue(), 
        UInt("b010").litValue(), 
        UInt("b011").litValue(), 
        UInt("b100").litValue(),
        UInt("b101").litValue(),
        UInt("b110").litValue(),
        UInt("b111").litValue()
        )
      val selAns = List(
        UInt("b0").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b1").litValue(),
        UInt("b1").litValue(),
        UInt("b1").litValue(),
        UInt("b0").litValue()
        )

      for (i <- 0 until sel.length) {
        poke(c.io.a, sel(i))
        expect(c.io.c, selAns(i))
      }
    }

    launchCppTester((c : MSDFMulSELTest) => new MSDFMulSELTests(c))
  }

  // @Test def testMSDFExpSEL() {
  //   class MSDFExpSELTest extends Module {
  //     val io = new Bundle {
  //       val a = UInt(INPUT, 3)
  //       val c = UInt(OUTPUT, 2)
  //     }
  //     io.c := MSDFExp.SEL(io.a)
  //   }

  //   class MSDFExpSELTests(c : MSDFExpSELTest) extends Tester(c) {
  //     val sel = List(
  //       UInt("b000").litValue(), 
  //       UInt("b001").litValue(), 
  //       UInt("b010").litValue(), 
  //       UInt("b011").litValue(), 
  //       UInt("b100").litValue(),
  //       UInt("b101").litValue(),
  //       UInt("b110").litValue(),
  //       UInt("b111").litValue()
  //       )
  //     val selAns = List(
  //       UInt("b0").litValue(), 
  //       UInt("b0").litValue(), 
  //       UInt("b10").litValue(), 
  //       UInt("b10").litValue(), 
  //       UInt("b1").litValue(),
  //       UInt("b0").litValue(),
  //       UInt("b0").litValue(),
  //       UInt("b0").litValue()
  //       )

  //     for (i <- 0 until sel.length) {
  //       poke(c.io.a, sel(i))
  //       expect(c.io.c, selAns(i))
  //     }
  //   }

  //   launchCppTester((c : MSDFExpSELTest) => new MSDFExpSELTests(c))
  // }

  @Test def testMSDFDivSEL() {
    class MSDFDivSELTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 5)
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFDiv.SEL(io.a)
    }

    class MSDFDivSELTests(c : MSDFDivSELTest) extends Tester(c, isTrace=doTrace) {
      val sel = List(
        UInt("b00000").litValue(), 
        UInt("b00001").litValue(), 
        UInt("b00010").litValue(), 
        UInt("b00011").litValue(), 
        UInt("b00100").litValue(),
        UInt("b00101").litValue(),
        UInt("b00110").litValue(),
        UInt("b00111").litValue(),
        UInt("b01000").litValue(), 
        UInt("b01001").litValue(), 
        UInt("b01010").litValue(), 
        UInt("b01011").litValue(), 
        UInt("b01100").litValue(),
        UInt("b01101").litValue(),
        UInt("b01110").litValue(),
        UInt("b01111").litValue(),
        UInt("b10000").litValue(), 
        UInt("b10001").litValue(), 
        UInt("b10010").litValue(), 
        UInt("b10011").litValue(), 
        UInt("b10100").litValue(),
        UInt("b10101").litValue(),
        UInt("b10110").litValue(),
        UInt("b10111").litValue(),
        UInt("b11000").litValue(), 
        UInt("b11001").litValue(), 
        UInt("b11010").litValue(), 
        UInt("b11011").litValue(), 
        UInt("b11100").litValue(),
        UInt("b11101").litValue(),
        UInt("b11110").litValue(),
        UInt("b11111").litValue()
        )
      val selAns = List(
        UInt("b00").litValue(), 
        UInt("b00").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b10").litValue(), 
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b01").litValue(),
        UInt("b00").litValue(),
        UInt("b00").litValue()
        )

      for (i <- 0 until sel.length) {
        poke(c.io.a, sel(i))
        expect(c.io.c, selAns(i))
      }
    }

    launchCppTester((c : MSDFDivSELTest) => new MSDFDivSELTests(c))
  }

  @Test def testMSDFDivU() {
    class MSDFDivUTest extends Module {
      val io = new Bundle {
        val x = UInt(INPUT, 2)
        val d = UInt(INPUT, 2)
        val c = UInt(OUTPUT, 6)
      }
      io.c := MSDFDiv.U(io.x, io.d, UInt("b0"))
    }

    class MSDFDivUTests(c : MSDFDivUTest) extends Tester(c, isTrace=doTrace) {
      val u = List(
        List(UInt("b10").litValue(), UInt("b10").litValue()),
        List(UInt("b10").litValue(), UInt("b00").litValue()),
        List(UInt("b10").litValue(), UInt("b01").litValue()),
        List(UInt("b10").litValue(), UInt("b11").litValue()),
        List(UInt("b00").litValue(), UInt("b10").litValue()),
        List(UInt("b00").litValue(), UInt("b00").litValue()),
        List(UInt("b00").litValue(), UInt("b01").litValue()),
        List(UInt("b00").litValue(), UInt("b11").litValue()),
        List(UInt("b01").litValue(), UInt("b10").litValue()),
        List(UInt("b01").litValue(), UInt("b00").litValue()),
        List(UInt("b01").litValue(), UInt("b01").litValue()),
        List(UInt("b01").litValue(), UInt("b11").litValue()),
        List(UInt("b11").litValue(), UInt("b10").litValue()),
        List(UInt("b11").litValue(), UInt("b00").litValue()),
        List(UInt("b11").litValue(), UInt("b01").litValue()),
        List(UInt("b11").litValue(), UInt("b11").litValue())
        )
      val uAns = List(
        UInt("b000000").litValue(), 
        UInt("b000001").litValue(), 
        UInt("b000001").litValue(), 
        UInt("b000001").litValue(), 
        UInt("b111111").litValue(), 
        UInt("b000000").litValue(), 
        UInt("b000000").litValue(), 
        UInt("b000000").litValue(), 
        UInt("b111110").litValue(), 
        UInt("b111111").litValue(), 
        UInt("b111111").litValue(), 
        UInt("b111111").litValue(), 
        UInt("b111111").litValue(), 
        UInt("b000000").litValue(), 
        UInt("b000000").litValue(), 
        UInt("b000000").litValue() 
        )

      for (i <- 0 until u.length) {
        poke(c.io.x, u(i)(0))
        poke(c.io.d, u(i)(1))
        expect(c.io.c, uAns(i))
      }
    }

    launchCppTester((c : MSDFDivUTest) => new MSDFDivUTests(c))
  }

  //@Test def testMSDFDiv() {
    //class MSDFDivTest extends Module {
      //val io = new Bundle {
        //val x = UInt(INPUT, 2)
        //val d = UInt(INPUT, 2)
        //val start = Bool(INPUT)
        //val c = UInt(OUTPUT, 14)
      //}
      //io.c := MSDFDiv(io.x, io.d, io.start)
    //}

    //class MSDFDivTests(c : MSDFDivTest) extends Tester(c) {

      //for (i <- 0 until trials) {
        //var dX = r.nextDouble()/2
        //var dD = r.nextDouble()/2
        //while (dX/dD > 0.5) {
          //dX = r.nextDouble()/2
          //dD = r.nextDouble()/2
        //}
        //val x = SignedDigit.doubleToSigned(dX, 8)
        //val d = SignedDigit.doubleToSigned(dD, 8)
        //val res = new ArrayBuffer[Int]
        //for (j <- 0 until x.length + 4) {
          //val inX = if(j < x.length) x(j) else 0
          //val inD = if(j < d.length) d(j) else 0
          //poke(c.io.x, SignedDigit.toSignedDigit(inX))
          //poke(c.io.d, SignedDigit.toSignedDigit(inD))
          //val start = if (j == 0) BigInt(1) else BigInt(0)
          //poke(c.io.start, start)
          //peek(c.io.c)
          //if (j >= 4)
            //res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          //step(1)
        //}
        //val expectedRes = SignedDigit.signedToDouble(x)/SignedDigit.signedToDouble(d)
        //val dRes = SignedDigit.signedToDouble(res.toList)
        //val err = scala.math.abs(expectedRes - dRes)
        //val correct = if (err > scala.math.pow(2, -8)) false else true
        //expect(correct, "Expected: " + expectedRes.toString + "(" + (dX*dD).toString + ")" + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      //}
    //}
    //launchCppTester((c : MSDFDivTest) => new MSDFDivTests(c))
  //}

  @Test def testSDOnlineConversion() {
    class SDOnlineConversionTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val c = UInt(OUTPUT, 14)
      }
      io.c := SDOnlineConversion(io.a, Bool(false))
    }

    class SDOnlineConversionTests(c : SDOnlineConversionTest) extends Tester(c, isTrace=true) {
      val q = List(1, 1, 0, 1, -1, 0, 0, -1, 1, 0, 1, 0)
      val qAns = List(
        UInt("b100000000000").litValue(), 
        UInt("b110000000000").litValue(), 
        UInt("b110000000000").litValue(), 
        UInt("b110100000000").litValue(), 
        UInt("b110010000000").litValue(),
        UInt("b110010000000").litValue(),
        UInt("b110010000000").litValue(),
        UInt("b110001110000").litValue(),
        UInt("b110001111000").litValue(),
        UInt("b110001111000").litValue(),
        UInt("b110001111010").litValue(),
        UInt("b110001111010").litValue())

      for (i <- 0 until q.length) {
        poke(c.io.a, SignedDigit.toSignedDigit(q(i)))
        expect(c.io.c, qAns(i))
        step(1)
      }
    }

    launchCppTester((c : SDOnlineConversionTest) => new SDOnlineConversionTests(c))
  }
  
  @Test def testRevBits() {
    class RevBitsTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 6)
        val c = UInt(OUTPUT, 6)
      }
      io.c := RevBits(io.a)
    }

    class RevBitsTests(c : RevBitsTest) extends Tester(c, isTrace=true) {

        val in  = Integer.parseInt("101001", 2)
        val exp = Integer.parseInt("100101", 2)
        poke(c.io.a, in)
        expect(c.io.c, exp)
    }

    launchCppTester((c : RevBitsTest) => new RevBitsTests(c))
  }
  
  //@Test def testRevBitsMask() {
    //class RevBitsMaskTest extends Module {
      //val io = new Bundle {
        //val a = UInt(INPUT, 6)
        //val mask = UInt(INPUT, 6)
        //val c = UInt(OUTPUT, 6)
      //}
      //io.c := RevBits(io.a, io.mask)
    //}

    //class RevBitsMaskTests(c : RevBitsMaskTest) extends Tester(c, isTrace=true) {

        //val in  = Integer.parseInt("101001", 2)
        //val mask = Integer.parseInt("001000", 2)
        //val exp = Integer.parseInt("010101", 2)
        //poke(c.io.a, in)
        //poke(c.io.mask, mask)
        //expect(c.io.c, exp)
    //}

    //launchCppTester((c : RevBitsMaskTest) => new RevBitsMaskTests(c))
  //}

  @Test def testMSDFAdd() {
    class MSDFAddTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFAdd(io.a, io.b, io.start)
    }

    class MSDFAddTests(c : MSDFAddTest) extends Tester(c, isTrace=doTrace) {
      val digitNumber = 1
      for (i <- 0 until trials) {
        val dA = r.nextDouble()/2
        val dB = r.nextDouble()/2
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        for (j <- 0 until 8 + 2*digitNumber by digitNumber) {
          val inA = if(j < a.length) a.slice(j, j+digitNumber) else List.fill(digitNumber){0}
          val inB = if(j < b.length) b.slice(j, j+digitNumber) else List.fill(digitNumber){0}
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          poke(c.io.b, SignedDigit.toSignedDigit(inB))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          peek(c.io.c)
          poke(c.io.start, start)
          if (j >= 2*digitNumber)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt, digitNumber)
          step(1)
        }
        val expectedRes = SignedDigit.signedToDouble(a)+SignedDigit.signedToDouble(b)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "(" + (dA+dB).toString + ")" + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }

    launchCppTester((c : MSDFAddTest) => new MSDFAddTests(c))
  }

  @Test def testMSDFSub() {
    class MSDFSubTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFSub(io.a, io.b, io.start)
    }

    class MSDFSubTests(c : MSDFSubTest) extends Tester(c, isTrace=doTrace) {
      val digitNumber = 1
      for (i <- 0 until trials) {
        val dA = r.nextDouble()/2
        val dB = r.nextDouble()/2
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        for (j <- 0 until 8 + 2*digitNumber by digitNumber) {
          val inA = if(j < a.length) a.slice(j, j+digitNumber) else List.fill(digitNumber){0}
          val inB = if(j < b.length) b.slice(j, j+digitNumber) else List.fill(digitNumber){0}
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          poke(c.io.b, SignedDigit.toSignedDigit(inB))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          peek(c.io.c)
          poke(c.io.start, start)
          if (j >= 2*digitNumber)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt, digitNumber)
          step(1)
        }
        val expectedRes = SignedDigit.signedToDouble(a)-SignedDigit.signedToDouble(b)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "(" + (dA-dB).toString + ")" + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }

    launchCppTester((c : MSDFSubTest) => new MSDFSubTests(c))
  }

  @Test def testMSDFMul() {
    class MSDFMulTest extends Module {
      val io = new Bundle {
        val a = UInt(INPUT, 2)
        val b = UInt(INPUT, 2)
        val start = Bool(INPUT)
        val c = UInt(OUTPUT, 2)
      }
      io.c := MSDFMul(io.a, io.b, io.start)
    }

    class MSDFMulTests(c : MSDFMulTest) extends Tester(c, isTrace=false) {

      for (i <- 0 until trials) {
        val dA = r.nextDouble()/2
        val dB = r.nextDouble()/2
        val a = SignedDigit.doubleToSigned(dA, 8)
        val b = SignedDigit.doubleToSigned(dB, 8)
        val res = new ArrayBuffer[Int]
        for (j <- 0 until a.length + 3) {
          val inA = if(j < a.length) a(j) else 0
          val inB = if(j < b.length) b(j) else 0
          poke(c.io.a, SignedDigit.toSignedDigit(inA))
          poke(c.io.b, SignedDigit.toSignedDigit(inB))
          val start = if (j == 0) BigInt(1) else BigInt(0)
          poke(c.io.start, start)
          peek(c.io.c)
          if (j >= 3)
            res ++= SignedDigit.fromSignedDigit(peek(c.io.c).toInt)
          step(1)
        }
        val expectedRes = SignedDigit.signedToDouble(a)*SignedDigit.signedToDouble(b)
        val dRes = SignedDigit.signedToDouble(res.toList)
        val err = scala.math.abs(expectedRes - dRes)
        val correct = if (err > scala.math.pow(2, -8)) false else true
        expect(correct, "Expected: " + expectedRes.toString + "(" + (dA*dB).toString + ")" + "\tGot: " + dRes.toString + "\tError: " + err.toString)
      }
    }
    launchCppTester((c : MSDFMulTest) => new MSDFMulTests(c))
  }

}
