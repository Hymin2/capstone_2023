package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import android.content.Context
import android.graphics.Bitmap
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.MediaType
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MultiImageAdapter(private val items: ArrayList<File?>, val context: Context) :
    RecyclerView.Adapter<MultiImageAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var image = v.findViewById<ImageView>(R.id.multi_image)

        fun bind(listener: View.OnClickListener, item: File?) {
            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultiImageAdapter.ViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.multi_image_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: MultiImageAdapter.ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            Glide.with(context)
                .load(item)
                .override(500, 500)
                .into(holder.image)
        } else {
            // 파일이 null인 경우 기본 이미지 또는 오류 처리를 수행하세요.
            // 예: holder.image.setImageResource(R.drawable.default_image)
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addImage(file: File) {
        items.add(file)
        notifyDataSetChanged()
    }

    fun clearImages() {
        items.clear()
        notifyDataSetChanged()
    }

    fun getImages(): List<MultipartBody.Part> {
        val images: MutableList<MultipartBody.Part> = ArrayList()
        items.forEachIndexed { index, file ->
            file?.let {
                val requestFile = it.asRequestBody("image/*".toMediaType())
                val body = MultipartBody.Part.createFormData("image_$index", it.name, requestFile)
                images.add(body)
            }
        }
        return images
    }
}