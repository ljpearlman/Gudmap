<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<f:view>
  <html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
      <c:choose>
        <c:when test="${proj == 'EuReGene'}">
          <link href="<c:out value="${pageContext.request.contextPath}" />/css/euregene_css.css" type="text/css" rel="stylesheet">
        </c:when>
        <c:otherwise>
          <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
          <style type="text/css">@import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");</style>
        </c:otherwise>
      </c:choose>
      
      <title>Gene Expression Detail</title>
    </head>
    <body>
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText value="Stage" />
        <h:panelGroup>
        <h:outputText value="#{ExpressionDetailBean.expressionDetail.stage}" />
        </h:panelGroup>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        
        <h:outputText value="Component:" />
        <h:panelGroup>
          <h:panelGrid columns="2" columnClasses="text-top, data-textCol">
            <h:outputText styleClass="plaintext" value="Name:" />
            <h:outputText styleClass="datatext" value="#{ExpressionDetailBean.expressionDetail.componentName}" />
            <h:outputText styleClass="plaintext" value="ID:" />
            <h:outputText styleClass="datatext" value="#{ExpressionDetailBean.expressionDetail.componentId}" />
            <h:outputText styleClass="plaintext" value="Main Path:" />
            <h:dataTable style="vertical-align:bottom" value="#{ExpressionDetailBean.expressionDetail.componentDescription}" var="component">
            <h:column>
              <h:outputText styleClass="datatext" value="#{component}" />
            </h:column>
            </h:dataTable>
            
          </h:panelGrid>
        </h:panelGroup>
        
        <f:verbatim>&nbsp;</f:verbatim>
        <f:verbatim>&nbsp;</f:verbatim>
        
        <h:outputText value="Expression:" />
        <h:panelGroup>
          <h:panelGrid columns="2" columnClasses="text-top, data-textCol">
            <h:outputText styleClass="plaintext" value="Strength:" />
            <h:panelGroup>
              <h:outputText styleClass="datatext" value="#{ExpressionDetailBean.expressionDetail.primaryStrength}" />
              <h:outputText styleClass="datatext" value=", #{ExpressionDetailBean.expressionDetail.secondaryStrength}" rendered="#{ExpressionDetailBean.hasSecondaryStrength}" />
            </h:panelGroup>
          </h:panelGrid>  
          <h:dataTable rendered="#{ExpressionDetailBean.hasPatterns && proj == 'GUDMAP'}" 
                       bgcolor="white" cellpadding="5" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey"
                       value="#{ExpressionDetailBean.expressionDetail.pattern}" var="pattern">
            <h:column>
              <f:facet name="header">
                <h:outputText value="Pattern" styleClass="plaintext"/>
              </f:facet>
              <h:outputText styleClass="datatext" value="#{pattern.pattern}"  />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText value="Location(s)" styleClass="plaintext"/>
              </f:facet>
              <h:outputText styleClass="datatext" value="#{pattern.locations}" />
            </h:column>              
          </h:dataTable>
          <h:panelGrid columns="2" columnClasses="text-top, data-textCol">
          <h:outputText styleClass="plaintext" value="Pattern:" rendered="#{proj == 'EuReGene'}" />
          <h:dataTable rendered="#{ExpressionDetailBean.hasPatterns && proj == 'EuReGene'}" cellpadding="0" value="#{ExpressionDetailBean.expressionDetail.pattern}" var="pattern">
            <h:column>
              <h:outputText styleClass="datatext" value="#{pattern.pattern}"  />
            </h:column>
          </h:dataTable>
          </h:panelGrid>
          <h:panelGrid columns="2" columnClasses="text-top">
            <h:outputText styleClass="plaintext" value="Note:" />
            <h:outputText styleClass="datatext" value="#{ExpressionDetailBean.expressionDetail.expressionNote}" />
          </h:panelGrid>
          
        </h:panelGroup>
      </h:panelGrid>
      
      <br/>
      <h:form id="expressionDetailForm">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
      </h:form>
    </body>
  </html>
</f:view>