package org.made2020.grammar

import org.made2020.grammar.MadeEvaluator.MaybeState

import scala.io.Source
import scala.util.{Failure, Success}

object GrammarApp {
  def main(args: Array[String]): Unit = {
    val inputText: String = Source.fromResource("code.txt").getLines.mkString("\n")
    val parsedStatements = GrammarParsers(inputText)
    val finalState = parsedStatements.flatMap {
      _.foldLeft(Success(InitProgramState): MaybeState)(MadeEvaluator.transform)
    }
    finalState match {
      case Success(value) => println(value)
      case Failure(failure) => println(failure)
    }
  }
}
