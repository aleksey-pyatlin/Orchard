package Orchard.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '2e4d39c0-66cf-4f3f-aaca-1dc10db44fe1' (id = 'Orchard_Sonar')
accordingly and delete the patch script.
*/
changeBuildType("2e4d39c0-66cf-4f3f-aaca-1dc10db44fe1") {
    params {
        add {
            param("teamcity.dotCover.home", """C:\teamcity_agent\tools\JetBrains.dotCover.CommandLineTools.bundled""")
        }
    }
}