package `fun`.gladkikh.fastpallet5.network

import `fun`.gladkikh.fastpallet5.App
import `fun`.gladkikh.fastpallet5.Constants
import `fun`.gladkikh.fastpallet5.network.util.intity.ReqestObj
import `fun`.gladkikh.fastpallet5.network.util.intity.ResponseObj
import `fun`.gladkikh.fastpallet5.network.util.AutorithationUtil
import `fun`.gladkikh.fastpallet5.network.util.LogJSONInterceptor
import android.annotation.SuppressLint
import com.gladkikh.netreqest.entity.model.ResponseModel
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 *The interface which provides methods to get result of webservices
 */
object ApiFactory {

    private val gson =
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

    private val configInterceptor = Interceptor {
        val configUrl = it.request().url()
            .newBuilder()
            .addQueryParameter("uid", Constants.UID)
            .addQueryParameter("platform", "Android")
            .addQueryParameter("app_version", Constants.APP_VERSION)
            .addQueryParameter("version_os", Constants.OS_VERSION)
            .build()

        val configRequest = it.request()
            .newBuilder()
            .url(configUrl)
            .build()

        it.proceed(configRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authClient = OkHttpClient().newBuilder()
        .addInterceptor(configInterceptor)
        .addInterceptor(LogJSONInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(App.settingsRepository.settings.host ?: "")
            .client(authClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val api: Api = retrofit.create(Api::class.java)


    interface Api {
        @POST("host")
        fun getDataFromServer(
            @Header("Authorization") auth: String,
            @Body sendParamJson: ReqestModel
        ): Single<Response<ResponseModel>>
    }


    @SuppressLint("CheckResult")
    fun <T : ResponseObj> request(
        command: String,
        objRequest: ReqestObj,
        classResponse: Class<T>
    ): Single<ResponseObj> {

        val requestModel = ReqestModel(
            command = command,
            strDataIn = gson.toJson(objRequest)
        )


        return Single.just(App.settingsRepository.settings)
            .map {
                //Проверка на код тсд
                if (it.code.isNullOrEmpty()) {
                    throw Throwable("Не заполнен код ТСД")
                }
                return@map it
            }
            .map {
                //Строка авторизации
                return@map AutorithationUtil.getStringAutorization(
                    App.settingsRepository.settings.login,
                    App.settingsRepository.settings.pass
                )
            }.flatMap {
                api.getDataFromServer(
                    auth = it,
                    sendParamJson = requestModel
                )
            }
            .flatMap {
                when {
                    !it.isSuccessful -> Single.error<Throwable>(Throwable(it.errorBody().toString()))
                    else -> Single.just(it.body())
                }
            }.flatMap {
                val responseModel = it as ResponseModel

                when {
                    !(responseModel.success ?: true) -> Single.error<Throwable>(
                        Throwable(
                            responseModel.messError
                        )
                    )
                    else -> Single.just(responseModel)
                }
            }.map {
                gson.fromJson((it as ResponseModel).response ?: "", classResponse)
            }
    }
}