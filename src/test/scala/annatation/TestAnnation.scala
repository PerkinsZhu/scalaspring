package scala.annatation
import scala.reflect.runtime.universe._
import org.junit.Test

import scala.annatation.beans.Student

/**
  *
  * Created by PerkinsZhu on 7/3/18 12:07 AM
  *
  **/
class TestAnnation {


  @Test
  def testBean(): Unit ={
    val result = symbolOf[Student].annotations.find(_.tree.tpe =:= typeOf[Bean])
    println(result)
    symbolOf[Student].annotations.foreach(println)
  }
}
