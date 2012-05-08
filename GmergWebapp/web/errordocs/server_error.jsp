<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>




  
    <jsp:include page="/includes/header.jsp">
		<jsp:param name="headerParam" value="onlyHeader" />
    </jsp:include>
  
  <p class="plaintextbold">HTTP Status <c:out value="${pageContext.errorData.statusCode}" /></p>
  <p class="plaintext">
    An unexpected error has occurred preventing the server from fulfilling your request. 
    Detailed information regarding this error has been logged and will be addressed at the 
    first available opportunity. If the problem persists please inform the 
    <a href="mailto:gudmap-db@hgu.mrc.ac.uk" class="plaintext">server administrator</a>.
  </p>
  
  <p class="plaintext">
    <span class="plaintextbold">Error Message:</span>
    <br />
    <c:out value="${pageContext.errorData.throwable.message}" />
  </p>
  <jsp:include page="/includes/footer.jsp" />
  

