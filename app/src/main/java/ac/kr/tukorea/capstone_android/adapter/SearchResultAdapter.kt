package ac.kr.tukorea.capstone_android.adapter

import ac.kr.tukorea.capstone_android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SearchResultAdapter(private val SearchResultList:ArrayList<ac.kr.tukorea.capstone_android.data.SearchResult>) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val searchResultProductImage : ImageView = itemView.findViewById(R.id.searchResultProduct_Image)
        val searchResultProductName : TextView = itemView.findViewById(R.id.searchResultProductName_Text)
        val searchResultProdictPrice : TextView = itemView.findViewById(R.id.searchResultProductPrice_Text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.searchresult_list_item, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult = SearchResultList[position]
        holder.searchResultProductImage.setImageResource(searchResult.searchImage)
        holder.searchResultProductName.text = searchResult.searchProductName
        holder.searchResultProdictPrice.text = searchResult.searchProductPrice.toString()
    }

    override fun getItemCount(): Int {
        return SearchResultList.size
    }

}