package fourtune.meeron.presentation.ui.createworkspace

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import forutune.meeron.domain.model.EntryPointType
import forutune.meeron.domain.model.WorkSpace
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronProgressIndicator
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.ProfileImage
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateWorkspaceProfileScreen(
    viewModel: CreateWorkspaceProfileViewModel = hiltViewModel(),
    goToCreateTeam: (WorkSpace) -> Unit = {},
    goToHome: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let(viewModel::addImage)
        }

    val storagePermission = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    LaunchedEffect(key1 = currentComposer) {
        viewModel.toast.onEach {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }.launchIn(this)
    }
    Scaffold(
        topBar = { CenterTextTopAppBar(text = "????????? ??????") },
        content = {
            MeeronSingleButtonBackGround(
                enable = uiState.isVerify,
                onClick = {
                    when (viewModel.entryPointType) {
                        EntryPointType.Normal -> goToCreateTeam(uiState.workSpace)
                        EntryPointType.DynamicLink -> viewModel.createWorkspaceUser(goToHome)
                        EntryPointType.Edit -> viewModel.changeWorkspaceUser(onBack)
                    }
                }
            ) {
                MeeronProgressIndicator(showLoading = uiState.loading)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    contentPadding = PaddingValues(top = 50.dp, bottom = 100.dp)
                ) {
                    item {
                        Text(text = "??????????????????\n???????????? ??????????????????.", fontSize = 25.sp, color = colorResource(id = R.color.black))
                    }

                    item {
                        ProfileImage(
                            image = uiState.fileName
                        ) {
                            when (val status = storagePermission.status) {
                                PermissionStatus.Granted -> {
                                    pickPictureLauncher.launch("image/*")
                                }
                                is PermissionStatus.Denied -> {
                                    if (status.shouldShowRationale) {
                                        //????????????
                                    } else {
                                        //?????? ????????? ??????...
                                        // ACTION_APPLICATION_DETAILS_SETTINGS ???????????????
                                    }
                                    storagePermission.launchPermissionRequest()
                                }
                            }
                        }
                    }

                    items(CreateWorkspaceProfileViewModel.Info.values()) { info ->
                        when (info) {
                            CreateWorkspaceProfileViewModel.Info.NickName -> {
                                MeeronActionBox(
                                    factory = ContentFactory.LimitTextField(
                                        text = viewModel.workspaceInfoMap[info].orEmpty(),
                                        isEssential = info.isEssential,
                                        limit = info.limit,
                                        onValueChange = { viewModel.changeName(info, it) },
                                        error = uiState.isDuplicateNickname,
                                        errorText = "?????? ???????????? ???????????????."
                                    ),
                                    title = stringResource(id = info.title) + if (info.isEssential) " *" else ""
                                )

                            }
                            else -> {
                                MeeronActionBox(
                                    factory = ContentFactory.LimitTextField(
                                        text = viewModel.workspaceInfoMap[info].orEmpty(),
                                        isEssential = info.isEssential,
                                        limit = info.limit,
                                        onValueChange = { viewModel.changeText(info, it) },
                                    ),
                                    title = stringResource(id = info.title) + if (info.isEssential) " *" else ""
                                )

                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    CreateWorkspaceProfileScreen()
}