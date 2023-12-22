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

package ch.protonmail.android.mailsettings.presentation.settings.autolock.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel
import ch.protonmail.android.mailsettings.domain.model.autolock.AutoLockInsertionMode
import ch.protonmail.android.mailsettings.domain.model.autolock.AutoLockPin
import ch.protonmail.android.mailsettings.domain.model.autolock.AutoLockPinContinuationAction
import ch.protonmail.android.mailsettings.domain.model.autolock.AutoLockRemainingAttempts
import ch.protonmail.android.mailsettings.domain.repository.AutoLockPreferenceError
import ch.protonmail.android.mailsettings.domain.usecase.autolock.GetRemainingAutoLockAttempts
import ch.protonmail.android.mailsettings.domain.usecase.autolock.ObserveAutoLockPinValue
import ch.protonmail.android.mailsettings.domain.usecase.autolock.SaveAutoLockPin
import ch.protonmail.android.mailsettings.domain.usecase.autolock.ToggleAutoLockEnabled
import ch.protonmail.android.mailsettings.domain.usecase.autolock.UpdateRemainingAutoLockAttempts
import ch.protonmail.android.mailsettings.presentation.R
import ch.protonmail.android.mailsettings.presentation.settings.autolock.helpers.AutoLockTestData
import ch.protonmail.android.mailsettings.presentation.settings.autolock.mapper.pin.AutoLockPinErrorUiMapper
import ch.protonmail.android.mailsettings.presentation.settings.autolock.mapper.pin.AutoLockPinStepUiMapper
import ch.protonmail.android.mailsettings.presentation.settings.autolock.model.pin.AutoLockPinState
import ch.protonmail.android.mailsettings.presentation.settings.autolock.model.pin.AutoLockPinViewAction
import ch.protonmail.android.mailsettings.presentation.settings.autolock.model.pin.InsertedPin
import ch.protonmail.android.mailsettings.presentation.settings.autolock.model.pin.PinInsertionStep
import ch.protonmail.android.mailsettings.presentation.settings.autolock.model.pin.PinVerificationRemainingAttempts
import ch.protonmail.android.mailsettings.presentation.settings.autolock.reducer.pin.AutoLockPinReducer
import ch.protonmail.android.mailsettings.presentation.settings.autolock.ui.pin.AutoLockPinScreen
import ch.protonmail.android.mailsettings.presentation.settings.autolock.usecase.ClearPinDataAndForceLogout
import ch.protonmail.android.mailsettings.presentation.settings.autolock.viewmodel.pin.AutoLockPinViewModel
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.proton.core.util.kotlin.serialize
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

internal class AutoLockPinViewModelTest {

    private val observeAutoLockPin = mockk<ObserveAutoLockPinValue>()
    private val toggleAutoLockEnabled = mockk<ToggleAutoLockEnabled>()
    private val getRemainingAutoLockAttempts = mockk<GetRemainingAutoLockAttempts>()
    private val updateRemainingAutoLockAttempts = mockk<UpdateRemainingAutoLockAttempts>()
    private val saveAutoLockPin = mockk<SaveAutoLockPin>()
    private val clearPinDataAndForceLogout = mockk<ClearPinDataAndForceLogout>(relaxUnitFun = true)
    private val savedStateHandle = mockk<SavedStateHandle>()
    private val reducer = AutoLockPinReducer(AutoLockPinStepUiMapper(), AutoLockPinErrorUiMapper())

    private val viewModel by lazy {
        AutoLockPinViewModel(
            observeAutoLockPin,
            toggleAutoLockEnabled,
            getRemainingAutoLockAttempts,
            updateRemainingAutoLockAttempts,
            saveAutoLockPin,
            clearPinDataAndForceLogout,
            reducer,
            savedStateHandle
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `should return loaded state when data is fetched from a standalone start`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        val expectedState = AutoLockTestData.BaseLoadedState

        // When + Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `should move to previous screen when back action is performed from pin confirmation`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()

        // When + Then
        viewModel.state.test {
            skipItems(1)

            // Type pin of 4 digits and then confirm
            viewModel.insertPinAndConfirm("1234")
            skipItems(4)

            val intermediateState = awaitItem() as AutoLockPinState.DataLoaded
            assertEquals(PinInsertionStep.PinConfirmation, intermediateState.pinInsertionState.step)

            // Go back
            viewModel.submit(AutoLockPinViewAction.PerformBack)

            assertEquals(AutoLockTestData.BaseLoadedState, awaitItem())
        }
    }

    @Test
    fun `should dismiss the screen when going back from the main pin insertion screen`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        val expectedState = AutoLockTestData.BaseLoadedState.copy(closeScreenEffect = Effect.of(Unit))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.PerformBack)

            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `should dismiss the screen when going back from the main pin change screen`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.ChangePin)
        expectAttempts()
        val expectedCloseEffect = Effect.of(Unit)

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.PerformBack)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedCloseEffect, actual.closeScreenEffect)
            assertEquals(Effect.empty(), actual.navigateEffect)
        }
    }

    @Test
    fun `should not dismiss the screen when going back from the main pin verification screen`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.VerifyPin(AutoLockPinContinuationAction.None))
        expectAttempts()

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.PerformBack)

            expectNoEvents()
        }
    }

    @Test
    fun `should emit new state when pin digit is added`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        val expectedInsertedPin = InsertedPin(listOf(0))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.AddPinDigit(0))

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedInsertedPin, actual.pinInsertionState.pinInsertionUiModel.currentPin)
        }
    }

    @Test
    fun `should emit new state when pin digit is removed`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        val expectedInsertedPin = InsertedPin(listOf(1))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.AddPinDigit(1))
            viewModel.submit(AutoLockPinViewAction.AddPinDigit(0))

            skipItems(2)

            viewModel.submit(AutoLockPinViewAction.RemovePinDigit)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedInsertedPin, actual.pinInsertionState.pinInsertionUiModel.currentPin)
        }
    }

    @Test
    fun `should emit error when pins do not match`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        val expectedStep = PinInsertionStep.PinConfirmation
        val expectedError = Effect.of(TextUiModel(R.string.mail_settings_pin_insertion_error_no_match))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            // Insertion and confirmation
            viewModel.insertPinAndConfirm("1234")
            viewModel.insertPinAndConfirm("1233")
            skipItems(9)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedStep, actual.pinInsertionState.step)
            assertEquals(expectedError.consume(), actual.pinInsertionErrorEffect.consume())
        }
    }

    @Test
    fun `should emit a generic error when pin cannot be saved`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        coEvery {
            saveAutoLockPin(AutoLockTestData.BaseAutoLockPin)
        } returns AutoLockPreferenceError.DataStoreError.left()

        val expectedStep = PinInsertionStep.PinConfirmation
        val expectedError = Effect.of(TextUiModel(R.string.mail_settings_pin_insertion_error_unknown))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            // Insertion and confirmation
            viewModel.insertPinAndConfirm("1234")
            viewModel.insertPinAndConfirm("1234")
            skipItems(9)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedStep, actual.pinInsertionState.step)
            assertEquals(expectedError.consume(), actual.pinInsertionErrorEffect.consume())
        }
    }

    @Test
    fun `should emit a generic error when auto lock cannot be toggled`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        coEvery { saveAutoLockPin(AutoLockTestData.BaseAutoLockPin) } returns Unit.right()
        coEvery { toggleAutoLockEnabled(true) } returns AutoLockPreferenceError.DataStoreError.left()

        val expectedStep = PinInsertionStep.PinConfirmation
        val expectedError = Effect.of(TextUiModel(R.string.mail_settings_pin_insertion_error_unknown))

        // When + Then
        viewModel.state.test {
            skipItems(1)

            // Insertion and confirmation
            viewModel.insertPinAndConfirm("1234")
            viewModel.insertPinAndConfirm("1234")
            skipItems(9)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedStep, actual.pinInsertionState.step)
            assertEquals(expectedError.consume(), actual.pinInsertionErrorEffect.consume())
        }
    }

    @Test
    fun `should emit a completion event when pin confirmation succeeds`() = runTest {
        // Given
        expectStandaloneStart()
        expectAttempts()
        coEvery { saveAutoLockPin(AutoLockTestData.BaseAutoLockPin) } returns Unit.right()
        coEvery { toggleAutoLockEnabled(true) } returns Unit.right()

        val expectedStep = PinInsertionStep.PinConfirmation
        val expectedCloseEffect = Effect.of(Unit)

        // When + Then
        viewModel.state.test {
            skipItems(1)

            // Insertion and confirmation
            viewModel.insertPinAndConfirm("1234")
            viewModel.insertPinAndConfirm("1234")
            skipItems(9)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedStep, actual.pinInsertionState.step)
            assertEquals(expectedCloseEffect, actual.closeScreenEffect)
            assertEquals(Effect.empty(), actual.pinInsertionErrorEffect)
            assertEquals(Effect.empty(), actual.navigateEffect)
        }
    }

    @Test
    fun `should restore pin attempts when the pin change verification has success`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.ChangePin)
        expectAttempts()
        expectExistingPin("1234")
        expectValidAutoLockAttemptsUpdate()

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.insertPinAndConfirm("1234")

            cancelAndConsumeRemainingEvents()
        }

        coVerifySequence {
            updateRemainingAutoLockAttempts(PinVerificationRemainingAttempts.MaxAttempts)
            clearPinDataAndForceLogout wasNot called
        }
    }

    @Test
    fun `should decrement pin attempts when the pin change verification fails`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.ChangePin)
        expectAttempts(value = PinVerificationRemainingAttempts.MaxAttempts)
        expectExistingPin("1234")
        expectValidAutoLockAttemptsUpdate()

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.AddPinDigit(1))
            viewModel.submit(AutoLockPinViewAction.PerformConfirm)

            cancelAndConsumeRemainingEvents()
        }

        coVerifySequence {
            updateRemainingAutoLockAttempts(PinVerificationRemainingAttempts.MaxAttempts - 1)
            clearPinDataAndForceLogout wasNot called
        }
    }

    @Test
    fun `should call use case to force logout and clear auto lock to defaults when no attempts are left`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.ChangePin)
        expectAttempts(value = 1)
        expectExistingPin("1234")
        expectValidAutoLockAttemptsUpdate()

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.AddPinDigit(1))
            skipItems(1)

            viewModel.submit(AutoLockPinViewAction.PerformConfirm)
            expectNoEvents()
        }

        coVerifySequence {
            updateRemainingAutoLockAttempts wasNot called
            clearPinDataAndForceLogout()
        }
    }

    @Test
    fun `should close the screen when verification is completed without continuation`() = runTest {
        // Given
        expectConditionalStart(AutoLockInsertionMode.VerifyPin(AutoLockPinContinuationAction.None))
        expectAttempts()
        expectExistingPin("1234")
        expectValidAutoLockAttemptsUpdate()

        val expectedCloseEffect = Effect.of(Unit)

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.insertPinAndConfirm("1234")
            skipItems(4)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(expectedCloseEffect, actual.closeScreenEffect)
            assertEquals(Effect.empty(), actual.navigateEffect)
        }
    }

    @Test
    fun `should navigate to continuation when verification is completed with one`() = runTest {
        // Given
        val expectedDestination = "destination"
        expectConditionalStart(
            AutoLockInsertionMode.VerifyPin(AutoLockPinContinuationAction.NavigateToDeepLink(expectedDestination))
        )
        expectAttempts()
        expectExistingPin("1234")
        expectValidAutoLockAttemptsUpdate()

        val expectedNavigationEffect = Effect.of(expectedDestination)

        // When + Then
        viewModel.state.test {
            skipItems(1)

            viewModel.insertPinAndConfirm("1234")
            skipItems(4)

            val actual = awaitItem() as AutoLockPinState.DataLoaded

            assertEquals(Effect.empty(), actual.closeScreenEffect)
            assertEquals(expectedNavigationEffect, actual.navigateEffect)
        }
    }

    private fun expectConditionalStart(mode: AutoLockInsertionMode) {
        every {
            savedStateHandle.get<String>(AutoLockPinScreen.AutoLockPinModeKey)
        } returns mode.serialize()
    }

    private fun expectStandaloneStart() {
        every { savedStateHandle.get<String>(AutoLockPinScreen.AutoLockPinModeKey) } returns ""
    }

    private fun expectAttempts(value: Int = 10) {
        coEvery { getRemainingAutoLockAttempts() } returns AutoLockRemainingAttempts(value).right()
    }

    private fun expectValidAutoLockAttemptsUpdate() {
        coEvery { updateRemainingAutoLockAttempts(any()) } returns Unit.right()
    }

    private fun expectExistingPin(pin: String) {
        every { observeAutoLockPin() } returns flowOf(AutoLockPin(pin).right())
    }

    private fun AutoLockPinViewModel.insertPinAndConfirm(pin: String) {
        pin.toCharArray().forEach { submit(AutoLockPinViewAction.AddPinDigit(it.digitToInt())) }
        submit(AutoLockPinViewAction.PerformConfirm)
    }
}
