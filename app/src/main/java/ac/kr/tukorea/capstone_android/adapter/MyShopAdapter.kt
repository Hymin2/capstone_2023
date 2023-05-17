package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class MyShopAdapter(private val MyShopList:ArrayList<Int>) :
    RecyclerView.Adapter<MyShopAdapter.MyShopViewHolder>() {

    class MyShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val myShopImage : ImageView = itemView.findViewById(R.id.myShop_recyclerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myshop__list_item, parent, false)
        return MyShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyShopViewHolder, position: Int) {
        val myShop = MyShopList[position]
        holder.myShopImage.setImageResource(myShop)
    }

    override fun getItemCount(): Int {
        return MyShopList.size
    }

}