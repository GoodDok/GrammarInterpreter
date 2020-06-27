package org.made2020.grammar

import scala.util.matching.Regex
import scala.util.parsing.combinator._
import scala.util.parsing.input.{CharSequenceReader, Position}

object GrammarParsers extends JavaTokenParsers with PackratParsers {

  override protected val whiteSpace: Regex = """\h+""".r

  lazy val statements: Parser[List[Statement]] = statement ~ rep("\n" ~> statement) ^^ { case x ~ y => x :: y }

  lazy val statement: Parser[Statement] = ident ~ "=" ~ expr ^^ statementOp

  lazy val expr: Parser[Expression] = positioned(sum)

  lazy val sum: PackratParser[Expression] = positioned(sum ~ ("+" | "-") ~ product ^^ binaryOp | product)

  lazy val product: PackratParser[Expression] = positioned(product ~ ("*" | "/") ~ term ^^ binaryOp | term)

  lazy val term: Parser[Expression] = positioned("(" ~> expr <~ ")" | wholeNumber ^^ (n => Number(n.toInt)) | ident ^^ Ident)


  def binaryOp(p: Expression ~ String ~ Expression): BinaryOperation = p match {
    case l ~ op ~ r => BinaryOperation(l, BinaryOperator(op), r)
  }

  def statementOp(p: String ~ String ~ Expression): Statement = p match {
    case name ~ "=" ~ e => Statement(Ident(name), e)
  }

  def apply(code: String): Either[ParseException, List[Statement]] =
    parseAll(statements, new PackratReader(new CharSequenceReader(code))) match {
      case Success(result, _) => Right(result)
      case f: Failure => Left(ParseException(f.msg, f.next.pos))
    }

  case class ParseException(msg: String, pos: Position)

}
