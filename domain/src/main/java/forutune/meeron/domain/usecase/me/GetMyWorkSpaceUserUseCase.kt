package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.model.WorkspaceUser
import javax.inject.Inject

class GetMyWorkSpaceUserUseCase @Inject constructor(

) {
    suspend operator fun invoke(): WorkspaceUser {
        return WorkspaceUser(workspaceUserId = 3, workspaceId = 1, nickname = "제로", position = "Android")
    }
}