/**
 * ownCloud Android client application
 *
 * @author Abel García de Prada
 * @author Christian Schabesberger
 * Copyright (C) 2020 ownCloud GmbH.
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

package com.owncloud.android.data.files.repository

import com.owncloud.android.data.files.datasources.LocalFileDataSource
import com.owncloud.android.data.files.datasources.RemoteFileDataSource
import com.owncloud.android.domain.files.FileRepository
import com.owncloud.android.domain.files.model.MIME_DIR
import com.owncloud.android.domain.files.model.OCFile

class OCFileRepository(
    private val localFileDataSource: LocalFileDataSource,
    private val remoteFileDataSource: RemoteFileDataSource
) : FileRepository {
    override fun checkPathExistence(path: String, userLogged: Boolean): Boolean =
        remoteFileDataSource.checkPathExistence(path, userLogged)

    override fun getUrlToOpenInWeb(openWebEndpoint: String, fileId: String): String =
        remoteFileDataSource.getUrlToOpenInWeb(openWebEndpoint = openWebEndpoint, fileId = fileId)

    override fun createFolder(
        remotePath: String,
        parentFolder: OCFile
    ) {
        remoteFileDataSource.createFolder(
            remotePath = remotePath,
            createFullPath = false,
            isChunksFolder = false
        ).also {
            localFileDataSource.saveFilesInFolder(
                folder = parentFolder,
                listOfFiles = listOf(
                    OCFile(
                        remotePath = remotePath,
                        owner = parentFolder.owner,
                        modificationTimestamp = System.currentTimeMillis(),
                        length = 0,
                        mimeType = MIME_DIR
                    )
                )
            )
        }
    }

    override fun getFileById(fileId: Long): OCFile? =
        localFileDataSource.getFileById(fileId)

    override fun getFileByRemotePath(remotePath: String, owner: String): OCFile? =
        localFileDataSource.getFileByRemotePath(remotePath, owner)

    override fun getFolderContent(folderId: Long): List<OCFile> =
        localFileDataSource.getFolderContent(folderId)

    override fun getFolderImages(folderId: Long): List<OCFile> =
        localFileDataSource.getFolderImages(folderId)

    override fun refreshFolder(remotePath: String) {
        remoteFileDataSource.refreshFolder(remotePath).also {
            localFileDataSource.saveFilesInFolder(
                folder = it.first(),
                listOfFiles = it.drop(1)
            )
        }
    }

    override fun saveFile(file: OCFile) {
        localFileDataSource.saveFile(file)
    }
}
