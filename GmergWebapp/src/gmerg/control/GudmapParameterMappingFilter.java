package gmerg.control;

import gmerg.utils.DbUtility;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GudmapParameterMappingFilter implements Filter{
	

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && isRequestConatinsMappingParameter(request)) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletRequestWrapper wrappedRequest = new CustomHttpServletRequest(httpRequest);
			filterChain.doFilter(wrappedRequest, response);
		}
		else
			filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}
	
	private boolean isRequestConatinsMappingParameter(ServletRequest request) {
		if (request.getParameter("MGI")!=null)
			return true;
		return false;
	}

	/************************************************************
	 * Private class to wrap the reqest and add new parameters 
	 * 
	 */
	private class CustomHttpServletRequest extends HttpServletRequestWrapper {
		
		private HttpServletRequest originalRequest;
//		private final String[][] mappingParamNames = {{"gene", "MGI"}, {"genes", "MGI"}, {"symbol", "MGI"}, {"symbols", "MGI"} };
//		private final String[][] mappingParamNames = {{"MGI", "gene"}, {"symbol", "gene"}, {"symbols", "gene"} };
		private boolean isMappingRequired;
//		private HashMap<String, Boolean> synonymsParams; 

		private class MappedParam {
			String id;
			HashSet<String> synonyms;
			String synonymValue;
			public MappedParam (String id) {
				this.id = id;
				synonyms = new HashSet<String>();
				synonymValue = null;
			}
			public void addSynonym(String synonym, String value) {
				if (synonyms.contains(synonym))
					return;
				if (synonymValue==null)
					synonymValue = value;
				else
					synonymValue += ";" + value; 
				synonyms.add(synonym);
			}
			public String getSynonymValue() {
				return synonymValue;
			}
			public String[] getSynonymValues(HttpServletRequest request) {
				ArrayList<String> values = new ArrayList<String>();
				for(String synonym: synonyms) {
					String[] sysnonymValues = request.getParameterValues(synonym);
					for (String value:sysnonymValues)
						values.add(value);
				}
				return (String[])values.toArray();
			}
		}
		
		private HashMap<String, MappedParam> synonymParams; 
		
		
		/*
		 * Public methods overriding parent class methods inorder to add new request parameter(s) 
		 */
		public CustomHttpServletRequest(HttpServletRequest request) {
			super(request);
			final String[][] mappingParamNames = {{"MGI", "gene"}}; //, {"symbol", "gene"}, {"symbols", "gene"} };
			originalRequest = request;
//			synonymsParams = new HashMap<String, Boolean>();
			synonymParams = new HashMap<String, MappedParam>();
			for (int i=0; i<mappingParamNames.length; i++) 
				if (originalRequest.getParameter(mappingParamNames[i][1])==null) {
					String synonymParamValue = getParameterFromSynonym(mappingParamNames[i][0]);
					if (synonymParamValue != null){
						isMappingRequired = true;
	//					synonymsParams.put(mappingParamNames[i][1], true);
						MappedParam mappedParam = synonymParams.get(mappingParamNames[i][1]);
						if (mappedParam==null) { 
							mappedParam = new MappedParam(mappingParamNames[i][1]);
							synonymParams.put(mappingParamNames[i][1], mappedParam);
						}
						mappedParam.addSynonym(mappingParamNames[i][1], synonymParamValue);
					}
				}
		}
		
		public String getParameter(String name) {
			String value = originalRequest.getParameter(name);
			if (value == null) {
				MappedParam mappedParam = synonymParams.get(name);
				if (mappedParam != null)
					return mappedParam.getSynonymValue();
			}
			return value;
		}
		
		public String[] getParameterValues(String name) {
			String[] value = originalRequest.getParameterValues(name);
			if (value == null) {
				MappedParam mappedParam = synonymParams.get(name);
				if (mappedParam != null)
					return mappedParam.getSynonymValues(originalRequest);
			}
			return value;
		}

		public Map getParameterMap() {
			if (!isMappingRequired)
				return originalRequest.getParameterMap();
			Map map = new HashMap();
			map.putAll(originalRequest.getParameterMap());
			for (MappedParam mappedParam:synonymParams.values()) {
				String[] paramValues = mappedParam.getSynonymValues(originalRequest); 
				if (paramValues != null)
					map.put(mappedParam.id, paramValues);
			}
			return map;
		}
		
		public Enumeration getParameterNames() {
			if (!isMappingRequired)
				return originalRequest.getParameterNames();
			
			HashMap params = (HashMap)originalRequest.getParameterMap();
			Set<String> names = new HashSet<String>();
			names.addAll(params.keySet());
			
			for (MappedParam mappedParam:synonymParams.values()) 
				if (mappedParam.getSynonymValue()!=null)
					names.add(mappedParam.id);
			
			final Iterator<String> it = names.iterator();
			Enumeration<String> namesEnum = new Enumeration<String>() {
				public boolean hasMoreElements() {
					return it.hasNext();
				}
				public String nextElement() {
					String next = it.next();
					return next;
				}
			};
			return namesEnum;
		}

		/*
		 * Private methoda
		 */
		private String getParameterFromSynonym(String synonym) {
			String synonymParam = originalRequest.getParameter(synonym);
			if (synonymParam != null)
				if(synonym.equalsIgnoreCase("MGI"))
					return DbUtility.retrieveGeneSymbolByMGIId(synonymParam);
			return null;
		}
		
/*			
		private String[] getParameterValuesFromSynonym(String name) {
			if (name == null)
				return null;
			String synonym = getSynonym(name);
			
			String[] synonymParamValues = originalRequest.getParameterValues(synonym);
			
			if (synonymParamValues != null) {
				String[] paramValues = new String[synonymParamValues.length];
				for (int i=0; i<synonymParamValues.length; i++)
					paramValues[i] = DbUtility.retrieveGeneSymbolByMGIId(synonymParamValues[i]);	
			}
			return null;
		}
		
		private String getSynonym(String name) {
			for (int i=0; i<mappingParamNames.length; i++) 
				if (name.equalsIgnoreCase(mappingParamNames[i][0]))
					return mappingParamNames[i][1];
			return null;
		}
*/		
		/*		
		private String getMappedParam() {
			for (int i=0; i<mappingParamNames.length; i++) {
				String explicitParam = originalRequest.getParameter(mappingParamNames[i][0]);
				if (explicitParam!=null)
					return explicitParam;
			}
			return null;
		}
*/		

	}
}
