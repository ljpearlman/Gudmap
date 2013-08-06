<!-- Author: Mehran Sharghi			 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  
 	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id (#{MicroarraySingleSubmissionBean.id})" rendered="#{!MicroarraySingleSubmissionBean.exist}"/>
                <h:panelGrid columns="1"  rendered="#{MicroarraySingleSubmissionBean.exist}" >
	         <h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as private" rendered="#{!MicroarraySingleSubmissionBean.submission.released && !MicroarraySingleSubmissionBean.submission.deleted}"/>
	         <h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as deleted" rendered="#{MicroarraySingleSubmissionBean.submission.deleted && MicroarraySingleSubmissionBean.submission.released}"/>
	         <h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.id} cannot be displayed because it is marked as private and deleted" rendered="#{!MicroarraySingleSubmissionBean.submission.released && MicroarraySingleSubmissionBean.submission.deleted}"/>
	         <h:outputText styleClass="plaintextbold" value="<br/><br/>For assistance please contact " rendered="#{!MicroarraySingleSubmissionBean.submission.released || MicroarraySingleSubmissionBean.submission.deleted}" escape="false"/>
	         <h:outputLink styleClass="text_bottom" value="mailto:GUDMAP-EDITORS@gudmap.org" rendered="#{!MicroarraySingleSubmissionBean.submission.released || MicroarraySingleSubmissionBean.submission.deleted}">
		gudmap-editors@gudmap.org
	          </h:outputLink>
                </h:panelGrid>
	
	<h:form id="mainForm" rendered="#{MicroarraySingleSubmissionBean.exist && MicroarraySingleSubmissionBean.submission.released && !MicroarraySingleSubmissionBean.submission.deleted}">

                <h:panelGrid columns="1" width="100%" styleClass="block-stripey">
                          <h:outputText styleClass="plaintextbold" value="#{MicroarraySingleSubmissionBean.submission.accID}" />
                </h:panelGrid>
    
                <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
    	          <h:outputText value="#{stageSeriesLong} Stage" />
    	          <h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{MicroarraySingleSubmissionBean.submission.stage}definition.html">
    		<h:outputText value="#{stageSeriesShort}#{MicroarraySingleSubmissionBean.submission.stage}" />
    	          </h:outputLink>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{null != MicroarraySingleSubmissionBean.submission.originalImages}">
		<h:outputText value="Images"  />
		<h:dataTable  columnClasses="text-normal,text-top" value="#{MicroarraySingleSubmissionBean.submission.originalImages}" var="image">
			        <h:column>
			                <h:panelGrid columns="2" width="180" >
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="mywindow=window.open('#{image[0].clickFilePath}','#{image[0].accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image[0].filePath}" styleClass="topAlign, leftAlign" width="80"/>
					</h:outputLink>
					<h:outputText styleClass="notetext, topAlign, leftAlign" value="#{image[0].note}"/>
				</h:panelGrid>
			        </h:column>
			        <h:column>
			                <h:panelGrid rendered="#{null!=image[1]}" columns="2" width="180" >
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="window.open('#{image[1].clickFilePath}','#{image[1].accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image[1].filePath}"  styleClass="topAlign" width="80"/>
					</h:outputLink>
					<h:outputText styleClass="notetext, topAlign, leftAlign" value="#{image[1].note}"/>
				</h:panelGrid>
			        </h:column>
			        <h:column>
			                <h:panelGrid rendered="#{null!=image[2]}" columns="2" width="180" >
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="window.open('#{image[2].clickFilePath}','#{image[2].accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image[2].filePath}"  styleClass="topAlign" width="80"/>
					</h:outputLink>
					<h:outputText styleClass="notetext, topAlign, leftAlign" value="#{image[2].note}"/>
				</h:panelGrid>
			        </h:column>
			        <h:column>
			                <h:panelGrid rendered="#{null!=image[3]}" columns="2" width="180" >
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="window.open('#{image[3].clickFilePath}','#{image[3].accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image[3].filePath}"  styleClass="topAlign" width="80"/>
					</h:outputLink>
					<h:outputText styleClass="notetext, topAlign, leftAlign" value="#{image[3].note}"/>
				</h:panelGrid>
			        </h:column>
		</h:dataTable>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
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
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey" rendered="#{not empty MicroarraySingleSubmissionBean.submission.archiveId}">
		<h:outputText value="Archive ID"/>
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{MicroarraySingleSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{MicroarraySingleSubmissionBean.submission.archiveId != null}">
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.archiveId}" />
		</h:outputLink>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Principal Investigator(s)" />
		<t:dataList id="piDataList" var="piInfo"
				value="#{MicroarraySingleSubmissionBean.submission.principalInvestigators}">
				<h:outputLink  title="#{piInfo.fullAddress}"  styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
					<h:outputText value="#{piInfo.name}, " />
				</h:outputLink>
				<h:outputText title="#{piInfo.fullAddress}"  styleClass="datatext" value="#{piInfo.displayAddress}" /><br/>
		</t:dataList>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Submitted By" />
		<h:panelGroup>
			<h:outputLink title="#{MicroarraySingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{MicroarraySingleSubmissionBean.submission.submitter.id})">
			               <h:outputText value="#{MicroarraySingleSubmissionBean.submission.submitter.name}, " />
			</h:outputLink>
			<h:outputText title="#{MicroarraySingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{MicroarraySingleSubmissionBean.submission.submitter.displayAddress}" />			
		</h:panelGroup>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Specimen Details" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Sample GEO ID:" />
			<h:panelGroup>
				<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
					<f:param value="#{MicroarraySingleSubmissionBean.submission.sample.geoID}" name="acc" />
					<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.geoID}" />
				</h:outputLink>
			</h:panelGroup>
			
			<h:outputText value="Sample Description:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.description}" />
			
			<h:outputText value="Title:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.title}" />
			
			<h:outputText value="Component(s) Sampled:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.source}" />
			
			<h:outputText value="Organism:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.organism}" />

			<h:outputText value="Tissue:" rendered="#{not empty MicroarraySingleSubmissionBean.submission.tissue}"/>
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.tissue}" rendered="#{not empty MicroarraySingleSubmissionBean.submission.tissue}"/>
			
			<h:outputText value="Strain:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.strain}" />


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
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.sex}" />
			
			<h:outputText value="Development Age:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.devAge}" />
			
			<h:outputText value="#{stageSeriesLong} Stage:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.stage}" />

			<h:outputText value="Developmental Landmark:" rendered="#{not empty MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks}"/>
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks}" rendered="#{not empty MicroarraySingleSubmissionBean.submission.sample.developmentalLandmarks}"/>

			<h:outputText value="Pool Size:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.poolSize}" />
			
			<h:outputText value="Pooled Sample:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.pooledSample}" />
			
			<h:outputText value="Dissection Method:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.dissectionMethod}" />
			
			<h:outputText value="Experimental Design:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.experimentalDesign}" />
		</h:panelGrid>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
		<h:outputText value="Array Hybridization" />
		<h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
			<h:outputText value="Extracted Molecule:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.molecule}" />
			
			<h:outputText value="A260:280 Ratio:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.a_260_280}" />
			
			<h:outputText value="RNA Extraction Protocol:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.extractionProtocol}" />
			
			<h:outputText value="Target Amplification Manufacturer/kit:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationKit}" escape="false" />
			
			<h:outputText value="Target Amplification Protocol:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationProtocol}" />
			
			<h:outputText value="Rounds of Amplification:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.amplificationRounds}" />
			
			<h:outputText value="Amount Labeled Target Hybridized To Array:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.volTargHybrid}" />
			
			<h:outputText value="Label:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.label}" />
			
			<h:outputText value="Label Protocol:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.labelProtocol}" />

			<h:outputText value="Array Hyb/Wash Protocol:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.washScanHybProtocol}" />
			
			<h:outputText value="Scan Protocol:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.scanProtocol}" />
			
			<h:outputText value="GCOS Tgt Value:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.gcosTgtVal}" />
			
			<h:outputText value="Data Analysis Method:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.dataAnalProtocol}" />
			
			<h:outputText value="Reference:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.sample.reference}" />
		</h:panelGrid>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
                                <h:outputText value="Series Details" />
                                <h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
        	                                <h:outputText value="Series GEO ID:" />
        	                                <h:panelGroup>
        		                        <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
        			              <f:param value="#{MicroarraySingleSubmissionBean.submission.series.geoID}" name="acc" />
        			               <h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.geoID}" />
        		                        </h:outputLink>
        	                                </h:panelGroup>
        	
        	                                <h:outputText value="Number of Samples:" />
        	                                <h:panelGroup>
        		                             <h:outputLink styleClass="datatext" value="series.html">
					<f:param value="#{MicroarraySingleSubmissionBean.submission.series.oid}" name="seriesId" />
					<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.numSamples} samples" />
			             </h:outputLink>
		                </h:panelGroup>
			
		                <h:outputText value="Title:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.title}" />
			
			<h:outputText value="Summary:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.summary}" />
			
			<h:outputText value="Type:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.type}" />
			
			<h:outputText value="Overall Design:" />
			<h:outputText value="#{MicroarraySingleSubmissionBean.submission.series.design}" />
		</h:panelGrid>
               </h:panelGrid>
		
               <h:panelGrid columns="2" width="100%" columnClasses="arrayLCol,arrayRCol" styleClass="block-stripey">
                                <h:outputText value="Platform Details" />
                                <h:panelGrid columns="2" columnClasses="data-titleCol,data-textCol, data-textCol">
                                                <h:outputText value="Platform GEO ID:" />
                                                <h:panelGroup>
                                                          <h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi" target="gmerg_external">
                                                                 <f:param value="#{MicroarraySingleSubmissionBean.submission.platform.geoID}" name="acc"/>
                                                                 <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.geoID}" />
                                                          </h:outputLink>
                                                </h:panelGroup>
          
                                                <h:outputText value="Title:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.title}" />

                                                <h:outputText value="Distribution:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.distribution}" />
          
                                                <h:outputText value="Technology:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.technology}" />
          
                                                <h:outputText value="Organism:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.organism}" />
          
                                                <h:outputText value="Manufacturer:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.manufacturer}" />
          
                                                <h:outputText value="Manufacturer Protocol:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.manufactureProtocol}" />
          
                                                <h:outputText value="Catalogue Number:" />
                                                <h:outputText value="#{MicroarraySingleSubmissionBean.submission.platform.catNo}" />
                              </h:panelGrid>
               </h:panelGrid>
  </h:form>  
  <f:subview id="footer">

    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>
