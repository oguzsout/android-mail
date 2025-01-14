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

package ch.protonmail.android.mailsettings.presentation.settings.customizetoolbar.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ch.protonmail.android.mailcommon.presentation.NO_CONTENT_DESCRIPTION
import ch.protonmail.android.mailsettings.presentation.R
import me.proton.core.compose.theme.ProtonDimens
import me.proton.core.compose.theme.ProtonTheme

@Composable
internal fun ToolbarDisclaimer(@StringRes textRes: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(
            ProtonTheme.colors.shadowRaised,
            RoundedCornerShape(size = ProtonDimens.ExtraSmallSpacing)
        ).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(ProtonDimens.DefaultIconSize),
            painter = painterResource(id = R.drawable.ic_info_circle),
            contentDescription = NO_CONTENT_DESCRIPTION,
            tint = ProtonTheme.colors.iconNorm
        )
        Spacer(modifier = Modifier.width(ProtonDimens.SmallSpacing))
        Text(
            text = stringResource(textRes),
            color = ProtonTheme.colors.textNorm,
            style = ProtonTheme.typography.body1Regular
        )
    }
}
