<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<head>
	<style type="text/css">
	<!--
	body{margin:0;
	font-family: Arial, Helvetica, sans-serif;
		font-size: 0.8em;
	}
	td,th,div{
			font-size: 0.8em;
	}
	a{
		color: #0066FF;
	}
	a:hover{
		color: #666;
		text-decoration: none;
	}
	
	h1{
	margin:0px;
	height:70px;
	line-height:70px;
	background: #6699CC;
	color: #fff;
	border-bottom:solid 1px #006699;
	}
	
	.search{
	background:#ddeeff;
	padding-top:5px;
	padding-bottom:5px;
	border-bottom:solid 1px #9ABBCB;
	}
	form{margin:0;
	}
	.result{
	width:70%;
		clear:both;
	margin-bottom:20px;
	margin-top:20px;}
	.result h3{
			margin:0px;
			line-height: 25px;
			font-size: 1.3em;
	}
	.linked {
		padding-top: 5px;
		padding-bottom: 5px;
	}
	.linked a{
	
	margin-right: 10px;
	border:solid 1px #CCCCCC;
	padding:3px 10px 3px 10px;
	text-decoration: none;
	}
	.linked a:hover{
	
	margin-right: 10px;
	border:solid 1px #0066FF;
		background: #0066FF;
		color: #fff;
	
	}
	hr{
	
		border: dashed 1px #ddd;
			display: block;
			background:#fff;
			height: 1px;
	}
	.footer{
		font-size:0.8em;
		border-top:solid 1px #ddd;
		padding-top:10px;
	}
	.footer a{color:#666;
	text-decoration: none;}
	.footer a:hover{
		text-decoration: underline;
	}
	-->
	</style>
</head>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	<h:form id="mainForm">	
	<h:panelGrid width="100%" columns="1">
				<h:inputText id="input" value="#{DatabaseHomepageBean.luceneInput}" required="true"/>
				<h:commandLink id="go4" action="#{DatabaseHomepageBean.goSearch}">
					<h:graphicImage url="../images/focus/go2.gif" alt="Go" styleClass="icon" />
				</h:commandLink>
			<h:panelGrid width="100%" columns="1">	
			<t:dataList id="labs1"
               styleClass="plaintextbold"
               var="row"
               value="#{DatabaseHomepageBean.result}"
               layout="simple"
               rowCountVar="count"
               rowIndexVar="index">
               
				<h:outputLink styleClass="datatext" value="#{row.htmlPath}">
					<h:outputText value="#{row.htmlTitle}" />
				</h:outputLink>	
			</t:dataList>
			</h:panelGrid>
	</h:panelGrid>
	
	
</h:form>	
<jsp:include page="/includes/footer.jsp" />
</f:view>