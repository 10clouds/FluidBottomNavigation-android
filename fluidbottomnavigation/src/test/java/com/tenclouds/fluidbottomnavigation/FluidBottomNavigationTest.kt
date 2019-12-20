package com.tenclouds.fluidbottomnavigation

import android.app.Activity
import com.nhaarman.mockitokotlin2.verify
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener
import com.tenclouds.fluidbottomnavigation.util.ShadowResourcesCompat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
        packageName = "com.tenclouds.fluidbottomnavigation",
        sdk = [21],
        shadows = [(ShadowResourcesCompat::class)])
class FluidBottomNavigationTest {

    private lateinit var fluidBottomNavigation: FluidBottomNavigation
    private val controller = Robolectric.buildActivity(Activity::class.java).create().start()
    private val fluidBottomNavigationItems =
            listOf(
                    FluidBottomNavigationItem("Tab1"),
                    FluidBottomNavigationItem("Tab2"),
                    FluidBottomNavigationItem("Tab3"))
    private val onTabSelectedListener = mock(OnTabSelectedListener::class.java)

    @Before
    fun setup() {
        fluidBottomNavigation =
                FluidBottomNavigation(controller.get())
                        .apply {
                            items = fluidBottomNavigationItems
                            onTabSelectedListener = this@FluidBottomNavigationTest.onTabSelectedListener
                        }
    }

    @Test
    fun `selected tab position and item sets after context recreate`() {
        fluidBottomNavigation.selectTab(1)
        controller.configurationChange()
        assertEquals(1, fluidBottomNavigation.getSelectedTabPosition())
        fluidBottomNavigation.selectTab(2)
        controller.configurationChange()
        assertEquals(2, fluidBottomNavigation.getSelectedTabPosition())
        fluidBottomNavigation.selectTab(0)
        controller.configurationChange()
        assertEquals(0, fluidBottomNavigation.getSelectedTabPosition())
    }

    @Test
    fun `selectTab invokes onTabSelected on OnTabSelectedListener`() {
        fluidBottomNavigation.selectTab(1)
        verify(onTabSelectedListener).onTabSelected(1)
        fluidBottomNavigation.selectTab(2)
        verify(onTabSelectedListener).onTabSelected(2)
        fluidBottomNavigation.selectTab(0)
        verify(onTabSelectedListener).onTabSelected(0)
    }

    @Test
    fun `selectTab changes selected tab position`() {
        fluidBottomNavigation.selectTab(1)
        assertEquals(1, fluidBottomNavigation.getSelectedTabPosition())
        fluidBottomNavigation.selectTab(2)
        assertEquals(2, fluidBottomNavigation.getSelectedTabPosition())
        fluidBottomNavigation.selectTab(0)
        assertEquals(0, fluidBottomNavigation.getSelectedTabPosition())
    }

    @Test
    fun `selectTab changes selected tab item`() {
        fluidBottomNavigation.selectTab(1)
        assertEquals(fluidBottomNavigationItems[1], fluidBottomNavigation.selectedTabItem)
        fluidBottomNavigation.selectTab(2)
        assertEquals(fluidBottomNavigationItems[2], fluidBottomNavigation.selectedTabItem)
        fluidBottomNavigation.selectTab(0)
        assertEquals(fluidBottomNavigationItems[0], fluidBottomNavigation.selectedTabItem)
    }

    @Test
    fun `hide hides navigation`() {
        fluidBottomNavigation.isVisible = true
        fluidBottomNavigation.hide()
        assertFalse(fluidBottomNavigation.isVisible)
    }

    @Test
    fun `show shows navigation`() {
        fluidBottomNavigation.isVisible = false
        fluidBottomNavigation.show()
        assertTrue(fluidBottomNavigation.isVisible)
    }

    @Test
    fun `getTabsSize returns correct items size`() {
        assertEquals(fluidBottomNavigationItems.size, fluidBottomNavigation.getTabsSize())
    }
}