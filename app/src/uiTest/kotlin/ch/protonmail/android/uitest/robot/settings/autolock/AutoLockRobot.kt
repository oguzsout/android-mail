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

package ch.protonmail.android.uitest.robot.settings.autolock

import ch.protonmail.android.uitest.robot.settings.SettingsRobot

@Suppress("unused", "ExpressionBodySyntax")
class AutoLockRobot {

    fun navigateUptToSettings(): SettingsRobot {
        return SettingsRobot(TODO("Inject composeTestRule in this robot when used"))
    }

    fun enableAutoLock(): PinRobot {
        return PinRobot()
    }

    fun changeAutoLockTimer(): AutoLockTimeoutRobot {
        return AutoLockTimeoutRobot()
    }

    /**
     * Represents Auto lock timeout pop up with options list.
     */
    class AutoLockTimeoutRobot {

        fun selectImmediateAutoLockTimeout(): AutoLockRobot {
            return AutoLockRobot()
        }

        fun selectFiveMinutesAutoLockTimeout(): AutoLockRobot {
            return AutoLockRobot()
        }
    }
}
