package com.tenclouds.fluidbottomnavigation

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener
import kotlinx.android.synthetic.main.item.view.*

class FluidBottomNavigation : FrameLayout {

    companion object {
        @VisibleForTesting var IS_UNIT_TEST = false
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var items: List<FluidBottomNavigationItem> = listOf()
        set(value) {
            if (value.size < 3)
                throw TooMuchItemsException(resources)
            if (value.size > 5)
                throw TooLittleItemsException(resources)

            field = value
            drawLayout()
        }


    var onTabSelectedListener: OnTabSelectedListener? = null


    var accentColor: Int? = null
    var backColor: Int? = null
    var iconColor: Int? = null
    var iconSelectedColor: Int? = null
    var textColor: Int? = null
    var textFont: Typeface? = null

    var selectedTabItem: FluidBottomNavigationItem? = null

    internal var bottomBarHeight: Int? = null
    internal var bottomBarWidth: Int? = null

    @VisibleForTesting var isVisible = true
    private var selectedTabPosition = DEFAULT_SELECTED_TAB_POSITION
    private var backgroundView: View? = null
    private val views: MutableList<View> = ArrayList()

    private fun init(attrs: AttributeSet?) {
        getAttributesOrDefaultValues(attrs)
        clipToPadding = false
        layoutParams =
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        bottomBarHeight ?: 0)
    }

    fun selectTab(position: Int) {
        if (position == selectedTabPosition) return

        if (IS_UNIT_TEST.not()) {
            animateDeselectItemView(views[selectedTabPosition])
            animateSelectItemView(views[position])
        }

        this.selectedTabPosition = position
        this.selectedTabItem = items[position]

        onTabSelectedListener?.onTabSelected(position)
    }

    fun show() {
        if (isVisible.not()) {
            animateShow()
            isVisible = true
        }
    }

    fun hide() {
        if (isVisible) {
            animateHide()
            isVisible = false
        }
    }

    private fun drawLayout() {
        if (IS_UNIT_TEST) return

        bottomBarHeight = resources.getDimension(R.dimen.fluidBottomNavigationHeightWithOpacity).toInt()
        backgroundView = View(context)

        removeAllViews()
        views.clear()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    calculateHeight(bottomBarHeight ?: 0)
            ).let {
                addView(backgroundView, it)
            }
        }

        post { requestLayout() }

        LinearLayout(context)
                .apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                }
                .let {
                    Pair(
                            it,
                            FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    bottomBarHeight ?: 0,
                                    Gravity.BOTTOM))
                }
                .let { (linearLayoutContainer, layoutParams) ->
                    addView(linearLayoutContainer, layoutParams)
                    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            viewTreeObserver.removeOnGlobalLayoutListenerCompat(this)
                            bottomBarWidth = width
                            drawItemsViews(linearLayoutContainer)
                        }
                    })
                }
    }

    private fun drawItemsViews(linearLayout: LinearLayout) {
        if (bottomBarWidth == 0 || items.isEmpty()) {
            return
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemViewHeight = resources.getDimension(R.dimen.fluidBottomNavigationHeightWithOpacity)
        val itemViewWidth = ((bottomBarWidth ?: 0) / items.size)

        for (itemPosition in items.indices) {
            inflater
                    .inflate(R.layout.item, this, false)
                    .let {
                        views.add(it)
                        linearLayout
                                .addView(it,
                                        FrameLayout.LayoutParams(
                                                itemViewWidth,
                                                itemViewHeight.toInt()))
                    }
            drawItemView(itemPosition)
        }
    }

    private fun drawItemView(position: Int) {
        if (IS_UNIT_TEST) return

        val view = views[position]
        val item = items[position]

        with(view) {
            if (items.size > 3) {
                container.setPadding(0, 0, 0, container.paddingBottom)
            }

            with(icon) {
                setImageDrawable(item.drawable)
                if (selectedTabPosition == position)
                    animateSelectItemView(view)
                else
                    setTintColor(getColor(iconColor))
            }

            with(title) {
                typeface = textFont
                text = item.title
                setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimension(R.dimen.fluidBottomNavigationTextSize))
            }

            with(topAnimatedImageView) {
                AnimatedVectorDrawableCompat
                        .create(context, R.drawable.top)
                        .let {
                            it?.stop()
                            setImageDrawable(it)
                        }
            }

            backgroundContainer.setOnClickListener { selectTab(position) }
        }
    }

    fun getTabsSize() = items.size

    private fun getAttributesOrDefaultValues(attrs: AttributeSet?) {
        if (IS_UNIT_TEST) return

        bottomBarHeight = resources.getDimension(R.dimen.fluidBottomNavigationHeightWithOpacity).toInt()
        accentColor = ContextCompat.getColor(context, R.color.accentColor)
        backColor = ContextCompat.getColor(context, R.color.backColor)
        textColor = ContextCompat.getColor(context, R.color.textColor)
        iconColor = ContextCompat.getColor(context, R.color.iconColor)
        iconSelectedColor = ContextCompat.getColor(context, R.color.iconSelectedColor)
        textFont = ResourcesCompat.getFont(context, R.font.rubik_regular)

        if (attrs != null) {
            with(context
                    .obtainStyledAttributes(
                            attrs,
                            R.styleable.FluidBottomNavigation,
                            0, 0)) {
                selectedTabPosition = getInt(
                        R.styleable.FluidBottomNavigation_defaultTabPosition,
                        DEFAULT_SELECTED_TAB_POSITION)

                accentColor = getColor(
                        R.styleable.FluidBottomNavigation_accentColor,
                        ContextCompat.getColor(context, R.color.accentColor))
                backColor = getColor(
                        R.styleable.FluidBottomNavigation_backColor,
                        ContextCompat.getColor(context, R.color.backColor))
                iconColor = getColor(
                        R.styleable.FluidBottomNavigation_iconColor,
                        ContextCompat.getColor(context, R.color.iconColor))
                textColor = getColor(
                        R.styleable.FluidBottomNavigation_textColor,
                        ContextCompat.getColor(context, R.color.iconSelectedColor))
                iconSelectedColor = getColor(
                        R.styleable.FluidBottomNavigation_iconSelectedColor,
                        ContextCompat.getColor(context, R.color.textColor))
                textFont = ResourcesCompat.getFont(
                        context,
                        getResourceId(
                                R.styleable.FluidBottomNavigation_textFont,
                                R.font.rubik_regular))
                recycle()
            }
        }
    }

    fun getSelectedTabPosition() = this.selectedTabPosition

    override fun onSaveInstanceState() =
            Bundle()
                    .apply {
                        putInt(EXTRA_SELECTED_TAB_POSITION, selectedTabPosition)
                        putParcelable(EXTRA_SELECTED_SUPER_STATE, super.onSaveInstanceState())
                    }

    override fun onRestoreInstanceState(state: Parcelable?) =
            if (state is Bundle?) {
                selectedTabPosition = state
                        ?.getInt(EXTRA_SELECTED_TAB_POSITION) ?: DEFAULT_SELECTED_TAB_POSITION
                state?.getParcelable(EXTRA_SELECTED_SUPER_STATE)
            } else {
                state
            }
                    .let {
                        super.onRestoreInstanceState(it)
                    }
}
