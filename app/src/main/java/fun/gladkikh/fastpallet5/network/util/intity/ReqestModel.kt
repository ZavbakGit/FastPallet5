package `fun`.gladkikh.fastpallet5.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReqestModel(
    @SerializedName("command")
    @Expose
    var command: String,
    @SerializedName("str_data_in")
    @Expose
    var strDataIn: String
)
