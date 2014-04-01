package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class MvnProject(moduleIds: Seq[Dependency], dependencies: Seq[Dependency]) {
    require(moduleIds != null && !moduleIds.isEmpty, "at least one module id is required")

    def id: String = moduleIds.head.getGroupId

    def isDependentOn(project: MvnProject): Boolean = {
        if (dependencies != null && !dependencies.isEmpty) {
            !dependencies.filter(dep => project.moduleIds.exists(DependencyUtils.equal(_, dep))).isEmpty
        } else {
            false
        }
    }

    def filterDependants(projects: Seq[MvnProject]): Seq[MvnProject] = {
        projects.filter(p => p.isDependentOn(this))
    }
}
