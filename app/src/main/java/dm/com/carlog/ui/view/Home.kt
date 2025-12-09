package dm.com.carlog.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import dm.com.carlog.R
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleWithStats
import dm.com.carlog.ui.component.SectionDivider
import dm.com.carlog.ui.component.VehicleItem
import dm.com.carlog.ui.theme.CarLogTheme

@Composable
fun Home(
    modifier: Modifier = Modifier,
    vehiclesWithStats: List<VehicleWithStats> = emptyList(),
    onAddVehicleClick: () -> Unit = { },
    onEditVehicleClick: (Vehicle) -> Unit = { _ -> },
    onDeleteVehicleClick: (Vehicle) -> Unit = { _ -> },
    onExportVehicleClick: (Vehicle) -> Unit = { _ -> },
    onVehicleClick: (VehicleWithStats) -> Unit = { _ -> },
    name: String? = null,
    shouldExit: Boolean = false
) {
    val view = LocalView.current

    SideEffect {
        val activity = view.context as? Activity
        activity?.let {
            val windowInsetsController = WindowInsetsControllerCompat(
                it.window,
                view
            )
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    var isHeaderVisible by remember { mutableStateOf(false) }
    var areItemsVisible by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()

    LaunchedEffect(shouldExit) {
        if (shouldExit) {
            areItemsVisible = false
            kotlinx.coroutines.delay(300)
            isHeaderVisible = false
        } else {
            isHeaderVisible = true
            kotlinx.coroutines.delay(300)
            areItemsVisible = true
        }
    }

    val fabScale by animateFloatAsState(
        targetValue = if (areItemsVisible && !shouldExit) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fab_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            AnimatedVisibility(
                visible = isHeaderVisible && !shouldExit,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 600)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {

                    SectionDivider(
                        title = R.string.your_vehicles
                    )
                }
            }

            AnimatedVisibility(
                visible = areItemsVisible && !shouldExit,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 600)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeOut(
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    state = lazyListState
                ) {
                    items(
                        items = vehiclesWithStats,
                        key = { it.vehicle.id }
                    ) { vehicleWithStats ->
                        VehicleItem(
                            vehicleWithStats = vehicleWithStats,
                            onClick = { onVehicleClick(vehicleWithStats) },
                            onEditClick = { onEditVehicleClick(vehicleWithStats.vehicle) },
                            onDeleteClick = { onDeleteVehicleClick(vehicleWithStats.vehicle) },
                            onExportClick = { onExportVehicleClick(vehicleWithStats.vehicle) },
                            modifier = Modifier.animateItem()
                        )
                    }

                    if (vehiclesWithStats.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.nothing_to_see_here),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(bottom = dimensionResource(R.dimen.bottom_spacer))
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddVehicleClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_small))
                .scale(fabScale),
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.fab_icon)
                )
            },
            text = { Text(text = stringResource(R.string.add_vehicle)) },
            shape = MaterialTheme.shapes.medium
        )
    }
}

@Preview
@Composable
internal fun PreviewHome() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Home(
                vehiclesWithStats = listOf(
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = "1",
                            name = "My Car",
                            manufacturer = "Toyota",
                            model = "Avanza",
                            year = 2010,
                            licensePlate = "020-111",
                            vin = "862346ajdaaaaa"
                        ),
                        latestOdometer = 150000,
                        averageFuelEconomy = 14.5,
                        totalFuelAdded = 400.0,
                        totalSpent = 12000000.0,
                        refuelCount = 40,
                        refuelPerMonth = 3.0,
                        avgGallonRefueled = 7.0,
                        avgSpentPerRefuel = 85000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = "2",
                            name = "My Motorcycle",
                            manufacturer = "Honda",
                            model = "Vario 150",
                            year = 2019,
                            licensePlate = "020-222",
                            vin = "862346j76876aa"
                        ),
                        latestOdometer = 25000,
                        averageFuelEconomy = 45.0,
                        totalFuelAdded = 150.0,
                        totalSpent = 3000000.0,
                        refuelCount = 25,
                        refuelPerMonth = 2.5,
                        avgGallonRefueled = 6.0,
                        avgSpentPerRefuel = 120000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = "3",
                            name = "My Second Motorcycle",
                            manufacturer = "Yamaha",
                            model = "NMAX 155",
                            year = 2021,
                            licensePlate = "020-333",
                            vin = "862346987af8787777777"
                        ),
                        latestOdometer = 12000,
                        averageFuelEconomy = 42.0,
                        totalFuelAdded = 60.0,
                        totalSpent = 1800000.0,
                        refuelCount = 10,
                        refuelPerMonth = 1.5,
                        avgGallonRefueled = 6.0,
                        avgSpentPerRefuel = 180000.0
                    ),
                    VehicleWithStats(
                        vehicle = Vehicle(
                            id = "4",
                            name = "My Old Car",
                            manufacturer = "Daihatsu",
                            model = "Xenia",
                            year = 2005,
                            licensePlate = "020-444",
                            vin = "862329873272aaaa8723"
                        ),
                        latestOdometer = 220000,
                        averageFuelEconomy = 10.5,
                        totalFuelAdded = 600.0,
                        totalSpent = 9000000.0,
                        refuelCount = 60,
                        refuelPerMonth = 5.0,
                        avgGallonRefueled = 10.0,
                        avgSpentPerRefuel = 150000.0
                    )
                )
            )
        }
    }
}
