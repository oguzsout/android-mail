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

package ch.protonmail.android.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import ch.protonmail.android.feature.account.RemoveAccountDialog
import ch.protonmail.android.mailconversation.domain.ConversationId
import ch.protonmail.android.mailconversation.presentation.ConversationDetail
import ch.protonmail.android.mailmailbox.presentation.MailboxScreen
import ch.protonmail.android.mailsettings.presentation.accountsettings.AccountSettingScreen
import ch.protonmail.android.mailsettings.presentation.accountsettings.conversationmode.ConversationModeSettingScreen
import ch.protonmail.android.mailsettings.presentation.settings.MainSettingsScreen
import ch.protonmail.android.navigation.model.Destination
import ch.protonmail.android.navigation.model.Destination.Dialog.RemoveAccount
import ch.protonmail.android.sidebar.Sidebar
import me.proton.core.compose.navigation.require
import me.proton.core.compose.theme.ProtonTheme
import me.proton.core.domain.entity.UserId
import timber.log.Timber

@Composable
fun Home(
    onSignIn: (UserId?) -> Unit,
    onSignOut: (UserId) -> Unit,
    onSwitch: (UserId) -> Unit,
    onSubscription: () -> Unit,
    onReportBug: () -> Unit
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerShape = RectangleShape,
        drawerScrimColor = ProtonTheme.colors.blenderNorm,
        drawerContent = {
            Sidebar(
                onRemove = { navController.navigate(RemoveAccount(it)) },
                onSignOut = onSignOut,
                onSignIn = onSignIn,
                onSwitch = onSwitch,
                onMailLocation = { /* stack screens? */ },
                onFolder = { /*navController.navigate(...)*/ },
                onLabel = { /*navController.navigate(...)*/ },
                onSettings = { navController.navigate(Destination.Screen.Settings.route) },
                onSubscription = onSubscription,
                onReportBug = onReportBug,
                drawerState = scaffoldState.drawerState
            )
        }
    ) { contentPadding ->
        Box(
            Modifier.padding(contentPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.Screen.Mailbox.route,
            ) {
                addMailbox(navController)
                addConversationDetail()
                addRemoveAccountDialog(navController)
                addSettings(navController)
                addAccountSettings(navController)
                addConversationModeSettings(navController)
            }
        }
    }
}

private fun NavGraphBuilder.addMailbox(
    navController: NavHostController
) = composable(
    route = Destination.Screen.Mailbox.route
) {
    MailboxScreen(
        navigateToConversation = { conversationId: ConversationId ->
            navController.navigate(Destination.Screen.Conversation(conversationId))
        }
    )
}

private fun NavGraphBuilder.addConversationDetail() = composable(
    route = Destination.Screen.Conversation.route,
) {
    ConversationDetail(Destination.Screen.Conversation.getConversationId(it.require(Destination.key)))
}

private fun NavGraphBuilder.addRemoveAccountDialog(navController: NavHostController) = dialog(
    route = Destination.Dialog.RemoveAccount.route,
) {
    RemoveAccountDialog(
        userId = Destination.Dialog.RemoveAccount.getUserId(it.require(Destination.key)),
        onRemoved = { navController.popBackStack() },
        onCancelled = { navController.popBackStack() }
    )
}

fun NavGraphBuilder.addSettings(navController: NavHostController) = composable(
    route = Destination.Screen.Settings.route
) {
    MainSettingsScreen(
        onAccountClicked = {
            Timber.d("Account settings item clicked. Navigating to account settings..")
            navController.navigate(Destination.Screen.AccountSettings.route)
        },
        onThemeClick = {
            Timber.i("Theme setting clicked")
        },
        onPushNotificationsClick = {
            Timber.i("Push Notifications setting clicked")
        },
        onAutoLockClick = {
            Timber.i("Auto Lock setting clicked")
        },
        onAlternativeRoutingClick = {
            Timber.i("Alternative routing setting clicked")
        },
        onAppLanguageClick = {
            Timber.i("App language setting clicked")
        },
        onCombinedContactsClick = {
            Timber.i("Combined contacts setting clicked")
        },
        onSwipeActionsClick = {
            Timber.i("Swipe actions setting clicked")
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}

fun NavGraphBuilder.addAccountSettings(navController: NavHostController) = composable(
    route = Destination.Screen.AccountSettings.route
) {
    AccountSettingScreen(
        onBackClick = { navController.popBackStack() },
        onSubscriptionClick = {
            Timber.i("Subscription setting clicked")
        },
        onPasswordManagementClick = {
            Timber.i("Password management setting clicked")
        },
        onRecoveryEmailClick = {
            Timber.i("Recovery email setting clicked")
        },
        onConversationModeClick = {
            Timber.i("Conversation mode setting clicked")
            navController.navigate(Destination.Screen.ConversationModeSettings.route)
        },
        onDefaultEmailAddressClick = {
            Timber.i("Default email address setting clicked")
        },
        onDisplayNameClick = {
            Timber.i("Display name setting clicked")
        },
        onPrivacyClick = {
            Timber.i("Privacy setting clicked")
        },
        onSearchMessageContentClick = {
            Timber.i("Search message content setting clicked")
        },
        onLabelsFoldersClick = {
            Timber.i("Labels folders setting clicked")
        },
        onLocalStorageClick = {
            Timber.i("Local storage setting clicked")
        },
        onSnoozeNotificationsClick = {
            Timber.i("Snooze notification setting clicked")
        }
    )
}

fun NavGraphBuilder.addConversationModeSettings(navController: NavHostController) = composable(
    route = Destination.Screen.ConversationModeSettings.route
) {
    ConversationModeSettingScreen(
        modifier = Modifier,
        onBackClick = { navController.popBackStack() }
    )
}
