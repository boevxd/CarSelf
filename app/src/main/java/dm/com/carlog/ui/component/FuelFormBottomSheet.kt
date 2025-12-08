package dm.com.carlog.ui.component

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.ui.theme.CarLogTheme
import dm.com.carlog.util.convertMillisToDate
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelFormBottomSheet(
    modifier: Modifier = Modifier,
    onSaveButtonClick: () -> Unit = { },
    onDismissRequest: () -> Unit = { },
    onCloseButtonClick: () -> Unit = { },
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    showSheet: Boolean = true,
    isProcessing: Boolean = false,
    isEdit: Boolean = false,
    dateValue: Long = System.currentTimeMillis(),
    onDateValueChange: (Long) -> Unit = { },
    dateError: String? = null,
    odometerValue: String = "",
    onOdometerValueChange: (String) -> Unit = { },
    odometerError: String? = null,
    tripValue: String = "",
    onTripValueChange: (String) -> Unit = { },
    tripError: String? = null,
    fuelAddedValue: String = "",
    onFuelAddedValueChange: (String) -> Unit = { },
    fuelAddedError: String? = null,
    pricePerGallonValue: String = "",
    onPricePerGallonValueChange: (String) -> Unit = { },
    pricePerGallonError: String? = null,
    canCalculateTrip: Boolean = true,
    onCanCalculateTripClick: () -> Unit = { },
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(if (isEdit) R.string.edit_refuel else R.string.record_refuel),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    TextField(
                        value = convertMillisToDate(dateValue),
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(dateValue) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showDatePicker = true
                                    }
                                }
                            },
                        label = { Text(text = stringResource(R.string.date)) },
                        placeholder = { Text(text = stringResource(R.string.date_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null
                            )
                        },
                        readOnly = true,
                        supportingText = if (dateError != null) {
                            { Text(text = dateError) }
                        } else null,
                        isError = dateError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = odometerValue,
                        onValueChange = onOdometerValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.odometer)) },
                        placeholder = { Text(text = stringResource(R.string.odometer_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null
                            )
                        },
                        supportingText = if (odometerError != null) {
                            { Text(text = odometerError) }
                        } else null,
                        isError = odometerError != null,
                        suffix = {
                            Text(text = stringResource(R.string.miles_abbr))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    Row {
                        TextField(
                            value = tripValue,
                            onValueChange = onTripValueChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(3 / 4f),
                            label = { Text(text = stringResource(R.string.trip)) },
                            placeholder = { Text(text = stringResource(R.string.trip_placeholder)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Route,
                                    contentDescription = null
                                )
                            },
                            supportingText = if (tripError != null) {
                                { Text(text = tripError) }
                            } else null,
                            isError = tripError != null,
                            suffix = {
                                Text(text = stringResource(R.string.miles_abbr))
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            shape = MaterialTheme.shapes.small
                        )

                        Button(
                            onClick = onCanCalculateTripClick,
                            modifier = Modifier
                                .weight(1 / 4f)
                                .padding(start = dimensionResource(R.dimen.padding_small))
                                .height(dimensionResource(R.dimen.calc_button_height)),
                            shape = MaterialTheme.shapes.small,
                            enabled = canCalculateTrip,
                        ) {
                            Text(text = stringResource(R.string.calc))
                        }
                    }

                    TextField(
                        value = fuelAddedValue,
                        onValueChange = onFuelAddedValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.fuel_added)) },
                        placeholder = { Text(text = stringResource(R.string.fuel_added_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocalGasStation,
                                contentDescription = null
                            )
                        },
                        supportingText = if (fuelAddedError != null) {
                            { Text(text = fuelAddedError) }
                        } else null,
                        isError = fuelAddedError != null,
                        suffix = {
                            Text(text = stringResource(R.string.gallon_abbr))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = pricePerGallonValue,
                        onValueChange = onPricePerGallonValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.price_per_gallon)) },
                        placeholder = { Text(text = stringResource(R.string.price_per_gallon_placeholder)) },
                        leadingIcon = {
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale.getDefault()).currency?.symbol
                                    ?: "$",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        supportingText = if (pricePerGallonError != null) {
                            { Text(text = pricePerGallonError) }
                        } else null,
                        isError = pricePerGallonError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(vertical = dimensionResource(R.dimen.padding_medium)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                ) {
                    Button(
                        onClick = onCloseButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = onSaveButtonClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                        enabled = !isProcessing,
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.progress_indicator_size)),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(text = stringResource(R.string.save))
                        }
                    }
                }

            }
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { onDateValueChange(it) },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FuelFormBottomSheetPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            FuelFormBottomSheet()
        }
    }
}