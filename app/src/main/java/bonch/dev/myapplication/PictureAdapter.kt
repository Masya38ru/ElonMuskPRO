package bonch.dev.myapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView


class PictureAdapter :
    RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    private val items = listOf(
        "https://img0.liveinternet.ru/images/attach/c/1/58/895/58895836_0_2a883_8933efd9_XL.jpg",
        "https://cs.pikabu.ru/post_img/2013/08/08/5/1375944655_592971946.jpg",
        "https://i.pinimg.com/736x/17/a3/a8/17a3a8381771953e5df17a6ef90cdeb3--middle-fingers-funny-pets.jpg",
        "https://img-hw.xvideos.com/videos/profiles/profthumb/84/2d/26/brian808/profile_1_big.jpg",
        "https://www.proza.ru/pics/2013/07/11/1100.jpg",
        "https://i.mycdn.me/i?r=AzEPZsRbOZEKgBhR0XGMT1RkXIkzoSU89xL0yGqn8o8poaaKTM5SRkZCeTgDn6uOyic",
        "https://www.caption.me/images/flickr/263331.jpg",
        "https://pics.me.me/fuck-you-cat-7834808.png",
        "https://i02.fotocdn.net/s108/2811661b2a784b71/public_pin_m/2395182812.jpg",
        "https://i.pinimg.com/736x/2b/9a/1a/2b9a1a8becf3bfeaee73bf87ddef736a.jpg"
    )

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

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.serialNumber.text = (position + 1).toString()
        DownloadImageTask(holder.picture).execute(items[position])
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumber = itemView.findViewById<TextView>(R.id.serial_number)
        internal val picture = itemView.findViewById<ImageView>(R.id.picture)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DownloadImageTask(internal var bmImage: ImageView) :
        AsyncTask<String, Void, Bitmap>() {

        @SuppressLint("LongLogTag")
        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Ошибка передачи изображения", e.message)
                e.printStackTrace()
            }

            return mIcon11
        }


        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
        }
    }
}