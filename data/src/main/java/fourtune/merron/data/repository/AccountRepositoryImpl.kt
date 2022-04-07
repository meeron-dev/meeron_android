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
        return dataStore.data.map { it[DataStoreKeys.Account.isFirstVisitor] }.firstOrNull() ?: true
    }

    override suspend fun updateFirstVisitor() {
        dataStore.edit { it[DataStoreKeys.Account.isFirstVisitor] = false }
    }
}