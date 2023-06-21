package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ProductList
import ac.kr.tukorea.capstone_android.util.ServerInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import java.text.DecimalFormat

class ProductAdapter(private val productList : ArrayList<ProductList>, val context : Context)
    : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    fun getItem(position: Int) : ProductList{
        return productList[position]
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        val productName = itemView.findViewById<TextView>(R.id.searchResultProductName_Text)!!
        val productPrice = itemView.findViewById<TextView>(R.id.searchResultProductPrice_Text)!!
        val productImage = itemView.findViewById<ImageView>(R.id.searchResultProduct_Image)!!

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.searchresult_list_item,
            parent, false)

        return MyViewHolder(itemView, mListener)
    }

    private fun toLongFormat(price: Int): String {
        val formatter = DecimalFormat("###,###")
        return formatter.format(price) + "원"
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = productList[position]

        holder.productName.text = item.productName
        holder.productPrice.text = toLongFormat(item.averagePrice)

        val glideUrl = GlideUrl(
            ServerInfo.SERVER_URL.url + ServerInfo.PRODUCT_IMAGE_URI.url + item.images[0]
        )

        Glide.with(context).load(glideUrl)
            .override(75)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size;
    }
}