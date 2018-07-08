package com.perkinszhu.classes

import scala.reflect.runtime.universe._


/**
  * Created by PerkinsZhu on 2018/7/7 17:51
  **/

case class Request(requestPath: String, requestMethod: String)

case class Handle(controllerTag: Type, actionMethod: Symbol)

