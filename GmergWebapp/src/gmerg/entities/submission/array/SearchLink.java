/**
 * 
 */
package gmerg.entities.submission.array;

/**
 * @author xingjun
 *
 */
public class SearchLink {

    String urlSuffix;
    String urlPrefix;
    String institue;
    String name;
    
    public void setUrlPrefix (String url){
      this.urlPrefix = url;
    }
    public void setUrlSuffix (String url){
      this.urlSuffix = url;
    }
    public void setInstitue (String institue){
      this.institue = institue;
    }
    public void setName (String name){
      this.name = name;
    }
    
    public String getUrl(String param){
      return this.urlPrefix + param + this.urlSuffix;
    }
    public String getInstitue(){
      return this.institue;
    }
    public String getName(){
      return this.name;
    }
}
