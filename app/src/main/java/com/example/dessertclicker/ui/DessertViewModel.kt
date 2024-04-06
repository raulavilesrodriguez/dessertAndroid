package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    // Dessert UI state
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    private val currentDessertIndex by mutableIntStateOf(0)

    private var currentDessertPrice by mutableIntStateOf(Datasource.dessertList[currentDessertIndex].price)
    var currentDessertImageId by mutableIntStateOf(Datasource.dessertList[currentDessertIndex].imageId)


    fun updateValues(){
        _uiState.update{currentState ->
            currentState.copy(
                revenue = currentDessertPrice + _uiState.value.revenue,
                dessertsSold = 1 + _uiState.value.dessertsSold
            )
        }
        // Show the next dessert
        val dessertToShow = determineDessertToShow(
            Datasource.dessertList,
            _uiState.value.dessertsSold
        )
        currentDessertImageId = dessertToShow.imageId
        currentDessertPrice = dessertToShow.price
    }

    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

}