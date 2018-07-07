package comscala.classes

import com.perkinszhu.classes.ClassUtil
import org.junit.Test

/**
  * Created by PerkinsZhu on 2018/7/7 13:42
  **/
class ClassUtilTest {

  @Test
  def testGetClassSet(): Unit = {
    val classes = ClassUtil.getClassSet("com.user")
    println(classes)

  }

}
