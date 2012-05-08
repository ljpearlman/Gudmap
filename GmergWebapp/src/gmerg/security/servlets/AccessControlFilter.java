package gmerg.security.servlets;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccessControlFilter
    implements Filter {

  private FilterConfig config = null; //filter configuration object used to pass info to filter
  private String loginPage;

  /* init method called and completed before servlet handles its 1st request.
   * Typically used to perform serlvet initialisation
   */
  public void init(FilterConfig config) throws ServletException {
    this.config = config;
    loginPage = config.getInitParameter("loginPage"); //returns the value of the named init parameter
    if (loginPage == null) {
      throw new ServletException("loginPage init param missing");
    }
  }

  public void destroy() {
    config = null;
  }

  /* doFilter performs actual filtering work */
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {                     

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpServletResponse httpResp = (HttpServletResponse) response;

    if (!isAuthenticated(httpReq)) {
      String forwardURI = getForwardURI(httpReq);

      //Forward to the login page and stop further processing
      ServletContext context = config.getServletContext();
      RequestDispatcher rd = context.getRequestDispatcher(forwardURI);

      if (rd == null) {
        httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                           "Login page doesn't exist");
      }
      //rd.forward(request, response);
      httpResp.sendRedirect(getForwardURI(httpReq));
      return;
    }

    /* Process the rest of the filter chain, if any, and ultimately the
     * requested servlet or JSP page
     */
    chain.doFilter(request, response);
  }

  /* Returns true if the session contains the quthentication token */
  private boolean isAuthenticated(HttpServletRequest request) {
    boolean isAuthenticated = false;

    String cookieName = "loggedIn";
    Cookie cookies[] = request.getCookies();
    String loginStatus = null;
    if (cookies != null) {
      for (int i = 0; i < cookies.length; i++) {
        if (cookies[i].getName().equals(cookieName)) {
          loginStatus = cookies[i].getValue();
          break;
        }
      }
    }

    HttpSession session = request.getSession();
    if (loginStatus != null && loginStatus.equals("passwordAccepted")) {
      isAuthenticated = true;
    }

    return isAuthenticated;
  }

  /* Returns the context-relative path to the login page, with the parameters
   * used by the login page
   */
  private String getForwardURI(HttpServletRequest request) throws
      UnsupportedEncodingException {
    StringBuffer uri = new StringBuffer(loginPage);
    uri.append("?msg=You must login before accessing internal pages.&origURL=").
        append(URLEncoder.encode(getContextRelativeURI(request), "UTF-8"));
    return uri.toString();
  }

  /* Returns a context-relative path for the request, including the query
   * string, if any
   */
  private String getContextRelativeURI(HttpServletRequest request) {

    int ctxPathLength = request.getContextPath().length();
    String requestURI = request.getRequestURI();
    StringBuffer uri = new StringBuffer(requestURI.substring(ctxPathLength));
    String query = request.getQueryString(); //Returns the query string that is contained in the request URL after the path.
    if (query != null) {
      uri.append("?").append(query);
    }
    return uri.toString();
  }
}
