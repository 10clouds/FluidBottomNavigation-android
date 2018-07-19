# Fluid Bottom Navigation  [![Build Status](https://app.bitrise.io/app/339f26db491c854d/status.svg?token=DM799a3_NFuYxusOX-zoKA&branch=master)](https://app.bitrise.io/app/339f26db491c854d)  [![Download library](https://api.bintray.com/packages/10clouds-android/fluidbottomnavigation/fluid-bottom-navigation/images/download.svg)](https://bintray.com/10clouds-android/fluidbottomnavigation/fluid-bottom-navigation)



## Sample
<p align="center">
  <img src="static/sample.gif" alt="Sample Fluid Bottom Navigation"/>
</p>


## Installation
Just use the Maven repository. Library is on jCenter and mavenCentral.

**Gradle:**
```groovy
implementation 'com.tenclouds.fluidbottomnavigation:fluid-bottom-navigation:{last_release_version}'
```
**SBT:**
```groovy
libraryDependencies += "com.tenclouds.fluidbottomnavigation" % "fluid-bottom-navigation" % "{last_release_version}"
```
**Maven:**
```groovy
<dependency>
  <groupId>com.tenclouds.fluidbottomnavigation</groupId>
  <artifactId>fluid-bottom-navigation</artifactId>
  <version>{last_release_version}</version>
  <type>aar</type>
</dependency>
```

## Usage
Place **FluidBottomNavigation** in your layout:
```xml
<com.tenclouds.fluidbottomnavigation.FluidBottomNavigation
            android:id="@+id/fluidBottomNavigation"
            android:layout_height="wrap_content"
            android:layout_width="0dp" />
```
then set navigation items to component:
```kotlin
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
```
**Application with example is in [app folder](https://github.com/10clouds/FluidBottomNavigation-android/tree/master/app)**

## Customization
You can customize component from XML layout file, using attributes: 
```
app:accentColor="@color/accentColor"
app:backColor="@color/backColor"
app:iconColor="@color/iconColor"
app:iconSelectedColor="@color/iconSelectedColor"
app:textColor="@color/textColor"
```
or from Java/Kotlin code:
```kotlin 
fluidBottomNavigation.accentColor = ContextCompat.getColor(this, R.color.accentColor)
fluidBottomNavigation.backColor = ContextCompat.getColor(this, R.color.backColor)
fluidBottomNavigation.textColor = ContextCompat.getColor(this, R.color.textColor)
fluidBottomNavigation.iconColor = ContextCompat.getColor(this, R.color.iconColor)
fluidBottomNavigation.iconSelectedColor = ContextCompat.getColor(this, R.color.iconSelectedColor)
```