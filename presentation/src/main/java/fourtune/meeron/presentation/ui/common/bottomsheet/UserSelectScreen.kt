package fourtune.meeron.presentation.ui.common.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import forutune.meeron.domain.model.WorkspaceUser
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.UserGrids

@Composable
fun UserSelectScreen(
    title: String,
    users: List<WorkspaceUser> = emptyList(),
    onSearch: (String) -> Unit = {},
    onComplete: (List<WorkspaceUser>) -> Unit = {},
    selectedUsers: SnapshotStateList<WorkspaceUser> = remember {
        mutableStateListOf()
    },
    searchText: String = "",
    ownerIds: List<Long> = emptyList()
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height((LocalConfiguration.current.screenHeightDp * 0.9).dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MeeronSingleButtonBackGround(
            modifier = Modifier.padding(bottom = 50.dp),
            enable = selectedUsers.isNotEmpty(),
            onClick = { onComplete(selectedUsers) }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 46.dp),
                    text = title,
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.dark_gray)
                )
                Spacer(modifier = Modifier.padding(40.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = onSearch,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row {
                                Image(imageVector = Icons.Default.Search, contentDescription = null)
                                Spacer(modifier = Modifier.padding(6.dp))
                                innerTextField()
                            }
                            Spacer(modifier = Modifier.padding(6.dp))
                            Divider()
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                UserGrids(
                    displayUsers = users,
                    selectedUsers = selectedUsers,
                    ownerIds = ownerIds
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    UserSelectScreen(
        title = "제목입니다.",
        selectedUsers = remember {
            mutableStateListOf()
        },
    )
}