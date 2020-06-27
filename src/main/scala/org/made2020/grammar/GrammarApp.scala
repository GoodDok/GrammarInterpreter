package org.made2020.grammar

import org.made2020.grammar.MadeEvaluator.MaybeState

import scala.io.Source

object GrammarApp {
  def main(args: Array[String]): Unit = {
    val inputText: String = Source.fromResource("code.txt").getLines.mkString("\n")
    val parsedStatements = GrammarParsers(inputText)
    val finalState = parsedStatements.flatMap {
      _.foldLeft(Right(ProgramState(Map())): MaybeState)(MadeEvaluator.transform)
    }
    finalState match {
      case Right(value) => println(value)
      case Left(failure) => println(s"Execution failed: $failure")
    }
  }
}
