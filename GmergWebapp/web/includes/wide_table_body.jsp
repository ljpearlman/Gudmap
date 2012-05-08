<!doctype html public "-//w3c//dtd html 4.01 transitional//en">
<!-- GenericTable body frame JSF interface page					-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<head>
    <link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
<!--     <link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
 -->
 
</head>


<body class="stripey" >

<f:view>
	<h:panelGroup rendered="#{TableBean.tableEmpty}">
		<h:outputText value="<h3>There is no data for this table.</h3>" escape="no"/>
	</h:panelGroup>
	
	<h:form id="tableBodyForm" rendered="#{!TableBean.tableEmpty}" target="#{TableBean.tableBodyFormTarget}" >

		<jsp:include page="table_body.jsp" />

	</h:form>
  
</f:view>

</body>
