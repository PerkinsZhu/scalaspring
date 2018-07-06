package comscala.beans

/**
  * Created by PerkinsZhu on 2018/7/6 10:04
  **/
case class Person(name: String, age: Int)

class Student private(name: String, age: Int) extends Person(name, age) {
  def show(): String = {
    "Hello"
  }

  def run(): Unit = {

  }

  def runWithMe(me: String): Unit = {

  }

}

class Teacher(name: String, age: Int, isMale: Boolean, salary: Float, student: Student) extends Person(name, age) {

  override def toString = s"Teacher($name,$age,$isMale,$salary,$student)"
}
