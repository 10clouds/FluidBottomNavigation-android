package com.tenclouds.fluidbottomnavigationexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fluidBottomNavigation.accentColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.backColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.textColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fluidBottomNavigation.iconColor = ContextCompat.getColor(this, R.color.colorPrimary)
        fluidBottomNavigation.iconSelectedColor = ContextCompat.getColor(this, R.color.iconSelectedColor)

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