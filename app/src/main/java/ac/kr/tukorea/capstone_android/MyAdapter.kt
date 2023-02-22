package ac.kr.tukorea.capstone_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import org.w3c.dom.Text

class MyAdapter(private val productsList : ArrayList<Products>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
        parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productsList[position]
        holder.productImage.setImageResource(currentItem.productImage)
        holder.title.text = currentItem.title
        holder.price.text = currentItem.price.toString()
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val productImage : ShapeableImageView = itemView.findViewById(R.id.product_image)
        val title : TextView = itemView.findViewById(R.id.title)
        val price : TextView = itemView.findViewById(R.id.price)
    }

}