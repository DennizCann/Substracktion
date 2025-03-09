package com.denizcan.substracktion.util

import android.content.Context
import android.content.SharedPreferences

class RegionManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "region_prefs",
        Context.MODE_PRIVATE
    )

    fun getUserRegion(): String {
        // Önce kaydedilmiş bölgeyi kontrol et
        val savedRegion = prefs.getString("user_region", null)
        if (savedRegion != null) return savedRegion

        // Yoksa cihaz ayarlarından tespit et
        val locale = context.resources.configuration.locales[0]
        val country = locale.country
        
        // Bölgeyi kaydet
        setUserRegion(country)
        
        return country
    }

    fun setUserRegion(region: String) {
        prefs.edit().putString("user_region", region).apply()
    }
} 