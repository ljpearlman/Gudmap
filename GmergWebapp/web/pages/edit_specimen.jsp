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
      <h:outputText styleClass="titletext" value="Specimen"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>
      
    <h:panelGroup>

    <h:panelGrid columns="2" border="0" rowClasses="header-stripey" columnClasses="data-titleCol,data-textCol">
      <h:outputText styleClass="plaintextbold" value="Stage:" />
      <h:panelGroup>
        <h:outputText value="#{stageSeriesShort}" />
        <h:inputText value="#{ISHSingleSubmissionBean.submission.stage}" />
      </h:panelGroup>
          
      <h:outputText styleClass="plaintextbold" value="Other Staging System:"/>
      <h:panelGroup>
        <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.stageFormat} " />
        <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.otherStageValue}" />
      </h:panelGroup>
          
      <h:outputText styleClass="plaintextbold" value="Strain:" />
      <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.strain}" />
          
      <h:outputText styleClass="plaintextbold" value="Sex:" />
      <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.sex}" />
          
      <h:outputText styleClass="plaintextbold" value="Genotype:" />
      <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.genotype}" />
          
      <h:outputText styleClass="plaintextbold" value="Specimen Preparation:" />
      <h:panelGrid columns="2" columnClasses="text-top, text-bottom">
        <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.assayType}" />
        <h:panelGrid columns="3">
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText styleClass="plaintextbold" value="Fixation Method:" />
          <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.fixMethod}" />
        
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText styleClass="plaintextbold" value="Embedding:" />
          <h:inputText value="#{ISHSingleSubmissionBean.submission.specimen.embedding}" />
        </h:panelGrid>
      </h:panelGrid>

      <h:outputText styleClass="plaintextbold" value="Experiment Notes:" />
      <h:inputTextarea value="#{ISHSingleSubmissionBean.submission.specimen.notes}"></h:inputTextarea>
    </h:panelGrid>  
    </h:panelGroup>
      <br/>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Speicimen" action="alert('specimen updated')" />
      </h:panelGrid>
    </body>
  </html>
</f:view>
