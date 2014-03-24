package com.rozky.maven.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import java.io.{File, FileReader}
import scala.collection.mutable
import scala.collection.JavaConversions._
import java.net.URL
import org.apache.commons.io.IOUtils

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

    private def toString(url: URL): String = {
        val in = url.openStream()
        try {
            IOUtils.toString( in )
        } finally {
            IOUtils.closeQuietly(in)
        }
    }
}
