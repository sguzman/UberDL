package com.github.sguzman.scala.uber.typesafe.verify

case class ProfileNav(
                     documents: Documents,
                     profile: ProfileProfile,
                     vehicles: Vehicles,
                     primarySideNav: Option[Int],
                     url: Option[String]
                     )
