package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun MeeronLogoText(text: String) {
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