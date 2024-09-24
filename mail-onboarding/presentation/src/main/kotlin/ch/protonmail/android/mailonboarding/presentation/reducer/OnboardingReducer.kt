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

package ch.protonmail.android.mailonboarding.presentation.reducer

import ch.protonmail.android.mailonboarding.presentation.model.OnboardingOperation
import ch.protonmail.android.mailonboarding.presentation.model.OnboardingState
import javax.inject.Inject

class OnboardingReducer @Inject constructor() {

    fun newStateFrom(operation: OnboardingOperation): OnboardingState {
        return when (operation) {
            is OnboardingOperation.Event.ShowOnboarding.UpsellingOn -> OnboardingState.Shown.UpsellingOn
            is OnboardingOperation.Event.ShowOnboarding.UpsellingOff -> OnboardingState.Shown.UpsellingOff
            is OnboardingOperation.Action.CloseOnboarding -> OnboardingState.Hidden
        }
    }
}
