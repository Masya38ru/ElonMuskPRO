package bonch.dev.myapplication

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("1080/600")
    fun loadImage(): Call<ResponseBody>
}