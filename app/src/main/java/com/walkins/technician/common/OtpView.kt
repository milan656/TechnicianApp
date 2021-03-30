package com.walkins.technician.common

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.MovementMethod
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.walkins.technician.R


class OtpView(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatEditText(context, attrs, defStyleAttr) {
    private val viewType: Int
    private var otpViewItemCount: Int
    private var otpViewItemWidth: Int
    private var otpViewItemHeight: Int
    private var otpViewItemRadius: Int
    private var otpViewItemSpacing: Int
    private val paint: Paint
    private val animatorTextPaint: TextPaint? = TextPaint()

    /**
     * Gets the line colors for the different states (normal, selected, focused) of the OtpView.
     *
     * @attr ref R.styleable#OtpView_lineColor
     * @see .setLineColor
     * @see .setLineColor
     */
    var lineColors: ColorStateList?
        private set

    /**
     *
     * Return the current color selected for normal line.
     *
     * @return Returns the current item's line color.
     */
    @get:ColorInt
    var currentLineColor: Int = Color.BLACK
        private set
    private var lineWidth: Int
    private val textRect: Rect = Rect()
    private val itemBorderRect = RectF()
    private val itemLineRect = RectF()
    private val path: Path = Path()
    private val itemCenterPoint = PointF()
    private var defaultAddAnimator: ValueAnimator? = null
    private var isAnimationEnable = false
    private var blink: Blink? = null
    private var isCursorVisible: Boolean
    private var drawCursor = false
    private var cursorHeight = 0f
    private var cursorWidth: Int
    private var cursorColor: Int
    private var itemBackgroundResource = 0
    private var itemBackground: Drawable?
    private var hideLineWhenFilled: Boolean
    private val rtlTextDirection: Boolean
    private var maskingChar: String?
    private var onOtpCompletionListener: OnOtpCompletionListener? = null
    private var isAllCaps_: Boolean? = false

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.otpViewStyle
    ) {
    }

    override fun setTypeface(tf: Typeface?, style: Int) {
        super.setTypeface(tf, style)
    }

    override fun setTypeface(tf: Typeface?) {
        super.setTypeface(tf)
        animatorTextPaint?.set(getPaint())
    }

    private fun setMaxLength(maxLength: Int) {
        filters = if (maxLength >= 0) arrayOf<InputFilter>(LengthFilter(maxLength)) else NO_FILTERS
    }

    private fun setupAnimator() {
        defaultAddAnimator = ValueAnimator.ofFloat(0.5f, 1f)
        defaultAddAnimator?.setDuration(150)
        defaultAddAnimator?.setInterpolator(DecelerateInterpolator())
        defaultAddAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val scale = animation.animatedValue as Float
            val alpha = (255 * scale).toInt()
            animatorTextPaint!!.textSize = textSize * scale
            animatorTextPaint.alpha = alpha
            postInvalidate()
        })
    }

    private fun checkItemRadius() {
        if (viewType == VIEW_TYPE_LINE) {
            val halfOfLineWidth = lineWidth.toFloat() / 2
            require(otpViewItemRadius <= halfOfLineWidth) { "The itemRadius can not be greater than lineWidth when viewType is line" }
        } else if (viewType == VIEW_TYPE_RECTANGLE) {
            val halfOfItemWidth = otpViewItemWidth.toFloat() / 2
            require(otpViewItemRadius <= halfOfItemWidth) { "The itemRadius can not be greater than itemWidth" }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var width: Int
        val height: Int
        val boxHeight = otpViewItemHeight
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            val boxesWidth =
                (otpViewItemCount - 1) * otpViewItemSpacing + otpViewItemCount * otpViewItemWidth
            width = boxesWidth + ViewCompat.getPaddingEnd(this) + ViewCompat.getPaddingStart(this)
            if (otpViewItemSpacing == 0) {
                width -= (otpViewItemCount - 1) * lineWidth
            }
        }
        height =
            if (heightMode == MeasureSpec.EXACTLY) heightSize else boxHeight + paddingTop + paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (start != text.length) {
            moveSelectionToEnd()
        }
        if (text.length == otpViewItemCount && onOtpCompletionListener != null) {
            onOtpCompletionListener?.onOtpCompleted(text.toString())
        }
        makeBlink()
        if (isAnimationEnable) {
            val isAdd = lengthAfter - lengthBefore > 0
            if (isAdd && defaultAddAnimator != null) {
                defaultAddAnimator!!.end()
                defaultAddAnimator!!.start()
            }
        }
    }

    protected override fun onFocusChanged(
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            moveSelectionToEnd()
            makeBlink()
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (text != null && selEnd != text!!.length) {
            moveSelectionToEnd()
        }
    }

    private fun moveSelectionToEnd() {
        if (text != null) {
            setSelection(text!!.length)
        }
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (lineColors == null || lineColors!!.isStateful) {
            updateColors()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        updatePaints()
        drawOtpView(canvas)
        canvas.restore()
    }

    private fun updatePaints() {
        paint.setColor(currentLineColor)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(lineWidth.toFloat())
        getPaint().color = currentTextColor
    }

    private fun drawOtpView(canvas: Canvas) {
        val nextItemToFill: Int
        nextItemToFill = if (rtlTextDirection) {
            otpViewItemCount - 1
        } else {
            if (text != null) {
                text!!.length
            } else {
                0
            }
        }
        for (i in 0 until otpViewItemCount) {
            val itemSelected = isFocused && nextItemToFill == i
            val itemFilled = i < nextItemToFill
            var itemState: IntArray? = null
            if (itemFilled) {
                itemState = FILLED_STATE
            } else if (itemSelected) {
                itemState = SELECTED_STATE
            }
            paint.setColor(itemState?.let { getLineColorForState(it?.get(i)) } ?: currentLineColor)

            updateItemRectF(i)
            updateCenterPoint()
            canvas.save()
            if (viewType == VIEW_TYPE_RECTANGLE) {
                updateOtpViewBoxPath(i)
                canvas.clipPath(path)
            }
            drawItemBackground(canvas, itemState)
            canvas.restore()
            if (itemSelected) {
                drawCursor(canvas)
            }
            if (viewType == VIEW_TYPE_RECTANGLE) {
                drawOtpBox(canvas, i)
            } else if (viewType == VIEW_TYPE_LINE) {
                drawOtpLine(canvas, i)
            }
            if (DBG) {
                drawAnchorLine(canvas)
            }
            if (rtlTextDirection) {
                val reversedPosition = otpViewItemCount - i
                if (text!!.length >= reversedPosition) {
                    drawInput(canvas, i)
                } else if (!TextUtils.isEmpty(hint) && hint.length == otpViewItemCount) {
                    drawHint(canvas, i)
                }
            } else {
                if (text!!.length > i) {
                    drawInput(canvas, i)
                } else if (!TextUtils.isEmpty(hint) && hint.length == otpViewItemCount) {
                    drawHint(canvas, i)
                }
            }
        }
        if (isFocused
            && text != null && text!!.length != otpViewItemCount && viewType == VIEW_TYPE_RECTANGLE
        ) {
            val index = text!!.length
            updateItemRectF(index)
            updateCenterPoint()
            updateOtpViewBoxPath(index)
            paint.setColor(getLineColorForState(*SELECTED_STATE))
            drawOtpBox(canvas, index)
        }
    }

    private fun drawInput(canvas: Canvas, i: Int) {
        //allows masking for all number keyboard
        if (maskingChar != null &&
            (isNumberInputType(inputType) ||
                    isPasswordInputType(inputType))
        ) {
            drawMaskingText(canvas, i, Character.toString(maskingChar!![0]))
        } else if (isPasswordInputType(inputType)) {
            drawCircle(canvas, i)
        } else {
            drawText(canvas, i)
        }
    }

    private fun getLineColorForState(vararg states: Int): Int {
        return if (lineColors != null) lineColors!!.getColorForState(
            states,
            currentLineColor
        ) else currentLineColor
    }

    private fun drawItemBackground(canvas: Canvas, backgroundState: IntArray?) {
        if (itemBackground == null) {
            return
        }
        val delta = lineWidth.toFloat() / 2
        val left = Math.round(itemBorderRect.left - delta)
        val top = Math.round(itemBorderRect.top - delta)
        val right = Math.round(itemBorderRect.right + delta)
        val bottom = Math.round(itemBorderRect.bottom + delta)
        itemBackground!!.setBounds(left, top, right, bottom)
        if (viewType != VIEW_TYPE_NONE) {
            itemBackground!!.state = backgroundState ?: drawableState
        }
        itemBackground!!.draw(canvas)
    }

    private fun updateOtpViewBoxPath(i: Int) {
        var drawRightCorner = false
        var drawLeftCorner = false
        if (otpViewItemSpacing != 0) {
            drawRightCorner = true
            drawLeftCorner = drawRightCorner
        } else {
            if (i == 0 && i != otpViewItemCount - 1) {
                drawLeftCorner = true
            }
            if (i == otpViewItemCount - 1 && i != 0) {
                drawRightCorner = true
            }
        }
        updateRoundRectPath(
            itemBorderRect,
            otpViewItemRadius.toFloat(),
            otpViewItemRadius.toFloat(),
            drawLeftCorner,
            drawRightCorner
        )
    }

    private fun drawOtpBox(canvas: Canvas, i: Int) {
        if (text != null && hideLineWhenFilled && i < text!!.length) {
            return
        }
        canvas.drawPath(path, paint)
    }

    private fun drawOtpLine(canvas: Canvas, i: Int) {
        if (text != null && hideLineWhenFilled && i < text!!.length) {
            return
        }
        var drawLeft: Boolean
        var drawRight: Boolean
        drawRight = true
        drawLeft = drawRight
        if (otpViewItemSpacing == 0 && otpViewItemCount > 1) {
            if (i == 0) {
                drawRight = false
            } else if (i == otpViewItemCount - 1) {
                drawLeft = false
            } else {
                drawRight = false
                drawLeft = drawRight
            }
        }
        paint.setStyle(Paint.Style.FILL)
        paint.setStrokeWidth(lineWidth.toFloat() / 10)
        val halfLineWidth = lineWidth.toFloat() / 2
        itemLineRect[itemBorderRect.left - halfLineWidth, itemBorderRect.bottom - halfLineWidth, itemBorderRect.right + halfLineWidth] =
            itemBorderRect.bottom + halfLineWidth
        updateRoundRectPath(
            itemLineRect,
            otpViewItemRadius.toFloat(),
            otpViewItemRadius.toFloat(),
            drawLeft,
            drawRight
        )
        canvas.drawPath(path, paint)
    }

    private fun drawCursor(canvas: Canvas) {
        if (drawCursor) {
            val cx = itemCenterPoint.x
            val cy = itemCenterPoint.y
            val y = cy - cursorHeight / 2
            val color: Int = paint.getColor()
            val width: Float = paint.getStrokeWidth()
            paint.setColor(cursorColor)
            paint.setStrokeWidth(cursorWidth.toFloat())
            canvas.drawLine(cx, y, cx, y + cursorHeight, paint)
            paint.setColor(color)
            paint.setStrokeWidth(width)
        }
    }

    private fun updateRoundRectPath(rectF: RectF, rx: Float, ry: Float, l: Boolean, r: Boolean) {
        updateRoundRectPath(rectF, rx, ry, l, r, r, l)
    }

    private fun updateRoundRectPath(
        rectF: RectF, rx: Float, ry: Float,
        tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
    ) {
        path.reset()
        val l = rectF.left
        val t = rectF.top
        val r = rectF.right
        val b = rectF.bottom
        val w = r - l
        val h = b - t
        val lw = w - 2 * rx
        val lh = h - 2 * ry
        path.moveTo(l, t + ry)
        if (tl) {
            path.rQuadTo(0f, -ry, rx, -ry)
        } else {
            path.rLineTo(0f, -ry)
            path.rLineTo(rx, 0f)
        }
        path.rLineTo(lw, 0f)
        if (tr) {
            path.rQuadTo(rx, 0f, rx, ry)
        } else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, ry)
        }
        path.rLineTo(0f, lh)
        if (br) {
            path.rQuadTo(0f, ry, -rx, ry)
        } else {
            path.rLineTo(0f, ry)
            path.rLineTo(-rx, 0f)
        }
        path.rLineTo(-lw, 0f)
        if (bl) {
            path.rQuadTo(-rx, 0f, -rx, -ry)
        } else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, -ry)
        }
        path.rLineTo(0f, -lh)
        path.close()
    }

    private fun updateItemRectF(i: Int) {
        val halfLineWidth = lineWidth.toFloat() / 2
        var left = (scrollX
                + ViewCompat.getPaddingStart(this)
                + i * (otpViewItemSpacing + otpViewItemWidth) + halfLineWidth)
        if (otpViewItemSpacing == 0 && i > 0) {
            left = left - lineWidth * i
        }
        val right = left + otpViewItemWidth - lineWidth
        val top = scrollY + paddingTop + halfLineWidth
        val bottom = top + otpViewItemHeight - lineWidth
        itemBorderRect[left, top, right] = bottom
    }

    private fun drawText(canvas: Canvas, i: Int) {
        val paint: Paint? = getPaintByIndex(i)
        paint?.setColor(currentTextColor)
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition: Int
            reversedCharPosition = if (text == null) {
                reversedPosition
            } else {
                reversedPosition - text!!.length
            }
            if (reversedCharPosition <= 0 && text != null) {
                drawTextAtBox(canvas, paint, text, Math.abs(reversedCharPosition))
            }
        } else if (text != null) {
            drawTextAtBox(canvas, paint, text, i)
        }
    }

    private fun drawMaskingText(canvas: Canvas, i: Int, maskingChar: String) {
        val paint: Paint? = getPaintByIndex(i)
        paint?.setColor(currentTextColor)
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition: Int
            reversedCharPosition = if (text == null) {
                reversedPosition
            } else {
                reversedPosition - text!!.length
            }
            if (reversedCharPosition <= 0 && text != null) {
                drawTextAtBox(
                    canvas, paint, text.toString().replace(".".toRegex(), maskingChar),
                    Math.abs(reversedCharPosition)
                )
            }
        } else if (text != null) {
            drawTextAtBox(canvas, paint, text.toString().replace(".".toRegex(), maskingChar), i)
        }
    }

    private fun drawHint(canvas: Canvas, i: Int) {
        val paint: Paint? = getPaintByIndex(i)
        paint?.setColor(currentHintTextColor)
        if (rtlTextDirection) {
            val reversedPosition = otpViewItemCount - i
            val reversedCharPosition = reversedPosition - hint.length
            if (reversedCharPosition <= 0) {
                drawTextAtBox(canvas, paint, hint, Math.abs(reversedCharPosition))
            }
        } else {
            drawTextAtBox(canvas, paint, hint, i)
        }
    }

    private fun drawTextAtBox(canvas: Canvas, paint: Paint?, text: CharSequence?, charAt: Int) {
        paint?.getTextBounds(text.toString(), charAt, charAt + 1, textRect)
        val cx = itemCenterPoint.x
        val cy = itemCenterPoint.y
        val x: Float = cx - Math.abs(textRect.width() as Float) / 2 - textRect.left
        val y: Float = cy + Math.abs(textRect.height() as Float) / 2 - textRect.bottom
        if (isAllCaps_!!) {
            if (paint != null) {
                canvas.drawText(text.toString().toUpperCase(), charAt, charAt + 1, x, y, paint)
            }
        } else {
            if (text != null) {
                if (paint != null) {
                    canvas.drawText(text, charAt, charAt + 1, x, y, paint)
                }
            }
        }
    }

    private fun drawCircle(canvas: Canvas, i: Int) {
        val paint: Paint? = getPaintByIndex(i)
        val cx = itemCenterPoint.x
        val cy = itemCenterPoint.y
        if (rtlTextDirection) {
            val reversedItemPosition = otpViewItemCount - i
            val reversedCharPosition = reversedItemPosition - hint.length
            if (reversedCharPosition <= 0) {
                if (paint != null) {
                    canvas.drawCircle(cx, cy, paint.getTextSize() / 2, paint)
                }
            }
        } else {
            if (paint != null) {
                canvas.drawCircle(cx, cy, paint.getTextSize() / 2, paint)
            }
        }
    }

    private fun getPaintByIndex(i: Int): Paint? {
        return if (text != null && isAnimationEnable && i == text!!.length - 1) {
            animatorTextPaint!!.color = getPaint().color
            animatorTextPaint
        } else {
            getPaint()
        }
    }

    private fun drawAnchorLine(canvas: Canvas) {
        var cx = itemCenterPoint.x
        var cy = itemCenterPoint.y
        paint.setStrokeWidth(1f)
        cx -= paint.getStrokeWidth() / 2
        cy -= paint.getStrokeWidth() / 2
        path.reset()
        path.moveTo(cx, itemBorderRect.top)
        path.lineTo(cx, itemBorderRect.top + Math.abs(itemBorderRect.height()))
        canvas.drawPath(path, paint)
        path.reset()
        path.moveTo(itemBorderRect.left, cy)
        path.lineTo(itemBorderRect.left + Math.abs(itemBorderRect.width()), cy)
        canvas.drawPath(path, paint)
        path.reset()
        paint.setStrokeWidth(lineWidth.toFloat())
    }

    private fun updateColors() {
        var shouldInvalidate = false
        val color = if (lineColors != null) lineColors!!.getColorForState(
            drawableState,
            0
        ) else currentTextColor
        if (color != currentLineColor) {
            currentLineColor = color
            shouldInvalidate = true
        }
        if (shouldInvalidate) {
            invalidate()
        }
    }

    private fun updateCenterPoint() {
        val cx = itemBorderRect.left + Math.abs(itemBorderRect.width()) / 2
        val cy = itemBorderRect.top + Math.abs(itemBorderRect.height()) / 2
        itemCenterPoint[cx] = cy
    }

    override fun getDefaultMovementMethod(): MovementMethod {
        return DefaultMovementMethod.instance!!
    }

    /**
     * Sets the line color for all the states (normal, selected,
     * focused) to be this color.
     *
     * @param color A color value in the form 0xAARRGGBB.
     * Do not pass a resource ID. To get a color value from a resource ID, call
     * [getColor][androidx.core.content.ContextCompat.getColor].
     * @attr ref R.styleable#OtpView_lineColor
     * @see .setLineColor
     * @see .getLineColors
     */
    fun setLineColor(@ColorInt color: Int) {
        lineColors = ColorStateList.valueOf(color)
        updateColors()
    }

    /**
     * Sets the line color.
     *
     * @attr ref R.styleable#OtpView_lineColor
     * @see .setLineColor
     * @see .getLineColors
     */
    fun setLineColor(colors: ColorStateList?) {
        requireNotNull(colors) { "Color cannot be null" }
        lineColors = colors
        updateColors()
    }

    /**
     * Sets the line width.
     *
     * @attr ref R.styleable#OtpView_lineWidth
     * @see .getLineWidth
     */
    fun setLineWidth(@Px borderWidth: Int) {
        lineWidth = borderWidth
        checkItemRadius()
        requestLayout()
    }

    /**
     * @return Returns the width of the item's line.
     * @see .setLineWidth
     */
    fun getLineWidth(): Int {
        return lineWidth
    }
    /**
     * @return Returns the count of items.
     * @see .setItemCount
     */
    /**
     * Sets the count of items.
     *
     * @attr ref R.styleable#OtpView_itemCount
     * @see .getItemCount
     */
    var itemCount: Int
        get() = otpViewItemCount
        set(count) {
            otpViewItemCount = count
            setMaxLength(count)
            requestLayout()
        }
    /**
     * @return Returns the radius of square.
     * @see .setItemRadius
     */
    /**
     * Sets the radius of square.
     *
     * @attr ref R.styleable#OtpView_itemRadius
     * @see .getItemRadius
     */
    var itemRadius: Int
        get() = otpViewItemRadius
        set(itemRadius) {
            otpViewItemRadius = itemRadius
            checkItemRadius()
            requestLayout()
        }
    /**
     * @return Returns the spacing between two items.
     * @see .setItemSpacing
     */
    /**
     * Specifies extra space between two items.
     *
     * @attr ref R.styleable#OtpView_itemSpacing
     * @see .getItemSpacing
     */
    @get:Px
    var itemSpacing: Int
        get() = otpViewItemSpacing
        set(itemSpacing) {
            otpViewItemSpacing = itemSpacing
            requestLayout()
        }
    /**
     * @return Returns the height of item.
     * @see .setItemHeight
     */
    /**
     * Sets the height of item.
     *
     * @attr ref R.styleable#OtpView_itemHeight
     * @see .getItemHeight
     */
    var itemHeight: Int
        get() = otpViewItemHeight
        set(itemHeight) {
            otpViewItemHeight = itemHeight
            updateCursorHeight()
            requestLayout()
        }
    /**
     * @return Returns the width of item.
     * @see .setItemWidth
     */
    /**
     * Sets the width of item.
     *
     * @attr ref R.styleable#OtpView_itemWidth
     * @see .getItemWidth
     */
    var itemWidth: Int
        get() = otpViewItemWidth
        set(itemWidth) {
            otpViewItemWidth = itemWidth
            checkItemRadius()
            requestLayout()
        }

    /**
     * Specifies whether the text animation should be enabled or disabled.
     * By the default, the animation is disabled.
     *
     * @param enable True to start animation when adding text, false to transition immediately
     */
    fun setAnimationEnable(enable: Boolean) {
        isAnimationEnable = enable
    }

    /**
     * Specifies whether the line (border) should be hidden or visible when text entered.
     * By the default, this flag is false and the line is always drawn.
     *
     * @param hideLineWhenFilled true to hide line on a position where text entered,
     * false to always show line
     * @attr ref R.styleable#OtpView_hideLineWhenFilled
     */
    fun setHideLineWhenFilled(hideLineWhenFilled: Boolean) {
        this.hideLineWhenFilled = hideLineWhenFilled
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(size)
        updateCursorHeight()
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(unit, size)
        updateCursorHeight()
    }

    fun setOtpCompletionListener(otpCompletionListener: OnOtpCompletionListener?) {
        onOtpCompletionListener = otpCompletionListener
    }
    //region ItemBackground
    /**
     * Set the item background to a given resource. The resource should refer to
     * a Drawable object or 0 to remove the item background.
     *
     * @param resId The identifier of the resource.
     * @attr ref R.styleable#OtpView_android_itemBackground
     */
    fun setItemBackgroundResources(@DrawableRes resId: Int) {
        if (resId != 0 && itemBackgroundResource != resId) {
            return
        }
        itemBackground = ResourcesCompat.getDrawable(resources, resId, context.theme)
        setItemBackground(itemBackground)
        itemBackgroundResource = resId
    }

    /**
     * Sets the item background color for this view.
     *
     * @param color the color of the item background
     */
    fun setItemBackgroundColor(@ColorInt color: Int) {
        if (itemBackground is ColorDrawable) {
            (itemBackground?.mutate() as ColorDrawable).color = color
            itemBackgroundResource = 0
        } else {
            setItemBackground(ColorDrawable(color))
        }
    }

    /**
     * Set the item background to a given Drawable, or remove the background.
     *
     * @param background The Drawable to use as the item background, or null to remove the
     * item background
     */
    fun setItemBackground(background: Drawable?) {
        itemBackgroundResource = 0
        itemBackground = background
        invalidate()
    }
    //endregion
    //region Cursor
    /**
     * Sets the width (in pixels) of cursor.
     *
     * @attr ref R.styleable#OtpView_cursorWidth
     * @see .getCursorWidth
     */
    fun setCursorWidth(@Px width: Int) {
        cursorWidth = width
        if (isCursorVisible()) {
            invalidateCursor(true)
        }
    }

    /**
     * @return Returns the width (in pixels) of cursor.
     * @see .setCursorWidth
     */
    fun getCursorWidth(): Int {
        return cursorWidth
    }

    /**
     * Sets the cursor color.
     *
     * @param color A color value in the form 0xAARRGGBB.
     * Do not pass a resource ID. To get a color value from a resource ID, call
     * [getColor][androidx.core.content.ContextCompat.getColor].
     * @attr ref R.styleable#OtpView_cursorColor
     * @see .getCursorColor
     */
    fun setCursorColor(@ColorInt color: Int) {
        cursorColor = color
        if (isCursorVisible()) {
            invalidateCursor(true)
        }
    }

    /**
     * Gets the cursor color.
     *
     * @return Return current cursor color.
     * @see .setCursorColor
     */
    fun getCursorColor(): Int {
        return cursorColor
    }

    fun setMaskingChar(maskingChar: String?) {
        this.maskingChar = maskingChar
        requestLayout()
    }

    fun getMaskingChar(): String? {
        return maskingChar
    }

    override fun setCursorVisible(visible: Boolean) {
        if (isCursorVisible != visible) {
            isCursorVisible = visible
            invalidateCursor(isCursorVisible)
            makeBlink()
        }
    }

    override fun isCursorVisible(): Boolean {
        return isCursorVisible
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == View.SCREEN_STATE_ON) {
            resumeBlink()
        } else if (screenState == View.SCREEN_STATE_OFF) {
            suspendBlink()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resumeBlink()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        suspendBlink()
    }

    private fun shouldBlink(): Boolean {
        return isCursorVisible() && isFocused
    }

    private fun makeBlink() {
        if (shouldBlink()) {
            if (blink == null) {
                blink = Blink()
            }
            removeCallbacks(blink)
            drawCursor = false
            postDelayed(blink, BLINK.toLong())
        } else {
            if (blink != null) {
                removeCallbacks(blink)
            }
        }
    }

    private fun suspendBlink() {
        if (blink != null) {
            blink!!.cancel()
            invalidateCursor(false)
        }
    }

    private fun resumeBlink() {
        if (blink != null) {
            blink!!.unCancel()
            makeBlink()
        }
    }

    private fun invalidateCursor(showCursor: Boolean) {
        if (drawCursor != showCursor) {
            drawCursor = showCursor
            invalidate()
        }
    }

    private fun updateCursorHeight() {
        val delta = 2 * dpToPx()
        cursorHeight = if (otpViewItemHeight - textSize > delta) textSize + delta else textSize
    }

    private inner class Blink : Runnable {
        private var cancelled = false
        override fun run() {
            if (cancelled) {
                return
            }
            removeCallbacks(this)
            if (shouldBlink()) {
                invalidateCursor(!drawCursor)
                postDelayed(this, BLINK.toLong())
            }
        }

        fun cancel() {
            if (!cancelled) {
                removeCallbacks(this)
                cancelled = true
            }
        }

        fun unCancel() {
            cancelled = false
        }
    }

    //endregion
    private fun dpToPx(): Int {
        return (2.toFloat() * resources.displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        private const val DBG = false
        private const val BLINK = 500
        private const val DEFAULT_COUNT = 4
        private val NO_FILTERS = arrayOfNulls<InputFilter>(0)
        private val SELECTED_STATE = intArrayOf(

        )
        private val FILLED_STATE = intArrayOf(
            R.attr.OtpState_filled
        )
        private const val VIEW_TYPE_RECTANGLE = 0
        private const val VIEW_TYPE_LINE = 1
        private const val VIEW_TYPE_NONE = 2
        private fun isPasswordInputType(inputType: Int): Boolean {
            val variation =
                inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
            return (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                    == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
        }

        private fun isNumberInputType(inputType: Int): Boolean {
            return inputType == EditorInfo.TYPE_CLASS_NUMBER
        }
    }

    init {
        val res: Resources = resources
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setStyle(Paint.Style.STROKE)
        animatorTextPaint!!.set(getPaint())
        val theme: Resources.Theme = context.getTheme()
        val typedArray: TypedArray =
            theme.obtainStyledAttributes(attrs, R.styleable.OtpView, defStyleAttr, 0)
        viewType = typedArray.getInt(R.styleable.OtpView_OtpViewType, VIEW_TYPE_NONE)
        otpViewItemCount = typedArray.getInt(R.styleable.OtpView_OtpItemCount, DEFAULT_COUNT)
        otpViewItemHeight = typedArray.getDimension(
            R.styleable.OtpView_OtpItemHeight,
            res.getDimensionPixelSize(R.dimen.otp_view_item_size).toFloat()
        ).toInt()
        otpViewItemWidth = typedArray.getDimension(
            R.styleable.OtpView_OtpItemWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_item_size).toFloat()
        ).toInt()
        otpViewItemSpacing = typedArray.getDimensionPixelSize(
            R.styleable.OtpView_OtpItemSpacing,
            res.getDimensionPixelSize(R.dimen.otp_view_item_spacing)
        )
        otpViewItemRadius = typedArray.getDimension(R.styleable.OtpView_OtpItemRadius, 0f).toInt()
        lineWidth = typedArray.getDimension(
            R.styleable.OtpView_OtpLineWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_item_line_width).toFloat()
        ).toInt()
        lineColors = typedArray.getColorStateList(R.styleable.OtpView_OtpLineColor)
        isCursorVisible = typedArray.getBoolean(R.styleable.OtpView_android_cursorVisible, true)
        cursorColor = typedArray.getColor(R.styleable.OtpView_OtpCursorColor, currentTextColor)
        cursorWidth = typedArray.getDimensionPixelSize(
            R.styleable.OtpView_OtpCursorWidth,
            res.getDimensionPixelSize(R.dimen.otp_view_cursor_width)
        )
        itemBackground = typedArray.getDrawable(R.styleable.OtpView_android_itemBackground)
        hideLineWhenFilled = typedArray.getBoolean(R.styleable.OtpView_OtpHideLineWhenFilled, false)
        rtlTextDirection = typedArray.getBoolean(R.styleable.OtpView_OtpRtlTextDirection, false)
        maskingChar = typedArray.getString(R.styleable.OtpView_OtpMaskingChar)
        isAllCaps = typedArray.getBoolean(R.styleable.OtpView_android_textAllCaps, false)
        typedArray.recycle()
        if (lineColors != null) {
            currentLineColor = lineColors!!.defaultColor
        }
        updateCursorHeight()
        checkItemRadius()
        setMaxLength(otpViewItemCount)
        paint.setStrokeWidth(lineWidth.toFloat())
        setupAnimator()
        setTextIsSelectable(false)
    }

    interface OnOtpCompletionListener {
        fun onOtpCompleted(otp: String?)
    }
}