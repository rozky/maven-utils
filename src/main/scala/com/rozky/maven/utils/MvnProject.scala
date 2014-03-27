package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class MvnProject(ownArtifacts: Seq[Dependency], dependencies: Seq[Dependency]) {
    require(ownArtifacts != null && !ownArtifacts.isEmpty, "at least one own artifact is required")

    def isDependentOn(project: MvnProject): Boolean = {
        if (dependencies != null && !dependencies.isEmpty) {
            !dependencies.filter(dep => project.ownArtifacts.exists(DependencyUtils.equal(_, dep))).isEmpty
        } else {
            false
        }
    }

    def filterDependants(projects: Seq[MvnProject]): Seq[MvnProject] = {
        projects.filter(p => p.isDependentOn(this))
    }


}
