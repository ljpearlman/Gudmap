<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <html>
    <head>
      <title>Gudmap Edit Window</title>
      <link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
      <style type="text/css">@import("${pageContext.request.contextPath}/css/ie51.css");</style>
    </head>
    <body>
      <h:outputText styleClass="titletext" value="Acknowledgement"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>
      
      <h:panelGrid columns="1">
        <h:panelGroup styleClass="header-stripey">
          <h:dataTable value="#{ISHSingleSubmissionBean.submission.acknowledgements}" var="ack">
            <h:column>
             <f:facet name="header">
               <h:outputText value="Edit" styleClass="plaintextbold" />
             </f:facet>
              <h:selectBooleanCheckbox value="#{EditImageBean.editable}" onclick="submit()" />
            </h:column>
            <h:column>
             <f:facet name="header">
               <h:outputText styleClass="plaintextbold" value="Acknowledgement" />
             </f:facet>
             <h:inputText styleClass="datatext" value="#{ack}" rendered="#{ack.editable}"/>
             <h:outputText value="#{ack}" rendered="#{not ack.editable}" />
            </h:column>
          </h:dataTable>
        </h:panelGroup>
      </h:panelGrid>

      <br/>
      <h:panelGrid columns="4" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Acknowledgement" action="alert('Acknowledgement updated')" />
        <h:commandButton value="Add Acknowledgement" action="alert('Acknowledgement Added')" />
        <h:commandButton value="Remove Acknowledgement" action="alert('Acknowledgement removed')" />
      </h:panelGrid>
    </body>
  </html>
</f:view>
