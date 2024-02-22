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

package ch.protonmail.android.mailcontact.presentation.contactgroupform

import ch.protonmail.android.mailcontact.presentation.model.ContactGroupFormUiModel
import me.proton.core.contact.domain.entity.ContactEmailId

sealed interface ContactGroupFormOperation

sealed interface ContactGroupFormViewAction : ContactGroupFormOperation {
    data class OnUpdateMemberList(
        val selectedContactEmailIds: List<String>
    ) : ContactGroupFormViewAction
    object OnCloseClick : ContactGroupFormViewAction
    object OnSaveClick : ContactGroupFormViewAction
    data class OnRemoveMemberClick(
        val contactEmailId: ContactEmailId
    ) : ContactGroupFormViewAction
    data class OnUpdateName(
        val name: String
    ) : ContactGroupFormViewAction
}

sealed interface ContactGroupFormEvent : ContactGroupFormOperation {
    data class ContactGroupLoaded(
        val contactGroupFormUiModel: ContactGroupFormUiModel
    ) : ContactGroupFormEvent
    object LoadError : ContactGroupFormEvent
    object Close : ContactGroupFormEvent
    object SaveContactGroupError : ContactGroupFormEvent
    object SavingContactGroup : ContactGroupFormEvent
    object ContactGroupCreated : ContactGroupFormEvent
    object ContactGroupUpdated : ContactGroupFormEvent
    object UpdateMembersError : ContactGroupFormEvent
}
