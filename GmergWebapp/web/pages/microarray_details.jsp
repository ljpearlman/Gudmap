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
      
      <title>Microarray Detail</title>
    </head>
    <body>
		<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
	        <h:outputText value="Probe ID:" />
	        <h:panelGroup>
	        <h:outputText value="#{MicroarrayDetailsBean.probeId}" />
	        </h:panelGroup>       
       		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

	        <h:outputText value="Gene:" />
	        <h:panelGroup>
	        <h:outputText value="#{MicroarrayDetailsBean.geneSymbol}" />
	        </h:panelGroup>       
       		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
       		
       		<h:outputText value="Component:" />
	        <h:panelGroup>
	               <h:outputText value="#{MicroarrayDetailsBean.component}" />
	               <h:outputLink rendered="#{!MicroarrayDetailsBean.gudmapId}" value="#{MicroarrayDetailsBean.gudmapIdUrl}">
			<h:outputText value="#{MicroarrayDetailsBean.gudmapId}" />
	                </h:outputLink>
	        </h:panelGroup>       
       		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
       		
       		<h:outputText value="Value:" />
	        <h:panelGroup>
	        <h:outputText value="#{MicroarrayDetailsBean.value}" />
	        </h:panelGroup>  
	             
       		<f:verbatim>&nbsp;</f:verbatim>
       		<f:verbatim>&nbsp;</f:verbatim>   

	    	<h:outputText value="Meta-data for probe:" />
	        <h:panelGroup>
	          <h:panelGrid columns="2" columnClasses="text-top, data-textCol">
	            <h:outputText styleClass="plaintext" value="Probe Seq ID:" />
	            <h:outputText styleClass="datatext" value="#{MicroarrayDetailsBean.refSeq}" />
	            <h:outputText styleClass="plaintext" value="Entrez Gene ID:" />
	            <h:outputText styleClass="datatext" value="#{MicroarrayDetailsBean.entrezGeneId}" />
	            <h:outputText styleClass="plaintext" value="MGI Gene ID:" />
	            <h:outputText styleClass="datatext" value="#{MicroarrayDetailsBean.mgiGeneId}" />
	            <h:outputText styleClass="plaintext" value="Human Ortholog Symbol:" />
	            <h:outputText styleClass="datatext" value="#{MicroarrayDetailsBean.humanOrthologSymbol}" />
	            <h:outputText styleClass="plaintext" value="Human Ortholog Entrez ID:" />
	            <h:outputText styleClass="datatext" value="#{MicroarrayDetailsBean.humanOrthologEntrezId}" />	
	          </h:panelGrid>
	        </h:panelGroup>
	        
      </h:panelGrid>       		    		
        
      <br/>
      
      <h:panelGroup>
        <h:commandButton value="Close" onclick="self.close()" type="button" />
      </h:panelGroup>      

    </body>
  </html>
</f:view>