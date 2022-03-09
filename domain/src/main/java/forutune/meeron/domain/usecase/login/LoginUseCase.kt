package forutune.meeron.domain.usecase.login

import forutune.meeron.domain.di.IoDispatcher
import forutune.meeron.domain.model.LoginUser
import forutune.meeron.domain.repository.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        getMe: suspend () -> LoginUser
    ) = withContext(dispatcher) {
        val me = getMe()
        return@withContext loginRepository.login(me)
    }


    suspend operator fun invoke(
        isKakaoLoginAvailable: () -> Boolean,
        kakaoLogin: suspend () -> Unit,
        kakaoLoginWithAccount: suspend () -> Unit,
        getMe: suspend () -> LoginUser
    ) = withContext(dispatcher) {
        if (isKakaoLoginAvailable()) {
            kakaoLogin()
        } else {
            kakaoLoginWithAccount()
        }
        invoke(getMe)
    }
}