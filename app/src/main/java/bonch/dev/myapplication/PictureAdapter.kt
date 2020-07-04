package bonch.dev.myapplication

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PictureAdapter :
    RecyclerView.Adapter<PictureAdapter.PictureHolder>() {

    private val items = arrayListOf<Bitmap>()
    private val itemUrls = arrayListOf<String>()

    companion object {
        const val MAX_SIZE = 500
        const val MIN_SIZE = 10
    }

    fun addItem(image: Bitmap, url: String) {
        itemUrls.add(url)
        if (items.size >= MAX_SIZE)
            items[itemCount % MAX_SIZE] = image
        else items.add(image)
    }

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

    override fun getItemCount() = itemUrls.size

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        val index = position % MAX_SIZE
        holder.serialNumber.text = (position + 1).toString()
        holder.picture.setImageBitmap(items[index])
    }

    override fun onViewRecycled(holder: PictureHolder) {
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumber = itemView.findViewById<TextView>(R.id.serial_number)
        internal val picture = itemView.findViewById<ImageView>(R.id.picture)
    }

}