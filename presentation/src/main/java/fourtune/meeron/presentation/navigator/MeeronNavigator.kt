package fourtune.meeron.presentation.navigator

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import forutune.meeron.domain.Const
import fourtune.meeron.presentation.R
import fourtune.meeron.presentation.navigator.ext.encodeJson
import fourtune.meeron.presentation.navigator.type.DateType
import fourtune.meeron.presentation.navigator.type.MeetingType
import fourtune.meeron.presentation.navigator.type.WorkSpaceType
import fourtune.meeron.presentation.ui.DynamicLinkEntryScreen
import fourtune.meeron.presentation.ui.NameInitScreen
import fourtune.meeron.presentation.ui.OnBoardingScreen
import fourtune.meeron.presentation.ui.TOSScreen
import fourtune.meeron.presentation.ui.calendar.CalendarScreen
import fourtune.meeron.presentation.ui.calendar.all.ShowAllScreen
import fourtune.meeron.presentation.ui.createmeeting.agenda.CreateAgendaScreen
import fourtune.meeron.presentation.ui.createmeeting.complete.CompleteMeetingScreen
import fourtune.meeron.presentation.ui.createmeeting.date.CreateMeetingDateScreen
import fourtune.meeron.presentation.ui.createmeeting.information.CreateMeetingInfoScreen
import fourtune.meeron.presentation.ui.createmeeting.participants.CreateMeetingParticipantsScreen
import fourtune.meeron.presentation.ui.createmeeting.time.CreateMeetingTimeScreen
import fourtune.meeron.presentation.ui.createworkspace.*
import fourtune.meeron.presentation.ui.home.HomeScreen
import fourtune.meeron.presentation.ui.login.LoginScreen

sealed interface Navigate {
    fun route() = requireNotNull(this::class.qualifiedName)
    fun route(vararg argument: Any) = route() + argument.joinToString("") { "/$it" }
    fun deepLink(vararg argument: String): String = SCHEME + queries(argument)

    fun destination(vararg path: Any) = route() + path.joinToString("") { "/{$it}" }

    object Login : Navigate
    object Calendar : Navigate
    object ShowAll : Navigate

    object OnBoarding : Navigate

    object InviteDynamicLink : Navigate

    sealed interface SignIn : Navigate {
        object TOS : SignIn
        object NameInit : SignIn
    }

    sealed interface CreateWorkspace : Navigate {
        object CreateOrJoin : CreateWorkspace
        object Join : CreateWorkspace
        object Name : CreateWorkspace
        object Profile : CreateWorkspace
        object Team : CreateWorkspace
        object Complete : CreateWorkspace
    }

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

    private fun queries(argument: Array<out String>): String = argument.mapIndexed { index, arg ->
        val prefix = if (index == 0) "?"
        else "&"

        "$prefix$arg={$arg}"
    }.joinToString("")

    companion object {
        //todo 다이나믹링크 스펙 바뀌면 변경하기...
        private const val SCHEME = "https://ronmee.page.link"
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MeeronNavigator(startDestination: Navigate) {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination.route()
//        startDestination = Navigate.OnBoarding.route()
    ) {
        composable(route = Navigate.Login.route()) {
            LoginScreen(
                goToHome = {
                    navController.popBackStack()
                    navController.navigate(Navigate.BottomNavi.Home.route())
                },
                goToSignIn = {
                    navController.popBackStack()
                    navController.navigate(Navigate.SignIn.TOS.route())
                },
                showOnBoarding = {
                    navController.popBackStack()
                    navController.navigate(Navigate.OnBoarding.route())
                }
            )
        }

        composable(route = Navigate.SignIn.TOS.route()) {
            TOSScreen(
                onNext = { navController.navigate(Navigate.SignIn.NameInit.route()) }
            )
        }

        composable(route = Navigate.SignIn.NameInit.route()) {
            NameInitScreen(onNext = { navController.navigate(Navigate.CreateWorkspace.CreateOrJoin.route()) })
        }

        composable(route = Navigate.CreateWorkspace.CreateOrJoin.route()) {
            CreateOrJoinScreen(
                onCreate = { navController.navigate(Navigate.CreateWorkspace.Name.route()) },
                onJoin = { navController.navigate(Navigate.CreateWorkspace.Join.route()) }
            )
        }

        composable(route = Navigate.CreateWorkspace.Join.route()) {
            JoinScreen(close = { navController.navigateUp() })
        }

        composable(route = Navigate.CreateWorkspace.Name.route()) {
            CreateWorkSpaceNameScreen(
                onPrevious = { navController.navigateUp() },
                onNext = { navController.navigate(Navigate.CreateWorkspace.Profile.route(it)) }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Profile.destination(Const.WorkspaceName),
            arguments = listOf(navArgument(Const.WorkspaceName) { type = NavType.StringType })
        ) {
            CreateWorkspaceProfileScreen(
                onNext = { navController.navigate(Navigate.CreateWorkspace.Team.route(it.encodeJson())) }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Team.destination(Const.WorkSpace),
            arguments = listOf(element = navArgument(Const.WorkSpace) { type = WorkSpaceType() })
        ) {
            CreateTeamScreen(
                onPrevious = { navController.navigateUp() },
                onNext = { workspaceId -> navController.navigate(Navigate.CreateWorkspace.Complete.route(workspaceId)) }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Complete.destination(Const.WorkspaceId),
            arguments = listOf(navArgument(Const.WorkspaceId) { type = NavType.LongType })
        ) {
            CreateCompleteScreen(
                onComplete = {
                    navController.navigate(Navigate.BottomNavi.Home.route())
                },
                showOnBoarding = {
                    navController.navigate(Navigate.OnBoarding.route())
                }
            )
        }

        composable(route = Navigate.OnBoarding.route()) {
            OnBoardingScreen(goToHome = { navController.navigate(Navigate.BottomNavi.Home.route()) })
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
                addMeeting = { navController.navigate(Navigate.CreateMeeting.Date.route()) },
                createWorkspace = { navController.navigate(Navigate.CreateWorkspace.CreateOrJoin.route()) }
            )
        }

        composable(
            route = Navigate.InviteDynamicLink.route(),
            deepLinks = listOf(navDeepLink {
                //"https://ronmee.page.link?id={id}"
                uriPattern = Navigate.InviteDynamicLink.deepLink("id")
            })
        ) {
            DynamicLinkEntryScreen()
        }


        composable(
            route = Navigate.BottomNavi.Team.route(),
        ) {

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
                showAll = { date ->
                    navController.navigate(Navigate.ShowAll.route(date.encodeJson()))
                }
            )
        }

        composable(
            route = Navigate.ShowAll.destination(Const.Date),
            arguments = listOf(element = navArgument(Const.Date) { type = DateType() })
        ) {
            ShowAllScreen(onAction = { navController.navigateUp() })
        }

        composable(Navigate.CreateMeeting.Date.route()) {
            CreateMeetingDateScreen(
                onAction = { navController.navigateUp() },
                onNext = { meeting -> navController.navigate(Navigate.CreateMeeting.Time.route(meeting.encodeJson())) }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Time.destination(Const.Meeting),
            arguments = listOf(element = navArgument(Const.Meeting) { type = MeetingType() })
        ) {
            CreateMeetingTimeScreen(
                onAction = { navController.popBackStack(route = Navigate.BottomNavi.Home.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() },
                onNext = { meeting -> navController.navigate(Navigate.CreateMeeting.Information.route(meeting.encodeJson())) }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Information.destination(Const.Meeting),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType() }
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
