import sbt._

class LuceneDemoProject(info: ProjectInfo) extends DefaultProject(info) {
    val lucene = "org.apache.lucene" % "lucene-core" % "3.1.0"
    val luceneHighlighter = "org.apache.lucene" % "lucene-highlighter" % "3.1.0"
}
