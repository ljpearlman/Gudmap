<!-- Author: Bernard Haggarty																 -->
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
 
 
      <h:outputText value="There are no antibodies in the database with the supplied probe id." styleClass="plaintext" rendered="#{AntibodyBean.antibody == null}" />
      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{AntibodyBean.antibody != null}">
        <h:outputText value="Accession ID" />
        <h:panelGroup>
        <h:outputLink styleClass="plaintextbold" value="http://www.informatics.jax.org/accession/#{AntibodyBean.antibody.accessionId}" >
        <h:outputText value="#{AntibodyBean.antibody.accessionId}" />
        </h:outputLink>
        <h:outputText styleClass="plaintext" value=" (#{AntibodyBean.antibody.maProbeId})" />
        </h:panelGroup>
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Name " />
        <h:outputText styleClass="datatext" value="#{AntibodyBean.antibody.name}" />


        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Gene" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Symbol:" />
          <h:outputLink value="gene.html" styleClass="datatext">
            <f:param name="gene" value="#{AntibodyBean.antibody.geneSymbol}" />
            <h:outputText value="#{AntibodyBean.antibody.geneSymbol}" />
          </h:outputLink>
          <h:outputText value="Name:" />
          <h:panelGroup>
            <h:outputText styleClass="datatext" value="#{AntibodyBean.antibody.geneName}" />
            <h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/accession/#{AntibodyBean.antibody.locusTag}" target="_blank">
              <h:outputText value="(#{AntibodyBean.antibody.locusTag})" />
            </h:outputLink>
          </h:panelGroup>
        </h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Epitope (Uniprot ID)" />
        <h:outputText styleClass="datatext" value="#{AntibodyBean.antibody.uniprotId}" />
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{AntibodyBean.antibody.seqStartLocation > 0}">        
        <h:outputText value="Amino-terminus(start)" />
        <h:outputText styleClass="datatext" value="#{AntibodyBean.antibody.seqStartLocation}"/>
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{AntibodyBean.antibody.seqEndLocation > 0}">        
        <h:outputText value="Carboxy-terminus(end)" />
        <h:outputText styleClass="datatext" value="#{AntibodyBean.antibody.seqEndLocation}" />
		<h:outputText value="&nbsp" escape="false"/>
		<h:outputText value="&nbsp" escape="false"/>       
	  </h:panelGrid>

      <h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%">                
        <h:outputText value="Antibody Type" />
        <h:panelGrid columns="2" columnClasses="plaintext,datatext">
          <h:outputText value="Type:" />
          <h:outputText value="#{AntibodyBean.antibody.type}" />
          
          <h:outputText value="Immunoglobulin Isotype:" />
          <h:outputText value="#{AntibodyBean.antibody.immunoglobulinIsotype}" />

          <h:outputText value="Chain Subtype:" />
          <h:outputText value="#{AntibodyBean.antibody.chainType}" />
        </h:panelGrid>


        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        <h:outputText value="Probe Type" />
        <h:outputText styleClass="datatext" value="antibody" />
                
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="IHC Data" />
		
		<h:dataTable cellspacing="5" value="#{AntibodyBean.antibody.ishFilteredSubmissions}" var="sub">
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
        
               
      </h:panelGrid>
	
	<jsp:include page="/includes/footer.jsp" />
    
</f:view>