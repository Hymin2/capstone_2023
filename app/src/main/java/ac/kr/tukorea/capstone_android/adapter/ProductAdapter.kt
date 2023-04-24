package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ProductList
import ac.kr.tukorea.capstone_android.util.App
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.Target
import com.github.mikephil.charting.utils.Utils.init

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = productList[position]

        holder.productName.text = item.productName

        val glideUrl = GlideUrl(
            item.path.replace("localhost", "10.0.2.2"),
            LazyHeaders.Builder()
                .addHeader("Authorization", App.prefs.getString("access_token", ""))
                .build()
        )

        Glide.with(context).load(glideUrl)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size;
    }
}