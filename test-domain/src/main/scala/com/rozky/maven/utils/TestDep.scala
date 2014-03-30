package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class TestDep(groupId: String, artifactId: String, version: String = "1.0", artifactType: String = "jar")  {
    def toDependency: Dependency = {
        TestDep.asDependency(this)
    }
}
object TestDep {

    def topDependency = TestDep("com.test", "at-top", "1.0")

    def create(name: String): TestDep = TestDep(name, name)

    implicit def asDependency(testDependency: TestDep): Dependency = {
        val dependency: Dependency = new Dependency
        dependency.setGroupId(testDependency.groupId)
        dependency.setArtifactId(testDependency.artifactId)
        dependency.setVersion(testDependency.version)
        dependency.setType(testDependency.artifactType)
        dependency
    }
}