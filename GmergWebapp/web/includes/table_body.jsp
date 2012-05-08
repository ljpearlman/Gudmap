<%-- Author: Mehran Sharghi	--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%-- ==================================== Table Body Section ==================================== --%>
<t:dataTable id="genericTable" value="#{TableBean.data}" var="row" rows="#{TableBean.tableView.displayRowsPerPage}"
			 width="#{TableBean.width}" cellspacing="#{TableBean.cellSpacing}" cellpadding="#{TableBean.cellPadding}" border="0" bgcolor="white"
			 rowClasses="table-stripey, table-nostripe" styleClass="bodySectionTable" >
			 
	<t:columns value="#{TableBean.columnHeaders}" var="columnHeader" headertitle="#{TableBean.columnHeaderMouseOver}"
				headerstyleClass="#{TableBean.columnHeaderStyleClass}" style="#{TableBean.columnStyle}" >
		<f:facet name="header">
			<h:panelGroup>
				<h:panelGroup rendered="#{columnHeader.type==0}" >
					<h:outputText rendered="#{!columnHeader.sortable}"
								  value="#{columnHeader.title} #{TableBean.total}" styleClass="plaintextbold" escape="false" />
					<h:commandLink rendered="#{columnHeader.sortable}" 
								   action="#{TableBean.sort}"  title="#{columnHeader.mouseOver}" styleClass="plaintextbold" >
						<h:outputText rendered="#{columnHeader.type==0}" value="#{columnHeader.title} #{TableBean.total}" escape="false" />
						<f:param name="tableOperation" value="sort" />
						<f:param name="title" value="#{columnHeader.title}" />
					</h:commandLink>
				</h:panelGroup>
				<h:panelGroup rendered="#{columnHeader.type==1}" >
					<h:graphicImage rendered="#{!columnHeader.sortable}" styleClass="icon" value="#{columnHeader.imageName}" alt="    " />
					<h:commandLink rendered="#{columnHeader.sortable}" 
								   action="#{TableBean.sort}"  title="#{columnHeader.mouseOver}" styleClass="plaintextbold" >
						<h:graphicImage styleClass="icon" value="#{columnHeader.imageName}" alt="    " />
						<f:param name="tableOperation" value="sort" />
						<f:param name="title" value="#{columnHeader.title}" />
					</h:commandLink>
				</h:panelGroup>
				<h:selectBooleanCheckbox rendered="#{columnHeader.type==2}" id="toggleSelectAll" value="false"
										 title="click to select/deselect all items in this page" onclick="toggleSelectAll(event, this);" >
				</h:selectBooleanCheckbox>
			</h:panelGroup>
		</f:facet>
		
		<jsp:include page="table_body_dataItem.jsp" />

		<%-------------------- Complex DataItem that conatins an array of DataItems (80) ------------------%>
		<h:panelGroup rendered="#{TableBean.complexDataItem }" >
			<h:dataTable value="#{TableBean.dataItemComplexValue}" columnClasses="leftAlign" >
				<h:column>
					<jsp:include page="table_body_dataItem.jsp" />
				</h:column>
			</h:dataTable>
		</h:panelGroup>

	</t:columns>

</t:dataTable>
