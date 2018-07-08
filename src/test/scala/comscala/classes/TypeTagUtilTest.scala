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
    /*classHelper.actionMap.foreach(item =>{
      val handel = item._2
      val mirror = runtimeMirror(handel.controllerTag.getClass.getClassLoader)
      val instanceMirror = mirror.reflect(handel.controllerTag)
      val addPerson = instanceMirror.reflectMethod(handel.actionMethod.asMethod)
        addPerson()

    }
    )*/

  }

}