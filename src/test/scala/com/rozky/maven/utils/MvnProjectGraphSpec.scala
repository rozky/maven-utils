package com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}

class MvnProjectGraphSpec extends FlatSpec with Matchers {

    it should "checks if one project is parent of a other project" in {
        // given
        val graph: MvnProjectGraph = MvnProjectGraph(List(
            TestProject(List("0"), List()),
            TestProject(List("1"), List("0")),
            TestProject(List("2"), List()),
            TestProject(List("3"), List("1"))
        ))

        // when
        graph.getNode(graph.projects(0)).isParentOf(graph.projects(1)) should be(true)
        graph.getNode(graph.projects(0)).isParentOf(graph.projects(3)) should be(true)
        graph.getNode(graph.projects(1)).isParentOf(graph.projects(3)) should be(true)
        graph.getNode(graph.projects(1)).isParentOf(graph.projects(0)) should be(false)
        graph.getNode(graph.projects(1)).isParentOf(graph.projects(2)) should be(false)
        graph.getNode(graph.projects(0)).isParentOf(graph.projects(2)) should be(false)
        graph.getNode(graph.projects(2)).isParentOf(graph.projects(0)) should be(false)
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
            TestProject(List("1"), List("0")),
            TestProject(List("2"), List()),
            TestProject(List("3"), List("1"))
        ))

        // when
        graph.getChildrenOf(graph.projects(0)) should be(List(graph.projects(1), graph.projects(3)))
        graph.getDirectChildrenOf(graph.projects(1)) should be(List(graph.projects(3)))
        graph.getDirectChildrenOf(graph.projects(2)) should be(List())
    }
}
