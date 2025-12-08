package dm.com.carlog.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.data.fuel.FuelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FuelState(
    val fuels: List<Fuel> = emptyList()
)

@HiltViewModel
class FuelViewModel @Inject constructor(
    private val fuelRepository: FuelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FuelState())
    val uiState: StateFlow<FuelState> = _uiState.asStateFlow()

    fun populateFuels(vehicleId: String) {
        viewModelScope.launch {
            try {
                fuelRepository.getByVehicleId(vehicleId).collect { fuels ->
                    _uiState.update { it.copy(fuels = fuels) }
                }
            } catch (e: Exception) {
                Log.e("FuelViewModel", "Error fetching fuels", e)
            }
        }
    }

    fun removeFromList(fuel: Fuel) {
        _uiState.update { currentState ->
            currentState.copy(fuels = currentState.fuels.filter { it.id != fuel.id })
        }
    }
}