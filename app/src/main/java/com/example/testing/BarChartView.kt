package com.example.testing

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.DecelerateInterpolator

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    private val axisPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.RIGHT
    }
    private val gridLinePaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }
    private val borderPaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var data = listOf<Int>()
    private var labels = listOf<String>()
    private var animatedHeights = listOf<Float>()

    // Zooming feature
    private var scaleFactor = 1f
    private val scaleGestureDetector =
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // Adjust zoom only for the data area
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(1f, 3f) // Limit zoom from 1x to 3x
                invalidate()
                return true
            }
        })

    // Set data dynamically
    fun setData(newData: List<Int>, newLabels: List<String>) {
        data = newData
        labels = newLabels
        animatedHeights = List(data.size) { 0f } // Initialize animated heights to zero
        startAnimation()
    }

    // Animation for bar heights
    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000 // Animation duration in milliseconds
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            animatedHeights = data.map { it * progress }
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Calculate dimensions
        val chartWidth = width * 0.85f  // Reserve 15% for Y-axis labels and padding
        val barWidth = chartWidth / (data.size * 2)  // Bars width occupies 50% of space per item
        val maxValue = 500  // Max value for the Y-axis scale (100 to 500)
        val minValue = 100
        val padding = 100f  // Bottom padding to accommodate labels
        val labelOffset = 70f  // Extra space for labels to avoid overlapping
        val leftPadding = 100f  // Space on the left for the Y-axis labels
        val topPadding = 60f  // Top padding to create space from the top of the view

        // Draw the border around the entire chart
        canvas.drawRect(leftPadding, topPadding, width - 40f, height - padding, borderPaint)

        // Draw Y-axis labels (100 to 500) and grid lines
        val step = 100  // Interval for Y-axis labels
        for (i in minValue..maxValue step step) {
            val yPosition =
                height - padding - labelOffset - ((i.toFloat() - minValue) / (maxValue - minValue)) * (height - padding - labelOffset - topPadding)
            // Draw the Y-axis labels
            canvas.drawText(i.toString(), leftPadding - 10f, yPosition + 10f, axisPaint)
            // Draw grid lines for clarity
            canvas.drawLine(leftPadding, yPosition, width - 40f, yPosition, gridLinePaint)
        }

        // Save the canvas state and apply zoom transformation only to the data area
        canvas.save()
        canvas.translate(leftPadding, topPadding)  // Translate to the chart area
        canvas.scale(scaleFactor, scaleFactor)  // Scale the canvas for zoom effect

        for ((index, value) in animatedHeights.withIndex()) {
            // Set bar color based on condition (e.g., value > 200)
            barPaint.color =
                if (value > 200) Color.GREEN else Color.RED  // Bar color based on value

            val barHeight =
                (value.toFloat() / maxValue) * (height - padding - labelOffset - topPadding)
            val left = index * (barWidth * 2) + 40f  // Offset the bars for spacing
            val top = (height - padding - labelOffset - barHeight - topPadding) / scaleFactor
            val right = left + barWidth
            val bottom = (height - padding - labelOffset - topPadding) / scaleFactor

            // Draw the bar
            canvas.drawRect(left, top, right, bottom, barPaint)

            // Draw the text label just below the bar, but still inside the border
            canvas.drawText(labels[index], left + barWidth / 2f, bottom + 40f, textPaint)
        }

        canvas.restore()  // Restore the canvas to its original state
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        // Pass the touch event to the ScaleGestureDetector
        scaleGestureDetector.onTouchEvent(event)
        return true
    }
}




