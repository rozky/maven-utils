package com.rozky.maven.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import java.io.{File, FileReader}
import scala.collection.mutable
import scala.collection.JavaConversions._
import java.net.URL
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils

object MavenUtils {
    val POM_FILE = "pom.xml"
    val pomReader = new MavenXpp3Reader()

    /**
     * Recursively gets all declared dependencies in the supplied pom and its sub modules
     * @param pomURL the URL of a pom.xml to get dependencies for
     * @return the declared dependencies in the pom and its sub modules(if any)
     */
    def getProjectDeclaredDependencies(pomURL: URL): Seq[Dependency] = {
        val pom: Model = PomUtils.parse(pomURL)
        val baseURL = pomURL.toString.replaceAll("/" + POM_FILE, "")

        val subModulesDependencies: List[Dependency] = pom.getModules
            .map(_.asInstanceOf[String])
            .map(module => new URL(s"$baseURL/$module/$POM_FILE"))
            .map(modulePom => getProjectDeclaredDependencies(modulePom))
            .foldLeft(List[Dependency]()) { (result, moduleDependencies) => result ++ moduleDependencies }

        val moduleDependencies: mutable.Buffer[Dependency] = pom.getDependencies
            .map(_.asInstanceOf[Dependency])

        moduleDependencies ++ subModulesDependencies
    }
    
    def getProject(pomURL: URL): MvnProject = {
        val pom: Model = PomUtils.parse(pomURL)
        val baseURL = pomURL.toString.replaceAll("/" + POM_FILE, "")
        val topModule = getProjectModule(pom)

        val subModules = pom.getModules
            .map(_.asInstanceOf[String])
            .map(module => new URL(s"$baseURL/$module/$POM_FILE"))
            .map(modulePomURL => getProjectModule(modulePomURL))

        val moduleIds: Seq[Dependency] = Seq(topModule.id) ++ subModules.map(_.id).toSeq
        val dependencies = (subModules.map(_.dependencies) :+ topModule.dependencies).flatten.distinct
        MvnProject(moduleIds, dependencies)
    }

    def getProjectModule(pomURL: URL): MvnProjectModule = {
        getProjectModule(PomUtils.parse(pomURL))
    }

    def getProjectModule(pom: Model): MvnProjectModule = {
        val moduleDependencies = pom.getDependencies.map(_.asInstanceOf[Dependency])
        val pomId = getPomId(pom)
        MvnProjectModule(pomId, moduleDependencies)
    }

    private def getPomId(pom: Model): Dependency = {
        val id: Dependency = new Dependency()
        id.setArtifactId(pom.getArtifactId)

        if (StringUtils.isBlank(pom.getGroupId) && pom.getParent != null) {
            id.setGroupId(pom.getParent.getGroupId)
        } else {
            id.setGroupId(pom.getGroupId)
        }

        if (StringUtils.isBlank(pom.getVersion) && pom.getParent != null) {
            id.setVersion(pom.getParent.getVersion)
        } else {
            id.setVersion(pom.getVersion)
        }
        id
    }

    private def toString(url: URL): String = {
        val in = url.openStream()
        try {
            IOUtils.toString( in )
        } finally {
            IOUtils.closeQuietly(in)
        }
    }
}
