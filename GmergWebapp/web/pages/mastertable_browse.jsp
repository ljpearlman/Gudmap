<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%-- moved style definition to gudmap_css.css - xingjun - 30/07/2010 --%>
<%-- 
<head>
	<style>
		.panelFirstColumn{
			vertical-align:top; 
			margin-top:0; 
			margin-left:auto; 
			margin-right:auto;
			width:11em;
		}
	</style>
</head>
--%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
                <a4j:keepAlive beanName="MasterTableBrowseBean" />
<%--
	<h:form rendered="genelist"> 	
		<h:commandLink value="View gene list in JavaTreeView" id="treeViewActionLink" action="#{MasterTableBrowseBean.startTreeView}" >
			<f:param name="sourceId" value="#{MasterTableBrowseBean.genelistId}" />
		</h:commandLink>
		
		<%-- ++++++++++++++++++++++++++++++ TreeView Applet +++++++++++++++++++++++++++++++++++++++++ -- %>
		<h:panelGroup rendered="#{MasterTableBrowseBean.displayTreeView}">
			<f:verbatim>
				<applet code="edu/stanford/genetics/treeview/applet/GudmapApplet.class" MAYSCRIPT archive="
			</f:verbatim>
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/TreeView.jar," escape="false" /> 
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/nanoxml-2.2.2.jar," escape="false" />
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/analysis.jar\"" escape="false" />
					<h:outputText value="width='1' height='1'alt='Your browser is not running the visualisation applet! ' >" escape="false" />
					<h:outputText value="<param name='genelistId' value='#{MasterTableBrowseBean.genelistId}'/>" escape="false" />
			<f:verbatim>
					<param name="sessionId" value="${pageContext.session.id}"/>
					<param name="dataSource" value="genelist"/>
				</applet>
			</f:verbatim>
		</h:panelGroup>
	</h:form>

		<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
			<h:outputText value= "  (View Microarray Analysis Help) " />
		</h:outputLink>			
--%>
	<h:outputText styleClass="bigplaintext" value="#{MasterTableBrowseBean.title} > Microarray Expression" rendered="true" escape="false" />

	<h:form id="topPanelForm">
		<h:panelGrid columns="4" width="100%" styleClass="header-stripey" cellpadding="5" columnClasses="leftAlign, leftAlign">
			
			<h:panelGrid columns="3" columnClasses="leftAlign, leftAlign" >	
				<h:outputText value="Display format: " styleClass="plaintextbold" />
				<h:selectOneRadio value="#{MasterTableBrowseBean.viewMode}" style="display: inline-table; verticle-align: top">
					<f:selectItem itemValue="0" itemLabel="tabbed panels" />
					<f:selectItem itemValue="1" itemLabel="sequential" />
				</h:selectOneRadio>
 <%--			
			<h:dataTable value="#{MasterTableBrowseBean.allMasterTables}" var="masterTable" width="100%"
						 cellspacing="2" cellpadding="2" border="0" bgcolor="white" 
						 rowClasses="table-stripey, table-nostripe" headerClass="align-top-stripey">
				<h:column>
					<f:facet name="header">
						<h:selectBooleanCheckbox id="toggleSelectAllExprestionProfiles" value="false"
												 title="click to select/deselect all microarray expression profiles" 
												 onclick="toggleSelectAll(event, this, 'selectExprestionProfile');" >
						</h:selectBooleanCheckbox>
					</f:facet>
					<h:selectBooleanCheckbox id="selectExprestionProfile" value="#{masterTable.selected}"/>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Microarray expression profiles to display" styleClass="plaintextbold" />
					</f:facet>
					<h:outputText styleClass="plaintext" value="#{masterTable.info.title}" />
				</h:column>	
			
			</h:dataTable>
--%> 

			<h:panelGrid columns="3" columnClasses="leftAlign, leftAlign, leftAlign" >		
	            <rich:dropDownMenu id="menu" style="border:1px solid #{a4jSkin.panelBorderColor}" direction="bottom-right" jointPoint="bl">
		            <f:facet name="label"> 
	                    <h:panelGroup>
	                        <h:outputText value="Select microarray expression profiles "/>
	                        <h:graphicImage value="/images/down.gif" styleClass="pic"/>
	                    </h:panelGroup>
	                </f:facet>
	                        
	                <rich:menuItem value="Reproductive Gonadal (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerGonadalMOE430}" reRender="topPanelForm:menu" rendered="#{!MasterTableBrowseBean.selectedGonadalMOE430}"/>
	                <rich:menuItem value="Reproductive Gonadal (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerGonadalMOE430}" reRender="topPanelForm:menu" rendered="#{MasterTableBrowseBean.selectedGonadalMOE430}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	                
	                <rich:menuItem value="Lower Urinary Tract (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerLUTMOE430}" reRender="topPanelForm:menu" rendered="#{!MasterTableBrowseBean.selectedLUTMOE430}"/>
	                <rich:menuItem value="Lower Urinary Tract (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerLUTMOE430}" reRender="topPanelForm:menu" rendered="#{MasterTableBrowseBean.selectedLUTMOE430}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	                
	                <rich:menuItem value="Developing Kidney (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerKidneyMOE430}" reRender="topPanelForm:menu" rendered="#{!MasterTableBrowseBean.selectedKidneyMOE430}"/>
	                <rich:menuItem value="Developing Kidney (MOE430)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerKidneyMOE430}" reRender="topPanelForm:menu" rendered="#{MasterTableBrowseBean.selectedKidneyMOE430}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	
	                <rich:menuItem value="Developing Pelvic Ganglia FACS (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerPelvicGangliaST1}" reRender="menu" rendered="#{!MasterTableBrowseBean.selectedPelvicGangliaST1}"/>
	                <rich:menuItem value="Developing Pelvic Ganglia FACS (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerPelvicGangliaST1}" reRender="menu" rendered="#{MasterTableBrowseBean.selectedPelvicGangliaST1}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	                
	                <rich:menuItem value="JGA Single Cell (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerJGAST1}" reRender="menu" rendered="#{!MasterTableBrowseBean.selectedJGAST1}"/>
	                <rich:menuItem value="JGA Single Cell (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerJGAST1}" reRender="menu" rendered="#{MasterTableBrowseBean.selectedJGAST1}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	                
	                <rich:menuItem value="Developing Kidney (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerKidneyST1}" reRender="menu" rendered="#{!MasterTableBrowseBean.selectedKidneyST1}"/>
	                <rich:menuItem value="Developing Kidney (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerKidneyST1}" reRender="menu" rendered="#{MasterTableBrowseBean.selectedKidneyST1}" >
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	                
	                <rich:menuItem value="Developing Gonadal FACS (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerGonadalST1}" reRender="menu" rendered="#{!MasterTableBrowseBean.selectedGonadalST1}"/>
	                <rich:menuItem value="Developing Gonadal FACS (ST1)" submitMode="ajax" immediate="true" actionListener="#{MasterTableBrowseBean.listenerGonadalST1}" reRender="menu" rendered="#{MasterTableBrowseBean.selectedGonadalST1}">
		                <f:facet name="icon">
		                    <h:graphicImage value="../images/tick.gif"/>  
		                </f:facet>             
	                </rich:menuItem>
	            </rich:dropDownMenu>
		 			
				<h:commandLink id="updatePage" action="#{MasterTableBrowseBean.updatePage}">
					<h:graphicImage value="../images/gotopage.gif" title="Update Page" alt="update" styleClass="icon"/>
					<f:param name="actionMethod" value="updatePage" />
					<f:param name="prevSelections" value="#{MasterTableBrowseBean.selectionsString}" />
				</h:commandLink>
				
				<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
					<h:outputText value= "  (View Microarray Analysis Help) " />
				</h:outputLink>			
				
			</h:panelGrid>
			</h:panelGrid>  
			
   			<h:panelGrid columns="2" width="100%" columnClasses="leftAlign" >
				<h:outputText value="&nbsp;" escape="false"/>
   			</h:panelGrid>	
   				
		</h:panelGrid>
	</h:form>
	
	<br>

	<%-- jstl forEach can't be used because of timeing problem and jfs datatables/datalist can't be used because of confusions with template requirement --%>

	<%-- tabbed panels display --%>
	<h:panelGroup rendered="#{MasterTableBrowseBean.viewMode=='0'}" >
		<h:outputText value=" Microarray Expression Profile for: " styleClass="plaintextbold" />
		<h:outputText value="#{MasterTableBrowseBean.title}  " styleClass="plaintext" />
		
		<h:outputLink id="topGene" target="_blank" 
					value ="http://toppgene.cchmc.org/ToppGene/CheckInput.action?training_set=#{MasterTableBrowseBean.geneList}&type=HGNC&query=TOPPFUN">
<%-- 			<h:outputText value="Send To ToppFun" /> --%>
			<h:graphicImage value="../images/ToppFun_Button.png" width="60"/>
		</h:outputLink>

		<rich:tabPanel  switchType="client" >
			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[0].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[0].selected}" >
				<f:subview id="masterTableBrowse_t0">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[0].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[1].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[1].selected}">
				<f:subview id="masterTableBrowse_t1">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[1].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[2].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[2].selected}">
				<f:subview id="masterTableBrowse_t2">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[2].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[3].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[3].selected}">
				<f:subview id="masterTableBrowse_t3">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[3].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[4].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[4].selected}">
				<f:subview id="masterTableBrowse_t4">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[4].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[5].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[5].selected}">
				<f:subview id="masterTableBrowse_t5">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[5].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[6].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[6].selected}">
				<f:subview id="masterTableBrowse_t6">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[6].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

		</rich:tabPanel>
	</h:panelGroup>	

	<%-- sequential display --%>
	<h:panelGroup rendered="#{MasterTableBrowseBean.viewMode=='1'}" >
		<br>
		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[0].selected}" >	
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[0].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_0">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[0].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[1].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[1].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>
			<f:subview id="masterTableBrowse_1">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[1].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[2].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[2].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
			<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_2">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[2].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>
		
		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[3].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[3].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_3">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[3].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[4].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[4].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_4">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[4].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[5].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[5].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_5">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[5].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

		<h:panelGroup rendered="#{MasterTableBrowseBean.allMasterTables[6].selected}" >
			<h:outputText value="#{MasterTableBrowseBean.allMasterTables[6].info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
				<h:outputText value= "  (View Microarray Analysis Help)" />
			</h:outputLink>			
			<f:subview id="masterTableBrowse_6">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[6].setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

	</h:panelGroup>	


	<jsp:include page="/includes/footer.jsp" />
</f:view>

