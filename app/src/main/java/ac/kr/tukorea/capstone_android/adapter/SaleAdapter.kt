package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.data.Products
import ac.kr.tukorea.capstone_android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class SaleAdapter(private val productsList : ArrayList<Products>) :
    RecyclerView.Adapter<SaleAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        mListener = listener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_list_item,
            parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productsList[position]
        holder.productImage.setImageResource(currentItem.saleImage)
        holder.title.text = currentItem.saleTitle
        holder.price.text = currentItem.salePrice.toString()
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView)
    {
        val productImage : ShapeableImageView = itemView.findViewById(R.id.product_image)
        val title : TextView = itemView.findViewById(R.id.title)
        val price : TextView = itemView.findViewById(R.id.price)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}