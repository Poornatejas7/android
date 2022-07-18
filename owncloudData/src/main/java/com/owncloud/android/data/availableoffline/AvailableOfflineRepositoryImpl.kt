/**
 * ownCloud Android client application
 *
 * @author Abel García de Prada
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
package com.owncloud.android.data.availableoffline

import com.owncloud.android.domain.availableoffline.AvailableOfflineRepository
import com.owncloud.android.domain.availableoffline.model.AvailableOfflineStatus
import com.owncloud.android.domain.files.FileRepository
import com.owncloud.android.domain.files.model.OCFile

class AvailableOfflineRepositoryImpl(
    val fileRepository: FileRepository,
) : AvailableOfflineRepository {
    override fun setFileAsAvailableOffline(fileToSetAsAvailableOffline: OCFile) {
        fileRepository.updateFileWithNewAvailableOfflineStatus(fileToSetAsAvailableOffline, AvailableOfflineStatus.AVAILABLE_OFFLINE)
    }

    override fun unsetFileAsAvailableOffline(fileToSetAsAvailableOffline: OCFile) {
        fileRepository.updateFileWithNewAvailableOfflineStatus(fileToSetAsAvailableOffline, AvailableOfflineStatus.NOT_AVAILABLE_OFFLINE)
    }

}