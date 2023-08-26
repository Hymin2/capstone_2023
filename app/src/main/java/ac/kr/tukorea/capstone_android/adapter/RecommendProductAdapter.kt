package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.PostInfo
import ac.kr.tukorea.capstone_android.data.ProductList
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target
import java.text.DecimalFormat

class RecommendProductAdapter (private var items: ArrayList<ProductList>, val context : Context) : RecyclerView.Adapter<RecommendProductAdapter.RecommenListViewHolder>() {
    private lateinit var mListener : RecommendProductAdapter.onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: RecommendProductAdapter.onItemClickListener) {
        mListener = listener
    }

    fun getItem(position: Int) : ProductList{
        return items[position]
    }

    class RecommenListViewHolder(itemView: View, listener: RecommendProductAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.recommend_image_view)
        val productName: TextView = itemView.findViewById(R.id.recommend_name_tv)
        val avgPrice: TextView = itemView.findViewById(R.id.recommend_price_tv)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommenListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_list_item, parent, false)
        return RecommenListViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: RecommenListViewHolder, position: Int) {
        val item = items[position]

        val glideUrl = GlideUrl(
            ServerInfo.SERVER_URL.url + ServerInfo.PRODUCT_IMAGE_URI.url + item.images[0]
        )
        Glide.with(context).load(glideUrl)
            .override(100)
            .into(holder.productImage)

        holder.productName.text = item.productName
        holder.avgPrice.text = toLongFormat(item.averagePrice)
    }

    private fun toLongFormat(price: Int): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}