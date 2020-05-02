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

    fun addItem(image: Bitmap) {
        items.add(image)
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

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PictureHolder, position: Int) {
        holder.serialNumber.text = (position + 1).toString()
        holder.picture.setImageBitmap(items[position])
    }

    override fun onViewRecycled(holder: PictureHolder) {
    }

    inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val serialNumber = itemView.findViewById<TextView>(R.id.serial_number)
        internal val picture = itemView.findViewById<ImageView>(R.id.picture)
    }

}