package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.{Div, Mult, Sub, Sum}
import org.scalatest.TryValues._
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
    testEval(BinaryOperation(Id("a"), Mult, Id("a")), ProgramState(Map("a" -> 3))) shouldBe 9
    testEval(BinaryOperation(Id("k"), Sub, Id("t")), ProgramState(Map("k" -> -2, "t" -> 15))) shouldBe -17
  }

  it should "provide useful exception messages" in {
    MadeEvaluator.evaluate(ProgramState(Map("b" -> 3)))(BinaryOperation(Id("a"), Mult, Id("a")))
      .failure.exception.getMessage should include("Unable to find value for 'a' variable!")

    MadeEvaluator.evaluate(InitProgramState)(BinaryOperation(Number(4), Div, BinaryOperation(Number(3), Sub, Number(3))))
      .failure.exception.getMessage should include("Division by 0 detected!")
  }

  it should "update program state on transform" in {
    MadeEvaluator.transform(Success(InitProgramState), Statement(Id("testVar"), BinaryOperation(Number(1), Sub, Number(2))))
      .success.value.variables should contain theSameElementsAs (Map("testVar" -> -1))
  }

  it should "overwrite old values" in {
    MadeEvaluator.transform(Success(ProgramState(Map("testVar" -> 5))), Statement(Id("testVar"), BinaryOperation(Number(1), Sub, Number(2))))
      .success.value.variables should contain theSameElementsAs (Map("testVar" -> -1))
  }
}
