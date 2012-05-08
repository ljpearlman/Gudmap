package gmerg.control;

import gmerg.assemblers.TrackingIPAssembler;
import gmerg.utils.CookieOperations;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GudmapTrackingIPFilter implements Filter{
	private static final String[] pages = { "database_homepage.html", 
		  "gene.html",
		  "focus_quick_submissions.html",
		  "focus_gene_browse.html",
		  "focus_gene_index_browse.html",
		  "edit_entry_page_per_lab.html",
		  "analysis.html",
		  "mic_submission.html",
		  "ish_submission.html"} ;
	
	private static final String[] ips = { "127.0.0.1", 
		  "192.168.",
		  "193.63.",
		"129.215." } ;
	

	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			trackingIP(httpServletRequest);
		}
		filterChain.doFilter(request, response);
	}

	public void trackingIP(HttpServletRequest httpServletRequest) {
		String appName = CookieOperations.getCookieValue("appName", "", httpServletRequest);
		String appVersion = CookieOperations.getCookieValue("appVersion", "", httpServletRequest);
		String userAgent = CookieOperations.getCookieValue("userAgent", "", httpServletRequest);
		String platform = CookieOperations.getCookieValue("platform", "", httpServletRequest);
		//System.out.println("VIEW ID:"+httpServletRequest.getServletPath());
		//System.out.println("REMOTE IP:"+httpServletRequest.getRemoteAddr());
		String key = appName+appVersion+userAgent+platform;
		//System.out.println("BROWSER:"+lookupBrowser(key)+":"+lookupPlatform(key));	
		
		if(lookup(httpServletRequest.getServletPath(), pages) && !lookup(httpServletRequest.getRemoteAddr(), ips)) {							
			//System.out.println("TRUE:");
			TrackingIPAssembler assembler = new TrackingIPAssembler();
			if(!key.equals(""))
				assembler.updateIPLog(httpServletRequest.getRemoteAddr(), httpServletRequest.getServletPath(), lookupBrowser(key), lookupPlatform(key));
		}
			
	}
	
	public String lookupBrowser(String key) {
		if(key.indexOf("Firefox/2.") > -1) {
			return "Firefox 2.0";
		} else if(key.indexOf("Firefox/3.") > -1) {
			return "Firefox 3.0";
		} else if(key.indexOf("Safari/") > -1) {
			return "Safari";
		} else if(key.indexOf("MSIE%207.0") > -1) {
			return "IE 7.0";
		} else if(key.indexOf("MSIE%206.0") > -1) {
			return "IE 6.0";
		} else if(key.indexOf("Mozilla") > -1) {
			return "Mozilla";
		} else {
			return "Other";
		}
		
	}
	
	public String lookupPlatform(String key) {
		if(key.indexOf("Windows") > -1) {
			return "Windows";
		} else if(key.indexOf("Macintosh") > -1) {
			return "Mac";
		} else if(key.indexOf("Linux%20i686") > -1) {
			return "Linux";
		} else {
			return "Other";
		}
		
	}
	
	public boolean lookup(String key, String[] values) {
		boolean result = false;
		if(null != values) {
			for(int i = 0; i < values.length; i++) {
				if(key.indexOf(values[i]) > -1) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public void destroy() {
		
	}

}
