package comscala.reflection

/**
  *
  * Created by PerkinsZhu on 7/4/18 9:22 PM
  *
  **/

import comscala.beans.{Person, Student}
import org.junit.Test

import scala.reflect.runtime.universe._

object ReflectionHelpers extends ReflectionHelpers

trait ReflectionHelpers {

  protected val classLoaderMirror = runtimeMirror(getClass.getClassLoader)

  /**
    * Encapsulates functionality to reflectively invoke the constructor
    * for a given case class type `T`.
    *
    * @tparam T the type of the case class this factory builds
    */
  class CaseClassFactory[T: TypeTag] {

    val tpe = typeOf[T]
    val classSymbol = tpe.typeSymbol.asClass

    if (!(tpe <:< typeOf[Product] && classSymbol.isCaseClass)) {
      throw new IllegalArgumentException(
        "CaseClassFactory only applies to case classes!"
      )
    }

    val classMirror = classLoaderMirror reflectClass classSymbol

    val constructorSymbol = tpe.decl(termNames.CONSTRUCTOR)

    val defaultConstructor =
      if (constructorSymbol.isMethod) {
        constructorSymbol.asMethod
      } else {
        val ctors = constructorSymbol.asTerm.alternatives
        ctors.map {
          _.asMethod
        }.find {
          _.isPrimaryConstructor
        }.get
      }

    val constructorMethod = classMirror reflectConstructor defaultConstructor


    /**
      * Attempts to create a new instance of the specified type by calling the
      * constructor method with the supplied arguments.
      *
      * @param args the arguments to supply to the constructor method
      */
    def buildWith(args: Seq[_]): T = constructorMethod(args: _*).asInstanceOf[T]

  }

}

class TestCase {

  @Test
  def testDemo(): Unit = {
    val personFactory = new ReflectionHelpers.CaseClassFactory[Student]
    val result: Student = personFactory.buildWith(Seq("Connor", 27))
    val expected = Person("Connor", 27)
    assert(result == expected)
    //    println(result)
  }


}


object TypeTagBuilder {
  def main(args: Array[String]): Unit = {
    val result = new TypeTagBuilder().createTypeTag("com.user.PersonService")
    val result2 = new TypeTagBuilder().stringToTypeTag("com.user.PersonService")

  }

}

class TypeTagBuilder() {

  import scala.reflect.runtime.universe._
  import scala.reflect.runtime.currentMirror
  import scala.tools.reflect.ToolBox

  val toolbox = currentMirror.mkToolBox()

  def createTypeTag(tp: String): TypeTag[_] = {
    val ttree = toolbox.parse(s"scala.reflect.runtime.universe.typeTag[$tp]")
    toolbox.eval(ttree).asInstanceOf[TypeTag[_]]
  }

  import scala.reflect.runtime.universe._
  import scala.reflect.api

  def stringToTypeTag[A](name: String): TypeTag[A] = {
    val c = Class.forName(name) // obtain java.lang.Class object from a string
    val mirror = runtimeMirror(c.getClassLoader) // obtain runtime mirror
    val sym = mirror.staticClass(name) // obtain class symbol for `c`
    val tpe = sym.selfType // obtain type object for `c`
    // create a type tag which contains above type object
    TypeTag(mirror, new api.TypeCreator {
      def apply[U <: api.Universe with Singleton](m: api.Mirror[U]) =
        if (m eq mirror) {
          tpe.asInstanceOf[U#Type]
        } else {
          throw new IllegalArgumentException(s"Type tag defined in $mirror cannot be migrated to other mirrors.")
        }
    })
  }
}