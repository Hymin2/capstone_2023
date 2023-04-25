package ac.kr.tukorea.capstone_android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.data.GraphData
import ac.kr.tukorea.capstone_android.databinding.FragmentGraphMonthBinding
import ac.kr.tukorea.capstone_android.databinding.FragmentGraphWeekBinding
import android.os.Build
import android.widget.HorizontalScrollView
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class graphMonth : Fragment() {
    private var _binding: FragmentGraphMonthBinding? = null
    private val binding get() = _binding!!

    val graphDataList: List<GraphData> = listOf(
        GraphData("03-25",1),
        GraphData("03-26",2),
        GraphData("03-27",3),
        GraphData("03-28",2),
        GraphData("03-29",4),
        GraphData("03-30",6),
        GraphData("03-31",5),
        GraphData("04-01",2),
        GraphData("04-02",1),
        GraphData("04-03",7),
        GraphData("04-04",6),
        GraphData("04-05",5),
        GraphData("04-06",4),
        GraphData("04-07",3),
        GraphData("04-08",2),
        GraphData("04-09",1),
        GraphData("04-10",2),
        GraphData("04-11",3),
        GraphData("04-12",2),
        GraphData("04-13",6),
        GraphData("04-14",5),
        GraphData("04-15",4),
        GraphData("04-16",2),
        GraphData("04-17",2),
        GraphData("04-18",3),
        GraphData("04-19",4),
        GraphData("04-20",5),
        GraphData("04-21",2),
        GraphData("04-22",5),
        GraphData("04-23",5),
        GraphData("04-24",4),
        GraphData("04-25",3),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_graph_week, container, false)
        _binding = FragmentGraphMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart = view.findViewById<LineChart>(R.id.monthLineChart)
        val xAxis = lineChart.xAxis

        // y축
        val entries : MutableList<Entry> = mutableListOf()
        for (i in graphDataList.indices){
            entries.add(Entry(i.toFloat(),graphDataList[i].price.toFloat()))
        }
        val lineDataSet = LineDataSet(entries,"entries")

        lineDataSet.apply {
            color = resources.getColor(R.color.black,null)
            circleRadius = 2f
            lineWidth = 1f
            setCircleColor(resources.getColor(R.color.purple_700, null))
            circleHoleColor = resources.getColor(R.color.purple_700, null)
            setDrawHighlightIndicators(false)
            setDrawValues(true) // 숫자표시
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
        }

        xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(true)
            setDrawLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = XAxisCustomFormatter(changeDateText(graphDataList))
            textColor = resources.getColor(R.color.black, null)
            textSize = 10f
            labelRotationAngle = 0f
            setLabelCount(10, true)
        }
        val horizontalScrollView = view.findViewById<HorizontalScrollView>(R.id.graphMonth_scrollview)
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
}