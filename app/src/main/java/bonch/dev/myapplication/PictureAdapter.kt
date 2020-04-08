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


class PictureAdapter(var items: List<PictureItem>, val callback: Callback) :
    RecyclerView.Adapter<PictureAdapter.PictureHolder>() {
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
        holder.bind(items[position])
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val serialNumber = itemView.findViewById<TextView>(R.id.serial_number)
        private val picture = itemView.findViewById<ImageView>(R.id.picture)
        fun bind(item: PictureItem) {
            serialNumber.text = item.serial_number.toString()
            DownloadImageTask(picture)
                .execute(item.link)

        }
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
    interface Callback {
        fun onItemClicked(item: PictureItem)
    }
}