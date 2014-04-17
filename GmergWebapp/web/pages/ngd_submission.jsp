<!-- Author: Mehran Sharghi			 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  
 	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id (#{NGDSingleSubmissionBean.id})" rendered="#{!NGDSingleSubmissionBean.renderPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as private" rendered="#{!NGDSingleSubmissionBean.submission.released && !NGDSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as deleted" rendered="#{NGDSingleSubmissionBean.submission.deleted && NGDSingleSubmissionBean.submission.released}"/>
	<h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.id} cannot be displayed because it is marked as private and deleted" rendered="#{!NGDSingleSubmissionBean.submission.released && NGDSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="<br/><br/>For assistance please contact " rendered="#{!NGDSingleSubmissionBean.submission.released || NGDSingleSubmissionBean.submission.deleted}" escape="false"/>
	<h:outputLink styleClass="text_bottom" value="mailto:GUDMAP-EDITORS@gudmap.org" rendered="#{!NGDSingleSubmissionBean.submission.released || NGDSingleSubmissionBean.submission.deleted}">
		gudmap-editors@gudmap.org
	</h:outputLink>
	
	<h:form id="mainForm" rendered="#{NGDSingleSubmissionBean.renderPage && NGDSingleSubmissionBean.submission.released && !NGDSingleSubmissionBean.submission.deleted}">

		 <h:panelGrid columns="1" width="100%" styleClass="block-stripey">
		           <h:outputText styleClass="plaintextbold" value="#{NGDSingleSubmissionBean.submission.accID}" />
		 </h:panelGrid>
    
         <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
    	    <h:outputText value="#{stageSeriesLong} Stage" />
    	    <h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{NGDSingleSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{stageSeriesShort}#{NGDSingleSubmissionBean.submission.stage}" />
    	    </h:outputLink>
        </h:panelGrid>
        
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
    	    <h:outputText value="External/Internal Links: " />
    	    <h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{NGDSingleSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{NGDSingleSubmissionBean.submission.stage}" />
    	    </h:outputLink>
        </h:panelGrid>
        
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
    	    <h:outputText value="Results Notes: " />
    		<h:outputText value="#{NGDSingleSubmissionBean.submission.stage}" />
        </h:panelGrid>
		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{null != NGDSingleSubmissionBean.submission.originalImages}">
			<h:outputText value="Images"  />
			<h:dataTable  columnClasses="text-normal,text-top" value="#{NGDSingleSubmissionBean.submission.originalImages}" var="image">
			        <h:column>
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="mywindow=window.open('#{image.clickFilePath}','#{image.accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image.filePath}" width="80"/>
					</h:outputLink>
			        </h:column>
			        <h:column>
					<h:outputText styleClass="notetext, topAlign" value="#{image.note}"/>
			        </h:column>
			</h:dataTable>
		</h:panelGrid>
		
        <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Supplementary Data Files" />
		<h:panelGrid columns="2" columnClasses="plaintext, datatext">
			<h:outputText value="Raw file:" />
			<h:outputLink styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.filesLocation}CEL/#{NGDSingleSubmissionBean.submission.celFile}">
				<h:outputText value="#{NGDSingleSubmissionBean.submission.celFile}" />
			</h:outputLink>
			
			<h:outputText value="Processed file(s):" />
			<h:outputLink styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.filesLocation}CHP/#{NGDSingleSubmissionBean.submission.chpFile}">
				<h:outputText value="#{NGDSingleSubmissionBean.submission.chpFile}" />
			</h:outputLink>
<%-- 			
			<h:outputText value="RPT file:" />
			<h:outputLink styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.filesLocation}RPT/#{NGDSingleSubmissionBean.submission.rptFile}">
				<h:outputText value="#{NGDSingleSubmissionBean.submission.rptFile}" />
			</h:outputLink>
			
			<h:outputText value="TXT file:" />
			<h:outputLink styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.filesLocation}TXT/#{NGDSingleSubmissionBean.submission.txtFile}">
				<h:outputText value="#{NGDSingleSubmissionBean.submission.txtFile}" />
			</h:outputLink>
			
--%>
		</h:panelGrid>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{not empty NGDSingleSubmissionBean.submission.archiveId}">
		<h:outputText value="Archive/Batch ID"/>
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{NGDSingleSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{NGDSingleSubmissionBean.submission.archiveId != null}">
			<h:outputText value="#{NGDSingleSubmissionBean.submission.archiveId}" />
		</h:outputLink>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Principal Investigator(s)" />
		<t:dataList id="piDataList" var="piInfo"
				value="#{NGDSingleSubmissionBean.submission.principalInvestigators}">
				<h:outputLink  title="#{piInfo.fullAddress}"  styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
					<h:outputText value="#{piInfo.name}, " />
				</h:outputLink>
				<h:outputText title="#{piInfo.fullAddress}"  styleClass="datatext" value="#{piInfo.displayAddress}" /><br/>
		</t:dataList>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Submitted By" />
		<h:panelGroup>
			<h:outputLink title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{NGDSingleSubmissionBean.submission.submitter.id})">
			               <h:outputText value="#{NGDSingleSubmissionBean.submission.submitter.name}, " />
			</h:outputLink>
			<h:outputText title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.submitter.displayAddress}" />			
		</h:panelGroup>
               </h:panelGrid>
               
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Contributors" />
		<h:panelGroup>
			<h:outputLink title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{NGDSingleSubmissionBean.submission.submitter.id})">
			               <h:outputText value="#{NGDSingleSubmissionBean.submission.submitter.name}, " />
			</h:outputLink>
			<h:outputText title="#{NGDSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{NGDSingleSubmissionBean.submission.submitter.displayAddress}" />			
		</h:panelGroup>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Specimen Details" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Sample GEO ID:" />
			<h:panelGroup>
				<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
					<f:param value="#{NGDSingleSubmissionBean.submission.sample.geoID}" name="acc" />
					<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.geoID}" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Sample Description:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.description}" />
			
			<h:outputText value="Sample Name:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.title}" />
			
			<h:outputText value="Component(s) Sampled:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.source}" />
			
			<h:outputText value="Organism:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.organism}" />

			<h:outputText value="Tissue:" rendered="#{not empty NGDSingleSubmissionBean.submission.tissue}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.tissue}" rendered="#{not empty NGDSingleSubmissionBean.submission.tissue}"/>
			
			<h:outputText value="Strain:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.strain}" />


				<h:outputText value="Genotype:" />
				<h:outputText value="Wild type" rendered="#{null == ISHSingleSubmissionBean.submission.allele}"/>
			                <t:dataTable id="alleleContentTable" 
        	                                                                   value="#{ISHSingleSubmissionBean.submission.allele}" var="allele"  style="margin-left:-5px; " rendered="#{null != ISHSingleSubmissionBean.submission.allele}">
				           <t:column>
				             <h:panelGrid columns="3">
					<h:outputText value="#{allele.title}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />

					<h:outputText value="Gene" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputLink styleClass="datatext" value="gene.html">
						<h:outputText value="#{allele.geneSymbol}" />
						<f:param name="gene" value="#{allele.geneSymbol}" />
					</h:outputLink>

					<h:outputText value="MGI ID"  rendered="#{not empty allele.alleleId}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleId}"/>
					<h:outputText rendered="#{empty allele.alleleIdUrl && not empty allele.alleleId}" value="#{allele.alleleId}" escape="false"/>
					<h:outputLink rendered="#{not empty allele.alleleIdUrl && not empty allele.alleleId}"  styleClass="datatext" value="#{allele.alleleIdUrl}" target="gmerg_external2">
						<h:outputText value="#{allele.alleleId}" />
					</h:outputLink>

					<h:outputText value="MGI Symbol or lab name"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{allele.alleleName}" escape="false" />

					<h:outputText value="Type"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputText value="#{allele.type}"/>
			
					<h:outputText value="Allele First Chromatid" rendered="#{not empty allele.alleleFirstChrom}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleFirstChrom}" />
					<h:outputText value="#{allele.alleleFirstChrom}" rendered="#{not empty allele.alleleFirstChrom}" escape="false"/>
									
					<h:outputText value="Allele Second Chromatid"  rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:outputText value="#{allele.alleleSecondChrom}"  rendered="#{not empty allele.alleleSecondChrom}" escape="false"/>
					
					<h:outputText value="Notes:" rendered="#{not empty allele.notes}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.notes}"/>
					<h:outputText value="#{allele.notes}" rendered="#{not empty allele.notes}" escape="false" />
				         </h:panelGrid>
				    </t:column>
			                </t:dataTable>

			<h:outputText value="Sex:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.sex}" />
			
			<h:outputText value="Development Age:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.devAge}" />
			
			<h:outputText value="#{stageSeriesLong} Stage:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.stage}" />

			<h:outputText value="Developmental Landmark:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.developmentalLandmarks}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.developmentalLandmarks}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.developmentalLandmarks}"/>

			<h:outputText value="Pooled Sample:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.pooledSample}" />
			
			<h:outputText value="Pool Size:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.poolSize}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.poolSize}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.poolSize}" />			
			
			<h:outputText value="Dissection Method:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.dissectionMethod}" />
			
			<h:outputText value="Experiment Design:" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.experimentalDesign}"/>
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.experimentalDesign}" rendered="#{not empty NGDSingleSubmissionBean.submission.sample.experimentalDesign}" />
		
			<h:outputText value="Sample Notes:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.dissectionMethod}" />
		</h:panelGrid>
               </h:panelGrid>
		
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Protocol Details" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Library Strategy:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="Extracted Molecule:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="Isolation Method:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.a_260_280}" />
			
			<h:outputText value="Library Construction Protocol:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.extractionProtocol}" />
			
			<h:outputText value="Labelling Method:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationKit}" escape="false" />
			
			<h:outputText value="Sequencing Method:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationProtocol}" />
			
			<h:outputText value="Single or Paired End:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationRounds}" />
			
			<h:outputText value="Instrument Model:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.volTargHybrid}" />
			
			<h:outputText value="Protocol Notes:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.label}" />
			
			<h:outputText value="Library Pool Size:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.labelProtocol}" />

			<h:outputText value="Library Reads:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.washScanHybProtocol}" />
			
			<h:outputText value="Read Length:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.scanProtocol}" />
			
			<h:outputText value="Mean Insert Size:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.gcosTgtVal}" />
		</h:panelGrid>
       </h:panelGrid>
       
       <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Data Processing and Transcript Profile" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Total No of Reads (RAW Only):" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="Reads Before Cleanup (RAW Only):" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="Single or Paired End:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.a_260_280}" />
			
			<h:outputText value="Processing Step:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.extractionProtocol}" />
			
			<h:outputText value="Genome Build or Alignment Reference Sequence:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationKit}" escape="false" />
			
			<h:outputText value="% Aligned to Genome:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationProtocol}" />
			
			<h:outputText value="% UnAligned to Genome:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.amplificationRounds}" />
			
			<h:outputText value="% RNA Reads:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.volTargHybrid}" />
			
			<h:outputText value="Format and Content:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.sample.label}" />
		</h:panelGrid>
       </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
                                <h:outputText value="Series Details" />
                                <h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
        	                                <h:outputText value="Series GEO ID:" />
        	                                <h:panelGroup>
        		                        <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
        			              <f:param value="#{NGDSingleSubmissionBean.submission.series.geoID}" name="acc" />
        			               <h:outputText value="#{NGDSingleSubmissionBean.submission.series.geoID}" />
        		                        </h:outputLink>
        	                                </h:panelGroup>
        	
        	                                <h:outputText value="Number of Samples:" />
        	                                <h:panelGroup>
        		                             <h:outputLink styleClass="datatext" value="series.html">
					<f:param value="#{NGDSingleSubmissionBean.submission.series.oid}" name="seriesId" />
					<h:outputText value="#{NGDSingleSubmissionBean.submission.series.numSamples} samples" />
			             </h:outputLink>
		                </h:panelGroup>
			
		                <h:outputText value="Title:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.title}" />
			
			<h:outputText value="Summary:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.summary}" />
			
			<h:outputText value="Type:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.type}" />
			
			<h:outputText value="Overall Design:" />
			<h:outputText value="#{NGDSingleSubmissionBean.submission.series.design}" />
		</h:panelGrid>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
                                <h:outputText value="Platform Details" />
                                <h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
                                                <h:outputText value="Platform GEO ID:" />
                                                <h:panelGroup>
                                                          <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
                                                                 <f:param value="#{NGDSingleSubmissionBean.submission.platform.geoID}" name="acc"/>
                                                                 <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.geoID}" />
                                                          </h:outputLink>
                                                </h:panelGroup>
          
                                                <h:outputText value="Title:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.title}" />

                                                <h:outputText value="Distribution:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.distribution}" />
          
                                                <h:outputText value="Technology:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.technology}" />
          
                                                <h:outputText value="Organism:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.organism}" />
          
                                                <h:outputText value="Manufacturer:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.manufacturer}" />
          
                                                <h:outputText value="Manufacturer Protocol:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.manufactureProtocol}" />
          
                                                <h:outputText value="Catalogue Number:" />
                                                <h:outputText value="#{NGDSingleSubmissionBean.submission.platform.catNo}" />
                              </h:panelGrid>
               </h:panelGrid>
  </h:form>  
  <f:subview id="footer">

    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>
