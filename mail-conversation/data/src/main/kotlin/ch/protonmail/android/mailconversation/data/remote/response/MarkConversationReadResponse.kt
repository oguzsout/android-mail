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

package ch.protonmail.android.mailconversation.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkConversationReadResponse(

    @SerialName("Code")
    val code: Int,

    @SerialName("Responses")
    val responses: List<MarkConversationReadResponseBody>,

    @SerialName("UndoToken")
    val undoToken: MarkConversationReadUndoToken
)

@Serializable
data class MarkConversationReadResponseBody(

    @SerialName("ID")
    val id: String,

    @SerialName("Response")
    val response: MarkConversationReadResponseCode
)

@Serializable
data class MarkConversationReadResponseCode(

    @SerialName("Code")
    val code: Int
)

@Serializable
data class MarkConversationReadUndoToken(

    @SerialName("Token")
    val token: String,

    @SerialName("ValidUntil")
    val validUntil: Int
)
