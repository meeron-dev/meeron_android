package forutune.meeron.domain.usecase

import forutune.meeron.domain.repository.AccountRepository
import javax.inject.Inject

class IsFirstVisitUserUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Boolean {
        val firstVisit = accountRepository.isFirstVisitor()
        if (!firstVisit) accountRepository.updateFirstVisitor()
        return firstVisit
    }
}