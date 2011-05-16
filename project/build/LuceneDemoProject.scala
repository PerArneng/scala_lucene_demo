/* ========================================================================
 * Copyright 2011 Per Arneng (Scalebit AB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================
 */
import sbt._

class LuceneDemoProject(info: ProjectInfo) extends DefaultProject(info) {
    val lucene = "org.apache.lucene" % "lucene-core" % "3.1.0"
    val luceneHighlighter = "org.apache.lucene" % "lucene-highlighter" % "3.1.0"
}
