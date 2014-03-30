package com.rozky.maven.utils

case class MvnProjectGraph(projects: Seq[MvnProject]) {
    require(projects != null, "projects can't be null")

    val nodes: Map[MvnProject, GraphNode] = projects.map { project =>
        (project, GraphNode(project, project.filterDependants(projects)))
    }.toMap

    def getDirectChildrenOf(project: MvnProject): Seq[MvnProject] = {
        val node: Option[GraphNode] = nodes.get(project)
        if (node.isDefined) {
            node.get.childProjects
        } else {
            throw new IllegalArgumentException("unknown project: " + project)
        }
    }

    def getChildrenOf(project: MvnProject): Seq[MvnProject] = {
        val node: Option[GraphNode] = nodes.get(project)
        if (node.isDefined) {
            node.get.childProjects ++ node.get.childProjects.map(child => getChildrenOf(child)).flatten
        } else {
            throw new IllegalArgumentException("unknown project: " + project)
        }
    }

    def getNode(project: MvnProject): GraphNode = {
        val node: Option[GraphNode] = nodes.get(project)
        if (node.isDefined) {
            node.get
        } else {
            throw new IllegalArgumentException("unknown project: " + project)
        }
    }

    case class GraphNode(project: MvnProject, childProjects: Seq[MvnProject] = List()) {
        require(childProjects != null, "child project can't be null")

        // todo - should not call getNode from wrapping type
        def isParentOf(project: MvnProject): Boolean = {
            if (childProjects.contains(project)) {
                true
            } else {
                childProjects.map(p => getNode(p).isParentOf(project)).exists(_.equals(true))
            }
        }
    }
}
