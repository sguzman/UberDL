package com.github.sguzman.scala.uber.typesafe.data.statement

case class Request(
                  uri: URI,
                  method: String,
                  headers: Map[String,String]
                  )
