<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
   	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
   	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
</head>

<body bgcolor="white" onload="setTimeout('resetLoginForm()', 50)">
	<f:view >
		<%-- JSF form can not be used because of autocomplete attribute; BUT a mixture of html form and jsf tags is problematic so I used jsf form and cleared all tags in the onload method --%>
		<h:form id="loginForm" style="visibility:hidden">
			<h:panelGrid width="100%" cellspacing="2" cellpadding="2">
				<h:outputText value=" "/>
				<h:panelGrid columns="2" cellpadding="3" cellspacing="3" >
					<h:outputText styleClass="plaintext" value="Enter username: "/>
					<h:inputText id="userName" value="#{userBean.userName}" />
					<h:outputText value=" "/>
					<h:outputText value=" "/>
					<h:outputText styleClass="plaintext" value="Enter password: "/>
					<h:inputSecret id="password" value="#{userBean.password}" />
				</h:panelGrid>
				<h:outputText value=" "/>
				<h:outputText id="loginMessage" value="#{userBean.loginMessage} &nbsp" style="color:red" styleClass="plaintext" escape="false" />
				<h:outputText value=" "/>
				<h:panelGrid columns="3" width="100%" cellspacing="8" style="text-align:center" >
					<h:commandButton value="Login" action="#{userBean.login}" />
					<h:commandButton value="Reset" action="#{userBean.resetLogin}" />
					<h:commandButton value="Cancel" action="#{userBean.cancelLogin}" />
				</h:panelGrid>
			</h:panelGrid>
		</h:form>
	</f:view>
</body>