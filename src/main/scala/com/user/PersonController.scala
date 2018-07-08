package com.user

import com.perkinszhu.annotation.item.{Action, Controller}
import org.slf4j.LoggerFactory

/**
  *
  * Created by PerkinsZhu on 7/8/18 9:10 AM
  *
  **/
@Controller("person")
class PersonController {
  val logger = LoggerFactory.getLogger(this.getClass)

  @Action("/addPerson",method = "GET")
  def addPerson(): Int = {
    logger.info("正在查询person")
    190
  }


}
