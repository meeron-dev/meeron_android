package fourtune.meeron.presentation.navigator

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
import fourtune.meeron.presentation.navigator.ext.encodeJson
import fourtune.meeron.presentation.navigator.type.DateType
import fourtune.meeron.presentation.navigator.type.MeetingType
import fourtune.meeron.presentation.navigator.type.TeamType
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
import fourtune.meeron.presentation.ui.home.MainScreen
import fourtune.meeron.presentation.ui.login.LoginScreen
import fourtune.meeron.presentation.ui.team.AddTeamScreen
import fourtune.meeron.presentation.ui.team.AdministerTeamScreen
import fourtune.meeron.presentation.ui.team.TeamMemberPickerScreen
import fourtune.meeron.presentation.ui.team.TeamMemberPickerViewModel

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

    object Main : Navigate

    sealed interface CreateMeeting : Navigate {
        object Date : CreateMeeting
        object Time : CreateMeeting
        object Information : CreateMeeting
        object Agenda : CreateMeeting
        object Participants : CreateMeeting
        object Complete : CreateMeeting
    }

    sealed interface Team : Navigate {
        object Administer : Team
        object Add : Team
        object TeamMemberPicker : Team
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
//        startDestination = Navigate.CreateWorkspace.CreateOrJoin.route()
    ) {
        composable(route = Navigate.Login.route()) {
            LoginScreen(
                goToHome = {
                    navController.popBackStack()
                    navController.navigate(Navigate.Main.route())
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
            arguments = listOf(
                navArgument(Const.WorkspaceName) {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            CreateWorkspaceProfileScreen(
                onNext = { workspace ->
                    navController.navigate(Navigate.CreateWorkspace.Team.route(workspace.encodeJson()))
                }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Team.destination(Const.WorkSpace),
            arguments = listOf(navArgument(Const.WorkSpace) { type = WorkSpaceType() })
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
                    navController.navigate(Navigate.Main.route()) {
                        popUpTo(Navigate.Login.route()) { inclusive = true }
                    }
                },
                showOnBoarding = {
                    navController.navigate(Navigate.OnBoarding.route()) {
                        popUpTo(Navigate.Login.route()) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Navigate.OnBoarding.route()) {
            OnBoardingScreen(goToHome = {
                navController.navigate(Navigate.Main.route()) {
                    popUpTo(Navigate.Login.route()) { inclusive = true }
                }
            })
        }

        composable(route = Navigate.Main.route()) {
            MainScreen(
                openCalendar = { navController.navigate(Navigate.Calendar.route()) },
                addMeeting = { navController.navigate(Navigate.CreateMeeting.Date.route()) },
                createWorkspace = { navController.navigate(Navigate.CreateWorkspace.CreateOrJoin.route()) },
                administerTeam = { navController.navigate(route = Navigate.Team.Administer.route(it.team.encodeJson())) },
                goToAddTeamMember = { navController.navigate(Navigate.Team.Add.route()) }
            )
        }

        composable(
            route = Navigate.Team.Administer.destination(Const.Team),
            arguments = listOf(navArgument(Const.Team) { type = TeamType() })
        ) {
            AdministerTeamScreen(
                onBack = { navController.navigateUp() },
                goToAddTeamMember = {navController.navigate(Navigate.Team.TeamMemberPicker.route(TeamMemberPickerViewModel.Type.Administer))}
            )
        }

        composable(
            route = Navigate.Team.Add.route()
        ) {
            AddTeamScreen(
                onAction = { navController.navigateUp() },
                openTeamPicker = { navController.navigate(Navigate.Team.TeamMemberPicker.route(TeamMemberPickerViewModel.Type.Add)) }
            )
        }

        composable(
            route = Navigate.Team.TeamMemberPicker.destination(Const.TeamMemberPickerType),
            arguments = listOf(navArgument(Const.TeamMemberPickerType) {
                type = NavType.EnumType(TeamMemberPickerViewModel.Type::class.java)
            })
        ) {
            TeamMemberPickerScreen(onAction = {})
        }

        composable(
            route = Navigate.InviteDynamicLink.route(),
            deepLinks = listOf(navDeepLink {
                //"https://ronmee.page.link?id={id}"
                uriPattern = Navigate.InviteDynamicLink.deepLink("id")
            })
        ) {
            DynamicLinkEntryScreen(
                goToTOS = {
                    navController.navigate(Navigate.SignIn.TOS.route()) {
                        popUpTo(Navigate.Main.route()) { inclusive = true }
                    }
                },
                goToLogin = {
                    navController.navigate(Navigate.Login.route()) {
                        popUpTo(Navigate.Main.route()) { inclusive = true }
                    }
                },
                goToHome = {
                    navController.navigate(Navigate.Main.route()) {
                        popUpTo(Navigate.Main.route()) { inclusive = true }
                    }
                }
            )
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
                onAction = { navController.popBackStack(route = Navigate.Main.route(), inclusive = false) },
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
                onLoad = { navController.navigate(Navigate.Calendar.route()) },
                onAction = {
                    navController.popBackStack(Navigate.Main.route(), inclusive = false)
                }
            )
        }

        composable(
            route = Navigate.CreateMeeting.Agenda.destination(Const.Meeting),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType() },
            )
        ) {
            CreateAgendaScreen(
                onAction = { navController.popBackStack(route = Navigate.Main.route(), inclusive = false) },
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
                onAction = { navController.popBackStack(route = Navigate.Main.route(), inclusive = false) },
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
                onAction = { navController.popBackStack(route = Navigate.Main.route(), inclusive = false) },
                onNext = { navController.popBackStack(route = Navigate.Main.route(), inclusive = false) },
                onPrevious = { navController.navigateUp() }
            )
        }
    }
}
