package ac.kr.tukorea.capstone_android.fragment

import ac.kr.tukorea.capstone_android.API.RetrofitAPI
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.GraphData
import ac.kr.tukorea.capstone_android.data.UsedProductPrice
import ac.kr.tukorea.capstone_android.databinding.FragmentGraphWeekBinding
import ac.kr.tukorea.capstone_android.retrofit.RetrofitProduct
import android.content.Context
import android.content.Intent.getIntent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.fragment_graph_week.*
import java.lang.Long.getLong


class graphWeek : Fragment() {

    private var _binding: FragmentGraphWeekBinding? = null
    private val binding get() = _binding!!

    //private val service = RetrofitAPI.productService

    val graphDataList: List<UsedProductPrice> = listOf(
        UsedProductPrice("2023-03-19",890000),
        UsedProductPrice("2023-03-20",900000),
        UsedProductPrice("2023-03-24",900000),
        UsedProductPrice("2023-03-30",915000),
        UsedProductPrice("2023-03-31",899000),
        UsedProductPrice("2023-04-03",890000),
        UsedProductPrice("2023-04-07",780000),
        UsedProductPrice("2023-04-09",890000),
        UsedProductPrice("2023-04-10",1080000),
        UsedProductPrice("2023-04-11",840000),
        UsedProductPrice("2023-04-12",810000),
        UsedProductPrice("2023-04-13",806667),
        UsedProductPrice("2023-04-14",890000),
        UsedProductPrice("2023-04-15",890000),
        UsedProductPrice("2023-04-17",780000),
        UsedProductPrice("2023-04-18",815000),
        UsedProductPrice("2023-04-19",878000),
        UsedProductPrice("2023-04-20",830000),
        UsedProductPrice("2023-04-23",780000),
        UsedProductPrice("2023-04-24",823750),
        UsedProductPrice("2023-04-25",870000),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_graph_week, container, false)
        _binding = FragmentGraphWeekBinding.inflate(inflater, container, false)

        var graphDataArrayList : ArrayList<UsedProductPrice>?
        //var graphPriceList : ArrayList<Int>?
        //var grpahTimeList : ArrayList<String>

        graphDataArrayList = arguments?.getSerializable("UsedProductPrice") as ArrayList<UsedProductPrice>?
        var a = graphDataArrayList
        Log.e("데이터1" , "$a")
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val xAxis = binding.weekLineChart.xAxis

        // y축
        val entries : MutableList<Entry> = mutableListOf()
        for (i in graphDataList.indices){
            entries.add(Entry(i.toFloat(),graphDataList[i].price.toFloat()))
            var a= entries[i]
          //  Log.e("graphr값","$a")
        }
        val lineDataSet = LineDataSet(entries,"entries")
/*

        var graphDataArrayList : ArrayList<UsedProductPrice>
        var graphPriceList : ArrayList<Int>?
        var grpahTimeList : ArrayList<String>

        graphPriceList = arguments?.getIntegerArrayList("priceList")
        Log.e("데이터" , "$graphPriceList")
        val a = arguments?.getSerializable("UsedProductPrice")
        Log.e("데이터" , "$a")
        var retrofitProduct = RetrofitProduct()
        retrofitProduct.getProductUsedPrice(productId,binding)

*/

        lineDataSet.apply {
            color = resources.getColor(android.R.color.holo_red_dark,null)
            setDrawCircles(true)
            circleRadius = 2f
            lineWidth = 1f
            setCircleColor(resources.getColor(android.R.color.holo_red_dark, null))
            circleHoleColor = resources.getColor(R.color.white, null)
            setDrawHighlightIndicators(true)
            highLightColor = resources.getColor(android.R.color.holo_red_dark, null)
            setDrawValues(false) // 숫자표시
            valueTextColor = resources.getColor(R.color.black, null)
            valueFormatter = DefaultValueFormatter(0)  // 소숫점 자릿수 설정
            valueTextSize = 10f
        }

        binding.weekLineChart.apply {
            axisRight.isEnabled = true   //y축 사용여부
            axisLeft.isEnabled = false
            axisRight.setDrawGridLines(false)
            legend.isEnabled = false    //legend 사용여부
            description.isEnabled = false //주석
            isDragXEnabled = true   // x 축 드래그 여부
            isScaleYEnabled = false //y축 줌 사용여부
            isScaleXEnabled = false //x축 줌 사용여부
            setDrawMarkers(true)
            val customMarkerView = CustomMarkerView(context, layoutResource = R.layout.graphmarker)
            marker = customMarkerView
        }

        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(false)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(graphDataList))
            textColor = resources.getColor(R.color.black, null)
            textSize = 10f
            labelRotationAngle = 0f
            setLabelCount(100, true)
        }
        val horizontalScrollView = view.findViewById<HorizontalScrollView>(R.id.graphWeek_scrollview)
        horizontalScrollView.post{
            horizontalScrollView.scrollTo(
                weekLineChart.width,
                0
            )
        }

        binding.weekLineChart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged() //데이터 갱신
            invalidate() // view갱신
        }
        binding.weekLineChart
    }

    fun changeDateText(dataList: List<UsedProductPrice>): List<String> {
        val dataTextList = ArrayList<String>()
        for (i in dataList.indices) {
            val textSize = dataList[i].time.length
            val dateText = dataList[i].time.substring(textSize - 2, textSize)
            if (dateText == "01") {
                dataTextList.add(dataList[i].time)
            } else {
                dataTextList.add(dateText)
            }
        }
        return dataTextList
    }

    class XAxisCustomFormatter(val xAxisData: List<String>) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return xAxisData[(value).toInt()]
        }

    }

    class CustomMarkerView : MarkerView {
        private var tvContent: TextView
        // marker
        constructor(context: Context?, layoutResource: Int) : super(context, layoutResource) {
            tvContent = findViewById(R.id.tvContent)
        }

        // draw override를 사용해 marker의 위치 조정 (bar의 상단 중앙)
        override fun draw(canvas: Canvas?) {
            canvas!!.translate(-(width/100).toFloat(), -(height).toFloat() )
            super.draw(canvas)
        }

        // entry를 content의 텍스트에 지정
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            tvContent.text = e?.y?.toInt().toString() + "원"
            super.refreshContent(e, highlight)
        }

    }
}