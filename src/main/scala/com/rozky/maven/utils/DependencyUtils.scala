package com.rozky.maven.utils

import org.apache.maven.model.Dependency

object DependencyUtils {
    def equal(left: Dependency, right: Dependency): Boolean = {
        try {
            left.getGroupId.equals(right.getGroupId) &&
                left.getArtifactId.equals(right.getArtifactId) &&
                left.getVersion.equals(right.getVersion)
        } catch {
            case e: Exception => false
        }
    }
}
