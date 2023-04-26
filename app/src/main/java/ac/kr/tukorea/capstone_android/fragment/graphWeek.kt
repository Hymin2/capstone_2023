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
import java.lang.Long.getLong


class graphWeek : Fragment() {

    private var _binding: FragmentGraphWeekBinding? = null
    private val binding get() = _binding!!

    private val service = RetrofitAPI.productService

    val graphDataList: List<GraphData> = listOf(
/*        GraphData("04-20",212323),
        GraphData("04-21",323122),
        GraphData("04-22",623121),
        GraphData("04-23",74535),
        GraphData("04-24",13345),
        GraphData("04-25",36656),
        GraphData("04-26",24545),
        GraphData("04-20",22323),
        GraphData("04-21",331221),
        GraphData("04-22",6222),
        GraphData("04-23",72312),
        GraphData("04-24",112313),
        GraphData("04-25",31232),
        GraphData("04-26",233),
        GraphData("04-20",2444),
        GraphData("04-21",3555),
        GraphData("04-22",6111),
        GraphData("04-23",72222),
        GraphData("04-24",13333),
        GraphData("04-25",34444),
        GraphData("04-26",25555),
        GraphData("04-20",26666),
        GraphData("04-21",37777),
        GraphData("04-22",68888),
        GraphData("04-23",7999),*/
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_graph_week, container, false)
        _binding = FragmentGraphWeekBinding.inflate(inflater, container, false)

/*        var graphDataArrayList : ArrayList<UsedProductPrice>
        var graphPriceList : ArrayList<Int>?
        var grpahTimeList : ArrayList<String>

        graphPriceList = arguments?.getSerializable("priceList") as ArrayList<Int>?
        var a = graphPriceList?.get(0)
        Log.e("데이터" , "$a")*/
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart = view.findViewById<LineChart>(R.id.weekLineChart)
        val xAxis = lineChart.xAxis

        // y축
        val entries : MutableList<Entry> = mutableListOf()
        for (i in graphDataList.indices){
            entries.add(Entry(i.toFloat(),graphDataList[i].price.toFloat()))
        }
        val lineDataSet = LineDataSet(entries,"entries")

        var graphDataArrayList : ArrayList<UsedProductPrice>
        var graphPriceList : ArrayList<Int>?
        var grpahTimeList : ArrayList<String>

        graphPriceList = arguments?.getIntegerArrayList("priceList")
        Log.e("데이터" , "$graphPriceList")
/*        val a = arguments?.getSerializable("UsedProductPrice")


        Log.e("데이터" , "$a")*/
        // var retrofitProduct = RetrofitProduct()
        //retrofitProduct.getProductUsedPrice(productId,binding)


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

        lineChart.apply {
            axisRight.isEnabled = false   //y축 사용여부
            axisLeft.isEnabled = false
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
            setDrawAxisLine(false)
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
                lineChart.width,
                0
            )
        }

        lineChart.apply {
            data = LineData(lineDataSet)
            notifyDataSetChanged() //데이터 갱신
            invalidate() // view갱신
        }
        lineChart
    }

    fun changeDateText(dataList: List<GraphData>): List<String> {
        val dataTextList = ArrayList<String>()
        for (i in dataList.indices) {
            val textSize = dataList[i].date.length
            val dateText = dataList[i].date.substring(textSize - 2, textSize)
            if (dateText == "01") {
                dataTextList.add(dataList[i].date)
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