package com.denizcan.substracktion.util

import android.content.Context
import android.content.SharedPreferences

class LanguageManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "language_prefs",
        Context.MODE_PRIVATE
    )

    fun saveLanguage(language: Language) {
        prefs.edit().putString("selected_language", language.name).apply()
    }

    fun getLanguage(): Language {
        val languageName = prefs.getString("selected_language", Language.ENGLISH.name)
        return try {
            Language.valueOf(languageName ?: Language.ENGLISH.name)
        } catch (e: Exception) {
            Language.ENGLISH
        }
    }
} 