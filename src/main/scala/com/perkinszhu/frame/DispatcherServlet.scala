package com.perkinszhu.frame

import com.perkinszhu.classes.{ClassHelper, Handle, Request}
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import org.slf4j.LoggerFactory

import scala.reflect.runtime.universe._

/**
  *
  * Created by PerkinsZhu on 7/8/18 4:42 PM
  *
  **/
class DispatcherServlet extends HttpServlet {
  val logger = LoggerFactory.getLogger(this.getClass)
  val classHelper = new ClassHelper("com.user")

  override def init(): Unit = {
    logger.info("--init----")
    super.init()
  }

  def dealTask(handle: Handle): Option[Any] = {
    val tag = handle.controllerTag
    val method = handle.actionMethod.asMethod
    val mirror = runtimeMirror(handle.controllerTag.getClass.getClassLoader)
    val consM = tag.decl(termNames.CONSTRUCTOR).asMethod
    val init = mirror.reflectClass(tag.typeSymbol.asClass).reflectConstructor(consM)
    val controller = init()
    val instanceMirror = mirror.reflect(controller)
    val addPerson = instanceMirror.reflectMethod(method)
    Some(addPerson())
  }

  override def service(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val requestMethod = req.getMethod.toUpperCase()
    val requestPath = req.getPathInfo
    val handle = classHelper.actionMap.get(Request(requestPath, requestMethod))
    if (!handle.isEmpty) {
      //反射被调用处理逻辑
      val result = dealTask(handle.get)
      logger.info("获取到action结果："+result.toString)
      val out = resp.getWriter
      out.write("this is result view")
      out.flush()
      out.close()
    } else {
      logger.warn(s"未找到请求路径[${requestPath}]")
      //TODO 返回404
      val out = resp.getWriter
      out.write("404 未找到该请求")
      out.flush()
      out.close()
    }
  }

}
