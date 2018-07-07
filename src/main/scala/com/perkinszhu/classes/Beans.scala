package com.perkinszhu.classes

import java.lang.reflect.Method
import javassist.bytecode.stackmap.TypeTag

/**
  * Created by PerkinsZhu on 2018/7/7 17:51
  **/

case class Request(method: String, path: String)

case class Handle(controllerTypeTag: TypeTag[_], actionMethod: Method)

