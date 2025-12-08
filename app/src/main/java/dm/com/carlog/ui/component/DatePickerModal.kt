package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.util.PastOrPresentSelectableDates
import dm.com.carlog.ui.theme.CarLogTheme
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentZonedDateTime = ZonedDateTime.now(ZoneId.systemDefault())
    val zoneOffset = currentZonedDateTime.offset
    val initialDateMillis = currentZonedDateTime.toInstant().toEpochMilli() + zoneOffset.totalSeconds * 1000

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = PastOrPresentSelectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis ?: initialDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
fun DatePickerModalPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DatePickerModal(onDateSelected = {}, onDismiss = {})
        }
    }
}