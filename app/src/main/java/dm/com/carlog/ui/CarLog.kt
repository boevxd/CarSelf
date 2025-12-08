package dm.com.carlog.ui

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dm.com.carlog.model.DeleteType
import dm.com.carlog.model.DeleteViewModel
import dm.com.carlog.model.FuelFormViewModel
import dm.com.carlog.model.FuelViewModel
import dm.com.carlog.model.VehicleFormViewModel
import dm.com.carlog.model.VehicleViewModel
import dm.com.carlog.ui.component.DeleteBottomSheet
import dm.com.carlog.ui.component.FuelFormBottomSheet
import dm.com.carlog.ui.component.VehicleFormBottomSheet
import dm.com.carlog.ui.view.Detail
import dm.com.carlog.ui.view.Home
import dm.com.carlog.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class CarLogNav {
    HOME, VEHICLE_DETAIL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarLog() {
    val navController: NavHostController = rememberNavController()
    val context: Context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val vehicleViewModel: VehicleViewModel = hiltViewModel()
    val vehicleFormViewModel: VehicleFormViewModel = hiltViewModel()
    val fuelViewModel: FuelViewModel = hiltViewModel()
    val fuelFormViewModel: FuelFormViewModel = hiltViewModel()
    val deleteViewModel: DeleteViewModel = hiltViewModel()

    val vehicleUiState by vehicleViewModel.uiState.collectAsState()
    val vehicleFormUiState by vehicleFormViewModel.uiState.collectAsState()
    val fuelUiState by fuelViewModel.uiState.collectAsState()
    val fuelFormUiState by fuelFormViewModel.uiState.collectAsState()
    val deleteUiState by deleteViewModel.uiState.collectAsState()

    val vehicleSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val fuelSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val deleteSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = CarLogNav.HOME.name,
            modifier = Modifier.statusBarsPadding()
        ) {
            composable(route = CarLogNav.HOME.name) {
                var shouldExit by remember { mutableStateOf(false) }

                Home(
                    vehiclesWithStats = vehicleUiState.vehiclesWithStats,
                    onAddVehicleClick = {
                        vehicleFormViewModel.showSheet()
                    },
                    onEditVehicleClick = { vehicle ->
                        vehicleFormViewModel.setupEdit(vehicle)
                        vehicleFormViewModel.showSheet()
                    },
                    onDeleteVehicleClick = { vehicle ->
                        deleteViewModel.setIsOnDetails(false)
                        deleteViewModel.updateSelectedVehicle(vehicle)
                        deleteViewModel.showSheet()
                    },
                    onVehicleClick = { vehicleWithStats ->
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            fuelFormViewModel.clearEdit()
                            vehicleViewModel.updateSelectedVehicleWithStats(vehicleWithStats)
                            fuelViewModel.populateFuels(vehicleWithStats.vehicle.id)
                            navController.navigate(CarLogNav.VEHICLE_DETAIL.name)
                        }
                    },
                    shouldExit = shouldExit,
                )
            }

            composable(route = CarLogNav.VEHICLE_DETAIL.name) {
                if (vehicleUiState.selectedVehicleWithStats == null) {
                    navController.popBackStack()
                    showToast(context, "Selected vehicle not found, should not be possible", true)
                }
                var shouldExit by remember { mutableStateOf(false) }

                BackHandler {
                    shouldExit = true
                    scope.launch {
                        kotlinx.coroutines.delay(300)
                        navController.popBackStack()
                    }
                }

                Detail(
                    onBackClick = {
                        shouldExit = true
                        scope.launch {
                            kotlinx.coroutines.delay(300)
                            navController.popBackStack()
                        }
                    },
                    onEditClick = {
                        vehicleFormViewModel.setupEdit(vehicleUiState.selectedVehicleWithStats!!.vehicle)
                        vehicleFormViewModel.showSheet()
                    },
                    onDeleteClick = {
                        deleteViewModel.setIsOnDetails(true)
                        deleteViewModel.updateSelectedVehicle(vehicleUiState.selectedVehicleWithStats!!.vehicle)
                        deleteViewModel.showSheet()
                    },
                    onExportVehicleClick = {
                        vehicleFormViewModel.exportVehicleData(context, vehicleUiState.selectedVehicleWithStats!!.vehicle)
                    },
                    vehicleWithStats = vehicleUiState.selectedVehicleWithStats!!,
                    fuels = fuelUiState.fuels,
                    onAddFuelClick = {
                        fuelFormViewModel.showSheet()
                    },
                    onFuelEditClick = { fuel ->
                        fuelFormViewModel.setupEdit(fuel)
                        fuelFormViewModel.showSheet()
                    },
                    onFuelDeleteClick = { fuel ->
                        deleteViewModel.updateSelectedFuel(fuel)
                        deleteViewModel.setDeleteType(DeleteType.FUEL)
                        deleteViewModel.showSheet()
                    },
                    shouldExit = shouldExit
                )
            }
        }

        FuelFormBottomSheet(
            onDismissRequest = {
                fuelFormViewModel.hideSheet()
                if (fuelFormUiState.isEdit) {
                    fuelFormViewModel.clearEdit()
                }
            },
            onCloseButtonClick = {
                scope.launch {
                    fuelSheetState.hide()
                    fuelFormViewModel.hideSheet()
                    if (fuelFormUiState.isEdit) {
                        fuelFormViewModel.clearEdit()
                    }
                }
            },
            onSaveButtonClick = {
                if (fuelFormUiState.isEdit) {
                    fuelFormViewModel.updateFuel(
                        context = context,
                        vehicle = vehicleUiState.selectedVehicleWithStats!!.vehicle,
                        fuel = fuelFormUiState.selectedFuel!!,
                        previousOdometer = fuelUiState.fuels.filter { fuel ->
                            fuel.date < fuelFormUiState.selectedFuel!!.date || (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                        }.maxWithOrNull(compareBy({ it.date }, { it.createdAt }))?.odometer,
                        onSuccess = {
                            scope.launch {
                                fuelViewModel.populateFuels(vehicleUiState.selectedVehicleWithStats!!.vehicle.id)
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelSheetState.hide()
                                fuelFormViewModel.clearEdit()
                                fuelFormViewModel.hideSheet()
                            }
                        })
                } else {
                    fuelFormViewModel.addFuel(
                        context = context,
                        vehicle = vehicleUiState.selectedVehicleWithStats!!.vehicle,
                        previousOdometer = fuelUiState.fuels.maxWithOrNull(
                            compareBy(
                                { it.date },
                                { it.createdAt })
                        )?.odometer,
                        onSuccess = {
                            scope.launch {
                                fuelViewModel.populateFuels(vehicleUiState.selectedVehicleWithStats!!.vehicle.id)
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelSheetState.hide()
                                fuelFormViewModel.resetForm()
                                fuelFormViewModel.hideSheet()
                            }
                        })
                }
            },
            sheetState = fuelSheetState,
            isProcessing = fuelFormUiState.isProcessing,
            isEdit = fuelFormUiState.isEdit,
            showSheet = fuelFormUiState.showSheet,
            dateValue = fuelFormViewModel.date,
            onDateValueChange = { fuelFormViewModel.updateDate(it) },
            dateError = fuelFormUiState.errorState.dateError,
            odometerValue = fuelFormViewModel.odometer,
            onOdometerValueChange = { fuelFormViewModel.updateOdometer(it) },
            odometerError = fuelFormUiState.errorState.odometerError,
            tripValue = fuelFormViewModel.trip,
            onTripValueChange = { fuelFormViewModel.updateTrip(it) },
            tripError = fuelFormUiState.errorState.tripError,
            fuelAddedValue = fuelFormViewModel.fuelAdded,
            onFuelAddedValueChange = { fuelFormViewModel.updateFuelAdded(it) },
            fuelAddedError = fuelFormUiState.errorState.fuelAddedError,
            pricePerGallonValue = fuelFormViewModel.pricePerGallon,
            onPricePerGallonValueChange = { fuelFormViewModel.updatePricePerGallon(it) },
            pricePerGallonError = fuelFormUiState.errorState.pricePerGallonError,
            canCalculateTrip = fuelFormViewModel.odometer.isNotEmpty() && fuelUiState.fuels.isNotEmpty() &&
                    (if (fuelFormUiState.isEdit)
                        fuelUiState.fuels.any { fuel ->
                            fuel.date < fuelFormUiState.selectedFuel!!.date ||
                                    (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                        }
                    else true),
            onCanCalculateTripClick = {
                val previousOdometer = if (fuelFormUiState.isEdit) {
                    fuelUiState.fuels.filter { fuel ->
                        fuel.date < fuelFormUiState.selectedFuel!!.date || (fuel.date == fuelFormUiState.selectedFuel!!.date && fuel.createdAt < fuelFormUiState.selectedFuel!!.createdAt)
                    }.maxWithOrNull(compareBy({ it.date }, { it.createdAt }))?.odometer
                } else {
                    fuelUiState.fuels.maxWithOrNull(
                        compareBy(
                            { it.date },
                            { it.createdAt })
                    )?.odometer
                }
                fuelFormViewModel.calculateTripFromPreviousOdometer(previousOdometer)
            })


        VehicleFormBottomSheet(
            onDismissRequest = {
                vehicleFormViewModel.hideSheet()
                if (vehicleFormUiState.isEdit) vehicleFormViewModel.clearEdit()
            },
            onCloseButtonClick = {
                scope.launch {
                    vehicleSheetState.hide()
                    vehicleFormViewModel.hideSheet()
                    if (vehicleFormUiState.isEdit) vehicleFormViewModel.clearEdit()
                }
            },
            onSaveButtonClick = {
                if (vehicleFormUiState.isEdit) {
                    vehicleFormViewModel.updateVehicle(
                        context = context,
                        vehicle = vehicleFormUiState.selectedVehicle!!,
                        onSuccess = {
                            scope.launch {
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                vehicleSheetState.hide()
                                vehicleFormViewModel.clearEdit()
                                vehicleFormViewModel.hideSheet()
                            }
                        })
                } else {
                    vehicleFormViewModel.addVehicle(context = context, onSuccess = {
                        scope.launch {
                            vehicleSheetState.hide()
                            vehicleFormViewModel.resetForm()
                            vehicleFormViewModel.hideSheet()
                        }
                    })
                }
            },
            sheetState = vehicleSheetState,
            isProcessing = vehicleFormUiState.isProcessing,
            isEdit = vehicleFormUiState.isEdit,
            showSheet = vehicleFormUiState.showSheet,
            nameValue = vehicleFormViewModel.name,
            onNameValueChange = { vehicleFormViewModel.updateName(it) },
            nameError = vehicleFormUiState.errorState.nameError,
            manufacturerValue = vehicleFormViewModel.manufacturer,
            onManufacturerValueChange = { vehicleFormViewModel.updateManufacturer(it) },
            manufacturerError = vehicleFormUiState.errorState.manufacturerError,
            modelValue = vehicleFormViewModel.model,
            onModelValueChange = { vehicleFormViewModel.updateModel(it) },
            modelError = vehicleFormUiState.errorState.modelError,
            yearValue = vehicleFormViewModel.year,
            onYearValueChange = { vehicleFormViewModel.updateYear(it) },
            yearError = vehicleFormUiState.errorState.yearError,
            licensePlateValue = vehicleFormViewModel.license_plate,
            onLicensePlateValueChange = { vehicleFormViewModel.updateLicensePlate(it) },
            vinValue = vehicleFormViewModel.vin,
            onVinValueChange = { vehicleFormViewModel.updateVin(it) }
        )

        DeleteBottomSheet(
            onDismissRequest = {
                deleteViewModel.hideSheet()
            },
            onCloseButtonClick = {
                scope.launch {
                    deleteSheetState.hide()
                    deleteViewModel.hideSheet()
                }
            },
            onDeleteClick = {
                if (deleteUiState.deleteType == DeleteType.VEHICLE) {
                    deleteViewModel.deleteVehicle(
                        context = context,
                        vehicle = deleteUiState.selectedVehicle!!,
                        onSuccess = {
                            scope.launch {
                                if (deleteViewModel.isOnDetails) {
                                    deleteViewModel.setIsOnDetails(false)
                                    navController.popBackStack()
                                }

                                deleteSheetState.hide()
                                deleteViewModel.clear()
                                deleteViewModel.hideSheet()
                            }
                        })
                } else {
                    deleteViewModel.deleteFuel(
                        context = context,
                        fuel = deleteUiState.selectedFuel!!,
                        onSuccess = {
                            scope.launch {
                                vehicleViewModel.updateSelectedVehicleAfterEdit()
                                fuelViewModel.removeFromList(deleteUiState.selectedFuel!!)
                                deleteSheetState.hide()
                                deleteViewModel.clear()
                                deleteViewModel.hideSheet()
                            }
                        })
                }
            },
            sheetState = deleteSheetState,
            isProcessing = deleteUiState.isProcessing,
            showSheet = deleteUiState.showSheet,
            name = deleteUiState.name
        )
    }
}