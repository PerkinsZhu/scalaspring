package com.perkinszhu.classes

import com.user.PersonController

import scala.reflect.ClassTag
import scala.reflect.api.Universe
import scala.reflect.runtime.universe

/**
  * Created by PerkinsZhu on 2018/7/7 16:43
  **/
class TypeTagBuilder() {

  import scala.reflect.api
  import scala.reflect.runtime.currentMirror
  import scala.reflect.runtime.universe._
  import scala.tools.reflect.ToolBox


  val toolbox = currentMirror.mkToolBox()

  def createTypeTag(tp: String): TypeTag[_] = {
    val tree = toolbox.parse(s"scala.reflect.runtime.universe.typeTag[$tp]")
    val typeTag = toolbox.eval(tree).asInstanceOf[TypeTag[_]]
    test(tp, typeTag)
    typeTag
  }

  def stringToTypeTag[A](name: String): TypeTag[A] = {
    val c = Class.forName(name) // obtain java.lang.Class object from a string
    val mirror = runtimeMirror(c.getClassLoader) // obtain runtime mirror
    val sym = mirror.staticClass(name) // obtain class symbol for `c`
    val tpe = sym.selfType // obtain type object for `c`
    // create a type tag which contains above type object
    val typeTag = TypeTag[A](mirror, new api.TypeCreator {
      def apply[U <: Universe with Singleton](m: api.Mirror[U]) =
        if (m eq mirror) {
          tpe.asInstanceOf[U#Type]
        } else {
          throw new IllegalArgumentException(s"Type tag defined in $mirror cannot be migrated to other mirrors.")
        }
    })

    test(name, typeTag)
    typeTag
  }
/*  def createTypeTag(tp: String): TypeTag[_] = {
    val ttagCall = s"scala.reflect.runtime.universe.typeTag[$tp]"
    val tpe = toolbox.typecheck(toolbox.parse(ttagCall), toolbox.TYPEmode).tpe.resultType.typeArgs.head

    TypeTag(currentMirror, new reflect.api.TypeCreator {
      def apply[U <: reflect.api.Universe with Singleton](m: reflect.api.Mirror[U]) = {
        assert(m eq mirror, s"TypeTag[$tpe] defined in $mirror cannot be migrated to $m.")
        tpe.asInstanceOf[U#Type]
      }
    }
  }*/


  private def test[T](name: String, typeTag: TypeTag[T]) = {
    if (name == "com.user.PersonController") {


      val myType = typeOf(typeTag)
      val m = universe.runtimeMirror(myType.getClass.getClassLoader)
      val classC = myType.typeSymbol.asClass
      val cm = m.reflectClass(classC)
      val ctorC = myType.decl(universe.termNames.CONSTRUCTOR).asMethod
      val ctorm = cm.reflectConstructor(ctorC)
      val daata = ctorm()
      println(daata)

      val methodX = myType.decl(universe.TermName("addPerson")).asMethod
      val im = m.reflect(daata)
//      val classTag = ClassTag[T]( typeTag.mirror.runtimeClass( typeTag.tpe ) )
//      val im = m.reflect()

      println(im)
      println(methodX)
      val mm = im.reflectMethod(methodX)
      mm()

 /*     val m = universe.runtimeMirror(tag.getClass.getClassLoader)
      val mn = tag.decl(universe.TermName("addPerson")).asMethod
      val im2 = m.reflect(tag.typeSymbol.asClass)
      //                   ^ and here
      val toCall = im2.reflectMethod(mn)
      toCall()
*/
//      val hackedMirror = runtimeMirror(typeTag.mirror.classLoader).asInstanceOf[scala.reflect.runtime.JavaMirrors#JavaMirror]
     /* println(typeTag)
      println(typeTag.getClass.getClassLoader)
      im.reflectMethod(tempM)*/

    }
  }
  def getTypeTag[T: universe.TypeTag](obj: T) = universe.typeTag[T]
  def typeToClassTag[T: TypeTag]: ClassTag[T] = {
    ClassTag[T]( typeTag[T].mirror.runtimeClass( typeTag[T].tpe ) )
  }

}


/*import scala.reflect.runtime.universe._
import java.lang.reflect.Method
object methodToScala {
  def apply(jmeth: Method): MethodSymbol = {
    // NOTE: change to your mirror of choice
    // be sure to pass the correct classloader
    // or otherwise your classes might end up being not found
    val mirror = runtimeMirror(getClass.getClassLoader)
//    val hackedMirror = mirror.asInstanceOf[scala.reflect.runtime.JavaMirrors#JavaMirror]
//    hackedMirror.methodToScala(jmeth).asInstanceOf[MethodSymbol]
  }
}*/
