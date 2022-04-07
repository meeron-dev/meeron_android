package fourtune.meeron.presentation.navigator

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import forutune.meeron.domain.Const
import forutune.meeron.domain.model.EntryPointType
import fourtune.meeron.presentation.navigator.ext.encodeJson
import fourtune.meeron.presentation.navigator.type.*
import fourtune.meeron.presentation.ui.DynamicLinkEntryScreen
import fourtune.meeron.presentation.ui.calendar.CalendarScreen
import fourtune.meeron.presentation.ui.calendar.all.ShowAllScreen
import fourtune.meeron.presentation.ui.createmeeting.agenda.CreateAgendaScreen
import fourtune.meeron.presentation.ui.createmeeting.complete.CompleteMeetingScreen
import fourtune.meeron.presentation.ui.createmeeting.date.CreateMeetingDateScreen
import fourtune.meeron.presentation.ui.createmeeting.information.CreateMeetingInfoScreen
import fourtune.meeron.presentation.ui.createmeeting.participants.CreateMeetingParticipantsScreen
import fourtune.meeron.presentation.ui.createmeeting.time.CreateMeetingTimeScreen
import fourtune.meeron.presentation.ui.createworkspace.*
import fourtune.meeron.presentation.ui.detail.*
import fourtune.meeron.presentation.ui.home.MainScreen
import fourtune.meeron.presentation.ui.home.my.EditAccountScreen
import fourtune.meeron.presentation.ui.home.my.EditWorkspaceScreen
import fourtune.meeron.presentation.ui.home.my.InquiryOrHomepageScreen
import fourtune.meeron.presentation.ui.home.my.MyMeeronEvent
import fourtune.meeron.presentation.ui.home.team.add.AddTeamScreen
import fourtune.meeron.presentation.ui.home.team.admin.AdministerTeamScreen
import fourtune.meeron.presentation.ui.home.team.createcomplete.TeamCreateCompleteScreen
import fourtune.meeron.presentation.ui.home.team.picker.TeamMemberPickerScreen
import fourtune.meeron.presentation.ui.home.team.picker.TeamMemberPickerViewModel
import fourtune.meeron.presentation.ui.login.LoginScreen
import fourtune.meeron.presentation.ui.start.NameInitScreen
import fourtune.meeron.presentation.ui.start.OnBoardingScreen
import fourtune.meeron.presentation.ui.start.TOSScreen

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
        object TemCreateComplete : Team
    }

    sealed interface Detail : Navigate {
        object Meeting : Detail
        object Agenda : Detail
        object ParticipantState : Detail
        object Team : Detail
        object WorkspaceUser : Detail
    }

    sealed interface MyMeeron : Navigate {
        object EditWorkspace : MyMeeron
        object EditAccount : MyMeeron
        object InquiryOrHomepage : MyMeeron
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
fun MeeronNavigator(startDestination: String) {
    val navController = rememberAnimatedNavController()
    val context = LocalContext.current

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Navigate.Login.destination(Const.EntryPointType),
            arguments = listOf(
                navArgument(Const.EntryPointType) {
                    type = NavType.EnumType(EntryPointType::class.java)
                }
            )
        ) {
            LoginScreen(
                goToHome = {
                    navController.popBackStack()
                    navController.navigate(Navigate.Main.route())
                },
                goToSignIn = {
                    navController.popBackStack()
                    navController.navigate(Navigate.SignIn.TOS.route(it))
                },
                showOnBoarding = {
                    navController.popBackStack()
                    navController.navigate(Navigate.OnBoarding.route())
                }
            )
        }

        composable(
            route = Navigate.SignIn.TOS.destination(Const.EntryPointType),
            arguments = listOf(
                navArgument(Const.EntryPointType) {
                    type = NavType.EnumType(EntryPointType::class.java)
                }
            )
        ) {
            TOSScreen(
                onNext = { navController.navigate(Navigate.SignIn.NameInit.route(it)) }
            )
        }

        composable(
            route = Navigate.SignIn.NameInit.destination(Const.EntryPointType),
            arguments = listOf(
                navArgument(Const.EntryPointType) {
                    type = NavType.EnumType(EntryPointType::class.java)
                }
            )
        ) {
            NameInitScreen(
                goToCreateOrJoin = { navController.navigate(Navigate.CreateWorkspace.CreateOrJoin.route()) },
                goToCreateWorkspaceProfile = {
                    navController.navigate(
                        Navigate.CreateWorkspace.Profile.route(
                            "dynamic",
                            EntryPointType.DynamicLink
                        )
                    )
                }
            )
        }

        composable(route = Navigate.CreateWorkspace.CreateOrJoin.destination()) {
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
                onNext = { navController.navigate(Navigate.CreateWorkspace.Profile.route(it, EntryPointType.Normal)) }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Profile.destination(Const.WorkspaceName, Const.EntryPointType),
            arguments = listOf(
                navArgument(Const.WorkspaceName) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(Const.EntryPointType) {
                    type = NavType.EnumType(EntryPointType::class.java)
                }
            )
        ) {
            CreateWorkspaceProfileScreen(
                goToCreateTeam = { workspace ->
                    navController.navigate(Navigate.CreateWorkspace.Team.route(workspace.encodeJson()))
                },
                goToHome = {
                    navController.navigate(Navigate.Main.route()) {
                        popUpTo(Navigate.Login.route())
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Navigate.CreateWorkspace.Team.destination(Const.WorkSpace),
            arguments = listOf(navArgument(Const.WorkSpace) { type = WorkSpaceType(context) })
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
                navController.popBackStack()
                navController.navigate(Navigate.Main.route())
            })
        }

        composable(route = Navigate.Main.route()) {
            MainScreen(
                openCalendar = { navController.navigate(Navigate.Calendar.route()) },
                addMeeting = { navController.navigate(Navigate.CreateMeeting.Date.route()) },
                goToAddTeamMember = { navController.navigate(Navigate.Team.Add.route()) },
                administerTeam = { navController.navigate(route = Navigate.Team.Administer.route(it.team.encodeJson())) },
                goToMeetingDetail = { navController.navigate(Navigate.Detail.Meeting.route(it.encodeJson())) }
            ) { event ->
                when (event) {
                    MyMeeronEvent.EditAccount -> navController.navigate(Navigate.MyMeeron.EditAccount.route())
                    MyMeeronEvent.EditProfile -> {
                        navController.navigate(Navigate.CreateWorkspace.Profile.route("edit", EntryPointType.Edit))
                    }
                    MyMeeronEvent.EditWorkspace -> navController.navigate(Navigate.MyMeeron.EditWorkspace.route())
                    MyMeeronEvent.InquiryOrHomepage -> navController.navigate(Navigate.MyMeeron.InquiryOrHomepage.route())
                }
            }
        }

        composable(
            route = Navigate.Detail.Meeting.destination(Const.Meeting),
            arguments = listOf(navArgument(Const.Meeting) { type = MeetingType(context) })
        ) {
            MeetingDetailScreen(
                goToAgendaDetail = { navController.navigate(Navigate.Detail.Agenda.route(it.encodeJson())) },
                goToParticipantState = { navController.navigate(Navigate.Detail.ParticipantState.route(it.encodeJson())) },
                goToTeamDetail = { meeting, teamId ->
                    navController.navigate(Navigate.Detail.Team.route(meeting.encodeJson(), teamId))
                },
                onBack = { navController.navigateUp() },
            )
        }

        composable(
            route = Navigate.Detail.ParticipantState.destination(Const.Meeting),
            arguments = listOf(navArgument(Const.Meeting) { type = MeetingType(context) })
        ) {
            ParticipantStateScreen(onAction = { navController.popBackStack() })
        }

        composable(
            route = Navigate.Detail.Agenda.destination(Const.Meeting),
            arguments = listOf(navArgument(Const.Meeting) { type = MeetingType(context) })
        ) {
            AgendaDetailScreen(onBack = { navController.navigateUp() })
        }

        composable(
            route = Navigate.Detail.Team.destination(Const.Meeting, Const.TeamId),
            arguments = listOf(
                navArgument(Const.Meeting) { type = MeetingType(context) },
                navArgument(Const.TeamId) { type = NavType.LongType }
            )
        ) {
            TeamDetailScreen(
                onBack = { navController.navigateUp() },
                onClickWorkspaceUser = { workspaceUser, team ->
                    navController.navigate(
                        Navigate.Detail.WorkspaceUser.route(
                            workspaceUser.encodeJson(),
                            team.encodeJson()
                        )
                    )
                }
            )
        }

        composable(
            route = Navigate.Detail.WorkspaceUser.destination(Const.WorkspaceUser, Const.Team),
            arguments = listOf(
                navArgument(Const.WorkspaceUser) { type = WorkspaceUserType(context) },
                navArgument(Const.Team) { type = TeamType(context) }
            )
        ) {
            WorkspaceUserDetailScreen(onAction = { navController.popBackStack() })
        }

        composable(
            route = Navigate.Team.Administer.destination(Const.Team),
            arguments = listOf(navArgument(Const.Team) { type = TeamType(context) })
        ) {
            AdministerTeamScreen(
                onBack = { navController.popBackStack() },
                goToAddTeamMember = { teamId ->
                    navController.navigate(
                        Navigate.Team.TeamMemberPicker.route(
                            TeamMemberPickerViewModel.Type.Administer,
                            teamId
                        )
                    )
                }
            )
        }

        composable(
            route = Navigate.Team.Add.route()
        ) {
            AddTeamScreen(
                onAction = { navController.navigateUp() },
                openTeamPicker = { teamId ->
                    navController.navigate(
                        Navigate.Team.TeamMemberPicker.route(TeamMemberPickerViewModel.Type.Add, teamId)
                    )
                }
            )
        }

        composable(
            route = Navigate.Team.TeamMemberPicker.destination(Const.TeamMemberPickerType, Const.TeamId),
            arguments = listOf(
                navArgument(Const.TeamMemberPickerType) {
                    type = NavType.EnumType(TeamMemberPickerViewModel.Type::class.java)
                },
                navArgument(Const.TeamId) {
                    type = NavType.LongType
                }
            )
        ) {
            TeamMemberPickerScreen(
                onBack = { navController.navigateUp() },
                goToMain = { navController.popBackStack(Navigate.Main.route(), false) },
                goToTeamCreateComplete = { navController.navigate(Navigate.Team.TemCreateComplete.route(it)) }
            )
        }

        composable(
            route = Navigate.Team.TemCreateComplete.destination(Const.TeamId),
            arguments = listOf(navArgument(Const.TeamId) { type = NavType.LongType })
        ) {
            TeamCreateCompleteScreen {
                navController.popBackStack(Navigate.Main.route(), false)
            }
        }

        composable(
            route = Navigate.MyMeeron.EditWorkspace.destination()
        ) {
            EditWorkspaceScreen(
                goToMyMeeron = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Navigate.MyMeeron.EditAccount.destination()
        ) {
            EditAccountScreen(
                goToMyMeeron = {
                    navController.navigateUp()
                },
                goToLogin = {
                    navController.navigate(Navigate.Login.route(EntryPointType.Normal)) {
                        popUpTo(Navigate.Main.route())
                    }
                }
            )
        }

        composable(Navigate.MyMeeron.InquiryOrHomepage.destination()) {
            InquiryOrHomepageScreen(onBack = { navController.navigateUp() })
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
                    navController.navigate(Navigate.Login.route(EntryPointType.DynamicLink)) {
                        popUpTo(Navigate.Main.route()) { inclusive = true }
                    }
                },
                goToCreateProfile = {
                    navController.navigate(
                        Navigate.CreateWorkspace.Profile.route(
                            "dynamic",
                            EntryPointType.DynamicLink
                        )
                    )
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
                },
                goToMeetingDetail = { meeting -> navController.navigate(Navigate.Detail.Meeting.route(meeting.encodeJson())) }
            )
        }

        composable(
            route = Navigate.ShowAll.destination(Const.Date),
            arguments = listOf(element = navArgument(Const.Date) { type = DateType(context) })
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
            arguments = listOf(element = navArgument(Const.Meeting) { type = MeetingType(context) })
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
                navArgument(Const.Meeting) { type = MeetingType(context) }
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
                navArgument(Const.Meeting) { type = MeetingType(context) },
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
                navArgument(Const.Meeting) { type = MeetingType(context) },
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
                navArgument(Const.Meeting) { type = MeetingType(context) },
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
