package gmerg.control;

import gmerg.assemblers.PredictiveTextSearchAssembler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

public class GudmapPredictiveTextFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String requestURI = httpServletRequest.getRequestURI();
			if (requestURI.indexOf("ajaxPredictiveText") >= 0) {
				String match_anywhere = (String)httpServletRequest.getParameter("match_anywhere");
				String prefix = (String)httpServletRequest.getParameter("query");
				String type = (String)httpServletRequest.getParameter("input_type");
				String id = (String)httpServletRequest.getParameter("id");
				String suggestions = getXmlSuggestions(type, id, prefix);
				byte[] ajaxResponse = suggestions.getBytes();
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.setHeader("Cache-Control", "no-store");
				httpServletResponse.addHeader("Pragma", "no-cache");
				httpServletResponse.setContentType("text/xml");
				try {
					OutputStream outputStream = httpServletResponse.getOutputStream();
					outputStream.write(ajaxResponse);
					outputStream.flush();
					outputStream.close();		
				} 
				catch(IOException e) {
					System.out.println("Error! exception raised when writing autocomplete ajax response.");
				}
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	private String getXmlSuggestions(String type, String id, String prefix) {
		String xmlSuggestions = "";
		PredictiveTextSearchAssembler assembler = new PredictiveTextSearchAssembler();
		ArrayList<String> predictions = null;
		int limit = 20;

		if ("gene".equalsIgnoreCase(type))
			predictions = assembler.getGenes(prefix, 1, limit);
		else if ("anatomy".equalsIgnoreCase(type))
			predictions = assembler.getAnatomyStructures(prefix, 1, limit);
		else if ("function".equalsIgnoreCase(type))
			predictions = assembler.getGeneFunctions(prefix, 1, limit);
		
		xmlSuggestions = "<?xml version=\"1.0\" ?><ajax-response>\n<response type=\"object\" id=\"" + id + "_updater\">\n ";
		if (predictions!=null && predictions.size()>0) {
			xmlSuggestions += "<matches>";
			for (int i=0; i<predictions.size(); i++) {
				xmlSuggestions += "<entry>";
				xmlSuggestions += "<text>";
				xmlSuggestions += predictions.get(i);
				xmlSuggestions += "</text>";
				xmlSuggestions += "<value>" + String.valueOf(i+1) + "</value>";
				xmlSuggestions += "</entry>";
			}
			xmlSuggestions += "</matches>";
		}
		xmlSuggestions += "</response></ajax-response>";
		
		return xmlSuggestions;
	}			
}
