<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
    

    <h:outputLink value="http://www.gudmap.org/Diseases/SDH/index.html">
      <h:outputText value="GUDMAP Disease Pages" />
    </h:outputLink>
    <br>


    

    
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>