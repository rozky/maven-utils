package com.rozky.maven.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import java.io.{InputStream, InputStreamReader, File, FileReader}
import scala.collection.mutable
import scala.collection.JavaConversions._
import scala.xml.XML
import java.net.URL

class MavenUtils {
    val POM_FILE = "pom.xml"
    val pomReader = new MavenXpp3Reader()

    def getDeclaredDependencies(url: URL): Seq[Dependency] = {

        var is: InputStream = null

        try {
            is = url.openStream()
            val pomModel: Model = pomReader.read(new InputStreamReader(is))
            pomModel.getDependencies.map(_.asInstanceOf[Dependency])
        } finally {
            if (is != null) {
                is.close()
            }
        }
    }

    private def getDeclaredDependencies(pomHome: String, filter: Dependency => Boolean): Seq[Dependency] = {

        XML.load(new URL(""))

        val pomFile: String = s"$pomHome/$POM_FILE"
        val pomModel: Model = pomReader.read(new FileReader(new File(pomFile)))

        val subModulesDependencies: List[Dependency] = pomModel.getModules
            .map(_.asInstanceOf[String])
            .map(module => s"$pomHome/$module")
            .map(moduleHome => getDeclaredDependencies(moduleHome, filter))
            .foldLeft(List[Dependency]()) { (result, moduleDependencies) => result ++ moduleDependencies }

        val moduleDependencies: mutable.Buffer[Dependency] = pomModel.getDependencies
            .map(_.asInstanceOf[Dependency])
            .filter(filter)

        moduleDependencies ++ subModulesDependencies
    }

    case class MvnProject(gitRepositoryUrl: String,
                          jenkinsJobUrl: String,
                          dependencies: List[Dependency],
                          dependsOn: List[Dependency])
}
