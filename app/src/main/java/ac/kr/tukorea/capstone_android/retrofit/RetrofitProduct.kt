package ac.kr.tukorea.capstone_android.retrofit

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.activity.DetailActivity
import ac.kr.tukorea.capstone_android.adapter.ProductAdapter
import ac.kr.tukorea.capstone_android.data.*
import ac.kr.tukorea.capstone_android.databinding.*
import ac.kr.tukorea.capstone_android.fragment.Main
import ac.kr.tukorea.capstone_android.fragment.graphWeek
import ac.kr.tukorea.capstone_android.util.App
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RetrofitProduct{
    private val service = RetrofitAPI.productService

    fun getProductList(name : String?, filter : String?, category: Long, binding: FragmentMainBinding, fragment: Main) {
            service.getProductList(token = App.prefs.getString("access_token", ""), name, filter, category).enqueue(object: Callback<ProductListResponseBody>{
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
                                    var intent = Intent(binding.root.context, DetailActivity::class.java)
                                    var item = adapter.getItem(position)

                                    intent.putExtra("productId", item.id)
                                    intent.putExtra("productName", item.productName)
                                    intent.putExtra("productPath", item.path)
                                    /*
                                    val graphWeekFragment = graphWeek()
                                    var bundle = Bundle()
                                    bundle.putLong("productId", item.id)
                                    val id = item.id
                                    Log.e("보내는 아이디","$id")
                                    graphWeekFragment.arguments = bundle
                                    */

                                    fragment.startActivity(intent)
                                }
                            })
                        }


                    }else{
                        val retrofitRefresh = RetrofitRefresh()
                        retrofitRefresh.refreshToken()

                        getProductList(name, filter, category, binding, fragment)
                    }
                }

                override fun onFailure(call: Call<ProductListResponseBody>, t: Throwable) {
                    Log.d("Product List 불러오기", "실패(서버 에러)")
                }
            })
    }

    fun getProductDetails(id : Long, binding: ActivityDetailBinding) {
        service.getProductDetails(token = App.prefs.getString("access_token", ""), id).enqueue(object : Callback<ProductDetailsResponseBody>{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<ProductDetailsResponseBody>,
                response: Response<ProductDetailsResponseBody>,
            ) {
                if(response.isSuccessful) {
                    var details = response.body()!!.message.productDetails
                    var usedPrice = response.body()!!.message.usedProductPrices

                    binding.apply {
                        details.stream().forEach { item ->
                            when (item.detailName) {
                                "프로세서" -> processorValueTextView.text =
                                    item.detailContent.replace("/", "")
                                "RAM" -> ramValueTextView.text = item.detailContent
                                "내장메모리" -> memoryValueTextView.text = item.detailContent
                                "통신" -> communicationValueTextView.text = item.detailContent
                                "카메라" -> cameraValueTextView.text =
                                    item.detailContent.replace("/ ", "\n")
                                "보안/기능" -> functionValueTextView.text = item.detailContent
                                "크기" -> inchValueTextView.text = item.detailContent + "인치"
                                "디스플레이" -> displayValueTextView.text = item.detailContent
                                "사운드" -> soundValueTextView.text = item.detailContent
                                "배터리" -> batteryValueTextView.text = item.detailContent
                            }
                        }
                        //var graphDataArrayList = ArrayList<UsedProductPrice>()
                        var graphPriceList = ArrayList<Int>()
                        var graphTimeList = ArrayList<String>()
                        usedPrice.stream().forEach { item ->
                           //graphDataArrayList.add(item)
                            graphPriceList.add(item.price)
                            graphTimeList.add(item.time)
                        }
                        //Log.e("아이템","$graphDataArrayList")
                        val graphWeekFragment =graphWeek()
                        var bundle = Bundle()
                        bundle.putIntegerArrayList("priceList",graphPriceList)
                        bundle.putStringArrayList("timeList",graphTimeList)
                        //bundle.putSerializable("UsedProductPrice", graphDataArrayList)
                        //bundle.putParcelableArrayList("list", graphDataArrayList as ArrayList<out Parcelable?>?)
                        Log.e("번들","$bundle")
                        graphWeekFragment.arguments = bundle
/*                      val transaction = FragmentManager().beginTransaction()
                        transaction.add(R.id.graph_viewPager, graphWeekFragment)
                        transaction.commit()*/
                    }

                } else{
                    val retrofitRefresh = RetrofitRefresh()
                    retrofitRefresh.refreshToken()
                    getProductDetails(id, binding)
                }
            }

            override fun onFailure(call: Call<ProductDetailsResponseBody>, t: Throwable) {
                Log.d("Product Details 불러오기", "실패(서버 에러)")
            }

        })
    }
}