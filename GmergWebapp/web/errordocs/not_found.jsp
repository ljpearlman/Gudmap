<!-- Author: Mehran Sharghi																	 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>


<f:subview id="errorPage" >
<%-- 
  <jsp:include page="/includes/header.jsp" />
--%>  
  <p class="plaintextbold">HTTP Status <c:out value="${pageContext.errorData.statusCode}" /></p>
  
  <p class="plaintext">
    The requested document was not found on the server. Please make sure you have entered the correct address for the document you are looking for. 
  </p>
<%-- 
  <jsp:include page="/includes/footer.jsp" />
--%>  
</f:subview>
