package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.file.FileFetcher
import com.devappspros.barcodescanner.data.file.JsonManager
import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsAdditiveResponse
import java.io.File

class DevAppsAdditiveResponseRepository(private val fileFetcher: FileFetcher) {

    fun getAdditiveResponseList(fileNameWithExtension: String,
                                        fileUrlName: String,
                                        tagList: List<String>): List<DevAppsAdditiveResponse> {

        val file = fileFetcher.fetchFile(fileNameWithExtension, fileUrlName)

        return if(file.exists()) getAdditiveResponseList(tagList, file) else listOf()
    }

    private fun getAdditiveResponseList(tagList: List<String>, jsonFile: File): List<DevAppsAdditiveResponse> {

        val devAppsAdditiveResponseList = mutableListOf<DevAppsAdditiveResponse>()
        val jsonManager = JsonManager<DevAppsAdditiveResponse>(jsonFile)

        for (tag in tagList) {
            // On récupère un AllergenResponse dans le fichier Json correspondant au tag
            val devAppsAdditiveResponse: DevAppsAdditiveResponse? = jsonManager.get(tag, DevAppsAdditiveResponse::class)

            if(devAppsAdditiveResponse != null) {
                devAppsAdditiveResponse.tag = tag
                devAppsAdditiveResponseList.add(devAppsAdditiveResponse)
            }
        }

        return devAppsAdditiveResponseList
    }
}