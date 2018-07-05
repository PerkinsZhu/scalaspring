package com.user

import com.perkinszhu.annotation.item.Service
import com.user.beans.Person
import javax.inject.Singleton

/**
  *
  * Created by PerkinsZhu on 7/3/18 10:07 PM
  *
  **/
@Service
@deprecated
class PersonService {

  @Singleton
  def show(person: Person): Unit = {
    println(person)
  }

  override def toString: String = {
     "I am PersonService"
  }
}
