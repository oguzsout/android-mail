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

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "ch.protonmail.android.testdata"
    compileSdk = Config.compileSdk

    defaultConfig {
        minSdk = Config.minSdk
        lint.targetSdk = Config.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.kotlinx.immutableCollections)

    implementation(libs.proton.core.account)
    implementation(libs.proton.core.contact)
    implementation(libs.proton.core.domain)
    implementation(libs.proton.core.featureFlag)
    implementation(libs.proton.core.label)
    implementation(libs.proton.core.mailSettings)
    implementation(libs.proton.core.network)
    implementation(libs.proton.core.plan)
    implementation(libs.proton.core.user)
    implementation(libs.proton.core.userSettings)

    implementation(project(":mail-common"))
    implementation(project(":mail-conversation"))
    implementation(project(":mail-label"))
    implementation(project(":mail-mailbox"))
    implementation(project(":mail-message"))
    implementation(project(":mail-detail"))
}
