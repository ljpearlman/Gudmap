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
<style>
    .login_div_border{
	border:thin solid #06C;
	font-family: Arial, Helvetica, sans-serif;
	margin: 0px;
	padding-left: 5px;
}
    </style>
</head>

<f:view >
	<jsp:include page="/includes/header.jsp">
		<jsp:param name="headerParam" value="onlyHeader" />
		<jsp:param name="headerOnloadParam" value="setTimeout('resetLoginForm()', 20)" />
	</jsp:include>
	<%-- JSF form can not be used because of autocomplete attribute; BUT a mixture of html form and jsf tags is problematic so I used jsf form and cleared all tags in the onload method --%>
	<h:panelGroup>
	<f:verbatim>
			 <h1>Login Page   <a href="#Link735744Context" name="Link735744Context" id="Link735744Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID735744', 'Login', 'Login is optional. &lt;br&gt;When logged-in you can save personal collections of genes, images and database entries and submit new gene expression annotations using the online annotation tool. Please contact &lt;a href=\&quot;mailto:GUDMAP-EDITORS@gudmap.org\&quot;&gt; GUDMAP-EDITORS@gudmap.org &lt;/a&gt; for a login. &lt;br&gt;', 'Link735744Context')"><img src="../../images/focus/n_information.gif" width="22" height="24" border="0" /></a></h1>
			  </f:verbatim></h:panelGroup>
	<h:form id="loginForm" style="visibility:hidden">
		<h:panelGrid width="300px" cellspacing="2" cellpadding="2" styleClass="centreAlign" style="text-align:left" >
		  <h:panelGrid columns="2" cellpadding="2" cellspacing="2" style="text-align:center" >
			<h:outputText value="Please login" styleClass="plaintextbold" />
			
			  
			
		  </h:panelGrid>
		  <h:panelGrid width="100%" cellspacing="2" cellpadding="2" styleClass="header-stripey" style="border:solid black 1px" >
		    <h:outputText value=" "/>
		    <h:panelGrid columns="2" cellpadding="3" cellspacing="3" >
		      <h:outputText styleClass="plaintext" value="Enter username: "/>
		      <h:inputText id="userName" value="#{userBean.userName}" onblur="this.value=trim(this.value)" />
		      
		      <h:outputText value=" "/>
		      <h:outputText value=" "/>
		      
		      <h:outputText styleClass="plaintext" value="Enter password: "/>
		      <h:inputSecret id="password" value="#{userBean.password}" onblur="this.value=trim(this.value)" />
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
		</h:panelGrid>
	</h:form>
	
	<br/></br>
	<p><strong>Login is optional and allows the user to access additional functions</strong></p>
    <div class="login_div_border">
    <p><strong>Collections:</strong></p>
    <ul>
      <li>Save and store both ISH or microarray data as a collection (unique user data set) following a QUERY or BROWSE search function within the GUDMAP Gene Expression Database</li>

      <li>Determine gene expression patterns that may  either be unique between two data sets (differences) or shared between two or more data sets (similarities).</li>
      <li>Download search results as text files to the user's computer and share collection data sets/analyses with other users within GUDMAP</li>  
    </ul>
    <p><strong>Register to use the advanced collection functions. A username &amp; password will be emailed to you</strong>.</p>

    <p><strong><a href="http://www.gudmap.org/Help/Collections_Help.html">Collections Help Pages</a></strong></p>

    </div><br>
    
    <div class="login_div_border">
    <p><strong>Annotation Tool:</strong></p>
    <p>The Annotation Tool allows the submission of new gene expression annotations.<br>
This online interface requires a login and is available to registered laboratories.<br>
Please contact <a href="mailto:GUDMAP-EDITORS@gudmap.org">GUDMAP-EDITORS@gudmap.org</a> to express your interest</p>

    <p><strong><a href="http://www.gudmap.org/Help/Annotation_Tool_Help.html">Annotation Tool Help Pages</a></strong></p>
    </div>
	<jsp:include page="/includes/footer.jsp" />
</f:view>
	

