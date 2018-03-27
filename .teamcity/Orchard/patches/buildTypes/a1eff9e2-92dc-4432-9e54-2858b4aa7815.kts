package Orchard.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with uuid = 'a1eff9e2-92dc-4432-9e54-2858b4aa7815' (id = 'Orchard_Develop')
accordingly and delete the patch script.
*/
changeBuildType("a1eff9e2-92dc-4432-9e54-2858b4aa7815") {
    check(paused == false) {
        "Unexpected paused: '$paused'"
    }
    paused = true
}
