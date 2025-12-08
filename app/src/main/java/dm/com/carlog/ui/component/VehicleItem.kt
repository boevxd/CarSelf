package dm.com.carlog.ui.component

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleWithStats
import dm.com.carlog.ui.theme.CarLogTheme
import dm.com.carlog.util.formatNumber

@Composable
fun VehicleItem(
    modifier: Modifier = Modifier,
    vehicleWithStats: VehicleWithStats = VehicleWithStats(
        vehicle = Vehicle(
            id = "1",
            name = "Placeholder",
            manufacturer = "Placeholder",
            model = "Placeholder",
            year = 0,
            licensePlate = "222-333",
            vin = "1111111111111"
        ),
        latestOdometer = 0,
        averageFuelEconomy = 0.0,
        totalFuelAdded = 0.0,
        totalSpent = 0.0,
        refuelCount = 0,
        refuelPerMonth = 0.0,
        avgGallonRefueled = 0.0,
        avgSpentPerRefuel = 0.0
    ),
    onClick: () -> Unit = { },
    onEditClick: (Vehicle) -> Unit = { _ -> },
    onDeleteClick: (Vehicle) -> Unit = { _ -> }
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showMenu = true }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.shadow_elevation)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.6f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.vehicle_manufacturer_model,
                        vehicleWithStats.vehicle.manufacturer,
                        vehicleWithStats.vehicle.model,
                        vehicleWithStats.vehicle.year.toString()
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = vehicleWithStats.vehicle.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(
                        R.string.vehicle_avg,
                        formatNumber(vehicleWithStats.averageFuelEconomy, 2)
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text(
                    text = stringResource(
                        R.string.miles_abbr_value,
                        formatNumber(vehicleWithStats.latestOdometer.toDouble())
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis
                )

            }
        }

        Box(
            modifier = Modifier.align(Alignment.End),
        ) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                shape = MaterialTheme.shapes.medium,
                containerColor = CardDefaults.cardColors().containerColor,
                shadowElevation = dimensionResource(R.dimen.shadow_elevation)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        showMenu = false
                        onEditClick(vehicleWithStats.vehicle)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit)
                        )
                    },
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        showMenu = false
                        onDeleteClick(vehicleWithStats.vehicle)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete)
                        )
                    },
                    colors = MenuDefaults.itemColors().copy(
                        textColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewVehicleItem() {
    CarLogTheme(darkTheme = true) {
        VehicleItem(
            vehicleWithStats = VehicleWithStats(
                vehicle = Vehicle(
                    id = "1",
                    name = "Main Motorcycle",
                    manufacturer = "Honda",
                    model = "PCX 160",
                    year = 2024,
                    licensePlate = "777-777",
                    vin = "9999999999999"
                ),
                latestOdometer = 980000,
                averageFuelEconomy = 39.99,
                totalFuelAdded = 420.0,
                totalSpent = 4200000.0,
                refuelCount = 69,
                refuelPerMonth = 4.2
            ),
        )
    }
}