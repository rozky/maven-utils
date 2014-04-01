package com.rozky.maven.utils

import java.net.URL

object MvnProjectGraphFactory {

      def create(projectURLs: List[URL]): MvnProjectGraph = {


          val projects: List[MvnProject] = projectURLs.map { projectURL =>
              val pomURL = new URL(projectURL.toString + "/pom.xml")
              MavenUtils.getProject(pomURL)
          }

          MvnProjectGraph(projects)
      }
}
