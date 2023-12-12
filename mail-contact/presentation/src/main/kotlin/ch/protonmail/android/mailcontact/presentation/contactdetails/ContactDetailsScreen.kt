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

package ch.protonmail.android.mailcontact.presentation.contactdetails

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ch.protonmail.android.mailcommon.presentation.ConsumableLaunchedEffect
import ch.protonmail.android.mailcommon.presentation.ConsumableTextEffect
import ch.protonmail.android.mailcommon.presentation.compose.dismissKeyboard
import ch.protonmail.android.mailcommon.presentation.ui.CommonTestTags
import ch.protonmail.android.mailcontact.presentation.R
import me.proton.core.compose.component.ProtonCenteredProgress
import me.proton.core.compose.component.ProtonSnackbarHost
import me.proton.core.compose.component.ProtonSnackbarHostState
import me.proton.core.compose.component.ProtonSnackbarType
import me.proton.core.compose.component.appbar.ProtonTopAppBar
import me.proton.core.compose.flow.rememberAsState
import me.proton.core.compose.theme.ProtonDimens
import me.proton.core.compose.theme.ProtonTheme
import me.proton.core.compose.theme.defaultNorm
import ch.protonmail.android.mailcontact.presentation.previewdata.ContactDetailsPreviewData.contactDetailsSampleData

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContactDetailsScreen(actions: ContactDetailsScreen.Actions, viewModel: ContactDetailsViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val view = LocalView.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostErrorState = ProtonSnackbarHostState(defaultType = ProtonSnackbarType.ERROR)
    val state = rememberAsState(flow = viewModel.state, initial = viewModel.initialState).value

    val customActions = actions.copy(
        onDeleteClick = {
            viewModel.submit(ContactDetailsViewAction.OnDeleteClick)
        }
    )

    Scaffold(
        topBar = {
            ContactDetailsTopBar(actions = customActions)
        },
        content = { paddingValues ->
            when (state) {
                is ContactDetailsState.Data -> {
                    ContactDetailsContent(
                        state = state,
                        actions = customActions,
                        modifier = Modifier.padding(paddingValues)
                    )

                    ConsumableTextEffect(effect = state.closeWithSuccess) { message ->
                        actions.exitWithSuccessMessage(message)
                    }
                }
                is ContactDetailsState.Loading -> {
                    ProtonCenteredProgress(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )

                    ConsumableTextEffect(effect = state.errorLoading) { message ->
                        actions.exitWithErrorMessage(message)
                    }
                }
            }
        },
        snackbarHost = {
            ProtonSnackbarHost(
                modifier = Modifier.testTag(CommonTestTags.SnackbarHostError),
                hostState = snackbarHostErrorState
            )
        }
    )

    ConsumableLaunchedEffect(effect = state.close) {
        dismissKeyboard(context, view, keyboardController)
        customActions.onBackClick()
    }
}

@Composable
fun ContactDetailsContent(
    state: ContactDetailsState.Data,
    actions: ContactDetailsScreen.Actions,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.padding(
                top = ProtonDimens.DefaultSpacing,
                start = ProtonDimens.DefaultSpacing
            ),
            style = ProtonTheme.typography.defaultNorm,
            text = state.contact.name
        )
    }
}

@Composable
fun ContactDetailsTopBar(actions: ContactDetailsScreen.Actions) {
    ProtonTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { },
        navigationIcon = {
            IconButton(onClick = actions.onBackClick) {
                Icon(
                    tint = ProtonTheme.colors.iconNorm,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.presentation_back)
                )
            }
        },
        actions = {
            IconButton(onClick = actions.onEditClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_proton_pen),
                    tint = ProtonTheme.colors.iconNorm,
                    contentDescription = stringResource(R.string.edit_contact_content_description)
                )
            }
            IconButton(onClick = actions.onDeleteClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_proton_trash),
                    tint = ProtonTheme.colors.iconNorm,
                    contentDescription = stringResource(R.string.delete_contact_content_description)
                )
            }
        }
    )
}

object ContactDetailsScreen {

    const val ContactDetailsContactIdKey = "contact_details_contact_id"

    data class Actions(
        val onBackClick: () -> Unit,
        val exitWithSuccessMessage: (String) -> Unit,
        val exitWithErrorMessage: (String) -> Unit,
        val onEditClick: () -> Unit,
        val onDeleteClick: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                onBackClick = {},
                exitWithSuccessMessage = {},
                exitWithErrorMessage = {},
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun ContactDetailsScreenPreview() {
    ContactDetailsContent(
        state = ContactDetailsState.Data(contact = contactDetailsSampleData),
        actions = ContactDetailsScreen.Actions.Empty
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun ContactDetailsTopBarPreview() {
    ContactDetailsTopBar(
        actions = ContactDetailsScreen.Actions.Empty
    )
}
