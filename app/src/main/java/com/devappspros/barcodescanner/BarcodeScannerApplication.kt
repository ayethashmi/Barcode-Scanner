/*
 * Barcode Scanner
 * Copyright (C) 2021  Atharok
 *
 * This file is part of Barcode Scanner.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.devappspros.barcodescanner

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.devappspros.barcodescanner.common.injections.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BarcodeScannerApplication: Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BarcodeScannerApplication)
            modules(appModules)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
}