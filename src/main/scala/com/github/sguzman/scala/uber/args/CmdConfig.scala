package com.github.sguzman.scala.uber.args

case class CmdConfig(title: String = "GoldMiner") extends scopt.OptionParser[Cmd](title) {
  head(title, "1.0")

  opt[String]('c', "cookie")
    .text("Cookie")
    .required()
    .valueName("<cookie>")
    .minOccurs(1)
    .maxOccurs(1)
    .action((x, c) => c.copy(cookie = x))

  help("help")
    .text("this")
}