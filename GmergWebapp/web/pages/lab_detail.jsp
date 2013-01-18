<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>
  <html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
          <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
          <style type="text/css">@import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");</style>
      <title>Source Detail</title>
    </head>
    <body>
    	<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" rendered="#{PersonBean.people == null}" >
    		<h:outputText value="Name:" />
    		<h:outputText value="#{PersonBean.person.name}" />
    		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
    		
    		<h:outputText value="Institution:" />
    		<h:outputText value="#{PersonBean.person.lab}" />
    		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
    		
    		<h:outputText value="Address:" />
    		<h:panelGrid columns="1" border="0">
    			<h:outputText styleClass="datatext" value="#{PersonBean.person.address}" />
    			<h:outputText styleClass="datatext" value="#{PersonBean.person.address2}" rendered="#{PersonBean.person.address2 != null}" />
    			<h:outputText styleClass="datatext" value="#{PersonBean.person.city} #{PersonBean.person.postcode}" />
    			<h:outputText styleClass="datatext" value="#{PersonBean.person.country}" />
    		</h:panelGrid>
    		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
    		
    		<h:outputText value="Tel:" />
    		<h:outputText value="#{PersonBean.person.phone}" />
    		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
    		
    		<h:outputText value="Email:" />
	                <h:outputLink value="mailto:#{PersonBean.person.email}">
    		        <h:outputText value="#{PersonBean.person.email}" />
	                </h:outputLink>
    	</h:panelGrid>
    	
    	<t:dataList id="peopleDataList" var="peopleInfo" value="#{PersonBean.people}" rowCountVar="count" rowIndexVar="index" layout="unorderedList"
				rendered="#{PersonBean.people != null}">
				
				<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" >
					<h:outputText value="Name:" />
					<h:outputText value="#{peopleInfo.name}" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Institution:" />
					<h:outputText value="#{peopleInfo.lab}" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Address:" />
					<h:panelGrid columns="1" border="0">
						<h:outputText styleClass="datatext" value="#{peopleInfo.address}" />
						<h:outputText styleClass="datatext" value="#{peopleInfo.city} #{peopleInfo.postcode}" />
						<h:outputText styleClass="datatext" value="#{peopleInfo.country}" />
					</h:panelGrid>
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Tel:" />
					<h:outputText value="#{peopleInfo.phone}" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Email:" />
	                                                                <h:outputLink value="mailto:#{peopleInfo.email}">
					          <h:outputText value="#{peopleInfo.email}" />
	                                                                </h:outputLink>
				</h:panelGrid>
				<h:panelGrid width="100%" rowClasses="header-stripey"  >
					<f:verbatim>&nbsp;</f:verbatim> 
				</h:panelGrid>
		</t:dataList>
		
		<h:panelGrid>
			<h:outputText value="" />
		</h:panelGrid>
		<h:panelGrid columns="1" >
			<h:commandButton value="Close" onclick="self.close()" />
		</h:panelGrid>
	</body>
  </html>
</f:view>