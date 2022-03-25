package forutune.meeron.domain.usecase.workspace

import forutune.meeron.domain.repository.WorkSpaceRepository
import forutune.meeron.domain.usecase.me.GetMeUseCase
import javax.inject.Inject

class GetLatestWorkspaceIdUseCase @Inject constructor(
    private val getMe: GetMeUseCase,
    private val workSpaceRepository: WorkSpaceRepository
) {
    suspend operator fun invoke():Long {//todo 지금은 가장 첫번째지만 나중에는 바꿔야 함.. (use datastore : account)
        val me = getMe()
        val workSpaceInfos = workSpaceRepository.getUserWorkspace(me.userId)
        return workSpaceInfos.first().workSpaceId
    }
}