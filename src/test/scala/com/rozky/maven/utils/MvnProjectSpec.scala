package com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}

class MvnProjectSpec extends FlatSpec with Matchers {

    it should "reject to create an instance if own artifacts are null" in {
        val exception = intercept[IllegalArgumentException] {
            MvnProject(null, null)
        }

        exception.getMessage should be("requirement failed: at least one own artifact is required")
    }

    it should "depend on a another project if at least one of other project own artifact is listed in dependencies" in {
        // given
        val topProject: MvnProject = MvnProject(List(TestDep.topDependency), List())

        // when
        val isDependant: Boolean = MvnProject(List(TestDep("com.test", "test")), List(TestDep.topDependency)).isDependentOn(topProject)

        // then
        isDependant should be(true)
    }

    it should "not depend on a another project if none of other project own artifact is listed in dependencies" in {
        // given
        val topProject: MvnProject = MvnProject(List(TestDep.topDependency), List())

        // when
        val isDependant: Boolean = MvnProject(List(TestDep("com.test", "test")), List()).isDependentOn(topProject)

        // then
        isDependant should be(false)
    }

    it should "filter dependant projects and return only projects that depends on the project" in {
        // given
        val l1Artifact: TestDep = TestDep("com.test", "level-1", "1.0")
        val l2P1Artifact: TestDep = TestDep("com.test.project-1", "level-2", "1.0")
        val l2P2Artifact: TestDep = TestDep("com.test.project-2", "level-2", "1.0")

        val l1Project: MvnProject = MvnProject(List(l1Artifact), List())
        val l2Project1: MvnProject = MvnProject(List(l2P1Artifact), List(l1Artifact))
        val l2Project2: MvnProject = MvnProject(List(l2P2Artifact), List())

        val allProjects = List(l1Project, l2Project1, l2Project2)

        // when
        val dependantProjects: Seq[MvnProject] = l1Project.filterDependants(allProjects)

        // then
        dependantProjects should be(List(l2Project1))
    }
}
