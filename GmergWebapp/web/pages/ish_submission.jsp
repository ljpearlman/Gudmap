<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%-- moved style definition to gudmap_css.css - xingjun - 30/07/2010 --%>
<%-- 
<head>
	<style>
		.width95 {
			width:95%;
		}
		.width5 {
			width:5%;
		}
		td.width5 {
			text-align:right;
			vertical-align:bottom;
		}
		.width85 {
			width:85%;
		}
		.width15 {
			width:15%;
		}		
	</style>
</head>
--%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id" rendered="#{!ISHSingleSubmissionBean.renderPage}"/>
	
	<h:form id="mainForm" rendered="#{ISHSingleSubmissionBean.renderPage}">
<%-- xingjun - 24/11/2009
		<h:panelGrid rendered="#{!UserBean.userLoggedIn}" width="100%" columns="1" rowClasses="header-stripey,header-nostripe">
--%>
		<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe">
			<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID}" rendered="#{ISHSingleSubmissionBean.submission.euregeneId == ''}" />
			<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID} (#{ISHSingleSubmissionBean.submission.euregeneId})" rendered="#{ISHSingleSubmissionBean.submission.euregeneId != ''}"/>
		</h:panelGrid>

		
<%-- added by xingjun - 02/06/2008 - display status note if editor logged in - begin --%>
<%-- xingjun - 24/11/2009
		<h:inputHidden id="hiddenSubId" value="#{EditSubmissionSupportBean.subAccessionId}" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5}"/>
		<h:panelGrid rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5}" width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        	<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID}"/>
        	<h:panelGrid columns="1">
        		<h:dataTable id="statusNoteTable" bgcolor="white" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey"
        					value="#{EditSubmissionBean.statusNotes}" var="statusNotes">
        			<h:column>
        				<f:facet name="header">
        					<h:outputText id="select" value="Select" styleClass="plaintext"/>
        				</f:facet>
        				<h:selectBooleanCheckbox value="#{statusNotes.selected}"/>
        			</h:column>
        			<h:column>
        				<f:facet name="header">
        					<h:outputText styleClass= "plaintext" value="Status Note" />
        				</f:facet>
        				<h:inputText styleClass= "datatext" size="50" value="#{statusNotes.statusNote}" />
        			</h:column>
        		</h:dataTable>
        		<f:verbatim>&nbsp;</f:verbatim>
        		
        		<h:outputText rendered="#{EditSubmissionBean.noStatusNoteCheckedForDeletion}" styleClass="plainred" value="Please make a selection before clicking on the Delete button"/>
        		<h:outputText rendered="#{EditSubmissionBean.emptyStatusNoteExists}" styleClass="plainred" value="Status note could not be empty!"/>
        		
        		<h:panelGrid columns="5" cellspacing="5">
        			<h:commandLink id="deleteStatusNoteLink" value="Delete" action="#{EditSubmissionBean.deleteStatusNote}" >
        				<f:param name="submissionId" value="#{ISHSingleSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<h:commandLink id="addStatusNoteLink" value="Add" action="#{EditSubmissionBean.addStatusNote}" >
        				<f:param name="submissionId" value="#{ISHSingleSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<h:commandLink id="cancelStatusNoteLink" value="Cancel Modification" action="#{EditSubmissionBean.cancelModification}">
        				<f:param name="submissionId" value="#{ISHSingleSubmissionBean.submission.accID}" />
        			</h:commandLink>
        			<f:verbatim>&nbsp;</f:verbatim>
        			<h:commandButton id="commitButton" value="Commit Change" action="#{EditSubmissionBean.commitModification}"/>
        		</h:panelGrid>
        	</h:panelGrid>
        </h:panelGrid>
--%>
<%-- added by xingjun - 02/06/2008 - display status note if editor logged in - end --%>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">

            <h:outputText styleClass="plaintextbold" value="Data Source:" />
                        
            <h:graphicImage value="../images/GUDMAP_Logo.png" styleClass="icon" height="50" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}"/>
            <h:graphicImage value="../images/button_euregene2.png" styleClass="icon" height="50" rendered="#{ISHSingleSubmissionBean.submission.project == 'EUREGENE'}"/>
            <f:verbatim>&nbsp;</f:verbatim>
            <f:verbatim>&nbsp;</f:verbatim>


			<h:outputText value="Gene"/>
			<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.assayType == 'ISH' || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.geneSymbol}" />
					<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.probe.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.geneName}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.assayType == 'IHC' || ISHSingleSubmissionBean.submission.assayType == 'OPT'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.geneSymbol}" />
					<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.antibody.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.geneName}" />
			</h:panelGroup>
			<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.assayType == 'TG'}" >
				<h:outputLink styleClass="plaintext" value="gene.html">
<%-- 
					<h:outputText value="#{ISHSingleSubmissionBean.submission.transgenic.promoter}" />
					<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.transgenic.promoter}" />
--%>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.transgenic.geneSymbol}" />
					<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.transgenic.geneSymbol}" />
				</h:outputLink>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.transgenic.geneName}" />
			</h:panelGroup>
			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>

			<h:outputText value="Theiler Stage" />
<%-- 
			<h:outputLink styleClass="plaintext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{ISHSingleSubmissionBean.submission.stage}" rendered="#{siteSpecies == 'mouse'}">
--%>
			<h:outputLink styleClass="plaintext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{ISHSingleSubmissionBean.submission.stage}definition.html" rendered="#{siteSpecies == 'mouse'}">
				<h:outputText value="#{stageSeriesShort}#{ISHSingleSubmissionBean.submission.stage}" />
			</h:outputLink>
			<h:outputLink styleClass="plaintext" value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" rendered="#{siteSpecies == 'Xenopus laevis'}">
				<h:outputText value="#{stageSeriesShort}#{ISHSingleSubmissionBean.submission.stage}" />
			</h:outputLink>
			
			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>


			<h:outputText value="Tissue" />
			<h:outputText value="#{ISHSingleSubmissionBean.submission.tissue}" /> 


			
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
			<h:outputText value="Images" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable columnClasses="text-normal,text-top" value="#{ISHSingleSubmissionBean.submission.originalImages}" var="image">
					<h:column>
						<h:outputLink rendered="#{ISHSingleSubmissionBean.submission.assayType != 'OPT'}" id="thumbnail" value="#" onclick="openZoomViewer('#{ISHSingleSubmissionBean.submission.accID}', '#{image[2]}', '#{image[4]}'); return false;" >
							<h:graphicImage styleClass="icon" value="#{image[0]}" height="50"/>
						</h:outputLink>
						<h:outputLink rendered="#{ISHSingleSubmissionBean.submission.assayType == 'OPT'}" id="opt_thumbnail" value="#" onclick="window.open('#{image[0]}','#{image[2]}','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,height=500,width=400'); return false;" >
							<h:graphicImage styleClass="icon" value="#{image[3]}" height="50"/>
						</h:outputLink>
					</h:column>
					<h:column>
						<h:outputText styleClass="notetext" value="#{image[1]}"/>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editImage" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
							  onclick="var w=window.open('edit_image.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
    
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP' && (ISHSingleSubmissionBean.submission.assayType == 'ISH' || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)') && ISHSingleSubmissionBean.submission.specimen.assayType == 'wholemount'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="Whole-mount in situ hybridization is subject to technical limitations that may influence accuracy of the data (" />
				<h:outputLink styleClass="plaintext" value="#" onclick="var w=window.open('wish_moreInfo.jsf','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;" >
					<h:outputText value="more info" />
				</h:outputLink>
				<h:outputText styleClass="plaintextbold" value=")." />
			</h:panelGroup>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>
  
		<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe" rendered="#{ISHSingleSubmissionBean.submission.project == 'EUREGENE'}">
			<h:panelGroup>
				<h:outputText styleClass="plaintextbold" value="IMPORTANT NOTE ABOUT EUREGENE ANNOTATIONS: " />
				<h:outputText styleClass="plaintext" value="Annotations within this entry are currently under review by the GUDMAP editorial office and Kylie Georgas (Little Lab, University of Queensland). In due course the annotations will be changed to reflect this review process. Original EuReGene annotations will continue to be available from www.euregene.org" />
			</h:panelGroup>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>
  
		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" >
			<h:outputText value="Submitters" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
					<h:outputText value="Principal Investigator:" />
					<h:panelGroup>
						<h:outputText value="#{ISHSingleSubmissionBean.submission.principalInvestigator.name}, " />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.principalInvestigator.lab}, " />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.principalInvestigator.address}, " />
						<h:outputLink styleClass="datatext" value="mailto:#{ISHSingleSubmissionBean.submission.principalInvestigator.email}">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.principalInvestigator.email}"/>
						</h:outputLink>
					</h:panelGroup>
					<h:outputText value="Authors:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.authors}" />
					<h:outputText value="Submitted By:" />
					<h:panelGroup>
						<h:outputText value="#{ISHSingleSubmissionBean.submission.submitter.name}, " />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.submitter.lab}, " />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.submitter.address}, " />
						<h:outputLink styleClass="datatext" value="mailto:#{ISHSingleSubmissionBean.submission.submitter.email}">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.submitter.email}"/>
						</h:outputLink>
					</h:panelGroup>
					<h:outputText rendered="#{ISHSingleSubmissionBean.submission.archiveId != '0'}" value="Archive ID:" />
					<h:outputLink rendered="#{ISHSingleSubmissionBean.submission.archiveId != '0'}" styleClass="datatext" value="http://www.gudmap.org/Submission_Archive/index.html##{ISHSingleSubmissionBean.submission.archiveId}" >
						<h:outputText value="#{ISHSingleSubmissionBean.submission.archiveId}" />
					</h:outputLink>
					<h:outputText value="Submission ID:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.labId}" />
				</h:panelGrid>
				
				<h:outputLink id="editSubmitters" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
					          onclick="var w=window.open('edit_person.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
			
			<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
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
					<h:outputText value="Possible" styleClass="plaintext" />
					
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
					
					<h:graphicImage value="/images/tree/OtherRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project != 'GUDMAP'}" />
					<h:outputText value="Other" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					<h:outputText value="Restricted" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					<h:outputText value="Single cell" styleClass="plaintext" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
					
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
					<h:commandLink action="#{ISHSingleSubmissionBean.displayOfAnnotatedGps}" rendered="#{siteSpecies!='Xenopus laevis' && ISHSingleSubmissionBean.annotationDisplayType!='list'}" styleClass="plaintext" >
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
									<h:outputText rendered="#{component.secondaryStrength != null && component.secondaryStrength != ''}" value="(#{component.secondaryStrength})" styleClass="datatext" />
								</h:panelGroup>
							</h:panelGrid>
						</h:column>
						<h:column rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}">
							<f:facet name="header">
								<h:outputText value="Patterns/Locations" styleClass="plaintextbold"/>
							</f:facet>
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{component.pattern != null}">
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
							<h:dataTable border="0" cellspacing="0" cellpadding="2" columnClasses="text-top" value="#{component.pattern}" var="pattern" rendered="#{component.pattern != null}">
								<h:column>
									<h:panelGrid columns="2">
										<h:graphicImage value="#{pattern.patternImage}" styleClass="icon" alt="" />
										<h:outputText value="#{pattern.pattern}" styleClass="datatext"/>
									</h:panelGrid>
								</h:column>
							</h:dataTable>
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
					<h:panelGroup rendered="#{siteSpecies != 'Xenopus laevis'}">
						<h:outputText styleClass="plaintextbold" value="G " />
						<h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
		
			<h:outputText value="&nbsp" escape="false"/>
			<h:outputText value="&nbsp" escape="false"/>
		</h:panelGrid>
		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			
			<h:outputText value="Antibody" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'IHC' || ISHSingleSubmissionBean.submission.assayType == 'OPT'}" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" rendered ="#{ISHSingleSubmissionBean.submission.assayType == 'IHC' || ISHSingleSubmissionBean.submission.assayType == 'OPT'}">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
			
					<h:outputText value="Protein:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.geneSymbol}" />
								<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.antibody.geneSymbol}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.geneName} " />
							<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/accession/#{ISHSingleSubmissionBean.submission.antibody.locusTag}">
								<h:outputText value="(#{ISHSingleSubmissionBean.submission.antibody.locusTag})" />
							</h:outputLink>
						</h:panelGroup>
					</h:panelGrid>
				
					<h:outputText rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE' || siteSpecies != 'mouse'}" value="Name:" />
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE' || siteSpecies != 'mouse'}">
						<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.name}" />
					</h:panelGroup>
			
					<h:outputText value="Accession ID:" rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}" />
						<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.project == 'GUDMAP'}">
							<h:outputLink styleClass="datatext" value="http://www.informatics.jax.org/accession/#{ISHSingleSubmissionBean.submission.antibody.accessionId}">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.accessionId} " />
							</h:outputLink>	

 							<h:outputLink styleClass="datatext" value="antibody.html" target="_blank">
								<f:param name="antibody" value="#{ISHSingleSubmissionBean.submission.antibody.maProbeId}" />
								<h:outputText value=" (#{ISHSingleSubmissionBean.submission.antibody.maProbeId})" />
							</h:outputLink>
 
  						</h:panelGroup>
			
					<h:outputText value="Epitope (Uniprot ID):"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.uniprotId}" rendered="#{ISHSingleSubmissionBean.submission.antibody.uniprotId != ''}"/>
					<h:outputText value="unknown" rendered="#{ISHSingleSubmissionBean.submission.antibody.uniprotId == ''}"/>
					<h:outputText value="Amino-terminus (start):" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation}" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="Carboxy-terminus (end):" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.seqEndLocation}" rendered="#{ISHSingleSubmissionBean.submission.antibody.seqStartLocation > 0 && ISHSingleSubmissionBean.submission.antibody.seqEndLocation > 0}"/>
			
					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Supplier: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.supplier}" />
			
					<h:outputText value="Catalogue Number: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.catalogueNumber}" />
			
					<h:outputText value="Lot Number: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.lotNumber}" />
			
					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
			
					<h:outputText value="Antibody Type: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.type}" />
			
					<h:outputText value="Host: " />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.host}"/>
					
					<h:outputText value="Purification Method: "/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.purificationMethod}" />

					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
					
					<h:outputText value="Immunoglobulin Isotype:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.immunoglobulinIsotype}" />
					
					<h:outputText value="Chain Subtype:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.antibody.chainType}" />
					
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
			
					<f:verbatim>&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;</f:verbatim>
			

					<h:outputText styleClass="plaintext,text-top" value="Antibody Dilution:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.dilution}" />

					<h:outputText styleClass="plaintext,text-top" value="Antibody Notes:" rendered="#{ISHSingleSubmissionBean.submission.antibody.notes != null}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.notes}"
								  rendered="#{ISHSingleSubmissionBean.submission.antibody.notes != null}" />
								  
					<h:outputText styleClass="plaintext,text-top" value="Lab Probe ID:" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.antibody.labProbeId}" />
				</h:panelGrid>
				
			</h:panelGrid>

		    <h:outputText value="Probe" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'ISH' || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)'}" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'ISH'  || ISHSingleSubmissionBean.submission.assayType == 'ISH (sense probe)'}">
				<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
				
					<h:outputText value="Gene:" />
					<h:panelGrid columns="1" border="0" columnClasses="text-top,text-normal">
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Symbol: " />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.geneSymbol}" />
								<f:param name="gene" value="#{ISHSingleSubmissionBean.submission.probe.geneSymbol}" />
							</h:outputLink>
						</h:panelGroup>
						<h:panelGroup>
							<h:outputText styleClass="plaintext" value="Name: " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.geneName} " />
							<h:outputText styleClass="datatext" value="(" rendered="#{ISHSingleSubmissionBean.submission.probe.geneIdUrl != null && ISHSingleSubmissionBean.submission.probe.geneIdUrl != ''}" />
							<h:outputLink styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.geneIdUrl}" >
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.geneID}" />
							</h:outputLink>
							<h:outputText styleClass="datatext" value=")" rendered="#{ISHSingleSubmissionBean.submission.probe.geneIdUrl != null && ISHSingleSubmissionBean.submission.probe.geneIdUrl != ''}" />
						</h:panelGroup>
					</h:panelGrid>
									
					<h:outputText rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE' || siteSpecies != 'mouse'}" value="Probe ID:" />
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.project != 'EUREGENE' || siteSpecies != 'mouse'}">
						<h:outputLink styleClass="datatext" rendered="#{ISHSingleSubmissionBean.renderPrbNameURL}" value="#{ISHSingleSubmissionBean.submission.probe.probeNameURL}" target="_blank">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>
						 <%--
						<h:outputText styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL}" value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />					
        				<h:outputText styleClass="datatext" rendered="#{ISHSingleSubmissionBean.renderPrbNameURL}" value=" (#{ISHSingleSubmissionBean.submission.probe.maprobeID})" />
         				--%>
        				<%-- Bernie 27/06/2010 Mantis 558 Task1  --%>
        				<%--Bernie 15/02/2012 Mantis 558 Task A2 --%>
						<h:outputLink styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL && ISHSingleSubmissionBean.submission.probe.maprobeID != ISHSingleSubmissionBean.submission.probe.probeName}" value="#{ISHSingleSubmissionBean.submission.probe.probeNameURL}">
							<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
						</h:outputLink>	
						<h:outputLink styleClass="datatext" rendered="#{!ISHSingleSubmissionBean.renderPrbNameURL && ISHSingleSubmissionBean.submission.probe.maprobeID == ISHSingleSubmissionBean.submission.probe.probeName}" value="/gudmap/pages/probe.html" target="_blank">
							<f:param name="probe" value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ISHSingleSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" #{ISHSingleSubmissionBean.submission.probe.maprobeID}" />
						</h:outputLink>	
						<%--Bernie 15/02/2012 Mantis 558 Task A1,A3 --%>				
						<h:outputLink styleClass="datatext" rendered="#{ISHSingleSubmissionBean.submission.probe.maprobeID !='' && ISHSingleSubmissionBean.submission.probe.maprobeID != ISHSingleSubmissionBean.submission.probe.probeName}" value="/gudmap/pages/probe.html" target="_blank">
							<f:param name="probe" value="#{ISHSingleSubmissionBean.submission.probe.probeName}" />
							<f:param name="maprobe" value="#{ISHSingleSubmissionBean.submission.probe.maprobeID}" />
							<h:outputText value=" (#{ISHSingleSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>
						
						<%-- Mantis 558 - TaskA1 
						<h:outputLink styleClass="datatext" rendered="#{ISHSingleSubmissionBean.submission.probe.maprobeID !=''}" value="/gudmap/pages/probe.html?probe=#{ISHSingleSubmissionBean.submission.probe.probeName}" target="_blank">
							<h:outputText value=" (#{ISHSingleSubmissionBean.submission.probe.maprobeID})" />
						</h:outputLink>
						--%>
					</h:panelGroup>
					
					<h:outputText value="Name of cDNA:" rendered="#{siteSpecies == 'mouse'}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.cloneName}" rendered="#{siteSpecies == 'mouse'}" />
					
					<h:outputText value="Additional Name of cDNA:" rendered="#{siteSpecies == 'mouse' && ISHSingleSubmissionBean.submission.probe.additionalCloneName != null && ISHSingleSubmissionBean.submission.probe.additionalCloneName != ''}"/>
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.additionalCloneName}" rendered="#{siteSpecies == 'mouse' && ISHSingleSubmissionBean.submission.probe.additionalCloneName != null && ISHSingleSubmissionBean.submission.probe.additionalCloneName != ''}" />
					
					<h:outputText value="Sequence ID:" />
					<h:panelGrid  rowClasses="text-top" columns="1" border="0" >
						<h:panelGroup>
							<%-- 
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seqStatus} " />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seqInfo} " rendered="#{ISHSingleSubmissionBean.renderPrbSeqInfo}" />
							--%>
							<h:outputLink target="_blank" styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.genbankURL}" rendered="#{ISHSingleSubmissionBean.submission.probe.genbankID != ''}" >
								<h:outputText  value="#{ISHSingleSubmissionBean.submission.probe.genbankID}" />
							</h:outputLink>
							<h:outputText styleClass="datatext" value="unknown" rendered="#{ISHSingleSubmissionBean.submission.probe.genbankID == ''}" />
						</h:panelGroup>
					</h:panelGrid>
					
					<%-- added by xingjun -- 02/05/2007 --- start --%>
					<h:outputText styleClass="plaintext" value="5' primer sequence:" rendered="#{ISHSingleSubmissionBean.submission.probe.seq5Primer != null && ISHSingleSubmissionBean.submission.probe.seq5Primer != '' && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ISHSingleSubmissionBean.submission.probe.seq5Primer}" rendered="#{ISHSingleSubmissionBean.submission.probe.seq5Primer != null && ISHSingleSubmissionBean.submission.probe.seq5Primer != '' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="3' primer sequence:" rendered="#{ISHSingleSubmissionBean.submission.probe.seq3Primer != null && ISHSingleSubmissionBean.submission.probe.seq3Primer != '' && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					<h:outputText styleClass="plaintextseq" value="#{ISHSingleSubmissionBean.submission.probe.seq3Primer}" rendered="#{ISHSingleSubmissionBean.submission.probe.seq3Primer != null && ISHSingleSubmissionBean.submission.probe.seq3Primer != '' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<h:outputText styleClass="plaintext" value="5' primer location:" rendered="#{ISHSingleSubmissionBean.submission.probe.seq5Loc != null && ISHSingleSubmissionBean.submission.probe.seq5Loc != '' && ISHSingleSubmissionBean.submission.probe.seq5Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seq5Loc}" rendered="#{ISHSingleSubmissionBean.submission.probe.seq5Loc != null && ISHSingleSubmissionBean.submission.probe.seq5Loc != '' && ISHSingleSubmissionBean.submission.probe.seq5Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}"/>
					
					<h:outputText styleClass="plaintext" value="3' primer location:"  rendered="#{ISHSingleSubmissionBean.submission.probe.seq3Loc != null && ISHSingleSubmissionBean.submission.probe.seq3Loc != '' && ISHSingleSubmissionBean.submission.probe.seq3Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.seq3Loc}" rendered="#{ISHSingleSubmissionBean.submission.probe.seq3Loc != null && ISHSingleSubmissionBean.submission.probe.seq3Loc != '' && ISHSingleSubmissionBean.submission.probe.seq3Loc != 'n/a' && ISHSingleSubmissionBean.renderPrbSeqInfo}" />
					
					<%-- added by ying full sequence -- 04/05/2007 --- start --%>
					<h:outputText value="Template Sequence:" rendered="#{ISHSingleSubmissionBean.submission.probe.fullSequence != null}"/>
					<h:outputLink styleClass="datatext" value="#" onclick="var w=window.open('sequence.jsf?id=#{ISHSingleSubmissionBean.submission.accID}','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;"  rendered="#{ISHSingleSubmissionBean.submission.probe.fullSequence != null}">
						<h:outputText value="(Click to see sequence.)" />
					</h:outputLink>
					<%-- added by ying full sequence -- 04/05/2007 --- end --%>
					
					<%-- added by xingjun -- 02/05/2007 --- end --%>
					
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
					
					<h:outputText value="Lab Probe ID:" rendered="#{ISHSingleSubmissionBean.submission.probe.labProbeId != null && ISHSingleSubmissionBean.submission.probe.labProbeId != ''}" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.probe.labProbeId}" rendered="#{ISHSingleSubmissionBean.submission.probe.labProbeId != null && ISHSingleSubmissionBean.submission.probe.labProbeId != ''}" />
					
					<h:outputText styleClass="plaintext,text-top" value="Probe Notes:" rendered="#{ISHSingleSubmissionBean.submission.probe.notes != null && ISHSingleSubmissionBean.submission.probe.notes != ''}" />
					<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.probe.notes}" rendered="#{ISHSingleSubmissionBean.submission.probe.notes != null && ISHSingleSubmissionBean.submission.probe.notes != ''}" />

					<%-- moved here from under the Template Sequence field --%>
					<h:outputText value="Curator Notes:" rendered="#{ISHSingleSubmissionBean.submission.probe.maprobeNotes != null}" />
					<h:dataTable value="#{ISHSingleSubmissionBean.submission.probe.maprobeNotes}" var="maprobeNote" rendered="#{ISHSingleSubmissionBean.submission.probe.maprobeNotes != null}">
						<h:column>
							<h:outputText styleClass="datatext" value="#{maprobeNote}" />
						</h:column>
					</h:dataTable>
				</h:panelGrid>
				<h:outputLink id="editProbe" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_probe.html?accessionId=#{ISHSingleSubmissionBean.submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
			
			<h:outputText value="&nbsp" escape="false"/>
			<h:outputText value="&nbsp" escape="false"/>
						
       	</h:panelGrid>
       	
        <%-- added by xingjun - 27/08/2008 - end --%>
		<h:panelGrid width="100%" columns="2" rendered="#{ISHSingleSubmissionBean.submission.assayType == 'TG'}" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" >
			<h:outputText value="#{ISHSingleSubmissionBean.transgenicTitle}"/>
			<t:dataTable id="transgenicContentTable" width="100%" rowClasses="header-stripey" columnClasses="leftCol,rightCol" headerClass="align-top-stripey"
        	     value="#{ISHSingleSubmissionBean.submission.transgenics}" var="transgenics" rowIndexVar="row">
				<t:column>
					<f:verbatim rendered="#{row>0}"> 
						<hr width="100%" align="center"/>
					</f:verbatim>
					<h:outputText value="Allele #{transgenics.serialNo}" styleClass="plaintextbold" rendered="#{ISHSingleSubmissionBean.submission.multipleTransgenics}"/>
					<h:panelGrid width="100%" columns="2" columnClasses="width95, width5">
						<h:panelGrid border="0" columns="2" columnClasses="data-titleCol,data-textCol">
							<h:outputText value="Gene Reported:" />
							<h:outputLink styleClass="datatext" value="gene.html">
								<h:outputText value="#{transgenics.geneSymbol}" />
								<f:param name="gene" value="#{transgenics.geneSymbol}" />
							</h:outputLink>
	
							<h:outputText value="Mutated Gene Id:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.geneId}" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="Reference for Allele Description:" />
					<h:outputText rendered="#{empty transgenics.mutatedAlleleIdUrl}" value="#{transgenics.mutatedAlleleId}" escape="false"/>
					<h:outputLink rendered="#{not empty transgenics.mutatedAlleleIdUrl}"  styleClass="datatext" value="#{transgenics.mutatedAlleleIdUrl}" >
						<h:outputText value="#{transgenics.mutatedAlleleId}" />
					</h:outputLink>
							<h:outputText value="Allele Name:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="Allele Description:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.mutatedAlleleName}" />
	
							<h:outputText value="Reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="#{transgenics.labelProduct}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
			
							<h:outputText value="Direct method for visualising reporter:" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
							<h:outputText value="#{transgenics.visMethod}" rendered="#{transgenics.mutantType=='transgenic insertion'}" />
			
							<h:outputText value="Allele First Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.alleleFirstChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />
			
							<h:outputText value="Allele Second Chromatid:" rendered="#{transgenics.mutantType=='mutant allele'}" />
							<h:outputText value="#{transgenics.alleleSecondChrom}" rendered="#{transgenics.mutantType=='mutant allele'}" />
			
							<h:outputText styleClass="plaintext,text-top" value="Notes:" rendered="#{transgenics.notes != null}" />
					<h:outputText value="#{transgenics.notes}" rendered="#{transgenics.notes != null}" escape="false" />
						</h:panelGrid>
						<h:outputLink id="editTransgenic" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
									onclick="var w=window.open('edit_probe.html?accessionId=#{ISHSingleSubmissionBean.submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
							<h:outputText value="[Edit]" />
						</h:outputLink>
					</h:panelGrid>
				</t:column>
			</t:dataTable>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
		</h:panelGrid>

		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			<h:outputText value="Specimen" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:panelGrid columns="2" border="0" columnClasses="data-titleCol,data-textCol">
					<h:outputLink value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" styleClass="plaintext" rendered="#{siteSpecies == 'Xenopus laevis'}"> 
						<h:outputText value="Theiler Stage:" />
					</h:outputLink>
<%-- 
					<h:outputLink value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/MAstaging.html" styleClass="plaintext" rendered="#{siteSpecies == 'mouse'}"> 
--%>
					<h:outputLink value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/stagecriteria.html" styleClass="plaintext" rendered="#{siteSpecies == 'mouse'}"> 
						<h:outputText value="Theiler Stage:" />
					</h:outputLink>
					
					<h:outputLink styleClass="datatext" value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html" rendered="#{siteSpecies == 'Xenopus laevis'}">
						<h:outputText value="#{stageSeriesShort}#{ISHSingleSubmissionBean.submission.stage}" />
					</h:outputLink>
<%-- 
					<h:outputLink styleClass="datatext" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts#{ISHSingleSubmissionBean.submission.stage}" rendered="#{siteSpecies == 'mouse'}">
--%>
					<h:outputLink styleClass="datatext" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts#{ISHSingleSubmissionBean.submission.stage}definition.html" rendered="#{siteSpecies == 'mouse'}">
						<h:outputText value="#{stageSeriesShort}#{ISHSingleSubmissionBean.submission.stage}" />
					</h:outputLink>

					<h:outputText value="Other Staging System:" />
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.specimen.stageFormat != 'dpc' && ISHSingleSubmissionBean.submission.specimen.stageFormat != 'P'}">
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.otherStageValue}" />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.stageFormat} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.specimen.stageFormat == 'dpc'}">
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.otherStageValue}" />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.stageFormat} " />
					</h:panelGroup>
					<h:panelGroup rendered="#{ISHSingleSubmissionBean.submission.specimen.stageFormat == 'P'}">
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.stageFormat} " />
						<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.otherStageValue}" />
					</h:panelGroup>
								
					<h:outputText value="Tissue" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.tissue}" />
					
					<h:outputText value="Strain:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.strain}" />
					
					<h:outputText value="Sex:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.sex}" />
					
					<h:outputText value="Genotype:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.genotype}" />
					
					<h:outputText value="Specimen Preparation:" />
					<h:panelGrid columns="2" columnClasses="text-top, text-bottom">
						<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.assayType}" />
						<h:panelGrid columns="3">
							<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
							<h:outputText styleClass="plaintext" value="Fixation Method:" />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.fixMethod}" />
					
							<h:graphicImage alt="" value="../images/spacet.gif" width="35" height="1" />
							<h:outputText styleClass="plaintext" value="Embedding:" />
							<h:outputText styleClass="datatext" value="#{ISHSingleSubmissionBean.submission.specimen.embedding}" />
						</h:panelGrid>
					</h:panelGrid>
					
					<h:outputText value="Experiment Notes:" />
					<h:outputText value="#{ISHSingleSubmissionBean.submission.specimen.notes}" />
				</h:panelGrid>  
				<h:outputLink id="editSpecimen" rendered="#{UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_specimen.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>  
				
			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
					
			<h:outputText value="Linked Publications" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable  value="#{ISHSingleSubmissionBean.submission.linkedPublications}" var="pub">
					<h:column>
						<h:outputText styleClass="plaintext" value="#{pub[0]} " />
						<h:outputText styleClass="plaintext" value="(#{pub[1]}) " />
						<h:outputText styleClass="plaintextbold" value="#{pub[2]} " />
                        <h:outputText styleClass="plaintext" style="font-style:italic" value="#{pub[3]}, " rendered="#{pub[3]!=null && pub[3]!=''}" />
                        <h:outputText styleClass="plaintextbold" value="#{pub[4]}" rendered="#{pub[4]!=null && pub[4]!=''}"/>
                        <h:outputText styleClass="plaintext" value=", " rendered="#{pub[4]!=null && pub[4]!=''}" />
                        <h:outputText styleClass="plaintext" value="#{pub[6]}" />
					</h:column>
				</h:dataTable>
				<h:outputLink id="editPublication" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_linked_publication.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
					
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
					 
							<h:panelGroup rendered="#{link[2] != null && link[2] != ''}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="plaintext" value="http://www.gudmap.org">
									<h:outputText styleClass="datatext" value="#{link[2]}" />
								</h:outputLink>
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editLinkedSubmission" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
							onclick="var w=window.open('edit_linked_submission.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>

			<f:verbatim>&nbsp;</f:verbatim>
			<f:verbatim>&nbsp;</f:verbatim>
    
			<h:outputText value="Acknowledgements" />
			<h:panelGrid width="100%" columns="2" columnClasses="width95, width5" >
				<h:dataTable value="#{ISHSingleSubmissionBean.submission.acknowledgements}" var="ack">
					<h:column>
						<h:panelGrid columns="1">
							<h:panelGroup>
								<h:outputText styleClass="plaintext" value="Project: " />
								<h:outputText styleClass="datatext" value="#{ack[0]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[1] != ''}">
								<h:outputText styleClass="plaintext" value="Name(s): " />
								<h:outputText styleClass="datatext" value="#{ack[1]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[2] != ''}">  
								<h:outputText styleClass="plaintext" value="Address: " />
								<h:outputText styleClass="datatext" value="#{ack[2]}" />
							</h:panelGroup>
							<h:panelGroup rendered="#{ack[3] != ''}">  
								<h:outputText styleClass="plaintext" value="URL: " />
								<h:outputLink styleClass="datatext" value="#{ack[3]}">
									<h:outputText value="#{ack[3]}" />
								</h:outputLink>
							</h:panelGroup>
							<h:panelGroup>  
								<h:outputText styleClass="plaintext" value="Reason: " />
								<h:outputText styleClass="datatext" value="#{ack[4]}" escape="true" />
							</h:panelGroup>
						</h:panelGrid>
					</h:column>
				</h:dataTable>
				<h:outputLink id="editAcknowledgement" rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && UserBean.user.userType!='EXAMINER'}"
					  onclick="var w=window.open('edit_acknowledgement.html?accessionId=#{submission.accID}','editPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
					<h:outputText value="[Edit]" />
				</h:outputLink>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
					
	<jsp:include page="/includes/footer.jsp" />
</f:view>