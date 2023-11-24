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

package ch.protonmail.android.maillabel.presentation.folderform

import androidx.compose.ui.graphics.Color
import ch.protonmail.android.mailcommon.presentation.Effect
import ch.protonmail.android.mailcommon.presentation.model.TextUiModel
import me.proton.core.label.domain.entity.Label
import me.proton.core.label.domain.entity.LabelId

sealed interface FolderFormState {

    val close: Effect<Unit>
    val isSaveEnabled: Boolean

    data class Loading(
        override val close: Effect<Unit> = Effect.empty(),
        override val isSaveEnabled: Boolean = false
    ) : FolderFormState

    sealed interface Data : FolderFormState {

        val name: String
        val color: String
        val parent: Label?
        val notifications: Boolean
        val colorList: List<Color>
        val openParentFolderList: Effect<Unit>
        val closeWithSuccess: Effect<TextUiModel>
        val showErrorSnackbar: Effect<TextUiModel>

        data class Create(
            override val isSaveEnabled: Boolean,
            override val name: String,
            override val color: String,
            override val parent: Label?,
            override val notifications: Boolean,
            override val colorList: List<Color>,
            override val openParentFolderList: Effect<Unit> = Effect.empty(),
            override val close: Effect<Unit> = Effect.empty(),
            override val closeWithSuccess: Effect<TextUiModel> = Effect.empty(),
            override val showErrorSnackbar: Effect<TextUiModel> = Effect.empty()
        ) : Data

        data class Update(
            override val isSaveEnabled: Boolean,
            override val name: String,
            override val color: String,
            override val parent: Label?,
            override val notifications: Boolean,
            override val colorList: List<Color>,
            override val openParentFolderList: Effect<Unit> = Effect.empty(),
            override val close: Effect<Unit> = Effect.empty(),
            override val closeWithSuccess: Effect<TextUiModel> = Effect.empty(),
            override val showErrorSnackbar: Effect<TextUiModel> = Effect.empty(),
            val labelId: LabelId
        ) : Data
    }
}


