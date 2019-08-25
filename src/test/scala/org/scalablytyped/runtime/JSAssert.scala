package org.scalablytyped.runtime

import org.junit.Assert._

import scala.scalajs.js

object JSAssert {
  def assertJSUndefined(obj: Any): Unit =
    assertTrue(s"Expected <$obj> to be <undefined>.", js.isUndefined(obj))
}
