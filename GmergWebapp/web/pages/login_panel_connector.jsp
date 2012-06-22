<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
   	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
</head>

<body bgcolor="white" onload="loginFrameOnload()" >
	<f:view >
		<h:form id="loginPanelConnectorForm">
			<h:inputHidden id="userLoggedIn" value="#{UserBean.userLoggedIn}"/>
			<input type="hidden" id="loginOperation" value="${param.loginOperation}"/>
			<h:commandLink value="refresh" id="refreshLoginPanelLink" action="#{UserBean.refreshLoginPanel}" />
		</h:form>
	</f:view>
</body>