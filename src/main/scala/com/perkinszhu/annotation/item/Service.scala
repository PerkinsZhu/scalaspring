package com.perkinszhu.annotation.item

import scala.annotation.StaticAnnotation
import scala.tools.nsc.io.Path

/**
  *
  * Created by PerkinsZhu on 7/3/18 10:09 PM
  *
  **/
class Service(name: String = "") extends StaticAnnotation

class Controller(name: String = "") extends StaticAnnotation

class Action(path: String = "", method: String = "GET") extends StaticAnnotation

class Inject(name: String = "") extends StaticAnnotation

class Request(path: String, method: String = "GET") extends StaticAnnotation
