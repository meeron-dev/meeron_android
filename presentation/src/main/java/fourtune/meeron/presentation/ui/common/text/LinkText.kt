package fourtune.meeron.presentation.ui.common.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun LinkText(text: String) = buildAnnotatedString {
    append(text)
    addStyle(SpanStyle(textDecoration = TextDecoration.Underline), 0, text.length)
}