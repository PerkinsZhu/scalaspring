package com.perkinszhu.classes

import org.apache.commons.lang3.builder.HashCodeBuilder

import scala.reflect.runtime.universe._


/**
  * Created by PerkinsZhu on 2018/7/7 17:51
  **/

case class Request(requestPath: String, requestMethod: String) {
  override def hashCode(): Int = {
    HashCodeBuilder.reflectionHashCode(requestMethod + ":" + requestPath)
  }
}

case class Handle(controllerTag: Type, actionMethod: Symbol)

