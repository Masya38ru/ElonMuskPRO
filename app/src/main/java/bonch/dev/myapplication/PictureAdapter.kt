package bonch.dev.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PictureAdapter :
    RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    private val items = arrayOfNulls<Bitmap>(10)

    private val BASE_URL = "https://picsum.photos/"
    private var retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private var apiService: APIService = retrofit.create(APIService::class.java)
    private var call = apiService.loadImage()

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PictureHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.picture_item,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    @SuppressLint("LongLogTag")
    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.serialNumber.text = (position + 1).toString()
        if (items[position] != null) {
            holder.picture.setImageBitmap(items[position])
        } else {
            call.clone().enqueue(object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i("Ошибка выполнения запроса", t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    items[position] = BitmapFactory.decodeStream(response.body()?.byteStream())
                    holder.picture.setImageBitmap(items[position])
                }
            })
        }
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumber = itemView.findViewById<TextView>(R.id.serial_number)
        internal val picture = itemView.findViewById<ImageView>(R.id.picture)
    }

}