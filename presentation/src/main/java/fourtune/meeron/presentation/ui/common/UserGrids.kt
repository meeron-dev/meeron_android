package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import forutune.meeron.domain.model.WorkspaceUser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserGrids(
    users: List<WorkspaceUser>,
    selectedUsers: SnapshotStateList<WorkspaceUser>
) {
    LazyVerticalGrid(cells = GridCells.Fixed(4)) {
        items(items = users, key = { it.workspaceUserId }) { user ->
            UserItem(
                modifier = Modifier.clickable {
                    if (selectedUsers.contains(user)) {
                        selectedUsers.remove(user)
                    } else {
                        selectedUsers.add(user)
                    }
                },
                user = user,
                selected = selectedUsers.contains(user),
                admin = false
            )
        }
    }
}