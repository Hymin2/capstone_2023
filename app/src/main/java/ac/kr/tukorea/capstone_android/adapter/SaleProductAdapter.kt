package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.OthersProfileActivity
import ac.kr.tukorea.capstone_android.data.FeedList
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SaleProductAdapter(private var saleProductList: ArrayList<FeedList>) :
    RecyclerView.Adapter<SaleProductAdapter.saleProductViewHolder>() {

    class saleProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val saleProductProfileImage: ImageView =
            itemView.findViewById(R.id.feedList_userProfileImage)
        val saleProductUserNickName: TextView =
            itemView.findViewById(R.id.feedList_userNickName)
        val saleProductProductImage: ImageView =
            itemView.findViewById(R.id.feedList_productImage)
        val saleProductProductModel: TextView =
            itemView.findViewById(R.id.feedList_title)
        val saleProductProductPrice: TextView =
            itemView.findViewById(R.id.feedList_productPrice)
        val saleProductProductMain: TextView =
            itemView.findViewById(R.id.feedList_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): saleProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_list_item, parent, false)
        return saleProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: saleProductViewHolder, position: Int) {
        val saleProduct = saleProductList[position]
        holder.saleProductProfileImage.setImageResource(saleProduct.feedProfileImage)
        holder.saleProductUserNickName.text = saleProduct.feedUserNickName
        holder.saleProductProductImage.setImageResource(saleProduct.feedProductImage)
        holder.saleProductProductModel.text = saleProduct.feedProductModel
        holder.saleProductProductPrice.text = "${saleProduct.feedProductPrice} 원"
        holder.saleProductProductMain.text = saleProduct.feedProductMain

        holder.saleProductUserNickName.setOnClickListener {
            val clickedPosition = holder.adapterPosition
            val clickedItem = saleProductList[clickedPosition]
            val intent =
                Intent(holder.itemView.context, OthersProfileActivity::class.java)
            intent.putExtra("NickName", clickedItem.feedUserNickName)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return saleProductList.size
    }

    fun updateList(list: ArrayList<FeedList>) {
        saleProductList = list
        notifyDataSetChanged()
    }
}
