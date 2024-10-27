package com.devappspros.barcodescanner.data.repositories

import android.content.Context
import com.devappspros.barcodescanner.R
import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.JsonManager
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsAdditiveClassResponse
import com.devappspros.barcodescanner.domain.entity.dependencies.AdditiveClass
import com.devappspros.barcodescanner.domain.repositories.DevAppsAdditiveClassRepository
import java.io.File

class DevAppsAdditiveClassRepositoryImpl(private val context: Context,
                                         private val fileFetcher: FileFetcher): DevAppsAdditiveClassRepository {


    override suspend fun getAdditiveClassList(fileNameWithExtension: String,
                                              fileUrlName: String,
                                              tagList: List<String>): List<AdditiveClass> {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getAdditiveClassList(tagList, file) else listOf()
    }

    /**
     * Récupère une liste d'AdditiveClass.
     */
    private fun getAdditiveClassList(tagList: List<String>, jsonFile: File): List<AdditiveClass> {

        val additiveClassList = mutableListOf<AdditiveClass>()
        val jsonManager = JsonManager<DevAppsAdditiveClassResponse>(jsonFile)

        for (tag in tagList) {
            // On récupère un AdditiveClassResponse dans le fichier Json correspondant au tag
            val devAppsAdditiveClassResponse: DevAppsAdditiveClassResponse? = jsonManager.get(tag, DevAppsAdditiveClassResponse::class)

            // Si on n'a pas le nom de l'AdditiveClass, on met celui du tag
            val additiveClassName = devAppsAdditiveClassResponse?.name?.toLocaleLanguage() ?: tag

            if(additiveClassName.isNotBlank()) {
                // On génère un AdditiveClass à partir de l'AdditiveClassResponse qu'on ajoute à la liste
                additiveClassList.add(
                    AdditiveClass(
                        tag = tag,
                        name = additiveClassName.trim(),
                        description = getDescription(tag, devAppsAdditiveClassResponse).trim()
                    )
                )
            }
        }

        return additiveClassList
    }

    private fun getDescription(tag: String, devAppsAdditiveClassResponse: DevAppsAdditiveClassResponse?): String {

        val description = devAppsAdditiveClassResponse?.description?.toLocaleLanguage()

        return if(description.isNullOrBlank()){

            // Non présent dans fichier: additives_classes.json
            if(tag == "en:stabilizer"){
                context.getString(R.string.stabilizer_description_label)
            }else{
                context.getString(R.string.additive_no_information_found_label)
            }
        } else  {
            description
        }
    }
}