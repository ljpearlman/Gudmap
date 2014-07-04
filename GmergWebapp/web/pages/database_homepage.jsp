<%-- 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib prefix="rich" uri="http://richfaces.ajax4jsf.org/rich" %>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>


<f:view>
<jsp:include page="/includes/header.jsp">
	<jsp:param name="headerParam" value="databaseHomepage" />
</jsp:include>

<h:form id="mainForm" >
<h:panelGrid columns="2" cellspacing="0px">
	<h:panelGroup style="margin-right:20px"> <%-- left hand side --%>
		<f:verbatim>
		<script type="text/javascript">
						function handleInput (inField, e) {
							//alert(e.keyCode);
							if (e.keyCode === 13) {
								if(inField.id=='mainForm:geneInput')
				                	document.getElementById('mainForm:go1').click();
								else if(inField.id=='mainForm:anatomy')
				                	document.getElementById('mainForm:go2').click();
								else if(inField.id=='mainForm:accession')
				                	document.getElementById('mainForm:go3').click();
								else if(inField.id=='mainForm:geneFn')
				                	document.getElementById('mainForm:go5').click();
				            }
				            return false;//so that mainForm is not called
				        }
		</script>
		<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" BGCOLOR="#FFFFFF" >
			<TR>
				<TD>&nbsp;</TD>
				<TD width="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD width="30" class="top_left_border">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD valign="top"><img src="../images/focus/n_gene.png" width="130" height="24" alt="gene" /></TD>
				<TD style="padding-right:15px"  valign="top">
					<a href="#Link263854Context" name="Link263854Context" id="Link263854Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263854', 'Gene Query', 'Search for genes with expression data in the database. This search addresses all in situ, and micro-array expression data.  &lt;br&gt;Type gene symbol or gene name or synonym (&lt;a href=\&quot;http://www.informatics.jax.org/genes.shtml\&quot;&gt;see MGI&lt;/a&gt;). Find gene symbols using predictive text. Note: Wnt* = starts with Wnt.     &lt;a href=\&quot;http://www.gudmap.org/Help/Query_Help.html#gene\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;&lt;br&gt;Choose the output format from the options dialogue.&lt;br&gt;', 'Link263854Context')"> 
						<img src="../images/focus/n_information.png" alt="information" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD>
					</f:verbatim>	      
			                                <h:inputText id="geneInput" value="#{DatabaseHomepageBean.geneInput}" size="18"  onkeypress="handleInput(this,event)"/>
                                                                                     <a4j:region renderRegionOnly="true">
                                                                                         <rich:suggestionbox for="geneInput" var="geneResult" suggestionAction="#{DatabaseHomepageBean.autocomplete}" >
                                                                                           <h:column>
                                                                                                <h:outputText value="#{geneResult}" />
                                                                                           </h:column>
                                                                                         </rich:suggestionbox>
                                                                                     </a4j:region>
					<f:verbatim>	      
				</TD>
				<TD>
					</f:verbatim>	      
					<h:outputLink onclick="showGeneOptionsPanels(event);return false;">
						<h:graphicImage id="optionlink" url="../images/focus/n_options_2.png" alt="Options" styleClass="icon"/>
					</h:outputLink>
					<h:panelGrid id="genePanel" width="265" border="1" cellspacing="0" cellpadding="0" bgcolor="white"
					             style="position:absolute;z-index:100;visibility:hidden;text-align:right">
						<h:panelGrid id="geneTableOuterFrame" width="260" cellspacing="0" cellpadding="0" bgcolor="white" >
							<h:outputLink onclick="hideGeneOptionsPanels(event); return false;" styleClass="plaintext" style="float:right" >
								<h:outputText value="Close " />
							</h:outputLink>
							<h:panelGrid id="geneTableInnerFrame" cellpadding="2" cellspacing="2" styleClass="plaintext">
								<h:messages errorClass="plainred" />
								<h:outputText styleClass="plaintextbold" value="Gene Search Options" />
								<h:panelGroup>
									<h:outputText value=" " />
								</h:panelGroup>
								<h:panelGroup id="geneSearchOptionFrame" styleClass="text-align:center">
									<h:outputText  value="Search for: " />
									<h:selectOneMenu id="geneSearchResultOption" value="#{DatabaseHomepageBean.geneSearchResultOption}"
													onchange="disEnableGeneStageInGenePanel()" >
										<f:selectItems value="#{DatabaseHomepageBean.geneSearchResultOptionItems}" />
									</h:selectOneMenu>
								</h:panelGroup>
								<h:panelGroup>
									<h:outputText value="Theiler Stage "/>
									<h:selectOneMenu id="geneStage" value="#{DatabaseHomepageBean.geneStage}" 
										disabled="#{DatabaseHomepageBean.geneSearchResultOption=='genes'}" 
										onchange="setGeneStageHidden()" >
										<f:selectItems value="#{DatabaseHomepageBean.stageItems}" />
									</h:selectOneMenu>
									xingjun - 27/05/2010 - use geneStageHidden to set geneStage value - dont know why selectOneMenu not working
									<h:inputHidden id="geneStageHidden" value="#{DatabaseHomepageBean.geneStage}" />
								</h:panelGroup>
								<h:panelGroup rendered="#{1 == 0}">
									<h:selectOneMenu id="geneAnnotation" value="#{DatabaseHomepageBean.geneAnnotation}">
										<f:selectItems value="#{DatabaseHomepageBean.annotationItems}" />
									</h:selectOneMenu>
								</h:panelGroup>
							</h:panelGrid>
						</h:panelGrid>
					</h:panelGrid>
					<f:verbatim>
				</TD>
				<TD>&nbsp;</TD>
				<TD>
					</f:verbatim>
					<h:commandLink id="go1" action="#{DatabaseHomepageBean.search}">
						<h:graphicImage url="../images/focus/n_go_2.png" alt="Go" styleClass="icon" />
						<f:param name="query" value="Gene" />
					</h:commandLink>
					<f:verbatim>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD colspan="7" class="left_border" >&nbsp;</TD>
			</TR>
			<TR>
				
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					<img src="../images/focus/n_anatomy.png" width="130" height="24" alt="anatomy" />
				</TD>
				<TD valign="top">
					<a href="#Link787011Context" name="Link787011Context" id="Link787011Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID787011', 'Query Anatomy', 'Search for genes annotated with expression present, uncertain or not detected in a structure. This search addresses annotated in situ expression data and microarray data. &lt;br&gt;Type exact name of structure or exact synonym or EMAP anatomy ID. Find anatomy terms with predictive text or &lt;a href=\&quot;http://www.gudmap.org/gudmap/pages/boolean_test.html\&quot;&gt;search anatomy tree&lt;/a&gt; then return to enter text.   &lt;a href=\&quot;http://www.gudmap.org/Help/Query_Help.html#anatomy\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;&lt;br&gt;For Boolean searches (genes expressed in X and/or/not Y) &lt;a href=\&quot;http://www.gudmap.org/gudmap/pages/boolean_test.html\&quot;&gt;go here&lt;/a&gt;&lt;br&gt;', 'Link787011Context')">
						<img src="../images/focus/n_information.png" alt="information" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD>
				</f:verbatim>
					<h:inputText id="anatomy" value="#{DatabaseHomepageBean.anatomyInput}" size="18" onkeypress="handleInput(this,event)"/>
                                                                                     <a4j:region renderRegionOnly="true">
                                                                                         <rich:suggestionbox for="anatomy" var="anatomyResult" suggestionAction="#{DatabaseHomepageBean.autocomplete}" >
                                                                                           <h:column>
                                                                                                <h:outputText value="#{anatomyResult}" />
                                                                                           </h:column>
                                                                                         </rich:suggestionbox>
                                                                                     </a4j:region>
					<%-- <h:commandButton action="#{DatabaseHomepageBean.search}" style="display:none;width:2px;" value="submit">
						<f:setPropertyActionListener value="Anatomy" target="#{DatabaseHomepageBean.query}"/>
					</h:commandButton> --%>
					<f:verbatim>
					
				</TD>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD>
					</f:verbatim>
					
					<h:commandLink id="go2" action="#{DatabaseHomepageBean.search}">
						<h:graphicImage url="../images/focus/n_go_2.png" alt="Go" styleClass="icon" />
						<f:param name="query" value="Anatomy" />
					</h:commandLink>
					<f:verbatim>
					
					
				</TD>
			</TR>
			
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD colspan="7" class="left_border" >&nbsp;</TD>
			</TR>
			<TR>
				<TD align="right" valign="top"><img src="../images/focus/n_query.png" width="69" height="24" alt="query" /></TD>
				<TD class="top_border">&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="boolean_test.html">
						<h:graphicImage url="../images/focus/n_booleanAnatomy.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link494047Context" name="Link494047Context" id="Link494047Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID494047', 'Boolean Search', ' Search for genes expressed in structure X and/or/not in structure Y. Click &lt;br&gt; Boolean Anatomy for this search page where there is additional help.&lt;a href=\&quot;http://www.gudmap.org/Help/Query_Help.html#boolean\&quot;&gt;More....&lt;/a', 'Link494047Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD colspan="7" class="left_border" >&nbsp;</TD>
			</TR>
			
			<TR>
				
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top"><img src="../images/focus/n_accessionID.png" width="130" height="24" alt="accession id" /></TD>
				<TD valign="top">
					<a href="#Link579027Context" name="Link579027Context" id="Link579027Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID579027', 'Query Accession ID', 'Search for database entries related to an accession number from the following databases: GUDMAP, ENSEMBL, MGI Gene ID.  List IDs separated by \';\'. Lists are treated as A OR B.  Example,  ENSMUSG00000050846; gudmap:8328. This search addresses all in situ data but will include microarrays for GUDMAP IDs.      &lt;a href=\&quot;http://www.gudmap.org/Help/Query_Help.html#accession\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;', 'Link579027Context')">
						<img src="../images/focus/n_information.png" alt="information" width="22" height="24" border="0" />
					</a>
				</TD>	
				<TD>
					
					</f:verbatim>
					<h:inputText id="accession" value="#{DatabaseHomepageBean.accessionInput}" size="18"  onkeypress="handleInput(this,event)"/>
                                                                                     <a4j:region renderRegionOnly="true">
                                                                                         <rich:suggestionbox for="accession" var="accessionResult" suggestionAction="#{DatabaseHomepageBean.autocomplete}" >
                                                                                           <h:column>
                                                                                                <h:outputText value="#{accessionResult}" />
                                                                                           </h:column>
                                                                                         </rich:suggestionbox>
                                                                                     </a4j:region>
					
					<%-- <h:commandButton action="#{DatabaseHomepageBean.search}" style="display:none;width:2px;" value="submit">
						<f:setPropertyActionListener value="Accession ID" target="#{DatabaseHomepageBean.query}"/>
					</h:commandButton> --%>
					<f:verbatim>
					
				</TD>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:commandLink id="go3" action="#{DatabaseHomepageBean.search}">
						<h:graphicImage url="../images/focus/n_go_2.png" alt="Go" styleClass="icon" />
						<f:param name="query" value="Accession ID" />
					</h:commandLink>
					<f:verbatim>
				</TD>
				
			</TR>
			
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD colspan="7" class="left_border" >&nbsp;</TD>
			</TR>
			<TR>
			    <TD>&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					<IMG src="../images/focus/n_geneFunction.png" width="130" height="24" alt="gene function" />
				</TD>
				<TD valign="top">
					<a href="#Link880920Context" name="Link880920Context" id="Link880920Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID880920', 'Query Gene Function', 'Search for genes and probes &lt;a href=\&quot;http://www.geneontology.org/GO.evidence.tree.shtml\&quot;&gt;annotated&lt;/a&gt; with a Gene Ontology (GO) Molecular Function,  Biological Process, or Subcellular Location term. This search addresses all in situ data in GUDMAP.     &lt;br&gt;Type exact GO term or ID. The search accepts GO terms for Molecular Function, Biological Process, or Subcellular Location. Find GO terms using predictive text then copy to the query box. List terms separated by semi colon (;), Lists are treated as A OR B.     &lt;a  href=\&quot;http://www.gudmap.org/Help/Query_Help.html#function\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;', 'Link880920Context')">
						<img src="../images/focus/n_information.png" alt="information" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD>
					</f:verbatim>
					<h:inputText id="geneFn" value="#{DatabaseHomepageBean.geneFunctionInput}" size="18"  onkeypress="handleInput(this,event)"/>
                                                                                     <a4j:region renderRegionOnly="true">
                                                                                         <rich:suggestionbox for="geneFn" var="geneFnResult" suggestionAction="#{DatabaseHomepageBean.autocomplete}" >
                                                                                           <h:column>
                                                                                                <h:outputText value="#{geneFnResult}" />
                                                                                           </h:column>
                                                                                         </rich:suggestionbox>
                                                                                     </a4j:region>
					<f:verbatim>
				</TD>
				<TD>
					</f:verbatim>
					<h:outputLink onclick="showGeneFunctionOptionsPanel(event);return false;">
						<h:graphicImage id="fnOptionlink" url="../images/focus/n_options_2.png" alt="Options" styleClass="icon"/>
					</h:outputLink>
					<h:panelGrid id="geneFunctionPanel" width="265" border="1" cellspacing="0" cellpadding="0" bgcolor="white"
					             style="position:absolute;z-index:100;visibility:hidden;text-align:right">
						<h:panelGrid id="geneFunctionTableOuterFrame" width="260" cellspacing="0" cellpadding="0" bgcolor="white">
							<h:outputLink onclick="hideGeneFunctionOptionsPanels(event); return false;" styleClass="nav3" style="float:right">
								<h:outputText value="Close " />
							</h:outputLink>
							<h:panelGrid cellpadding="2" cellspacing="2">
								<h:messages errorClass="plainred" />
								<h:outputText styleClass="plaintextbold" value="Gene Function Search Options" />
								<h:panelGroup>
									<h:outputText styleClass="plaintext" value=" " />
								</h:panelGroup>
								<h:panelGroup styleClass="text-align:center">
									<h:outputText styleClass="plaintext" value="Search for: " />
									<h:selectOneMenu id="geneFunctionSearchResultOption" value="#{DatabaseHomepageBean.geneFunctionSearchResultOption}"
													onchange="disableTheilerStageInGeneFunctionPanel()">
										<f:selectItems value="#{DatabaseHomepageBean.geneFunctionSearchResultOptionItems}" />
									</h:selectOneMenu>
								</h:panelGroup>
								<h:panelGroup>
									<h:outputText styleClass="plaintext" value="Theiler Stage"/>
									<h:selectOneMenu id="geneFunctionStage" value="#{DatabaseHomepageBean.geneFunctionStage}">
										<f:selectItems value="#{DatabaseHomepageBean.stageItems}" />
									</h:selectOneMenu>
								</h:panelGroup>
								<h:panelGroup rendered="#{1 == 0}">
									<h:selectOneMenu id="geneFunctionAnnotation" value="#{DatabaseHomepageBean.geneAnnotation}">
										<f:selectItems value="#{DatabaseHomepageBean.annotationItems}" />
									</h:selectOneMenu>
								</h:panelGroup>
							</h:panelGrid>
						</h:panelGrid>
					</h:panelGrid>
					<f:verbatim>
				</TD>
				<TD />
				<TD valign="top">
					</f:verbatim>
					<h:commandLink id="go5" action="#{DatabaseHomepageBean.search}">
						<h:graphicImage url="../images/focus/n_go_2.png" alt="Go" styleClass="icon" />
						<f:param name="query" value="Gene Function" />
					</h:commandLink>
					<f:verbatim>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD colspan="7" class="left_border" >&nbsp;</TD>
			</TR>
			<TR>
			    <TD>&nbsp;</TD>
			    <TD>&nbsp;</TD>
				<TD class="top_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="http://www.gudmap.org/gudmap_dis/index.jsp">
						<h:graphicImage url="../images/focus/n_disease.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link494048Context" name="Link494048Context" id="Link494048Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID494048', 'Browse Disease', ' A link to the GUDMAP Disease Resource. The resource contains a searchable database of associations between genes, genitourinary disease and renal/urinary & reproductive phenotypes.', 'Link494048Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			    <TD>&nbsp;</TD>
			    <TD>&nbsp;</TD>
			    <TD>&nbsp;</TD>
			    <TD>&nbsp;</TD>
			</TR>
		</TABLE>
	
		<p>&nbsp;</p>
		
		<%-------------------  Buttom tree menu ----------------------%>	
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" VALIGN="top" >
			<TR>
				<TD/>
				<TD/>
				<TD>&nbsp;</TD>
	 			<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="series_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_series.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link494046Context" name="Link494046Context" id="Link494046Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID494046', 'Browse Series', ' Series returns a list of all the microarray experiments (series) in GUDMAP.  From there you can go to individual experiments and then to individual samples.     &lt;a href=\&quot;http://www.gudmap.org/Help/Browse_Help.html#gene\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;&lt;br&gt;The Series and Sample buttons access raw expression data which is available for download either from the individual sample pages or from the &lt;a href=\&quot;http://www.gudmap.org/Submission_Archive/index.html\&quot;&gt;Submission Archive.&lt;/a&gt;  To see Analyses of Microarray Gene Lists click &lt;a href=\&quot;http://www.gudmap.org/gudmap/pages/genelist_tree.html\&quot;&gt;here.&lt;/a&gt;&lt;br&gt;', 'Link494046Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD/>
				<TD/>
				<TD>&nbsp;</TD>
	 			<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD align="left" valign="top">
					</f:verbatim>
					<h:outputLink value="focus_mic_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_sample.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link782434Context" name="Link782434Context" id="Link782434Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID782434', 'Browse Sample', ' Sample returns a list of all micro-array samples. From there, click GUDMAP Entry ID to view data for each sample.  &lt;a href=\&quot;http://www.gudmap.org/Help/Browse_Help.html#anatomy\&quot;&gt;More...&lt;/a&gt;&lt;br&gt;&lt;br&gt;The Series and Sample buttons access raw expression data which is available for download either from the individual sample pages or from the &lt;a href=\&quot;http://www.gudmap.org/Submission_Archive/index.html\&quot;&gt;Submission Archive.&lt;/a&gt;  To see Analyses of Microarray Gene Lists click &lt;a href=\&quot;http://www.gudmap.org/gudmap/pages/genelist_tree.html\&quot;&gt;here.&lt;/a&gt;&lt;br&gt;', 'Link782434Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD>&nbsp;</TD>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			
			<TR>
				<TD />
				<TD width="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD class="top_left_border">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD class="top_border"  valign="top"><IMG SRC="../images/focus/n_array_2.png" ALT="Array" WIDTH="124" HEIGHT="24" BORDER="0" NAME="array_pix" /></TD>
				<TD class="top_border"></TD>
				<TD  class="top_border" width="50">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD width="30" class="top_border">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				
			    <TD align="left" valign="top">
					</f:verbatim>
					<h:outputLink value="focus_platform_browse.html#{DatabaseHomepageBean.organParam}" >
						<h:graphicImage width="130" height="24" url="../images/focus/n_platform.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link141211Context" name="Link141211Context" id="Link141211Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID141211', 'Browse Platform', 'Platform lists all microarray platforms (chips) used in GUDMAP&lt;br&gt;', 'Link141211Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR> 
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			
			<!-- REPLACE THE NEXT 2 ROWS WITH THE ONES COMMENTED OUT ABOVE FOR NEXT GEN -->
			<%-- <TR>
				<TD />
				<TD width="30">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD> 
				<TD />
				<TD />
				<TD width="50">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
				<!-- <TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>  -->
				<TD width="30" class="top_left_border">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
			    <TD valign="top">
					</f:verbatim>
					<h:outputLink value="series_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_series.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link494046Context" name="Link494046Context" id="Link494046Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID494046', 'Browse Series', ' Series returns a list of all the microarray experiments (series) in GUDMAP.  From there you can go to individual experiments and then to individual samples.     &lt;a href=\&quot;http://www.gudmap.org/Help/Browse_Help.html#gene\&quot;&gt;More....&lt;/a&gt;&lt;br&gt;&lt;br&gt;The Series and Sample buttons access raw expression data which is available for download either from the individual sample pages or from the &lt;a href=\&quot;http://www.gudmap.org/Submission_Archive/index.html\&quot;&gt;Submission Archive.&lt;/a&gt;  To see Analyses of Microarray Gene Lists click &lt;a href=\&quot;http://www.gudmap.org/gudmap/pages/genelist_tree.html\&quot;&gt;here.&lt;/a&gt;&lt;br&gt;', 'Link494046Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR> 
			<TR> 
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
			</TR> --%>
			
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD />
				<TD class="top_left_border">&nbsp;</TD>
				<TD class="top_border"  valign="top"><IMG SRC="../images/focus/n_seq.png" ALT="array" WIDTH="124" HEIGHT="24" BORDER="0" NAME="geo_accession_pix" /></TD>
				<TD class="top_border">&nbsp;</TD>
				<TD class="top_border">&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD align="left" valign="top">
					</f:verbatim>
					<h:outputLink value="ngd_series_browse.html#{DatabaseHomepageBean.organParam}" >
						<h:graphicImage width="130" height="24" url="../images/focus/n_series.png" alt="Next Gen Series" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link141495Context" name="Link141495Context" id="Link141495Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID141495', 'Browse Next Gen Series', 'Next Gen Series returns a list of all the Next Gen series in GUDMAP. From there you can link to individual series and then to individual samples.     &lt;a href=\&quot;http://www.gudmap.org/Help/Browse_Help.html#nextgen\&quot;&gt;More....&lt;/a&gt;', 'Link141495Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				
				
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD/>
				<TD/>
				<TD class="top_left_border">&nbsp;</TD>
	 			<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_stage_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_theilerStage.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link209534Context" name="Link209534Context" id="Link209534Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID209534', 'Browse Theiler Stage', 'Theiler Stage returns a list of GUDMAP database entries organized by developmental (Theiler) stage. Theiler Stages are described &lt;a href=\&quot;http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/stagedefinition.html\&quot;&gt;here&lt;/a&gt;; see also &lt;a href=\&quot;http://www.gudmap.org/About/Tutorial/index.html\&quot;&gt;Tutorial on genitourinary development.&lt;/a&gt;&lt;br&gt;', 'Link209534Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD/>
				<TD class="top_border">&nbsp;</TD>
				<TD align="left" valign="top">
					</f:verbatim>
					<h:outputLink value="focus_ngd_browse.html#{DatabaseHomepageBean.organParam}" >
						<h:graphicImage width="130" height="24" url="../images/focus/n_sample.png" alt="Next Gen Sample" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link141535Context" name="Link141535Context" id="Link141535Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID141535', 'Browse Next Gen Samples', 'Next Gen Sample returns a list of all Next Gen samples. From there, click GUDMAP Entry ID to view data for each sample.     &lt;a href=\&quot;http://www.gudmap.org/Help/Browse_Help.html#nextgen\&quot;&gt;More....&lt;/a&gt;', 'Link141535Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD/>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD align="right" valign="top"><img src="../images/focus/n_browse.png" width="69" height="24" /></TD>
				<TD class="top_border">&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_gene_index_browse.html#{DatabaseHomepageBean.organParam}" >
					<h:graphicImage url="../images/focus/n_gene.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link227081Context" name="Link227081Context" id="Link227081Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID227081', 'Browse Gene', 'Browse&gt;Gene returns a list of all genes for which there is in situ gene expression data in the database. &lt;br&gt;', 'Link227081Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD>&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_ish_browse.html?specimenType=WISH#{DatabaseHomepageBean.organParamForIshTypes}">
						<h:graphicImage url="../images/focus/n_wish.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link571020Context" name="Link571020Context" id="Link571020Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID571020', 'Browse WISH', 'WISH returns a list of Wholemount In situ Hybridisation data &lt;br&gt;', 'Link571020Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD/>
				<TD/>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top"/>
					</f:verbatim>
					<h:outputLink rendered='#{!(DatabaseHomepageBean.focusedOrgan==null || DatabaseHomepageBean.focusedOrgan=="")}'
									value="focus_insitu_browse.html#{DatabaseHomepageBean.organParam}" >
						<h:graphicImage url="../images/focus/n_inSitu.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<h:outputLink rendered='#{DatabaseHomepageBean.focusedOrgan==null || DatabaseHomepageBean.focusedOrgan==""}'
									value="ish_browse.html">
						<h:graphicImage url="../images/focus/n_inSitu.png" alt="Go" styleClass="icon" />
						<f:param name="browseId" value="browseAll" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD class="top_border">
					<a href="#Link368081Context" name="Link368081Context" id="Link368081Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID368081', 'Browse In situ', 'In situ returns a list of total in situ data. This includes wholemount ISH (WISH), section ISH (SISH), OPT and immunohistochemistry (IHC) data &lt;br&gt;', 'Link368081Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD class="top_border">&nbsp;</TD>
				<TD class="top_left_border">&nbsp;</TD>
				<TD align="left" valign="top">
					</f:verbatim>
					<h:outputLink value="focus_ish_browse.html?specimenType=SISH#{DatabaseHomepageBean.organParamForIshTypes}">
						<h:graphicImage url="../images/focus/n_sish.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link767387Context" name="Link767387Context" id="Link767387Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID767387', 'Browse SISH', 'SISH returns a list of Section In situ Hybridisation data &lt;br&gt;', 'Link767387Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD/>
				<TD/>
				<TD height="24" class="top_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_tg_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_transgenic.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link491918Context" name="Link491918Context" id="Link491918Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID491918', 'Browse Transgenic', 'Transgenic returns a list of transgene reporter expression data &lt;br&gt;', 'Link491918Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
				<TD/>
				<TD class="top_left_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_ish_browse.html?specimenType=OPT#{DatabaseHomepageBean.organParamForIshTypes}">
						<h:graphicImage url="../images/focus/n_opt.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link848332Context" name="Link848332Context" id="Link848332Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID848332', 'Browse OPT', 'OPT returns a list of Optical Projection Tomography data &lt;br&gt;', 'Link848332Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR>
			<TR>
				<TD STYLE="height:10px">&nbsp;</TD>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="left_border">&nbsp;</TD>
				<TD/>
				<TD/>
			</TR>
			<TR>
				<TD/>
				<TD/>
				<TD>&nbsp;</TD>
				<TD/>
				<TD/>
				<TD/>
				<TD class="top_border">&nbsp;</TD>
				<TD valign="top">
					</f:verbatim>
					<h:outputLink value="focus_ihc_browse.html#{DatabaseHomepageBean.organParam}">
						<h:graphicImage url="../images/focus/n_ihc.png" alt="Go" styleClass="icon" />
					</h:outputLink>
					<f:verbatim>
				</TD>
				<TD valign="top">
					<a href="#Link848332Context" name="Link848332Context" id="Link848332Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID848332', 'Browse IHC', 'IHC returns a list of immunohistochemistry data &lt;br&gt;', 'Link848332Context')">
						<img src="../images/focus/n_information.png" width="22" height="24" border="0" />
					</a>
				</TD>
			</TR>
		</TABLE></f:verbatim>
	</h:panelGroup> <%------------ left hand side ------------%>
	
	<%-------------------------------- right-hand side ------------------------------------%>	
	<%-- <h:panelGrid cellspacing="0" cellpadding="5px" border="1" style="margin-left:2em;margin-top:-64px;">  --%>
	<h:panelGrid cellspacing="0" cellpadding="5px" border="1" style="margin-left:2em;margin-top:-2px;"> 
		<h:panelGrid columns="3" cellspacing="0" cellpadding="2px" width="100%" styleClass="plaintextlight" >
			<h:outputText value="Assay" styleClass="plaintextbold" />
			<h:outputText value="Genes" styleClass="plaintextbold" />
			<h:outputText value="Entries" styleClass="plaintextbold" />
			
			<h:outputText value="ISH" />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totIshGenes}"/>
			<h:outputLink value="focus_ish_browse.html#{DatabaseHomepageBean.organParam}" rendered="#{DatabaseHomepageBean.dbSummary.totAvailIshSubs!='0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totAvailIshSubs}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totAvailIshSubs}" rendered="#{DatabaseHomepageBean.dbSummary.totAvailIshSubs=='0'}"/>
			
			<h:outputText value="WISH" />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totWishGenes}"/>
			<h:outputLink value="focus_ish_browse.html?specimenType=WISH#{DatabaseHomepageBean.organParamForIshTypes}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsWISH !='0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsWISH}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsWISH}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsWISH=='0'}"/>
			
			<h:outputText value="SISH" />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totSishGenes}"/>
			<h:outputLink value="focus_ish_browse.html?specimenType=SISH#{DatabaseHomepageBean.organParamForIshTypes}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsSISH !='0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsSISH}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsSISH}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsSISH=='0'}"/>
			
			<h:outputText value="OPT" />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totOptGenes}"/>
			<h:outputLink value="focus_ish_browse.html?specimenType=OPT#{DatabaseHomepageBean.organParamForIshTypes}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsOPT !='0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsOPT}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsOPT}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsOPT=='0'}"/>
			
			<h:outputText value="IHC" />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totIhcGenes}"/>
			<h:outputLink value="focus_ihc_browse.html#{DatabaseHomepageBean.organParam}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsIHC != '0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsIHC}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsIHC}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsIHC == '0'}"/>
			
			<h:outputText value="Tg"   style="cursor:help" title="Please note: 2 transgenic reporter genes are representated by full GUDMAP entries (8 total) and 25 are defined as a series of pdf characterisations." />
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totTgGenes + 25}"  style="cursor:help" title="Please note: 2 transgenic reporter genes are representated by full GUDMAP entries (8 total) and 25 are defined as a series of pdf characterisations." />
			<h:outputLink value="focus_tg_browse.html#{DatabaseHomepageBean.organParam}"   style="cursor:help" title="Please note: 2 transgenic reporter genes are representated by full GUDMAP entries (8 total) and 25 are defined as a series of pdf characterisations." rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsTG != '0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsTG}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsTG}" rendered="#{DatabaseHomepageBean.dbSummary.totalAvailableSubmissionsTG == '0'}"/>
			
			<h:outputText value="Microarray" />
			<h:outputText value=""/>
			<h:outputLink value="focus_mic_browse.html#{DatabaseHomepageBean.organParam}" rendered="#{DatabaseHomepageBean.dbSummary.totAvailArraySubs != '0'}">
				<h:outputText value="#{DatabaseHomepageBean.dbSummary.totAvailArraySubs}" />
			</h:outputLink>
			<h:outputText value="#{DatabaseHomepageBean.dbSummary.totAvailArraySubs}" rendered="#{DatabaseHomepageBean.dbSummary.totAvailArraySubs == '0'}"/>
			
			
		</h:panelGrid>

		<h:panelGrid cellspacing="0" cellpadding="2px" width="100%" >
			<h:panelGrid rendered="#{EntryPageBean.databaseServer == 'clynelish'}"	style="border:1px solid red; width:100%;" >
			</h:panelGrid>
			<h:outputText value="<span class='plaintextboldlight'>Last Editorial Update:</span> #{EntryPageBean.lastEditorialUpdate}" styleClass="plaintextlight" escape="false" />
			<h:outputText value="<span class='plaintextboldlight'>Last Software Update:</span> #{EntryPageBean.lastSoftwareUpdate} (V #{EntryPageBean.applicationVersion})" styleClass="plaintextlight" escape="false" />
		</h:panelGrid>
  
		<h:panelGrid cellspacing="0" cellpadding="2px" >
			<h:outputText value="Focus by Organ / System" styleClass="plaintextboldlight" />
			<h:panelGrid width="100%" columns="2" styleClass="plaintextlight">
				<h:outputLink value="database_homepage.html?focusedOrgan=1" rendered="#{DatabaseHomepageBean.focusedOrgan!='1'}">
					<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Metanephros_d.png" />
				</h:outputLink>
				<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Metanephros_d.png" rendered="#{DatabaseHomepageBean.focusedOrgan=='1'}"  style="border:1px solid blue"/>
				<h:outputLink value="database_homepage.html?focusedOrgan=1" rendered="#{DatabaseHomepageBean.focusedOrgan!='1'}" >
					<h:outputText value="Metanephros" />
				</h:outputLink>
				<h:outputText value="Metanephros" style="color:blue" rendered="#{DatabaseHomepageBean.focusedOrgan=='1'}"/>
				
				<h:outputLink value="database_homepage.html?focusedOrgan=5" rendered="#{DatabaseHomepageBean.focusedOrgan!='5'}">
					<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/LUT_d.png" />
				</h:outputLink>
				<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/LUT_d.png" rendered="#{DatabaseHomepageBean.focusedOrgan=='5'}"  style="border:1px solid blue"/>
				<h:outputLink value="database_homepage.html?focusedOrgan=5" rendered="#{DatabaseHomepageBean.focusedOrgan!='5'}">
					<h:outputText value="Lower urinary tract" />
				</h:outputLink>
				<h:outputText value="Lower urinary tract" style="color:blue" rendered="#{DatabaseHomepageBean.focusedOrgan=='5'}"/>
				
				<h:outputLink value="database_homepage.html?focusedOrgan=2" rendered="#{DatabaseHomepageBean.focusedOrgan!='2'}">
					<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Early_UGS_d.png" />
				</h:outputLink>
				<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Early_UGS_d.png" rendered="#{DatabaseHomepageBean.focusedOrgan=='2'}"  style="border:1px solid blue"/>
				<h:outputLink value="database_homepage.html?focusedOrgan=2" rendered="#{DatabaseHomepageBean.focusedOrgan!='2'}">
					<h:outputText value="Early reproductive system" />
				</h:outputLink>
				<h:outputText value="Early reproductive system" style="color:blue" rendered="#{DatabaseHomepageBean.focusedOrgan=='2'}"/>
				
				<h:outputLink value="database_homepage.html?focusedOrgan=3" rendered="#{DatabaseHomepageBean.focusedOrgan!='3'}">
					<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Male_d.png" />
				</h:outputLink>
				<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Male_d.png" rendered="#{DatabaseHomepageBean.focusedOrgan=='3'}"  style="border:1px solid blue"/>
				<h:outputLink value="database_homepage.html?focusedOrgan=3" rendered="#{DatabaseHomepageBean.focusedOrgan!='3'}">
					<h:outputText value="Male reproductive system" />
				</h:outputLink>
				<h:outputText value="Male reproductive system" style="color:blue" rendered="#{DatabaseHomepageBean.focusedOrgan=='3'}"/>
				
				<h:outputLink value="database_homepage.html?focusedOrgan=4" rendered="#{DatabaseHomepageBean.focusedOrgan!='4'}">
					<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Female_d.png" />
				</h:outputLink>
				<h:graphicImage styleClass="icon" height="70" width="70" value="../images/focus/Female_d.png" rendered="#{DatabaseHomepageBean.focusedOrgan=='4'}"  style="border:1px solid blue"/>
				<h:outputLink value="database_homepage.html?focusedOrgan=4" rendered="#{DatabaseHomepageBean.focusedOrgan!='4'}">
					<h:outputText value="Female reproductive system" />
				</h:outputLink>
				<h:outputText value="Female reproductive system" style="color:blue" rendered="#{DatabaseHomepageBean.focusedOrgan=='4'}"/>
			</h:panelGrid>
		</h:panelGrid>
	</h:panelGrid> <%-- right-hand side --%>
</h:panelGrid>
</h:form>

<h:form id="uploadForm" enctype="multipart/form-data"  onsubmit="getById('uploadForm:uploadGeneStage').value = getById('mainForm:geneStage').value;">
	<h:inputHidden id="uploadResultOption" value="#{DatabaseHomepageBean.uploadResultOption}"/>
	<h:inputHidden id="uploadGeneStage" value="#{DatabaseHomepageBean.geneStage}"/>
	<h:panelGrid id="uploadPanel" width="265" border="1" cellspacing="0" cellpadding="0" bgcolor="white"
	             style="position:absolute;z-index:100;visibility:hidden;text-align:right">
		<h:panelGrid id="geneTableOuterFrame2" width="260" cellspacing="0" cellpadding="0" bgcolor="white" styleClass="plaintext">
			<h:outputText value="&nbsp;" escape="false" />
			<h:panelGrid cellpadding="2" cellspacing="2" columns="3" styleClass="plaintext">
				<h:outputText value="Upload a" />
				<h:outputLink styleClass="datatext" value="batch_query.html">
				<h:outputText value="batch query" />
				</h:outputLink>
				<h:outputText value="file: " />
			</h:panelGrid>
			<h:outputText value="(Max 100 gene symbols)" />
			<h:panelGrid cellpadding="2" cellspacing="2" styleClass="plaintext">
				<t:inputFileUpload id="myFileId" value="#{DatabaseHomepageBean.myFile}" storage="file"
									accept="txt/txt,txt/csv" required="false" />
				<h:commandButton value="Upload"	action="#{DatabaseHomepageBean.processMyFile}" />
			</h:panelGrid>
		</h:panelGrid>
	</h:panelGrid>
</h:form>

<jsp:include page="/includes/footer.jsp" />

</f:view>


