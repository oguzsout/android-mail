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

package ch.protonmail.android.maildetail.domain.usecase

import arrow.core.Either
import arrow.core.Nel
import arrow.core.continuations.either
import ch.protonmail.android.mailcommon.domain.mapper.mapToEither
import ch.protonmail.android.mailcommon.domain.model.ConversationId
import ch.protonmail.android.mailcommon.domain.model.DataError
import ch.protonmail.android.maildetail.domain.model.MessageWithLabels
import ch.protonmail.android.mailmessage.domain.entity.Message
import ch.protonmail.android.mailmessage.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import me.proton.core.domain.entity.UserId
import me.proton.core.label.domain.entity.LabelType
import me.proton.core.label.domain.repository.LabelRepository
import javax.inject.Inject

class ObserveConversationMessagesWithLabels @Inject constructor(
    private val labelRepository: LabelRepository,
    private val messageRepository: MessageRepository
) {

    operator fun invoke(
        userId: UserId,
        conversationId: ConversationId
    ): Flow<Either<DataError, Nel<MessageWithLabels>>> =
        combine(
            labelRepository.observeLabels(userId, type = LabelType.MessageLabel).mapToEither(),
            labelRepository.observeLabels(userId, type = LabelType.MessageFolder).mapToEither(),
            messageRepository.observeCachedMessages(userId, conversationId).ignoreLocalErrors()
        ) { labelsEither, foldersEither, messagesEither ->
            either {
                val labels = labelsEither.bind()
                val folders = foldersEither.bind()
                val messages = messagesEither.bind()
                messages.map { message ->
                    MessageWithLabels(
                        message = message,
                        labels = labels + folders
                    )
                }
            }
        }

    private fun Flow<Either<DataError, Nel<Message>>>.ignoreLocalErrors(): Flow<Either<DataError, Nel<Message>>> =
        filter { either ->
            either.fold(
                ifLeft = { error -> error !is DataError.Local },
                ifRight = { true }
            )
        }
}
