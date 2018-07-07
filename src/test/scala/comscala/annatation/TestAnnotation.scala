package comscala.annatation
import comscala.beans.Student
import org.junit.Test

import scala.reflect.runtime.universe._

/**
  *
  * Created by PerkinsZhu on 7/3/18 12:07 AM
  *
  **/
class TestAnnotation {


  @Test
  def testBean(): Unit ={
    val result = symbolOf[Student].annotations.find(_.tree.tpe =:= typeOf[Bean])
    println(result)
    symbolOf[Student].annotations.foreach(println)
  }
}
