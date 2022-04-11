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
                            append("님,\n이제 회의는 걱정마세요")
                        },
                        mainDesc = "똑똑한 회의 관리,미론과 함께",
                        subDesc = "회의 준비부터 마무리까지 맡겨 주세요",
                        image = R.drawable.ic_illustration_onboarding_1
                    )

                }
                OnBoard.Second.ordinal -> {
                    OnBoardingScreen(
                        title = buildAnnotatedString {
                            append("출근 전 ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("1분이면 충분해요")
                            }
                        },
                        mainDesc = "주어진 회의를 한눈에",
                        subDesc = "캘린더와 회의카드로 회의 일정을 \n확인하고 준비해보아요",
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
                                append("실속없는\n회의는 ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("그만")
                                }
                            },
                            mainDesc = "모두에게 명확한 회의",
                            subDesc = "회의 전에 정보를 숙지하고,\n본인의 상태를 공유할 수 있어요",
                            image = R.drawable.ic_illustration_onboarding_3,
                        )
                    }
                }
//                OnBoard.Forth.ordinal -> {
//                    OnBoardingScreen(
//                        title = buildAnnotatedString {
//                            append("마무리까지\n")
//                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
//                                append("완벽하게")
//                            }
//                        },
//                        mainDesc = "이번 회의도 문제없이",
//                        subDesc = "회의 결과를 확인하고\n부담없이 피드백을 남겨보아요",
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
                text = "시작하기"
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