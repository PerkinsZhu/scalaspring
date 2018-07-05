package scala.classutil

import java.io.File

import org.clapper.classutil.{ClassFinder, MapToBean}
import org.junit.Test

/**
  *
  * Created by PerkinsZhu on 7/5/18 9:53 PM
  *
  **/
class TestCase {

  def main(args: Array[String]): Unit = {
    testGetClasss()
  }

  @Test
  def testGetClasss(): Unit = {
    val finder = ClassFinder()
    val classes = finder.getClasses // classes is an Iterator[ClassInfo]
    classes.foreach(println)
  }

  @Test
  def getClassFromJar(): Unit = {
    val classpath = List("/home/perkinszhu/data/test/gson-2.8.2.jar").map(new File(_))
    val finder = ClassFinder(classpath)
    val classes = finder.getClasses.filter(_.isConcrete)
    classes.foreach(println)
  }

  @Test
  def mapTOBean(): Unit ={

    val charList = List('a', 'b', 'c')

    val subMap = Map("sub1" -> 1, "sub2" -> 2)
    val map =  Map("int" -> 1,
      "float" -> 2f,
      "someString" -> "three",
      "intClass" -> classOf[Int],
      "subMap" -> subMap,
      "list" -> charList)
    val obj = MapToBean(map)

    obj.getClass.getMethods.filter(_.getName startsWith "get").foreach(println)

    def call(methodName: String) = {
      val method = obj.getClass.getMethod(methodName)
      method.invoke(obj)
    }

    println()
    println("getSubMap returns " + call("getSubMap"))
  }
}
