/**
 * ownCloud Android client application
 *
 * @author David González Verdugo
 * @author Abel García de Prada
 * @author Juan Carlos Garrote Gascón
 *
 * Copyright (C) 2022 ownCloud GmbH.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.data.capabilities.repository

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import com.owncloud.android.data.capabilities.datasources.LocalCapabilitiesDataSource
import com.owncloud.android.data.capabilities.datasources.RemoteCapabilitiesDataSource
import com.owncloud.android.domain.capabilities.CapabilityRepository
import com.owncloud.android.domain.capabilities.model.OCCapability
import com.owncloud.android.lib.common.accounts.AccountUtils
import com.owncloud.android.lib.resources.status.OwnCloudVersion

class OCCapabilityRepository(
    private val localCapabilitiesDataSource: LocalCapabilitiesDataSource,
    private val remoteCapabilitiesDataSource: RemoteCapabilitiesDataSource,
    private val accountManager: AccountManager,
) : CapabilityRepository {

    override fun getCapabilitiesAsLiveData(accountName: String): LiveData<OCCapability?> {
        return localCapabilitiesDataSource.getCapabilitiesForAccountAsLiveData(accountName)
    }

    override fun getStoredCapabilities(
        accountName: String
    ): OCCapability? = localCapabilitiesDataSource.getCapabilityForAccount(accountName)

    override fun refreshCapabilitiesForAccount(
        accountName: String
    ) {
        val capabilitiesFromNetwork = remoteCapabilitiesDataSource.getCapabilities(accountName)
        localCapabilitiesDataSource.insert(listOf(capabilitiesFromNetwork))
    }
}
