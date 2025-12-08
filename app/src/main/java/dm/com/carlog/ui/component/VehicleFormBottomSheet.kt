package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.ui.theme.CarLogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormBottomSheet(
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
    nameValue: String = "",
    onNameValueChange: (String) -> Unit = { },
    nameError: String? = null,
    manufacturerValue: String = "",
    onManufacturerValueChange: (String) -> Unit = { },
    manufacturerError: String? = null,
    modelValue: String = "",
    onModelValueChange: (String) -> Unit = { },
    modelError: String? = null,
    yearValue: String = "",
    onYearValueChange: (String) -> Unit = { },
    yearError: String? = null,
    licensePlateValue: String = "",
    onLicensePlateValueChange: (String) -> Unit = { },
    licensePlateError: String? = null,
    vinValue: String = "",
    onVinValueChange: (String) -> Unit = { },
    vinError: String? = null
) {
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
                    text = stringResource(if (isEdit) R.string.edit_vehicle else R.string.add_new_vehicle),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    TextField(
                        value = nameValue,
                        onValueChange = onNameValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.vehicle_name)) },
                        placeholder = { Text(text = stringResource(R.string.vehicle_name_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null
                            )
                        },
                        supportingText = if (nameError != null) { { Text(text = nameError) } } else null,
                        isError = nameError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = manufacturerValue,
                        onValueChange = onManufacturerValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.manufacturer)) },
                        placeholder = { Text(text = stringResource(R.string.manufacturer_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Construction,
                                contentDescription = null
                            )
                        },
                        supportingText = if (manufacturerError != null) { { Text(text = manufacturerError) } } else null,
                        isError = manufacturerError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = modelValue,
                        onValueChange = onModelValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.model)) },
                        placeholder = { Text(text = stringResource(R.string.model_placeholder)) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Build, contentDescription = null)
                        },
                        supportingText = if (modelError != null) { { Text(text = modelError) } } else null,
                        isError = modelError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = yearValue,
                        onValueChange = onYearValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.year)) },
                        placeholder = { Text(text = stringResource(R.string.year_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null
                            )
                        },
                        supportingText = if (yearError != null) { { Text(text = yearError) } } else null,
                        isError = yearError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = licensePlateValue,
                        onValueChange = onLicensePlateValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.license_plate)) },
                        placeholder = { Text(text = stringResource(R.string.license_plate_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null
                            )
                        },
                        supportingText = if (licensePlateError != null) { { Text(text = licensePlateError) } } else null,
                        isError = licensePlateError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )

                    TextField(
                        value = vinValue,
                        onValueChange = onVinValueChange,
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.vin)) },
                        placeholder = { Text(text = stringResource(R.string.vin_placeholder)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null
                            )
                        },
                        supportingText = if (vinError != null) { { Text(text = vinError) } } else null,
                        isError = vinError != null,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VehicleFormBottomSheetPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            VehicleFormBottomSheet()
        }
    }
}