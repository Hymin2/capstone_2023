package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ProductList
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target

class TopItemAdapter(private val productList : ArrayList<ProductList>, val context : Context)
    : RecyclerView.Adapter<TopItemAdapter.MyViewHolder>(){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopItemAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.searchresult_list_item,
            parent, false)

        return TopItemAdapter.MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: TopItemAdapter.MyViewHolder, position: Int) {
        val item = productList[position]

        holder.productName.text = item.productName
        holder.productPrice.text = item.averagePrice.toString() + "원"

        val glideUrl = GlideUrl(
            item.path.replace("localhost", "10.0.2.2")
        )

        Glide.with(context).load(glideUrl)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View, listener: TopItemAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView)
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
}