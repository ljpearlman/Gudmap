<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://gudmap.org/jsf/core" prefix="g"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
--%>

	<h:form id="topPanelForm">
		<h:panelGrid columns="2" width="100%" styleClass="header-stripey" cellpadding="5" columnClasses="panelFirstColumn, leftAlign">
			<h:panelGrid>
				<h:outputText value="Display format: " styleClass="plaintextbold" />
				<h:selectOneRadio value="#{MasterTableBrowseBean.viewMode}" layout="pageDirection">
					<f:selectItem itemValue="0" itemLabel="tabbed panels" />
					<f:selectItem itemValue="1" itemLabel="sequential" />
				</h:selectOneRadio>
				<h:panelGrid width="100%" columnClasses="rightAlign" >
					<h:commandLink id="updatePage" action="#{MasterTableBrowseBean.updatePage}">
						<h:graphicImage value="../images/gotopage.gif" title="Update Page" alt="update" styleClass="icon"/>
						<f:param name="actionMethod" value="updatePage" />
						<f:param name="prevSelectios" value="#{MasterTableBrowseBean.selectionsString}" />
					</h:commandLink>
				</h:panelGrid>
			</h:panelGrid>
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
		</h:panelGrid>
	</h:form>
	
	<br>

	<%-- jstl forEach can't be used because of timeing problem and jfs datatables/datalist can't be used because of confusions with template requirement --%>

	<%-- tabbed panels display --%>
	<h:panelGroup rendered="#{MasterTableBrowseBean.viewMode=='0'}" >
		<h:outputText value=" Microarray Expression Profile for: " styleClass="plaintextbold" />
		<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
		<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
			<h:outputText value= "  (View Microarray Analysis Help)" />
		</h:outputLink>			
		
		<g:tabbedPanel id="masterTablePanels" value="0"  activeTabStyleClass="header-stripey" activeSubStyleClass="header-stripey" tabContentStyleClass="header-stripey">

			<g:tabPane id="Kidney_MOE430" label="#{MasterTableBrowseBean.allMasterTables[0].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[0].selected}" >
				<f:subview id="masterTableBrowse_t0">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[0].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</g:tabPane>

			<g:tabPane id="LUT_MOE430" label="#{MasterTableBrowseBean.allMasterTables[1].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[1].selected}">
				<f:subview id="masterTableBrowse_t1">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[1].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</g:tabPane>

			<g:tabPane id="Reproductive_MOE430" label="#{MasterTableBrowseBean.allMasterTables[2].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[2].selected}">
				<f:subview id="masterTableBrowse_t2">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[2].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</g:tabPane>

			<g:tabPane id="Kidney_ST1" label="#{MasterTableBrowseBean.allMasterTables[3].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[3].selected}">
				<f:subview id="masterTableBrowse_t3">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[3].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</g:tabPane>

		</g:tabbedPanel>
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
	</h:panelGroup>	


	<jsp:include page="/includes/footer.jsp" />
</f:view>

<%--
	<h:outputText value="" rendered="#{MasterTableBrowseBean.dumy}" />
	<c:forEach items="${MasterTableBrowseBean.allMasterTables}" var="masterTable">
		<c:if test="${masterTable.selected}">
			<span class="plaintextbold"><c:out value="${masterTable.info.title} Microarray Expression Profile for: " /></span>
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<c:set var="tableViewName" value="${MasterTableBrowseBean.viewName}_${masterTable.info.id}" scope="page"/>
			<f:subview id="mastertableBrowse${masterTable.info.id}">
				<% gmerg.utils.table.TableUtil.setTableViewNameParam((String)pageContext.getAttribute("tableViewName")); %>
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</c:if>
	</c:forEach>
	
	<h:dataTable value="#{MasterTableBrowseBean.allMasterTablesDataModel}" var="masterTable" >
		<h:column>
		<h:panelGroup rendered="#{masterTable.selected}">
			<h:outputText value="#{masterTable.info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			
			<f:subview id="mastertableBrowse${masterTable.info.id}">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>
		</h:column>
	</h:dataTable>


	<h:outputText value="" rendered="#{MasterTableBrowseBean.dumy}" />
	<c:forEach items="${MasterTableBrowseBean.allMasterTables}" var="masterTableJSTL">
		<c:set var="testvar" value="${masterTableJSTL}" scope="request" /> <%-- this is to make loop variable accessible for JSF EL -- %>
		<h:outputText value="#{testvar.info.title} xxxx:#{testvar.selected}" styleClass="plaintextbold" />
		<h:outputText value="" rendered="#{MasterTableBrowseBean.dumy1}" />
		<br><c:out value="${masterTableJSTL.info.title} yyyy: ${masterTableJSTL.selected} " />
		<br><c:out value="${testvar.info.title} zzz: ${testvar.selected} " />
		<br>
		<h:panelGroup rendered="#{testvar.selected}">
			<h:outputText value="#{testvar.info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<f:subview id="mastertableBrowse${testvar.info.id}">
				<h:outputText value="" rendered="#{testvar.setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>
	</c:forEach>



	<t:dataList id="mastertablesList" var="masterTable" value="#{MasterTableBrowseBean.allMasterTablesDataModel}" layout="ordered" >
		<h:panelGroup rendered="#{masterTable.selected}">
			<h:outputText value="#{masterTable.info.title} Microarray Expression Profile for: " styleClass="plaintextbold" />
			<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
			<br/>
			<c:out value="---${masterTable.info.id}---" />
			<br/>
			<c:out value="---${MasterTableBrowseBean.title}---" />
			<br/>
			<c:out value="---${MasterTableBrowseBean.masterTableId}---" />
			<br/>
			<f:subview id="mastertableBrowse${MasterTableBrowseBean.masterTableId}">
				<h:outputText value="" rendered="#{MasterTableBrowseBean.setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
			<br><br>
		</h:panelGroup>

	</t:dataList>

--%>		


