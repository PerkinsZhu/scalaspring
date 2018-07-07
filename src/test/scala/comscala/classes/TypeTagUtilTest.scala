package comscala.classes

import com.perkinszhu.classes.TypeTagUtil
import org.junit.Test

/**
  * Created by PerkinsZhu on 2018/7/7 13:42
  **/
class TypeTagUtilTest {

  @Test
  def testGetClassSet(): Unit = {
    val result = TypeTagUtil.getClassSet("com.user")
    println(result)
  }

}