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

package ch.protonmail.android.mailmessage.domain.usecase

import ch.protonmail.android.mailmessage.domain.entity.Recipient
import me.proton.core.contact.domain.entity.Contact
import me.proton.core.util.kotlin.EMPTY_STRING
import me.proton.core.util.kotlin.takeIfNotBlank
import javax.inject.Inject

class ResolveParticipantName @Inject constructor() {

    operator fun invoke(
        participant: Recipient,
        contacts: List<Contact>,
        fallbackType: FallbackType = FallbackType.ADDRESS
    ): String {
        val contactEmail = contacts.firstNotNullOfOrNull { contact ->
            contact.contactEmails.find { it.email == participant.address }
        }

        return contactEmail?.name?.takeIfNotBlank() ?: participant.name.ifEmpty {
            getFallbackName(participant, fallbackType)
        }
    }

    private fun getFallbackName(participant: Recipient, fallbackType: FallbackType): String {
        return when (fallbackType) {
            FallbackType.ADDRESS -> participant.address
            FallbackType.USERNAME -> participant.address.split('@').first()
            FallbackType.NONE -> EMPTY_STRING
        }
    }

    enum class FallbackType {
        ADDRESS,
        USERNAME,
        NONE
    }
}
