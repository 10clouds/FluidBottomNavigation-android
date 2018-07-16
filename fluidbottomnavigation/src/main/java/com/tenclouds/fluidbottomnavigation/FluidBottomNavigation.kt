package com.tenclouds.fluidbottomnavigation

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener

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

    private val views: MutableList<View> = ArrayList()
    private var backgroundView: View? = null
    private var selectedTabPosition = DEFAULT_SELECTED_TAB_POSITION
    var height: Int? = null
    var accentColor: Int? = null
    var backColor: Int? = null
    var iconColor: Int? = null
    var iconSelectedColor: Int? = null
    var textColor: Int? = null

    var selectedTabItem: FluidBottomNavigationItem? = null
    @VisibleForTesting var isVisible = true

    private fun init(attrs: AttributeSet?) {
        getAttributesOrDefaultValues(attrs)
        clipToPadding = false
        layoutParams =
                ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        height ?: 0)
    }

    fun selectTab(position: Int) {
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

        height = resources.getDimension(R.dimen.fluidBottomNavigationHeight).toInt()
        backgroundView = View(context)

        clearViews()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    calculateHeight(height ?: 0)
            ).let {
                addView(backgroundView, it)
            }
        }

        LinearLayout(context)
                .apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    setBackgroundColor(ContextCompat.getColor(context, backColor ?: -1))
                }
                .let {
                    Pair(
                            it,
                            FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    height ?: 0,
                                    Gravity.BOTTOM))
                }
                .let { (linearLayoutContainer, layoutParams) ->
                    addView(linearLayoutContainer, layoutParams)
                }

        post { requestLayout() }
    }

    private fun createItems(linearLayout: LinearLayout) {

    }

    @SuppressLint("NewApi", "ResourceType")
    @Suppress("NAME_SHADOWING")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun calculateHeight(layoutHeight: Int): Int {
        var layoutHeight = layoutHeight
        var navigationBarHeight = 0

        resources.getIdentifier("navigation_bar_height", "dimen", "android")
                .let {
                    if (it > 0)
                        navigationBarHeight = resources.getDimensionPixelSize(it)
                }

        intArrayOf(android.R.attr.windowTranslucentNavigation)
                .let {
                    with(context.theme
                            .obtainStyledAttributes(it)) {
                        val translucentNavigation = getBoolean(0, true)
                        if (hasImmersive() && !translucentNavigation) {
                            layoutHeight += navigationBarHeight
                        }
                        recycle()
                    }
                }

        return layoutHeight
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hasImmersive(): Boolean {

        val displayMetrics = DisplayMetrics()
        val realDisplayMetrics = DisplayMetrics()
        with((context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay) {
            getMetrics(displayMetrics)
            getRealMetrics(realDisplayMetrics)
        }

        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        return realWidth > displayWidth || realHeight > displayHeight
    }

    private fun clearViews() {
        removeAllViews()
        views.clear()
    }

    fun redraw() {
        drawLayout()
    }

    fun getTabsSize() = items.size

    private fun getAttributesOrDefaultValues(attrs: AttributeSet?) {
        if (IS_UNIT_TEST) return

        height = resources.getDimension(R.dimen.fluidBottomNavigationHeight).toInt()
        accentColor = ContextCompat.getColor(context, R.color.accentColor)
        backColor = ContextCompat.getColor(context, R.color.backColor)
        textColor = ContextCompat.getColor(context, R.color.textColor)
        iconColor = ContextCompat.getColor(context, R.color.iconColor)
        iconSelectedColor = ContextCompat.getColor(context, R.color.iconSelectedColor)

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
