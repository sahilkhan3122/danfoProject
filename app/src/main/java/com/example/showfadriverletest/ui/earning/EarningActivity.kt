package com.example.showfadriverletest.ui.earning

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showfadriverletest.BR
import com.example.showfadriverletest.R
import com.example.showfadriverletest.base.BaseActivity
import com.example.showfadriverletest.common.Constants
import com.example.showfadriverletest.component.showFailAlert
import com.example.showfadriverletest.databinding.ActivityEarningBinding
import com.example.showfadriverletest.network.Resource
import com.example.showfadriverletest.response.earning.EarningResponse
import com.example.showfadriverletest.ui.earning.adapter.EarningHistoryAdapter
import com.example.showfadriverletest.ui.earning.viewmodel.EarningViewModel
import com.example.showfadriverletest.util.CommonFun
import com.example.showfadriverletest.util.customechart.CustomBarChart
import com.example.showfadriverletest.util.gone
import com.example.showfadriverletest.view.showSnackBar
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class EarningActivity : BaseActivity<ActivityEarningBinding, EarningViewModel>() {
    override val layoutId: Int
        get() = R.layout.activity_earning
    override val bindingVariable: Int
        get() = BR.viewModel

    private var reportType = ""

    private var selectDate = 0
    private var selectWeek = 0
    private var isWeekly = false
    private var currentDate = ""

    private var strFromDate = ""
    private var calendar = Calendar.getInstance()

    private val monEarning = 100f
    private var tueEarning = 0f
    private var wedEarning = 3000f
    private var thuEarning = 50f
    private val friEarning = 200f
    private var satEarning = 500f
    private var sunEarning = 100f

    private var cal = Calendar.getInstance()
    private var days: Array<String?>? = null

    private var list: ArrayList<EarningResponse.EarningsItem> = ArrayList()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var earningHistoryAdapter: EarningHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportType = Constants.REPORT_TYPE_DAILY
        currentDate = System.currentTimeMillis().toDateString(getString(R.string.yyyy_mm_dd))
        binding.tvDateTitle.text = currentDate
        initRv()
        binding.apply {
            setUnderLineText(tvDayWeekEarning)
            header.tvTitle.text = getString(R.string.earnings)
            header.ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }



    override fun setUpObserver() {
        mViewModel.setNavigator(this)
        mViewModel.getearningReportObservable().observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    myDialog.hide()
                    if (it.data!!.status) {
                        initBarChart(
                            it.data.graph?.get(0)?.sun,
                            it.data.graph?.get(0)?.mon,
                            it.data.graph?.get(0)?.tue,
                            3000f.toInt(),
                            it.data.graph?.get(0)?.thu,
                            it.data.graph?.get(0)?.fri,
                            it.data.graph?.get(0)?.sat,
                        )
                        if(it.data.earnings!!.isNotEmpty()){
                            list.clear()
                            list.addAll(it.data.earnings!!)
                            earningHistoryAdapter = EarningHistoryAdapter(this, list) { position ->
                                Toast.makeText(this, "position$position", Toast.LENGTH_SHORT).show()
                            }
                            binding.recyclerView.layoutManager = layoutManager
                            binding.recyclerView.adapter = earningHistoryAdapter
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.tvHistory.visibility = View.VISIBLE
                        }else{
                            binding.tvHistory.visibility = View.VISIBLE
                            binding.recyclerView.gone()
                        }

                    }else{
                        showFailAlert(it.message!!)
                    }
                }

                Resource.Status.LOADING -> {
                    myDialog.show()
                    Log.e("LOADING-----------------", "LOADING::${it.status}")
                }

                Resource.Status.ERROR -> {
                    myDialog.hide()
                    lifecycleScope.launch {
                        if (!checkIsSessionnOut(it.code, getString(R.string.session_expire))) {
                            it.message?.let { message ->
                                if (CommonFun.checkIsConnectionReset(it.code)) {
                                    getString(R.string.no_internet)
                                } else {
                                    message
                                }
                            }
                        }
                    }
                }

                Resource.Status.NO_INTERNET_CONNECTION -> {
                    myDialog.hide()
                    binding.root.showSnackBar(getString(R.string.please_check_your_internet))
                    Log.e("NO_INTERNET_CONNECTION", "NO_INTERNET_CONNECTION::${it.status}")
                }

                else -> {
                    Toast.makeText(this, "Error :${it.message}", Toast.LENGTH_SHORT).show()
                    binding.root.showSnackBar("${it.message}")
                }
            }
        }
    }



    private fun setUnderLineText(textView: TextView) {
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    fun earning(view: View) {
        when (view.id) {
            R.id.ivBackward -> {
                if (reportType.equals(Constants.REPORT_TYPE_WEEKLY, ignoreCase = true)) {
                    mViewModel.type = "daily"
                    cal.firstDayOfWeek = Calendar.SUNDAY
                    cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY

                    val days: Array<String?> = getDays(cal)
                    println("ARRAY " + (days[0]))
                    println("ARRAY " + (days[1]))
                    println("ARRAY " + (days[2]))
                    println("ARRAY " + (days[3]))
                    println("ARRAY " + (days[4]))
                    println("ARRAY " + (days[5]))
                    println("ARRAY " + (days[6]))
                    cal.add(Calendar.DATE, -7)
                    binding.tvDateTitle.text = days[0].toString() + "-" + days[6].toString()
                    val fromDate = days[0].toString()
                    val toDate = days[6].toString()

                    try{
                        mViewModel.earningReportApi(fromDate, toDate)
                    }catch (e:java.lang.Exception){
                        Toast.makeText(this@EarningActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }

                    binding.recyclerView.visibility = View.VISIBLE

                } else if (reportType.equals(Constants.REPORT_TYPE_DAILY, ignoreCase = true)) {
                    mViewModel.type = "daily"
                    binding.tvDateTitle.text = System.currentTimeMillis().toDateString("yyyy-MM-dd")
                    binding.recyclerView.visibility = View.VISIBLE
                    this.calendar.add(Calendar.DAY_OF_MONTH, -1)
                    binding.tvDateTitle.text = this.calendar.timeInMillis.toDateString("yyyy-MM-dd")
                    strFromDate = binding.tvDateTitle.text.toString()
                    mViewModel.earningReportApi(strFromDate, "")
                }
            }

            R.id.ivForward -> {
                if (reportType.equals(Constants.REPORT_TYPE_WEEKLY, ignoreCase = true)) {
                    mViewModel.type = "daily"
                    cal.firstDayOfWeek = Calendar.SUNDAY
                    cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY

                    val days: Array<String?> = getDays(cal)
                    println("ARRAY " + (days[0]))
                    println("ARRAY " + (days[1]))
                    println("ARRAY " + (days[2]))
                    println("ARRAY " + (days[3]))
                    println("ARRAY " + (days[4]))
                    println("ARRAY " + (days[5]))
                    println("ARRAY " + (days[6]))
                    cal.add(Calendar.DATE, +7)
                    binding.tvDateTitle.text = days[0].toString() + "-" + days[6].toString()
                    val fromDate = days[0].toString()
                    val toDate = days[6].toString()

                    try{
                        mViewModel.earningReportApi(fromDate, toDate)
                    }catch (e:java.lang.Exception){
                        Toast.makeText(this@EarningActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                    }

                } else if (reportType.equals(Constants.REPORT_TYPE_DAILY, ignoreCase = true)) {

                    if (currentDate == binding.tvDateTitle.text.toString()) {
                        this.calendar.add(Calendar.DAY_OF_MONTH, 0)
                        binding.tvDateTitle.text =
                            this.calendar.timeInMillis.toDateString("yyyy-MM-dd")
                        strFromDate = binding.tvDateTitle.text.toString()
                    } else {
                        this.calendar.add(Calendar.DAY_OF_MONTH, 1)
                        binding.tvDateTitle.text =
                            this.calendar.timeInMillis.toDateString("yyyy-MM-dd")
                        strFromDate = binding.tvDateTitle.text.toString()
                        mViewModel.earningReportApi(strFromDate, "")
                    }
                }
            }

            R.id.tvDayWeekEarning -> {
                if (isWeekly) {
                    isWeekly = false
                    binding.tvDayWeekEarning.text = "Weekly"
                    binding.llChartEarning.visibility = View.GONE
                    reportType = Constants.REPORT_TYPE_DAILY
                    mViewModel.type = reportType
                    selectWeek = 0/* getDayOfWeek(selectWeek)*/
                } else {
                    isWeekly = true
                    binding.tvDayWeekEarning.text = "Daily"
                    binding.llChartEarning.visibility = View.VISIBLE
                    reportType = Constants.REPORT_TYPE_WEEKLY
                    mViewModel.type = reportType
                    selectDate = 0

                    cal.firstDayOfWeek = Calendar.SUNDAY
                    cal[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY

                    days = getDays(cal)
                    binding.tvDateTitle.text = days!![0].toString() + "-" + days!![6].toString()
                   /*  getSingleDate(selectDate)*/

                }
            }
        }
    }

    private fun getDays(starting: Calendar): Array<String?> {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val days = arrayOfNulls<String>(7)
        val mod = Calendar.getInstance()
        mod.time = starting.time
        for (i in days.indices) {
            mod.add(Calendar.DATE, 1)
            days[i] = format.format(mod.time)
        }
        return days
    }

    private fun Long.toDateString(dateFormat: String): String {
        return try {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val date = Date()
            date.time = this
            formatter.format(date)
        } catch (e: Exception) {
            Log.e(
                "error",
                "String.toDateString > Exception trying to parse timestamp: $this using date format $dateFormat"
            )
            ""
        }
    }

    private fun initBarChart(
        sun: Int?,
        mon: Int?,
        tue: Int?,
        wed: Int?,
        thu: Int?,
        fri: Int?,
        sat: Int?,
    ) {
        binding.apply {
            barChartEarning.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChartEarning.description.isEnabled = false // Hide Description
            barChartEarning.xAxis.setDrawGridLines(false) // Hide Vertical Grid Line
            barChartEarning.axisLeft.isEnabled = false // Hide Left Y Axis Value
            barChartEarning.axisRight.isEnabled = false // Hide Right Y Axis Value
            barChartEarning.legend.isEnabled = false // Hide bottom square color
            barChartEarning.isHighlightPerTapEnabled = true
            barChartEarning.setScaleEnabled(false)
            val mv = CustomMarkerView(this@EarningActivity, R.layout.layout_chart_marker)
            barChartEarning.marker = mv
            val barChartRender = CustomBarChart(
                barChartEarning, barChartEarning.animator, barChartEarning.viewPortHandler
            )
            barChartRender.setRadius(12)
            barChartEarning.renderer = barChartRender
            val noOfEmp = ArrayList<BarEntry>()
            Log.e(TAG, "NEW Monday    ======> $monEarning")
            Log.e(TAG, "NEW Tuesday   ======> $tueEarning")
            Log.e(TAG, "NEW Wednesday ======> $wedEarning")
            Log.e(TAG, "NEW Thursday  ======> $thuEarning")
            Log.e(TAG, "NEW Friday    ======> $friEarning")
            Log.e(TAG, "NEW Saturday  ======> $satEarning")
            Log.e(TAG, "NEW Sunday    ======> $sunEarning")
            noOfEmp.add(BarEntry(0f, mon?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(1f, tue?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(2f, wed?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(3f, thu?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(4f, fri?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(5f, sat?.toFloat() ?: 0f))
            noOfEmp.add(BarEntry(6f, sun?.toFloat() ?: 0f))
            val week = ArrayList<String>()
            week.add("Mon")
            week.add("Tue")
            week.add("Wed")
            week.add("Thu")
            week.add("Fri")
            week.add("Sat")
            week.add("Sun")
            val xAxis: XAxis = barChartEarning.xAxis
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String {
                    return week[value.toInt()]
                }
            }
            val barDataSet = BarDataSet(noOfEmp, "")
            barDataSet.setGradientColor(Color.parseColor("#401B60"), Color.parseColor("#8591AE"))
            barDataSet.setDrawValues(false)
            barChartEarning.animateY(1000)
            val data = BarData(barDataSet)
            data.barWidth = 0.4f
            barChartEarning.data = data
        }
    }
    private fun initRv() {
        dataStore.getUserId.asLiveData().observe(this@EarningActivity) {
            mViewModel.id = it.toString()
        }
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    inner class CustomMarkerView(context: Context?, layoutResource: Int) :
        MarkerView(context, layoutResource) {
        private val tvContent: TextView = findViewById<View>(R.id.tvContent) as TextView

        override fun refreshContent(e: Entry, highlight: Highlight) {
            tvContent.text = "" + e.y // set the entry-value as the display text
        }

        override fun getOffset(): MPPointF {
            return MPPointF(
                -(width / 2).toFloat(), -height.toFloat()
            ) // place the midpoint of marker over the bar
        }
    }
}

