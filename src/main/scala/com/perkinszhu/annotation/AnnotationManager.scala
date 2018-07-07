package com.perkinszhu.annotation

import scala.collection.mutable

/**
  *
  * Created by PerkinsZhu on 7/3/18 10:02 PM
  *
  **/
class AnnotationManager {

  val scanPath = ""
  val pathMap = mutable.HashMap.empty[String, AnyRef]




}

case class RequestInfo(path: String, method: String, controller: AnyRef, handleMethod: String)