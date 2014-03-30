package com.rozky.maven.utils

import org.apache.maven.model.Dependency

case class MvnProjectModule(id: Dependency, dependencies: Seq[Dependency])
