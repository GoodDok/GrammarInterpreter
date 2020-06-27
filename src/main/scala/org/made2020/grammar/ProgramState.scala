package org.made2020.grammar

case class ProgramState(variables: Map[String, Int]) {
  override def toString: String = variables.map { case (k, v) => s"$k = $v" }.mkString("\n")
}

object InitProgramState extends ProgramState(Map())
