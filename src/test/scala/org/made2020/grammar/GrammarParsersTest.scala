package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.{Div, Sub, Sum}
import org.scalatest.TryValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class GrammarParsersTest extends AnyFlatSpec with Matchers {
  behavior of "GrammarParsers"

  it should "parse statements correctly" in {
    GrammarParsers("a =-115").success.value shouldBe List(Statement(Id("a"), Number(-115)))
    GrammarParsers("bb = 15 \n a = (bb - 134) / 15").success.value shouldBe List(
      Statement(Id("bb"), Number(15)),
      Statement(Id("a"), BinaryOperation(BinaryOperation(Id("bb"), Sub, Number(134)), Div, Number(15)))
    )
    GrammarParsers("c = a - (b - (c - d))").success.value shouldBe List(
      Statement(Id("c"), BinaryOperation(Id("a"), Sub, BinaryOperation(Id("b"), Sub, BinaryOperation(Id("c"), Sub, Id("d")))))
    )
    GrammarParsers("res = a + b / c").success.value shouldBe List(
      Statement(Id("res"), BinaryOperation(Id("a"), Sum, BinaryOperation(Id("b"), Div, Id("c"))))
    )
  }

  it should "fail on invalid input" in {
    GrammarParsers("1res = a + b / c").failure.exception shouldBe a[ParseException]
    GrammarParsers("res = 1000000000000000000000 + b / c").failure.exception shouldBe a[ParseException]
  }

}
