package com.devappspros.barcodescanner.data.repositories

import com.devappspros.barcodescanner.data.model.openFoodFactsDependenciesResponse.DevAppsAdditiveResponse
import com.devappspros.barcodescanner.domain.entity.dependencies.Additive
import com.devappspros.barcodescanner.domain.entity.dependencies.AdditiveClass
import com.devappspros.barcodescanner.domain.entity.dependencies.OverexposureRiskRate
import com.devappspros.barcodescanner.domain.repositories.DevAppsAdditiveClassRepository
import com.devappspros.barcodescanner.domain.repositories.DevAppsAdditivesRepository

class DevAppsAdditivesRepositoryImpl(private val devAppsAdditiveResponseRepository: DevAppsAdditiveResponseRepository,
                                     private val devAppsAdditiveClassRepository: DevAppsAdditiveClassRepository): DevAppsAdditivesRepository {


    override suspend fun getAdditivesList(additiveFileNameWithExtension: String,
                                          additiveFileUrlName: String,
                                          tagList: List<String>,
                                          additiveClassFileNameWithExtension: String,
                                          additiveClassFileUrlName: String): List<Additive> {

        val devAppsAdditiveResponseList: List<DevAppsAdditiveResponse> =
            devAppsAdditiveResponseRepository.getAdditiveResponseList(
                fileNameWithExtension = additiveFileNameWithExtension,
                fileUrlName = additiveFileUrlName,
                tagList = tagList
            )

        val additiveClassTagList: List<String> = generateAdditivesClassesTagsList(devAppsAdditiveResponseList)

        val additiveClassList: List<AdditiveClass> = devAppsAdditiveClassRepository.getAdditiveClassList(
            additiveClassFileNameWithExtension, additiveClassFileUrlName, additiveClassTagList)

        return generateAdditivesList(devAppsAdditiveResponseList, additiveClassList)
    }

    /**
     * Récupère tous les tags de tous les AdditiveClass de tous les AdditiveResponse.
     */
    private fun generateAdditivesClassesTagsList(additivesResponseList: List<DevAppsAdditiveResponse>): List<String> {
        val allAdditivesClassesTagsList = mutableListOf<String>()
        for(additiveResponse in additivesResponseList){
            val additivesClassesTagsList = additiveResponse.additivesClasses?.value?.replace(", ", ",")?.split(",")
            if(additivesClassesTagsList!=null) {
                for (additiveClassTag in additivesClassesTagsList) {
                    if(!allAdditivesClassesTagsList.contains(additiveClassTag))
                        allAdditivesClassesTagsList.add(additiveClassTag)
                }
            }
        }
        return allAdditivesClassesTagsList
    }

    private fun generateAdditivesList(additivesResponseList: List<DevAppsAdditiveResponse>,
                                      additiveClassList: List<AdditiveClass>): List<Additive> {

        val additivesList: MutableList<Additive> = mutableListOf()

        for(additiveResponse in additivesResponseList){

            // Si on n'a pas le nom de l'additif, on met celui du tag
            val additiveName: String = additiveResponse.name?.toLocaleLanguage() ?: additiveResponse.tag

            val thisAdditiveClassList = mutableListOf<AdditiveClass>()
            val additiveClassTagsList = additiveResponse.additivesClasses?.value?.replace(", ", ",")?.split(",")

            if(!additiveClassTagsList.isNullOrEmpty()) {
                // Recupère dans la liste des AdditiveClass, les AdditiveClass que possèdent l'AdditiveResponse
                for (additiveClass in additiveClassList) {
                    if (additiveClassTagsList.contains(additiveClass.tag)){
                        thisAdditiveClassList.add(additiveClass)
                    }
                }
            }

            additivesList.add(
                Additive(
                    tag = additiveResponse.tag,
                    name = additiveName,
                    additiveClassList = thisAdditiveClassList,
                    overexposureRiskRate = determineOverexposureRisk(additiveResponse)
                )
            )

        }

        return additivesList
    }

    private fun determineOverexposureRisk(devAppsAdditiveResponse: DevAppsAdditiveResponse?): OverexposureRiskRate {

        return when(devAppsAdditiveResponse?.efsaEvaluationOverexposureRisk?.value){
            OverexposureRiskRate.LOW.id -> OverexposureRiskRate.LOW
            OverexposureRiskRate.MODERATE.id -> OverexposureRiskRate.MODERATE
            OverexposureRiskRate.HIGH.id -> OverexposureRiskRate.HIGH
            else -> OverexposureRiskRate.NONE
        }
    }
}