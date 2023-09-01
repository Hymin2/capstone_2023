package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.UsedProductPrice
import ac.kr.tukorea.capstone_android.data.UsedProductPriceResponseBody
import ac.kr.tukorea.capstone_android.databinding.FragmentGraph3monthBinding
import ac.kr.tukorea.capstone_android.util.App
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class graph3Month(val productId : Long) : Fragment() {
    private var _binding: FragmentGraph3monthBinding? = null
    private val binding get() = _binding!!
    private val service = RetrofitAPI.productService

    private var graphDataList: MutableList<UsedProductPrice> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("생명주기3개월","Create")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.e("생명주기 3개월","onCreateView")
        _binding = FragmentGraph3monthBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("생명주기 3개월","onViewCreated")

        service.getUsedProductPrice(App.prefs.getString("access_token", ""), productId, 3).enqueue(object :
            Callback<UsedProductPriceResponseBody> {
            override fun onResponse(
                call: Call<UsedProductPriceResponseBody>,
                response: Response<UsedProductPriceResponseBody>,
            ) {
                if(response.isSuccessful){
                    graphDataList = response.body()!!.message.usedProductPrices as MutableList<UsedProductPrice>
                    createGraph(view)
                    binding.threeMonthLineChart.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<UsedProductPriceResponseBody>, t: Throwable) {

            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun createGraph(view :View){

        val xAxis = binding.threeMonthLineChart.xAxis

        // y축
        val entries : MutableList<Entry> = mutableListOf()
        for (i in graphDataList.indices){
            entries.add(Entry(i.toFloat(),graphDataList[i].price.toFloat()))
        }
        val lineDataSet = LineDataSet(entries,"entries")


        lineDataSet.apply {
            color = resources.getColor(android.R.color.holo_red_dark,null)
            setDrawCircles(false)
            lineWidth = 1f
            setDrawHighlightIndicators(false)
            highLightColor = resources.getColor(android.R.color.holo_red_dark, null)
            setDrawValues(false) // 숫자표시
            valueTextColor = resources.getColor(R.color.black, null)
            valueFormatter = DefaultValueFormatter(0)  // 소숫점 자릿수 설정
            valueTextSize = 10f
        }

        binding.threeMonthLineChart.apply {
            axisRight.isEnabled = false   //y축 사용여부
            axisLeft.isEnabled = true
            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(true)
            legend.isEnabled = false    //legend 사용여부
            description.isEnabled = false //주석
            isDragXEnabled = true   // x 축 드래그 여부
            isScaleYEnabled = false //y축 줌 사용여부
            isScaleXEnabled = false //x축 줌 사용여부



            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = XAxisCustomFormatter(changeDateText(graphDataList))
                textColor = resources.getColor(R.color.black, null)
                textSize = 10f
                labelRotationAngle = 0f
                setLabelCount(4, true) // 레이블 개수와 간격 설정
            }
            val horizontalScrollView = view.findViewById<HorizontalScrollView>(R.id.graphThreeMonth_scrollview)
            horizontalScrollView.post{
                horizontalScrollView.scrollTo(
                    binding.threeMonthLineChart.width,
                    0
                )
            }

            data = LineData(lineDataSet)
            notifyDataSetChanged() //데이터 갱신
            invalidate() // view갱신
        }
    }

    fun changeDateText(dataList: List<UsedProductPrice>): List<String> {
        val dataTextList = ArrayList<String>()
        for (i in dataList.indices) {
            val textSize = dataList[i].time.length
            val dateText = dataList[i].time.substring(textSize - 5, textSize) // 월-일만 추출
            dataTextList.add(dateText)
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData: List<String>) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }

    }

}