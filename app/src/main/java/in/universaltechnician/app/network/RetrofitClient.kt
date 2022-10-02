package `in`.universaltechnician.app.network

import `in`.universaltechnician.app.utils.Const.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().apply{ level = HttpLoggingInterceptor.Level.BODY })
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .build()
    private var retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getClient(): APIService {
        return retrofit.create(APIService::class.java)

    }


}