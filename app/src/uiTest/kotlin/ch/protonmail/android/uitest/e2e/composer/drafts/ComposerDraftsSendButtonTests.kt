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

package ch.protonmail.android.uitest.e2e.composer.drafts

import ch.protonmail.android.di.ServerProofModule
import ch.protonmail.android.networkmocks.mockwebserver.combineWith
import ch.protonmail.android.networkmocks.mockwebserver.requests.get
import ch.protonmail.android.networkmocks.mockwebserver.requests.matchWildcards
import ch.protonmail.android.networkmocks.mockwebserver.requests.respondWith
import ch.protonmail.android.networkmocks.mockwebserver.requests.withStatusCode
import ch.protonmail.android.test.annotations.suite.RegressionTest
import ch.protonmail.android.uitest.MockedNetworkTest
import ch.protonmail.android.uitest.helpers.core.TestId
import ch.protonmail.android.uitest.helpers.core.navigation.Destination
import ch.protonmail.android.uitest.helpers.core.navigation.navigator
import ch.protonmail.android.uitest.helpers.login.LoginTestUserTypes
import ch.protonmail.android.uitest.helpers.network.mockNetworkDispatcher
import ch.protonmail.android.uitest.robot.common.section.fullscreenLoaderSection
import ch.protonmail.android.uitest.robot.composer.composerRobot
import ch.protonmail.android.uitest.robot.composer.section.recipients.toRecipientSection
import ch.protonmail.android.uitest.robot.composer.section.topAppBarSection
import ch.protonmail.android.uitest.robot.composer.section.verify
import ch.protonmail.android.uitest.robot.mailbox.mailboxRobot
import ch.protonmail.android.uitest.robot.mailbox.section.listSection
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import me.proton.core.auth.domain.usecase.ValidateServerProof
import org.junit.Test

@RegressionTest
@HiltAndroidTest
@UninstallModules(ServerProofModule::class)
internal class ComposerDraftsSendButtonTests : MockedNetworkTest(
    loginType = LoginTestUserTypes.Paid.FancyCapybara
), ComposerDraftsTests {

    @JvmField
    @BindValue
    val serverProofValidation: ValidateServerProof = mockk(relaxUnitFun = true)

    @Test
    @TestId("222787")
    fun checkComposerSendButtonEnabledUponOpeningAValidDraft() {
        mockWebServer.dispatcher combineWith mockNetworkDispatcher {
            addMockRequests(
                get("/mail/v4/messages?Page=0&PageSize=75&Limit=75&LabelID=8&Sort=Time&Desc=1")
                    respondWith "/mail/v4/messages/messages_222787.json"
                    withStatusCode 200,
                get("/mail/v4/messages/*")
                    respondWith "/mail/v4/messages/message-id/message-id_222787.json"
                    withStatusCode 200 matchWildcards true
            )
        }

        navigator { navigateTo(Destination.Drafts) }

        mailboxRobot {
            listSection { clickMessageByPosition(0) }
        }

        composerRobot {
            fullscreenLoaderSection { waitUntilGone() }

            topAppBarSection { verify { isSendButtonEnabled() } }
        }
    }

    @Test
    @TestId("222787/2", "222788")
    fun checkComposerSendButtonDisabledUponRemovingRecipientFromValidDraft() {
        mockWebServer.dispatcher combineWith mockNetworkDispatcher {
            addMockRequests(
                get("/mail/v4/messages?Page=0&PageSize=75&Limit=75&LabelID=8&Sort=Time&Desc=1")
                    respondWith "/mail/v4/messages/messages_222787.json"
                    withStatusCode 200,
                get("/mail/v4/messages/*")
                    respondWith "/mail/v4/messages/message-id/message-id_222787.json"
                    withStatusCode 200 matchWildcards true
            )
        }

        navigator { navigateTo(Destination.Drafts) }

        mailboxRobot {
            listSection { clickMessageByPosition(0) }
        }

        composerRobot {
            fullscreenLoaderSection { waitUntilGone() }

            toRecipientSection { deleteChipAt(position = 0) }

            topAppBarSection { verify { isSendButtonDisabled() } }
        }
    }
}
