<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%-- moved style definition to gudmap_css.css - xingjun - 30/07/2010 --%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id (#{ISHSingleSubmissionBean.id})" rendered="#{!ISHSingleSubmissionBean.renderPage}"/>
	<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.id} cannot be displayed because it is marked as private" rendered="#{!ISHSingleSubmissionBean.submission.released && !ISHSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.id} cannot be displayed because it is marked as deleted" rendered="#{ISHSingleSubmissionBean.submission.deleted && ISHSingleSubmissionBean.submission.released}"/>
	<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.id} cannot be displayed because it is marked as private and deleted" rendered="#{!ISHSingleSubmissionBean.submission.released && ISHSingleSubmissionBean.submission.deleted}"/>
	<h:outputText styleClass="plaintextbold" value="<br/><br/>For assistance please contact " rendered="#{!ISHSingleSubmissionBean.submission.released || ISHSingleSubmissionBean.submission.deleted}" escape="false"/>
	<h:outputLink styleClass="text_bottom" value="mailto:GUDMAP-EDITORS@gudmap.org" rendered="#{!ISHSingleSubmissionBean.submission.released || ISHSingleSubmissionBean.submission.deleted}">
		gudmap-editors@gudmap.org
	</h:outputLink>


</p>	
	<h:form id="mainForm" rendered="#{ISHSingleSubmissionBean.renderPage && ISHSingleSubmissionBean.submission.released && !ISHSingleSubmissionBean.submission.deleted}">
		<h:panelGrid width="100%"  border="0" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol">
		<h:panelGrid border="0" columns="1">
			<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID}" rendered="#{empty ISHSingleSubmissionBean.submission.euregeneId}" />
			<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID} (#{ISHSingleSubmissionBean.submission.euregeneId})" rendered="#{not empty ISHSingleSubmissionBean.submission.euregeneId}"/>
		                   
            <h:graphicImage value="../images/GUDMAP_Logo.png" styleClass="icon" height="50" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}"/>
            <h:graphicImage value="../images/button_euregene2.png" styleClass="icon" height="50" rendered="#{ISHSingleSubmissionBean.submission.project == 'EUREGENE'}"/>
		</h:panelGrid>

		<h:panelGrid width="100%" border="0" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol">
			<h:outputText value="Gene:"/>
			<h:panelGroup >
				<h:outputLink styleClass="plaintext" value="gene.html">
					<h:outputText value="#{ISHSingleSubmissionBean.submission.geneSymbol}" />
					<f:param name="geneId" value="#{ISHSingleSubmissionBean.submission.geneId}" />
				</h:outputLink>
				<h:outputText styleClass="datatext" value=", #{ISHSingleSubmissionBean.submission.geneName}" rendered="#{not empty ISHSingleSubmissionBean.submission.geneName}"/>
			</h:panelGroup>
			
			<h:outputText value="Stage:" />
			<h:outputLink styleClass="plaintext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/#{ISHSingleSubmissionBean.submission.stageLowerCase}definition.html" rendered="#{ISHSingleSubmissionBean.submission.specimen.species == 'Mus musculus'}">
				<h:outputText value="#{ISHSingleSubmissionBean.submission.stageName}" rendered="#{ISHSingleSubmissionBean.submission.specimen.species == 'Mus musculus'}"/>
			</h:outputLink>
				<h:outputText value="#{ISHSingleSubmissionBean.submission.stageName}" rendered="#{ISHSingleSubmissionBean.submission.specimen.species == 'Homo sapiens'}"/>
				
			<h:outputText value="Tissue:" />
			<h:outputText value="#{ISHSingleSubmissionBean.submission.tissue}"/> 

			<h:outputText value="Species:" />
			<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.species}"/> 

			<h:outputText value="Assay Type:"/>
			<h:outputText value="#{ISHSingleSubmissionBean.submission.assayType} #{ISHSingleSubmissionBean.submission.specimen.assayType}" rendered="#{ISHSingleSubmissionBean.submission.specimen.assayType != 'unspecified' }"/> 
			<h:outputText value="#{ISHSingleSubmissionBean.submission.assayType}" rendered="#{ISHSingleSubmissionBean.submission.specimen.assayType == 'unspecified' }"/> 
		</h:panelGrid>

		</h:panelGrid>


			



		<h:panelGrid  width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" >
			<h:outputText value="Images" />
			<h:dataTable  columnClasses="text-normal,text-top" value="#{ISHSingleSubmissionBean.submission.originalImages}" var="image">
			        <h:column>
					<h:outputLink value="#" styleClass="plaintext" target="_blank"
					                       onclick="mywindow=window.open('#{image.clickFilePath}','#{image.accessionId}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=1000,height=1000');return false">
						<h:graphicImage value="#{image.filePath}"/>
					</h:outputLink>
			        </h:column>
			        <h:column>
					<h:outputText styleClass="notetext, topAlign" value="#{image.note}"/>
			        </h:column>
			</h:dataTable>
			<%-- <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{null != ISHSingleSubmissionBean.submission.wlzImage}"/>
			<h:panelGroup rendered="#{null != ISHSingleSubmissionBean.submission.wlzImage}">
					<h:outputText  value="View 3D opt image: "></h:outputText>
					<h:outputLink value="#{ISHSingleSubmissionBean.submission.wlzImage.clickFilePath}">
						<h:graphicImage value="../images/opt_logo.png" height="30" width="30"/>
					</h:outputLink>			
			</h:panelGroup> --%>
			
			<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{null != ISHSingleSubmissionBean.submission.wlzImage}" />
			<%-- <h:outputLink value="#{ISHSingleSubmissionBean.submission.wlzImage.filePath}" rendered="#{null != ISHSingleSubmissionBean.submission.wlzImage}">
					<h:outputText value="Download 3D images in woolz format "></h:outputText>
			</h:outputLink> --%>
			<h:outputText value="3D OPT - Awaiting data."  rendered="#{null != ISHSingleSubmissionBean.submission.wlzImage}"></h:outputText>

		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" rendered="#{not empty ISHSingleSubmissionBean.submission.resultNotes}">
			<h:outputText value="Results Notes" />
			<h:dataTable columnClasses="text-normal,text-top" value="#{ISHSingleSubmissionBean.submission.resultNotes}" var="note">
				<h:column>
					<h:outputText value="#{note}"/>
				</h:column>
			</h:dataTable>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="1" styleClass="block-stripey" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP' && (ISHSingleSubmissionBean.submission.assayType == 'ISH' || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)') && ISHSingleSubmissionBean.submission.specimen.assayType == 'wholemount'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="Whole-mount in situ hybridization is subject to technical limitations that may influence accuracy of the data (" />
				<h:outputLink styleClass="plaintext" value="#" onclick="var w=window.open('wish_moreInfo.jsf','wholemount','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;" >
					<h:outputText value="more info" />
				</h:outputLink>
				<h:outputText styleClass="plaintextbold" value=")." />
			</h:panelGroup>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="1" styleClass="block-stripey" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP' && ISHSingleSubmissionBean.submission.assayType == 'TG' && ISHSingleSubmissionBean.submission.specimen.assayType == 'mouse marker strain'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="Mouse marker strain entries are unique GUDMAP entries that can contain data for multiple probes at multiple stages of development (" />
				<h:outputLink styleClass="plaintext" value="#" onclick="var w=window.open('tg_moreInfo.jsf','mouse marker strain','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;" >
					<h:outputText value="more info" />
				</h:outputLink>
				<h:outputText styleClass="plaintextbold" value=")." />
			</h:panelGroup>
		</h:panelGrid>
  
		<h:panelGrid width="100%" columns="1" styleClass="block-stripey" rendered="#{ISHSingleSubmissionBean.submission.project == 'EUREGENE'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="IMPORTANT NOTE ABOUT EUREGENE ANNOTATIONS: " />
				<h:outputText styleClass="plaintext" value="Annotations within this entry are currently under review by the GUDMAP editorial office and Kylie Georgas (Little Lab, University of Queensland). In due course the annotations will be changed to reflect this review process. Original EuReGene annotations will continue to be available from www.euregene.org" />
			</h:panelGroup>
		</h:panelGrid>
  
		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" >
			<h:outputText value="Submitters" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
		            <h:outputText value="Principal Investigator(s):" />
			        <t:dataList id="piDataList" var="piInfo" value="#{ISHSingleSubmissionBean.submission.principalInvestigators}" >
						<h:outputLink  title="#{piInfo.fullAddress}"  styleClass="datatext" value="javascript:showLabDetails(#{piInfo.id})">
						      <h:outputText value="#{piInfo.name}, " />
						</h:outputLink>
						<h:outputText title="#{piInfo.fullAddress}"  styleClass="datatext" value="#{piInfo.displayAddress}" /><br/>
			        </t:dataList>
					<h:outputText value="Contributors:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.authors}" />
					<h:outputText value="Submitted By:" />
					<h:panelGroup>
                            <h:outputLink title="#{ISHSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="javascript:showLabDetails(#{ISHSingleSubmissionBean.submission.submitter.id})">
					             <h:outputText value="#{ISHSingleSubmissionBean.submission.submitter.name}, " />
					       </h:outputLink>
					       <h:outputText title="#{ISHSingleSubmissionBean.submission.submitter.fullAddress}" styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.submitter.displayAddress}" />
					</h:panelGroup>
			
			<h:outputText value="Archive ID:" rendered="#{ISHSingleSubmissionBean.submission.archiveId > 0}"/>
			<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{ISHSingleSubmissionBean.submission.archiveId}" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.archiveId > 0}">
				<h:outputText value="#{ISHSingleSubmissionBean.submission.archiveId}"    rendered="#{ISHSingleSubmissionBean.submission.archiveId > 0}"/>
			</h:outputLink>
			<h:outputText value="Batch ID:"  rendered="#{ISHSingleSubmissionBean.submission.batchId > 0}"/>
			<h:outputLink value="/gudmap/pages/focus_insitu_browse.html?batchId=#{ISHSingleSubmissionBean.submission.batchId}" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.batchId > 0}">
				<h:outputText value="#{ISHSingleSubmissionBean.submission.batchId}"  rendered="#{ISHSingleSubmissionBean.submission.batchId > 0}"/>
			</h:outputLink>

			
					<h:outputText value="Submission ID:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.labId}" />
				</h:panelGrid>
				
			</h:panelGrid>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol">
			<h:panelGroup>
				<h:outputText value="Expression Mapping <br/><br/>" escape="false"/>
				<h:outputText value="Expression Strengths Key:" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}">
					<h:graphicImage value="/images/tree/DetectedRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (unspecified strength)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/StrongRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (strong)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/ModerateRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (moderate)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/WeakRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (weak)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/PossibleRound20x20.gif" styleClass="icon" />
					<h:outputText value="Uncertain" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/NotDetectedRoundMinus20x20.gif" styleClass="icon" />
					<h:outputText value="Not Detected" styleClass="plaintext" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
				</h:panelGrid>
					
				<h:outputText value="Expression Patterns Key:" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}">
					<h:graphicImage value="/images/tree/HomogeneousRound20x20.png" styleClass="icon" />
					<h:outputText value="Homogeneous" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/GradedRound20x20.png" styleClass="icon" />
					<h:outputText value="Graded" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/RegionalRound20x20.png" styleClass="icon" />
					<h:outputText value="Regional" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/SpottedRound20x20.png" styleClass="icon" />
					<h:outputText value="Spotted" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/UbiquitousRound20x20.png" styleClass="icon" />
					<h:outputText value="Ubiquitous" styleClass="plaintext" />
<!--  					
					<h:graphicImage value="/images/tree/OtherRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project != 'GUDMAP'}" />
					<h:outputText value="Other" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					<h:outputText value="Restricted" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					<h:outputText value="Single cell" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
-->	
					<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" />
					<h:outputText value="Restricted" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" />
					<h:outputText value="Single cell" styleClass="plaintext" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>				
				</h:panelGrid>

				<h:outputText value="Nerve Density: <br/><br/>" escape="false" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}" />
				<h:outputText value="Relative to Total:" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}">
					
					<h:graphicImage value="/images/tree/max_high.png" styleClass="icon" />
					<h:outputText value="Maximum" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/mod_medium.png" styleClass="icon" />
					<h:outputText value="Moderate" styleClass="plaintext" />

					<h:graphicImage value="/images/tree/min_low.png" styleClass="icon" />
					<h:outputText value="Low" styleClass="plaintext" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>	
				</h:panelGrid>
				<h:outputText value="Relative to P0/Adult:" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}" />
				<h:panelGrid columns="2" rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}">

					<h:graphicImage value="/images/tree/inc_large.png" styleClass="icon" />
					<h:outputText value="Increase, large" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/inc_small.png" styleClass="icon" />
					<h:outputText value="Increase, small" styleClass="plaintext" />

					<h:graphicImage value="/images/tree/dec_large.png" styleClass="icon" />
					<h:outputText value="Decrease, large" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/dec_small.png" styleClass="icon" />
					<h:outputText value="Decrease, small" styleClass="plaintext" />
					
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					<h:graphicImage value="/images/tree/note.gif" styleClass="icon" />
					<h:outputText value="Contains note" styleClass="plaintext" />
				</h:panelGrid>
			</h:panelGroup>
			
			
			
			
			<h:panelGroup>
				<h:outputText value="No Expression Mapping Data Available" styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.expressionMapped}" />
				
				<h:panelGrid columns="3" columnClasses="table-stripey" bgcolor="white" cellspacing="2" cellpadding="4">
					<h:commandLink action="#{ISHSingleSubmissionBean.annotation}" styleClass="plaintext">
						<h:outputText value="#{ISHSingleSubmissionBean.annotationDisplayLinkTxt}" />
						<f:param name="annotationDisplay" value="#{ISHSingleSubmissionBean.annotationDisplayType}"/>
						<f:param name="displayOfAnnoGps" value="#{ISHSingleSubmissionBean.displayOfAnnoGps}"/>
					</h:commandLink>
					<h:commandLink action="#{ISHSingleSubmissionBean.displayOfAnnotatedGps}" rendered="#{ISHSingleSubmissionBean.annotationDisplayType!='list'}" styleClass="plaintext" >
						<h:outputText value="#{ISHSingleSubmissionBean.displayOfAnnotatedGpsTxt}" />
						<f:param name="annotationDisplay" value="#{ISHSingleSubmissionBean.annotationDisplayType}"/>
						<f:param name="displayOfAnnoGps" value="#{ISHSingleSubmissionBean.displayOfAnnoGps}"/>
					</h:commandLink>
					<f:verbatim>
						<a href="#Link260010Context" name="Link260010Context" id="Link260010Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID260010', 'Gene Query', 'Clicking on these buttons allows you to alter the display format of the annotations. Subsequent visits to this page will use your last selected format options.', 'Link260010Context')"> 
							<img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" />
						</a>
					</f:verbatim>
				</h:panelGrid>
				<h:inputHidden value="#{ISHSingleSubmissionBean.submissionID}" />
				<h:panelGroup rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType == 'list'}">
					<h:outputText value="<script type='text/javascript'>SUBMISSION_ID ='#{ISHSingleSubmissionBean.submission.accID}'</script>" escape="false"/>
					<h:dataTable cellpadding="2" width="100%" headerClass="align-left" rowClasses="table-nostripe, table-stripey" 
								 columnClasses="align-left" value="#{ISHSingleSubmissionBean.submission.annotatedComponents}" var="component" >
						<h:column>
							<f:facet name="header">
								<h:outputText value="ComponentID" styleClass="plaintextbold"/>
							</f:facet>
							<h:outputLink styleClass="datatext" value="javascript:showExprInfo('#{component.componentId}','#{component.componentName}','0')">
								<h:outputText value="#{component.componentId}" />
							</h:outputLink>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Component " styleClass="plaintextbold"/>
							</f:facet>
							<h:outputLink styleClass="datatext" value="javascript:showExprInfo('#{component.componentId}','#{component.componentName}','0')">
								<h:outputText value="#{component.componentName}" />
								<h:graphicImage value="../images/tree/note.gif" rendered="#{component.noteExists}" styleClass="icon" />
							</h:outputLink>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Expression" styleClass="plaintextbold"/>
							</f:facet>
							<h:panelGrid columns="2">
								<h:graphicImage value="#{component.expressionImage}" styleClass="icon" alt="" />
								<h:panelGroup>
									<h:outputText value="#{component.primaryStrength} " styleClass="datatext" />
									<h:outputText rendered="#{component.secondaryStrength != ' ' && component.secondaryStrength != ''}" value="(#{component.secondaryStrength})" styleClass="datatext" />
									<h:outputText rendered="#{component.secondaryStrength == ' ' || component.secondaryStrength == ''}" value="(unspecified)" styleClass="datatext" />
								</h:panelGroup>
							</h:panelGrid>
						</h:column>
						<h:column rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}">
							<f:facet name="header">
								<h:outputText value="Patterns/Locations" styleClass="plaintextbold"/>
							</f:facet>
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{not empty component.pattern}">
								<h:column>
									<h:panelGrid columns="2">
										<h:graphicImage value="#{pattern.patternImage}" styleClass="icon" alt="" />
										<h:panelGrid columns="2" columnClasses="text-top">
											<h:outputText value="#{pattern.pattern}" styleClass="datatext"/>
											<h:outputText value="#{pattern.locations}" styleClass="datatext"/>
										</h:panelGrid>
									</h:panelGrid>
								</h:column>
							</h:dataTable>
						</h:column>
						<h:column rendered="#{ISHSingleSubmissionBean.submission.project == 'EUREGENE'}">
							<f:facet name="header">
								<h:outputText value="Pattern" styleClass="plaintextbold"/>
							</f:facet>
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{not empty component.pattern}">
								<h:column>
									<h:panelGrid columns="2">
										<h:graphicImage value="#{pattern.patternImage}" styleClass="icon" alt="" />
										<h:outputText value="#{pattern.pattern}" styleClass="datatext"/>
									</h:panelGrid>
								</h:column>
							</h:dataTable>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Nerve Densities" styleClass="plaintextbold"/>
							</f:facet>
							<h:panelGrid columns="2">
									<h:graphicImage value="#{component.densityImageRelativeToTotal}" styleClass="icon" alt="" rendered="#{component.densityImageRelativeToTotal != null}"/>
									<h:outputText value="Rel to Total: #{component.densityRelativeToTotal}" styleClass="datatext" rendered="#{component.densityImageRelativeToTotal != null}"/>
									<h:graphicImage value="#{component.densityImageRelativeToAge}" styleClass="icon" alt="" rendered="#{component.densityImageRelativeToAge != null}"/>
									<h:outputText value="Rel to P0/Adult: #{component.densityChange}" styleClass="datatext" rendered="#{component.densityImageRelativeToAge != null}"/>
							</h:panelGrid>
						</h:column>
					</h:dataTable>
				</h:panelGroup>
				<h:panelGroup rendered="#{ISHSingleSubmissionBean.expressionMapped && ISHSingleSubmissionBean.annotationDisplayType != 'list'}">
					<h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
						<h:outputText value="Javascript Tree Menu" />
					</h:outputLink>
					<f:verbatim>
						<script type="text/javascript">
							<c:forEach items="${ISHSingleSubmissionBean.submission.annotationTree}" var="row">
								<c:out value="${row}" escapeXml="false"/>
							</c:forEach>
						</script>
						<script type="text/javascript">initializeDocument('${ISHSingleSubmissionBean.displayOfAnnoGps}')</script>
						<noscript>
							<span class="plaintext">A tree of annotated anatomical components will open here if you enable JavaScript in your browser.</span>
						</noscript>
						&nbsp;
					</f:verbatim>
				</h:panelGroup>
			</h:panelGroup>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'IHC' || ISHSingleSubmissionBean.submission.assayType == 'OPT'}">
			<h:outputText value="Antibody"  />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
					<h:outputText value="Protein:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.geneSymbol}" />
								<f:param name="geneId" value="#{ISHSingleSubmissionBean.submission.geneId}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.geneName} " />
							<h:outputLink styleClass="datatext" value="http://www.ncbi.nlm.nih.gov/gene/#{ISHSingleSubmissionBean.submission.antibody.locusTag}" rendered="#{ISHSingleSubmissionBean.submission.specimen.species == 'Homo sapiens'}">
								<h:outputText value=" (NCBI ID: #{ISHSingleSubmissionBean.submission.antibody.locusTag})" />
							</h:outputLink>							
							<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/accession/#{ISHSingleSubmissionBean.submission.antibody.locusTag}" rendered="#{ISHSingleSubmissionBean.submission.specimen.species != 'Homo sapiens'}">
								<h:outputText value=" (#{ISHSingleSubmissionBean.submission.antibody.locusTag})" />
							</h:outputLink>
						</h:panelGroup>
					</h:panelGrid>
				
					<h:outputText rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE'}" value="Name:" />
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE'}">
						<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.name}" />
					</h:panelGroup>
			
					<h:outputText value="Accession ID:" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
						<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}">
							<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/accession/#{ISHSingleSubmissionBean.submission.antibody.accessionId}" rendered="#{not empty ISHSingleSubmissionBean.submission.antibody.accessionId}">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.accessionId} " />
							</h:outputLink>	
							<h:outputText value="No MGI ID " rendered="#{empty ISHSingleSubmissionBean.submission.antibody.accessionId}"/>

 							<h:outputLink styleClass="datatext" value="antibody.html" target="_blank">
								<f:param name="antibody" value="#{ISHSingleSubmissionBean.submission.antibody.maProbeId}" />
								<h:outputText value=" (#{ISHSingleSubmissionBean.submission.antibody.maProbeId})" />
							</h:outputLink>
 
  						</h:panelGroup>
			
					<h:outputText value="Epitope (Uniprot ID):"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.uniprotId}" rendered="#{not empty ISHSingleSubmissionBean.submission.antibody.uniprotId}"/>
					<h:outputText value="unknown" rendered="#{empty ISHSingleSubmissionBean.submission.antibody.uniprotId}"/>
					<h:outputText value="Amino-terminus (start):" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation}" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="Carboxy-terminus (end):" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.seqEndLocation}" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
			
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Supplier: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.supplier}" />
			
					<h:outputText value="Catalogue Number: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.catalogueNumber}" />
			
					<h:outputText value="Lot Number: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.lotNumber}" />
			
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Type: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.type}" />
			
					<h:outputText value="Host: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.host}"/>
					
					<h:outputText value="Purification Method: "/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.purificationMethod}" />

					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Immunoglobulin Isotype:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.immunoglobulinIsotype}" />
					
					<h:outputText value="Variant Detected:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.detectedVariantValue}" />
					
					<h:outputText value="Species Specificity:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.speciesSpecificity}" />
					
					<h:outputText value="Detection System:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Direct Label: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.directLabel}"/>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Secondary Antibody: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.secondaryAntibody}"/>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Signal Detection Method: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.signalDetectionMethod}"/>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Detection Notes: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.detectionNotes}"/>
						</h:panelGroup>
					</h:panelGrid>
			
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
			

					<h:outputText styleClass="plaintext,text-top" value="Dilution:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.dilution}" />

					<h:outputText styleClass="plaintext,text-top" value="Notes:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.notes}" />
								  
					<h:outputText styleClass="plaintext,text-top" value="Lab Antibody ID:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.labProbeId}" />
				</h:panelGrid>
			</h:panelGrid>
		 </h:panelGrid>

		 <h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'ISH' || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)'}">
		                <h:outputText value="Probe"  />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
				
					<h:outputText value="Gene:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.geneSymbol}" />
								<f:param name="geneId" value="#{ISHSingleSubmissionBean.submission.geneId}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.geneName} " />
							<h:outputText styleClass="datatext" value="(" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.geneIdUrl}" />
							<h:outputLink styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.geneIdUrl}" >
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.geneID}" />
							</h:outputLink>
							<h:outputText styleClass="datatext" value=")" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.geneIdUrl}" />
						</h:panelGroup>
					</h:panelGrid>
									
					<h:outputText  value="Probe ID:" />
					<h:panelGroup>
						<h:outputLink styleClass="datatext" rendered="#{ISHSingleSubmissionBean.renderPrbNameURL}" value="#{ISHSingleSubmissionBean.submission.probe.probeNameURL}" target="_blank">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>
						<h:outputLink styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL && ISHSingleSubmissionBean.submission.probe.maprobeID != ISHSingleSubmissionBean.submission.probe.probeName}" value="#{ISHSingleSubmissionBean.submission.probe.probeNameURL}">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>	
					    <h:outputText value="No MGI ID " rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL && ISHSingleSubmissionBean.submission.probe.maprobeID == ISHSingleSubmissionBean.submission.probe.probeName}"/>
						<h:outputLink styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL && ISHSingleSubmissionBean.submission.probe.maprobeID == ISHSingleSubmissionBean.submission.probe.probeName}" value="probe.html" target="_blank">
							<f:param name="probe" value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ISHSingleSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" (#{ISHSingleSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>	

						<h:outputLink styleClass="datatext" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.maprobeID && ISHSingleSubmissionBean.submission.probe.maprobeID != ISHSingleSubmissionBean.submission.probe.probeName}" value="probe.html" target="_blank">
							<f:param name="probe" value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ISHSingleSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" (#{ISHSingleSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>
					</h:panelGroup>
					
					<h:outputText value="Name of cDNA:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.cloneName}" />
					
					<h:outputText value="Additional Name of cDNA:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.additionalCloneName}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.additionalCloneName}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.additionalCloneName}" />
					
					<h:outputText value="Sequence ID:" />
					<h:panelGrid  rowClasses="text-top" columns="1" border="0" >
						<h:panelGroup>
							<h:outputLink target="_blank" styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.genbankURL}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.genbankID}" >
								<h:outputText  value="#{ISHSingleSubmissionBean.submission.probe.genbankID}" />
							</h:outputLink>
							<h:outputText styleClass="datatext" value="unknown" rendered="#{empty ISHSingleSubmissionBean.submission.probe.genbankID}" />
						</h:panelGroup>
					</h:panelGrid>
					
					<h:outputText styleClass="plaintext" value="5' primer sequence:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq5Primer && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ISHSingleSubmissionBean.submission.probe.seq5Primer}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq5Primer && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="3' primer sequence:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq3Primer && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ISHSingleSubmissionBean.submission.probe.seq3Primer}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq3Primer && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="5' primer location:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq5Loc && ISHSingleSubmissionBean.submission.probe.seq5Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seq5Loc}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq5Loc && ISHSingleSubmissionBean.submission.probe.seq5Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					
					<h:outputText styleClass="plaintext" value="3' primer location:"  rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq3Loc && ISHSingleSubmissionBean.submission.probe.seq3Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seq3Loc}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.seq3Loc && ISHSingleSubmissionBean.submission.probe.seq3Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText value="Template Sequence:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.fullSequence}"/>
					<h:outputLink styleClass="datatext" value="#" onclick="var w=window.open('sequence.jsf?id=#{ISHSingleSubmissionBean.submission.accID}','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;"  rendered="#{not empty ISHSingleSubmissionBean.submission.probe.fullSequence}">
						<h:outputText value="(Click to see sequence.)" />
					</h:outputLink>
					
					<h:outputText value="Origin of Clone used to make the Probe:" />
					<h:panelGrid columns="4" border="0">
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.source}" />
						<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
						<h:outputText styleClass="plaintext" value="Strain:" />
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.strain}" />
					
						<f:verbatim>&nbsp;</f:verbatim>
						<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
						<h:outputText styleClass="plaintext" value="Tissue:" />
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.tissue}" />
					</h:panelGrid>
					
					<h:outputText value="Probe Type:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.type}" />
					
					<h:outputText value="Type:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.geneType}" />
					
					<h:outputText value="Labelled with:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.labelProduct}" />
					
					<h:outputText value="Visualisation method:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.visMethod}" />
					
					<h:outputText value="Lab Probe ID:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.labProbeId}" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.labProbeId}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.labProbeId}" />
					
					<h:outputText styleClass="plaintext,text-top" value="Probe Notes:" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.notes}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.notes}" rendered="#{not empty ISHSingleSubmissionBean.submission.probe.notes}" />

					<h:outputText value="Curator Notes:" rendered="#{null != ISHSingleSubmissionBean.submission.probe.maprobeNotes}" />
					<h:dataTable value="#{ISHSingleSubmissionBean.submission.probe.maprobeNotes}" var="maprobeNote" rendered="#{null != ISHSingleSubmissionBean.submission.probe.maprobeNotes}">
						<h:column>
							<h:outputText styleClass="datatext" value="#{maprobeNote}" />
						</h:column>
					</h:dataTable>
				</h:panelGrid>
			</h:panelGrid>
       	               </h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol">
			<h:outputText value="Specimen" />
			<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
				<h:outputText value="Stage:" />
					
				<h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/#{ISHSingleSubmissionBean.submission.stageLowerCase}definition.html" rendered="#{ISHSingleSubmissionBean.submission.specimen.species == 'Mus musculus'}">
				<h:outputText value="#{ISHSingleSubmissionBean.submission.stage}" />
				</h:outputLink>
				
				<h:outputText value="#{ISHSingleSubmissionBean.submission.stage}" rendered="#{ISHSingleSubmissionBean.submission.specimen.species != 'Mus musculus'}"/>

				<h:outputText rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.otherStage}" value="Other Staging System:" />
				<h:outputText rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.otherStage}" value="#{ISHSingleSubmissionBean.submission.specimen.otherStage}" />

				<h:outputText value="Tissue:" />
				<h:outputText value="#{ISHSingleSubmissionBean.submission.tissue}" />
					
				<h:outputText value="Strain:" />
				<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.strain}" />

				<h:outputText value="Genotype:" />
				<h:outputText value="wild type" rendered="#{null == ISHSingleSubmissionBean.submission.allele}"/>
			                <t:dataTable id="alleleContentTable" 
        	                                                                   value="#{ISHSingleSubmissionBean.submission.allele}" var="allele"  style="margin-left:-5px; "  rendered="#{null != ISHSingleSubmissionBean.submission.allele}">
				           <t:column>
				             <h:panelGrid columns="3">
					<h:outputText value="#{allele.title}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />

					<h:outputText value="Gene" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
					<h:outputLink styleClass="datatext" value="gene.html">
						<h:outputText value="#{allele.geneSymbol}" />
						<f:param name="geneId" value="#{allele.geneId}" />
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
					<h:outputText value="#{allele.alleleFirstChrom}" escape="false" rendered="#{not empty allele.alleleFirstChrom}" />
									
					<h:outputText value="Allele Second Chromatid"  rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.alleleSecondChrom}"/>
					<h:outputText value="#{allele.alleleSecondChrom}"  escape="false" rendered="#{not empty allele.alleleSecondChrom}" />
					
					<h:outputText value="Notes:" rendered="#{not empty allele.notes}" />
					<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" rendered="#{not empty allele.notes}"/>
					<h:outputText value="#{allele.notes}" rendered="#{not empty allele.notes}" escape="false" />
				         </h:panelGrid>
				    </t:column>
			                </t:dataTable>
					
			                <h:outputText value="Sex:" />
				<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.sex}" />
					
				<h:outputText value="Specimen Preparation:"  rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.fixMethod || not empty ISHSingleSubmissionBean.submission.specimen.embedding}" />
				<h:panelGrid columns="2" columnClasses="text-top, text-bottom" style="margin-left:-5px; "   rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.fixMethod || not empty ISHSingleSubmissionBean.submission.specimen.embedding}" >
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.assayType}" />
					<h:panelGrid columns="3">
					                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1"  rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.fixMethod}" />
						<h:outputText styleClass="plaintext" value="Fixation Method:" rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.fixMethod}" />
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.fixMethod}"  rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.fixMethod}" />

					                <h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1"  rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.embedding}" />
						<h:outputText styleClass="plaintext" value="Embedding:" rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.embedding}" />
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.embedding}"  rendered="#{not empty ISHSingleSubmissionBean.submission.specimen.embedding}" />
					</h:panelGrid>
				</h:panelGrid>
				
				<h:outputText value="Experiment Notes:" rendered="#{null != ISHSingleSubmissionBean.submission.specimen.notes}"/>
				<h:dataTable value="#{ISHSingleSubmissionBean.submission.specimen.notes}" var="note" rendered="#{null != ISHSingleSubmissionBean.submission.specimen.notes}">
                                                                         <h:column>
					< h:outputText value="#{note}" />
                                                                         </h:column>
			                 </h:dataTable>
			 </h:panelGrid>  
		</h:panelGrid>  

		<h:panelGrid width="100%" columns="2" rendered="#{ISHSingleSubmissionBean.submission.transgenic}"  styleClass="block-stripey" columnClasses="leftCol,rightCol" >
			<h:outputText value="Transgene Visualisation" />
			<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
				<h:outputText value="Reporter:" />
				<h:outputText value="#{ISHSingleSubmissionBean.submission.allele[0].reporter}" />
			
				<h:outputText value="Direct method for visualising reporter:" />
				<h:outputText value="#{ISHSingleSubmissionBean.submission.allele[0].visMethod}" />
		               </h:panelGrid>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{not empty ISHSingleSubmissionBean.submission.linkedPublications}">
			<h:outputText value="Linked Publications" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable  value="#{ISHSingleSubmissionBean.submission.linkedPublications}" var="pub">
					<h:column>
						<%-- <h:outputText styleClass="plaintext" value="#{pub[0]} " />
						<h:outputText styleClass="plaintext" value="(#{pub[1]}) " />
						<h:outputText styleClass="plaintextbold" value="#{pub[2]} " />
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[3]}, " rendered="#{not empty pub[3]}" />
                        <h:outputText styleClass="plaintextbold" value="#{pub[4]}" rendered="#{not empty pub[4]}"/>
                        <h:outputText styleClass="plaintext" value=", " rendered="#{not empty pub[4]}" />
                        <h:outputText styleClass="plaintext" value="#{pub[6]}" /> --%>
                        <h:outputText styleClass="plaintext" value="#{pub[0]}" /> <verbatim><br></verbatim> 
                        <h:outputText styleClass="plaintextbold" value="#{pub[2]}" /><verbatim><br></verbatim> 
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[3]}, " rendered="#{not empty pub[3]}" />
						<h:outputText styleClass="plaintext" value="#{pub[1]}, " />
						<h:outputText styleClass="plaintextbold" value="#{pub[4]}" rendered="#{not empty pub[4]}"/>
						<h:outputText styleClass="plaintextbold" value="(#{pub[5]})" rendered="#{not empty pub[5]}"/>
						<h:outputText styleClass="plaintextbold" value=":#{pub[6]}" rendered="#{not empty pub[6]}"/><verbatim><br></verbatim> 
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[7]}: " rendered="#{not empty pub[7]}" />
                        <h:outputText styleClass="plaintextbold" value="#{pub[8]}" rendered="#{not empty pub[8]}"/>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{null != ISHSingleSubmissionBean.submission.linkedSubmissions}">
			<h:outputText value="Linked Submissions" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{ISHSingleSubmissionBean.submission.linkedSubmissions}" var="link">
					<h:column>
						<h:panelGrid columns="1">
							<h:panelGroup>
								<h:outputText styleClass="plaintextbold" value="Database: " />
								<h:outputText styleClass="plaintextbold" value="#{link[0]}" />
							</h:panelGroup>
					
							<h:dataTable styleClass="browseTable" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey"
							               bgcolor="white" cellpadding="5" value="#{link[1]}" var="accessionIDAndTypes">
								<h:column>
									<f:facet name="header">
										<h:outputText value="Accession ID" styleClass="plaintextbold" />
									</f:facet>
									<h:outputLink styleClass="plaintext" id="submissionID" value="ish_submission.html">
										<f:param name="id" value="#{accessionIDAndTypes[0]}" />
									 	<h:outputText value="#{accessionIDAndTypes[0]}"/>
					 				</h:outputLink>
					 			</h:column>
								<h:column>
									<f:facet name="header">
					   					<h:outputText value="Link Type(s)" styleClass="plaintextbold" />
									</f:facet>
									<t:dataList id="linkTypes" styleClass="plaintext" rowIndexVar="index"
					                		    var="linkType" value="#{accessionIDAndTypes[1]}" layout="simple" rowCountVar="count" >
					 					<h:outputText styleClass="plaintext" value="#{linkType}" />
										<f:verbatim>&nbsp;</f:verbatim>
									</t:dataList>
								</h:column>
							</h:dataTable>
					 
							<h:panelGroup rendered="#{not empty link[2]}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="plaintext" value="http://www.gudmap.org">
									<h:outputText styleClass="datatext" value="#{link[2]}" />
								</h:outputLink>
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" styleClass="block-stripey" columnClasses="leftCol,rightCol"  rendered="#{null != ISHSingleSubmissionBean.submission.acknowledgements}">
			<h:outputText value="Acknowledgements" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{ISHSingleSubmissionBean.submission.acknowledgements}" var="ack">
					<h:column>
						<h:outputText value = "#{ack}"/>
					</h:column>
				</h:dataTable>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
					
	<jsp:include page="/includes/footer.jsp" />
</f:view>