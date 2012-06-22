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
      <h:outputText styleClass="titletext" value="Linked Submission"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>
      
      <h:panelGrid columns="1" styleClass="header-stripey">
        <h:panelGroup>
          <h:dataTable value="#{ishSubmissionBean.submission.linkedSubmissions}" var="link">
            <h:column>
              <f:facet name="header">
                <h:outputText value="Edit" styleClass="plaintextbold" />
              </f:facet>
              <h:selectBooleanCheckbox value="#{EditImageBean.editable}" onclick="submit()" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText value="Database" styleClass="plaintextbold" />
              </f:facet>
              <h:outputText styleClass="plaintextbold" value="#{link[0]}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText value="Accession ID" styleClass="plaintextbold" />
              </f:facet>
              <h:outputText value="#{accessionIDAndTypes[0]}"/>
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText value="Link Type(s)" styleClass="plaintextbold" />
              </f:facet>
              <h:outputText styleClass="plaintext" value="#{linkType}" />
              <f:verbatim>&nbsp;</f:verbatim>
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText value="URL" styleClass="plaintextbold" />
              </f:facet>
              <h:outputText styleClass="datatext" value="www.gudmap.org" />
            </h:column>
          </h:dataTable>
        </h:panelGroup>
      </h:panelGrid>
      
      <br/>
      <h:panelGrid columns="4" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Linked Submission" action="alert('Linked Submission Updated')" />
        <h:commandButton value="Add Linked Submission" action="alert('Linked Submission Added')" />
        <h:commandButton value="Remove Linked Submission" action="alert('Linked Submission removed')" />
      </h:panelGrid>
    </body>
  </html>
</f:view>
