package com.devappspros.barcodescanner.common.injections

import android.content.ClipboardManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.common.utils.KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT
import com.devappspros.barcodescanner.common.utils.KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_STRING
import com.devappspros.barcodescanner.data.api.CoverArtArchiveService
import com.devappspros.barcodescanner.data.api.MusicBrainzService
import com.devappspros.barcodescanner.data.api.OpenBeautyFactsService
import com.devappspros.barcodescanner.data.api.OpenFoodFactsService
import com.devappspros.barcodescanner.data.api.OpenLibraryService
import com.devappspros.barcodescanner.data.api.OpenPetFoodFactsService
import com.devappspros.barcodescanner.data.database.AppDatabase
import com.devappspros.barcodescanner.data.database.BankDao
import com.devappspros.barcodescanner.data.database.BarcodeDao
import com.devappspros.barcodescanner.data.database.CustomUrlDao
import com.devappspros.barcodescanner.data.database.createBankDao
import com.devappspros.barcodescanner.data.database.createBarcodeDao
import com.devappspros.barcodescanner.data.database.createCustomUrlDao
import com.devappspros.barcodescanner.data.database.createDatabase
import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.FileStream
import com.devappspros.barcodescanner.data.file.image.BarcodeBitmapGenerator
import com.devappspros.barcodescanner.data.file.image.BarcodeSvgGenerator
import com.devappspros.barcodescanner.data.file.image.BitmapSharer
import com.devappspros.barcodescanner.data.network.createApiClient
import com.devappspros.barcodescanner.data.repositories.DevAppsAdditiveClassRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsAdditiveResponseRepository
import com.devappspros.barcodescanner.data.repositories.DevAppsAdditivesRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsAllergensRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsBankRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsBarcodeRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsBeautyProductRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsBookProductRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsCountriesRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsCustomUrlRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsDynamicShortcutRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsFileStreamRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsFoodProductRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsImageExportRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsImageGeneratorRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsInstalledAppsRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsLabelsRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsMusicProductRepositoryImpl
import com.devappspros.barcodescanner.data.repositories.DevAppsPetFoodProductRepositoryImpl
import com.devappspros.barcodescanner.data.shortcuts.DynamicShortcutsHandler
import com.devappspros.barcodescanner.domain.entity.barcode.Barcode
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeFormatDetails
import com.devappspros.barcodescanner.domain.entity.barcode.BarcodeType
import com.devappspros.barcodescanner.domain.entity.barcode.QrCodeErrorCorrectionLevel
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeBitmapAnalyser
import com.devappspros.barcodescanner.domain.library.DevAppsBarcodeFormatChecker
import com.devappspros.barcodescanner.domain.library.DevAppsBeepManager
import com.devappspros.barcodescanner.domain.library.DevAppsDateConverter
import com.devappspros.barcodescanner.domain.library.DevAppsIban
import com.devappspros.barcodescanner.domain.library.DevAppsInternetChecker
import com.devappspros.barcodescanner.domain.library.DevAppsSettingsManager
import com.devappspros.barcodescanner.domain.library.DevAppsVCardReader
import com.devappspros.barcodescanner.domain.library.DevAppsVibratorAppCompat
import com.devappspros.barcodescanner.domain.library.wifiSetup.configuration.DevAppsWifiSetupWithNewLibrary
import com.devappspros.barcodescanner.domain.library.wifiSetup.configuration.DevAppsWifiSetupWithOldLibrary
import com.devappspros.barcodescanner.domain.repositories.DevAppsAdditiveClassRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsAdditivesRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsAllergensRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsBankRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsBarcodeRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsBeautyProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsBookProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsCountriesRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsCustomUrlRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsDynamicShortcutRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsFileStreamRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsFoodProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsImageExportRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsImageGeneratorRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsInstalledAppsRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsLabelsRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsMusicProductRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsPetFoodProductRepository
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseBankUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseBarcodeUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsDatabaseCustomUrlUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsDevAppsDynamicShortcutUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsExternalFoodProductDependencyUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsImageManagerUseCase
import com.devappspros.barcodescanner.domain.usecases.DevAppsProductUseCase
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBankViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseBarcodeViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDatabaseCustomUrlViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsDynamicShortcutViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsExternalFileViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsImageManagerViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsInstalledAppsViewModel
import com.devappspros.barcodescanner.presentation.viewmodel.DevAppsProductViewModel
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsAbstractActionsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsAgendaActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsBarcodeContentsModifierModalBottomSheetFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsBeautyActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsBookActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsContactActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsDefaultActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsFoodActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsLocalizationActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsMusicActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsPetFoodActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsPhoneActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsProductActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsSmsActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsUrlActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeAnalysis.actions.DevAppsWifiActionsFragmentDevAppsDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsAbstractBarcodeFormCreatorFragment
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorAztecFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorCodabarFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorCode128FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorCode39FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorCode93FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorDataMatrixFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorEAN13FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorEAN8FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarCodeFormCreatorITFFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorPDF417FragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrAgendaFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrApplicationFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrContactFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrEpcFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrLocalisationFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrMailFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrPhoneFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrSmsFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrTextFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrUrlFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorQrWifiFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorUPCAFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.barcodeFormCreator.DevAppsBarcodeFormCreatorUPCEFragmentDevApps
import com.devappspros.barcodescanner.presentation.views.fragments.main.DevAppsMainBarcodeCreatorListFragment
import com.devappspros.barcodescanner.presentation.views.fragments.main.DevAppsMainBarcodeHistoryFragment
import com.devappspros.barcodescanner.presentation.views.fragments.main.DevAppsMainCameraXScannerFragment
import com.devappspros.barcodescanner.presentation.views.fragments.main.DevAppsMainSettingsFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.Result
import com.google.zxing.ResultMetadataType
import com.google.zxing.client.result.ParsedResult
import com.google.zxing.client.result.ParsedResultType
import com.google.zxing.client.result.ResultParser
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.KClass

val appModules by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        listOf<Module>(
            androidModule,
            libraryModule,
            libraryApi29Module,
            viewModelModule,
            useCaseModule,
            repositoryModule,
            dataModule,
            fragmentsModule
        )
    } else {
        listOf<Module>(
            androidModule,
            libraryModule,
            viewModelModule,
            useCaseModule,
            repositoryModule,
            dataModule,
            fragmentsModule
        )
    }
}

val androidModule: Module = module {
    single<DevAppsBeepManager> { DevAppsBeepManager() }
    single<DevAppsVibratorAppCompat> { DevAppsVibratorAppCompat(androidApplication().applicationContext) }
    single<ConnectivityManager> { androidApplication().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single<ClipboardManager> { androidApplication().applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    single<InputMethodManager> { androidApplication().applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    single<WifiManager> { androidApplication().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    single<LocationManager> { androidApplication().applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    factory<Barcode> { (contents: String, formatName: String, qrErrorCorrectionLevel: QrCodeErrorCorrectionLevel) ->
        Barcode(
            contents = contents,
            formatName = formatName,
            scanDate = System.currentTimeMillis(),
            errorCorrectionLevel = qrErrorCorrectionLevel.name
        ).apply {
            type = get<BarcodeType> {
                parametersOf(this)
            }.name
        }
    }

    factory { (contents: String, format: BarcodeFormat) ->
        val result = Result(contents, null, null, format)
        ResultParser.parseResult(result)
    }

    factory<BarcodeType> { (barcode: Barcode) ->
        when {
            barcode.is1DProductBarcodeFormat -> {
                if(barcode.isBookBarcode()) BarcodeType.BOOK else BarcodeType.UNKNOWN_PRODUCT
            }
            barcode.is1DIndustrialBarcodeFormat -> BarcodeType.INDUSTRIAL
            else -> {
                val parsedResult: ParsedResult = get {
                    parametersOf(barcode.contents, barcode.getBarcodeFormat())
                }
                when(parsedResult.type){
                    ParsedResultType.ADDRESSBOOK -> BarcodeType.CONTACT
//                    ParsedResultType.EMAIL_ADDRESS -> BarcodeType.MAIL
                    ParsedResultType.PRODUCT -> BarcodeType.UNKNOWN_PRODUCT
                    ParsedResultType.URI -> BarcodeType.URL
                    ParsedResultType.TEXT -> BarcodeType.TEXT
                    ParsedResultType.GEO -> BarcodeType.LOCALISATION
                    ParsedResultType.TEL -> BarcodeType.PHONE
                    ParsedResultType.SMS -> BarcodeType.SMS
                    ParsedResultType.CALENDAR -> BarcodeType.AGENDA
                    ParsedResultType.WIFI -> BarcodeType.WIFI
                    ParsedResultType.ISBN -> BarcodeType.BOOK
                    ParsedResultType.VIN -> BarcodeType.TEXT
                    else -> BarcodeType.UNKNOWN
                }
            }
        }
    }

    factory<QrCodeErrorCorrectionLevel>(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_STRING)) { (errorCorrectionLevel: String?) ->
        when(errorCorrectionLevel){
            "L" -> QrCodeErrorCorrectionLevel.L
            "M" -> QrCodeErrorCorrectionLevel.M
            "Q" -> QrCodeErrorCorrectionLevel.Q
            "H" -> QrCodeErrorCorrectionLevel.H
            else -> QrCodeErrorCorrectionLevel.NONE
        }
    }

    factory<QrCodeErrorCorrectionLevel>(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_RESULT)) { (result: Result) ->
        var errorCorrectionLevel: QrCodeErrorCorrectionLevel = QrCodeErrorCorrectionLevel.NONE
        result.resultMetadata?.let { metadata ->
            val errorCorrectionLevelStr = metadata[ResultMetadataType.ERROR_CORRECTION_LEVEL] as? String
            errorCorrectionLevel = get(named(KOIN_NAMED_ERROR_CORRECTION_LEVEL_BY_STRING)) {
                parametersOf(errorCorrectionLevelStr)
            }
        }
        errorCorrectionLevel
    }

    factory { Bundle() }
}

val libraryModule: Module = module {
    single<DevAppsSettingsManager> { DevAppsSettingsManager(androidContext()) }
    single<DevAppsBarcodeBitmapAnalyser>{ DevAppsBarcodeBitmapAnalyser() }
    single<DevAppsBarcodeFormatChecker> { DevAppsBarcodeFormatChecker(androidContext()) }
    single<DevAppsVCardReader> { DevAppsVCardReader(androidContext()) }
    single<DevAppsWifiSetupWithOldLibrary> { DevAppsWifiSetupWithOldLibrary() }
    single<DevAppsIban> { DevAppsIban() }
    single<DevAppsInternetChecker> { DevAppsInternetChecker() }
    single<DevAppsDateConverter> { DevAppsDateConverter() }

    factory { Date() }
    factory { (pattern: String) -> SimpleDateFormat(pattern, Locale.getDefault()) }
}

@RequiresApi(Build.VERSION_CODES.Q)
val libraryApi29Module: Module = module {
    single<DevAppsWifiSetupWithNewLibrary> { DevAppsWifiSetupWithNewLibrary() }
}

val viewModelModule: Module = module {
    viewModel {
        DevAppsProductViewModel(get<DevAppsProductUseCase>())
    }

    viewModel {
        DevAppsDatabaseBarcodeViewModel(get<DevAppsDatabaseBarcodeUseCase>())
    }

    viewModel {
        DevAppsDatabaseCustomUrlViewModel(get<DevAppsDatabaseCustomUrlUseCase>())
    }

    viewModel {
        DevAppsDatabaseBankViewModel(get<DevAppsDatabaseBankUseCase>())
    }

    viewModel {
        DevAppsExternalFileViewModel(get<DevAppsExternalFoodProductDependencyUseCase>())
    }

    viewModel {
        DevAppsInstalledAppsViewModel(get<DevAppsInstalledAppsRepository>())
    }

    viewModel {
        DevAppsImageManagerViewModel(get<DevAppsImageManagerUseCase>())
    }

    viewModel {
        DevAppsDynamicShortcutViewModel(get<DevAppsDevAppsDynamicShortcutUseCase>())
    }
}

val useCaseModule: Module = module {
    factory<DevAppsProductUseCase> {
        DevAppsProductUseCase(
            devAppsFoodProductRepository = get<DevAppsFoodProductRepository>(),
            devAppsBeautyProductRepository = get<DevAppsBeautyProductRepository>(),
            devAppsPetFoodProductRepository = get<DevAppsPetFoodProductRepository>(),
            devAppsMusicProductRepository = get<DevAppsMusicProductRepository>(),
            devAppsBookProductRepository = get<DevAppsBookProductRepository>()
        )
    }

    factory<DevAppsDatabaseBarcodeUseCase> {
        DevAppsDatabaseBarcodeUseCase(get<DevAppsBarcodeRepository>(), get<DevAppsFileStreamRepository>())
    }

    factory<DevAppsDatabaseCustomUrlUseCase> {
        DevAppsDatabaseCustomUrlUseCase(get<DevAppsCustomUrlRepository>())
    }

    factory<DevAppsDatabaseBankUseCase> {
        DevAppsDatabaseBankUseCase(get<DevAppsBankRepository>())
    }

    factory<DevAppsExternalFoodProductDependencyUseCase> {
        DevAppsExternalFoodProductDependencyUseCase(
            devAppsLabelsRepository = get<DevAppsLabelsRepository>(),
            devAppsAdditivesRepository = get<DevAppsAdditivesRepository>(),
            devAppsAllergensRepository = get<DevAppsAllergensRepository>(),
            devAppsCountriesRepository = get<DevAppsCountriesRepository>()
        )
    }

    factory { DevAppsImageManagerUseCase(get<DevAppsImageGeneratorRepository>(), get<DevAppsImageExportRepository>()) }

    factory { DevAppsDevAppsDynamicShortcutUseCase(get<DevAppsDynamicShortcutRepository>()) }
}

val repositoryModule: Module = module {

    single<DevAppsFoodProductRepository> {
        DevAppsFoodProductRepositoryImpl(get<OpenFoodFactsService>())
    }

    single<DevAppsBeautyProductRepository> {
        DevAppsBeautyProductRepositoryImpl(get<OpenBeautyFactsService>())
    }

    single<DevAppsPetFoodProductRepository> {
        DevAppsPetFoodProductRepositoryImpl(get<OpenPetFoodFactsService>())
    }

    single<DevAppsMusicProductRepository> {
        DevAppsMusicProductRepositoryImpl(get<MusicBrainzService>(), get<CoverArtArchiveService>())
    }

    single<DevAppsBookProductRepository> {
        DevAppsBookProductRepositoryImpl(get<OpenLibraryService>())
    }

    single<DevAppsBarcodeRepository> {
        DevAppsBarcodeRepositoryImpl(get<BarcodeDao>())
    }

    single<DevAppsCustomUrlRepository> {
        DevAppsCustomUrlRepositoryImpl(get<CustomUrlDao>())
    }

    single<DevAppsBankRepository> {
        DevAppsBankRepositoryImpl(get<BankDao>())
    }

    single<DevAppsLabelsRepository> {
        DevAppsLabelsRepositoryImpl(get<FileFetcher>())
    }

    single<DevAppsAdditiveResponseRepository> {
        DevAppsAdditiveResponseRepository(get<FileFetcher>())
    }

    single<DevAppsAdditiveClassRepository> {
        DevAppsAdditiveClassRepositoryImpl(androidContext(), get<FileFetcher>())
    }

    single<DevAppsAdditivesRepository> {
        DevAppsAdditivesRepositoryImpl(get<DevAppsAdditiveResponseRepository>(), get<DevAppsAdditiveClassRepository>())
    }

    single<DevAppsAllergensRepository> {
        DevAppsAllergensRepositoryImpl(get<FileFetcher>())
    }

    single<DevAppsCountriesRepository> {
        DevAppsCountriesRepositoryImpl(get<FileFetcher>())
    }

    single<DevAppsInstalledAppsRepository> {
        DevAppsInstalledAppsRepositoryImpl(androidContext())
    }

    single<DevAppsImageGeneratorRepository> {
        DevAppsImageGeneratorRepositoryImpl(get<BarcodeBitmapGenerator>(), get<BarcodeSvgGenerator>())
    }

    single<DevAppsImageExportRepository> {
        DevAppsImageExportRepositoryImpl(get<FileStream>(), get<BitmapSharer>())
    }

    single<DevAppsFileStreamRepository> { DevAppsFileStreamRepositoryImpl(get<FileStream>()) }

    single<DevAppsDynamicShortcutRepository> {
        DevAppsDynamicShortcutRepositoryImpl(get<DynamicShortcutsHandler>())
    }
}

val dataModule: Module = module {

    single<OpenFoodFactsService> {
        val baseUrl = androidContext().getString(R.string.base_api_open_food_facts_url)
        createApiClient(androidContext(), baseUrl).create(OpenFoodFactsService::class.java)
    }

    single<OpenBeautyFactsService> {
        val baseUrl = androidContext().getString(R.string.base_api_open_beauty_facts_url)
        createApiClient(androidContext(), baseUrl).create(OpenBeautyFactsService::class.java)
    }

    single<OpenPetFoodFactsService> {
        val baseUrl = androidContext().getString(R.string.base_api_open_pet_food_facts_url)
        createApiClient(androidContext(), baseUrl).create(OpenPetFoodFactsService::class.java)
    }

    single<MusicBrainzService> {
        val baseUrl = androidContext().getString(R.string.base_api_musicbrainz_url)
        createApiClient(androidContext(), baseUrl).create(MusicBrainzService::class.java)
    }

    single<CoverArtArchiveService> {
        val baseUrl = androidContext().getString(R.string.base_api_cover_art_archive_url)
        createApiClient(androidContext(), baseUrl).create(CoverArtArchiveService::class.java)
    }

    single<OpenLibraryService> {
        val baseUrl = androidContext().getString(R.string.base_api_open_library_url)
        createApiClient(androidContext(), baseUrl).create(OpenLibraryService::class.java)
    }

    single<AppDatabase> {
        createDatabase(androidContext())
    }

    single<BarcodeDao> {
        createBarcodeDao(get<AppDatabase>())
    }

    single<BankDao> {
        createBankDao(get<AppDatabase>())
    }

    single<CustomUrlDao> {
        createCustomUrlDao(get<AppDatabase>())
    }

    single<FileFetcher> { FileFetcher(androidContext()) }

    single<MultiFormatWriter> { MultiFormatWriter() }
    single<BarcodeBitmapGenerator> { BarcodeBitmapGenerator(get<MultiFormatWriter>()) }
    single<BarcodeSvgGenerator> { BarcodeSvgGenerator(get<MultiFormatWriter>()) }

    single { BitmapSharer(androidContext()) }

    single { FileStream(androidContext()) }

    single { DynamicShortcutsHandler(androidContext()) }
}

val fragmentsModule = module {

    factory { DevAppsMainCameraXScannerFragment() }
    factory { DevAppsMainBarcodeHistoryFragment() }
    factory { DevAppsMainBarcodeCreatorListFragment() }
    factory { DevAppsMainSettingsFragment() }

    factory { DevAppsBarcodeFormCreatorAztecFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorCodabarFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorCode39FragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorCode93FragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorCode128FragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorDataMatrixFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorEAN8FragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorEAN13FragmentDevApps() }
    factory { DevAppsBarCodeFormCreatorITFFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorPDF417FragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrAgendaFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrApplicationFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrContactFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrEpcFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrLocalisationFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrMailFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrPhoneFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrSmsFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrTextFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrUrlFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorQrWifiFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorUPCAFragmentDevApps() }
    factory { DevAppsBarcodeFormCreatorUPCEFragmentDevApps() }

    factory<DevAppsAbstractBarcodeFormCreatorFragment> { (barcodeFormatDetails: BarcodeFormatDetails) ->
        when(barcodeFormatDetails){
            BarcodeFormatDetails.AZTEC -> get<DevAppsBarcodeFormCreatorAztecFragmentDevApps>()
            BarcodeFormatDetails.CODABAR -> get<DevAppsBarcodeFormCreatorCodabarFragmentDevApps>()
            BarcodeFormatDetails.CODE_39 -> get<DevAppsBarcodeFormCreatorCode39FragmentDevApps>()
            BarcodeFormatDetails.CODE_93 -> get<DevAppsBarcodeFormCreatorCode93FragmentDevApps>()
            BarcodeFormatDetails.CODE_128 -> get<DevAppsBarcodeFormCreatorCode128FragmentDevApps>()
            BarcodeFormatDetails.DATA_MATRIX -> get<DevAppsBarcodeFormCreatorDataMatrixFragmentDevApps>()
            BarcodeFormatDetails.EAN_8 -> get<DevAppsBarcodeFormCreatorEAN8FragmentDevApps>()
            BarcodeFormatDetails.EAN_13 -> get<DevAppsBarcodeFormCreatorEAN13FragmentDevApps>()
            BarcodeFormatDetails.ITF -> get<DevAppsBarCodeFormCreatorITFFragmentDevApps>()
            BarcodeFormatDetails.PDF_417 -> get<DevAppsBarcodeFormCreatorPDF417FragmentDevApps>()
            BarcodeFormatDetails.QR_AGENDA -> get<DevAppsBarcodeFormCreatorQrAgendaFragmentDevApps>()
            BarcodeFormatDetails.QR_APPLICATION -> get<DevAppsBarcodeFormCreatorQrApplicationFragmentDevApps>()
            BarcodeFormatDetails.QR_CONTACT -> get<DevAppsBarcodeFormCreatorQrContactFragmentDevApps>()
            BarcodeFormatDetails.QR_EPC -> get<DevAppsBarcodeFormCreatorQrEpcFragmentDevApps>()
            BarcodeFormatDetails.QR_LOCALISATION -> get<DevAppsBarcodeFormCreatorQrLocalisationFragmentDevApps>()
//            BarcodeFormatDetails.QR_MAIL -> get<BarcodeFormCreatorQrMailFragment>()
            BarcodeFormatDetails.QR_PHONE -> get<DevAppsBarcodeFormCreatorQrPhoneFragmentDevApps>()
            BarcodeFormatDetails.QR_SMS -> get<DevAppsBarcodeFormCreatorQrSmsFragmentDevApps>()
            BarcodeFormatDetails.QR_TEXT -> get<DevAppsBarcodeFormCreatorQrTextFragmentDevApps>()
            BarcodeFormatDetails.QR_URL -> get<DevAppsBarcodeFormCreatorQrUrlFragmentDevApps>()
            BarcodeFormatDetails.QR_WIFI -> get<DevAppsBarcodeFormCreatorQrWifiFragmentDevApps>()
            BarcodeFormatDetails.UPC_A -> get<DevAppsBarcodeFormCreatorUPCAFragmentDevApps>()
            BarcodeFormatDetails.UPC_E -> get<DevAppsBarcodeFormCreatorUPCEFragmentDevApps>()
        }
    }

    factory<KClass<out DevAppsAbstractActionsFragmentDevApps>> { (barcodeType: BarcodeType) ->
        when(barcodeType){
            BarcodeType.AGENDA -> DevAppsAgendaActionsFragmentDevAppsDevApps::class
            BarcodeType.CONTACT -> DevAppsContactActionsFragmentDevAppsDevApps::class
            BarcodeType.LOCALISATION -> DevAppsLocalizationActionsFragmentDevAppsDevApps::class
//            BarcodeType.MAIL -> DevAppsEmailActionsFragmentDevApps::class
            BarcodeType.PHONE -> DevAppsPhoneActionsFragmentDevAppsDevApps::class
            BarcodeType.SMS -> DevAppsSmsActionsFragmentDevAppsDevApps::class
            BarcodeType.TEXT -> DevAppsDefaultActionsFragmentDevAppsDevApps::class
            BarcodeType.URL -> DevAppsUrlActionsFragmentDevAppsDevApps::class
            BarcodeType.WIFI -> DevAppsWifiActionsFragmentDevAppsDevApps::class
            BarcodeType.FOOD -> DevAppsFoodActionsFragmentDevAppsDevApps::class
            BarcodeType.PET_FOOD -> DevAppsPetFoodActionsFragmentDevAppsDevApps::class
            BarcodeType.BEAUTY -> DevAppsBeautyActionsFragmentDevAppsDevApps::class
            BarcodeType.MUSIC -> DevAppsMusicActionsFragmentDevAppsDevApps::class
            BarcodeType.BOOK -> DevAppsBookActionsFragmentDevAppsDevApps::class
            BarcodeType.INDUSTRIAL -> DevAppsDefaultActionsFragmentDevAppsDevApps::class
            BarcodeType.MATRIX -> DevAppsDefaultActionsFragmentDevAppsDevApps::class
            BarcodeType.UNKNOWN -> DevAppsDefaultActionsFragmentDevAppsDevApps::class
            BarcodeType.UNKNOWN_PRODUCT -> DevAppsProductActionsFragmentDevAppsDevApps::class
        }
    }

    factory { (barcode: Barcode) ->
        DevAppsBarcodeContentsModifierModalBottomSheetFragment.newInstance(barcode)
    }
}