package com.github.sguzman.scala.uber

import java.net.SocketTimeoutException
import java.util.UUID

import com.github.sguzman.scala.uber.args.{Cmd, CmdConfig}
import com.github.sguzman.scala.uber.typesafe.data.all_data.AllDataStatement
import com.github.sguzman.scala.uber.typesafe.data.statement.Statement
import com.github.sguzman.scala.uber.typesafe.data.trip.Trip
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpResponse}

object Main {
  implicit final class ArgString(a: Array[String]) {
    def cookie = CmdConfig().parse(a, Cmd()).get.cookie

    def all =
      Http("https://partners.uber.com/p3/money/statements/all_data/")
        .header("Cookie", a.cookie)
        .asString.body.all

    def statement(uuid: UUID): HttpResponse[String] =
      util.Try(Http(s"https://partners.uber.com/p3/money/statements/view/${uuid.toString}")
        .header("Cookie", a.cookie)
        .asString) match {
        case Success(v) => v
        case Failure(e) => e match {
          case _: SocketTimeoutException => statement(uuid)
        }
      }

    def trip(uuid: UUID): HttpResponse[String] =
      util.Try(Http(s"https://partners.uber.com/p3/money/trips/trip_data/${uuid.toString}")
        .header("Cookie", a.cookie)
        .asString) match {
        case Success(v) =>
          Console.err.println(uuid)
          v
        case Failure(e) => e match {
          case _: SocketTimeoutException => trip(uuid)
        }
      }
  }

  implicit final class StrWrap(str: String) {
    def all = decode[Array[AllDataStatement]](str).right.get
    def statement = decode[Statement](str).right.get
    def trip = decode[Trip](str).right.get
  }

  def main(args: Array[String]): Unit =
    println(args.all.par.flatMap{t =>
      args.statement(t.uuid)
        .body
        .statement
        .body.driver.trip_earnings.trips.keys.toList
        .map(args.trip(_).body.trip)
    }.toList.asJson.noSpaces)
}
