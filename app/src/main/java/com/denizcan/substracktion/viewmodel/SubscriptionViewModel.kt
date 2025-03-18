package com.denizcan.substracktion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.model.*
import com.denizcan.substracktion.repository.SubscriptionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val repository: SubscriptionRepository = SubscriptionRepository()
) : ViewModel() {
    // UI State'leri
    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services = _services.asStateFlow()

    private val _selectedServicePlans = MutableStateFlow<List<ServicePlan>>(emptyList())
    val selectedServicePlans = _selectedServicePlans.asStateFlow()

    private val _userSubscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val userSubscriptions = _userSubscriptions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        // Başlangıçta servisleri ve kullanıcının üyeliklerini yükle
        loadServices()
        loadUserSubscriptions()
    }

    private fun loadServices() {
        viewModelScope.launch {
            repository.getServices()
                .catch { e -> _error.value = e.message }
                .collect { services ->
                    _services.value = services
                }
        }
    }

    private fun loadUserSubscriptions() {
        viewModelScope.launch {
            repository.getUserSubscriptions()
                .catch { e -> _error.value = e.message }
                .collect { subscriptions ->
                    _userSubscriptions.value = subscriptions
                }
        }
    }

    fun loadServicePlans(serviceId: String) {
        viewModelScope.launch {
            repository.getServicePlans(serviceId)
                .catch { e -> _error.value = e.message }
                .collect { plans ->
                    _selectedServicePlans.value = plans
                }
        }
    }

    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.addSubscription(subscription)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSubscription(subscription: Subscription) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.updateSubscription(subscription)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSubscription(subscriptionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteSubscription(subscriptionId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 