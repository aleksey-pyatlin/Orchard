package Orchard

import Orchard.buildTypes.*
import Orchard.vcsRoots.*
import Orchard.vcsRoots.Vcs_Orchard_Develop
import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.Project
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.VersionedSettings
import jetbrains.buildServer.configs.kotlin.v2017_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "c58659de-f5ce-44b2-ab74-0aaa2149b179"
    id = "Orchard"
    parentId = "_Root"
    name = "Orchard"

    vcsRoot(Supersite_CD_RND)
    vcsRoot(Vcs_Orchard_Develop)

    buildType(Orchard_Publish)
    buildType(Orchard_Metrics)
    buildType(Orchard_Sonar)
    buildType(Orchard_Develop)

    features {
        versionedSettings {
            id = "PROJECT_EXT_2"
            mode = VersionedSettings.Mode.ENABLED
            buildSettingsMode = VersionedSettings.BuildSettingsMode.PREFER_SETTINGS_FROM_VCS
            rootExtId = Vcs_Orchard_Develop.id
            showChanges = false
            settingsFormat = VersionedSettings.Format.KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }
})
