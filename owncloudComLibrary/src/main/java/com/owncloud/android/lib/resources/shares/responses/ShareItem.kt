/*
 * ownCloud Android client application
 *
 * @author Fernando Sanz Velasco
 * Copyright (C) 2021 ownCloud GmbH.
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
 *
 *
 *
 *
 */

package com.owncloud.android.lib.resources.shares.responses

import com.owncloud.android.lib.common.network.WebdavUtils
import com.owncloud.android.lib.resources.shares.RemoteShare
import com.owncloud.android.lib.resources.shares.RemoteShare.Companion.DEFAULT_PERMISSION
import com.owncloud.android.lib.resources.shares.RemoteShare.Companion.INIT_EXPIRATION_DATE_IN_MILLIS
import com.owncloud.android.lib.resources.shares.RemoteShare.Companion.INIT_SHARED_DATE
import com.owncloud.android.lib.resources.shares.ShareType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.File

@JsonClass(generateAdapter = true)
data class ShareItem(
    val id: String? = null,

    @Json(name = "share_with")
    val shareWith: String? = null,

    val path: String? = null,
    val token: String? = null,

    @Json(name = "item_type")
    val itemType: String? = null,

    @Json(name = "share_with_displayname")
    val sharedWithDisplayName: String? = null,

    @Json(name = "share_with_additional_info")
    val sharedWithAdditionalInfo: String? = null,

    val name: String? = null,

    @Json(name = "url")
    val shareLink: String? = null,

    @Json(name = "share_type")
    val shareType: Int? = null,

    val permissions: Int? = null,

    @Json(name = "stime")
    val sharedDate: Long? = null,

    @Json(name = "expiration")
    val expirationDate: String? = null,
) {
    fun toRemoteShare() = RemoteShare(
        id = id ?: "0",
        shareWith = shareWith.orEmpty(),
        path = if (itemType == ItemType.FOLDER.fileValue) path.plus(File.separator) else path.orEmpty(),
        token = token.orEmpty(),
        itemType = itemType.orEmpty(),
        sharedWithDisplayName = sharedWithDisplayName.orEmpty(),
        sharedWithAdditionalInfo = sharedWithAdditionalInfo.orEmpty(),
        name = name.orEmpty(),
        shareLink = shareLink.orEmpty(),
        shareType = ShareType.values().firstOrNull { it.value == shareType } ?: ShareType.UNKNOWN,
        permissions = permissions ?: DEFAULT_PERMISSION,
        sharedDate = sharedDate ?: INIT_SHARED_DATE,
        expirationDate = expirationDate?.let {
            WebdavUtils.parseResponseDate(it)?.time
        } ?: INIT_EXPIRATION_DATE_IN_MILLIS,
        isFolder = itemType?.equals(ItemType.FOLDER.fileValue) ?: false
    )
}

enum class ItemType(val fileValue: String) { FILE("file"), FOLDER("folder") }
