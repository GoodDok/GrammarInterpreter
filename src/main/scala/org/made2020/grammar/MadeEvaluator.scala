package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.MaybeResult

import scala.util.{Failure, Success, Try}


object MadeEvaluator {

  type MaybeState = Try[ProgramState]

  def transform(programState: MaybeState, statement: Statement): MaybeState =
    programState.flatMap(state =>
      evaluate(state)(statement.expression)
        .map(value => ProgramState(state.variables + (statement.variable.name -> value)))
    )

  def evaluate(programState: ProgramState)(expression: Expression): MaybeResult = {
    def eval: Expression => MaybeResult = evaluate(programState)

    val result = expression match {
      case Number(value) => Success(value)
      case Id(name) => programState.variables.get(name) match {
        case Some(value) => Success(value)
        case None => Failure(EvaluateException(s"Unable to find value for '$name' variable!"))
      }
      case BinaryOperation(left, op, right) => op.operator2funcMaybe(eval(left), eval(right))
    }
    result match {
      case Failure(EvaluateException(msg, _)) => Failure(EvaluateException(msg, expression.pos))
      case value => value
    }
  }
}
