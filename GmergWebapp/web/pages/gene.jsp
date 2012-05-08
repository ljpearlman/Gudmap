<!-- Author: Mehran Sharghi																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h3>Gene Details</h3>
	<h:outputText value="There is no available data on the selected gene." styleClass="plaintext" rendered="#{geneInfoBean.gene == null}" />
	<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{geneInfoBean.gene != null}">
		<h:outputText value="Symbol" />
		<h:outputText styleClass="plaintextbold" value="#{geneInfoBean.gene.symbol}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Name" />
		<h:outputText styleClass="datatext" value="#{geneInfoBean.gene.name}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Synonyms" />
		<h:outputText styleClass="datatext" value="#{geneInfoBean.gene.synonyms}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Chromosome" rendered="#{siteSpecies != 'Xenopus laevis'}" />
		<h:panelGrid columns="2" columnClasses="plaintext,datatext" rendered="#{siteSpecies != 'Xenopus laevis'}">
			<h:outputText value="Chromosome:" />
			<h:outputText value="#{geneInfoBean.gene.xsomeName}" />
			<h:outputText value="Start:" />
			<h:outputText value="#{geneInfoBean.gene.xsomeStart}" />
			<h:outputText value="End:" />
			<h:outputText value="#{geneInfoBean.gene.xsomeEnd}" />
			<h:outputText value="Genome Build:" />
			<h:outputText value="#{geneInfoBean.gene.genomeBuild}" />
		</h:panelGrid>
		<f:verbatim rendered="#{siteSpecies != 'Xenopus laevis'}" >&nbsp;</f:verbatim><f:verbatim rendered="#{siteSpecies != 'Xenopus laevis'}" >&nbsp;</f:verbatim>
	</h:panelGrid>

	<f:subview id="geneStripTable" rendered="#{geneInfoBean.gene!=null}">
		<h:outputText value="" rendered="#{geneInfoBean.setTableViewNameToGeneStrip}" />
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<h:panelGrid columns="2" rowClasses="header-nostripe,header-stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{proj != 'EuReGene' && geneInfoBean.gene != null}">
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Probes Linked to Gene" />
		<h:panelGrid columns="2" columnClasses="datatext,datatext">
		<%--  Bernie 30/06/2011 Mantis 558 Task2 - mod to display the maprobe id --%>
		<%--  Bernie 4/7/2011 Mantis 558 Task4 - mod to provide hyperlinks --%>
		<h:dataTable value="#{geneInfoBean.gene.assocProbes}" var="probe">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{probe[3]}" rendered="#{probe[3] != 'probe.html'}">
					<h:outputText value="#{probe[1]}"/>
				</h:outputLink>
				<h:outputLink styleClass="datatext" value="#{probe[3]}" rendered="#{probe[3] == 'probe.html'}">
					<f:param name="probe" value="#{probe[2]}" />
					<h:outputText value="#{probe[1]}"/>
				</h:outputLink>
			</h:column>
			<h:column>
				<h:outputLink styleClass="datatext" value="probe.html">
					<f:param name="probe" value="#{probe[1]}" />
					<f:param name="maprobe" value="#{probe[2]}" />
					<h:outputText value="(#{probe[2]})" rendered="#{probe[1] != probe[2]}"/>
				</h:outputLink>
			</h:column>			
		</h:dataTable>
		</h:panelGrid>
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>

	<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{geneInfoBean.gene != null}">
		<h:outputText value="Links" />
		<h:panelGrid columns="2" columnClasses="plaintext,datatext">
			<h:outputText value="MGI:" rendered="#{siteSpecies != 'Xenopus laevis'}" />
			<h:outputText value="HGNC:" rendered="#{siteSpecies == 'Xenopus laevis'}" />
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.mgiURL}" rendered="#{!geneInfoBean.xenbaseEntryExists}" target="_blank">
				<h:outputText value="#{geneInfoBean.gene.mgiAccID}" />
			</h:outputLink>
			<h:outputLink styleClass="plaintext" rendered="#{siteSpecies == 'Xenopus laevis' && geneInfoBean.xenbaseEntryExists}" value="#{geneInfoBean.gene.hgncSearchSymbolURL}" target="_blank">
				<h:outputText value="Search HGNC" />
			</h:outputLink>
			
			<h:outputText value="Xenbase:" rendered="#{siteSpecies == 'Xenopus laevis' && geneInfoBean.xenbaseEntryExists}" />
			<h:outputLink rendered="#{siteSpecies == 'Xenopus laevis' && geneInfoBean.xenbaseEntryExists}" styleClass="plaintext" value="#{geneInfoBean.gene.mgiURL}" target="_blank">
				<h:outputText value="#{geneInfoBean.gene.mgiAccID}" />
			</h:outputLink>
			
			<h:outputText value="Ensembl:" rendered="#{siteSpecies != 'Xenopus laevis'}" />
			<h:outputLink rendered="#{siteSpecies != 'Xenopus laevis'}" styleClass="plaintext" value="#{geneInfoBean.gene.ensemblURL}" target="_blank">
				<h:outputText value="#{geneInfoBean.gene.ensemblID}" />
			</h:outputLink>
<%-- 
			<h:outputText value="RefSeq:" rendered="#{siteSpecies != 'Xenopus laevis'}" />
			<h:outputLink rendered="#{siteSpecies != 'Xenopus laevis'}" styleClass="plaintext" value="#{geneInfoBean.gene.refSeqURL}" target="_blank">
				<h:outputText value="#{geneInfoBean.gene.refSeqID}" />
			</h:outputLink>
--%>						
			<h:outputText value="UCSC:" rendered="#{siteSpecies != 'Xenopus laevis'}" />
			<h:outputLink rendered="#{siteSpecies != 'Xenopus laevis'}" styleClass="plaintext" value="#{geneInfoBean.gene.ucscURL}" target="_blank">
				<h:outputText value="View probes for this gene in UCSC Browser" />
			</h:outputLink>
			
			<h:outputText value="OMIM:" />
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.omimURL}" target="_blank">
				<h:outputText value="Search OMIM" />
			</h:outputLink>
			
			<h:outputText value="Entrez:" />
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.entrezURL}" target="_blank">
				<h:outputText rendered="#{geneInfoBean.entrezIdExists}" value="#{geneInfoBean.gene.entrezID}" />
				<h:outputText rendered="#{!geneInfoBean.entrezIdExists}" value="Search entrez" />
			</h:outputLink>
			
			<h:outputText value="Genecards:" />
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.geneCardURL}" target="_blank">
				<h:outputText value="Search GeneCards" />
			</h:outputLink>
			
			<h:outputText value="GO:" />
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.goURL}#tabular" rendered="#{siteSpecies != 'Xenopus laevis'}" target="_blank">
				<h:outputText value="MGI GO Annotations" />
			</h:outputLink>
			<h:outputLink styleClass="plaintext" value="#{geneInfoBean.gene.goURL}" rendered="#{siteSpecies == 'Xenopus laevis'}" target="_blank">
				<h:outputText value="Search GO" />
			</h:outputLink>
			
			<h:outputText value="GUDMAP:" rendered="#{proj == 'EuReGene'}"/>
			<h:outputLink styleClass="plaintext" value="http://gudmap.hgu.mrc.ac.uk/gudmap/pages/ish_submissions.html" target="_blank" rendered="#{proj == 'EuReGene'}">
				<f:param name="inputType" value="symbol" />
				<f:param name="stage" value="" />
				<f:param name="criteria" value="equals" />
				<f:param name="output" value="gene" />
				<f:param name="ignoreExpression" value="true" />
				<f:param value="#{geneInfoBean.gene.symbol}" name="geneSymbol" />
				<f:param value="geneQueryISH" name="queryType" />
				<h:outputText value="Search GUDMAP" />
			</h:outputLink>
			
			<h:outputText value="EuReGene:" rendered="#{proj == 'EuReGene'}"/>
			<h:outputLink styleClass="plaintext" value="/euregenedb/pages/ish_submissions.html" target="_blank" rendered="#{proj == 'EuReGene' && siteSpecies == 'Xenopus laevis'}">
				<f:param name="inputType" value="symbol" />
				<f:param name="stage" value="" />
				<f:param name="criteria" value="equals" />
				<f:param name="output" value="gene" />
				<f:param name="ignoreExpression" value="true" />
				<f:param value="#{geneInfoBean.gene.symbol}" name="geneSymbol" />
				<f:param value="geneQueryISH" name="queryType" />
				<h:outputText value="Search EuReGene mouse" />
			</h:outputLink>

			<h:outputLink styleClass="plaintext" value="/xgebase/pages/ish_submissions.html" target="_blank" rendered="#{proj == 'EuReGene' && siteSpecies == 'mouse'}">
				<f:param name="inputType" value="symbol" />
				<f:param name="stage" value="" />
				<f:param name="criteria" value="equals" />
				<f:param name="output" value="gene" />
				<f:param name="ignoreExpression" value="true" />
				<f:param value="#{geneInfoBean.gene.symbol}" name="geneSymbol" />
				<f:param value="geneQueryISH" name="queryType" />
				<h:outputText value="Search XGEbase" />
			</h:outputLink>
		</h:panelGrid>

		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="In-Situs" />
		<h:dataTable cellspacing="5" value="#{geneInfoBean.gene.ishSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
				<%--<h:outputText value=" (#{sub[2]}, #{sub[5]}, #{sub[8]}, #{sub[3]}, #{sub[6]})" styleClass="datatext" />--%>
				<%--<h:outputText value=" (#{sub[2]}, #{sub[5]}, #{sub[3]}, #{sub[6]})" styleClass="datatext" />--%>
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
			<%--  Bernie 4/7/2011 Mantis 558 Task4 - mod to provide hyperlinks --%>
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] != 'probe.html'}">
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] == 'probe.html'}">
					<f:param name="probe" value="#{sub[6]}" />
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
			</h:column>
			<%-- Bernie 30/06/2011 Mantis 558 Task2 --%>
			<h:column>
				<h:outputLink styleClass="datatext" value="probe.html">
					<f:param name="probe" value="#{sub[6]}" />
					<f:param name="maprobe" value="#{sub[9]}" />
					<h:outputText value=" (#{sub[9]})" rendered="#{sub[9] != ''}"/>
				</h:outputLink>
			</h:column>
		</h:dataTable>
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	
	<h:panelGrid columns="1" rowClasses="header-stripey" columnClasses="leftCol" width="100%" cellpadding="0" cellspacing="0" rendered="#{geneInfoBean.gene != null}">
		<h:outputText value="Microarrays"/>
	</h:panelGrid>

   	<f:subview id="GeneArrayTable" rendered="#{geneInfoBean.gene.numMicArrays != null && geneInfoBean.gene.numMicArrays > 0}">
		<h:outputText value="" rendered="#{geneInfoBean.setTableViewNameToArrayData}" />
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	<h:outputText value="" rendered="#{UtilityBean.setActiveTableViewName}" />

	<jsp:include page="/includes/footer.jsp" />
    
</f:view>

