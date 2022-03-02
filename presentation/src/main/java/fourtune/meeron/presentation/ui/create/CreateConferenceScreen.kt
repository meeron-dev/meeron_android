package fourtune.meeron.presentation.ui.create

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.CenterTextTopAppBar
import fourtune.meeron.presentation.ui.theme.MeeronTheme

sealed interface CreateConferenceDateEvent {
    object OnBack : CreateConferenceDateEvent
    object OnNext : CreateConferenceDateEvent
    class ChangeDate() : CreateConferenceDateEvent
}

@Composable
fun CreateConferenceScreen(
    viewModel: CreateConferenceViewModel = hiltViewModel(),
    onAction: () -> Unit,
    onNext: () -> Unit
) {
    CreateConferenceScreen(
        event = { event ->
            when (event) {
                is CreateConferenceDateEvent.ChangeDate -> TODO()
                CreateConferenceDateEvent.OnBack -> onAction()
                CreateConferenceDateEvent.OnNext -> onNext()
            }
        }
    )
}

@Composable
private fun CreateConferenceScreen(event: (CreateConferenceDateEvent) -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterTextTopAppBar(
                onAction = { event(CreateConferenceDateEvent.OnBack) },
                text = {
                    Text(
                        text = stringResource(id = R.string.create_conference),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.black)
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 50.dp, horizontal = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            DateScreen(openDatePicker = { context ->
                showDatePickerDialog(context)
            })

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { event(CreateConferenceDateEvent.OnNext) },
                contentPadding = PaddingValues(vertical = 18.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.primary))
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DateScreen(modifier: Modifier = Modifier, openDatePicker: (context: Context) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.create_data_title),
            fontSize = 25.sp,
            color = colorResource(id = R.color.black)
        )
        Spacer(modifier = Modifier.padding(32.dp))
        Column(
            Modifier
                .clickable { openDatePicker(context) }
        ) {
            Text(
                text = "YY/MM/DD",
                fontSize = 25.sp,
                color = colorResource(id = R.color.black),
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
            Divider(color = colorResource(id = R.color.light_gray))
        }
    }
}

private fun showDatePickerDialog(context: Context) {
    DatePickerDialog(
        context,
        R.style.DatePickerStyle,
        { _, year, month, dayOfMonth ->

        },
        2,
        2,
        2
    ).show()
}

@Preview
@Composable
private fun CreateConferenceScreenPrev() {
    MeeronTheme {
        CreateConferenceScreen()
    }
}