package org.scalablytyped.runtime

import scala.scalajs.js

object TypeTests {
  trait Test1 extends js.Object {
    val a: NullUndefOr[Int]
  }
  trait Test2 extends Test1 {
    val a: NullOr[Int]
  }
  trait Test3 extends Test1 {
    val a: UndefOr[Int]
  }
  trait Test4 extends Test2 with Test3 {
    val a: Required[Int]
  }

  new Test1 {
    override val a: NullUndefOr[Int] = null
  }
  new Test1 {
    override val a = 1
  }
  new Test2 {
    override val a = null
  }
  new Test2 {
    override val a = 1
  }
  new Test3 {
    override val a = UndefOr.fromJsUndefOr(js.undefined)
  }
  new Test3 {
    override val a = 1
  }
//  new Test4 {
//    override val a = js.undefined
//  }
  new Test4 {
    override val a = 1
  }
}
