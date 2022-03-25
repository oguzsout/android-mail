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

package ch.protonmail.android.mailsettings.presentation.accountsettings.conversationmode

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import ch.protonmail.android.mailsettings.domain.ObserveMailSettings
import ch.protonmail.android.mailsettings.presentation.accountsettings.conversationmode.ConversationModeSettingState.Data
import ch.protonmail.android.mailsettings.presentation.accountsettings.conversationmode.ConversationModeSettingState.Loading
import ch.protonmail.android.testdata.mailsettings.MailSettingsTestData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.proton.core.mailsettings.domain.entity.MailSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ConversationModeSettingViewModelTest {

    private val mailSettingsFlow = MutableSharedFlow<MailSettings?>()
    private val observeMailSettings = mockk<ObserveMailSettings> {
        every { this@mockk.invoke() } returns mailSettingsFlow
    }

    private lateinit var viewModel: ConversationModeSettingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        viewModel = ConversationModeSettingViewModel(observeMailSettings)
    }

    @Test
    fun `state has conversation mode flag when use case returns a valid mail settings`() = runTest {
        viewModel.state.test {
            // Given
            initialStateEmitted()

            // When
            mailSettingsFlow.emit(MailSettingsTestData.mailSettings)

            // Then
            val actual = awaitItem() as Data
            assertEquals(false, actual.isEnabled)
        }
    }

    @Test
    fun `state has null conversation mode when use case returns invalid mail settings`() = runTest {
        viewModel.state.test {
            // Given
            initialStateEmitted()

            // When
            mailSettingsFlow.emit(null)

            // Then
            val actual = awaitItem() as Data
            assertNull(actual.isEnabled)
        }
    }

    private suspend fun FlowTurbine<ConversationModeSettingState>.initialStateEmitted() {
        awaitItem() as Loading
    }

}
