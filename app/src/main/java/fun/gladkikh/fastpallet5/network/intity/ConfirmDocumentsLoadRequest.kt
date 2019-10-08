package `fun`.gladkikh.fastpallet5.network.intity


import `fun`.gladkikh.fastpallet5.network.util.intity.ReqestObj
import `fun`.gladkikh.fastpallet5.network.util.intity.ResponseObj
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class ConfirmDocumentsLoadRequest(codeTSD:String, list:List<DocConfirm>): ReqestObj {
    @SerializedName("date")
    @Expose
    var date: Date = Date()

    @SerializedName("code_tsd")
    @Expose
    var codeTSD: String = codeTSD

    @SerializedName("list_doc")
    @Expose
    var listDoc: List<DocConfirm> = list
}

data class DocConfirm(var guid:String,var type:String)



class ConfirmResponse:ResponseObj {
    @SerializedName("list_confirm")
    var listConfirm: List<ItemConfim> = listOf()
}

class ItemConfim{
    @SerializedName("guid")
    var guid: String? = null
    @SerializedName("status")
    var status: String? = null
}