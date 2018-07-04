import java.io.File
import java.net.{JarURLConnection, URLDecoder}

/**
  *
  * Created by PerkinsZhu on 7/3/18 10:35 PM
  *
  **/
object classParse {

  def main(args: Array[String]): Unit = {
    testParseClass()
  }

  //  @org.junit.Test
  def testParseClass(): Unit = {
    val packageName = "com.user"
    val packagePath = packageName.replace(".", "/")
    val urls = Thread.currentThread.getContextClassLoader.getResources(packagePath)
    while (urls.hasMoreElements) {
      val url = urls.nextElement()
      url.getProtocol match {
        case "jar" => {
          val jar = url.openConnection().asInstanceOf[JarURLConnection].getJarFile
          val element = jar.entries
          while (element.hasMoreElements) {
            val entry = element.nextElement()
            println(entry)
          }

        }
        case "file" => {
          val filePath = URLDecoder.decode(url.getFile, "UTF-8")
          parseFIle(new File(filePath), packageName)
        }
        case item: String => println(s"未处理类型:$item")
      }
    }
  }


  def parseFIle(file: File, packageName: String): Unit = {
    val files = file.listFiles()
    files.filter(_.isFile).foreach(file => {
      val fileName = file.getName
      val className = packageName + "." + fileName.substring(0, fileName.size - 6)
      val clazz = Thread.currentThread.getContextClassLoader.loadClass(className)
      clazz.getMethods.foreach(method =>{
//        println(method.getModifiers)
        println(clazz.getName + "-->"+method)
      })

//      println(clazz.newInstance())
    })
    files.filter(_.isDirectory).foreach(file => parseFIle(file, packageName + "." + file.getName))
  }


}
