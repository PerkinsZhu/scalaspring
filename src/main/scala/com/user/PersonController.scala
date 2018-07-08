package com.user

import com.perkinszhu.annotation.item.{Action, Controller}

/**
  *
  * Created by PerkinsZhu on 7/8/18 9:10 AM
  *
  **/
@Controller("person")
class PersonController {


  @Action("addPerson",method = "POST")
  def addPerson(): Unit = {
    println("----addPerson-----")
  }


}
