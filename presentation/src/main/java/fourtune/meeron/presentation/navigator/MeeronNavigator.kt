package fourtune.meeron.presentation.navigator

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import forutune.meeron.domain.Const
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.navigator.ext.encodeJson
import fourtune.meeron.presentation.ui.calendar.CalendarScreen
import fourtune.meeron.presentation.ui.calendar.all.ShowAllScreen
import fourtune.meeron.presentation.ui.create.agenda.CreateAgendaScreen
import fourtune.meeron.presentation.ui.create.complete.CompleteMeetingScreen
import fourtune.meeron.presentation.ui.create.date.CreateMeetingDateScreen
import fourtune.meeron.presentation.ui.create.information.CreateMeetingInfoScreen
import fourtune.meeron.presentation.ui.create.participants.CreateMeetingParticipantsScreen
import fourtune.meeron.presentation.ui.create.time.CreateMeetingTimeScreen
import fourtune.meeron.presentation.ui.home.HomeScreen
import fourtune.meeron.presentation.ui.login.LoginScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun destination(vararg path: Any) = route() + path.joinToString("") { "/{$it}" }
    fun route(vararg argument: Any) = route() + argument.joinToString("") { "/$it" }

    object Login : Navigate
    object Calendar : Navigate
    object ShowAll : Navigate

    sealed class BottomNavi(@DrawableRes val image: Int, @StringRes val text: Int) : Navigate {
        object Home : BottomNavi(R.drawable.ic_navi_home, R.string.home)
        object Team : BottomNavi(R.drawable.ic_navi_team, R.string.team)
        object My : BottomNavi(R.drawable.ic_navi_door, R.string.my_merron)
    }

    sealed interface CreateMeeting : Navigate {
        object Date : CreateMeeting
        object Time : CreateMeeting
        object Information : CreateMeeting
        object Agenda : CreateMeeting
        object Participants : CreateMeeting
        object Complete : CreateMeeting
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MeeronNavigator() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Navigate.BottomNavi.Home.route()
    ) {

        composable(route = Navigate.Login.route()) {
            LoginScreen(isLoginSuccess = {
                navController.popBackStack()
                navController.navigate(Navigate.BottomNavi.Home.route())
            })
        }

        composable(route = Navigate.BottomNavi.Home.route()) {
            HomeScreen(
                openCalendar = {
                    navController.navigate(Navigate.Calendar.route())
                },
                onBottomNaviClick = { bottomNavi ->
                    when (bottomNavi) {
                        Navigate.BottomNavi.Home -> navController.navigate(Navigate.BottomNavi.Home.route())
                        Navigate.BottomNavi.Team -> navController.navigate(Navigate.BottomNavi.Team.route())
                        Navigate.BottomNavi.My -> navController.navigate(Navigate.BottomNavi.My.route())
                    }
                },
                addMeeting = { navController.navigate(Navigate.CreateMeeting.Date.route()) }
            )
        }
        composable(Navigate.BottomNavi.Team.route()) {

        }

        composable(Navigate.BottomNavi.My.route()) {

        }

        composable(
            route = Navigate.Calendar.route(),
            enterTransition = { slideInVertically(initialOffsetY = { it }) },
            exitTransition = { slideOutVertically(targetOffsetY = { it }) },
        ) {
            CalendarScreen(
                onBack = { navController.navigateUp() },
                showAll = { navController.navigate(Navigate.ShowAll.route()) }
            )
        }

        composable(Navigate.ShowAll.route()) {
            ShowAllScreen(onAction = { navController.navigateUp() })
        }

        composable(Navigate.CreateMeeting.Date.route()) {
            CreateMeetingDateScreen(
                onAction = { navController.navigateUp() },
                onNext = { navController.navigate(Navigate.CreateMeeting.Time.route(it)) }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Time.destination(Const.Date),
            arguments = listOf(element = navArgument(Const.Date) { type = NavType.StringType })
        ) {
            CreateMeetingTimeScreen(
                onAction = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() },
                onNext = { date, time -> navController.navigate(Navigate.CreateMeeting.Information.route(date, time)) }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Information.destination(Const.Date, Const.Time),
            arguments = listOf(
                navArgument(Const.Date) { type = NavType.StringType },
                navArgument(Const.Time) { type = NavType.StringType }
            )
        ) {
            CreateMeetingInfoScreen(
                onNext = { meeting ->
                    navController.navigate(
                        Navigate.CreateMeeting.Agenda.route(meeting.encodeJson())
                    )
                },
                onPrevious = { navController.navigateUp() },
                onLoad = { navController.navigate(Navigate.Calendar.route()) }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Agenda.destination(Const.Meeting),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType() },
            )
        ) {
            CreateAgendaScreen(
                onAction = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() },
                onNext = { meeting ->
                    navController.navigate(
                        Navigate.CreateMeeting.Participants.route(meeting.encodeJson())
                    )
                }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Participants.destination(Const.Meeting),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType() },
            )
        ) {
            CreateMeetingParticipantsScreen(
                onAction = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() },
                onNext = { meeting ->
                    navController.navigate(
                        Navigate.CreateMeeting.Complete.route(meeting.encodeJson())
                    )
                }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Complete.destination(Const.Meeting),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType() },
            )
        ) {
            CompleteMeetingScreen(
                onAction = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onNext = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() }
            )
        }
    }
}
