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
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "ch.protonmail.android.mailmailbox.domain"
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
    implementation(libs.bundles.module.domain)
    implementation(libs.proton.core.contact)
    implementation(libs.proton.core.label)
    implementation(libs.proton.core.mailSettings)
    implementation(libs.proton.core.user)

    implementation(project(":mail-label:domain"))
    implementation(project(":mail-pagination:domain"))
    implementation(project(":mail-conversation:domain"))
    implementation(project(":mail-common:domain"))
    implementation(project(":mail-message:domain"))
    implementation(project(":mail-settings:domain"))

    testImplementation(libs.bundles.test)
    testImplementation(project(":test:test-data"))
}
