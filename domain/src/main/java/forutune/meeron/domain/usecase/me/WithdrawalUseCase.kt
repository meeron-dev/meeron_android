package forutune.meeron.domain.usecase.me

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.repository.UserRepository
import forutune.meeron.domain.usecase.ClearDataUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WithdrawalUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val clearData: ClearDataUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(kakaoLogout: suspend () -> Unit) {
        withContext(ioDispatcher) {
            userRepository.withdrawal()
            userRepository.setUserId(null)
            clearData.invoke()
            kakaoLogout()
        }
    }
}