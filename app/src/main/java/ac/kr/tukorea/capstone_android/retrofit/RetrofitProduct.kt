package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.adapter.ProductAdapter
import ac.kr.tukorea.capstone_android.data.ProductList
import ac.kr.tukorea.capstone_android.data.ProductListResponseBody
import ac.kr.tukorea.capstone_android.databinding.ActivityMainBinding
import ac.kr.tukorea.capstone_android.databinding.ActivityRegisterBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentMainBinding
import ac.kr.tukorea.capstone_android.util.App
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitProduct{
    private val service = RetrofitAPI.productService

    fun getProductList(name : String?, filter : String?, page : Int?, category: Long, binding: FragmentMainBinding) {
            service.getProductList(token = App.prefs.getString("access_token", ""), name, page, filter, category).enqueue(object: Callback<ProductListResponseBody>{
                override fun onResponse(
                    call: Call<ProductListResponseBody>,
                    response: Response<ProductListResponseBody>,
                ) {
                    if(response.isSuccessful){
                        val arrayList : ArrayList<ProductList> = response.body()!!.message.productList as ArrayList<ProductList>
                        val adapter = ProductAdapter(arrayList, binding.root.context)

                        binding.apply {
                            productRecyclerView.adapter = adapter
                            productRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                                    super.onScrolled(recyclerView, dx, dy)
                                }
                            })

                            adapter.setOnItemClickListener(object : ProductAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {

                                }
                            })
                        }


                    }else{
                        val retrofitRefresh = RetrofitRefresh()
                        retrofitRefresh.refreshToken()

                        getProductList(name, filter, page, category, binding)
                    }
                }

                override fun onFailure(call: Call<ProductListResponseBody>, t: Throwable) {

                }
            })
    }
}