<!-- Author: Mehran Sharghi			 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  
 	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id (#{MicroarraySingleSubmissionBean.id})" rendered="#{!MicroarraySingleSubmissionBean.renderPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as private" rendered="#{!MicroarraySingleSubmissionBean.publicPage && !MicroarraySingleSubmissionBean.deletedPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as deleted" rendered="#{MicroarraySingleSubmissionBean.deletedPage && MicroarraySingleSubmissionBean.publicPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as private and deleted" rendered="#{!MicroarraySingleSubmissionBean.publicPage && MicroarraySingleSubmissionBean.deletedPage}"/>
	<h:outputText styleClass="plaintextbold" value="<br/><br/>For assistance please contact " rendered="#{!MicroarraySingleSubmissionBean.publicPage || MicroarraySingleSubmissionBean.deletedPage}" escape="false"/>
	<h:outputLink styleClass="text_bottom" value="mailto:GUDMAP-EDITORS@gudmap.org" rendered="#{!MicroarraySingleSubmissionBean.publicPage || MicroarraySingleSubmissionBean.deletedPage}">
		gudmap-editors@gudmap.org
	</h:outputLink>
	
	<h:form id="mainForm" rendered="#{MicroarraySingleSubmissionBean.renderPage && MicroarraySingleSubmissionBean.publicPage && !MicroarraySingleSubmissionBean.deletedPage}">

    <h:panelGrid columns="1" width="100%" rowClasses="header-stripey,header-nostripe">
    <h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.submission.accID}" />
    <f:verbatim>&nbsp;</f:verbatim>
    </h:panelGrid>
    
    <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" rowClasses="header-stripey,header-nostripe">
    	<h:outputText value="#{stageSeriesLong} Stage" />
    	<h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{MicroarraySingleSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{stageSeriesShort}#{MicroarraySingleSubmissionBean.submission.stage}" />
    	</h:outputLink>

		<%--  commented out until further decision about this page - (Bernie 17/11/2010 Mantis 506 )
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Tissue" />
		<h:outputText value="#{MicroarraySingleSubmissionBean.submission.tissue}" />
		--%>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Images" rendered="#{MicroarraySingleSubmissionBean.submission.originalImages != null}" />
		<h:dataTable rendered="#{MicroarraySingleSubmissionBean.submission.originalImages != null}" columnClasses="text-normal,text-top" value="#{MicroarraySingleSubmissionBean.submission.originalImages}" var="image" >
			<h:column>
				<h:outputLink value="#" onclick="window.open('#{image[1]}', '', 'resizable=1,toolbar=0,scrollbars=1,width=600,height=600');return false;">
					<h:graphicImage styleClass="icon" value="#{image[0]}" height="50" onclick=""/>
				</h:outputLink>
			</h:column>
		</h:dataTable>
		
		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.originalImages != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.originalImages != null}">&nbsp;</f:verbatim>
		
		<h:outputText value="Supplemental Data Files" />
		<h:panelGrid columns="2" columnClasses="plaintext, datatext">
			<h:outputText value="CEL file:" />
			<h:outputLink styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.filesLocation}CEL/#{MicroarraySingleSubmissionBean.submission.celFile}">
				<h:outputText value="#{MicroarraySingleSubmissionBean.submission.celFile}" />
			</h:outputLink>
			
			<h:outputText value="CHP file:" />
			<h:outputLink styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.filesLocation}CHP/#{MicroarraySingleSubmissionBean.submission.chpFile}">
				<h:outputText value="#{MicroarraySingleSubmissionBean.submission.chpFile}" />
			</h:outputLink>
			
			<h:outputText value="RPT file:" />
			<h:outputLink styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.filesLocation}RPT/#{MicroarraySingleSubmissionBean.submission.rptFile}">
				<h:outputText value="#{MicroarraySingleSubmissionBean.submission.rptFile}" />
			</h:outputLink>
			
			<h:outputText value="TXT file:" />
			<h:outputLink styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.filesLocation}TXT/#{MicroarraySingleSubmissionBean.submission.txtFile}">
				<h:outputText value="#{MicroarraySingleSubmissionBean.submission.txtFile}" />
			</h:outputLink>
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

		<h:outputText value="Archive ID" rendered="#{MicroarraySingleSubmissionBean.submission.archiveId != null}" />
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{MicroarraySingleSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{MicroarraySingleSubmissionBean.submission.archiveId != null}">
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.archiveId}" />
		</h:outputLink>
		
		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.archiveId != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.archiveId != null}">&nbsp;</f:verbatim>
		
		<h:outputText value="Principal Investigator(s)" />
		<t:dataList id="piDataList" var="piInfo"
				value="#{MicroarraySingleSubmissionBean.submission.principalInvestigators}">
				<h:outputLink  title="#{piInfo.fullAddress}"  styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
					<h:outputText value="#{piInfo.name}, " />
				</h:outputLink>
				<h:outputText title="#{piInfo.fullAddress}"  styleClass="datatext" value="#{piInfo.displayAddress}" /><br/>
		</t:dataList>

                                <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Submitted By" />
		<h:panelGroup>
			<h:outputLink title="#{MicroarraySingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{MicroarraySingleSubmissionBean.submission.submitter.id})">
			               <h:outputText value="#{MicroarraySingleSubmissionBean.submission.submitter.name}, " />
			</h:outputLink>
			<h:outputText title="#{MicroarraySingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.submitter.displayAddress}" />			
		</h:panelGroup>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Specimen Details" />
		<h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Sample GEO ID:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:panelGroup>
				<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
					<f:param value="#{MicroarraySingleSubmissionBean.submission.sample.geoID}" name="acc" />
					<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.geoID}" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Sample Description:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.description}" />
			
			<h:outputText value="Title:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.title}" />
			
			<h:outputText value="Component(s) Sampled:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.source}" />
			
			<h:outputText value="Organism:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.organism}" />

			<h:outputText value="Tissue:" rendered="#{MicroarraySingleSubmissionBean.submission.tissue != null}"/>
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{MicroarraySingleSubmissionBean.submission.tissue != null}"/>
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.tissue}" rendered="#{MicroarraySingleSubmissionBean.submission.tissue != null}"/>
			
			<h:outputText value="Strain:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.strain}" />

			<h:outputText value="Sex:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.sex}" />
			
			<h:outputText value="Development Age:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.devAge}" />
			
			<h:outputText value="#{stageSeriesLong} Stage:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.stage}" />

			<h:outputText value="Developmental Landmark:" rendered="#{MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks != null}"/>
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks != null}"/>
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks}" rendered="#{MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks != null}"/>

			<h:outputText value="Pool Size:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.poolSize}" />
			
			<h:outputText value="Pooled Sample:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.pooledSample}" />
			
			<h:outputText value="Dissection Method:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.dissectionMethod}" />
			
			<h:outputText value="Experimental Design:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.experimentalDesign}" />
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Array Hybridization" />
		<h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Extracted Molecule:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="A260:280 Ratio:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.a_260_280}" />
			
			<h:outputText value="RNA Extraction Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.extractionProtocol}" />
			
			<h:outputText value="Target Amplification Manufacturer/kit:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationKit}" escape="false" />
			
			<h:outputText value="Target Amplification Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationProtocol}" />
			
			<h:outputText value="Rounds of Amplification:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationRounds}" />
			
			<h:outputText value="Amount Labeled Target Hybridized To Array:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.volTargHybrid}" />
			
			<h:outputText value="Label:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.label}" />
			
			<h:outputText value="Label Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.labelProtocol}" />

			<h:outputText value="Array Hyb/Wash Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.washScanHybProtocol}" />
			
			<h:outputText value="Scan Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.scanProtocol}" />
			
			<h:outputText value="GCOS Tgt Value:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.gcosTgtVal}" />
			
			<h:outputText value="Data Analysis Method:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.dataAnalProtocol}" />
			
			<h:outputText value="Reference:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.reference}" />
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

        <%-- added by xingjun - 21/07/2009 - end --%>
		<h:outputText value="#{MicroarraySingleSubmissionBean.transgenicTitle}" rendered="#{MicroarraySingleSubmissionBean.submission.transgenics != null}" />
		<t:dataTable id="transgenicTable" rowClasses="header-stripey" columnClasses="leftCol,rightCol" headerClass="align-top-stripey"
		             value="#{MicroarraySingleSubmissionBean.submission.transgenics}" var="transgenics" rowIndexVar="row" rendered="#{MicroarraySingleSubmissionBean.submission.transgenics != null}" >
			<t:column>
				<f:verbatim rendered="#{row>0}"> 
					<hr width="100%" align="center"/>
				</f:verbatim>
				<h:outputText value="Allele #{transgenics.serialNo}" styleClass="plaintextbold" rendered="#{MicroarraySingleSubmissionBean.submission.multipleTransgenics}"/>
				<h:panelGrid border="0" columns="3" columnClasses="data-titleCol,data-textCol,data-textCol">
					<h:outputText value="Gene Reported:" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputLink styleClass="datatext" value="gene.html">
						<h:outputText value="#{transgenics.geneSymbol}" />
						<f:param name="gene" value="#{transgenics.geneSymbol}" />
					</h:outputLink>

					<h:outputText value="Mutated Gene Id:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:outputLink styleClass="datatext" value="#{transgenics.geneIdUrl}" rendered="#{transgenics.mutantType=='mutant allele'}" target="gmerg_external1">
						<h:outputText value="#{transgenics.geneId}" />
					</h:outputLink>
 					
					<h:outputText value="Reference for Allele Description:"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText rendered="#{empty transgenics.mutatedAlleleIdUrl}" value="#{transgenics.mutatedAlleleId}" escape="false"/>
					<h:outputLink rendered="#{not empty transgenics.mutatedAlleleIdUrl}"  styleClass="datatext" value="#{transgenics.mutatedAlleleIdUrl}" target="gmerg_external2">
						<h:outputText value="#{transgenics.mutatedAlleleId}" />
					</h:outputLink>

					<h:outputText value="Allele Name:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
					<h:outputText value="Allele Description:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{transgenics.mutatedAlleleName}" />

					<h:outputText value="Reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
					<h:outputText value="#{transgenics.labelProduct}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
			
					<h:outputText value="Allele First Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
					<%--<h:outputText value="#{transgenics.alleleFirstChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />--%> 
					<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=alleleDetail&key=1654" rendered="#{transgenics.mutantType=='mutant allele'}" target="gmerg_external3" >
						<h:outputText value="#{transgenics.alleleFirstChrom}" />
					</h:outputLink>	
									
					<h:outputText value="Allele Second Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
					<%--<h:outputText value="#{transgenics.alleleSecondChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />--%>					 
					<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=alleleDetail&key=1654" rendered="#{transgenics.mutantType=='mutant allele'}" target="gmerg_external4" >
						<h:outputText value="#{transgenics.alleleSecondChrom}" />
					</h:outputLink>
					
					<%-- moved here from above Allele First Chromatid --%>
<%-- Jane B. asked this field to be displayed for Mutant Lllele entries as well - 09/03/2011
					<h:outputText value="Direct method for visualising reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{transgenics.visMethod}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
--%>
					<h:outputText value="Direct method for visualising reporter:" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{transgenics.visMethod}" />
			
					<h:outputText value="Notes:" rendered="#{transgenics.notes != null}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{transgenics.notes}" rendered="#{transgenics.notes != null}" escape="false" />
					<%-- commented out by xingjun - 08/03/2011 - no need to use an extra method: added <a> tag into the content in DB then escape it
					<h:outputText value="#{transgenics.notes}" rendered="#{transgenics.notes != null && transgenics.pubUrl == null}"/>					
					<h:outputLink styleClass="datatext" value="#{transgenics.pubUrl}" rendered="#{transgenics.pubUrl != null}" target="gmerg_external5" >
						<h:outputText value="#{transgenics.notes}" />
					</h:outputLink>
					--%>
				
<%-- xingjun - 22/01/2010 - use escape attribute to display url hyperlinks
					<h:panelGroup>
						<h:outputText styleClass="datatext" value="#{transgenics.notePrefix}" />
						<h:outputLink styleClass="datatext" value="#{transgenics.noteUrl}">
							<h:outputText value="#{transgenics.noteUrlText}"/>
						</h:outputLink>
						<h:outputText styleClass="datatext" value="#{transgenics.noteSuffix}" />
					</h:panelGroup>
--%>
				</h:panelGrid>
			</t:column>
		</t:dataTable>

		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.transgenics != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{MicroarraySingleSubmissionBean.submission.transgenics != null}">&nbsp;</f:verbatim>
        
        <h:outputText value="Series Details" />
        <h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
        	<h:outputText value="Series GEO ID:" />
        	<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
        	<h:panelGroup>
        		<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
        			<f:param value="#{MicroarraySingleSubmissionBean.submission.series.geoID}" name="acc" />
        			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.geoID}" />
        		</h:outputLink>
        	</h:panelGroup>
        	
        	<h:outputText value="Number of Samples:" />
        	<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
        	<h:panelGroup>
        		<h:outputLink styleClass="datatext" value="series.html">
<%-- changed to pass series oids to get series info in stead of the series geo ids
          <f:param value="#{MicroarraySingleSubmissionBean.submission.series.geoID}" name="seriesId" />
--%>
					<f:param value="#{MicroarraySingleSubmissionBean.submission.series.oid}" name="seriesId" />
					<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.numSamples} samples" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Title:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.title}" />
			
			<h:outputText value="Summary:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.summary}" />
			
			<h:outputText value="Type:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.type}" />
			
			<h:outputText value="Overall Design:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.design}" />
		</h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        
        <h:outputText value="Platform Details" />
        <h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
          <h:outputText value="Platform GEO ID:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:panelGroup>
            <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
              <f:param value="#{MicroarraySingleSubmissionBean.submission.platform.geoID}" name="acc"/>
              <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.geoID}" />
            </h:outputLink>
          </h:panelGroup>
          
          <h:outputText value="Title:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.title}" />

<%-- 
          <h:outputText value="Name:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.name}" />
--%>
          <h:outputText value="Distribution:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.distribution}" />
          
          <h:outputText value="Technology:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.technology}" />
          
          <h:outputText value="Organism:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.organism}" />
          
          <h:outputText value="Manufacturer:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.manufacturer}" />
          
          <h:outputText value="Manufacturer Protocol:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.manufactureProtocol}" />
          
          <h:outputText value="Catalogue Number:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.catNo}" />
        </h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
<%-- 
        <h:outputText value="Specimen" />
          <h:panelGrid columns="7" columnClasses="data-titleCol,data-textCol">
            <h:outputLink value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/MAstaging.html" styleClass="plaintext"> 
              <h:outputText value="Stage:" />
            </h:outputLink>
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputLink styleClass="datatext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{MicroarraySingleSubmissionBean.submission.stage}">
              <h:outputText value="#{stageSeriesShort}#{MicroarraySingleSubmissionBean.submission.stage}" />
            </h:outputLink>  
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Other Staging System:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:panelGroup>
            <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.stageFormat} " />
            <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.otherStageValue}" />
            </h:panelGroup>
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Strain:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.strain}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Sex:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.sex}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Genotype:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.genotype}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Specimen Preparation:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
              <h:outputText styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.specimen.assayType}" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="Fixation Method:" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.fixMethod}" />
                
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="Embedding:" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.specimen.embedding}" />
          </h:panelGrid>
--%>          
    </h:panelGrid>
  </h:form>  
  <f:subview id="footer">

    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>
