package fourtune.meeron.presentation.di

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import fourtune.meeron.presentation.navigator.type.*

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TypeInjector {
    fun inject(workspaceUserType: WorkspaceUserType)
    fun inject(dateType: DateType)
    fun inject(meetingType: MeetingType)
    fun inject(teamType: TeamType)
    fun inject(workSpaceType: WorkSpaceType)

    companion object {
        operator fun invoke(context: Context) =
            EntryPointAccessors.fromApplication(context, TypeInjector::class.java)
    }
}
