package org.made2020.grammar

import scala.util.parsing.input.Positional

sealed trait Expression extends Positional

case class Number(value: Int) extends Expression

case class Id(name: String) extends Expression

case class BinaryOperation(left: Expression, operator: BinaryOperator, right: Expression) extends Expression

case class Statement(variable: Id, expression: Expression)

