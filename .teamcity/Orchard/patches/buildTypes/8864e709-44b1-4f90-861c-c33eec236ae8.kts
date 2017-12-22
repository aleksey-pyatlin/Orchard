package Orchard.patches.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2017_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.NUnitStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.nunit
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
    name = "Orchard_Publish"

    params {
        param("Configuration", "Debug")
    }

    vcs {
        root("Orchard_Develop")

    }

    steps {
        script {
            name = "Restore packages"
            enabled = false
            scriptContent = """
                echo "test"
                
                call nuget restore src\Orchard.sln -Source "https://api.nuget.org/v3/index.json"
            """.trimIndent()
        }
        visualStudio {
            name = "Build"
            enabled = false
            path = "src/Orchard.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2015
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V14_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V14_0
        }
        nunit {
            name = "Run Tests"
            enabled = false
            nunitVersion = NUnitStep.NUnitVersion.NUnit_2_5_10
            runtimeVersion = NUnitStep.RuntimeVersion.v4_0
            includeTests = "/src/Orchard.Core.Tests/bin/%Configuration%/Orchard.Core.Tests.dll"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        script {
            name = "Deploy"
            scriptContent = """
                dir "C:\Program Files (x86)\MSBuild\Microsoft\VisualStudio\v11.0"
                deploy.cmd
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        commitStatusPublisher {
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "zxx5237ed1d5ddd9778568f31b2e678d78c03a8eaede92d82f16ffebea53323c001d19456e0eed0717a775d03cbe80d301b"
                }
            }
            param("secure:github_password", "zxxa70075bc751d9d27eca19f64cb355703")
            param("github_username", "aleksey-pyatlin")
        }
    }
}))

