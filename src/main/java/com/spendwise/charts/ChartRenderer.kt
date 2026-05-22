package com.spendwise.charts

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.spendwise.models.CategoryTotal
import com.spendwise.models.Expense
import java.util.Calendar

object ChartRenderer {
    private val colors = listOf(Color.rgb(23, 105, 224), Color.rgb(14, 159, 110), Color.rgb(217, 119, 6), Color.rgb(220, 38, 38), Color.rgb(124, 58, 237))

    fun renderPie(chart: PieChart, totals: List<CategoryTotal>) {
        val entries = totals.filter { it.total > 0 }.map { PieEntry(it.total.toFloat(), it.category) }
        val set = PieDataSet(entries, "").apply { this.colors = ChartRenderer.colors; valueTextSize = 12f }
        chart.data = PieData(set)
        chart.description.isEnabled = false
        chart.centerText = "Categories"
        chart.animateY(700)
        chart.invalidate()
    }

    fun renderWeeklyBars(chart: BarChart, expenses: List<Expense>) {
        val totals = FloatArray(7)
        expenses.forEach {
            val day = Calendar.getInstance().apply { timeInMillis = it.date }.get(Calendar.DAY_OF_WEEK) - 1
            totals[day] += it.amount.toFloat()
        }
        val entries = totals.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        chart.data = BarData(BarDataSet(entries, "Weekly spending").apply { colors = ChartRenderer.colors })
        chart.description.isEnabled = false
        chart.animateY(700)
        chart.invalidate()
    }
}
