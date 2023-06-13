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

package ch.protonmail.android.mailmailbox.presentation.mailbox.reducer

import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.maillabel.domain.model.MailLabel
import ch.protonmail.android.maillabel.domain.model.MailLabelId
import ch.protonmail.android.maillabel.presentation.text
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxEvent
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxListState
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxOperation
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxState
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxTopAppBarState
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.MailboxViewAction
import ch.protonmail.android.mailmailbox.presentation.mailbox.model.UnreadFilterState
import ch.protonmail.android.mailmailbox.presentation.mailbox.previewdata.MailboxStateSampleData
import ch.protonmail.android.testdata.label.LabelTestData
import ch.protonmail.android.testdata.mailbox.MailboxItemUiModelTestData
import ch.protonmail.android.testdata.mailbox.UnreadCountersTestData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import me.proton.core.mailsettings.domain.entity.ViewMode
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class MailboxReducerTest(
    private val testName: String,
    private val testInput: TestInput
) {

    private val mailboxListReducer: MailboxListReducer = mockk {
        every { newStateFrom(any(), any()) } returns reducedState.mailboxListState
    }
    private val topAppBarReducer: MailboxTopAppBarReducer = mockk {
        every { newStateFrom(any(), any()) } returns reducedState.topAppBarState
    }
    private val unreadFilterReducer: MailboxUnreadFilterReducer = mockk {
        every { newStateFrom(any(), any()) } returns reducedState.unreadFilterState
    }
    private val mailboxReducer = MailboxReducer(
        mailboxListReducer,
        topAppBarReducer,
        unreadFilterReducer
    )

    @Test
    fun `should reduce only the affected parts of the state`() = with(testInput) {
        // When
        val nextState = mailboxReducer.newStateFrom(currentState, operation)

        // Then
        if (shouldReduceMailboxListState) {
            verify {
                mailboxListReducer.newStateFrom(
                    currentState.mailboxListState,
                    operation as MailboxOperation.AffectingMailboxList
                )
            }
        } else {
            assertEquals(currentState.mailboxListState, nextState.mailboxListState, testName)
        }

        if (shouldReduceTopAppBarState) {
            verify {
                topAppBarReducer.newStateFrom(
                    currentState.topAppBarState,
                    operation as MailboxOperation.AffectingTopAppBar
                )
            }
        } else {
            assertEquals(currentState.topAppBarState, nextState.topAppBarState, testName)
        }

        if (shouldReduceUnreadFilterState) {
            verify {
                unreadFilterReducer.newStateFrom(
                    currentState.unreadFilterState,
                    operation as MailboxOperation.AffectingUnreadFilter
                )
            }
        } else {
            assertEquals(currentState.unreadFilterState, nextState.unreadFilterState, testName)
        }
    }

    companion object {

        private val spamLabel = MailLabel.System(MailLabelId.System.Spam)
        private val currentState = MailboxStateSampleData.Loading
        private val reducedState = MailboxState(
            mailboxListState = MailboxListState.Data(
                currentMailLabel = spamLabel,
                openItemEffect = Effect.empty(),
                scrollToMailboxTop = Effect.empty(),
                offlineEffect = Effect.empty(),
                refreshErrorEffect = Effect.empty(),
                refreshRequested = false
            ),
            topAppBarState = MailboxTopAppBarState.Data.DefaultMode(
                currentLabelName = spamLabel.text(),
                isComposerDisabled = false
            ),
            unreadFilterState = UnreadFilterState.Data(
                numUnread = 42,
                isFilterEnabled = false
            )
        )

        private val actions = listOf(
            TestInput(
                MailboxViewAction.EnterSelectionMode,
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = true,
                shouldReduceUnreadFilterState = false
            ),
            TestInput(
                MailboxViewAction.ExitSelectionMode,
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = true,
                shouldReduceUnreadFilterState = false
            ),
            TestInput(
                MailboxViewAction.OpenItemDetails(MailboxItemUiModelTestData.readMailboxItemUiModel),
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = false,
                shouldReduceUnreadFilterState = false
            ),
            TestInput(
                MailboxViewAction.EnableUnreadFilter,
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = false,
                shouldReduceUnreadFilterState = true
            ),
            TestInput(
                MailboxViewAction.DisableUnreadFilter,
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = false,
                shouldReduceUnreadFilterState = true
            )
        )

        private val events = listOf(
            TestInput(
                MailboxEvent.ItemDetailsOpenedInViewMode(
                    item = MailboxItemUiModelTestData.readMailboxItemUiModel,
                    preferredViewMode = ViewMode.NoConversationGrouping
                ),
                shouldReduceMailboxListState = true,
                shouldReduceTopAppBarState = false,
                shouldReduceUnreadFilterState = false
            ),
            TestInput(
                MailboxEvent.NewLabelSelected(
                    selectedLabel = LabelTestData.systemLabels.first(),
                    selectedLabelCount = UnreadCountersTestData.systemUnreadCounters.first().count
                ),
                shouldReduceMailboxListState = true,
                shouldReduceTopAppBarState = true,
                shouldReduceUnreadFilterState = true
            ),
            TestInput(
                MailboxEvent.SelectedLabelChanged(LabelTestData.systemLabels.first()),
                shouldReduceMailboxListState = true,
                shouldReduceTopAppBarState = true,
                shouldReduceUnreadFilterState = false
            ),
            TestInput(
                MailboxEvent.SelectedLabelCountChanged(UnreadCountersTestData.systemUnreadCounters.first().count),
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = false,
                shouldReduceUnreadFilterState = true
            ),
            TestInput(
                MailboxEvent.ComposerDisabledChanged(composerDisabled = false),
                shouldReduceMailboxListState = false,
                shouldReduceTopAppBarState = true,
                shouldReduceUnreadFilterState = false
            )
        )

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return (actions + events)
                .map { testInput ->
                    val testName = """
                        Operation: ${testInput.operation}
                        
                    """.trimIndent()
                    arrayOf(testName, testInput)
                }
        }
    }

    data class TestInput(
        val operation: MailboxOperation,
        val shouldReduceMailboxListState: Boolean,
        val shouldReduceTopAppBarState: Boolean,
        val shouldReduceUnreadFilterState: Boolean
    )
}
