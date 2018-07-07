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
      println(tag.tpe.typeSymbol.asClass.annotations)
      println(tag.tpe.decls)
    })
  }

  @Test
  def testClassHelper(): Unit = {
    val classHelper = new ClassHelper("com.user")
    println(classHelper.serviceTypeTagSet)
    println(classHelper.actionTypeTagSet)
    println(classHelper.injectTypeTagSet)
  }

}