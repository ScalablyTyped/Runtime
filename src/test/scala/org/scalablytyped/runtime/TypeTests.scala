package org.scalablytyped.runtime

import scala.scalajs.js

object TypeTests {
  trait Test1 extends js.Object {
    val a: NullUndefOr[Int] = js.undefined.asInstanceOf[NullUndefOr[Int]] // Error:(7, 34) Members of Scala.js-defined JS traits must either be abstract, or their right-hand-side must be `js.undefined`.
  }
  trait Test2 extends Test1 {
    override val a: NullOr[Int]
  }
  trait Test3 extends Test1 {
    override val a: UndefOr[Int] = js.undefined.asInstanceOf[UndefOr[Int]] // Error:(13, 34) Members of Scala.js-defined JS traits must either be abstract, or their right-hand-side must be `js.undefined`.
  }
  trait Test4 extends Test2 with Test3 {
    override val a: Required[Int]
  }

  new Test1 {}
  new Test1 {
    override val a = 1
  }
  new Test1 {
    override val a = NullUndefOr(1)
  }

  new Test2 {
    override val a = null
  }
  new Test2 {
    override val a = 1
  }
  new Test2 {
    override val a = NullOr(1)
  }

  new Test3 {}
  new Test3 {
    override val a = 1
  }
  new Test3 {
    override val a = UndefOr(1)
  }

  new Test4 {}
  new Test4 {
    override val a = 1
  }
  new Test4 {
    override val a = Required(1)
  }
}
