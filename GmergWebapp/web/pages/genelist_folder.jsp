<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	<h:form id="mainForm" >
		<t:tree id="tree" value="#{GenelistFolderBean.treeModel}"
				var="treeItem"
				styleClass="treeTable; plaintext"
				nodeClass="treeTableNode plaintext"
				headerClass="align-top-stripey treeTableHeader plaintextBold"
				rowClasses="table-stripey, table-nostripe"
				columnClasses="treeTableColumn, treeTableColumn, treeTableColumn centreAlign, treeTableColumn centreAlign, treeTableColumn centreAlign"
				expandRoot="true" iconClass="treeTableIcon">
			<t:treeColumn>
				<f:facet name="header">
					<h:outputText value="Gene List"/>
				</f:facet>
				<h:outputText value="#{treeItem.title}" escape="false" rendered="#{!treeItem.heatmapAvailable && treeItem.title != 'Brunskill et al (Dev Cell(2008) 15(5):781-91)'}" styleClass="#{treeItem.styleClass}"/>
				<h:panelGroup rendered="#{treeItem.title == 'Brunskill et al (Dev Cell(2008) 15(5):781-91)'}">
					<h:outputText value="Brunskill et al (" styleClass="#{treeItem.styleClass}" />
					<h:outputLink title="click to view publication" value="http://www.ncbi.nlm.nih.gov/pubmed/19000842" >
						<h:outputText value="Dev Cell(2008) 15(5):781-91" escape="false"/>
					</h:outputLink>
					<h:outputText value=")" styleClass="#{treeItem.styleClass}" />
				
				</h:panelGroup>
				<h:outputLink title="click to view gene list" value="mastertable_browse.html?genelistId=#{treeItem.genelistId}" rendered="#{treeItem.heatmapAvailable}" >
					<h:outputText value="#{treeItem.title}" escape="false"/>
				</h:outputLink>
			</t:treeColumn>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Lab" />
				</f:facet>
				<h:outputText value="#{treeItem.submitter}"  escape="false"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Summary" />
				</f:facet>
				<h:outputText value="#{treeItem.description}" escape="false" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Number of Probes" />
				</f:facet>
				<h:outputText value="#{treeItem.entriesNumber}" escape="false" styleClass="centreAlign" style="text-align: center" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Java&nbsp;Treeview" escape="false" />
				</f:facet>
				<h:outputText value="&nbsp;" escape="false" rendered="#{!treeItem.treeviewAvailable}" />
				<h:outputLink rendered="#{treeItem.treeviewAvailable}" 
							  value="" styleClass="plaintext" target="_blank" 
							  onclick="openTreeviewApplet('#{treeItem.treeviewParams}'); return false;">
					<h:outputText value="Treeview" escape="false" styleClass="centreAlign" />
				</h:outputLink>

			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Source File" />
				</f:facet>
				<h:outputText value="&nbsp;" escape="false" rendered="#{!treeItem.genelistNode}" />
				<h:outputLink value="#{treeItem.download}" rendered="#{treeItem.genelistNode}" >
					<h:outputText value="download" />
				</h:outputLink>
			</h:column>
		</t:tree>

		<%-- ++++++++++++++++++++++++++++++ TreeView Applet +++++++++++++++++++++++++++++++++++++++++ --% >
		<h:outputText value="#{GenelistFolderBean.javaTreeViewAppletCode}" escape="false" rendered="#{GenelistFolderBean.displayTreeView}" />

		<h:commandLink id="treeviewLink" action="#{GenelistFolderBean.launchTreeView}" >
			<f:param name="treeviewCommand" value="yes" />
		</h:commandLink>
--%>		
	</h:form>
 
	<jsp:include page="/includes/footer.jsp" />
</f:view>
