package comscala.classes

import javassist.bytecode.stackmap.TypeTag

import com.perkinszhu.classes.{ClassHelper, TypeTagUtil}
import org.junit.Test

import reflect.runtime.universe._

/**
  * Created by PerkinsZhu on 2018/7/7 13:42
  **/
class TypeTagUtilTest {

  @Test
  def testGetClassSet(): Unit = {
    val result = TypeTagUtil.getClassSet("com.user")
    println(result)
    val annotations = result.map(tag => {
      println(tag.typeSymbol.asClass.annotations)
      println(tag.decls)
    })
  }

  @Test
  def testClassHelper(): Unit = {
    val classHelper = new ClassHelper("com.user")
    println(classHelper.controllerTypeTagSet)
    println(classHelper.serviceTypeTagSet)
    println(classHelper.actionMap)
    println(classHelper.injectTypeTagSet)
    classHelper.actionMap.foreach(item => {
      val handel = item._2
      val tag = handel.controllerTag
      val method = handel.actionMethod.asMethod
      val mirror = runtimeMirror(handel.controllerTag.getClass.getClassLoader)
      val consM = tag.decl(termNames.CONSTRUCTOR).asMethod
      val init = mirror.reflectClass(tag.typeSymbol.asClass).reflectConstructor(consM)
      val controller = init()
      println(controller)
      val instanceMirror = mirror.reflect(controller)
      val addPerson = instanceMirror.reflectMethod(method)
      val result = addPerson()
      print(result)
    }
    )

  }

}