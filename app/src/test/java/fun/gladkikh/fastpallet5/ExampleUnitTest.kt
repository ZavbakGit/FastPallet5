package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.common.toSimpleString
import `fun`.gladkikh.fastpallet5.ui.base.zip2
import androidx.lifecycle.MutableLiveData
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    data class My(val name:String,val list:List<String>)


    @Test
    fun addition_isCorrect() {

        (0..3).map {
            My(it.toString(),(0..5).map {
                it.toString()
            })
        }.flatMap {
            it.list
        }



        var s:String? = null


        //println(s1)




//        var k: String? = "1"
//
//        k?.takeIf {
//            it == "1"
//        }?.run {
//            println(this)
//        }


        //println(s)

//        s.takeUnless {
//            s != null
//        }.let {
//            println(it)
//        }

        //
        assertEquals(4, 2 + 2)
    }
}
