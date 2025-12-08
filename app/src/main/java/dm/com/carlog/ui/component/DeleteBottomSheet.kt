package dm.com.carlog.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dm.com.carlog.R
import dm.com.carlog.ui.theme.CarLogTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBottomSheet(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = { },
    onDismissRequest: () -> Unit = { },
    onCloseButtonClick: () -> Unit = { },
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ),
    showSheet: Boolean = true,
    isProcessing: Boolean = false,
    name: String = "Placeholder",
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
                    text = stringResource(R.string.delete_confirmation),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.delete_text, name),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Column(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_medium)),
                ) {
                    Button(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .fillMaxWidth(),
                        enabled = !isProcessing,
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.progress_indicator_size)),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Text(text = stringResource(R.string.delete))
                        }
                    }

                    Button(
                        onClick = onCloseButtonClick,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DeleteBottomSheetPreview() {
    CarLogTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            DeleteBottomSheet()
        }
    }
}