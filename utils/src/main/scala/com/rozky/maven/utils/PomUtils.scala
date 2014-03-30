package com.rozky.maven.utils

import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import java.io.{InputStream, InputStreamReader}
import scala.collection.JavaConversions._
import java.net.URL
import org.apache.commons.io.IOUtils

object PomUtils {
    val pomReader = new MavenXpp3Reader()

    def parse(url: URL): Model = {
        val is: InputStream = url.openStream()

        try {
            pomReader.read(new InputStreamReader(is))
        } finally {
            IOUtils.closeQuietly(is)
        }
    }

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
}
