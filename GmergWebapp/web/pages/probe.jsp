<!-- Author: Mehran Sharghi																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

    <table border="0" width="100%" cellpadding="0" cellspacing="0">
      <tr>
        <td><h3>MaProbe Details</h3></td>
        <td align="right"></td>
      </tr>
    </table>
 
 
      <h:outputText value="There are no probes in the database with the supplied probe id." styleClass="plaintext" rendered="#{maProbeBean.maProbe == null}" />
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{maProbeBean.maProbe != null}">
        <h:outputText value="Probe ID" />
        <h:panelGroup>
        <h:outputLink styleClass="plaintextbold" value="#{maProbeBean.maProbe.probeNameURL}" rendered="#{maProbeBean.maProbe.probeNameURL != null && maProbeBean.maProbe.probeNameURL != ''}">
        <h:outputText value="#{maProbeBean.maProbe.probeName}" />
        </h:outputLink>
        <h:outputText styleClass="plaintextbold" value="#{maProbeBean.maProbe.probeName}" rendered="#{maProbeBean.maProbe.probeNameURL == null || maProbeBean.maProbe.probeNameURL == ''}" />
        <h:outputText styleClass="plaintext" value=" (#{maProbeBean.maProbe.maprobeID})" rendered="#{maProbeBean.maProbe.probeName != maProbeBean.maProbe.maprobeID}" />
        </h:panelGroup>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Name of cDNA" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.cloneName}" />
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

        <h:outputText value="Additional Name of cDNA" rendered="#{maProbeBean.maProbe.additionalCloneName != null && maProbeBean.maProbe.additionalCloneName != ''}" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.additionalCloneName}" rendered="#{maProbeBean.maProbe.additionalCloneName != null && maProbeBean.maProbe.additionalCloneName != ''}" />
        <f:verbatim rendered="#{maProbeBean.maProbe.additionalCloneName != null && maProbeBean.maProbe.additionalCloneName != ''}" >&nbsp;</f:verbatim>
        <f:verbatim rendered="#{maProbeBean.maProbe.additionalCloneName != null && maProbeBean.maProbe.additionalCloneName != ''}" >&nbsp;</f:verbatim>

        <h:outputText value="Sequence Status" />
        <h:panelGroup>
          <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seqStatus} " />
          <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seqInfo} " rendered="#{maProbeBean.maProbe.seqInfo != null && maProbeBean.maProbe.seqInfo != ''}" />
          <h:outputLink value="#{maProbeBean.maProbe.genbankURL}" styleClass="datatext" rendered="#{maProbeBean.maProbe.seqInfo != null && maProbeBean.maProbe.seqInfo != ''}">
            <h:outputText value="#{maProbeBean.maProbe.genbankID}" />
          </h:outputLink>
        </h:panelGroup>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Gene" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Symbol:" />
          <h:outputLink value="gene.html" styleClass="datatext">
            <f:param name="gene" value="#{maProbeBean.maProbe.geneSymbol}" />
            <h:outputText value="#{maProbeBean.maProbe.geneSymbol}" />
          </h:outputLink>
          <h:outputText value="Name:" />
          <h:panelGroup>
            <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.geneName}" />
            <h:outputLink styleClass="datatext" target="_blank" value="http://www.informatics.jax.org/searches/accession_report.cgi?id=#{maProbeBean.maProbe.geneID}" rendered="#{maProbeBean.maProbe.geneID != null && maProbeBean.maProbe.geneID != ''}">
              <h:outputText value=" (#{maProbeBean.maProbe.geneID})" />
            </h:outputLink>
          </h:panelGroup>
        </h:panelGrid>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="5' primer sequence" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seq5Primer}" />
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="3' primer sequence" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seq3Primer}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="5' primer location" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seq5Loc}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="3' primer location" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.seq3Loc}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Origin of Clone used to make the Probe" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Strain:" />
          <h:outputText value="#{maProbeBean.maProbe.strain}" />
          
          <h:outputText value="Tissue:" />
          <h:outputText value="#{maProbeBean.maProbe.tissue}" />
        </h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Probe Type" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.type}" />
                
        <%--Bernie 27/6/2011 Mantis 558 Task5 - added new outputText rows 
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Probe Notes"/>
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.notes}"/>
        --%>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Curator Notes"/>
		<h:dataTable value="#{maProbeBean.maProbe.maprobeNotes}" var="maprobeNote">
			<h:column>
				<h:outputText styleClass="datatext" value="#{maprobeNote}"/>
			</h:column>
		</h:dataTable>
		
		<%--Bernie 30/6/2011 Mantis 558 Task6 - added new outputText rows --%>
		<%--Bernie 15/02/2012 Mantis 558 Task C2 - change field name --%>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="ISH Data" />
		<h:dataTable cellspacing="5" value="#{maProbeBean.maProbe.ishFilteredSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
			</h:column>
			<h:column>
				<h:outputText value="#{sub[2]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[5]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[8]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[3]}" styleClass="datatext" />
			</h:column>
		</h:dataTable>
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        
<%-- 
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Type" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.geneType}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Labelled With" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.labelProduct}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Visualisation Method" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.visMethod}" />
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Probe Source" />
        <h:outputText styleClass="datatext" value="#{maProbeBean.maProbe.source}" />
--%>                
      </h:panelGrid>
	
	<jsp:include page="/includes/footer.jsp" />
    
</f:view>