package org.made2020.grammar

import org.made2020.grammar.BinaryOperator.MaybeResult

import scala.util.{Failure, Success, Try}

sealed trait BinaryOperator {

  def operator2funcMaybe: (MaybeResult, MaybeResult) => MaybeResult = {
    case (Success(l), Success(r)) => operator2func(l, r)
    case (failure@Failure(_), _) => failure
    case (_, failure@Failure(_)) => failure
  }

  def operator2func: (Int, Int) => MaybeResult
}

object BinaryOperator {
  type MaybeResult = Try[Int]


  case object Sum extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Success(a + b)
  }

  case object Sub extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Success(a - b)
  }

  case object Mult extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = (a, b) => Success(a * b)
  }

  case object Div extends BinaryOperator {
    override def operator2func: (Int, Int) => MaybeResult = {
      case (_, 0) => Failure(EvaluateException("Division by 0 detected!"))
      case (a, b) => Success(a / b)
    }
  }

  def apply(op: String): BinaryOperator = op match {
    case "+" => Sum
    case "-" => Sub
    case "*" => Mult
    case "/" => Div
  }
}

