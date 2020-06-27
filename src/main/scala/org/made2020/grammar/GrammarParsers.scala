package org.made2020.grammar

import scala.util.Try
import scala.util.matching.Regex
import scala.util.parsing.combinator._
import scala.util.parsing.input.CharSequenceReader

object GrammarParsers extends JavaTokenParsers with PackratParsers {

  // work with multiline code, leave only "horizontal" whitespaces
  override protected val whiteSpace: Regex = """\h+""".r

  lazy val statements: Parser[List[Statement]] = statement ~ rep("\n" ~> statement) ^^ { case x ~ y => x :: y }

  lazy val statement: Parser[Statement] = ident ~ "=" ~ expr ^^ statementOp

  lazy val expr: Parser[Expression] = positioned(sum)

  lazy val sum: PackratParser[Expression] = sum ~ ("+" | "-") ~ product ^^ binaryOp | product

  lazy val product: PackratParser[Expression] = product ~ ("*" | "/") ~ term ^^ binaryOp | term

  lazy val term: Parser[Expression] = "(" ~> expr <~ ")" | ident ^^ Id |
    wholeNumber ^? {
      case n if n.length < 10 => Number(n.toInt)
      case n if n.charAt(0) == '-' && n.length == 10 => Number(n.toInt)
    }

  def binaryOp(p: Expression ~ String ~ Expression): BinaryOperation = p match {
    case l ~ op ~ r => BinaryOperation(l, BinaryOperator(op), r)
  }

  def statementOp(p: String ~ String ~ Expression): Statement = p match {
    case name ~ "=" ~ e => Statement(Id(name), e)
  }

  def apply(code: String): Try[List[Statement]] =
    parseAll(statements, new PackratReader(new CharSequenceReader(code))) match {
      case Success(result, _) => util.Success(result)
      case f: Failure => util.Failure(ParseException(f.msg, f.next.pos))
    }

}
