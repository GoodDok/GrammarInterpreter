package org.made2020.grammar

import org.made2020.grammar.MadeEvaluator.MaybeState

import scala.io.Source
import scala.util.{Failure, Success}

object GrammarApp {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      throw new IllegalArgumentException("The path to the code file should be provided as an argument!")
    }
    val inputText: String = readFile(args(0))

    val parsedStatements = GrammarParsers(inputText)
    val finalState = parsedStatements.flatMap {
      _.foldLeft(Success(InitProgramState): MaybeState)(MadeEvaluator.transform)
    }
    finalState match {
      case Success(value) => println(value)
      case Failure(failure) => println(failure)
    }
  }

  private def readFile(filePath: String): String = {
    val inputSource = Source.fromFile(filePath)
    try {
      inputSource.getLines.mkString("\n")
    } finally {
      inputSource.close()
    }
  }
}
