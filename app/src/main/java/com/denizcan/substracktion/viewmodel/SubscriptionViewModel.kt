package com.denizcan.substracktion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denizcan.substracktion.model.*
import com.denizcan.substracktion.repository.SubscriptionRepository
import com.denizcan.substracktion.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val subscriptionRepository: SubscriptionRepository = SubscriptionRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {
    // UI State'leri
    private val _userCountry = MutableStateFlow<String>("TR") // Varsayılan
    val userCountry = _userCountry.asStateFlow()

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
        loadUserCountry()
        loadServices()
        loadUserSubscriptions()
    }

    private fun loadUserCountry() {
        viewModelScope.launch {
            userRepository.getUserProfile()
                .collect { user ->
                    // Firestore'dan gelen ülke kodunu büyük harfe çevir (standartlaştırma)
                    _userCountry.value = user?.country?.uppercase() ?: "TR"
                }
        }
    }

    private fun loadServices() {
        viewModelScope.launch {
            subscriptionRepository.getServices()
                .catch { e -> _error.value = e.message }
                .collect { services ->
                    _services.value = services
                }
        }
    }

    private fun loadUserSubscriptions() {
        viewModelScope.launch {
            subscriptionRepository.getUserSubscriptions()
                .catch { e -> _error.value = e.message }
                .collect { subscriptions ->
                    _userSubscriptions.value = subscriptions
                }
        }
    }

    fun loadServicePlans(serviceId: String) {
        viewModelScope.launch {
            subscriptionRepository.getServicePlans(serviceId)
                .catch { e -> _error.value = e.message }
                .collect { plans ->
                    // Sadece kullanıcının ülkesinde geçerli olan planları filtrele
                    val countryCode = userCountry.value
                    val availablePlans = plans.filter { plan ->
                        plan.prices.containsKey(countryCode)
                    }
                    _selectedServicePlans.value = availablePlans
                }
        }
    }

    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                subscriptionRepository.addSubscription(subscription)
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
                subscriptionRepository.updateSubscription(subscription)
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
                subscriptionRepository.deleteSubscription(subscriptionId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Seçili ülke için plan fiyatını al
    fun getPriceForCountry(plan: ServicePlan): Price? {
        return plan.prices[userCountry.value]
    }
} 