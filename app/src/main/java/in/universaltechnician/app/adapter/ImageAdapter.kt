package `in`.universaltechnician.app.adapter

import `in`.universaltechnician.app.R
import `in`.universaltechnician.app.model.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    private val list: ArrayList<Image>,
    private val onRemoveClick: (model: Image) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val model = list[position]
        holder.image.setImageBitmap(model.url)
//        holder.image.setImageURI(model.url)
        holder.btnRemove.setOnClickListener {
            onRemoveClick(model)
        }
    }

    override fun getItemCount(): Int = list.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.iv_photo)
        val btnRemove = itemView.findViewById<ImageView>(R.id.btn_remove)
    }
}