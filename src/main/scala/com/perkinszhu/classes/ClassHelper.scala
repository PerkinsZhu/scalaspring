package com.perkinszhu.classes

import java.io.File
import java.net.{JarURLConnection, URLDecoder}

import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.reflect.runtime.universe._

/**
  * Created by PerkinsZhu on 2018/7/7 11:26
  **/
object ClassHelper {
  val basePackage = ""
  lazy val classes = ClassUtil.getClassSet(basePackage)
}

object ClassUtil {
  val logger = LoggerFactory.getLogger(this.getClass)

  def getClassLoader(): ClassLoader = {
    Thread.currentThread().getContextClassLoader
  }

  def loadClass(className: String, isInitialized: Boolean): Class[_] = {
    Class.forName(className, isInitialized, getClassLoader())
  }

  def getClassSet(packageName: String): Set[Class[_]] = {
    val classSet = mutable.Set.empty[Class[_]]
    val urls = getClassLoader().getResources(packageName.replace(".", "/"))
    while (urls.hasMoreElements) {
      val url = urls.nextElement()
      val classes = url.getProtocol match {
        case "file" => {
          val filePath = URLDecoder.decode(url.getFile, "UTF-8")
          classSet ++= parseFIle(new File(filePath), packageName)
        }
        case "jar" => {
          //FIXME 在package中会存在jar吗？
          val entries = url.openConnection().asInstanceOf[JarURLConnection].getJarFile.entries()
          val javClassSet = mutable.Set.empty[Class[_]]
          while (entries.hasMoreElements) {
            val entry = entries.nextElement()
            if (entry.getName.endsWith(".class")) {
              javClassSet.add(getClassLoader().loadClass(entry.getName))
            }
          }
          classSet ++= javClassSet
        }
        case other => {
          logger.warn(s"w未识别的文件格式:$other")
          Nil
        }
      }
      classSet ++= classes
    }
    Set(classSet.toSeq: _*)
  }


  private def parseFIle(file: File, packageName: String): List[Class[_]] = {
    file.listFiles().toList.flatMap(file => {
      file.isDirectory match {
        case true => {
          val newPackageName = packageName + "." + file.getName
          parseFIle(file, newPackageName)
        }
        case false => {
          val fileName = file.getName
          val className = packageName + "." + fileName.substring(0, fileName.size - 6)
          List(getClassLoader().loadClass(className))
        }
      }
    })
  }

}