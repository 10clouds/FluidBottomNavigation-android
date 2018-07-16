package com.tenclouds.fluidbottomnavigation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.VisibleForTesting
import android.support.v4.content.ContextCompat
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
import kotlinx.android.synthetic.main.fluid_bottom_navigation_item.view.*

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
    var bottomBarWidth: Int? = null

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
        animateDeselectItemView(selectedTabPosition)
        animateSelectItemView(position)

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

        removeAllViews()
        views.clear()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    calculateHeight(height ?: 0)
            ).let {
                addView(backgroundView, it)
            }
        }

        post { requestLayout() }

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

        val itemViewHeight = resources.getDimension(R.dimen.fluidBottomNavigationHeight)
        val itemViewWidth = ((bottomBarWidth ?: 0) / items.size)

        for (itemPosition in items.indices) {
            inflater
                    .inflate(R.layout.fluid_bottom_navigation_item, this, false)
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
                container.setPadding(0, container.paddingTop, 0, container.paddingBottom)
            }

            with(itemIcon) {
                setImageDrawable(item.drawable)
                if (selectedTabPosition == position) {
                    isSelected = true
                    animateSelectItemView(position)
                } else {
                    isSelected = false
                }
            }

            with(itemTitle) {
//            if (titleTypeface != null) {
//                title.typeface = titleTypeface
//            } TODO
                text = item.title
                setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimension(R.dimen.fluidBottomNavigationTextSize))
            }

            setOnClickListener { selectTab(position) }
        }
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
