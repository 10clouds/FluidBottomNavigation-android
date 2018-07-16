package com.tenclouds.fluidbottomnavigationexample

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var state: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        fluidBottomNavigation.accentColor = R.color.colorPrimaryDark
        fluidBottomNavigation.backColor = R.color.colorPrimaryDark
        fluidBottomNavigation.textColor = R.color.colorPrimaryDark
        fluidBottomNavigation.iconColor = R.color.colorPrimary
        fluidBottomNavigation.iconSelectedColor = R.color.iconSelectedColor

        fluidBottomNavigation.items =
                listOf(
                        FluidBottomNavigationItem(
                                getString(R.string.news),
                                ContextCompat.getDrawable(this, R.drawable.ic_news)),
                        FluidBottomNavigationItem(
                                getString(R.string.inbox),
                                ContextCompat.getDrawable(this, R.drawable.ic_inbox)),
                        FluidBottomNavigationItem(
                                getString(R.string.calendar),
                                ContextCompat.getDrawable(this, R.drawable.ic_calendar)),
                        FluidBottomNavigationItem(
                                getString(R.string.chat),
                                ContextCompat.getDrawable(this, R.drawable.ic_chat)),
                        FluidBottomNavigationItem(
                                getString(R.string.profile),
                                ContextCompat.getDrawable(this, R.drawable.ic_profile)))
    }

}