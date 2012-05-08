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
      <h:outputText styleClass="titletext" value="Image"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>

      <h:panelGrid columns="1">
        <h:panelGroup>
          <h:dataTable columnClasses="text-normal,text-top" value="#{ishSubmissionBean.submission.originalImages}" var="image">
            <h:column>
              <h:selectBooleanCheckbox value="true" />
            </h:column>
            <h:column>
              <h:graphicImage styleClass="icon" value="#{image[0]}" height="50"/>
            </h:column>
            <h:column>
              <h:inputTextarea  rows="5" cols="20" value="#{image[1]}"/>
            </h:column>
          </h:dataTable>
        </h:panelGroup>
      </h:panelGrid>
      <br/>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Image" action="alert('Image Details updated')" />
      </h:panelGrid>
    </body>
  </html>
</f:view>
