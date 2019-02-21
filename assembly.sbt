runMain in Compile <<= Defaults.runMainTask(fullClasspath in Compile, runner in (Compile, run))

run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

assembleArtifact in packageScala := false

mergeStrategy in assembly <<= (mergeStrategy in assembly) {
  (old) => {
    case "plugin.properties" => MergeStrategy.discard
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => old(x)
  }
}

test in assembly := {} // skip test in assembly
