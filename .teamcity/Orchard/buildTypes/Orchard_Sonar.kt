package Orchard.buildTypes

import jetbrains.buildServer.configs.kotlin.v2017_2.*
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.MSBuildStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.msBuild
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2017_2.buildSteps.visualStudio

object Orchard_Sonar : BuildType({
    uuid = "2e4d39c0-66cf-4f3f-aaca-1dc10db44fe1"
    id = "Orchard_Sonar"
    name = "Sonar"
    description = "Runs Sonar Analysis on Orchard.Core"

    params {
        param("Configuration", "Debug")
        password("Sonar.Password", "credentialsJSON:995b1d5c-1804-41d0-800d-f12f59938160", display = ParameterDisplay.HIDDEN)
        param("teamcity.dotCover.home", """C:\teamcity_agent\tools\JetBrains.dotCover.CommandLineTools.bundled""")
    }

    vcs {
        root(Orchard.vcsRoots.Vcs_Orchard_Develop)

    }

    steps {
        script {
            name = "Restore Packages"
            scriptContent = """call nuget restore src\Orchard.sln -Source "https://api.nuget.org/v3/index.json""""
        }
        visualStudio {
            name = "Build Solution"
            path = "src/Orchard.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2017
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V15_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V15_0
            args = "/p:StaticAnalysisEnabled=true"
        }
        powerShell {
            name = "Run All Tests With Coverage"
            scriptMode = script {
                content = """
                    # Only these runners work in Docker container
                    nuget install NUnit.Runners -Version 2.6.1 -OutputDirectory ./src/packages
                    
                    # Clean previous run files
                    if (Test-Path "CoverageReport.html") {
                      Write-Output "Clearing old CoverageReport.html"
                      rm "CoverageReport.html"
                    }
                    if (Test-Path "TestResult.xml") {
                      Write-Output "Clearing old TestResult.xml"
                      rm "TestResult.xml"
                    }
                    
                    # Run tests
                    %teamcity.dotCover.home%\dotCover.exe analyse /Filters="-:module=*Tests*;" /TargetExecutable=".\src\packages\NUnit.Runners.2.6.1\tools\nunit-console.exe" /TargetArguments=".\src\Orchard.Core.Tests\bin\%Configuration%\Orchard.Core.Tests.dll" /TargetWorkingDir=. /Output="CoverageReport.html" /ReportType="HTML"
                """.trimIndent()
            }
        }
        script {
            name = "Initialize Sonar MSBuild Analysis"
            scriptContent = """"c:\Program Files\SonarQube\bin\SonarQube.Scanner.MSBuild.exe" begin /k:"Orchard.Core" /n:"Orchard Core Project" /v:"1.0" /d:sonar.host.url="http://sonardocker:9000" /d:sonar.login=admin /d:sonar.password=%Sonar.Password% /d:sonar.cs.dotcover.reportsPaths="CoverageReport.html" /d:sonar.cs.nunit.reportsPaths="TestResult.xml""""
        }
        msBuild {
            name = "Build Core Project"
            path = "src/Orchard.Web/Core/Orchard.Core.csproj"
            toolsVersion = MSBuildStep.MSBuildToolsVersion.V15_0
            platform = MSBuildStep.Platform.x64
            targets = "Rebuild"
            param("dotNetCoverage.dotCover.home.path", "%teamcity.tool.JetBrains.dotCover.CommandLineTools.DEFAULT%")
        }
        script {
            name = "Run Sonar Analysis and Push Results to Server"
            scriptContent = """"c:\Program Files\SonarQube\bin\SonarQube.Scanner.MSBuild.exe" end /d:sonar.login=admin /d:sonar.password=%Sonar.Password%"""
        }
    }
})
