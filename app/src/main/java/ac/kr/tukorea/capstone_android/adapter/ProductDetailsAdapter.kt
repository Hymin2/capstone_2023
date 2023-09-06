package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.ProductDetails
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductDetailsAdapter(private var items : ArrayList<ProductDetails>): RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailTitle = itemView.findViewById<TextView>(R.id.product_detail_title_textView)!!
        val detailValue = itemView.findViewById<TextView>(R.id.product_detail_value_textView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_detail_list_item,
            parent, false)
        return ProductDetailsAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.detailTitle.text = item.detailName
        holder.detailValue.text = item.detailContent
    }

    override fun getItemCount(): Int {
        return items.size
    }
}