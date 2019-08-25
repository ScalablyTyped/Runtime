package org.scalablytyped.runtime

import scala.language.{higherKinds, implicitConversions}
import scala.scalajs.js
import scala.scalajs.js.|
import scala.scalajs.js.|.Evidence

/**
  * The value, `null` or `undefined`
  */
sealed trait NullUndefOr[+A] extends js.Any
object NullUndefOr extends Companion {
  override type F[+A] = NullUndefOr[A]

  /* Duplicated for intellij. name it `apply` so users can be explicit */
  @inline implicit final def apply[A](a: A): NullUndefOr[A] =
    a.asInstanceOf[NullUndefOr[A]]

  /* Integrate with Scala.js built-in type */
  @inline implicit def fromJsUndefOr[A](a: js.UndefOr[A]): NullUndefOr[A] =
    a.asInstanceOf[NullUndefOr[A]]

  /* This is pretty arbitrary, but choose `undefined` as the empty value */
  @inline def Empty: NullUndefOr[Nothing] =
    UndefOr.Empty

  @inline implicit final class Ops[A](val self: NullUndefOr[A]) extends AnyVal with OpsBase[NullUndefOr, A] {
    @inline override def isEmpty: Boolean =
      self == null || js.isUndefined(self)

    @inline override protected def Empty: NullUndefOr[Nothing] =
      NullUndefOr.Empty
  }
}

/**
  * The value or `null`
  */
sealed trait NullOr[+A] extends NullUndefOr[A]
object NullOr extends Companion {
  override type F[+A] = NullOr[A]

  /* Duplicated for intellij. name it `apply` so users can be explicit */
  @inline implicit final def apply[A](a: A): NullOr[A] =
    a.asInstanceOf[NullOr[A]]

  @inline def Empty: NullOr[Nothing] =
    null

  @inline implicit final class Ops[A](val self: NullOr[A]) extends AnyVal with OpsBase[NullOr, A] {
    @inline override def isEmpty: Boolean =
      self == NullOr.Empty

    @inline override protected def Empty: NullOr[Nothing] =
      NullOr.Empty
  }
}

/**
  * The value or `undefined`
  */
sealed trait UndefOr[+A] extends NullUndefOr[A]
object UndefOr extends Companion {
  override type F[+A] = UndefOr[A]

  /* Duplicated for intellij. name it `apply` so users can be explicit */
  @inline implicit final def apply[A](a: A): UndefOr[A] =
    a.asInstanceOf[UndefOr[A]]

  /* Integrate with Scala.js built-in type */
  @inline implicit def fromJsUndefOr[A](a: js.UndefOr[A]): UndefOr[A] =
    a.asInstanceOf[UndefOr[A]]

  @inline def Empty: UndefOr[Nothing] =
    ().asInstanceOf[UndefOr[Nothing]]

  @inline implicit final class Ops[A](val self: UndefOr[A]) extends AnyVal with OpsBase[UndefOr, A] {
    @inline override def isEmpty: Boolean =
      js.isUndefined(self)

    @inline override protected def Empty: UndefOr[Nothing] =
      UndefOr.Empty
  }
}

/**
  * This can represent when a superclass has an optional field and a subclass narrows it to a required field
  */
sealed trait Required[+A] extends NullOr[A] with UndefOr[A]
object Required extends Companion {
  override type F[+A] = Required[A]

  /* Duplicated for intellij. name it `apply` so users can be explicit */
  @inline implicit final def apply[A](a: A): Required[A] =
    a.asInstanceOf[Required[A]]

  @inline implicit final class Ops[A](val self: Required[A]) extends AnyVal {
    @inline def get: A =
      self.asInstanceOf[A]
  }
}

trait Companion {
  type F[+A] <: NullUndefOr[A]

  /** Upcast `A` to `F[B1 | B2]`.
    *
    *  This needs evidence that `A <: B1 | B2`.
    */
  @inline implicit final def upcastUnion[A, B1, B2](a: A)(implicit ev: Evidence[A, B1 | B2]): F[B1 | B2] =
    a.asInstanceOf[F[B1 | B2]]

  @inline implicit final def isJsAny[A](value: F[A])(implicit ev: A => js.Any): js.Any =
    value.map(ev).asInstanceOf[js.Any]
}

sealed trait OpsBase[F[+_], A] extends Any { ops =>
  @inline protected def self: F[A]

  @inline protected def Empty: F[Nothing]

  /** Returns true if the option is empty, false otherwise. */
  @inline def isEmpty: Boolean

  /** Returns true if the option is not empty.
    */
  @inline final def isDefined: Boolean =
    !isEmpty

  /** Returns the option's value.
    *  @note The option must be nonEmpty.
    *  @throws java.util.NoSuchElementException if the option is empty.
    */
  @inline final def get: A =
    if (isEmpty) throw new NoSuchElementException(s"Value was empty: $self")
    else self.asInstanceOf[A]

  @inline final private def forceGet: A =
    self.asInstanceOf[A]

  /** Returns the option's value if the option is nonempty, otherwise
    *  return the result of evaluating `default`.
    *
    *  @param default  the default expression.
    */
  @inline final def getOrElse[B >: A](default: => B): B =
    if (isEmpty) default else forceGet

  /** Returns the option's value if it is nonempty,
    *  or `null` if it is empty.
    *  Although the use of null is discouraged, code written to use
    *  $option must often interface with code that expects and returns nulls.
    *  @example {{{
    *  val initalText: Option[String] = getInitialText
    *  val textField = new JComponent(initalText.orNull,20)
    *  }}}
    */
  @inline final def orNull[A1 >: A](implicit ev: Null <:< A1): A1 =
    this getOrElse ev(null)

  /** Returns the result of applying `f` to this $option's
    *  value if this $option is nonempty.
    *  Otherwise return $none.
    *
    *  @note This is similar to `flatMap` except here,
    *  `f` does not need to wrap its result in an $option.
    *
    *  @param  f   the function to apply
    *  @see flatMap
    *  @see foreach
    */
  @inline final def map[B](f: A => B): F[B] =
    (if (isEmpty) self else f(forceGet)).asInstanceOf[F[B]]

  /** Returns the result of applying `f` to this $option's
    *  value if the $option is nonempty.  Otherwise, evaluates
    *  expression `ifEmpty`.
    *
    *  @note This is equivalent to `$option map f getOrElse ifEmpty`.
    *
    *  @param  ifEmpty the expression to evaluate if empty.
    *  @param  f       the function to apply if nonempty.
    */
  @inline final def fold[B](ifEmpty: => B)(f: A => B): B =
    if (isEmpty) ifEmpty else f(forceGet)

  /** Returns the result of applying `f` to this $option's value if
    *  this $option is nonempty.
    *  Returns $none if this $option is empty.
    *  Slightly different from `map` in that `f` is expected to
    *  return an $option (which could be $none).
    *
    *  @param  f   the function to apply
    *  @see map
    *  @see foreach
    */
  @inline final def flatMap[B](f: A => F[B]): F[B] =
    if (isEmpty) self.asInstanceOf[F[B]] else f(forceGet)

  @inline def flatten[B](implicit ev: A <:< F[B]): F[B] =
    if (isEmpty) self.asInstanceOf[F[B]] else ev(forceGet)

  /** Returns this $option if it is nonempty '''and''' applying the predicate `p` to
    *  this $option's value returns true. Otherwise, return $none.
    *
    *  @param  p   the predicate used for testing.
    */
  @inline final def filter(p: A => Boolean): F[A] =
    if (isEmpty || p(forceGet)) self else Empty

  /** Returns this $option if it is nonempty '''and''' applying the predicate `p` to
    *  this $option's value returns false. Otherwise, return $none.
    *
    *  @param  p   the predicate used for testing.
    */
  @inline final def filterNot(p: A => Boolean): F[A] =
    if (isEmpty || !p(forceGet)) self else Empty

  /** Returns false if the option is $none, true otherwise.
    *  @note   Implemented here to avoid the implicit conversion to Iterable.
    */
  @inline final def nonEmpty: Boolean =
    isDefined

  /** We need a whole WithFilter class to honor the "doesn't create a new
    *  collection" contract even though it seems unlikely to matter much in a
    *  collection with max size 1.
    */
  @inline final class WithFilter(p: A => Boolean) {
    @inline def map[B](f: A => B): F[B] =
      if (isEmpty) self.asInstanceOf[F[B]]
      else if (!p(ops.forceGet)) Empty.asInstanceOf[F[B]]
      else ops.map(f)

    @inline def flatMap[B](f: A => F[B]): F[B] =
      if (isEmpty) self.asInstanceOf[F[B]]
      else if (!p(ops.forceGet)) Empty.asInstanceOf[F[B]]
      else ops.flatMap(f)

    @inline def foreach[U](f: A => U): Unit =
      if (isEmpty || !p(ops.forceGet)) ()
      else ops.foreach(f)

    @inline def withFilter(q: A => Boolean): WithFilter =
      new WithFilter(x => p(x) && q(x))
  }

  /** Necessary to keep $option from being implicitly converted to
    *  [[scala.collection.Iterable]] in `for` comprehensions.
    */
  @inline final def withFilter(p: A => Boolean): WithFilter =
    new WithFilter(p)

  /** Tests whether the $option contains a given value as an element.
    *
    *  `x.contains(y)` differs from `x == y` only in the fact that it will
    *  return `false` when `x` and `y` are both `undefined[F]`.
    *
    *  @param elem the element to test.
    *  @return `true` if the $option has an element that is equal (as
    *  determined by `==`) to `elem`, `false` otherwise.
    */
  @inline final def contains[A1 >: A](elem: A1): Boolean =
    !isEmpty && elem == forceGet

  /** Returns true if this option is nonempty '''and''' the predicate
    *  `p` returns true when applied to this $option's value.
    *  Otherwise, returns false.
    *
    *  @param  p   the predicate to test
    */
  @inline final def exists(p: A => Boolean): Boolean =
    !isEmpty && p(forceGet)

  /** Returns true if this option is empty '''or''' the predicate
    *  `p` returns true when applied to this $option's value.
    *
    *  @param  p   the predicate to test
    */
  @inline final def forall(p: A => Boolean): Boolean =
    isEmpty || p(forceGet)

  /** Apply the given procedure `f` to the option's value,
    *  if it is nonempty. Otherwise, do nothing.
    *
    *  @param  f   the procedure to apply.
    *  @see map
    *  @see flatMap
    */
  @inline final def foreach[U](f: A => U): Unit =
    if (!isEmpty) f(forceGet)

  /** Returns the result of applying `pf` to this $option's contained
    *  value, '''if''' this option is
    *  nonempty '''and''' `pf` is defined for that value.
    *  Returns $none otherwise.
    *
    *  @param  pf   the partial function.
    *  @return the result of applying `pf` to this $option's
    *  value (if possible), or $none.
    */
  @inline final def collect[B](pf: PartialFunction[A, B]): F[B] =
    if (isEmpty) self.asInstanceOf[F[B]]
    else pf.applyOrElse(forceGet, (_: A) => Empty).asInstanceOf[F[B]]

  /** Returns this $option if it is nonempty,
    *  otherwise return the result of evaluating `alternative`.
    *  @param alternative the alternative expression.
    */
  @inline final def orElse[B >: A](alternative: => F[B]): F[B] =
    if (isEmpty) alternative else self

  /** Returns a singleton iterator returning the $option's value
    *  if it is nonempty, or an empty iterator if the option is empty.
    */
  def iterator: scala.collection.Iterator[A] =
    if (isEmpty) scala.collection.Iterator.empty
    else scala.collection.Iterator.single(forceGet)

  /** Returns a singleton list containing the $option's value
    *  if it is nonempty, or the empty list if the $option is empty.
    */
  def toList: List[A] =
    if (isEmpty) Nil else forceGet :: Nil

  /** Returns a `Left` containing the given argument `left` if this $option is
    *  empty, or a `Right` containing this $option's value if this is nonempty.
    *
    *  @param left the expression to evaluate and return if this is empty
    *  @see toLeft
    */
  @inline final def toRight[X](left: => X): Either[X, A] =
    if (isEmpty) Left(left) else Right(forceGet)

  /** Returns a `Right` containing the given argument `right` if this is empty,
    *  or a `Left` containing this $option's value if this $option is nonempty.
    *
    *  @param right the expression to evaluate and return if this is empty
    *  @see toRight
    */
  @inline final def toLeft[X](right: => X): Either[A, X] =
    if (isEmpty) Right(right) else Left(forceGet)

  /** Returns a `Some` containing this $option's value
    *  if this $option is nonempty, [[scala.None None]] otherwise.
    */
  @inline final def toOption: Option[A] =
    if (isEmpty) None else Some(forceGet)

  /** Interop with Scala.js built-in type */
  @inline def toJsUndefOr: js.UndefOr[A] =
    if (isEmpty) js.undefined else self.asInstanceOf[js.UndefOr[A]]
}
