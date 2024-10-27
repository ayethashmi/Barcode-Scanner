package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.JsonManager
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsCountryResponse
import com.devappspros.barcodescanner.domain.entity.dependencies.Country
import com.devappspros.barcodescanner.domain.repositories.DevAppsCountriesRepository
import java.io.File

class DevAppsCountriesRepositoryImpl(private val fileFetcher: FileFetcher): DevAppsCountriesRepository {

    override suspend fun getCountriesList(fileNameWithExtension: String,
                                          fileUrlName: String,
                                          tagList: List<String>): List<Country> {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getCountriesList(tagList, file) else listOf()
    }

    override suspend fun getCountries(fileNameWithExtension: String,
                                      fileUrlName: String,
                                      tagList: List<String>): String {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getCountries(tagList, file) else ""
    }

    /**
     * Récupère une liste de Country.
     */
    private fun getCountriesList(tagList: List<String>, jsonFile: File): List<Country> {

        val countryList = mutableListOf<Country>()
        val jsonManager = JsonManager<DevAppsCountryResponse>(jsonFile)

        for (tag in tagList) {
            // On récupère un CountryResponse dans le fichier Json correspondant au tag
            val devAppsCountryResponse: DevAppsCountryResponse? = jsonManager.get(tag, DevAppsCountryResponse::class)

            // Si on n'a pas le nom du pays, on met celui du tag
            val countryName = devAppsCountryResponse?.name?.toLocaleLanguage() ?: tag

            if(countryName.isNotBlank()){
                // On génère un Country à partir de CountryResponse qu'on ajoute à la liste
                countryList.add(Country(tag = tag, name = countryName.trim()))
            }
        }

        return countryList
    }

    /**
     * Récupère une liste de Country au format String.
     */
    private fun getCountries(tagList: List<String>, jsonFile: File): String {

        val strBuilder = StringBuilder()
        val jsonManager = JsonManager<DevAppsCountryResponse>(jsonFile)

        for (tag in tagList) {

            // On récupère un CountryResponse dans le fichier Json correspondant au tag
            val devAppsCountryResponse: DevAppsCountryResponse? = jsonManager.get(tag, DevAppsCountryResponse::class)

            // Si on n'a pas le nom du pays, on met celui du tag
            val countryName = devAppsCountryResponse?.name?.toLocaleLanguage() ?: tag

            if(countryName.isNotBlank()){
                strBuilder.append(countryName.trim())

                if (tagList.last() != tag)
                    strBuilder.append(", ")
            }
        }

        return strBuilder.toString()
    }
}