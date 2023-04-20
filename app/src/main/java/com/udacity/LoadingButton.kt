package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private val loadingProgress = 0.96f
    private val primaryColor = ContextCompat.getColor(context,R.color.colorPrimary)
    private val primaryColorDark = ContextCompat.getColor(context,R.color.colorPrimaryDark)
    private val secondaryColor = ContextCompat.getColor(context,R.color.colorAccent)
    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
        color = primaryColor
    }


    init {
        isClickable = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthSize = w
        heightSize = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Draw any current squiggle
        canvas?.let { canvas ->
            val buttonRectF = RectF(0f,0f,widthSize.toFloat(),heightSize.toFloat())
            canvas.drawRect(buttonRectF,paint)
            //Draw Loading Progress Rectangle
            paint.color = primaryColorDark
            val progressRect = loadingProgress*100 * widthSize /100
            val loadingRectF = RectF(0f,0f,progressRect,heightSize.toFloat())
            canvas.drawRect(loadingRectF,paint)

            paint.color = Color.WHITE
            val text:String = getTextContent()
            val textSize = resources.getDimension(R.dimen.default_text_size)

            paint.textSize = textSize
            paint.textAlign = Paint.Align.CENTER
            val textWidth = paint.measureText(text)
            val textHeight = paint.fontMetrics.bottom - paint.fontMetrics.top

            //Some Middle horizontal line for ref
            canvas.drawLine(0f,heightSize/2f,widthSize.toFloat(),heightSize/2f,paint)

            //Some Middle Vertical line for ref
            canvas.drawLine(widthSize / 2f,0f,widthSize/2f,heightSize.toFloat(),paint)
            //TextBaseLine

            paint.color = Color.RED
            val baseLineY = heightSize/2f + 10f
            canvas.drawLine(0f,baseLineY,widthSize.toFloat(),baseLineY,paint)

            //Draw Text
            paint.color = Color.WHITE
            canvas.drawText(text,widthSize/2f,baseLineY,paint)


            //Draw Arc
            //Draw Rectangle After Text

            val rectStartX = widthSize/2f + textWidth /2f +10
            val rectStartY = heightSize * 0.3
            val rectEndX = rectStartX + 80
            val rectEndY = rectStartY + 80

            val rectF = RectF(rectStartX,rectStartY.toFloat(),rectEndX,rectEndY.toFloat())
            paint.color = secondaryColor
            val arcProgress = loadingProgress*100 * 350 /100
            canvas.drawArc(rectF,0f,arcProgress,true,paint)


        }
    }

    private fun drawRect(canvas: Canvas){

    }

    private fun drawArc(canvas: Canvas){
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
//        canvas.drawRect(rectF,paint)
//        paint.color = Color.RED
//        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint)
//
//        canvas.drawArc()
    }
    private fun getTextContent():String {
        return when(buttonState){
            ButtonState.Clicked -> context.getString(R.string.button_label)
            ButtonState.Completed -> context.getString(R.string.button_label)
            ButtonState.Loading -> context.getString(R.string.button_loading)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}