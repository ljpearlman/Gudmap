package gmerg.control;

import gmerg.beans.UserBean;
import gmerg.entities.Globals;
import gmerg.utils.Visit;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

public class GudmapAccessRestrictionFilter implements Filter{

		public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			HttpSession session = httpServletRequest.getSession();
			boolean userLoggedIn = false;
			if (session!=null) {
				UserBean userBean = (UserBean)session.getAttribute("userBean");
				if (userBean != null)
					userLoggedIn  =userBean.isUserLoggedIn(); 
			}
			String pageURI = httpServletRequest.getRequestURI().substring(httpServletRequest.getRequestURI().indexOf("/pages/")+7);
			if (!userLoggedIn && isRestrictedPage(httpServletRequest.getRequestURI())) {
				session.setAttribute("restrictedAccesssPage", pageURI);		// This is to transfer to requested page from login page
				String restrictedPageRedirect = httpServletRequest.getContextPath() + "/pages/restricted_access_redirect.html";
				httpServletResponse.sendRedirect(restrictedPageRedirect);
				return;
			}
			else 
				if (pageURI.indexOf("secure/login.")!=0 && pageURI.indexOf("restricted_access_redirect.")!=0)
					session.removeAttribute("restrictedAccesssPage");
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	public static boolean isRestrictedPage(String requestUrl) {
		int index=requestUrl.indexOf(".jsp");
		if (index<0) 
			index=requestUrl.indexOf(".jsf");
		if (index<0) 
			index=requestUrl.indexOf(".html");
		String s = requestUrl;
		if (index>=0) 
			s = requestUrl.substring(0, index);
		String[] restrictedPages = Globals.getAccessRestrictedPages();
		for (int i=0; i<restrictedPages.length; i++) {
			if (s.endsWith(restrictedPages[i])) 
				return true;
		}
//		System.out.println("-----required--------");		
		return false;
	}

}
