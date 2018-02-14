package com.github.sguzman.scala.uber

import com.github.sguzman.scala.uber.args.{Cmd, CmdConfig}
import com.github.sguzman.scala.uber.typesafe.data.all_data.AllDataStatement
import io.circe.generic.auto._
import io.circe.parser.decode

import scalaj.http.Http

object Main {
  implicit final class ArgString(a: Array[String]) {
    def cookie = CmdConfig().parse(a, Cmd()).get.cookie
    def all = Http("https://partners.uber.com/p3/money/statements/all_data/")
      .header("Cookie", a.cookie)
      .asString
  }

  implicit final class StrWrap(str: String) {
    def all = decode[Array[AllDataStatement]](str)
  }

  def main(args: Array[String]): Unit =
    args.all.body.all.right.get.par foreach println
}
