package com.rozky.maven.utils

import org.scalatest.{Matchers, FlatSpec}
import java.net.URL
import org.apache.maven.model.Dependency

class PomUtilsSpec extends FlatSpec with Matchers {

    it should "get declared dependencies from a remote pom.xml" in {
        System.setProperty("jsse.enableSNIExtension", "false")

        // given
        val remotePOM = new URL("https://raw.githubusercontent.com/rozky/atp-tennis/master/pom.xml")

        // when
        val declaredDependencies: Seq[Dependency] = PomUtils.getDeclaredDependencies(remotePOM)

        // then
        declaredDependencies.map(_.toString) should contain(
            TestDependency("com.rozky.common", "http-client", "${rozky.http.client.version}").toDependency.toString)
    }
}
