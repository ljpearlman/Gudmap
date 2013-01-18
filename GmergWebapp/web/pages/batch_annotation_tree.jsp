<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<fmt:setBundle basename="configuration" />

<fmt:message var="proj" scope="application" key="project" />
<fmt:message var="perspective" scope="application" key="perspective" />
<fmt:message var="stageSeriesShort" scope="application" key="stage_series_short" />
<fmt:message var="stageSeriesMedium" scope="application" key="stage_series_medium" />
<fmt:message var="stageSeriesLong" scope="application" key="stage_series_long" />
<fmt:message var="siteSpecies" scope="application" key="species"/>
<c:set var="pageName" value="${pageContext.request.servletPath}" />
<html>
<head>
	<style>
		.width95 {
			width:95%;
		}
		.width5 {
			width:5%;
		}
		td.width5 {
			text-align:right;
			vertical-align:bottom;
		}		
	</style>
	<title>GUDMAP Edit Batch Annotation Tree Window</title>
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/help.css" type="text/css" rel="stylesheet">
	<style type="text/css">
		@import("${pageContext.request.contextPath}/css/ie51.css");
	</style>
	<script src="${pageContext.request.contextPath}/scripts/formfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navfunctions.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/scripts/ua.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/ftiens4.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
	<c:set var="statusParams" value="<%= gmerg.utils.Visit.getStatus() %>" />
	<script type="text/javascript">
	
		function addStatusParam() {
			for (var i=0; i<document.forms.length; i++ ){
				var input = document.createElement('input');
				if (input) {
					input.type = 'hidden';
					input.name = 'statusParams';
					input.value = '${statusParams}';
				}
				document.forms[i].appendChild(input);
			}
		}
		
		function setCookie(c_name,value,expiredays)
		{
			var exdate=new Date();
			exdate.setDate(exdate.getDate()+expiredays);
			document.cookie=c_name+ "=" +escape(value)+
			((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
		}

		function getCookie(c_name)
		{
			if (document.cookie.length>0)
  			{
  				c_start=document.cookie.indexOf(c_name + "=");
  				if (c_start!=-1)
    			{ 
    				c_start=c_start + c_name.length+1; 
    				c_end=document.cookie.indexOf(";",c_start);
    				if (c_end==-1) c_end=document.cookie.length;
    				return unescape(document.cookie.substring(c_start,c_end));
    			} 
  			}
			return "";
		}
		
				
	</script>
	<c:set var="headerParam" value="${param['headerParam']}" />	
</head>
<body>
<f:view>
	<h:outputText value="Temporary GUDMAP ID:#{BatchAnnotationTreeBean.subIdCookie}"/>
	<h:form id="stageForm" styleClass="header-nostripe">
		<h:outputText value="Please select a Theiler Stage: " />
		<h:selectOneMenu id="stageMenu" value="#{BatchAnnotationTreeBean.stage}" styleClass="datatext" 
						disabled="#{UserBean.user.userType=='EXAMINER' 
								|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
								|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
								|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
								}">
			<f:selectItems value="#{BatchAnnotationTreeBean.stagesInPerspective}"/>
		</h:selectOneMenu>
		<f:verbatim>&nbsp;</f:verbatim>
		<h:commandButton id="displayTreeButton" action="#{BatchAnnotationTreeBean.displayTree}" 
						onclick="if(getCookie('STAGEINDB') != getById('stageForm:stageMenu').value) return confirm('You are about to change to a new stage, all the annotations of the old stage will be deleted, click yes to confirm.');" 
						value="Display Tree"
						disabled="#{UserBean.user.userType=='EXAMINER' 
								|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
								|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
								|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
								}"/>
	</h:form>
	
	<h:panelGrid columns="1" rendered="#{BatchAnnotationTreeBean.status==2}">
		<h:outputText value="Confliction exists in your selected terms with annotated terms, Please re-select the correct terms." styleClass="plainred"/>
	</h:panelGrid>
	
	<h:form id="annotationForm" styleClass="header-nostripe">
		<div id="ptnLcnNtMenu" style="display:none;position:absolute;visibility:hidden;z-index:1000;background-color:white;border:1px solid black">
			<ul style="list-style:none;margin:0;padding:0;"/>
		</div>
		<h:inputHidden value="#{BatchAnnotationTreeBean.stage}" />
		<h:panelGrid width="100%" columns="2" columnClasses="leftCol,rightCol">
			<h:panelGroup>
				<h:outputText value="Expression Key:" styleClass="plaintextbold" />
				<h:panelGrid columns="1">
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="p"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/DetectedRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (unspecified strength)" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/DetectedRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (unspecified strength)" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="s"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/StrongRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (strong)" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/StrongRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (strong)" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="m"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/ModerateRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (moderate)" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/ModerateRoundPlus20x20.gif" styleClass="icon" />
						<h:outputText value="Present (moderate)" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="w"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/WeakRoundPlus20x20.gif" styleClass="icon" />		
						<h:outputText value="Present (weak)" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/WeakRoundPlus20x20.gif" styleClass="icon" />		
						<h:outputText value="Present (weak)" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="u"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/PossibleRound20x20.gif" styleClass="icon" />
						<h:outputText value="Uncertain" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/PossibleRound20x20.gif" styleClass="icon" />
						<h:outputText value="Uncertain" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="nd"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/NotDetectedRoundMinus20x20.gif" styleClass="icon" />
						<h:outputText value="Not Detected" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/NotDetectedRoundMinus20x20.gif" styleClass="icon" />
						<h:outputText value="Not Detected" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addExpressionAnnotation}" id="ne"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/Frame20x20.gif" styleClass="icon" />
						<h:outputText value="Not Examined" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/Frame20x20.gif" styleClass="icon" />
						<h:outputText value="Not Examined" styleClass="plaintext" />
					</h:panelGroup>
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
				</h:panelGrid>
				
				<h:outputText value="Expression Patterns Key:" styleClass="plaintextbold" />
				<h:panelGrid columns="1" >
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="graded"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />			
						<h:graphicImage value="/images/tree/GradedRound20x20.png" styleClass="icon" />
						<h:outputText value="Graded" styleClass="plaintext" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/GradedRound20x20.png" styleClass="icon" />
						<h:outputText value="Graded" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="regional"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />				
						<h:graphicImage value="/images/tree/RegionalRound20x20.png" styleClass="icon" />
						<h:outputText value="Regional" styleClass="plaintext" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/RegionalRound20x20.png" styleClass="icon" />
						<h:outputText value="Regional" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="spotted"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />			
						<h:graphicImage value="/images/tree/SpottedRound20x20.png" styleClass="icon" />
						<h:outputText value="Spotted" styleClass="plaintext" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/SpottedRound20x20.png" styleClass="icon" />
						<h:outputText value="Spotted" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="ubiquitous"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />		
						<h:graphicImage value="/images/tree/UbiquitousRound20x20.png" styleClass="icon" />
						<h:outputText value="Ubiquitous" styleClass="plaintext" />
					</h:commandLink>		  			
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/UbiquitousRound20x20.png" styleClass="icon" />
						<h:outputText value="Ubiquitous" styleClass="plaintext" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="restricted"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
						<h:outputText value="Restricted" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
						<h:outputText value="Restricted" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					</h:panelGroup>
					<h:commandLink actionListener="#{BatchAnnotationTreeBean.addPattern}" id="sc"  styleClass="plaintext" 
								rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| BatchAnnotationTreeBean.submission.dbStatusId==2
									|| UserBean.user.userPrivilege==3 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=19 
									|| UserBean.user.userPrivilege==4 && BatchAnnotationTreeBean.submission.dbStatusId>4 && BatchAnnotationTreeBean.submission.dbStatusId<=21 
									|| UserBean.user.userPrivilege==5 && BatchAnnotationTreeBean.submission.dbStatusId>=4 && BatchAnnotationTreeBean.submission.dbStatusId<=23)}">
						<f:param name="stage" value="#{BatchAnnotationTreeBean.stage}" />
						<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
						<h:outputText value="Single cell" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					</h:commandLink>
					<h:panelGroup rendered="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>19) 
									|| UserBean.user.userPrivilege==4 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId==4 || BatchAnnotationTreeBean.submission.dbStatusId>21) 
									|| UserBean.user.userPrivilege==5 && (BatchAnnotationTreeBean.submission.dbStatusId==3 || BatchAnnotationTreeBean.submission.dbStatusId>23)
									}">
						<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
						<h:outputText value="Single cell" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					</h:panelGroup>
					<h:panelGrid columns="1" >
						<h:graphicImage value="/images/tree/note.gif" styleClass="icon" />
						<h:outputText value="Contains note" styleClass="plaintext" />
					</h:panelGrid>
				</h:panelGrid>
			</h:panelGroup>
			<h:panelGroup rendered="#{BatchAnnotationTreeBean.stage != null && BatchAnnotationTreeBean.stage != ''}">
				<h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
					<h:outputText value="Javascript Tree Menu" />
				</h:outputLink>
				<f:verbatim>
					<script type="text/javascript">
						<c:forEach items="${BatchAnnotationTreeBean.anatomyTreeContent}" var="row">
							<c:out value="${row}" escapeXml="false"/>
						</c:forEach>
					</script>
					<script type="text/javascript">initializeDocument()</script>
					<noscript>
						<span class="plaintext">A tree of annotated anatomical components will open here if you enable JavaScript in your browser.</span>
					</noscript>
					&nbsp;
				</f:verbatim>
				<h:panelGroup>
					<h:outputText styleClass="plaintextbold" value="G " />
					<h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
				</h:panelGroup>
			</h:panelGroup>
		</h:panelGrid>
		<f:verbatim><div id="components"></div></f:verbatim>
	</h:form>
	
	<h:form id="closeForm">
		<h:panelGroup>
			<h:commandButton id="closeButton" value="Close" onclick="self.close()" type="button" />
		</h:panelGroup>
	</h:form>
</f:view>
</body>
</html>