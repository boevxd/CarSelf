package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleWithStats
import dm.com.carlog.util.formatCurrency
import dm.com.carlog.util.formatNumber
import dm.com.carlog.ui.theme.CarLogTheme

@Composable
fun DetailStats(
    modifier: Modifier = Modifier,
    vehicleWithStats: VehicleWithStats = VehicleWithStats(
        vehicle = Vehicle(
            id = "1",
            name = "Placeholder",
            manufacturer = "Placeholder",
            model = "Placeholder",
            year = 0,
            licensePlate = "Placeholder",
            vin = "Placeholder"
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
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(2 / 3f),
                title = R.string.vin,
                value = vehicleWithStats.vehicle.vin
            )

            DetailCard(
                modifier = Modifier.weight(1 / 3f),
                title = R.string.license_plate,
                value = vehicleWithStats.vehicle.licensePlate
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(3 / 5f),
                title = R.string.odometer,
                value = stringResource(
                    R.string.miles_abbr_value,
                    formatNumber(vehicleWithStats.latestOdometer.toDouble())
                )
            )

            DetailCard(
                modifier = Modifier.weight(2 / 5f),
                title = R.string.fuel_economy,
                value = stringResource(
                    R.string.miles_per_gallon_abbr_value,
                    formatNumber(vehicleWithStats.averageFuelEconomy, 2)
                )
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(1 / 2f),
                title = R.string.avg_spent_per_refuel,
                value = formatCurrency(vehicleWithStats.avgSpentPerRefuel)
            )

            DetailCard(
                modifier = Modifier.weight(1 / 2f),
                title = R.string.refuel_per_month,
                value = stringResource(
                    R.string.refuel_per_month_value,
                    formatNumber(vehicleWithStats.refuelPerMonth, 1)
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            DetailCard(
                modifier = Modifier.weight(1 / 2f),
                title = R.string.total_refuel,
                value = stringResource(
                    R.string.gallon_abbr_value,
                    formatNumber(vehicleWithStats.totalFuelAdded, 2)
                )
            )

            DetailCard(
                modifier = Modifier.weight(1 / 2f),
                title = R.string.total_spent,
                value = formatCurrency(vehicleWithStats.totalSpent)
            )
        }
    }
}

@Preview
@Composable
fun DetailStatsPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailStats(
                vehicleWithStats = VehicleWithStats(
                    vehicle = Vehicle(
                        id = "1",
                        name = "My Motorcycle",
                        manufacturer = "Honda",
                        model = "PCX 160",
                        year = 2024,
                        licensePlate = "020-XHL",
                        vin = "862346987af878723"
                    ),
                    latestOdometer = 150000,
                    averageFuelEconomy = 39.5,
                    totalFuelAdded = 120.0,
                    totalSpent = 15000000.0,
                    refuelCount = 12,
                    refuelPerMonth = 2.5,
                    avgGallonRefueled = 7.0,
                    avgSpentPerRefuel = 85000.0
                )
            )
        }
    }

}