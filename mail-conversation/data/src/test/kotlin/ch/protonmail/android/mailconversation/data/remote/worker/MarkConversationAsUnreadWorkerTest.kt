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

package ch.protonmail.android.mailconversation.data.remote.worker

import java.net.UnknownHostException
import androidx.work.ListenableWorker.Result
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import arrow.core.right
import ch.protonmail.android.mailcommon.data.worker.Enqueuer
import ch.protonmail.android.mailcommon.domain.model.ConversationId
import ch.protonmail.android.mailcommon.domain.sample.ConversationIdSample
import ch.protonmail.android.mailcommon.domain.sample.UserIdSample
import ch.protonmail.android.mailconversation.data.remote.ConversationApi
import ch.protonmail.android.mailconversation.data.remote.resource.MarkConversationAsUnreadBody
import ch.protonmail.android.mailconversation.domain.repository.ConversationLocalDataSource
import ch.protonmail.android.mailconversation.domain.sample.ConversationSample
import ch.protonmail.android.maillabel.domain.model.MailLabelId
import ch.protonmail.android.mailmessage.data.remote.response.MarkUnreadResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import me.proton.core.network.data.ApiProvider
import me.proton.core.network.domain.ApiResult
import org.junit.Test
import kotlin.test.assertEquals

internal class MarkConversationAsUnreadWorkerTest {

    private val contextLabelId = MailLabelId.System.Archive.labelId

    private val userId = UserIdSample.Primary
    private val conversationIds = listOf(ConversationIdSample.WeatherForecast)
    private val nonRetryableException = SerializationException()
    private val retryableException = UnknownHostException()

    private val apiProvider: ApiProvider = mockk {
        coEvery { get<ConversationApi>(userId).invoke<MarkUnreadResponse>(block = any()) } coAnswers {
            val block = firstArg<suspend ConversationApi.() -> MarkUnreadResponse>()
            try {
                ApiResult.Success(block(conversationApi))
            } catch (e: Exception) {
                when (e) {
                    nonRetryableException -> ApiResult.Error.Parse(e)
                    retryableException -> ApiResult.Error.Connection()
                    else -> throw e
                }
            }
        }
    }
    private val conversationApi: ConversationApi = mockk()
    private val conversationLocalDataSource: ConversationLocalDataSource = mockk {
        coEvery {
            markRead(userId, conversationIds, contextLabelId)
        } returns listOf(ConversationSample.WeatherForecast).right()
    }
    private val params: WorkerParameters = mockk {
        every { taskExecutor } returns mockk(relaxed = true)
        every { inputData.getString(MarkConversationAsUnreadWorker.RawUserIdKey) } returns userId.id
        every {
            inputData.getStringArray(MarkConversationAsUnreadWorker.RawConversationIdsKey)
        } returns arrayOf(ConversationIdSample.WeatherForecast.id)
        every { inputData.getString(MarkConversationAsUnreadWorker.RawContextLabelId) } returns contextLabelId.id
    }
    private val workManager: WorkManager = mockk {
        coEvery { enqueue(ofType<OneTimeWorkRequest>()) } returns mockk()
    }


    private val worker = MarkConversationAsUnreadWorker(
        context = mockk(),
        workerParameters = params,
        apiProvider = apiProvider,
        conversationLocalDataSource = conversationLocalDataSource
    )

    @Test
    fun `worker is enqueued with correct constraints`() {
        // given
        val expectedNetworkType = NetworkType.CONNECTED

        // when
        Enqueuer(workManager).enqueue<MarkConversationAsUnreadWorker>(
            MarkConversationAsUnreadWorker.params(
                userId,
                conversationIds,
                contextLabelId
            )
        )

        // then
        val requestSlot = slot<OneTimeWorkRequest>()
        verify { workManager.enqueue(capture(requestSlot)) }
        assertEquals(expectedNetworkType, requestSlot.captured.workSpec.constraints.requiredNetworkType)
    }

    @Test
    fun `worker is enqueued with correct parameters`() {
        // when
        Enqueuer(workManager).enqueue<MarkConversationAsUnreadWorker>(
            MarkConversationAsUnreadWorker.params(
                userId,
                conversationIds,
                contextLabelId
            )
        )

        // then
        val requestSlot = slot<OneTimeWorkRequest>()
        verify { workManager.enqueue(capture(requestSlot)) }
        assertEquals(
            expected = userId.id,
            actual = requestSlot.captured.workSpec.input.getString(MarkConversationAsUnreadWorker.RawUserIdKey)
        )
        assertEquals(
            expected = conversationIds,
            actual = requestSlot.captured.workSpec.input.getStringArray(
                MarkConversationAsUnreadWorker.RawConversationIdsKey
            )?.toList()?.map { ConversationId(it) }
        )
    }

    @Test
    fun `api is called with the correct parameters`() = runTest {
        // given
        coEvery { conversationApi.markAsUnread(any()) } returns mockk()

        // when
        worker.doWork()

        // then
        coVerify {
            conversationApi.markAsUnread(MarkConversationAsUnreadBody(conversationIds.map { it.id }, contextLabelId.id))
        }
    }

    @Test
    fun `returns success if api call succeeds`() = runTest {
        // given
        coEvery { conversationApi.markAsUnread(any()) } returns mockk()

        // when
        val result = worker.doWork()

        // then
        assertEquals(Result.success(), result)
    }

    @Test
    fun `returns retry if api call fails with retryable error`() = runTest {
        // given
        coEvery { conversationApi.markAsUnread(any()) } throws retryableException

        // when
        val result = worker.doWork()

        // then
        assertEquals(Result.retry(), result)
    }

    @Test
    fun `returns failure if api call fails with non-retryable error`() = runTest {
        // given
        coEvery { conversationApi.markAsUnread(any()) } throws nonRetryableException

        // when
        val result = worker.doWork()

        // then
        assertEquals(Result.failure(), result)
    }

    @Test
    fun `rollback unread when api call fails with non-retryable error`() = runTest {
        // given
        coEvery { conversationApi.markAsUnread(any()) } throws nonRetryableException

        // when
        worker.doWork()

        // then
        coVerify { conversationLocalDataSource.markRead(userId, conversationIds, contextLabelId) }
    }
}
