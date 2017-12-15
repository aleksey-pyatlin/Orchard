package Orchard.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.NUnitStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.nunit
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.v2017_2.triggers.schedule

object Orchard_Metrics : BuildType({
    uuid = "20e0f37b-07c3-4f24-ac32-ace119c349cd"
    id = "Orchard_Metrics"
    name = "Orchard_Metrics"

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
        step {
            type = "dotnet-tools-inspectcode"
            enabled = false
            param("dotnet-tools-inspectcode.solution", "src/Orchard.sln")
            param("dotnet-tools-inspectcodeCustomSettingsProfile", "src/Orchard.5.0.ReSharper")
            param("dotnet-tools-inspectcode.project.filter", "Orchard.Web")
            param("TargetDotNetFramework_4.5.2", "true")
            param("jetbrains.resharper-clt.clt-path", "%teamcity.tool.jetbrains.resharper-clt.DEFAULT%")
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
            name = "Run All Tests With Coverage"
            nunitVersion = NUnitStep.NUnitVersion.NUnit_2_5_10
            runtimeVersion = NUnitStep.RuntimeVersion.v4_0
            includeTests = "/src/Orchard.Core.Tests/bin/%Configuration%/Orchard.Core.Tests.dll"
            coverage = dotcover {
                toolPath = "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%"
                assemblyFilters = "-:*Tests*"
            }
        }
    }

    triggers {
        schedule {
            schedulingPolicy = daily {
                hour = 1
            }
            triggerBuild = always()
            param("revisionRule", "lastFinished")
            param("dayOfWeek", "Sunday")
        }
    }
})
