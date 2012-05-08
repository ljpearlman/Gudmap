<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
	<h:outputText value="<iframe src='image_matrix_broad_table.jsf?gene=#{imageMatrixBean.gene}' width='100%' height='1000' align='RIGHT' frameborder='no'> </iframe>"  escape="false"/>
	
	
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>