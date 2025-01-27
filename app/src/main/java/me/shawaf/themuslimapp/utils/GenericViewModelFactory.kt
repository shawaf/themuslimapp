package me.shawaf.themuslimapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.InvocationTargetException

class GenericViewModelFactory<VM : ViewModel>(
    private val viewModelClass: Class<VM>,
    private val creator: () -> VM
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            try {
                @Suppress("UNCHECKED_CAST")
                return creator() as T
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Failed to create instance of ViewModel", e)
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}