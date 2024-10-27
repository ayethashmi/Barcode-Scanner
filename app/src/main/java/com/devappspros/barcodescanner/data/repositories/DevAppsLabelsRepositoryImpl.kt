package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.JsonManager
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsLabelResponse
import com.devappspros.barcodescanner.domain.entity.dependencies.Label
import com.devappspros.barcodescanner.domain.repositories.DevAppsLabelsRepository
import java.io.File

class DevAppsLabelsRepositoryImpl(private val fileFetcher: FileFetcher): DevAppsLabelsRepository {

    override suspend fun getLabels(fileNameWithExtension: String,
                                   fileUrlName: String,
                                   tagList: List<String>): List<Label> {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) obtainList(file, tagList) else listOf()
    }

    /**
     * Récupère une liste de Label.
     */
    private fun obtainList(jsonFile: File, tagList: List<String>): List<Label> {

        val labels = mutableListOf<Label>()

        val jsonManager = JsonManager<Array<DevAppsLabelResponse>>(jsonFile)
        val jsonDevAppsLabelRespons: Array<DevAppsLabelResponse>? = getFromJson(jsonManager)

        if(!jsonDevAppsLabelRespons.isNullOrEmpty()) {

            for(tag in tagList){
                for(schema in jsonDevAppsLabelRespons){
                    if(tag == schema.id){
                        labels.add(Label(tag = tag, imageUrl = schema.image))
                        break
                    }
                }
            }
        }

        return labels
    }

    /**
     * La clé "tags" est le nom du tableau que l'on récupère dans le fichier Json.
     */
    private inline fun <reified T: Any> getFromJson(jsonManager: JsonManager<T>) = jsonManager.get("tags", T::class)
}