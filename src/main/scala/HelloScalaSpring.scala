import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

/**
  *
  * Created by PerkinsZhu on 7/1/18 9:14 PM
  *
  **/
class HelloScalaSpring extends HttpServlet {

  override def doGet(req: HttpServletRequest
                     , res: HttpServletResponse
                    ) {

    res.setContentType("text/html")
    res.setCharacterEncoding("UTF-8")

    val responseBody: String =
      """<html>
        |  <body>
        |    <h1>HelloJava, world!</h1>
        |  </body>
        |</html>""".stripMargin
    res.getWriter.write(responseBody)
  }
}
