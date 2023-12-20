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

package ch.protonmail.android.testdata.contact

object ContactVCardSample {

    val marioVCardType2 = """
        BEGIN:VCARD
        VERSION:4.0
        PRODID:ez-vcard 0.11.3
        N:Mario Last Name;Mario First Name;;;
        TEL;PREF=1:1231231235
        TEL;TYPE=home;PREF=2:23233232323
        TEL;TYPE=pager;PREF=3:34343434
        TEL;TYPE=work;PREF=4:45454545
        TEL;TYPE=other;PREF=5:565656
        TEL;TYPE=cell;PREF=6:676767
        TEL;PREF=7:787887
        TEL;TYPE=fax;PREF=8:898989
        TEL;TYPE=pager;PREF=9:90909090
        ADR;PREF=1:;;Address Street1;City;Region;123;Country
        ADR;TYPE=other;PREF=2:;;Address Other1;City;Region;234;Country
        ADR;TYPE=home;PREF=3:;;Home address the rest is empty;;;;
        ADR;TYPE=work;PREF=4:;;Work address the rest is empty;;;;
        BDAY:20231214
        NOTE:Note1
        NOTE:Note2
        ORG:Organization1
        ORG:Organization2
        TITLE:Title
        LOGO:data:image/vnd.microsoft.icon;base64,AAABAAEAEBAAAAEAIABoBAAAFgAAACgAA
         AAQAAAAIAAAAAEAIAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABUxVNEVM1eWAAAAAAAAAAAAAAAAFDBS/xxBav8cQ
         Wr/HEFq/xxBaf8bQGn/GT1k/ydCZv84WoT/O16J/zteif8oTHb/HUJr/xtAaP8aP2f/Ei5P/yF
         Ebf8jR3H/I0hx/yJHcP8gRW7/HkNs/x1Ca//Jycn/xsXG/xk+Zf81VX7/O16J/zJWgP8gRW7/H
         EFp/xk+Zf8mRWv/M1aB/zZZhP82WYT/M1eB/8bGxv/o6On/6Ojp/+jo6f/o6On/ubm5/xg6YP8
         6XIf/NlmE/yFGb/8ZPGL/AAAAADNSev86XYf/O16J/zteif87Xon/O16J/zpdiP8vUn3/JUpz/
         x9Ebf8cQWr/Gz9o/zhZg/8tT3j/AAAAAAAAAAAAAAAAHD9m/x5DbP/l5OT//////9jX1/84WYP
         /O16J/9PS0///////6ejo/x5CbP8bQGj/AAAAAAAAAAAAAAAAAAAAAC1Re/8uUnz/+/v7/xERE
         P/7+/v/HUJr/xk7YP/7+/v/EREQ//v7+/8oTHb/H0Rt/wAAAAAAAAAAAAAAAAAAAAAwTXT/O16
         J//T09P8RERD/9PT0/y5SfP8jSHH/9PT0/xEREP8RERD/Ol2I/yNEav8AAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAADn5+f/6Ojo/9fW1v87Xon/O16J/9PS0v/p6en/6enp/wAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAIEVu/x1Ca/8cQWr/GDpi/zBNdP87Xon/NViD/yRIcv8AAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADNWgf8sUHr/I0dx/x1Ca/8bQGn/FjVZ/zpch/8
         vTnX/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOVuG/zpdiP8pTXf/H0Nt/
         xxBaf8UMVTDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA7Xon
         /O16J/y1Re/8fRG3/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAO16J/zteif87Xon/JEJo/wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAADJQeP87Xon/KUNn/wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAA/+cAAAAAAAAAAAAAAAAAAIABAADAAwAAwAMAAMADAADwDwAA8A8AAPAPAAD4HwAA/
         D8AAPw/AAD8fwAA//8AAA==
        PHOTO;PREF=1:data:image/vnd.microsoft.icon;base64,AAABAAEAICAAAAEAIACoEAAAF
         gAAACgAAAAgAAAAQAAAAAEAIAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAABQwUv8WNlr/Fzdd/xc4Xf8WNlv/FTNW/xMvUf8uS3GGKEJm/ylDZ
         /8qRWr/K0Zs/y1Jb/8vTXT/MVB4/yxLc/8mRm3/IUJp/x09ZP8ZO2H/GTpg/xg5X/8XOF3/Fjd
         c/xU0WP8TMFL/AAAAAAAAAAAAAAAAAAAAABUxVP8aPmX/HEFq/xxBav8cQWn/HEFp/xtAaf8bQ
         Gn/G0Bo/xtAZ/8ZPGP/Fjdc/yhCZ/8vS3L/NVV+/zpdiP87Xon/O16J/zteif83WoX/LVF7/yV
         Kc/8gRW7/HUJr/xxBav8bQGj/Gj9n/xo/Z/8ZPGP/FDFU/wAAAAAZOF5QHEBo/x1Ca/8dQmv/H
         UJr/x1Ca/8cQWr/HEFq/xxBav8cQWr/HEFq/xtAaf8bQGn/G0Bo/xo+Zv8YO2L/KkVq/zRTe/8
         6XIb/O16J/zteif87Xon/NVmD/ytPef8jSHH/HkNs/xxBav8bQGn/Gj9n/xo/Z/8ZPWT/AAAAA
         Bk3XP8gRW7/IEVu/yBFbv8gRW7/IEVu/yBEbv8fRG3/HkNs/x5DbP8dQmv/HUFr/xxBav8cQWr
         /HEFp/xtAaf8bQGj/Gj5m/xc5X/8wTnX/OFqE/zteif87Xon/O16J/zBUfv8mSnT/IERu/xxBa
         v8cQWn/Gj9n/xo/Z/8VM1f/HTxh/yVJc/8mSnT/Jkt0/ydLdf8nS3X/Jkt0/yVKc/8kSXL/I0h
         x/yJGcP8gRW7/H0Rt/9fX1//x8fH/+fn5//n5+f/t7e3/z8/P/xo/aP8YO2H/L0xy/zlbhf87X
         on/O16J/zRXgv8pTXf/IUZv/x1Ca/8cQWr/Gj9o/xc3XP8dOVv/LFB6/y1Re/8vU33/MFR+/zB
         Ufv8wVH7/L1N9/y5SfP8sUHr/Kk54/8vLy//09PT/9vb2//b29v/29vb/9vb2//b29v/29vb/8
         O/w/769vv8aP2f/Fzdc/zNRef87XYj/O16J/zZZhP8qTnj/IUZv/x1Ca/8cQWr/FjVZ/wAAAAA
         vT3j/N1qF/zlch/87Xon/O16J/zteif87Xon/O16J/zlch/+op6n/x8bH/8fGx//Hxsf/x8bH/
         8fGx//Hxsf/x8bH/8fGx//Hxsf/xsXF/5+en/8bQGj/GTxk/y1Jb/85W4b/O16J/zVZg/8pTXf
         /IUZv/xxAaP8AAAAAAAAAAAAAAAA1Vn//O16J/zteif87Xon/O16J/zteif87Xon/O16J/1xwi
         /9ccIv/XHCL/1xvi/9YbYj/VWqE/1Fogv9PZX//TWJ9/0the/9KYHr/SV95/xxBav8cQWn/Gz9
         n/xQxVP83WYL/O16J/zJWgP8lSXH/FzNW/wAAAAAAAAAAAAAAAAAAAAArRmv/ME52/zJReP80U
         3v/NlZ//zhZg/86XIb/O16J/zteif87Xon/O16J/zteif87Xon/OFuG/zJWgP8tUHv/KEx2/yN
         Icf8gRW7/HkNs/xxBav8cQWr/Gz9o/xQxU/81Vn//M1N7/wAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAVMFL1Gjxj/xo8Y/8ZOmH/GDhd/+Hg4f/19fX/8PDw/9fW1v84WoT/O16J/zteif8
         7Xon/O16J/zldh//T0tP/6unp//Dv7//k4+P/H0Rt/x1Ca/8cQWr/Gz9m/xUyVf8AAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB09ZP8hRW//IUVv/yBFbv/i4eH//v7+//7+/v/+/v7
         //f39/9LR0v8ULk/6MlB4/zhahP87Xon/O16J//n5+f/+/v7//v7+//7+/v/m5eX/IUVv/x5Db
         P8cQWr/Gj1l/wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJUdv/ylNd/8pTXf/KEx
         2//v7+/8RERD/EREQ/xEREP/8/Pz/6unp/xxAaf8aPGP/FTNX/yxIbv/k4+P//Pz8/xEREP8RE
         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
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAArRmv0O12I/zteif87Xon/O16J/zteif86X
         Yj/LVB6/x06X/8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA0VH3/O
         16J/zteif87Xon/O16J/zpdiP8sSXD/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAA7XYj/O16J/zteif84WoT/K0ds/wAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADlbhv86XYj/NFN8/wAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAApQ2f/ME10/yl
         Eaf8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
         AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA/////+AAAAeAAAABAAAAAQAAAAAAAAA
         AAAAAAIAAAAHAAAAB4AAAB/AAAA/wAAAP8AAAD/AAAA/wAAAP+AAAH/wAAH//AAB//gAAf/4AA
         H//AAD//wAB//+AB///wAf//+AH///gD///4A////Af///4P///+P////H///////8=
        ROLE:Role
        TZ:Europe/Paris
        MEMBER:Member
        LANG:English
        URL:http://proton.me
        GENDER:RATHER NOT SAY
        ANNIVERSARY:20231206
        END:VCARD
    """.trimIndent()

    val marioVCardType3 = """
        BEGIN:VCARD
        VERSION:4.0
        PRODID:ez-vcard 0.11.3
        FN;PREF=1:Mario@protonmail.com
        UID:proton-autosave-84a8d5e8-e999-4c3e-960e-eea05fc5fe91
        ITEM1.EMAIL;PREF=1:Mario@protonmail.com
        ITEM2.EMAIL;TYPE=home;PREF=2:home_email@Mario.protonmail.com
        ITEM3.EMAIL;TYPE=work;PREF=3:work_email@Mario.protonmail.com
        ITEM4.EMAIL;TYPE=other;PREF=4:other_email@Mario.protonmail.com
        END:VCARD
    """.trimIndent()

    val stefanoVCardType3 = """
        BEGIN:VCARD
        VERSION:4.0
        PRODID:ez-vcard 0.11.3
        FN;PREF=1:Stefano@protonmail.com
        UID:proton-autosave-84a8d5e8-e999-4c3e-960e-eea05fc5fe91
        ITEM1.EMAIL;PREF=1:Stefano@protonmail.com
        ITEM2.EMAIL;TYPE=home;PREF=2:home_email@Stefano.protonmail.com
        ITEM3.EMAIL;TYPE=work;PREF=3:work_email@Stefano.protonmail.com
        ITEM4.EMAIL;TYPE=other;PREF=4:other_email@Stefano.protonmail.com
        END:VCARD
    """.trimIndent()
}
