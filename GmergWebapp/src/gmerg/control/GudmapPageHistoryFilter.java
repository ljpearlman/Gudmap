package gmerg.control;

import gmerg.entities.Globals;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

public class GudmapPageHistoryFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String context = httpServletRequest.getContextPath() + "/pages/";
			String pageURI = httpServletRequest.getRequestURI();
			String[] historyRequiredPages = Globals.getHistoryRequiredPages();
			boolean isHistoryRequiredPage = false;
			for (int i=0; i<historyRequiredPages.length; i++) 
				if (pageURI.indexOf(context + historyRequiredPages[i]) >=0 ) { 
					isHistoryRequiredPage = true;
					break;
				}
			if (isHistoryRequiredPage) {
				String refererURL = httpServletRequest.getHeader("referer");
				if(refererURL!=null && refererURL.indexOf(context + "timeout") < 0) { // referer is not null & not the timeout page
					boolean isRefererInHistoryRequiredPages = false;
					for (int i=0; i<historyRequiredPages.length; i++) 
						if (refererURL.indexOf(context + historyRequiredPages[i]) >= 0 ) {
							isRefererInHistoryRequiredPages = true;
							break;
						}
					if (!isRefererInHistoryRequiredPages) {
						int pageIndex = refererURL.indexOf(context);
						if (pageIndex >= 0) 
							refererURL = refererURL.substring(pageIndex + context.length());
						HttpSession session = httpServletRequest.getSession();
						if (session!=null)
							session.setAttribute("refererURI", refererURL);
					}
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	public static String getRefererPage() {
		String refererURI = (String)FacesUtil.getSessionValue("refererURI");
		if (refererURI==null)
			return "database_homepage.html";
		
 		return Visit.getAddedStatusUrl(refererURI);
//		return refererURI;
	}
	
	public static void setRefererPage(String refererURI) {
		String referer = refererURI;
		if (refererURI.indexOf("/pages/")==0)
			referer = refererURI.substring(7);
		FacesUtil.setSessionValue("refererURI", referer);
	}
	
}
