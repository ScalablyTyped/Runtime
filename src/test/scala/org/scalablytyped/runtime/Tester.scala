package org.scalablytyped.runtime

import scala.scalajs.js

class T0() extends StObject

class T1[T](val t: T) extends T0

class T2[T, U](override val t: T, val u: U) extends T1[T](t)

object Tester {
  def tester(t1s: T1[js.Any]*) = ()

  tester(new T2(1, "").unsafeCast2)
}
