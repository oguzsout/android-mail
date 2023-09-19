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

package ch.protonmail.android.mailcomposer.domain.usecase

import ch.protonmail.android.mailcomposer.domain.model.DraftBody
import ch.protonmail.android.mailmessage.domain.model.MimeType
import ch.protonmail.android.testdata.message.DecryptedMessageBodyTestData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SplitMessageBodyQuoteTest {

    private val splitMessageBodyHtmlQuote = SplitMessageBodyHtmlQuote()

    @Test
    fun `returns given decrypted body and no quote when mime type is plain text`() {
        // Given
        val decryptedBody = DecryptedMessageBodyTestData.PlainTextDecryptedBody

        // When
        val actual = splitMessageBodyHtmlQuote(decryptedBody)

        // Then
        assertEquals(Pair(DraftBody(decryptedBody.value), null), actual)
    }

    @Test
    fun `returns decrypted body text extracted from html and no quote when the input body has no quote anchors`() {
        // Given
        val decryptedBody = DecryptedMessageBodyTestData.buildDecryptedMessageBody(
            value = HtmlBodyWithNoQuoteAnchors,
            mimeType = MimeType.Html
        )

        // When
        val actual = splitMessageBodyHtmlQuote(decryptedBody)

        // Then
        val expected = DraftBody("$BodyTypedContentRaw $BodyMoreTypedContentRaw")
        assertEquals(Pair(expected, null), actual)
    }

    @Test
    fun `returns non-html text extracted from html and quote when the input body has one of the quote anchors`() {
        // Given
        val decryptedBody = DecryptedMessageBodyTestData.buildDecryptedMessageBody(
            value = HtmlBodyWithProtonQuoteAnchor,
            mimeType = MimeType.Html
        )

        // When
        val actual = splitMessageBodyHtmlQuote(decryptedBody)
        val actualQuote = actual.second!!

        // Then
        assertEquals(DraftBody(BodyTypedContentRaw), actual.first)
        assertTrue(actualQuote.value.contains(ProtonQuoteAnchor))
        assertTrue(actualQuote.value.contains(HtmlQuotedContentRaw))
    }

    companion object {
        private const val BodyTypedContentRaw = "Typed in content"
        private const val BodyMoreTypedContentRaw = "this is additional typed content"
        private const val HtmlQuotedContentRaw = "<span> Any quoted html content here </span>"
        private const val ProtonQuoteAnchor = "<div class=\"protonmail_quote\">"

        private const val ProtonQuoteHtml = "$ProtonQuoteAnchor $HtmlQuotedContentRaw"

        private const val HtmlBodyWithNoQuoteAnchors = """
           <html> <head>
              <body>
                 $BodyTypedContentRaw
                 <div> $BodyMoreTypedContentRaw </div>
              </body>
           </head> </html>
        """

        private const val HtmlBodyWithProtonQuoteAnchor = """
           <html> <head>
              <body>
                 $BodyTypedContentRaw
                 $ProtonQuoteHtml
              </body>
           </head> </html>
        """
    }
}
