package Orchard.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.vcs.GitVcsRoot

object Supersite_CD_RND : GitVcsRoot({
    uuid = "e59171e9-64f8-49e9-98c5-04d9c18d5008"
    id = "Supersite_CD_RND"
    name = "Supersite_CD_R&D"
    url = "https://git.itransition.com/scm/supersitev/supersitev5common.git"
    branch = "refs/heads/cd-rnd"
    authMethod = password {
        userName = "a.pyatlin"
        password = "credentialsJSON:37e2130f-8329-4873-b7ea-5c8a502fb51e"
    }
})
