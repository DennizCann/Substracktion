package com.denizcan.substracktion.util

object EmailValidator {
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    
    fun isValid(email: String): Boolean {
        return email.matches(EMAIL_REGEX.toRegex()) && 
               !email.contains("..") &&  // Ardışık nokta olmamalı
               email.length <= 254 &&    // RFC 5321'e göre maksimum uzunluk
               !email.startsWith(".") && // Nokta ile başlamamalı
               !email.endsWith(".")     // Nokta ile bitmemeli
    }
} 