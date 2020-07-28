package bonch.dev.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.*


class RestClient {

    private val service: APIService

    init {
        val interceptor = Interceptor { chain ->
            Response.Builder()
                .code(600)
                .body(ResponseBody.create(null, ""))
                .protocol(Protocol.HTTP_2)
                .message(chain.proceed(chain.request()).header("Location").toString())
                .request(chain.request())
                .build() }
        val client = OkHttpClient.Builder()
        client.followRedirects(true)
        client.addNetworkInterceptor(interceptor)
        val httpClient = client.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
        service = retrofit.create<APIService>(APIService::class.java)
    }

    companion object {
        const val BASE_URL = "https://picsum.photos/"

        private val instance = RestClient()

        fun instance(): APIService {
            return instance.service
        }
    }
}