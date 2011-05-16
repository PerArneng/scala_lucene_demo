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
package com.scalebit.lucenedemo

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field}
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig};
import org.apache.lucene.queryParser.{ParseException, QueryParser};
import org.apache.lucene.search.{IndexSearcher, Query, TopScoreDocCollector};
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.search.highlight.{SimpleFragmenter, QueryScorer,
                                           SimpleHTMLFormatter, Highlighter}
import org.apache.lucene.analysis.{TokenStream, Analyzer}
import java.io.{IOException, File, StringReader};

object LuceneDemo {

    def listFiles(file : File) : List[File] = {
		if (file.isDirectory) {
			file.listFiles.toList.map(listFiles).flatten
		} else {
			List(file)
		}
	}

	def main(args : Array[String]) : Unit = {
        
        if (args.length < 2) {
            System.err.println("\033[31millegal nr of arguments")
            println("\033[37musage: lucene_demo <directory> <searchstring>")
            System.exit(1)
        }

        val dir      = args(0)
        val querystr = args(1)

        val defaultColumn = "absolute_path"
        val version       = Version.LUCENE_31
        val analyzer      = new StandardAnalyzer(version);
        val writerConfig  = new IndexWriterConfig(version, analyzer)
        val hitsPerPage   = 100;        

        val directory   = new RAMDirectory(); 
        val indexWriter = new IndexWriter(directory, writerConfig)
       
        listFiles(new File(dir)).foreach { file =>
            val doc = new Document()
            doc.add(new Field(defaultColumn, file.getAbsolutePath, 
                    Field.Store.YES, Field.Index.ANALYZED))    
            indexWriter.addDocument(doc)
        }
        
        indexWriter.close();
        
        val queryParser = new QueryParser(version, defaultColumn, analyzer)
        val query       = queryParser.parse(querystr)
        
        val searcher  = new IndexSearcher(directory, true);
        val collector = TopScoreDocCollector.create(hitsPerPage, true);

        searcher.search(query, collector);
        
        collector.topDocs().scoreDocs.toList.foreach { scoreDoc =>
            val doc = searcher.doc(scoreDoc.doc)
            println(highlight(analyzer, query, doc.get(defaultColumn), 
                              defaultColumn))
        }

	}

    def highlight(analyser : Analyzer, query : Query, text : String, 
                  column : String) : String = {

        val scorer      = new QueryScorer(query);
        val formatter   = new SimpleHTMLFormatter("\033[33m", "\033[37m");
        val highlighter = new Highlighter(formatter, scorer);
        highlighter.setTextFragmenter(new SimpleFragmenter(1));
        val stream = analyser.tokenStream(column, new StringReader(text));
        return highlighter.getBestFragments(stream, text, 100, "");
    }
}
