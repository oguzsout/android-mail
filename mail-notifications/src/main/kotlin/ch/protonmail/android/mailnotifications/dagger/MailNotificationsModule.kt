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

package ch.protonmail.android.mailnotifications.dagger

import ch.protonmail.android.mailnotifications.data.local.fcm.FcmTokenPreferencesImpl
import ch.protonmail.android.mailnotifications.domain.FcmTokenPreferences
import ch.protonmail.android.mailnotifications.permissions.NotificationsPermissionsOrchestrator
import ch.protonmail.android.mailnotifications.permissions.NotificationsPermissionsOrchestratorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MailNotificationsModule {

    @Module
    @InstallIn(SingletonComponent::class)
    internal interface BindsModule {

        @Binds
        @Singleton
        fun bindFcmTokenPreferences(implementation: FcmTokenPreferencesImpl): FcmTokenPreferences

        @Binds
        fun notificationsPermissionsOrchestrator(
            implementation: NotificationsPermissionsOrchestratorImpl
        ): NotificationsPermissionsOrchestrator
    }
}
