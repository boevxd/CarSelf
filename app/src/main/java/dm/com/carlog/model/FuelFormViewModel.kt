package dm.com.carlog.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.data.fuel.FuelRepository
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.util.showToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class FuelFormErrorState(
    var dateError: String? = null,
    var odometerError: String? = null,
    var tripError: String? = null,
    var fuelAddedError: String? = null,
    var pricePerGallonError: String? = null,
)

data class FuelFormState(
    val errorState: FuelFormErrorState = FuelFormErrorState(),
    val isProcessing: Boolean = false,
    val showSheet: Boolean = false,
    val isEdit: Boolean = false,
    val selectedFuel: Fuel? = null,
)

@HiltViewModel
class FuelFormViewModel @Inject constructor(
    val fuelRepository: FuelRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(FuelFormState())
    val uiState: StateFlow<FuelFormState> = _uiState.asStateFlow()

    var date by mutableLongStateOf(System.currentTimeMillis())
        private set

    var odometer by mutableStateOf("")
        private set

    var trip by mutableStateOf("")
        private set

    var fuelAdded by mutableStateOf("")
        private set

    var pricePerGallon by mutableStateOf("")
        private set

    fun updateDate(newDate: Long) {
        date = newDate
    }

    fun updateOdometer(newOdometer: String) {
        odometer = newOdometer
    }

    fun updateTrip(newTrip: String) {
        trip = newTrip
    }

    fun updateFuelAdded(newFuelAdded: String) {
        fuelAdded = newFuelAdded
    }

    fun updatePricePerGallon(newPricePerGallon: String) {
        pricePerGallon = newPricePerGallon
    }

    fun setupEdit(
        fuel: Fuel
    ) {
        date = fuel.date
        odometer = fuel.odometer.toString()
        trip = fuel.trip.toString()
        fuelAdded = fuel.fuelAdded.toString()
        pricePerGallon = fuel.pricePerGallon.toString()
        _uiState.update {
            it.copy(
                errorState = FuelFormErrorState(),
                isEdit = true,
                selectedFuel = fuel,
            )
        }
    }

    fun clearEdit() {
        _uiState.update {
            it.copy(
                errorState = FuelFormErrorState(),
                isEdit = false,
                selectedFuel = null,
            )
        }
        resetForm()
    }

    fun resetForm() {
        date = System.currentTimeMillis()
        odometer = ""
        trip = ""
        fuelAdded = ""
        pricePerGallon = ""
        _uiState.value = FuelFormState()
    }

    fun setProcessing(isProcessing: Boolean) {
        _uiState.update { it.copy(isProcessing = isProcessing) }
    }

    fun showSheet() {
        _uiState.update { it.copy(showSheet = true) }
    }

    fun hideSheet() {
        _uiState.update { it.copy(showSheet = false) }
    }

    fun validateForm(
        vehicle: Vehicle,
        previousOdometer: Int?
    ): Boolean {
        var isValid = true
        val errorState = FuelFormErrorState()

        if (odometer.isBlank()) {
            errorState.odometerError = "Odometer is required"
            isValid = false
        } else {
            val odometerValue = odometer.toIntOrNull()
            if (odometerValue == null || odometerValue < 0) {
                errorState.odometerError = "Odometer must be a positive number"
                isValid = false
            }

            if (previousOdometer != null && odometerValue != null && odometerValue < previousOdometer) {
                errorState.odometerError = "Odometer cannot be less than previous reading ($previousOdometer mi)"
                isValid = false
            }
        }

        if (trip.isBlank()) {
            errorState.tripError = "Trip is required"
            isValid = false
        } else {
            val tripValue = trip.toIntOrNull()
            if (tripValue == null || tripValue < 0) {
                errorState.tripError = "Trip must be a positive number"
                isValid = false
            }
        }

        if (fuelAdded.isBlank()) {
            errorState.fuelAddedError = "Fuel added is required"
            isValid = false
        } else {
            val fuelAddedValue = fuelAdded.toDoubleOrNull()
            if (fuelAddedValue == null || fuelAddedValue <= 0) {
                errorState.fuelAddedError = "Fuel added must be a positive number"
                isValid = false
            }
        }

        if (pricePerGallon.isBlank()) {
            errorState.pricePerGallonError = "Price per gallon is required"
            isValid = false
        } else {
            val pricePerGallon = this@FuelFormViewModel.pricePerGallon.toDoubleOrNull()
            if (pricePerGallon == null || pricePerGallon <= 0) {
                errorState.pricePerGallonError = "Price per gallon must be a positive number"
                isValid = false
            }
        }

        _uiState.update { it.copy(errorState = errorState) }
        return isValid
    }

    fun addFuel(
        context: Context,
        vehicle: Vehicle,
        previousOdometer: Int?,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm(vehicle, previousOdometer)) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val newFuel = Fuel(
                    id = UUID.randomUUID().toString(),
                    date = date,
                    odometer = odometer.toInt(),
                    trip = trip.toInt(),
                    fuelAdded = fuelAdded.toDouble(),
                    pricePerGallon = pricePerGallon.toDouble(),
                    vehicleId = vehicle.id,
                    // This will be calculated in repository
                    totalCost = 0.0,
                    fuelEconomy = 0.0,
                    costPerMile = 0.0,
                )
                fuelRepository.insert(newFuel)

                showToast(context, "Refuel recorded successfully")
                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error recording fuel: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun updateFuel(
        context: Context,
        vehicle: Vehicle,
        previousOdometer: Int?,
        fuel: Fuel,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm(vehicle, previousOdometer)) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val updatedFuel = fuel.copy(
                    date = date,
                    odometer = odometer.toInt(),
                    trip = trip.toInt(),
                    fuelAdded = fuelAdded.toDouble(),
                    pricePerGallon = pricePerGallon.toDouble()
                )
                fuelRepository.update(updatedFuel)

                showToast(context, "Refuel edited successfully")
                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error updating refuel: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun calculateTripFromPreviousOdometer(previousOdometer: Int?) {
        if (previousOdometer == null) return
        val odometerValue = odometer.toIntOrNull()

        if (odometerValue == null || odometerValue < 0) {
            _uiState.update {
                it.copy(
                    errorState = it.errorState.copy(
                        odometerError = "Odometer must be a positive number"
                    )
                )
            }
            return
        }

        if (odometerValue < previousOdometer) {
            _uiState.update {
                it.copy(
                    errorState = it.errorState.copy(
                        odometerError = "Odometer cannot be less than previous reading ($previousOdometer mi)"
                    )
                )
            }
            return
        }

        trip = (odometerValue - previousOdometer).toString()

        _uiState.update {
            it.copy(
                errorState = it.errorState.copy(
                    odometerError = null
                )
            )
        }

    }
}