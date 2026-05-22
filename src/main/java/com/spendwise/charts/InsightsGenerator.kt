package com.spendwise.charts

import com.spendwise.models.Budget
import com.spendwise.models.CategoryTotal

object InsightsGenerator {
    fun create(monthTotal: Double, budget: Budget?, categories: List<CategoryTotal>): List<String> {
        val insights = mutableListOf<String>()
        val top = categories.maxByOrNull { it.total }
        if (top != null) insights += "${top.category} is your largest category this period."
        if (budget != null && budget.amount > 0) {
            val percent = monthTotal / budget.amount
            if (percent >= 1) insights += "You have exceeded your monthly budget."
            else if (percent >= 0.8) insights += "You are close to your monthly budget."
            else insights += "You are tracking within budget."
        }
        if (insights.isEmpty()) insights += "Add expenses to unlock spending insights."
        return insights
    }
}
