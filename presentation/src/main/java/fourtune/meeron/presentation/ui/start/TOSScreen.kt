package fourtune.meeron.presentation.ui.start

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButton
import fourtune.meeron.presentation.ui.common.text.MeeronLogoText

@Composable
fun TOSScreen(viewModel: TOSViewModel = hiltViewModel(), onNext: (EntryPointType) -> Unit = {}) {
    val context = LocalContext.current
    var allAgree by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            MeeronLogoText("미론에 오신 것을\n환영합니다.")
            Spacer(modifier = Modifier.padding(55.dp))
            TOSOptions(context) { allAgree = it }
        }
        MeeronSingleButton(
            modifier = Modifier.padding(bottom = 50.dp, start = 38.dp, end = 38.dp),
            onClick = { onNext(viewModel.entryPointType) },
            enable = allAgree
        )
    }
}

@Composable
private fun TOSOptions(
    context: Context,
    onAllAgreed: (Boolean) -> Unit
) {
    var allAgree by remember {
        mutableStateOf(false)
    }
    val agrees = remember {
        mutableStateListOf(false, false)
    }
    LaunchedEffect(key1 = allAgree) {
        onAllAgreed(allAgree)
    }
    Row(modifier = Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        IconToggleButton(
            checked = allAgree,
            onCheckedChange = {
                allAgree = it
                agrees[0] = it
                agrees[1] = it
            }
        ) {
            Image(
                painter = painterResource(id = if (allAgree) R.drawable.toggle_enable else R.drawable.toggle_disable),
                contentDescription = null
            )
        }
        Text(text = "약관 전체 동의", fontSize = 16.sp, color = colorResource(id = R.color.black))
    }
    Spacer(modifier = Modifier.padding(10.dp))
    Divider(color = colorResource(id = R.color.dark_gray_white))
    Spacer(modifier = Modifier.padding(15.dp))
    MeeronCheckBox(
        text = "이용약관 동의(필수)",
        check = agrees[0],
        onCheckedChange = { checked ->
            agrees[0] = checked
            allAgree = agrees.all { it }

        },
        onDetail = {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Const.TermsOfUse)
                )
            )
        }
    )
    MeeronCheckBox(
        text = "개인정보 수집 및 이용동의(필수)",
        check = agrees[1],
        onCheckedChange = { checked ->
            agrees[1] = checked
            allAgree = agrees.all { it }
        },
        onDetail = {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Const.PersonalInformationCollectionAndUsageAgreement)
                )
            )
        }
    )
}

@Composable
private fun MeeronCheckBox(text: String, check: Boolean, onCheckedChange: (Boolean) -> Unit, onDetail: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconToggleButton(checked = check, onCheckedChange = onCheckedChange) {
                Image(
                    painter = painterResource(id = if (check) R.drawable.toggle_enable else R.drawable.toggle_disable),
                    contentDescription = null
                )
            }
            Text(text = text, fontSize = 15.sp, color = colorResource(id = R.color.black))
        }
        Image(
            modifier = Modifier.clickable(onClick = onDetail),
            painter = painterResource(id = R.drawable.ic_right_arrow_24),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TOSScreen()
}