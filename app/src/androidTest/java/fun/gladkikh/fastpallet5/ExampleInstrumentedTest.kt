package `fun`.gladkikh.fastpallet5

import `fun`.gladkikh.fastpallet5.network.ApiFactory
import `fun`.gladkikh.fastpallet5.network.ReqestModel
import `fun`.gladkikh.fastpallet5.network.intity.GetListDocsRequest
import `fun`.gladkikh.fastpallet5.network.intity.ListDocResponse
import `fun`.gladkikh.fastpallet5.ui.base.zip2
import androidx.lifecycle.MutableLiveData
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.gson.GsonBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    @Test
    fun testZip(){


    }

    @Test
    fun useAppContext() {

//        val gson =
//            GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//                .create()
//
//
//        val getListDocsRequest = GetListDocsRequest(codeTSD = "333")
//
//
//        val requestModel = ReqestModel(
//            command = "command_get_doc",
//            strDataIn = gson.toJson(getListDocsRequest)
//        )
//
//        ApiFactory.request(
//            command = "command_get_doc",
//            username = "Администратор",
//            pass = "",
//            objReqest = getListDocsRequest,
//            classResponse = ListDocResponse::class.java
//        ).subscribe({
//            println(it as ListDocResponse)
//        }, {
//            println(it)
//        })
//
//        val appContext = InstrumentationRegistry.getTargetContext()
        //assertEquals("fun.gladkikh.fastpallet5", appContext.packageName)
    }
}
