/*
 * Copyright (c) 2022 Proton Technologies AG
 * This file is part of Proton Technologies AG and Proton Mail.
 *
 * Proton Mail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Mail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.protonmail.android.mailcommon.data.file

import java.io.File
import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import me.proton.core.domain.entity.UserId
import me.proton.core.util.kotlin.HashUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

class InternalFileStorageTest {

    private val contextMock = mockk<Context> {
        every { filesDir } returns File(InternalStoragePath)
        every { cacheDir } returns File(CachedStoragePath)
    }
    private val fileHelperMock = mockk<FileHelper>()
    private val internalFileStorage = InternalFileStorage(contextMock, fileHelperMock, Dispatchers.Unconfined)

    @Before
    fun setUp() {
        mockkObject(HashUtils)
        every { HashUtils.sha256(MessageId.Raw) } returns MessageId.EncodedDigest
        every { HashUtils.sha256(UserId.Raw) } returns UserId.EncodedDigest
    }

    @After
    fun tearDown() {
        unmockkObject(HashUtils)
    }

    @Test
    fun `should read from file using a correct sanitised folder and filename and return body on success`() = runTest {
        // Given
        coEvery {
            fileHelperMock.readFromFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns MessageBody

        // When
        val actualFileContent = internalFileStorage.readFromFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertEquals(MessageBody, actualFileContent)
    }

    @Test
    fun `should read from cached file using a correct sanitised folder and filename and return body on success`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.readFromFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest)
                )
            } returns MessageBody

            // When
            val actualFileContent = internalFileStorage.readFromCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
            )

            // Then
            assertEquals(MessageBody, actualFileContent)
        }

    @Test
    fun `should read from file using a correct sanitised folder and filename and return null on failure`() = runTest {
        // Given
        coEvery {
            fileHelperMock.readFromFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns null

        // When
        val actualFileContent = internalFileStorage.readFromFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertNull(actualFileContent)
    }

    @Test
    fun `should read from cached file using a correct sanitised folder and filename and return null on failure`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.readFromFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest)
                )
            } returns null

            // When
            val actualFileContent = internalFileStorage.readFromCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
            )

            // Then
            assertNull(actualFileContent)
        }

    @Test
    fun `should read file using a correct sanitised folder and filename and return file on success`() = runTest {
        // Given
        val fileMock = mockk<File>()
        coEvery {
            fileHelperMock.getFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns fileMock

        // When
        val actualFile = internalFileStorage.getFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertEquals(fileMock, actualFile)
    }

    @Test
    fun `should read cached file using a correct sanitised folder and filename and return file on success`() = runTest {
        // Given
        val fileMock = mockk<File>()
        coEvery {
            fileHelperMock.getFile(
                folder = FileHelper.Folder(CompleteCachedFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns fileMock

        // When
        val actualFile = internalFileStorage.getCachedFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertEquals(fileMock, actualFile)
    }

    @Test
    fun `should read file using a correct sanitised folder and filename and return null on failure`() = runTest {
        // Given
        coEvery {
            fileHelperMock.getFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns null

        // When
        val actualFile = internalFileStorage.getFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertNull(actualFile)
    }

    @Test
    fun `should read cached file using a correct sanitised folder and filename and return null on failure`() = runTest {
        // Given
        coEvery {
            fileHelperMock.getFile(
                folder = FileHelper.Folder(CompleteCachedFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns null

        // When
        val actualFile = internalFileStorage.getCachedFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertNull(actualFile)
    }

    @Test
    fun `should write to file using a correct sanitised folder and filename and return true on success`() = runTest {
        // Given
        coEvery {
            fileHelperMock.writeToFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest),
                content = MessageBody
            )
        } returns true

        // When
        val fileWritten = internalFileStorage.writeToFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
            content = MessageBody
        )

        // Then
        assertTrue(fileWritten)
    }

    @Test
    fun `should write to cached file using a correct sanitised folder and filename and return true on success`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.writeToFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest),
                    content = MessageBody
                )
            } returns true

            // When
            val fileWritten = internalFileStorage.writeToCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
                content = MessageBody
            )

            // Then
            assertTrue(fileWritten)
        }

    @Test
    fun `should write to file using a correct sanitised folder and filename and return false on failure`() = runTest {
        // Given
        coEvery {
            fileHelperMock.writeToFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest),
                content = MessageBody
            )
        } returns false

        // When
        val fileWritten = internalFileStorage.writeToFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
            content = MessageBody
        )

        // Then
        assertFalse(fileWritten)
    }

    @Test
    fun `should write to cached file using a correct sanitised folder and filename and return false on failure`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.writeToFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest),
                    content = MessageBody
                )
            } returns false

            // When
            val fileWritten = internalFileStorage.writeToCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
                content = MessageBody
            )

            // Then
            assertFalse(fileWritten)
        }

    @Test
    fun `should write file using a correct sanitised folder and filename and return file on success`() = runTest {
        // Given
        val fileMock = mockk<File>()
        val fileByteArray = MessageBody.toByteArray()
        coEvery {
            fileHelperMock.writeToFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest),
                content = fileByteArray
            )
        } returns fileMock

        // When
        val actualFile = internalFileStorage.writeFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
            content = fileByteArray
        )

        // Then
        assertEquals(fileMock, actualFile)
    }

    @Test
    fun `should write cached file using a correct sanitised folder and filename and return file on success`() =
        runTest {
            // Given
            val fileMock = mockk<File>()
            val fileByteArray = MessageBody.toByteArray()
            coEvery {
                fileHelperMock.writeToFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest),
                    content = fileByteArray
                )
            } returns fileMock

            // When
            val actualFile = internalFileStorage.writeCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
                content = fileByteArray
            )

            // Then
            assertEquals(fileMock, actualFile)
        }

    @Test
    fun `should write file using a correct sanitised folder and filename and return null on failure`() = runTest {
        // Given
        val fileByteArray = MessageBody.toByteArray()
        coEvery {
            fileHelperMock.writeToFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest),
                content = fileByteArray
            )
        } returns null

        // When
        val actualFile = internalFileStorage.writeFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
            content = fileByteArray
        )

        // Then
        assertNull(actualFile)
    }

    @Test
    fun `should write cached file using a correct sanitised folder and filename and return null on failure`() =
        runTest {
            // Given
            val fileByteArray = MessageBody.toByteArray()
            coEvery {
                fileHelperMock.writeToFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest),
                    content = fileByteArray
                )
            } returns null

            // When
            val actualFile = internalFileStorage.writeCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw),
                content = fileByteArray
            )

            // Then
            assertNull(actualFile)
        }

    @Test
    fun `should delete a file using a correct sanitised folder and filename and return true on success`() = runTest {
        // Given
        coEvery {
            fileHelperMock.deleteFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns true

        // When
        val fileDeleted = internalFileStorage.deleteFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertTrue(fileDeleted)
    }

    @Test
    fun `should delete a cached file using a correct sanitised folder and filename and return true on success`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.deleteFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest)
                )
            } returns true

            // When
            val fileDeleted = internalFileStorage.deleteCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
            )

            // Then
            assertTrue(fileDeleted)
        }

    @Test
    fun `should delete a file using a correct sanitised folder and filename and return false on failure`() = runTest {
        // Given
        coEvery {
            fileHelperMock.deleteFile(
                folder = FileHelper.Folder(CompleteFolderPath),
                filename = FileHelper.Filename(MessageId.EncodedDigest)
            )
        } returns false

        // When
        val fileDeleted = internalFileStorage.deleteFile(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies,
            fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
        )

        // Then
        assertFalse(fileDeleted)
    }

    @Test
    fun `should delete a cached file using a correct sanitised folder and filename and return false on failure`() =
        runTest {
            // Given
            coEvery {
                fileHelperMock.deleteFile(
                    folder = FileHelper.Folder(CompleteCachedFolderPath),
                    filename = FileHelper.Filename(MessageId.EncodedDigest)
                )
            } returns false

            // When
            val fileDeleted = internalFileStorage.deleteCachedFile(
                userId = UserId.Object,
                folder = InternalFileStorage.Folder.MessageBodies,
                fileIdentifier = InternalFileStorage.FileIdentifier(MessageId.Raw)
            )

            // Then
            assertFalse(fileDeleted)
        }

    @Test
    fun `should delete a folder using a correct sanitised folder name and return true on success`() = runTest {
        // Given
        coEvery { fileHelperMock.deleteFolder(FileHelper.Folder(CompleteFolderPath)) } returns true

        // When
        val fileDeleted = internalFileStorage.deleteFolder(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies
        )

        // Then
        assertTrue(fileDeleted)
    }

    @Test
    fun `should delete a cached folder using a correct sanitised folder name and return true on success`() = runTest {
        // Given
        coEvery { fileHelperMock.deleteFolder(FileHelper.Folder(CompleteCachedFolderPath)) } returns true

        // When
        val fileDeleted = internalFileStorage.deleteCachedFolder(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies
        )

        // Then
        assertTrue(fileDeleted)
    }

    @Test
    fun `should delete a folder using a correct sanitised folder name and return false on success`() = runTest {
        // Given
        coEvery { fileHelperMock.deleteFolder(FileHelper.Folder(CompleteFolderPath)) } returns false

        // When
        val fileDeleted = internalFileStorage.deleteFolder(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies
        )

        // Then
        assertFalse(fileDeleted)
    }

    @Test
    fun `should delete a cached folder using a correct sanitised folder name and return false on success`() = runTest {
        // Given
        coEvery { fileHelperMock.deleteFolder(FileHelper.Folder(CompleteCachedFolderPath)) } returns false

        // When
        val fileDeleted = internalFileStorage.deleteCachedFolder(
            userId = UserId.Object,
            folder = InternalFileStorage.Folder.MessageBodies
        )

        // Then
        assertFalse(fileDeleted)
    }

    private companion object TestData {

        object MessageId {

            const val Raw = "message_id"
            const val Digest = "message_id_digest"
            const val EncodedDigest = "message_id_encoded"
        }

        object UserId {

            const val Raw = "user_id"
            val Object = UserId(Raw)
            const val Digest = "user_id_digest"
            const val EncodedDigest = "user_id_encoded"
        }

        const val InternalStoragePath = "/some/path/to/internal/storage"
        const val CompleteFolderPath = "$InternalStoragePath/${UserId.EncodedDigest}/message_bodies/"

        const val CachedStoragePath = "/some/path/to/cache/storage"
        const val CompleteCachedFolderPath = "$CachedStoragePath/${UserId.EncodedDigest}/message_bodies/"

        const val MessageBody = "I am a message body"
    }
}
