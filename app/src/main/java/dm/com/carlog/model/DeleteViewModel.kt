package dm.com.carlog.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.data.fuel.FuelRepository
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleRepository
import dm.com.carlog.util.convertMillisToDate
import dm.com.carlog.util.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class DeleteType {
    VEHICLE,
    FUEL
}

data class DeleteState(
    val showSheet: Boolean = false,
    val isProcessing: Boolean = false,
    val name: String = "",
    val selectedVehicle: Vehicle? = null,
    val selectedFuel: Fuel? = null,
    val deleteType: DeleteType = DeleteType.VEHICLE,
)

@HiltViewModel
class DeleteViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DeleteState())
    val uiState: StateFlow<DeleteState> = _uiState.asStateFlow()

    var isOnDetails by mutableStateOf(false)
        private set

    fun setIsOnDetails(value: Boolean) {
        isOnDetails = value
    }

    fun setDeleteType(value: DeleteType) {
        _uiState.update { it.copy(deleteType = value) }
    }

    fun showSheet() {
        _uiState.update { it.copy(showSheet = true) }
    }

    fun hideSheet() {
        _uiState.update { it.copy(showSheet = false) }
    }

    fun setProcessing(isProcessing: Boolean) {
        _uiState.update { it.copy(isProcessing = isProcessing) }
    }

    fun updateSelectedVehicle(vehicle: Vehicle) {
        _uiState.update {
            it.copy(
                name = vehicle.name,
                selectedVehicle = vehicle
            )
        }
    }

    fun updateSelectedFuel(fuel: Fuel) {
        _uiState.update {
            it.copy(
                name = "${convertMillisToDate(fuel.date)} - ${fuel.fuelAdded} L",
                selectedFuel = fuel
            )
        }
    }

    fun clear() {
        _uiState.update { DeleteState() }
    }

    fun deleteVehicle(
        context: Context,
        vehicle: Vehicle,
        onSuccess: () -> Unit = { },
    ) {
        viewModelScope.launch {
            try {
                setProcessing(true)

                vehicleRepository.delete(vehicle)

                showToast(context, "Vehicle deleted successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error deleting vehicle: ${e.message}")
            } finally {
                setProcessing(false)
            }
        }
    }

    fun deleteFuel(
        context: Context,
        fuel: Fuel,
        onSuccess: () -> Unit = { },
    ) {
        viewModelScope.launch {
            try {
                setProcessing(true)

                fuelRepository.delete(fuel)

                showToast(context, "Refuel deleted successfully")

                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error deleting refuel: ${e.message}")
            } finally {
                setProcessing(false)
            }
        }
    }
}