package dm.com.carlog.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.ui.theme.CarLogTheme
import dm.com.carlog.util.convertMillisToDate
import dm.com.carlog.util.formatCurrency
import dm.com.carlog.util.formatNumber

@Composable
fun FuelItem(
    modifier: Modifier = Modifier,
    fuel: Fuel = Fuel(
        id = "0",
        vehicleId = "0",
        date = 0,
        odometer = 0,
        trip = 0,
        fuelAdded = 0.0,
        pricePerGallon = 0.0,
        totalCost = 0.0,
        fuelEconomy = 0.0,
        costPerMile = 0.0
    ),
    onEditClick: (Fuel) -> Unit = { _ -> },
    onDeleteClick: (Fuel) -> Unit = { _ -> },
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = { _ -> }
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onExpandedChange(!expanded) },
                onLongClick = { showMenu = true }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.shadow_elevation)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = convertMillisToDate(fuel.date),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(R.dimen.padding_small),
                            vertical = dimensionResource(R.dimen.padding_extra_small)
                        )
                    )
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1 / 2f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = formatCurrency(fuel.totalCost),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = stringResource(
                            R.string.per_gallon_abbr_value,
                            formatCurrency(fuel.pricePerGallon)
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Column(
                    modifier = Modifier.weight(1 / 2f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.gallon_abbr_value,
                            formatNumber(fuel.fuelAdded, 2)
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )

                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1 / 3f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1 / 3f)
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1 / 3f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                        ) {
                            FuelItemDetail(
                                label = R.string.odometer,
                                value = stringResource(
                                    R.string.miles_abbr_value,
                                    formatNumber(fuel.odometer.toDouble())
                                )
                            )
                            FuelItemDetail(
                                label = R.string.fuel_economy,
                                value = stringResource(
                                    R.string.miles_per_gallon_abbr_value,
                                    formatNumber(fuel.fuelEconomy, 2)
                                )
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                        ) {
                            FuelItemDetail(
                                label = R.string.trip,
                                value = stringResource(
                                    R.string.miles_abbr_value,
                                    formatNumber(fuel.trip.toDouble())
                                )
                            )
                            FuelItemDetail(
                                label = R.string.cost_per_mile,
                                value = stringResource(
                                    R.string.per_mile_value,
                                    formatCurrency(fuel.costPerMile)
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        Button(
                            onClick = { onDeleteClick(fuel) },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text(text = stringResource(R.string.delete))
                        }
                        Button(
                            onClick = { onEditClick(fuel) },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(text = stringResource(R.string.edit))
                        }
                    }
                }
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
                        onEditClick(fuel)
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
                        onDeleteClick(fuel)
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
internal fun PreviewFuelItem() {
    CarLogTheme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            var expanded1 by remember { mutableStateOf(false) }
            var expanded2 by remember { mutableStateOf(true) }

            FuelItem(
                fuel = Fuel(
                    id = "1",
                    vehicleId = "1",
                    date = 1705324800000,
                    odometer = 15000,
                    trip = 250,
                    fuelAdded = 5.0,
                    pricePerGallon = 10_000.0,
                    totalCost = 50_000.0,
                    fuelEconomy = 50.0,
                    costPerMile = 200.0
                ),
                expanded = expanded1,
                onExpandedChange = { expanded1 = it }
            )

            FuelItem(
                fuel = Fuel(
                    id = "2",
                    vehicleId = "1",
                    date = 1705238400000,
                    odometer = 14750,
                    trip = 230,
                    fuelAdded = 4.6,
                    pricePerGallon = 12_000.0,
                    totalCost = 55_200.0,
                    fuelEconomy = 48.7,
                    costPerMile = 240.0
                ),
                expanded = expanded2,
                onExpandedChange = { expanded2 = it }
            )
        }
    }
}