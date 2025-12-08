package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleWithStats
import dm.com.carlog.ui.theme.CarLogTheme

@Composable
fun DetailHeader(
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
        averageFuelEconomy = 0.0
    ),
    refuelRecorded: Int = 0
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.shadow_elevation)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = vehicleWithStats.vehicle.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.vehicle_manufacturer_model,
                    vehicleWithStats.vehicle.manufacturer,
                    vehicleWithStats.vehicle.model,
                    vehicleWithStats.vehicle.year.toString()
                ),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(R.string.recorded_refuels, refuelRecorded),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun DetailHeaderPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailHeader(
                vehicleWithStats = VehicleWithStats(
                    vehicle = Vehicle(
                        id = "1",
                        name = "My Motorcycle",
                        manufacturer = "Honda",
                        model = "PCX 160",
                        year = 2024,
                        licensePlate = "444-123",
                        vin = "4444444444444444"
                    ),
                    latestOdometer = 15000,
                    averageFuelEconomy = 39.5
                )
            )
        }
    }
}