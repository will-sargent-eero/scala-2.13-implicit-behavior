# Echopraxia Demo Project showing 2.13 Implicit Behavior

Following on from [blog post](https://tersesystems.com/blog/2022/06/12/what-scala-adds-to-a-logging-api/), I realized that the implicit behavior that I'm seeing where I can provide an implicit `ToValue[Foo]` without having an import tax `import fb._` only happens on 2.13.

That is, I can compile the following in 2.13, but not in 2.12:

```scala
val logger = LoggerFactory.getLogger.withFieldBuilder(SomeFieldBuilder)
 
val myFoo: Foo = ...
logger.info("Some message {}", fb => fb.keyValue("foo", myFoo))
```

In 2.13, there is an explicit `SomeFieldBuilder.type` that gives the compiler enough information to deal with the implicits.

Also, implicit resolution only seems to happen when an `implicit val` is declared in the trait:

```scala
trait FooImplicitValBuilder extends FieldBuilder {
  implicit val fooToObjectValue: ToObjectValue[Foo] = { value =>
    com.tersesystems.echopraxia.api.Value.`object`(
      keyValue("name", value.name),
      keyValue("age", value.age)
    )
  }
}
object FooImplicitValBuilder extends FooImplicitValBuilder
```

Defining an implicit object does *not* work:

```scala
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
```

Apparently this is because you can get the exact type you want with an implicit val, but an implicit object [has its own type](https://stackoverflow.com/questions/65258339/why-prefer-implicit-val-over-implicit-object).

There must be something in the [implicit rules](https://www.scala-lang.org/files/archive/spec/2.13/07-implicits.html#implicit-parameters) for 2.13 that covers this exactly, but the spec is very dense.
