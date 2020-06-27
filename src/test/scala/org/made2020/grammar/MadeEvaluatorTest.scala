package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.Sum
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MadeEvaluatorTest extends AnyFlatSpec with Matchers {

  behavior of "MadeEvaluator"

  it should "evaluate the expressions correctly" in {
    val initState = ProgramState(Map())
    val newState = MadeEvaluator.transform(Right(initState), Statement(Ident("test"), BinaryOperation(Number(2), Sum, Number(2))))
    newState should matchPattern { case Right(ProgramState(vars)) => }
    newState match {
      case Right(ProgramState(vars)) => vars should contain theSameElementsAs Map("test" -> 4)
    }
  }

}
