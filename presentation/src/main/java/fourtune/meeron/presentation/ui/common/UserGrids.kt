package fourtune.meeron.presentation.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import forutune.meeron.domain.model.WorkspaceUser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserGrids(
    displayUsers: List<WorkspaceUser>,
    selectedUsers: SnapshotStateList<WorkspaceUser> = remember {
        mutableStateListOf()
    },
    onSelectUser: (WorkspaceUser) -> Unit = { user ->
        when {
            ownerIds.any { it == user.workspaceUserId } -> {
                //do nothing
            }
            selectedUsers.contains(user) -> {
                selectedUsers.remove(user)
            }
            else -> {
                selectedUsers.add(user)
            }
        }
    },
    ownerIds: List<Long> = emptyList(),
) {
    LazyVerticalGrid(cells = GridCells.Fixed(4)) {
        items(items = displayUsers, key = { it.workspaceUserId }) { user ->
            UserItem(
                modifier = Modifier.clickable { onSelectUser(user) },
                user = user,
                selected = selectedUsers.contains(user) || ownerIds.any { it == user.workspaceUserId },
                admin = ownerIds.any { it == user.workspaceUserId }
            )
        }
    }
}