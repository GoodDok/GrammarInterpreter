package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.{ExecutionException, MaybeResult}

case class ProgramState(variables: Map[String, Int])

object MadeEvaluator {

  type MaybeState = Either[ExecutionException, ProgramState]

  def transform(programState: MaybeState, statement: Statement): MaybeState =
    programState.flatMap(state =>
      evaluate(state)(statement.expression)
        .map(value => ProgramState(state.variables + (statement.variable.name -> value)))
    )

  def evaluate(programState: ProgramState)(expression: Expression): MaybeResult = {
    def eval: Expression => MaybeResult = evaluate(programState)

    val result = expression match {
      case Number(value) => Right(value)
      case Ident(name) => programState.variables.get(name) match {
        case Some(value) => Right(value)
        case None => Left(ExecutionException(s"Unable to find value for '$name' variable!"))
      }
      case BinaryOperation(left, op, right) => op.operator2funcMaybe(eval(left), eval(right))
    }
    result match {
      case Left(ExecutionException(msg, _)) => Left(ExecutionException(msg, expression.pos))
      case value => value
    }
  }
}
