/**
 * ownCloud Android client application
 *
 * @author Abel García de Prada
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
 */
package com.owncloud.android.domain.files.usecases

import com.owncloud.android.domain.exceptions.UnauthorizedException
import com.owncloud.android.domain.files.FileRepository
import com.owncloud.android.testutil.OC_FILE
import com.owncloud.android.testutil.OC_FOLDER
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoveFileUseCaseTest {
    private val repository: FileRepository = spyk()
    private val setLastUsageFileUseCase: SetLastUsageFileUseCase = mockk(relaxed = true)
    private val useCase = RemoveFileUseCase(repository, setLastUsageFileUseCase)
    private val useCaseParams = RemoveFileUseCase.Params(listOf(OC_FILE, OC_FOLDER), removeOnlyLocalCopy = true)

    @Test
    fun `remove file - ok`() {
        every { repository.deleteFiles(any(), any()) } returns Unit

        val useCaseResult = useCase(useCaseParams.copy(removeOnlyLocalCopy = false))

        assertTrue(useCaseResult.isSuccess)
        assertEquals(Unit, useCaseResult.getDataOrNull())

        verify(exactly = 1) { repository.deleteFiles(any(), removeOnlyLocalCopy = false) }
    }

    @Test
    fun `remove file - ok - remove local only`() {
        every { repository.deleteFiles(any(), any()) } returns Unit

        val useCaseResult = useCase(useCaseParams)

        assertTrue(useCaseResult.isSuccess)
        assertEquals(Unit, useCaseResult.getDataOrNull())

        verify(exactly = 1) { repository.deleteFiles(any(), removeOnlyLocalCopy = true) }
    }

    @Test
    fun `remove file - ko - empty list`() {
        val useCaseResult = useCase(useCaseParams.copy(listOfFilesToDelete = listOf()))

        assertTrue(useCaseResult.isError)
        assertTrue(useCaseResult.getThrowableOrNull() is IllegalArgumentException)

        verify(exactly = 0) { repository.deleteFiles(any(), removeOnlyLocalCopy = true) }
    }

    @Test
    fun `remove file - ko - other exception`() {
        every { repository.deleteFiles(any(), any()) } throws UnauthorizedException()

        val useCaseResult = useCase(useCaseParams)

        assertTrue(useCaseResult.isError)
        assertTrue(useCaseResult.getThrowableOrNull() is UnauthorizedException)

        verify(exactly = 1) { repository.deleteFiles(any(), any()) }
    }
}
