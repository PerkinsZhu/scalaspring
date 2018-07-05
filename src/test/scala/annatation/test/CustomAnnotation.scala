package annatation.test

import scala.annotation.StaticAnnotation
import scala.reflect.runtime.universe._

class CustomAnnotation(name: String, num: Int) extends StaticAnnotation {
  override def toString = s"Annotation args: name -> $name, num -> $num"
}

@CustomAnnotation("Annotation for Class", 2333)
class Test {
  @CustomAnnotation("Annotation for Member", 6666)
  val ff = ""
  def mm(ss: String, @CustomAnnotation("Annotation for Arg", 9999) arg: Int) = ""
}

object Main extends App {

  // 获取指定类型的注解信息，通过 Annotation.tree.tpe 获取注解的 Type 类型，以此进行筛选
  def getClassAnnotation[T: TypeTag, U: TypeTag] =
    symbolOf[T].annotations.find(_.tree.tpe =:= typeOf[U])

  // 通过字段名称获取指定类型的注解信息，注意查找字段名称时添加空格
  def getMemberAnnotation[T: TypeTag, U: TypeTag](memberName: String) =
    typeOf[T].decl(TermName(s"$memberName ")).annotations.find(_.tree.tpe =:= typeOf[U])

  // 通过方法名称和参数名称获取指定类型的注解信息
  def getArgAnnotation[T: TypeTag, U: TypeTag](methodName: String, argName: String) =
    typeOf[T].decl(TermName(methodName)).asMethod.paramLists.collect {
      case symbols => symbols.find(_.name == TermName(argName))
    }.headOption.fold(Option[Annotation](null))(_.get.annotations.find(_.tree.tpe =:= typeOf[U]))

  // 解析语法树，获取注解数据
  def getCustomAnnotationData(tree: Tree) = {
    val Apply(_, Literal(Constant(name: String)) :: Literal(Constant(num: Int)) :: Nil) = tree
    new CustomAnnotation(name, num)
  }

  getClassAnnotation[Test, CustomAnnotation].map(_.tree) foreach { classAnnotationTree =>
    val classAnnotation = getCustomAnnotationData(classAnnotationTree)
    println(classAnnotation)
  }

  getMemberAnnotation[Test, CustomAnnotation]("ff").map(_.tree) foreach { memberAnnotationTree =>
    val memberAnnotation = getCustomAnnotationData(memberAnnotationTree)
    println(memberAnnotation)
  }

  getArgAnnotation[Test, CustomAnnotation]("mm", "arg").map(_.tree) foreach { argAnnotationTree =>
    val argAnnotation = getCustomAnnotationData(argAnnotationTree)
    println(argAnnotation)
  }

}