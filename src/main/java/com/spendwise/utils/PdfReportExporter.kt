package com.spendwise.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import android.content.Intent
import com.spendwise.models.Expense
import java.io.File
import java.io.FileOutputStream

object PdfReportExporter {
    fun exportAndShare(context: Context, expenses: List<Expense>, currency: String) {
        val file = File(context.cacheDir, "SpendWise-monthly-report.pdf")
        val pdf = PdfDocument()
        try {
            val page = pdf.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create())
            val paint = Paint().apply { textSize = 18f; isFakeBoldText = true }
            page.canvas.drawText("SpendWise Monthly Report", 40f, 52f, paint)
            paint.textSize = 12f
            paint.isFakeBoldText = false
            var y = 90f
            expenses.take(32).forEach {
                page.canvas.drawText("${DateMoneyUtils.formatDate(it.date)}  ${it.title}  ${DateMoneyUtils.formatMoney(it.amount, currency)}", 40f, y, paint)
                y += 22f
            }
            pdf.finishPage(page)
            FileOutputStream(file).use { pdf.writeTo(it) }
        } finally {
            pdf.close()
        }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val share = Intent(Intent.ACTION_SEND).setType("application/pdf").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(share, "Share SpendWise report"))
    }
}
