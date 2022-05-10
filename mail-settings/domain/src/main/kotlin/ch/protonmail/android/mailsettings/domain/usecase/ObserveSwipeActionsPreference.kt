/*
 * Copyright (c) 2021 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonMail.
 *
 * ProtonMail is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonMail is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonMail.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.protonmail.android.mailsettings.domain.usecase

import ch.protonmail.android.mailsettings.domain.model.SwipeActionsPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.proton.core.domain.entity.UserId
import javax.inject.Inject

class ObserveSwipeActionsPreference @Inject constructor(
    private val observeMailSettings: ObserveMailSettings
) {

    operator fun invoke(userId: UserId): Flow<SwipeActionsPreference> =
        observeMailSettings(userId).map { mailSettings ->
            checkNotNull(mailSettings) { "Error occurred while getting MailSettings" }
            SwipeActionsPreference(
                swipeLeft = checkNotNull(mailSettings.swipeLeft?.enum) { "Swipe Left is null" },
                swipeRight = checkNotNull(mailSettings.swipeRight?.enum) { "Swipe Right is null" }
            )
        }
}
