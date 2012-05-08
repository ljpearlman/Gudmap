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
    <h:form>
      <h:outputText styleClass="titletext" value="Probe"/>
      <f:verbatim>&nbsp;</f:verbatim>
      <h:outputText styleClass="titletext" value="-----Under Struction..." />
      <br/>
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Probe Name:" />
        <h:outputText value="#{editProbeBean.probe.probeName}" />

        <h:outputText styleClass="plaintextbold" value="Clone Name"/>
        <h:outputText value="#{editProbeBean.probe.cloneName}" />

        <h:outputText value="Sequence:" />
        <h:panelGrid columns="2" rowClasses="text-top, header-stripey,header-nostripe">
          <h:outputText styleClass="plaintext" value="#{editProbeBean.probe.seqStatus}" />
          <h:panelGroup rendered="#{editProbeBean.renderProbeSeqInfo}" >
            <h:outputText styleClass="plaintext" value="#{editProbeBean.probe.seqInfo}" />
            <h:outputText value="#{editProbeBean.probe.genbankID}" />
          </h:panelGroup>
        </h:panelGrid>
        
        <h:outputText styleClass="plaintextbold" value="Gene:" />
        <h:panelGrid columns="1" columnClasses="text-top,text-normal">
          <h:panelGroup>
            <h:outputText styleClass="plaintext" value="Symbol: " />
            <h:outputText value="#{editProbeBean.probe.geneSymbol}" />
          </h:panelGroup>
          <h:panelGroup>
            <h:outputText styleClass="plaintext" value="Name: " />
            <h:outputText styleClass="datatext" value="#{editProbeBean.probe.geneName}" />
            <h:outputText styleClass="datatext" value="(#{editProbeBean.probe.geneID})" />
          </h:panelGroup>
        </h:panelGrid>

        <h:outputText styleClass="plaintextbold" value="5' primer sequence:"/>
        <h:inputText value="#{editProbeBean.probe.seq5Primer}" size="40"/>
        
        <h:outputText styleClass="plaintextbold" value="3' primer sequence:"/>
        <h:inputText value="#{editProbeBean.probe.seq3Primer}" size="40"/>
        
        <h:outputText styleClass="plaintextbold" value="5' primer location:"/>
        <h:inputText value="#{editProbeBean.probe.seq5Loc}" />
        
        <h:outputText styleClass="plaintextbold" value="3' primer location:"/>
        <h:inputText value="#{editProbeBean.probe.seq3Loc}" />

        <h:outputText styleClass="plaintextbold" value="Template Sequence:" rendered="#{editProbeBean.probe.fullSequence != null}"/>
        <h:outputLink styleClass="plaintext" value="#" onclick="var w=window.open('sequence.jsf?id=#{editProbeBean.submissionId}','sequencePopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;"  rendered="#{editProbeBean.probe.fullSequence != null}">
          <h:outputText value="(Click to see sequence.)" />
        </h:outputLink>
	  
        <h:outputText styleClass="plaintextbold" value="Notes:"/>
        <h:inputTextarea value="#{editProbeBean.probe.maprobeNoteString}" rows="5" cols="40" />

        <h:outputText styleClass="plaintextbold" value="Origin of Clone used to make the Probe:" />
        <h:panelGrid columns="4" border="0">
          <h:inputText styleClass="datatext" value="#{editProbeBean.probe.source}" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText styleClass="plaintextbold" value="Strain:" />
          <h:inputText styleClass="datatext" value="#{editProbeBean.probe.strain}" />
          
          <f:verbatim>&nbsp;</f:verbatim>
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText styleClass="plaintextbold" value="Tissue:" />
          <h:inputText styleClass="datatext" value="#{editProbeBean.probe.tissue}" />
        </h:panelGrid>
      
        <h:outputText styleClass="plaintextbold" value="Probe Type:" />
        <h:inputText value="#{editProbeBean.probe.type}" />
        
        <h:outputText styleClass="plaintextbold" value="Type:" />
        <h:inputText value="#{editProbeBean.probe.geneType}" />
        
        <h:outputText styleClass="plaintextbold" value="Labelled with:" />
        <h:inputText value="#{editProbeBean.probe.labelProduct}" />
        
        <h:outputText styleClass="plaintextbold" value="Visualisation method:" />
        <h:inputText value="#{editProbeBean.probe.visMethod}" />
          
        <h:outputText styleClass="plaintext,text-top" value="Additional Notes:" />
        <h:inputTextarea value="#{editProbeBean.probe.notes}" rows="5" cols="40" />

      </h:panelGrid>
      <br/>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
<%--
        <h:commandButton value="Update Probe" action="alert('Probe updated')" />
--%>
      </h:panelGrid>
    </h:form>
  </body>
  </html>
</f:view>
