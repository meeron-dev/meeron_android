package fourtune.meeron.presentation.ui.createworkspace

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import forutune.meeron.domain.model.WorkSpace
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButtonBackGround
import fourtune.meeron.presentation.ui.common.action.ContentFactory
import fourtune.meeron.presentation.ui.common.action.MeeronActionBox
import fourtune.meeron.presentation.ui.common.topbar.CenterTextTopAppBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateWorkspaceProfileScreen(
    viewModel: CreateWorkspaceProfileViewModel = hiltViewModel(),
    onNext: (WorkSpace) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    val pickPictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let(viewModel::addImage)
        }

    val storagePermission = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    Scaffold(
        topBar = { CenterTextTopAppBar(text = "프로필 작성") },
        content = {
            MeeronSingleButtonBackGround(
                enable = uiState.isVerify,
                onClick = { onNext(uiState.workSpace) }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    contentPadding = PaddingValues(top = 50.dp, bottom = 100.dp)
                ) {
                    item {
                        Text(text = "워크스페이스\n프로필을 작성해주세요.", fontSize = 25.sp, color = colorResource(id = R.color.black))
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
                                        //일반취소
                                    } else {
                                        //거절 눌렀을 경우...
                                        // ACTION_APPLICATION_DETAILS_SETTINGS 리다이렉트
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
                                        errorText = "이미 사용중인 별명입니다."
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

@Composable
private fun ProfileImage(image: String, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(color = Color(0xfff2f5f9)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(data = image, builder = {
                    placeholder(R.drawable.ic_profile_non)
                    error(R.drawable.ic_profile_non)
                }),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CreateWorkspaceProfileScreen { _ ->

    }
}