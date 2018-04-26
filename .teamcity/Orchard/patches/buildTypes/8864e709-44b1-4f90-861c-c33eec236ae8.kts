package Orchard.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = '8864e709-44b1-4f90-861c-c33eec236ae8' (id = 'Orchard_Publish')
accordingly and delete the patch script.
*/
changeBuildType("8864e709-44b1-4f90-861c-c33eec236ae8") {
    check(name == "Supersite_Publish") {
        "Unexpected name: '$name'"
    }
    name = "Deploy_Demohoster_Self-Contained"

    vcs {
        remove("Supersite_CD_RND")
    }
}
