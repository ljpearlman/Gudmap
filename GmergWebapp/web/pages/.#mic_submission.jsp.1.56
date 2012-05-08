<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
    <h:panelGrid columns="1" width="100%" rowClasses="header-stripey,header-nostripe">
    <h:outputText styleClass="plaintextbold" value="#{micSubmissionBean.submission.accID}" />
    <f:verbatim>&nbsp;</f:verbatim>
    </h:panelGrid>
    
    <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" rowClasses="header-stripey,header-nostripe">
    	<h:outputText value="#{stageSeriesLong} Stage" />
<%-- 
    	<h:outputLink styleClass="datatext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{micSubmissionBean.submission.stage}/">
--%>
    	<h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{micSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{stageSeriesShort}#{micSubmissionBean.submission.stage}" />
    	</h:outputLink>

		<%--  commented out until further decision about this page - (Bernie 17/11/2010 Mantis 506 )
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Tissue" />
		<h:outputText value="#{micSubmissionBean.submission.tissue}" />
		--%>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Images" rendered="#{micSubmissionBean.submission.originalImages != null}" />
		<h:dataTable rendered="#{micSubmissionBean.submission.originalImages != null}" columnClasses="text-normal,text-top" value="#{micSubmissionBean.submission.originalImages}" var="image" >
			<h:column>
				<h:outputLink value="#" onclick="window.open('#{image[1]}', '', 'resizable=1,toolbar=0,scrollbars=1,width=600,height=600');return false;">
					<h:graphicImage styleClass="icon" value="#{image[0]}" height="50" onclick=""/>
				</h:outputLink>
			</h:column>
		</h:dataTable>
		
		<f:verbatim rendered="#{micSubmissionBean.submission.originalImages != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{micSubmissionBean.submission.originalImages != null}">&nbsp;</f:verbatim>
		
		<h:outputText value="Supplemental Data Files" />
		<h:panelGrid columns="2" columnClasses="plaintext, datatext">
			<h:outputText value="CEL file:" />
			<h:outputLink styleClass="datatext" value="#{micSubmissionBean.submission.filesLocation}CEL/#{micSubmissionBean.submission.celFile}">
				<h:outputText value="#{micSubmissionBean.submission.celFile}" />
			</h:outputLink>
			
			<h:outputText value="CHP file:" />
			<h:outputLink styleClass="datatext" value="#{micSubmissionBean.submission.filesLocation}CHP/#{micSubmissionBean.submission.chpFile}">
				<h:outputText value="#{micSubmissionBean.submission.chpFile}" />
			</h:outputLink>
			
			<h:outputText value="RPT file:" />
			<h:outputLink styleClass="datatext" value="#{micSubmissionBean.submission.filesLocation}RPT/#{micSubmissionBean.submission.rptFile}">
				<h:outputText value="#{micSubmissionBean.submission.rptFile}" />
			</h:outputLink>
			
			<h:outputText value="TXT file:" />
			<h:outputLink styleClass="datatext" value="#{micSubmissionBean.submission.filesLocation}TXT/#{micSubmissionBean.submission.txtFile}">
				<h:outputText value="#{micSubmissionBean.submission.txtFile}" />
			</h:outputLink>
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

<%-- commented out until further decision about this page - (Mehran 11/06/09)
      <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
      
      <h:outputText value="Gene List:" />
      <f:verbatim>
        <iframe src="gene_list.html" width="615" height="500" align="middle">
        </iframe>
      </f:verbatim>
--%>

		<h:outputText value="Archive ID" rendered="#{micSubmissionBean.submission.archiveId != null}" />
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{micSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{micSubmissionBean.submission.archiveId != null}">
			<h:outputText value="#{micSubmissionBean.submission.archiveId}" />
		</h:outputLink>
		
		<f:verbatim rendered="#{micSubmissionBean.submission.archiveId != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{micSubmissionBean.submission.archiveId != null}">&nbsp;</f:verbatim>
		
		<h:outputText value="Principal Investigator(s)" />
		<h:panelGroup rendered="#{micSubmissionBean.submission.principalInvestigators == null}">
			<h:outputText styleClass="datatext" value="#{micSubmissionBean.submission.principalInvestigator.name}, " />
<%-- 		<h:outputText styleClass="datatext" value="#{micSubmissionBean.submission.principalInvestigator.address}, " />--%>
			<h:outputLink styleClass="datatext" value="javascript:showLabDetails(#{micSubmissionBean.submission.principalInvestigator.id})">
				<h:outputText value="#{micSubmissionBean.submission.principalInvestigator.lab}, #{micSubmissionBean.submission.principalInvestigator.city}, #{micSubmissionBean.submission.principalInvestigator.country}, " />
			</h:outputLink>
			<h:outputLink styleClass="datatext" value="mailto:#{micSubmissionBean.submission.principalInvestigator.email}"> 
				<h:outputText value="#{micSubmissionBean.submission.principalInvestigator.email}" />
			</h:outputLink>
		</h:panelGroup>
		<h:panelGroup rendered="#{micSubmissionBean.submission.principalInvestigators != null}">
			<t:dataList id="piDataList" 
				var="piInfo"
				value="#{micSubmissionBean.submission.principalInvestigators}"
				rowCountVar="count"
				rowIndexVar="index"
				layout="unorderedList">
					<h:panelGroup>
						<h:outputText styleClass="datatext" value="#{piInfo.name}, " />
						<h:outputLink styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
							<h:outputText value="#{piInfo.lab}, #{piInfo.city}, #{piInfo.country}, " />
						</h:outputLink>
						<h:outputLink styleClass="datatext" value="mailto:#{piInfo.email}">
							<h:outputText value="#{piInfo.email}" />
						</h:outputLink>
					</h:panelGroup>
			</t:dataList>
		</h:panelGroup>


                <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Submitted By" />
		<h:panelGroup>
			<h:outputText styleClass="datatext" value="#{micSubmissionBean.submission.submitter.name}, " />
<%--		<h:outputText styleClass="datatext" value="#{micSubmissionBean.submission.submitter.address}, " /> --%>
			<h:outputLink styleClass="datatext" value="javascript:showLabDetails(#{micSubmissionBean.submission.submitter.id})">
				<h:outputText value="#{micSubmissionBean.submission.submitter.lab}, #{micSubmissionBean.submission.submitter.city}, #{micSubmissionBean.submission.submitter.country}, " />			
			</h:outputLink>
			<h:outputLink styleClass="datatext" value="mailto:#{micSubmissionBean.submission.submitter.email}"> 
				<h:outputText value="#{micSubmissionBean.submission.submitter.email}" />
			</h:outputLink>
		</h:panelGroup>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Specimen Details" />
		<h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Sample GEO ID:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:panelGroup>
				<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
					<f:param value="#{micSubmissionBean.submission.sample.geoID}" name="acc" />
					<h:outputText value="#{micSubmissionBean.submission.sample.geoID}" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Sample Description:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.description}" />
			
			<h:outputText value="Title:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.title}" />
			
			<h:outputText value="Component(s) Sampled:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.source}" />
			
			<h:outputText value="Organism:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.organism}" />

			<h:outputText value="Tissue:" rendered="#{micSubmissionBean.submission.tissue != null}"/>
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{micSubmissionBean.submission.tissue != null}"/>
			<h:outputText value="#{micSubmissionBean.submission.tissue}" rendered="#{micSubmissionBean.submission.tissue != null}"/>
			
			<h:outputText value="Strain:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.strain}" />
<%-- 
          <h:outputText value="Mutation:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.sample.mutation}" />
--%>        
			<h:outputText value="Sex:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.sex}" />
			
			<h:outputText value="Development Age:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.devAge}" />
			
			<h:outputText value="#{stageSeriesLong} Stage:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.stage}" />

			<h:outputText value="Developmental Landmark:" rendered="#{micSubmissionBean.submission.sample.developmentalLandmarks != null}"/>
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{micSubmissionBean.submission.sample.developmentalLandmarks != null}"/>
			<h:outputText value="#{micSubmissionBean.submission.sample.developmentalLandmarks}" rendered="#{micSubmissionBean.submission.sample.developmentalLandmarks != null}"/>

			<h:outputText value="Pool Size:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.poolSize}" />
			
			<h:outputText value="Pooled Sample:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.pooledSample}" />
			
			<h:outputText value="Dissection Method:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.dissectionMethod}" />
			
			<h:outputText value="Experimental Design:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.experimentalDesign}" />
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		
		<h:outputText value="Array Hybridization" />
		<h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Extracted Molecule:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="A260:280 Ratio:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.a_260_280}" />
			
			<h:outputText value="RNA Extraction Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.extractionProtocol}" />
			
			<h:outputText value="Target Amplification Manufacturer/kit:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.amplificationKit}" escape="false" />
			
			<h:outputText value="Target Amplification Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.amplificationProtocol}" />
			
			<h:outputText value="Rounds of Amplification:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.amplificationRounds}" />
			
			<h:outputText value="Amount Labeled Target Hybridized To Array:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.volTargHybrid}" />
			
			<h:outputText value="Label:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.label}" />
			
			<h:outputText value="Label Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.labelProtocol}" />

			<h:outputText value="Array Hyb/Wash Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.washScanHybProtocol}" />
			
			<h:outputText value="Scan Protocol:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.scanProtocol}" />
			
			<h:outputText value="GCOS Tgt Value:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.gcosTgtVal}" />
			
			<h:outputText value="Data Analysis Method:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.dataAnalProtocol}" />
			
			<h:outputText value="Reference:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.sample.reference}" />
		</h:panelGrid>
		
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>

        <%-- added by xingjun - 21/07/2009 - end --%>
		<h:outputText value="#{micSubmissionBean.transgenicTitle}" rendered="#{micSubmissionBean.submission.transgenics != null}" />
		<t:dataTable id="transgenicTable" rowClasses="header-stripey" columnClasses="leftCol,rightCol" headerClass="align-top-stripey"
		             value="#{micSubmissionBean.submission.transgenics}" var="transgenics" rowIndexVar="row" rendered="#{micSubmissionBean.submission.transgenics != null}" >
			<t:column>
				<f:verbatim rendered="#{row>0}"> 
					<hr width="100%" align="center"/>
				</f:verbatim>
				<h:outputText value="Allele #{transgenics.serialNo}" styleClass="plaintextbold" rendered="#{micSubmissionBean.submission.multipleTransgenics}"/>
				<h:panelGrid border="0" columns="3" columnClasses="data-titleCol,data-textCol,data-textCol">
					<h:outputText value="Gene Reported:" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputLink styleClass="datatext" value="gene.html">
						<h:outputText value="#{transgenics.geneSymbol}" />
						<f:param name="gene" value="#{transgenics.geneSymbol}" />
					</h:outputLink>

					<h:outputText value="Mutated Gene Id:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
 					<%--<h:outputText value="#{transgenics.geneId}" rendered="#{transgenics.mutantType=='mutant allele'}" />--%>				 					
					<h:outputLink styleClass="datatext" value="#{transgenics.geneIdUrl}" rendered="#{transgenics.mutantType=='mutant allele'}" target="gmerg_external1">
						<h:outputText value="#{transgenics.geneId}" />
					</h:outputLink>
 					
<%-- 
					<h:outputText value="Allele Id:" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{transgenics.mutatedAlleleId}" />
--%>
					<h:outputText value="Reference for Allele Description:"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
					<%--<h:outputText value="#{transgenics.mutatedAlleleId}" escape="false"/>--%>					 				
					<h:outputLink styleClass="datatext" value="#{transgenics.mutatedAlleleIdUrl}" target="gmerg_external2">
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
					<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=alleleDetail&key=1654" rendered="#{transgenics.mutantType=='mutant allele'}"target="gmerg_external3" >
						<h:outputText value="#{transgenics.alleleFirstChrom}" />
					</h:outputLink>	
									
					<h:outputText value="Allele Second Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{transgenics.mutantType=='mutant allele'}" />
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
					<%--<h:outputText value="#{transgenics.alleleSecondChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />--%>					 
					<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=alleleDetail&key=1654" rendered="#{transgenics.mutantType=='mutant allele'}"target="gmerg_external4" >
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
					<%-- Bernie 25/11/2010 - added link (mantis 336) --%>
					<h:outputText value="#{transgenics.notes}" rendered="#{transgenics.notes != null}" escape="false" />
					<%-- commented out by xingjun - 08/03/2011 - no need to use an extra method: added <a> tag into the content in DB then escape it
					<h:outputText value="#{transgenics.notes}" rendered="#{transgenics.notes != null && transgenics.pubUrl == null}"/>					
					<h:outputLink styleClass="datatext" value="#{transgenics.pubUrl}" rendered="#{transgenics.pubUrl != null}"target="gmerg_external5" >
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

		<f:verbatim rendered="#{micSubmissionBean.submission.transgenics != null}">&nbsp;</f:verbatim>
		<f:verbatim rendered="#{micSubmissionBean.submission.transgenics != null}">&nbsp;</f:verbatim>
        
        <h:outputText value="Series Details" />
        <h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
        	<h:outputText value="Series GEO ID:" />
        	<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
        	<h:panelGroup>
        		<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
        			<f:param value="#{micSubmissionBean.submission.series.geoID}" name="acc" />
        			<h:outputText value="#{micSubmissionBean.submission.series.geoID}" />
        		</h:outputLink>
        	</h:panelGroup>
        	
        	<h:outputText value="Number of Samples:" />
        	<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
        	<h:panelGroup>
        		<h:outputLink styleClass="datatext" value="series.html">
<%-- changed to pass series oids to get series info in stead of the series geo ids
          <f:param value="#{micSubmissionBean.submission.series.geoID}" name="seriesId" />
--%>
					<f:param value="#{micSubmissionBean.submission.series.oid}" name="seriesId" />
					<h:outputText value="#{micSubmissionBean.submission.series.numSamples} samples" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Title:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.series.title}" />
			
			<h:outputText value="Summary:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.series.summary}" />
			
			<h:outputText value="Type:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.series.type}" />
			
			<h:outputText value="Overall Design:" />
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
			<h:outputText value="#{micSubmissionBean.submission.series.design}" />
		</h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
        
        <h:outputText value="Platform Details" />
        <h:panelGrid columns="3" columnClasses="data-titleCol,data-textCol, data-textCol">
          <h:outputText value="Platform GEO ID:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:panelGroup>
            <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
              <f:param value="#{micSubmissionBean.submission.platform.geoID}" name="acc"/>
              <h:outputText value="#{micSubmissionBean.submission.platform.geoID}" />
            </h:outputLink>
          </h:panelGroup>
          
          <h:outputText value="Title:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.title}" />

<%-- 
          <h:outputText value="Name:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.name}" />
--%>
          <h:outputText value="Distribution:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.distribution}" />
          
          <h:outputText value="Technology:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.technology}" />
          
          <h:outputText value="Organism:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.organism}" />
          
          <h:outputText value="Manufacturer:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.manufacturer}" />
          
          <h:outputText value="Manufacturer Protocol:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.manufactureProtocol}" />
          
          <h:outputText value="Catalogue Number:" />
          <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
          <h:outputText value="#{micSubmissionBean.submission.platform.catNo}" />
        </h:panelGrid>
        
        <f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
<%-- 
        <h:outputText value="Specimen" />
          <h:panelGrid columns="7" columnClasses="data-titleCol,data-textCol">
            <h:outputLink value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/MAstaging.html" styleClass="plaintext"> 
              <h:outputText value="Stage:" />
            </h:outputLink>
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputLink styleClass="datatext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{micSubmissionBean.submission.stage}">
              <h:outputText value="#{stageSeriesShort}#{micSubmissionBean.submission.stage}" />
            </h:outputLink>  
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Other Staging System:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:panelGroup>
            <h:outputText value="#{micSubmissionBean.submission.specimen.stageFormat} " />
            <h:outputText value="#{micSubmissionBean.submission.specimen.otherStageValue}" />
            </h:panelGroup>
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Strain:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{micSubmissionBean.submission.specimen.strain}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Sex:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{micSubmissionBean.submission.specimen.sex}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Genotype:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:outputText value="#{micSubmissionBean.submission.specimen.genotype}" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
            
            <h:outputText value="Specimen Preparation:" />
            <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
              <h:outputText styleClass="datatext" value="#{micSubmissionBean.submission.specimen.assayType}" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="Fixation Method:" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="#{micSubmissionBean.submission.specimen.fixMethod}" />
                
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="Embedding:" />
                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
                <h:outputText value="#{micSubmissionBean.submission.specimen.embedding}" />
          </h:panelGrid>
--%>          
    </h:panelGrid>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>
