package org.made2020.grammar

import scala.util.parsing.input.{NoPosition, Position}

class GrammarException(msg: String, pos: Position) extends Exception(msg) {
  override def toString: String = s"$msg (${pos.line + 1}:${pos.column + 1})"
}

case class ParseException(msg: String, pos: Position = NoPosition) extends GrammarException(msg, pos) {
  override def toString: String = "Parsing failed: " + super.toString
}

case class EvaluateException(msg: String, pos: Position = NoPosition) extends GrammarException(msg, pos) {
  override def toString: String = "Evaluation failed: " + super.toString
}