package com.gladkikh.netreqest.entity.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Replay Auth
 */

class ResponseModel {
    @SerializedName("time_reseption")
    @Expose
    var timeReseption: String? = null

    @SerializedName("command")
    @Expose
    var command: String? = null

    @SerializedName("time_response")
    @Expose
    var timeResponse: String? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("mess_error")
    @Expose
    var messError: String? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    override fun toString(): String {
        return "ResponseModel{" +
                "timeReseption='" + timeReseption + '\''.toString() +
                ", command='" + command + '\''.toString() +
                ", timeResponse='" + timeResponse + '\''.toString() +
                ", response='" + response + '\''.toString() +
                ", messError='" + messError + '\''.toString() +
                ", success=" + success +
                '}'.toString()
    }
}
