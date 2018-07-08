package com.perkinszhu.classes

import scala.reflect.api.Universe
import scala.reflect.api
import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._
import scala.tools.reflect.ToolBox


/**
  * Created by PerkinsZhu on 2018/7/7 16:43
  **/
class TypeTagBuilder() {

  val toolbox = currentMirror.mkToolBox()

  def createTypeTag(tp: String): TypeTag[_] = {
    val tree = toolbox.parse(s"scala.reflect.runtime.universe.typeTag[$tp]")
    toolbox.eval(tree).asInstanceOf[TypeTag[_]]
  }

  def stringToTypeTag[A](name: String): TypeTag[A] = {
    val c = Class.forName(name) // obtain java.lang.Class object from a string
    val mirror = runtimeMirror(c.getClassLoader) // obtain runtime mirror
    val sym = mirror.staticClass(name) // obtain class symbol for `c`
    val tpe = sym.selfType // obtain type object for `c`
    // create a type tag which contains above type object
    TypeTag[A](mirror, new api.TypeCreator {
      def apply[U <: Universe with Singleton](m: api.Mirror[U]) =
        if (m eq mirror) {
          tpe.asInstanceOf[U#Type]
        } else {
          throw new IllegalArgumentException(s"Type tag defined in $mirror cannot be migrated to other mirrors.")
        }
    })

  }
}
