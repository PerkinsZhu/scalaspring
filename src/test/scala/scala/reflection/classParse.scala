package scala.reflection

import java.io.File
import java.net.{JarURLConnection, URLDecoder}

import com.user.beans
import com.user.beans.Person
import org.junit.Test

import scala.reflect.macros.{Universe => mu}
import scala.reflect.runtime.{universe => ru}


/**
  *
  * Created by PerkinsZhu on 7/3/18 10:35 PM
  *
  **/
class classParse {

  @org.junit.Test
  def testParseClass(): Unit = {
    val packageName = "com.user"
    val packagePath = packageName.replace(".", "/")
    val urls = Thread.currentThread.getContextClassLoader.getResources(packagePath)
    while (urls.hasMoreElements) {
      val url = urls.nextElement()
      url.getProtocol match {
        case "jar" => {
          val jar = url.openConnection().asInstanceOf[JarURLConnection].getJarFile
          val element = jar.entries
          while (element.hasMoreElements) {
            val entry = element.nextElement()
            println(entry)
          }

        }
        case "file" => {
          val filePath = URLDecoder.decode(url.getFile, "UTF-8")
          parseFIle(new File(filePath), packageName)
        }
        case item: String => println(s"未处理类型:$item")
      }
    }
  }


  def parseFIle(file: File, packageName: String): Unit = {
    val files = file.listFiles()
    files.filter(_.isFile).foreach(file => {
      val fileName = file.getName
      val className = packageName + "." + fileName.substring(0, fileName.size - 6)
      val clazz = Thread.currentThread.getContextClassLoader.loadClass(className)
      clazz.getMethods.foreach(method => {
        //        println(method.getModifiers)
        //        println(clazz.getName + "-->" + method)
      })

      val clazzz = ru.rootMirror.classLoader.loadClass(className)
      //      val temp2 = ru.runtimeMirror(clazzz.getClassLoader).reflectClass(typesy)
      println(clazz == clazzz)
      clazzz.getMethods.foreach(m => {
        println(m.setAccessible(true))
      })
      clazzz.newInstance()

    })
    files.filter(_.isDirectory).foreach(file => parseFIle(file, packageName + "." + file.getName))
  }


  @Test
  def parseWithScala(): Unit = {
    val tag = getTypeTag(Person)
    val mirror = tag.mirror
    println(mirror.classLoader)
    println(mirror.RootPackage)
    val tpe = tag.tpe
    println(tpe)
    println(tpe.decls)
    val res = ru.rootMirror.classLoader.loadClass("com.user.beans.Person")
    //    val data = res.newInstance()
    //    println(data +"--"+res)

    val classPerson = ru.typeOf[beans.Person].typeSymbol.asClass
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val cm = m.reflectClass(classPerson)
    println(classPerson)
    println(cm)

    val ctor = ru.typeOf[beans.Person].decl(ru.termNames.CONSTRUCTOR).asMethod

    val ctorm = cm.reflectConstructor(ctor)
    val p = ctorm("aaaa", 12)
    println(p)

    val shippingTermSymb = ru.typeOf[beans.Person].decl(ru.TermName("name")).asTerm
    println(shippingTermSymb)
    val im = m.reflect(p)
    val shippingFieldMirror = im.reflectField(shippingTermSymb)
    println(shippingFieldMirror)
    val data = shippingFieldMirror.get
    println(data)
  }

  def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]


  @Test
  def testOther(): Unit = {
    println(Person)
    println(Person.getClass)
    println(classOf[beans.Person])
    println(ru.typeOf[beans.Person])
    println(ru.typeTag[beans.Person])
    val typeTag = ru.typeTag[beans.Person]
    val tpe = typeTag.tpe
    val mirror = typeTag.mirror
    val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
    val rootMirror = ru.rootMirror
    println(tpe)
    println(mirror)
    println(rootMirror)
    println(runtimeMirror)
    println(mirror.hashCode())
    println(rootMirror.hashCode())
    println(runtimeMirror.hashCode())

    val symbol = ru.typeOf[beans.Person].typeSymbol
    println(symbol)
    println(symbol.asClass)
    val cm = mirror.reflectClass(symbol.asClass)
    println(cm)
    val ctor = ru.typeOf[beans.Person].decl(ru.termNames.CONSTRUCTOR).asMethod
    val constract = cm.reflectConstructor(ctor)
    println("---------------------------------")
    tpe.decls.foreach(item => {
      println(item + "--->" + item.isPrivate)
    })
    //    typeTag.getClass.newInstance()

    println(constract.symbol)
    println(constract.symbol.asMethod)
  }

  @Test
  def testStudent(): Unit = {
    val decls = ru.typeOf[Student].decls
    decls.foreach(item => {
      if (item.isMethod) {
        val method = item.asMethod
        method.paramLists.flatten.foreach(sym => {
          println(s"$method --> ${sym} ---> ${sym.typeSignature}--->${sym.typeSignature}")
        })
        if (method.isConstructor) {
        }
      }
    })
  }

  @Test
  def createStudent(): Unit = {
    val personType = ru.typeOf[beans.Person]
    val constract = personType.decl(ru.termNames.CONSTRUCTOR).asMethod
    constract.paramLists
    val cm = ru.rootMirror.reflectClass(personType.typeSymbol.asClass)
    val constractMetod = cm.reflectConstructor(constract)
    val res = constractMetod("java", 23)
    println(res)

  }

  @Test
  def testTtt(): Unit = {
    val bean = BeanFactory.createBean[beans.Person]()
    println(bean)

  }

  class Ttt private()


}


object BeanFactory {

  import scala.reflect.runtime.universe._

  def createBean[T: TypeTag](): Option[T] = {
    val typee = ru.typeOf[T]
    val constructor = typee.decl(ru.termNames.CONSTRUCTOR).asMethod
    if (constructor.isPrivate) {
      println("private class can not created ")
      None
    } else {
      val classMirror = ru.runtimeMirror(getClass.getClassLoader).reflectClass(typee.typeSymbol.asClass)
      val constructorMethod = classMirror.reflectConstructor(constructor)
      val params = constructor.paramLists.flatten.map(par => {
        // =：= 判断两个类型是否相等
        if (par.typeSignature =:= typeOf[Int]) {
          0
        } else {
          if (par.typeSignature =:= typeOf[String]) {
            ""
          } else {
            null
          }
        }
      })
      Some(constructorMethod(params: _*).asInstanceOf[T])
    }
  }

}

