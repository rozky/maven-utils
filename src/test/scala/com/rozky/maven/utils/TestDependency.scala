package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class TestDependency(groupId: String, artifactId: String, version: String, artifactType: String = "jar")  {
    def toDependency: Dependency = {
        TestDependency.asDependency(this)
    }
}
object TestDependency {
    implicit def asDependency(testDependency: TestDependency): Dependency = {
        val dependency: Dependency = new Dependency
        dependency.setGroupId(testDependency.groupId)
        dependency.setArtifactId(testDependency.artifactId)
        dependency.setVersion(testDependency.version)
        dependency.setType(testDependency.artifactType)
        dependency
    }
}

