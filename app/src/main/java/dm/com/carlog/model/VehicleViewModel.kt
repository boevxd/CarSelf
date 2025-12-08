package dm.com.carlog.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dm.com.carlog.data.vehicle.VehicleRepository
import dm.com.carlog.data.vehicle.VehicleWithStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehicleState(
    val vehiclesWithStats: List<VehicleWithStats> = emptyList(),
    val selectedVehicleWithStats: VehicleWithStats? = null,
)

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehicleState())
    val uiState: StateFlow<VehicleState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                vehicleRepository.getAllWithStats().collect { list ->
                    _uiState.update { it.copy(vehiclesWithStats = list) }
                }
            } catch (e: Exception) {
                Log.e("VehicleViewModel", "Error fetching vehicles", e)
            }
        }
    }

    fun updateSelectedVehicleWithStats(vehicleWithStats: VehicleWithStats?) {
        _uiState.update { it.copy(selectedVehicleWithStats = vehicleWithStats) }
    }

    suspend fun updateSelectedVehicleAfterEdit() {
        val current = _uiState.value.selectedVehicleWithStats ?: return
        val updatedVehicleWithStats = vehicleRepository.getByIdWithStats(current.vehicle.id)
        _uiState.update { it.copy(selectedVehicleWithStats = updatedVehicleWithStats) }
    }
}