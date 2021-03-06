package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R

@Composable
fun DeletedUserItem(modifier: Modifier = Modifier, user: WorkspaceUser, onClickDelete: () -> Unit = {}) {
    Box(contentAlignment = Alignment.Center) {
        UserItem(modifier = modifier, user = user, selected = false, admin = false)
        Surface(
            modifier = Modifier
                .clip(CircleShape)
                .align(Alignment.TopEnd)
                .shadow(1.dp, shape = CircleShape)
                .clickable(onClick = onClickDelete),
            elevation = 4.dp,
            color = colorResource(id = R.color.dark_gray_white)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

@Composable
fun UserItem(modifier: Modifier = Modifier, user: WorkspaceUser, selected: Boolean, admin: Boolean) {
    val selectedModifier =
        if (selected) modifier.background(color = colorResource(id = R.color.primary).copy(alpha = 0.7f)) else modifier
    Column(modifier = Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .then(selectedModifier),
            contentAlignment = Alignment.Center
        ) {
            if (admin && selected) {
                Text(
                    text = stringResource(id = R.string.owners),
                    fontSize = 12.sp,
                    color = colorResource(R.color.dark_gray_white),
                    textAlign = TextAlign.Center
                )
            }
            Image(
                modifier = Modifier.clip(CircleShape),
                painter = rememberImagePainter(
                    data = user.profileImageUrl,
                    builder = {
                        placeholder(R.drawable.ic_person_24)
                        error(R.drawable.ic_person_24)
                    }
                ),
                contentDescription = null
            )
        }
        Text(
            text = user.nickname,
            fontSize = 13.sp,
            color = colorResource(id = if (selected) R.color.primary else R.color.black),
            textAlign = TextAlign.Center
        )
        Text(
            text = user.position,
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
        UserItem(user = WorkspaceUser(nickname = "zero", position = "android"), selected = true, admin = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    UserItem(user = WorkspaceUser(nickname = "zero", position = "android"), selected = false, admin = false)
}

@Preview(showBackground = true)
@Composable
private fun Preview3() {
    UserItem(user = WorkspaceUser(nickname = "zero", position = "android"), selected = true, admin = true)
}

@Preview(showBackground = true)
@Composable
private fun Preview4() {
    DeletedUserItem(user = WorkspaceUser(nickname = "zero", position = "android"))
}
