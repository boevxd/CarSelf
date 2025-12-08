package dm.com.carlog.model

import android.content.Context
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleRepository
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.data.fuel.FuelRepository
import dm.com.carlog.util.showToast
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.content.ContentValues
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class VehicleFormErrorState(
    var nameError: String? = null,
    var manufacturerError: String? = null,
    var modelError: String? = null,
    var yearError: String? = null
)

data class VehicleFormState(
    val errorState: VehicleFormErrorState = VehicleFormErrorState(),
    val showSheet: Boolean = false,
    val isProcessing: Boolean = false,
    val isEdit: Boolean = false,
    val selectedVehicle: Vehicle? = null,
)

@HiltViewModel
class VehicleFormViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VehicleFormState())
    val uiState: StateFlow<VehicleFormState> = _uiState.asStateFlow()

    var name by mutableStateOf("")
        private set
    var manufacturer by mutableStateOf("")
        private set
    var model by mutableStateOf("")
        private set
    var year by mutableStateOf("")
        private set
    var license_plate by mutableStateOf("")
        private set

    var vin by mutableStateOf("")
        private set

    fun updateName(newName: String) {
        name = newName
    }

    fun updateManufacturer(newManufacturer: String) {
        manufacturer = newManufacturer
    }

    fun updateModel(newModel: String) {
        model = newModel
    }

    fun updateYear(newYear: String) {
        year = newYear
    }

    fun updateLicensePlate(newLicensePlate: String) {
        license_plate = newLicensePlate
    }

    fun updateVin(newVin: String) {
        vin = newVin
    }

    fun setupEdit(
        vehicle: Vehicle,
    ) {
        name = vehicle.name
        manufacturer = vehicle.manufacturer
        model = vehicle.model
        year = vehicle.year.toString()
        license_plate = vehicle.licensePlate
        vin = vehicle.vin
        _uiState.update {
            it.copy(
                errorState = VehicleFormErrorState(),
                isEdit = true,
                selectedVehicle = vehicle,
            )
        }
    }

    fun clearEdit() {
        _uiState.update {
            it.copy(
                errorState = VehicleFormErrorState(),
                isEdit = false,
                selectedVehicle = null,
            )
        }
        resetForm()
    }

    fun resetForm() {
        name = ""
        manufacturer = ""
        model = ""
        year = ""
        license_plate = ""
        vin = ""
        _uiState.value = VehicleFormState()
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

    fun validateForm(): Boolean {
        var isValid = true
        val errorState = VehicleFormErrorState()

        if (name.isBlank()) {
            errorState.nameError = "Name cannot be empty"
            isValid = false
        }

        if (manufacturer.isBlank()) {
            errorState.manufacturerError = "Manufacturer cannot be empty"
            isValid = false
        }

        if (model.isBlank()) {
            errorState.modelError = "Model cannot be empty"
            isValid = false
        }

        if (year.isBlank()) {
            errorState.yearError = "Year cannot be empty"
            isValid = false
        } else {
            val maxYear = LocalDate.now().year + 5 // Allow up to 5 years in the future
            val yearInt = year.toIntOrNull()
            if (yearInt == null || yearInt < 1886 || yearInt > maxYear) { // First car invented in 1886
                errorState.yearError = "Year must be a valid number between 1886 and $maxYear"
                isValid = false
            }
        }

        _uiState.update { it.copy(errorState = errorState) }
        return isValid
    }

    fun addVehicle(
        context: Context,
        onSuccess: () -> Unit = { },
    ) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val newVehicle = Vehicle(
                    id = UUID.randomUUID().toString(),
                    name = name.trim(),
                    manufacturer = manufacturer.trim(),
                    model = model.trim(),
                    year = year.toInt(),
                    licensePlate = license_plate.trim(),
                    vin = vin.trim()
                )
                vehicleRepository.insert(newVehicle)

                showToast(context, "Vehicle added successfully")
                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error adding vehicle: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun updateVehicle(
        context: Context,
        vehicle: Vehicle,
        onSuccess: () -> Unit = {  },
    ) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                setProcessing(true)

                val updatedVehicle = vehicle.copy(
                    name = name.trim(),
                    manufacturer = manufacturer.trim(),
                    model = model.trim(),
                    year = year.toInt(),
                    licensePlate = license_plate.trim(),
                    vin = vin.trim()
                )
                vehicleRepository.update(updatedVehicle)

                showToast(context, "Vehicle edited successfully")
                onSuccess()
            } catch (e: Exception) {
                showToast(context, "Error editing vehicle: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    fun exportVehicleData(
        context: Context,
        vehicle: Vehicle,
    ) {
        viewModelScope.launch {
            try {
                setProcessing(true)

                val fuels = fuelRepository.getByVehicleId(vehicle.id).firstOrNull() ?: emptyList<Fuel>()

                // Build CSV content
                val csvBuilder = StringBuilder()
                csvBuilder.append("Vehicle Details\n")
                csvBuilder.append("Name,Manufacturer,Model,Year,License Plate,VIN\n")
                csvBuilder.append("${vehicle.name},${vehicle.manufacturer},${vehicle.model},${vehicle.year},${vehicle.licensePlate},${vehicle.vin}\n\n")

                csvBuilder.append("Fuel Records\n")
                csvBuilder.append("Date,Odometer,Trip,Fuel Added,Price per Gallon,Total Cost,Fuel Economy,Cost per Mile\n")
                fuels.forEach { fuel ->
                    csvBuilder.append("${formatDate(fuel.date)},${fuel.odometer},${fuel.trip},${fuel.fuelAdded},${fuel.pricePerGallon},${fuel.totalCost},${fuel.fuelEconomy},${fuel.costPerMile}\n")
                }

                val fileName = "${vehicle.name}_export"
                val csvData = csvBuilder.toString().toByteArray()

                // Save to Downloads directory
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "text/plain")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }

                // Create new file entry in the Downloads collection
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri != null) {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(csvData)
                    }

                    val values = ContentValues().apply {
                        put(MediaStore.Downloads.IS_PENDING, 0)
                    }
                    resolver.update(uri, values, null, null)
                }

                showToast(context, "Exported CSV!")
            } catch (e: Exception) {
                showToast(context, "Error exporting vehicle: ${e.message}", true)
            } finally {
                setProcessing(false)
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

}