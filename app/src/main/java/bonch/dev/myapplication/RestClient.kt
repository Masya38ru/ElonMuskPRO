package bonch.dev.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RestClient {

    private val service: APIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create<APIService>(APIService::class.java)
    }

    companion object {
        val BASE_URL = "https://picsum.photos/"

        private val instance = RestClient()

        fun instance(): APIService {
            return instance.service
        }
    }
}