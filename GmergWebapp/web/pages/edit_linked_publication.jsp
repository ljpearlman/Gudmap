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
      <h:outputText styleClass="titletext" value="Linked Publication"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>
      
      <h:panelGrid columns="1" styleClass="header-stripey">
        <h:panelGroup>
          <h:dataTable  value="#{ishSubmissionBean.submission.linkedPublications}" var="pub">
            <h:column>
              <f:facet name="header">
                <h:outputText value="Edit" styleClass="plaintextbold" />
              </f:facet>
              <h:selectBooleanCheckbox value="#{pub.editable}" onclick="submit()" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Author(s)" />
              </f:facet>
              <h:inputText value="#{pub[0]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[0]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Year" />
              </f:facet>
              <h:inputText value="#{pub[1]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[1]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Title" />
              </f:facet>
              <h:inputText value="#{pub[2]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[2]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Journal" />
              </f:facet>
              <h:inputText value="#{pub[3]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[3]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Volume" />
              </f:facet>
              <h:inputText value="#{pub[4]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[4]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Issue" />
              </f:facet>
              <h:inputText value="#{pub[5]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[5]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Pages" />
              </f:facet>
              <h:inputText value="#{pub[6]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[6]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="DB" />
              </f:facet>
              <h:inputText value="#{pub[7]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[7]}" rendered="#{not pub.editable}" />
            </h:column>
            <h:column>
              <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Accession" />
              </f:facet>
              <h:inputText value="#{pub[8]}" rendered="#{pub.editable}"/>
              <h:outputText styleClass="plaintext" value="#{pub[8]}" rendered="#{not pub.editable}" />
            </h:column>
          </h:dataTable>
        </h:panelGroup>
      </h:panelGrid>

      <br/>
      <h:panelGrid columns="4" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Linked Publication" action="alert('Publication Updated')" />
        <h:commandButton value="Add Linked Publication" action="alert('Publication Added')" />
        <h:commandButton value="Remove Linked Publication" action="alert('Publication removed')" />
      </h:panelGrid>
    </body>
  </html>
</f:view>
