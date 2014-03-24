package com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}
import java.net.URL
import org.apache.maven.model.Dependency

class MavenUtilsSpec extends FlatSpec with Matchers {

    val utils = new MavenUtils

    it should "get declared dependencies from remote pom.xml" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePom = new URL("https://raw.githubusercontent.com/rozky/atp-tennis/master/pom.xml")

        // when
        val declaredDependencies: Seq[Dependency] = utils.getDeclaredDependencies(remotePom)

        // then
        declaredDependencies.map(_.toString) should contain(
            TestDependency("com.rozky.common", "http-client", "${rozky.http.client.version}").toDependency.toString)
    }
}
