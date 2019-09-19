package `fun`.gladkikh.fastpallet5.network.intity


import `fun`.gladkikh.fastpallet5.network.util.intity.ReqestObj
import `fun`.gladkikh.fastpallet5.network.util.intity.ResponseObj
import com.google.gson.annotations.SerializedName
import java.util.*

data class GetListDocsRequest(
    var date: Date = Date(),
    @SerializedName("code_tsd")
    var codeTSD: String
) : ReqestObj


data class ListDocResponse(
    var codeTsd: String?,
    var listDocuments: List<DocResponse>?
) : ResponseObj


data class DocResponse (
    var guid: String,
    var type: String,
    var status: String,
    var number: String,
    var date: Date,
    var description: String,
    var listStringsProduct: List<StringProductResponse>?
)



