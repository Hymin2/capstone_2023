package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.PostInfo
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.Target


class MyShopAdapter(private val MyShopList:ArrayList<PostInfo>, val context : Context) :
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

        val glideUrl = GlideUrl(
            myShop.postImages[0].replace("localhost", "10.0.2.2")
        )

        Glide.with(context).load(glideUrl)
            .override(Target.SIZE_ORIGINAL)
            .into(holder.myShopImage)
    }

    override fun getItemCount(): Int {
        return MyShopList.size
    }

}