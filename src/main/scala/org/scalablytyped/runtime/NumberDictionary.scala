package org.scalablytyped.runtime

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSBracketAccess

trait NumberDictionary[+V] extends js.Object

object NumberDictionary {
  @js.native
  trait NumberDictionaryRaw[+V] extends js.Object {
    @JSBracketAccess
    def apply(index: Int): js.UndefOr[V] = js.native

    /** Set the element at the given index. */
    @JSBracketAccess
    def update[VV >: V](index: Int, value: VV): Unit = js.native
  }

  implicit def AsRaw[V](d: NumberDictionary[V]): NumberDictionaryRaw[V] =
    d.asInstanceOf[NumberDictionaryRaw[V]]
}
