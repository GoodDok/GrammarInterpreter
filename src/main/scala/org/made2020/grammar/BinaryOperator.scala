package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.MaybeResult

import scala.util.parsing.input.{NoPosition, Position}

sealed trait BinaryOperator {

  def operator2funcMaybe: (MaybeResult, MaybeResult) => MaybeResult = {
    case (Right(l), Right(r)) => operator2func(l, r)
    case (failure@Left(_), _) => failure
    case (_, failure@Left(_)) => failure
  }

  def operator2func: (Int, Int) => MaybeResult
}

object BinaryOperator {
  type MaybeResult = Either[ExecutionException, Int]


  case object Sum extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Right(a + b)
  }

  case object Sub extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Right(a - b)
  }

  case object Mult extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Right(a * b)
  }

  case object Div extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = {
      case (_, 0) => Left(ExecutionException("Division by 0 detected!"))
      case (a, b) => Right(a / b)
    }
  }

  case class ExecutionException(msg: String, pos: Position = NoPosition)

  def apply(op: String): BinaryOperator = op match {
    case "+" => Sum
    case "-" => Sub
    case "*" => Mult
    case "/" => Div
  }
}

