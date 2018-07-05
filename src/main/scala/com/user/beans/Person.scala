package com.user.beans

/**
  *
  * Created by PerkinsZhu on 7/3/18 10:12 PM
  *
  **/

case class Person(name: String, age: Int) {
  
  override def toString: String = {
    s"i am person:name:${name}, age:${age}"
  }


  def hello(): Unit = {
    println("====hello person=======")
  }
}
