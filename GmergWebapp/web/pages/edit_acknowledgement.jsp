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
          <h:dataTable value="#{ishSubmissionBean.submission.acknowledgements}" var="ack">
            <h:column>
             <f:facet name="header">
               <h:outputText value="Edit" styleClass="plaintextbold" />
             </f:facet>
              <h:selectBooleanCheckbox value="#{editImageBean.editable}" onclick="submit()" />
            </h:column>
            <h:column>
             <f:facet name="header">
               <h:outputText styleClass="plaintextbold" value="Project" />
             </f:facet>
             <h:inputText styleClass="datatext" value="#{ack[0]}" rendered="#{ack.editable}" />
             <h:outputText value="#{ack[0]}" rendered="#{not ack.editable}" />
            </h:column>
            <h:column>
             <f:facet name="header">
               <h:outputText styleClass="plaintextbold" value="Name(s)" />
             </f:facet>
             <h:inputText styleClass="datatext" value="#{ack[1]}" rendered="#{ack.editable}" />
             <h:outputText value="#{ack[1]}" rendered="#{not ack.editable}" />
            </h:column>
            <h:column>
             <f:facet name="header">
               <h:outputText styleClass="plaintextbold" value="Address" />
             </f:facet>
             <h:inputText styleClass="datatext" value="#{ack[2]}" rendered="#{ack.editable}" />
             <h:outputText value="#{ack[2]}" rendered="#{not ack.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="URL" />
              </f:facet>
              <h:inputText value="#{ack[3]}" rendered="#{ack.editable}" />
              <h:outputText value="#{ack[3]}" rendered="#{not ack.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Reason" />
              </f:facet>
              <h:inputText styleClass="datatext" value="#{ack[4]}" rendered="#{ack.editable}" />
              <h:outputText value="#{ack[4]}" rendered="#{not ack.editable}" />
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
