package it.com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}
import java.net.URL
import org.apache.maven.model.Dependency
import com.rozky.maven.utils.{TestDep, MavenUtils}

class MavenUtilsSpec extends FlatSpec with Matchers {

    it should "get declared dependencies from a remote pom.xml without child modules" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/atp-tennis/master/pom.xml")

        // when
        val declaredDependencies: Seq[Dependency] = MavenUtils.getProjectDeclaredDependencies(remotePOM)

        // then
        declaredDependencies.map(_.toString) should contain(
            TestDep("com.rozky.common", "http-client", "${rozky.http.client.version}").toDependency.toString)
    }

    it should "get declared dependencies from a remote pom.xml with child modules" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/bcomments/master/pom.xml")

        // when
        val declaredDependencies: Seq[Dependency] = MavenUtils.getProjectDeclaredDependencies(remotePOM)

        // then dependencies defined directly in the pom should be returned
        declaredDependencies.map(_.toString) should contain(
            TestDep("org.scala-lang", "scala-library", null).toDependency.toString)

        declaredDependencies.map(_.toString) should contain(
            TestDep("javax.servlet", "servlet-api", "3.0-alpha-1").toDependency.toString)
    }
}
