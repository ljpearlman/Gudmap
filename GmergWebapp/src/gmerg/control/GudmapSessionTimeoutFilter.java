package gmerg.control;

import gmerg.entities.Globals;

import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class GudmapSessionTimeoutFilter implements Filter{

	private static final String timeoutPage = "timeout";

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			// is session invalid?
//			System.out.println("---request-------"+httpServletRequest.getRequestURI());
//			System.out.println("---session id======"+httpServletRequest.getRequestedSessionId()+"    --->"+httpServletRequest.isRequestedSessionIdValid()+"   request param="+httpServletRequest.getParameter("request"));
			if (httpServletRequest.getRequestedSessionId() != null && 
				! httpServletRequest.isRequestedSessionIdValid() && 
				! "service".equalsIgnoreCase(httpServletRequest.getParameter("request")) &&
				isSessionControlRequired(httpServletRequest.getRequestURI()) ) {
					String timeoutUrl = httpServletRequest.getContextPath() + "/pages/" + timeoutPage  + ".html";
//					System.out.println("--------required--------! redirecting to timeoutpage : " + timeoutUrl);
					httpServletResponse.sendRedirect(timeoutUrl);
					return;
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	/*
	 * session shouldn't be checked for some pages. For example: for timeout page. Since we're 
	 * redirecting to timeout page from this filter, if we don't disable session control for it, 
	 * filter will again redirect to it and this will be result with an infinite loop... 
	 */
	public static boolean isSessionControlRequired(FacesContext context) {
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		return isSessionControlRequired(request.getRequestURI());
	}
	
	public static boolean isSessionControlRequired(String requestUrl) {
		int index=requestUrl.indexOf(".jsp");
		if (index<0) 
			index=requestUrl.indexOf(".jsf");
		if (index<0) 
			index=requestUrl.indexOf(".html");
		String s = requestUrl;
		if (index>=0) 
			s = requestUrl.substring(0, index);
//			System.out.println(requestUrl+"-------->"+s );		
		if (s.endsWith(timeoutPage) )
			return false;
		String[] noSessionControlUrls = Globals.getNoSessionControlPages();		
		for (int i=0; i<noSessionControlUrls.length; i++) {
			if (s.endsWith(noSessionControlUrls[i])) 
				return false;
		}
//		System.out.println("-----required--------");		
		return true;
	}

	public static boolean isRefreshRequired(String requestUrl) {
		int index=requestUrl.indexOf(".jsp");
		if (index<0) 
			index=requestUrl.indexOf(".jsf");
		if (index<0) 
			index=requestUrl.indexOf(".html");
		String s = requestUrl;
		if (index>=0) 
			s = requestUrl.substring(0, index);
		if (s.endsWith(timeoutPage) )
			return false;
		
		return true;
	}

}
