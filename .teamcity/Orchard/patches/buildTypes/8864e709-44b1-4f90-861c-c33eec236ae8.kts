package Orchard.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.MSBuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.msBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2017_2.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, create a buildType with uuid = '8864e709-44b1-4f90-861c-c33eec236ae8' (id = 'Orchard_Publish')
in the project with uuid = 'c58659de-f5ce-44b2-ab74-0aaa2149b179' and delete the patch script.
*/
create("c58659de-f5ce-44b2-ab74-0aaa2149b179", BuildType({
    uuid = "8864e709-44b1-4f90-861c-c33eec236ae8"
    id = "Orchard_Publish"
    name = "Supersite_Publish"

    params {
        param("build_configuration", "Release")
    }

    vcs {
        root("Supersite_CD_RND")

    }

    steps {
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
            args = "/p:DeployOnBuild=true /p:PublishProfile=%publish_profile%.pubxml  /p:Password=%msdeploy_pass% /p:Configuration=%build_configuration% /p:AllowUntrustedCertificate=true"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }
}))

