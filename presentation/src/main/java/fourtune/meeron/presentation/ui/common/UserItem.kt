package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fourtune.meeron.presentation.R

@Composable
fun UserItem(selected: Boolean, admin: Boolean) {
    val selectedModifier =
        if (selected) Modifier.background(color = colorResource(id = R.color.primary).copy(alpha = 0.7f)) else Modifier
    Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .then(selectedModifier),
            contentAlignment = Alignment.Center
        ) {
            if (admin) {
                Text(
                    text = "공동\n관리자",
                    fontSize = 12.sp,
                    color = colorResource(R.color.dark_gray_white),
                    textAlign = TextAlign.Center
                )
            }
            Image(
                modifier = Modifier.clip(CircleShape),
                painter = painterResource(R.drawable.ic_small_plus),
                contentDescription = null
            )
        }
        Text(
            text = "조수정",
            fontSize = 13.sp,
            color = colorResource(id = if (selected) R.color.primary else R.color.black),
            textAlign = TextAlign.Center
        )
        Text(
            text = "팀장",
            fontSize = 12.sp,
            color = colorResource(id = if (selected) R.color.primary else R.color.dark_gray),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Column {
        UserItem(true, false)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    UserItem(false, false)
}

@Preview(showBackground = true)
@Composable
private fun Preview3() {
    UserItem(true, true)
}