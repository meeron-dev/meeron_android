package fourtune.meeron.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun TOSScreen(onNext: () -> Unit = {}) {
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 38.dp, end = 38.dp),
            onClick = onNext,
            enabled = allAgree,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primary),
            ),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Text(
                text = stringResource(id = R.string.next),
                fontSize = 18.sp,
                color = colorResource(id = if (allAgree) R.color.white else R.color.light_gray)
            )
        }

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
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com")))
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
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com")))
        }
    )
}

@Composable
private fun MeeronLogoText(text: String) {
    Image(
        modifier = Modifier.padding(top = 90.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
        painter = painterResource(id = R.drawable.ic_meeron_symbol_logo),
        contentDescription = null
    )
    Text(
        modifier = Modifier.padding(horizontal = 20.dp),
        text = buildAnnotatedString {
            append(text)
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), 0, 2)
        },
        fontSize = 24.sp,
        color = colorResource(id = R.color.black),
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
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TOSScreen()
}