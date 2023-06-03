package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.GraphData
import ac.kr.tukorea.capstone_android.data.UsedProductPrice
import ac.kr.tukorea.capstone_android.databinding.FragmentGraph3monthBinding
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.fragment_graph_3month.view.*
import kotlinx.android.synthetic.main.fragment_graph_6month.view.*

class graph3Month : Fragment() {
    private var _binding: FragmentGraph3monthBinding? = null
    private val binding get() = _binding!!

    private val graphDataList: MutableList<UsedProductPrice> = mutableListOf(
/*        UsedProductPrice("22-05",10),
        UsedProductPrice("22-06",11),
        UsedProductPrice("22-07",13),
        UsedProductPrice("22-08",20),
        UsedProductPrice("22-09",5),
        UsedProductPrice("22-10",5),
        UsedProductPrice("22-11",12),
        UsedProductPrice("22-12",19),
        UsedProductPrice("23-01",15),
        UsedProductPrice("23-02",14),
        UsedProductPrice("23-03",4),
        UsedProductPrice("23-04",10),*/
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("생명주기3개월","Create")
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        Log.e("생명주기3개월","Resume")
        super.onResume()
    }

    override fun onPause() {
        Log.e("생명주기3개월","Pause")
        super.onPause()
    }

    override fun onStop() {
        Log.e("생명주기3개월","Stop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.e("생명주기3개월","DestroyView")
        super.onDestroyView()
    }
    override fun onDestroy() {
        Log.e("생명주기3개월","Destroy")
        super.onDestroy()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.e("생명주기 3개월","onCreateView")
        _binding = FragmentGraph3monthBinding.inflate(inflater, container, false)

        var graphDataArrayList : ArrayList<UsedProductPrice>?

        graphDataArrayList = arguments?.getSerializable("UsedProductPrice") as ArrayList<UsedProductPrice>?
/*        val times = graphDataArrayList?.map { it.time }
        val prices = graphDataArrayList?.map { it.price }*/
        val times : List<String> = listOf(
            "2023-04-25",
            "2023-04-25",
            "2023-04-25",
            "2023-04-25",
            "2023-04-25",
            "2023-04-25",
            "2023-04-25",)
        val prices : List<Int> = listOf(1,2,3,4,5,6,7)

        if (times != null && prices != null) {
            for (i in times.indices) {
                val usedProductPrice = UsedProductPrice(times[i], prices[i])
                graphDataList.add(usedProductPrice)
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("생명주기 3개월","onViewCreated")

        val xAxis = binding.threeMonthLineChart.xAxis

        // y축
        val entries : MutableList<Entry> = mutableListOf()
        for (i in graphDataList.indices){
            entries.add(Entry(i.toFloat(),graphDataList[i].price.toFloat()))
        }
        val lineDataSet = LineDataSet(entries,"entries")


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

        binding.threeMonthLineChart.apply {
            axisRight.isEnabled = false   //y축 사용여부
            axisLeft.isEnabled = false
            axisRight.setDrawGridLines(false)
            legend.isEnabled = false    //legend 사용여부
            description.isEnabled = false //주석
            isDragXEnabled = true   // x 축 드래그 여부
            isScaleYEnabled = false //y축 줌 사용여부
            isScaleXEnabled = false //x축 줌 사용여부

            // 커스텀 마커뷰 설정
            val customMarkerView = CustomMarkerView(context, layoutResource = R.layout.graphmarker)
            marker = customMarkerView

            // 커스텀 마커뷰 크기 설정
            customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)

            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = XAxisCustomFormatter(changeDateText(graphDataList))
                textColor = resources.getColor(R.color.black, null)
                textSize = 10f
                labelRotationAngle = 0f
                setLabelCount(5, true) // 레이블 개수와 간격 설정
            }
            val horizontalScrollView = view.findViewById<HorizontalScrollView>(R.id.graphThreeMonth_scrollview)
            horizontalScrollView.post{
                horizontalScrollView.scrollTo(
                    threeMonthLineChart.width,
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
            tvContent.setTextColor(Color.BLACK)
            super.refreshContent(e, highlight)
        }
    }
}