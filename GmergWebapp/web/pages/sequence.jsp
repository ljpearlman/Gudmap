<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>
<html>
  <head>
    <title>Gudmap Gene Expression Database</title>
    <c:choose>
        <c:when test="${proj == 'EuReGene'}">
          <link href="<c:out value="${pageContext.request.contextPath}" />/css/euregene_css.css" type="text/css" rel="stylesheet">
        </c:when>
        <c:otherwise>
          <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
          <style type="text/css">@import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");</style>
        </c:otherwise>
      </c:choose>
  </head>
  <body class="database">
    <h:panelGrid columns="1">
      <h:outputText styleClass="plaintext" value="Template Sequence for #{ISHSingleSubmissionBean.submission.probe.probeName}:" />
      <t:dataList id="sequences" styleClass="plaintextseq"
               var="sequence"
               value="#{ISHSingleSubmissionBean.submission.probe.fullSequence}"
               layout="simple" rowCountVar="count" rowIndexVar="index"
               rendered="#{ISHSingleSubmissionBean.submission.probe.fullSequence != null}">
        <h:outputText styleClass="plaintextseq" value="#{sequence}" />
      </t:dataList>
    </h:panelGrid>
    <br/>
    <h:panelGrid columns="1" styleClass="header-stripey" columnClasses="align-left">
      <h:commandButton value="Close" onclick="self.close()" type="button" />
    </h:panelGrid>
  </body>
</html>
</f:view>