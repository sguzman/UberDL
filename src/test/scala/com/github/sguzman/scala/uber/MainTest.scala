package com.github.sguzman.scala.uber

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class MainTest extends FlatSpec {

  behavior of "MainTest"

  it should "main" in {
    val cookie = System.getenv("COOKIE")
    cookie should not be null

    Main.main(Array("--cookie", cookie))
  }

}
