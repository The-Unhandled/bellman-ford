#!/usr/bin/env -S scala-cli shebang

//> using scala "3.3.3"
//> using dep "com.softwaremill.sttp.client3::core:3.9.8"
//> using dep "com.softwaremill.sttp.client3::circe:3.9.8"
//> using dep "io.circe::circe-core:0.14.10"
//> using dep "io.circe::circe-generic:0.14.10"
//> using dep "io.circe::circe-parser:0.14.10"
//> using test.dep "org.scalatest::scalatest:3.2.19"
//> using test.resourceDir src/test/resources

import swissborg.challenge.ChallengeApp

/* Borger, feel free to let your imagination shine but do not change this snippet. */
val url: String = args.length match {
  case 0 => "https://api.swissborg.io/v1/challenge/rates"
  case _ => args(0)
}

/* Add your stuff, be Awesome! */
ChallengeApp.run(url)
