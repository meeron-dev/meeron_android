package fourtune.meeron.presentation.ui.start

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.ui.common.MeeronSingleButton
import kotlinx.coroutines.launch


private enum class OnBoard {
    First,
    Second,
    Third,
//    Forth
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(viewModel: OnBoardingViewModel = hiltViewModel(), goToHome: () -> Unit = {}) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 50.dp, bottom = 50.dp)
            .fillMaxSize(),
    ) {

        HorizontalPager(
            count = OnBoard.values().size,
            state = pagerState,
        ) {

            when (pagerState.currentPage) {
                OnBoard.First.ordinal -> {
                    OnBoardingScreen(
                        title = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(uiState.name)
                            }
                            append("???,\n?????? ????????? ???????????????")
                        },
                        mainDesc = "????????? ?????? ??????,????????? ??????",
                        subDesc = "?????? ???????????? ??????????????? ?????? ?????????",
                        image = R.drawable.ic_illustration_onboarding_1
                    )

                }
                OnBoard.Second.ordinal -> {
                    OnBoardingScreen(
                        title = buildAnnotatedString {
                            append("?????? ??? ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("1????????? ????????????")
                            }
                        },
                        mainDesc = "????????? ????????? ?????????",
                        subDesc = "???????????? ??????????????? ?????? ????????? \n???????????? ??????????????????",
                        image = R.drawable.ic_illustration_onboarding_2
                    )
                }
                OnBoard.Third.ordinal -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = R.drawable.onboarding_background),
                            contentDescription = null
                        )
                        OnBoardingScreen(
                            title = buildAnnotatedString {
                                append("????????????\n????????? ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("??????")
                                }
                            },
                            mainDesc = "???????????? ????????? ??????",
                            subDesc = "?????? ?????? ????????? ????????????,\n????????? ????????? ????????? ??? ?????????",
                            image = R.drawable.ic_illustration_onboarding_3,
                        )
                    }
                }
//                OnBoard.Forth.ordinal -> {
//                    OnBoardingScreen(
//                        title = buildAnnotatedString {
//                            append("???????????????\n")
//                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
//                                append("????????????")
//                            }
//                        },
//                        mainDesc = "?????? ????????? ????????????",
//                        subDesc = "?????? ????????? ????????????\n???????????? ???????????? ???????????????",
//                        image = R.drawable.ic_illustration_onboarding_4
//                    )
//                }
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            HorizontalPagerIndicator(pagerState = pagerState, activeColor = colorResource(id = R.color.primary))
            Spacer(modifier = Modifier.padding(9.dp))
            MeeronSingleButton(
                modifier = Modifier.padding(horizontal = 18.dp),
                onClick = {
                    if (pagerState.currentPage == OnBoard.values().size - 1) {
                        goToHome()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                },
                enable = true,
                text = "????????????"
            )
        }

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    title: AnnotatedString = AnnotatedString(""),
    mainDesc: String,
    subDesc: String,
    @DrawableRes image: Int,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 24.sp,
            color = colorResource(id = R.color.black)
        )
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(40.dp))
            Image(
                painter = painterResource(id = image),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(18.dp))
            Text(
                text = mainDesc,
                fontSize = 16.sp,
                color = colorResource(id = R.color.dark_gray),
                fontWeight = FontWeight.Bold,
                lineHeight = 39.sp
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = subDesc,
                fontSize = 14.sp,
                color = colorResource(id = R.color.gray),
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.padding(28.dp))
        }

    }
}


@Preview
@Composable
private fun Preview() {
}