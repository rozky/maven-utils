package com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}

class MvnProjectGraphSpec extends FlatSpec with Matchers {

    it should "checks if one project is parent of a other project" in {
        // given
        val graph: MvnProjectGraph = MvnProjectGraph(List(
            TestProject(List("0"), List()),
            TestProject(List("1"), List("0", "-1")),
            TestProject(List("2"), List("-1")),
            TestProject(List("3"), List("1", "-1"))
        ))

        // when
        graph.isParent(graph.projects(0), graph.projects(1)) should be(true)
        graph.isParent(graph.projects(0), graph.projects(3)) should be(true)
        graph.isParent(graph.projects(1), graph.projects(3)) should be(true)
        graph.isParent(graph.projects(1), graph.projects(0)) should be(false)
        graph.isParent(graph.projects(1), graph.projects(2)) should be(false)
        graph.isParent(graph.projects(0), graph.projects(2)) should be(false)
        graph.isParent(graph.projects(2), graph.projects(0)) should be(false)

      graph.print()
    }

    it should "get direct children of a project" in {
        // given
        val graph: MvnProjectGraph = MvnProjectGraph(List(
            TestProject(List("0"), List()),
            TestProject(List("1"), List("0")),
            TestProject(List("2"), List()),
            TestProject(List("3"), List("1"))
        ))

        // when
        graph.getDirectChildrenOf(graph.projects(0)) should be(List(graph.projects(1)))
        graph.getDirectChildrenOf(graph.projects(2)) should be(List())
    }

    it should "get all children of a project" in {
        // given
        val graph: MvnProjectGraph = MvnProjectGraph(List(
            TestProject(List("0"), List()),
            TestProject(List("1"), List("0", "-1")),
            TestProject(List("2"), List()),
            TestProject(List("3"), List("1", "-1"))
        ))

        // when
        graph.getChildrenOf(graph.projects(0)) should be(List(graph.projects(1), graph.projects(3)))
        graph.getDirectChildrenOf(graph.projects(1)) should be(List(graph.projects(3)))
        graph.getDirectChildrenOf(graph.projects(2)) should be(List())
    }
}
