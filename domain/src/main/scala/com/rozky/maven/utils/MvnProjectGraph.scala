package com.rozky.maven.utils

import org.jgrapht.DirectedGraph
import org.jgrapht.graph.SimpleDirectedGraph
import com.rozky.maven.utils.MvnProjectGraph.Edge
import org.jgrapht.alg.DijkstraShortestPath
import java.util
import scala.collection.JavaConversions._

// todo - detect cyclying dependencies
case class MvnProjectGraph(projects: Seq[MvnProject]) {
    require(projects != null, "projects can't be null")

    private val graph: DirectedGraph[MvnProject, Edge] = new SimpleDirectedGraph[MvnProject, Edge](classOf[Edge])

    buildGraph()

    def getDirectChildrenOf(project: MvnProject): Seq[MvnProject] = {
      graph.outgoingEdgesOf(project).map(edge => edge.target).toSeq
    }

    def getChildrenOf(project: MvnProject): Seq[MvnProject] = {
      val children: Seq[MvnProject] = getDirectChildrenOf(project)
      children ++ children.map(child => getChildrenOf(child)).flatten
    }

    def findPath(source: MvnProject, target: MvnProject): util.List[Edge] = {
        DijkstraShortestPath.findPathBetween(graph, source, target)
    }
    
    def isParent(parent: MvnProject, child: MvnProject): Boolean = {
        def path = findPath(parent, child)
        path != null && !path.isEmpty
    }

    def print() {
        def toString(edge: Edge): String = s"[ ${edge.source.id} -> ${edge.target.id} ]"

        println(s"Graph { ${graph.edgeSet().map(toString).mkString(", ")} }")
    }

    private def buildGraph() {
        projects.foreach { project =>
            graph.addVertex(project)
            project.filterDependants(projects).foreach { dependentProject =>
                addEdge(project, dependentProject)
            }
        }
    }
    
    private def addEdge(source: MvnProject, target: MvnProject) {
        graph.addVertex(source)
        graph.addVertex(target)
        graph.addEdge(source, target, Edge(source, target))
    } 
}

object MvnProjectGraph {
    case class Edge(source: MvnProject, target: MvnProject)
}
