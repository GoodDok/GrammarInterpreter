package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.{Div, Mult, Sub, Sum}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class MadeEvaluatorTest extends AnyFlatSpec with Matchers {

  behavior of "MadeEvaluator"

  def testEval(expression: Expression, initState: ProgramState = InitProgramState): Int = {
    val newState = MadeEvaluator.evaluate(initState)(expression)
    newState match {
      case Success(value) => value
      case _ => fail("does not match the expected pattern")
    }
  }

  it should "evaluate the expressions correctly" in {
    testEval(BinaryOperation(Number(2), Sum, Number(2))) shouldBe 4
    testEval(BinaryOperation(BinaryOperation(Number(5), Sub, Number(2)), Mult, Number(-2))) shouldBe -6
    testEval(BinaryOperation(BinaryOperation(Number(5), Div, Number(2)), Mult, Number(-2))) shouldBe -4
  }

  it should "work with variables from program state" in {
    testEval(BinaryOperation(Ident("a"), Mult, Ident("a")), ProgramState(Map("a" -> 3))) shouldBe 9
    testEval(BinaryOperation(Ident("k"), Sub, Ident("t")), ProgramState(Map("k" -> -2, "t" -> 15))) shouldBe -17
  }

  it should "provide useful exception messages" in {
    the[EvaluateException] thrownBy
      MadeEvaluator.evaluate(ProgramState(Map("b" -> 3)))(BinaryOperation(Ident("a"), Mult, Ident("a"))) should have message
      "Unable to find value for 'a' variable!"
  }

}
