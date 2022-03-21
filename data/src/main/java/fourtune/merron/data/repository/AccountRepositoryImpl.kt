package fourtune.merron.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import forutune.meeron.domain.repository.AccountRepository
import fourtune.merron.data.source.local.preference.DataStoreKeys
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : AccountRepository {
    override suspend fun isFirstVisitor(): Boolean {
        val isFirstVisitor = dataStore.data.map { it[DataStoreKeys.Account.isFirstVisitor] }.firstOrNull() ?: false
        if (!isFirstVisitor) dataStore.edit { it[DataStoreKeys.Account.isFirstVisitor] = true }
        return isFirstVisitor
    }
}