<!-- Author: Mehran Sharghi																	 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>


<f:view>

  <jsp:include page="/includes/header.jsp" />
  
  <p class="plaintextbold">HTTP Status <c:out value="${pageContext.errorData.statusCode}" /></p>
  
  <p class="plaintext">
    The document you are trying to access is protected. You
    may not access it without a proper username and password. Please contact
    the web server manager if you have lost your account or think you are entitled
    to one.
  </p>

  <jsp:include page="/includes/footer.jsp" />
  
</f:view>