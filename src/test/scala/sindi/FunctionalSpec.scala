//      _____         ___                       
//     / __(_)__  ___/ (_)                      
//    _\ \/ / _ \/ _  / /                       
//   /___/_/_//_/\_,_/_/                        
//                                              
//  (c) 2011, Alois Cochard                     
//                                              
//  http://aloiscochard.github.com/sindi        
//                                              

package sindi

import org.specs2.mutable._

class FunctionalSpec extends Specification {

  "Sindi" should {
    "throw an exception when type is not bound" in {
      class Foo extends Context
      new Foo().inject[String] must throwAn[RuntimeException]
    }

    "bind concrete type" in {
      class Foo extends Context { override val bindings: Bindings = bind[String] to "sindi" }
      new Foo().inject[String] mustEqual "sindi"
    }

    "bind concrete type with qualifier" in {
      class Foo extends Context { override val bindings: Bindings = bind[String] to "sindi" as "sindi"}
      val foo = new Foo
      foo.inject[String] must throwAn[RuntimeException]
      foo.injectAs[String]("sindi") mustEqual "sindi"
    }

    "bind concrete type with scope" in {
      class Bar
      var state = 1
      class Foo extends Context { override val bindings: Bindings = bind[Bar] to new Bar scope { state } }
      val foo = new Foo
      val bar1 = foo.inject[Bar].hashCode
      bar1 mustEqual foo.inject[Bar].hashCode
      state = 2
      bar1 mustNotEqual foo.inject[Bar].hashCode
    }

    "bind abstract type" in {
      class Foo extends Context { override val bindings: Bindings = bind[String] to "sindi" }
      new Foo().inject[AnyRef] mustEqual "sindi"
    }

    "bind abstract type with FIFO priority" in {
      class FooA extends Context { override val bindings = Bindings(bind[AnyRef] to "scala",
                                                                    bind[String] to "sindi") }
      val fooA = new FooA
      fooA.inject[AnyRef] mustEqual "scala"
      fooA.inject[String] mustEqual "sindi"

      class FooB extends Context { override val bindings = Bindings(bind[String] to "sindi",
                                                                    bind[AnyRef] to "scala") }
      val fooB = new FooB
      fooB.inject[String] mustEqual "sindi"
      fooB.inject[AnyRef] mustEqual "sindi"
    }

    "bind parameterized type" in {
      val list = List("sindi")
      class Foo extends Context { override val bindings: Bindings = bind[List[String]] to list }
      new Foo().inject[List[String]] mustEqual list
      new Foo().inject[List[AnyRef]] mustEqual list

    }

    // TODO [aloiscochard] Test scope, qualifier, factory, Option
  }
}
