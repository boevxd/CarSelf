package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.ui.theme.CarLogTheme

@Composable
fun DetailTopButtons(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = { },
    onEditClick: () -> Unit = { },
    onDeleteClick: () -> Unit = { },
    onExportVehicleClick: () -> Unit = { },
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onBackClick,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = dimensionResource(R.dimen.shadow_elevation)
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            Button(
                onClick = onDeleteClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                )
            ) {
                Text(text = stringResource(R.string.delete))
            }

            Button(
                onClick = onEditClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                )
            ) {
                Text(text = stringResource(R.string.edit))
            }

            Button(
                onClick = onExportVehicleClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = dimensionResource(R.dimen.shadow_elevation)
                )
            ) {
                Text(text = stringResource(R.string.export))
            }
        }
    }

}

@Preview
@Composable
fun DetailTopButtonsPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailTopButtons()
        }
    }
}