package gmerg.sample.lucene.index;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import gmerg.sample.lucene.util.HTMLDocParser;
import gmerg.sample.lucene.util.REF_PROBE;
import gmerg.sample.lucene.util.XMLDocParser;

/**
 * This class is used to create index for html files
 *
 */
public class IndexManager {
	
	//the directory that stores html files
    private final String dataDir  = "/export/data0/qq/lucene/dbsource/db/";
    
    //the directory that is used to store lucene index
    private final String indexDir = "/export/data0/qq/lucene/dbsource/indices/";
    
    /**
     * create index
     */
    public boolean createIndex() throws IOException{
    	if(true == ifIndexExist()){
    	    return true;	
    	}
    	File dir = new File(dataDir);
    	if(!dir.exists()){
    		return false;
    	}
    	File[] htmls = dir.listFiles();
    	Directory fsDirectory = FSDirectory.getDirectory(indexDir, true);
    	Analyzer  analyzer    = new StandardAnalyzer();
    	IndexWriter indexWriter = new IndexWriter(fsDirectory, analyzer, true);
    	for(int i = 0; i < htmls.length; i++){
    		String htmlPath = htmls[i].getAbsolutePath();
   
    		if(htmlPath.endsWith(".html") || htmlPath.endsWith(".htm")){
        		addDocument(htmlPath, indexWriter);
    		} else if(htmlPath.endsWith(".xml")) {
    			addXMLDocument(htmlPath, indexWriter);
    		}
    	}
    	indexWriter.optimize();
    	indexWriter.close();
    	return true;
    	
    }
    
    /**
     * Add one document to the lucene index
     */
    public void addDocument(String htmlPath, IndexWriter indexWriter){
    	HTMLDocParser htmlParser = new HTMLDocParser(htmlPath);
    	String path    = htmlParser.getPath();
    	String title   = htmlParser.getTitle();
    	Reader content = htmlParser.getContent();
    	
    	Document document = new Document();
    	document.add(new Field("path",path,Field.Store.YES,Field.Index.NO));
    	document.add(new Field("title",title,Field.Store.YES,Field.Index.TOKENIZED));
    	document.add(new Field("content",content));
    	try {
			indexWriter.addDocument(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Add xml document to the lucene index
     */
    public void addXMLDocument(String xmlPath, IndexWriter indexWriter){    	
    	XMLDocParser xmlParser = new XMLDocParser(xmlPath);
    	try {
    		
	    	Document contactDocument  = null;
	    	REF_PROBE probe;
	    	ArrayList<REF_PROBE> probes = (ArrayList<REF_PROBE>)xmlParser.getProbes();
	    	System.out.println("probes:"+probes.size());
    		for(int i = 0; i < probes.size(); i++) {
    			probe = null;
    			probe = (REF_PROBE)probes.get(i);
		    	contactDocument  = new Document();
		    	if(null != probe.getRPR_MTF()) {
		    	contactDocument.add(new Field("RPR_MTF;", probe.getRPR_MTF(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_MTF_JAX", probe.getRPR_MTF_JAX(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_JAX_ACC", probe.getRPR_JAX_ACC(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_SYMBOL", probe.getRPR_SYMBOL(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_NAME", new StringReader(probe.getRPR_NAME())));
		    	contactDocument.add(new Field("RPR_SYNONYMS", new StringReader(probe.getRPR_SYNONYMS())));
		    	contactDocument.add(new Field("RPR_GENE_ID", probe.getRPR_GENE_ID(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_LOCUS_TAG;", probe.getRPR_LOCUS_TAG(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_GENBANK", probe.getRPR_GENBANK(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_ENSEMBL", probe.getRPR_ENSEMBL(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_UNIGENE", probe.getRPR_UNIGENE(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_5_PRIMER", probe.getRPR_5_PRIMER(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_3_PRIMER", probe.getRPR_3_PRIMER(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_5_ADAPTOR", probe.getRPR_5_ADAPTOR(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_3_ADAPTOR", probe.getRPR_3_ADAPTOR(), Field.Store.YES,Field.Index.TOKENIZED));
		    	contactDocument.add(new Field("RPR_TYPE", probe.getRPR_TYPE(), Field.Store.YES,Field.Index.TOKENIZED));
		    	
		    	/*Contact contact;
	    		ArrayList<Contact> contacts = (ArrayList<Contact>)xmlParser.getContacts();
	    		for(int i = 0; i < contacts.size(); i++) {
		    		contact = null;
		    		contact = (Contact)contacts.get(i);
			    	contactDocument  = new Document();
			    	contactDocument.add(new Field("name", contact.getName(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("address", contact.getAddress(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("city", contact.getCity(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("province", contact.getProvince(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("postalcode", contact.getPostalcode(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("country", contact.getCountry(), Field.Store.YES,Field.Index.TOKENIZED));
			    	contactDocument.add(new Field("telephone", contact.getTelephone(), Field.Store.YES,Field.Index.TOKENIZED));*/
		    	indexWriter.addDocument(contactDocument);
		    	}
	    	}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }    
    
    /**
     * judge if the index is already exist
     */
    public boolean ifIndexExist(){
        File directory = new File(indexDir);
        if(0 < directory.listFiles().length){
        	return true;
        }else{
        	return false;
        }
    }
    
    public String getDataDir(){
    	return this.dataDir;
    }
    
    public String getIndexDir(){
    	return this.indexDir;
    }
        
}
