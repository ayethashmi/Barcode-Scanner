package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.JsonManager
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsAllergenResponse
import com.devappspros.barcodescanner.domain.entity.dependencies.Allergen
import com.devappspros.barcodescanner.domain.repositories.DevAppsAllergensRepository
import java.io.File

class DevAppsAllergensRepositoryImpl(private val fileFetcher: FileFetcher): DevAppsAllergensRepository {

    override suspend fun getAllergensList(fileNameWithExtension: String,
                                          fileUrlName: String,
                                          tagList: List<String>): List<Allergen> {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getAllergensList(tagList, file) else listOf()
    }

    override suspend fun getAllergens(fileNameWithExtension: String,
                                      fileUrlName: String,
                                      tagList: List<String>): String {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getAllergens(tagList, file) else ""
    }

    /**
     * Récupère une liste d'Allergen.
     */
    private fun getAllergensList(tagList: List<String>, jsonFile: File): List<Allergen> {

        val allergensList = mutableListOf<Allergen>()
        val jsonManager = JsonManager<DevAppsAllergenResponse>(jsonFile)

        for (tag in tagList) {
            // On récupère un AllergenResponse dans le fichier Json correspondant au tag
            val devAppsAllergenResponse: DevAppsAllergenResponse? = jsonManager.get(tag, DevAppsAllergenResponse::class)

            // Si on n'a pas le nom de l'allergènes, on met celui du tag en retirant le préfix "fr:"
            val allergenName = devAppsAllergenResponse?.name?.toLocaleLanguage() ?: tag.removePrefix("fr:")

            if(allergenName.isNotBlank()) {
                // On génère un Allergen à partir de l'AllergenResponse qu'on ajoute à la liste
                allergensList.add(Allergen(tag = tag, name = allergenName.trim()))
            }
        }

        return allergensList
    }

    /**
     * Récupère une liste d'Allergen au format String.
     */
    private fun getAllergens(tagList: List<String>, jsonFile: File): String {

        val strBuilder = StringBuilder()
        val jsonManager = JsonManager<DevAppsAllergenResponse>(jsonFile)

        for (tag in tagList) {

            // On récupère un AllergenResponse dans le fichier Json correspondant au tag
            val devAppsAllergenResponse: DevAppsAllergenResponse? = jsonManager.get(tag, DevAppsAllergenResponse::class)

            // Si on n'a pas le nom de l'allergènes, on met celui du tag en retirant le préfix "fr:"
            val allergenName = devAppsAllergenResponse?.name?.toLocaleLanguage() ?: tag.removePrefix("fr:")

            if(allergenName.isNotBlank()) {
                strBuilder.append(allergenName.trim())

                if (tagList.last() != tag)
                    strBuilder.append(", ")
            }
        }

        return strBuilder.toString()
    }
}