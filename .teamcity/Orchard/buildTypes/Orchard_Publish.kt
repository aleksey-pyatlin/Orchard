package Orchard.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.MSBuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.msBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Orchard_Publish : BuildType({
    uuid = "8864e709-44b1-4f90-861c-c33eec236ae8"
    id = "Orchard_Publish"
    name = "Supersite_Publish"

    params {
        param("build_configuration", "Release")
        password("msdeploy_pass", "credentialsJSON:ad827cce-a116-4bbc-a443-c54dcaf6b8b7")
        param("publish_profile", "DemoHosterDocker")
    }

    vcs {
        root(Orchard.vcsRoots.Supersite_CD_RND)

    }

    steps {
        script {
            name = "Ping"
            scriptContent = "ping dotnetdocker"
        }
        script {
            name = "Copy connection strings config from template"
            scriptContent = """copy Aura.Supersite.WebHost\Connections.config.local.template Aura.Supersite.WebHost\Connections.config.local /Y"""
        }
        script {
            name = "Restore nuget packages and dependencies"
            scriptContent = """call "nuget.exe" restore "%system.teamcity.build.workingDir%\Aura.Supersite.sln""""
        }
        visualStudio {
            name = "Build solution"
            path = "Aura.Supersite.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2015
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V14_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V14_0
            configuration = "%build_configuration%"
        }
        msBuild {
            name = "Deploy project"
            path = """Aura.Supersite.WebHost\Aura.Supersite.WebHost.csproj"""
            version = MSBuildStep.MSBuildVersion.V14_0
            toolsVersion = MSBuildStep.MSBuildToolsVersion.V14_0
            args = """
                /p:DeployOnBuild=true /p:PublishProfile=%publish_profile%.pubxml  /p:Configuration=%build_configuration%
                /p:Password=%msdeploy_pass%
                /p:AllowUntrustedCertificate=true
            """.trimIndent()
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }
})
