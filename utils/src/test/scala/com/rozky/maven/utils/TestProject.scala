package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class TestProject(names: List[String], dependencies: List[String]) {
    def toMvnProject: MvnProject = TestProject.asMvnProject(this)
}

object TestProject {
    implicit def asMvnProject(project: TestProject): MvnProject = {
        val artifacts: List[Dependency] = project.names.map(p => TestDep.create(p).toDependency)
        val dependencies: List[Dependency] = project.dependencies.map(p => TestDep.create(p).toDependency)
        MvnProject(artifacts, dependencies)
    }
}
