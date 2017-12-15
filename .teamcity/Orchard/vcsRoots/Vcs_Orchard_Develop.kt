package Orchard.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object Vcs_Orchard_Develop : GitVcsRoot({
    uuid = "64256b05-9d45-4726-ba53-4e9a64bf2e2e"
    id = "Orchard_Develop"
    name = "Orchard_Develop"
    url = "git@github.com:aleksey-pyatlin/Orchard.git"
    branch = "dev"
    branchSpec = """
        +:refs/heads/*
        +:refs/pull/*/head
        +:refs/pull/(*/merge)
    """.trimIndent()
    authMethod = uploadedKey {
        uploadedKey = "Orchard_rsa"
    }
})
