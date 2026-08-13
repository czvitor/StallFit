package com.vitorsousa.stallfit.data.pdf

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import androidx.core.content.ContextCompat
import com.vitorsousa.stallfit.R
import com.vitorsousa.stallfit.data.local.entity.Intensity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

/**
 * Renders a ficha (workout template) as a styled PDF using Android's native PdfDocument/Canvas —
 * no third-party PDF library, matching this app's existing preference for hand-rolled rendering
 * over dependencies (see ui/components/LineChart.kt, drawn on a plain Compose Canvas). A4 page,
 * points as unit (1pt = 1/72in). Colors always follow ui/theme/Color.kt's light palette: the PDF
 * is read/printed outside the app regardless of which in-app theme is active.
 */
object WorkoutPdfGenerator {

    private const val PAGE_WIDTH = 595f
    private const val PAGE_HEIGHT = 842f
    private const val MARGIN = 32f
    private const val CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN

    private const val HEADER_HEIGHT_FIRST = 96f
    private const val HEADER_HEIGHT_CONTINUATION = 44f
    private const val ACCENT_BAR_HEIGHT = 3f
    private const val METADATA_BLOCK_HEIGHT = 92f
    private const val TABLE_HEADER_ROW_HEIGHT = 26f
    private const val DATA_ROW_HEIGHT = 34f
    private const val FOOTER_HEIGHT = 56f
    private const val SECTION_GAP = 16f

    private const val COL_EXERCISE = 175f
    private const val COL_SETS = 40f
    private const val COL_REPS = 50f
    private const val COL_INTENSITY = 100f
    private const val COL_REST = 66f
    private const val COL_WEIGHT = CONTENT_WIDTH - COL_EXERCISE - COL_SETS - COL_REPS - COL_INTENSITY - COL_REST

    private const val COLOR_PAGE_BG = 0xFFF8FAFC.toInt()
    private const val COLOR_HEADER_BG = 0xFF0D0F12.toInt()
    private const val COLOR_TABLE_HEADER_BG = 0xFF16191E.toInt()
    private const val COLOR_ACCENT = 0xFFCCFF00.toInt()
    private const val COLOR_ON_HEADER = 0xFFF0F3F8.toInt()
    private const val COLOR_TEXT_PRIMARY = 0xFF0F172A.toInt()
    private const val COLOR_TEXT_SECONDARY = 0xFF334155.toInt()
    private const val COLOR_TEXT_DISABLED = 0xFF64748B.toInt()
    private const val COLOR_ROW_STRIPE_A = 0xFFF8FAFC.toInt()
    private const val COLOR_ROW_STRIPE_B = 0xFFF1F5F9.toInt()
    private const val COLOR_BORDER = 0xFFCBD5E1.toInt()

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun generate(
        context: Context,
        template: WorkoutTemplateEntity,
        exercises: List<TemplateExerciseWithExercise>,
        studentName: String
    ): ByteArray {
        val document = PdfDocument()
        try {
            return renderPages(document, template, exercises, studentName, context)
        } finally {
            document.close()
        }
    }

    private fun renderPages(
        document: PdfDocument,
        template: WorkoutTemplateEntity,
        exercises: List<TemplateExerciseWithExercise>,
        studentName: String,
        context: Context
    ): ByteArray {
        val contentBottom = PAGE_HEIGHT - MARGIN - FOOTER_HEIGHT

        val firstPageTableTop = MARGIN + HEADER_HEIGHT_FIRST + SECTION_GAP + METADATA_BLOCK_HEIGHT + SECTION_GAP
        val continuationTableTop = MARGIN + HEADER_HEIGHT_CONTINUATION + SECTION_GAP

        val firstPageCapacity =
            ((contentBottom - firstPageTableTop - TABLE_HEADER_ROW_HEIGHT) / DATA_ROW_HEIGHT).toInt().coerceAtLeast(1)
        val continuationCapacity =
            ((contentBottom - continuationTableTop - TABLE_HEADER_ROW_HEIGHT) / DATA_ROW_HEIGHT).toInt().coerceAtLeast(1)

        val totalPages = when {
            exercises.size <= firstPageCapacity -> 1
            else -> 1 + ceil((exercises.size - firstPageCapacity).toDouble() / continuationCapacity).toInt()
        }

        val overallIntensity = exercises
            .map { it.templateExercise.intensity }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key

        val watermark = ContextCompat.getDrawable(context, R.drawable.stallfit_vertical_watermark)
        val symbol = ContextCompat.getDrawable(context, R.drawable.stallfit_symbol)
        val wordmark = ContextCompat.getDrawable(context, R.drawable.stallfit_wordmark)

        var exerciseIndex = 0
        var pageNumber = 0
        while (exerciseIndex < exercises.size || pageNumber == 0) {
            pageNumber++
            val isFirstPage = pageNumber == 1
            val page = document.startPage(
                PdfDocument.PageInfo.Builder(PAGE_WIDTH.toInt(), PAGE_HEIGHT.toInt(), pageNumber).create()
            )
            val canvas = page.canvas

            canvas.drawColor(COLOR_PAGE_BG)
            drawWatermark(canvas, watermark)

            val tableTop = if (isFirstPage) {
                drawHeader(canvas, template.title, symbol, wordmark, isFirstPage = true)
                drawMetadata(canvas, studentName, overallIntensity, template.notes)
                firstPageTableTop
            } else {
                drawHeader(canvas, template.title, symbol, wordmark, isFirstPage = false)
                continuationTableTop
            }

            var y = tableTop
            drawTableHeader(canvas, y)
            y += TABLE_HEADER_ROW_HEIGHT

            var rowIndexOnPage = 0
            while (exerciseIndex < exercises.size && y + DATA_ROW_HEIGHT <= contentBottom) {
                drawExerciseRow(canvas, y, rowIndexOnPage, exercises[exerciseIndex])
                y += DATA_ROW_HEIGHT
                exerciseIndex++
                rowIndexOnPage++
            }
            if (exercises.isEmpty()) {
                drawEmptyRow(canvas, y)
            }

            drawFooter(canvas, pageNumber, totalPages, isLastPage = exerciseIndex >= exercises.size)
            document.finishPage(page)
        }

        val output = ByteArrayOutputStream()
        document.writeTo(output)
        return output.toByteArray()
    }

    private fun drawWatermark(canvas: Canvas, watermark: Drawable?) {
        watermark ?: return
        val targetWidth = CONTENT_WIDTH * 0.62f
        val aspect = watermark.intrinsicHeight.toFloat() / watermark.intrinsicWidth.toFloat()
        val targetHeight = targetWidth * aspect
        val left = (PAGE_WIDTH - targetWidth) / 2f
        val top = (PAGE_HEIGHT - targetHeight) / 2f
        watermark.setBounds(left.toInt(), top.toInt(), (left + targetWidth).toInt(), (top + targetHeight).toInt())
        watermark.alpha = 14
        watermark.draw(canvas)
    }

    private fun drawHeader(
        canvas: Canvas,
        title: String,
        symbol: Drawable?,
        wordmark: Drawable?,
        isFirstPage: Boolean
    ) {
        val height = if (isFirstPage) HEADER_HEIGHT_FIRST else HEADER_HEIGHT_CONTINUATION
        canvas.drawRect(0f, 0f, PAGE_WIDTH, height, Paint().apply { color = COLOR_HEADER_BG })
        canvas.drawRect(0f, height - ACCENT_BAR_HEIGHT, PAGE_WIDTH, height, Paint().apply { color = COLOR_ACCENT })

        var logoLeft = MARGIN
        val logoTop = if (isFirstPage) 18f else 10f
        val symbolHeight = if (isFirstPage) 30f else 20f
        symbol?.let {
            val aspect = it.intrinsicWidth.toFloat() / it.intrinsicHeight.toFloat()
            val symbolWidth = symbolHeight * aspect
            it.setBounds(logoLeft.toInt(), logoTop.toInt(), (logoLeft + symbolWidth).toInt(), (logoTop + symbolHeight).toInt())
            it.draw(canvas)
            logoLeft += symbolWidth + 10f
        }
        wordmark?.let {
            val wordmarkHeight = symbolHeight * 0.62f
            val aspect = it.intrinsicWidth.toFloat() / it.intrinsicHeight.toFloat()
            val wordmarkWidth = wordmarkHeight * aspect
            val wordmarkTop = logoTop + (symbolHeight - wordmarkHeight) / 2f
            it.setBounds(logoLeft.toInt(), wordmarkTop.toInt(), (logoLeft + wordmarkWidth).toInt(), (wordmarkTop + wordmarkHeight).toInt())
            it.draw(canvas)
        }

        if (isFirstPage) {
            val titlePaint = Paint().apply {
                color = COLOR_ON_HEADER
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textSize = 19f
                isAntiAlias = true
            }
            val headerText = "FICHA DE TREINO — ${title.uppercase()}"
            canvas.drawText(truncate(headerText, titlePaint, CONTENT_WIDTH), MARGIN, height - 16f, titlePaint)
        } else {
            val titlePaint = Paint().apply {
                color = COLOR_ON_HEADER
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textSize = 11f
                isAntiAlias = true
            }
            val label = truncate(title.uppercase(), titlePaint, CONTENT_WIDTH - 140f)
            canvas.drawText(label, PAGE_WIDTH - MARGIN - titlePaint.measureText(label), height - 15f, titlePaint)
        }
    }

    private fun drawMetadata(canvas: Canvas, studentName: String, overallIntensity: Intensity?, notes: String?) {
        val top = MARGIN + HEADER_HEIGHT_FIRST + SECTION_GAP
        val labelPaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 9.5f
            isAntiAlias = true
        }
        val valuePaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 11.5f
            isAntiAlias = true
        }

        val generatedAt = Instant.now().atZone(ZoneId.systemDefault()).format(dateFormatter)
        val rows = listOf(
            "Aluno" to studentName.ifBlank { "—" },
            "Data de geração" to generatedAt,
            "Nível de intensidade" to (overallIntensity?.label ?: "—")
        )

        var y = top + 12f
        val columnWidth = CONTENT_WIDTH / rows.size
        rows.forEachIndexed { index, (label, value) ->
            val x = MARGIN + columnWidth * index
            canvas.drawText(label.uppercase(), x, y, labelPaint)
            canvas.drawText(truncate(value, valuePaint, columnWidth - 8f), x, y + 16f, valuePaint)
        }
        y += 34f

        if (!notes.isNullOrBlank()) {
            canvas.drawText("OBSERVAÇÕES", MARGIN, y, labelPaint)
            y += 14f
            val notesPaint = Paint().apply {
                color = COLOR_TEXT_PRIMARY
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                textSize = 10f
                isAntiAlias = true
            }
            wrapText(notes, notesPaint, CONTENT_WIDTH).take(2).forEach { line ->
                canvas.drawText(line, MARGIN, y, notesPaint)
                y += 13f
            }
        }
    }

    private fun drawTableHeader(canvas: Canvas, top: Float) {
        canvas.drawRect(MARGIN, top, MARGIN + CONTENT_WIDTH, top + TABLE_HEADER_ROW_HEIGHT, Paint().apply { color = COLOR_TABLE_HEADER_BG })

        val textPaint = Paint().apply {
            color = COLOR_ACCENT
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 8.5f
            isAntiAlias = true
        }
        val baseline = top + TABLE_HEADER_ROW_HEIGHT / 2f + 3f
        var x = MARGIN + 8f
        canvas.drawText("EXERCÍCIO", x, baseline, textPaint)
        x += COL_EXERCISE
        canvas.drawText("SÉRIES", x, baseline, textPaint)
        x += COL_SETS
        canvas.drawText("REPS", x, baseline, textPaint)
        x += COL_REPS
        canvas.drawText("NÍVEL DE CARGA", x, baseline, textPaint)
        x += COL_INTENSITY
        canvas.drawText("DESCANSO", x, baseline, textPaint)
        x += COL_REST
        canvas.drawText("CARGA BASE (KG)", x, baseline, textPaint)
    }

    private fun drawExerciseRow(canvas: Canvas, top: Float, rowIndexOnPage: Int, exercise: TemplateExerciseWithExercise) {
        val stripeColor = if (rowIndexOnPage % 2 == 0) COLOR_ROW_STRIPE_A else COLOR_ROW_STRIPE_B
        canvas.drawRect(MARGIN, top, MARGIN + CONTENT_WIDTH, top + DATA_ROW_HEIGHT, Paint().apply { color = stripeColor })
        canvas.drawLine(
            MARGIN, top + DATA_ROW_HEIGHT, MARGIN + CONTENT_WIDTH, top + DATA_ROW_HEIGHT,
            Paint().apply { color = COLOR_BORDER; strokeWidth = 0.75f }
        )

        val namePaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 10.5f
            isAntiAlias = true
        }
        val subPaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 8.5f
            isAntiAlias = true
        }
        val valuePaint = Paint().apply {
            color = COLOR_TEXT_PRIMARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 10f
            isAntiAlias = true
        }

        var x = MARGIN + 8f
        canvas.drawText(truncate(exercise.exerciseName, namePaint, COL_EXERCISE - 12f), x, top + 15f, namePaint)
        canvas.drawText(
            truncate("${exercise.muscleGroup} · ${exercise.equipment.label}", subPaint, COL_EXERCISE - 12f),
            x, top + 27f, subPaint
        )

        val rowCenterBaseline = top + DATA_ROW_HEIGHT / 2f + 3.5f
        x += COL_EXERCISE
        canvas.drawText(exercise.templateExercise.sets.toString(), x, rowCenterBaseline, valuePaint)
        x += COL_SETS
        canvas.drawText("${exercise.templateExercise.repRangeMin}-${exercise.templateExercise.repRangeMax}", x, rowCenterBaseline, valuePaint)
        x += COL_REPS
        canvas.drawText(exercise.templateExercise.intensity.label, x, rowCenterBaseline, valuePaint)
        x += COL_INTENSITY
        canvas.drawText("${exercise.templateExercise.restSeconds}s", x, rowCenterBaseline, valuePaint)
        x += COL_REST

        val weightKg = exercise.templateExercise.referenceWeightKg
        if (weightKg != null) {
            val weightPaint = Paint().apply {
                color = COLOR_TEXT_PRIMARY
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textSize = 10.5f
                isAntiAlias = true
            }
            canvas.drawText("${formatWeight(weightKg)} kg", x, rowCenterBaseline, weightPaint)
        } else {
            val lineY = top + DATA_ROW_HEIGHT / 2f + 4f
            canvas.drawLine(
                x, lineY, x + COL_WEIGHT - 16f, lineY,
                Paint().apply { color = COLOR_TEXT_DISABLED; strokeWidth = 1f; pathEffect = DashPathEffect(floatArrayOf(3f, 3f), 0f) }
            )
        }
    }

    private fun drawEmptyRow(canvas: Canvas, top: Float) {
        val textPaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 11f
            isAntiAlias = true
        }
        canvas.drawText("Esta ficha ainda não tem exercícios.", MARGIN + 8f, top + 20f, textPaint)
    }

    private fun drawFooter(canvas: Canvas, pageNumber: Int, totalPages: Int, isLastPage: Boolean) {
        val top = PAGE_HEIGHT - MARGIN - FOOTER_HEIGHT
        canvas.drawLine(MARGIN, top, MARGIN + CONTENT_WIDTH, top, Paint().apply { color = COLOR_BORDER; strokeWidth = 0.75f })

        if (isLastPage) {
            canvas.drawText(
                "Notas do treino:", MARGIN, top + 14f,
                Paint().apply {
                    color = COLOR_TEXT_SECONDARY
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
                    textSize = 9f
                    isAntiAlias = true
                }
            )
            canvas.drawLine(
                MARGIN + 90f, top + 12f, MARGIN + CONTENT_WIDTH * 0.6f, top + 12f,
                Paint().apply { color = COLOR_BORDER; strokeWidth = 0.5f; pathEffect = DashPathEffect(floatArrayOf(2f, 2f), 0f) }
            )
        }

        canvas.drawText(
            "STÄLLFIT", MARGIN, PAGE_HEIGHT - MARGIN + 4f,
            Paint().apply {
                color = COLOR_TEXT_DISABLED
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                textSize = 9f
                isAntiAlias = true
            }
        )

        val pagePaint = Paint().apply {
            color = COLOR_TEXT_SECONDARY
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 9f
            isAntiAlias = true
        }
        val pageLabel = "Página $pageNumber de $totalPages"
        canvas.drawText(pageLabel, MARGIN + CONTENT_WIDTH - pagePaint.measureText(pageLabel), PAGE_HEIGHT - MARGIN + 4f, pagePaint)
    }

    private fun formatWeight(value: Double): String =
        if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

    private fun truncate(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var end = text.length
        while (end > 0 && paint.measureText(text.substring(0, end) + "…") > maxWidth) {
            end--
        }
        return text.substring(0, end) + "…"
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(Regex("\\s+")).filter { it.isNotBlank() }
        val lines = mutableListOf<String>()
        var current = StringBuilder()
        for (word in words) {
            val candidate = if (current.isEmpty()) word else "$current $word"
            if (paint.measureText(candidate) > maxWidth && current.isNotEmpty()) {
                lines.add(current.toString())
                current = StringBuilder(word)
            } else {
                current = StringBuilder(candidate)
            }
        }
        if (current.isNotEmpty()) lines.add(current.toString())
        return lines
    }
}
