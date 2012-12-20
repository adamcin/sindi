//      _____         ___  
//     / __(_)__  ___/ (_)
//    _\ \/ / _ \/ _  / /
//   /___/_/_//_/\_,_/_/
//
//  (c) 2012, Alois Cochard
//
//  http://aloiscochard.github.com/sindi
//

package sindi
package config

import com.typesafe.config._

import core._

trait Configurable {
  import sindi.core._

  implicit val profile: Binding[Option[String], Profile] =
    as[Profile].bind(Option(System.getProperty("profile")).orElse(defaultProfile))

  class Configuration(implicit binding: Binding[Option[String], Profile]) 
    extends config.DefaultConfiguration(
      binding.inject match {
        case Some(profile) => 
          ConfigFactory.load("application-" + profile + ".conf")
           .withFallback(ConfigFactory.load())
        case None =>
          ConfigFactory.load()
      }
     )

  trait Profile

  protected val defaultProfile: Option[String] = None
}

