package com.perkinszhu.classes

import java.io.File
import java.net.{JarURLConnection, URLDecoder}

import com.perkinszhu.annotation.item.{Action, Controller, Inject, Service}
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.reflect.macros.whitebox.{Context => c}
import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

/**
  * Created by PerkinsZhu on 2018/7/7 11:26
  **/


class ClassHelper(packagePath: String) {
  val logger = LoggerFactory.getLogger(this.getClass)
  val typeTagSet = TypeTagUtil.getClassSet(packagePath)

  val serviceTypeTagSet = mutable.Set.empty[Type]
  val controllerTypeTagSet = mutable.Set.empty[Type]
  val actionMap = mutable.HashMap.empty[Request, Handle]
  val injectTypeTagSet = mutable.Set.empty[Type]

  def parseHandler(tag: Type): Unit = {
    tag.decls.foreach(symbol => {
      if (symbol.isMethod) {
        val action = symbol.asMethod.annotations.find(_.tree.tpe =:= typeOf[Action])
        if (action.nonEmpty) {
          val tree = action.get.tree
          val Literal(Constant(requestPath: String)) :: Literal(Constant(requestMethod: String)) :: Nil = tree.children.tail
          actionMap.+=((Request(requestPath, requestMethod), Handle(tag, symbol)))
        }
      }
    })
  }

  def getCustomAnnotationData(tree: Tree) = {
    val Apply(_, Literal(Constant(path: String)) :: Literal(Constant(method: String)) :: Nil) = tree
    new Action(path, method)
  }

  val init = {
    val serviceType = typeOf[Service]
    val controllerType = typeOf[Controller]
    val actionType = typeOf[Action]
    val injectType = typeOf[Inject]
    typeTagSet.foreach(tag => {
      tag.typeSymbol.asClass.annotations.foreach(anno => {
        anno.tree.tpe match {
          case `serviceType` =>
            serviceTypeTagSet += tag
          case `controllerType` => {
            controllerTypeTagSet += tag
            parseHandler(tag)
          }
          case `injectType` =>
            injectTypeTagSet += tag
          case other => logger.warn(s"未知注解[$other]类型")
        }
      })
    })
  }


}

object TypeTagUtil {

  val logger = LoggerFactory.getLogger(this.getClass)
  val classLoader = getClass.getClassLoader
  val typeTagBuilder = new TypeTagBuilder

  def getClassSet(packageName: String): Set[Type] = {
    val classSet = mutable.Set.empty[Type]
    val urls = classLoader.getResources(packageName.replace(".", "/"))
    while (urls.hasMoreElements) {
      val url = urls.nextElement()
      val classes = url.getProtocol match {
        case "file" => {
          val filePath = URLDecoder.decode(url.getFile, "UTF-8")
          classSet ++= parseFIle(new File(filePath), packageName)
        }
        case "jar" => {
          //FIXME 在package中会存在jar吗？ 这里未实现对jar的解析
          val entries = url.openConnection().asInstanceOf[JarURLConnection].getJarFile.entries()
          val javClassSet = mutable.Set.empty[Type]
          while (entries.hasMoreElements) {
            val entry = entries.nextElement()
            if (entry.getName.endsWith(".class")) {
              //javClassSet.add(typeTagBuilder (entry.getName))
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


  private def parseFIle(file: File, packageName: String): List[Type] = {
    file.listFiles().toList.flatMap(file => {
      file.isDirectory match {
        case true => {
          val newPackageName = packageName + "." + file.getName
          parseFIle(file, newPackageName)
        }
        case false => {
          val fileName = file.getName
          val className = packageName + "." + fileName.substring(0, fileName.size - 6)
          List(typeTagBuilder.createTypeTag(className))
          Nil
        }
      }
    })
  }

}