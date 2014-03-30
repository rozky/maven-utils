package it.com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}
import java.net.URL
import org.apache.maven.model.Dependency
import com.rozky.maven.utils.{MvnProject, TestDep, MavenUtils}

class MavenUtilsSpec extends FlatSpec with Matchers {

    it should "create mvn project from a remote pom.xml which has no child modules" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/atp-tennis/master/pom.xml")

        // when
        val project: MvnProject = MavenUtils.getProject(remotePOM)

        // then
        project.moduleIds.map(_.toString) should contain(
            TestDep("com.rozky.gambling.websites", "atp-tennis", "1.0-SNAPSHOT").toDependency.toString)

        project.dependencies.map(_.toString) should contain(
            TestDep("com.rozky.common", "http-client", "${rozky.http.client.version}").toDependency.toString)
    }


    it should "create mvn project from a remote pom.xml which has child modules" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/bcomments/master/pom.xml")

        // when
        val project: MvnProject = MavenUtils.getProject(remotePOM)

        // then
        val modules: Seq[String] = project.moduleIds.map(_.toString)
        modules should contain(TestDep("com.rlimited.bcomments", "parent", "1.0.0.SNAPSHOT").toDependency.toString)
        modules should contain(TestDep("com.rlimited.bcomments", "model", "1.0.0.SNAPSHOT").toDependency.toString)
        modules.size should be(5)

        // then
        val dependencies: Seq[String] = project.dependencies.map(_.toString)
        dependencies should contain(
            TestDep("org.scala-lang", "scala-library", null).toDependency.toString)

        dependencies should contain(
            TestDep("javax.servlet", "servlet-api", "3.0-alpha-1").toDependency.toString)
    }

    it should "get declared dependencies from a remote pom.xml without child modules" ignore {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/atp-tennis/master/pom.xml")

        // when
        val declaredDependencies: Seq[Dependency] = MavenUtils.getProjectDeclaredDependencies(remotePOM)

        // then
        declaredDependencies.map(_.toString) should contain(
            TestDep("com.rozky.common", "http-client", "${rozky.http.client.version}").toDependency.toString)
    }

    it should "get declared dependencies from a remote pom.xml with child modules" ignore {
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
