package com.example

import com.tersesystems.echopraxia.api.{FieldBuilderResult, Value}
import com.tersesystems.echopraxia.plusscala.LoggerFactory
import com.tersesystems.echopraxia.plusscala.api.FieldBuilder


object Main {
  private val implicitValLogger = LoggerFactory.getLogger.withFieldBuilder(FooImplicitValBuilder)
  private val implicitObjectLogger = LoggerFactory.getLogger.withFieldBuilder(FooImplicitObjectBuilder)

  val foo: Foo = Foo("name", 1)

  val implicitValFunction: FooImplicitValBuilder.type => FieldBuilderResult = { fb =>
    fb.obj("foo", foo)
  }

  def implicitValMethod(fb: FooImplicitValBuilder.type): FieldBuilderResult = {
    fb.obj("foo", foo)
  }

  val implicitObjectFunction: FooImplicitObjectBuilder.type => FieldBuilderResult = { fb =>
    fb.obj("foo", foo)
  }

  def implicitObjectMethod(fb: FooImplicitObjectBuilder.type): FieldBuilderResult = {
    fb.obj("foo", foo)
  }

  def main(args: Array[String]): Unit = {
    implicitValLogger.debug("using method {} {}", fb => fb.obj("foo", foo))
    implicitValLogger.debug("using method {} {}", fb => implicitValMethod(fb))
    implicitValLogger.debug("using method {} {}", implicitValFunction)

    implicitObjectLogger.debug("using method {} {}", fb => fb.obj("foo", foo))
    implicitObjectLogger.debug("using method {} {}", fb => implicitObjectMethod(fb))
    implicitObjectLogger.debug("using method {} {}", implicitObjectFunction)
  }
}

case class Foo(name: String, age: Int)

trait FooImplicitValBuilder extends FieldBuilder {
  implicit val fooToObjectValue: ToObjectValue[Foo] = { value =>
    com.tersesystems.echopraxia.api.Value.`object`(
      keyValue("name", value.name),
      keyValue("age", value.age)
    )
  }
}
object FooImplicitValBuilder extends FooImplicitValBuilder

trait FooImplicitObjectBuilder extends FieldBuilder {
  implicit object FooToObjectValue extends ToObjectValue[Foo] {
    override def toValue(value: Foo): Value.ObjectValue = {
      com.tersesystems.echopraxia.api.Value.`object`(
        keyValue("name", value.name),
        keyValue("age", value.age)
      )
    }
  }
}
object FooImplicitObjectBuilder extends FooImplicitObjectBuilder