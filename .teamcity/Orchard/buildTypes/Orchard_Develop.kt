package Orchard.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.NUnitStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.nunit
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.vcs

object Orchard_Develop : BuildType({
    uuid = "a1eff9e2-92dc-4432-9e54-2858b4aa7815"
    id = "Orchard_Develop"
    name = "Orchard_Develop"

    params {
        param("Configuration", "Debug")
    }

    vcs {
        root(Orchard.vcsRoots.Vcs_Orchard_Develop)

    }

    steps {
        script {
            name = "Restore packages"
            scriptContent = """call nuget restore src\Orchard.sln -Source "https://api.nuget.org/v3/index.json""""
        }
        visualStudio {
            name = "Build"
            path = "src/Orchard.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2015
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V14_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V14_0
            args = "/p:StaticAnalysisEnabled=true"
        }
        nunit {
            name = "Run Tests"
            nunitVersion = NUnitStep.NUnitVersion.NUnit_2_5_10
            runtimeVersion = NUnitStep.RuntimeVersion.v4_0
            includeTests = "/src/Orchard.Core.Tests/bin/%Configuration%/Orchard.Core.Tests.dll"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
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
})
