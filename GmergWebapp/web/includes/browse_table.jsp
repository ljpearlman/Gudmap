<%-- Author: Mehran Sharghi	--%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:panelGrid width="100%" cellspacing="0" cellpadding="0">
<h:form id="browseTableForm" onsubmit="return pageNumSubmition(event)" style="margin:0px" >

	<f:verbatim><div style="visibility:hidden"></f:verbatim> <%-- this only to fix a bug in IE (example happens in gene.html page %>
	<%--next two lines are to avoid f:param for distinguishingParam and parentId in each command link in the form --%>
	<h:outputText value='<input id="#{TableBean.distinguishingParam}" name="#{TableBean.distinguishingParam}" type="hidden" value="#{TableBean.tableView.name}"/>' escape="false" />
	<h:outputText value='<input id="#{TableBean.tableView.parentIdParam}" name="#{TableBean.tableView.parentIdParam}" type="hidden" value="#{TableBean.tableView.parentId}"/>' escape="false" />

	<%--This is used for expanding and shrinking table_body panel. It can not use TableBean.tableView.name because it wants to set the name value --%>
	<h:inputHidden id="tableViewName" value="#{TableBean.tableViewName}"/>
	<h:inputHidden id="tableBodyHeight" value="#{TableBean.tableView.height}" />
	<f:verbatim></div></f:verbatim> <%-- this only to fix a bug in IE (example happens in gene.html page %>

	<%-- this is useful if after log out staying in the same page
		 this is used by the header to logout when there is a table in the page 
		 ( it basically passes the parent distinguishing param if there is any ) --%>
	<h:commandLink id="logoutLink" action="#{UserBean.logout}" rendered="#{UserBean.userLoggedIn}">
	</h:commandLink>

	<%-- this is used to call an empty action method which returns null. the result is reloading the page --%>
	<h:commandLink id="reloadActionLink" action="#{TableBean.reload}" >
		<f:param name="tableOperation" value="reload" />
	</h:commandLink>

	<h:outputText styleClass="plaintext" value="<br/>#{TableBean.tableView.noDataMessage}<br/><br/>" escape="false" rendered="#{TableBean.tableEmpty}" />

	<h:panelGroup rendered="#{!TableBean.tableEmpty}" >
	
		<h:inputHidden id="selections" value="#{TableBean.selectionsString}"/>
		<h:inputHidden id="cellSelection" value="#{TableBean.cellSelection}"/>
	
		<h:panelGrid rendered="#{TableBean.tableView.displayHeader}" columns="6" border="0" frame="none" styleClass="header-stripey" width="100%" style="white-space:nowrap"
					 columnClasses="selectDeselectCol, columSelectionCol, columSelectionCol, messageCol, displayPerPageCol, entriesPerPageCol">

			<%-- =================================== Select/Desellect All Section =================================== --%>
			<%-- 
			<h:outputText rendered="#{TableBean.tableView.selectable==0}" value="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" escape="false" />

			<h:panelGroup rendered="#{TableBean.tableView.selectable!=0}">
				<h:outputLink value="#" onclick="selAll(event);return false;" title="Select all the entries in the current page" styleClass="nav3">
					<h:outputText value="Select All" />
				</h:outputLink>
				<h:outputText value=" | " />
				<h:outputLink value="#" onclick="deSelAll(event);return false;"	title="Deselect all the entries in the current page" styleClass="nav3">
					<h:outputText value="Deselect All" />
				</h:outputLink>
			</h:panelGroup>
			--%>
			<%-- ========================================== Filter Section ========================================== --%>
			<h:outputText value="" rendered="#{!TableBean.tableView.dynamicColumns}" />

			<h:panelGroup id="filterGroup" rendered="#{TableBean.filterDefined}" >
				<h:inputHidden id="filterSelections" value="#{TableBean.filtersSelection}" />
				<h:inputHidden id="filterDisplayForFirstTime" value="yes" />
				<h:graphicImage id="applyFilterImage" value="../images/ApplyFilter_btn.png" rendered="#{!TableBean.filterActive}"
								alt="ApplyFilter" styleClass="icon" style="cursor:pointer;" title="Click to apply filter to this result" onclick="filterClicked(event)"/>
				<h:graphicImage id="modifyFilterImage" value="../images/ModifyFilter_btn.png" rendered="#{TableBean.filterActive}"
								alt="ModifyFilter" styleClass="icon" style="cursor:pointer;" title="Click to modify your filter for this result" onclick="filterClicked(event)"/>
								
				<h:outputText value="<div id='#{TableBean.tableViewName}:filterPanel' style='z-index:100;max-height:500;visibility:hidden;border:1px solid black;' class='filterPanel' >" escape="false" />
					<h:panelGrid id="filterTable" width="100%" cellspacing="2" cellpadding="2" styleClass="leftAlign" >
						<h:dataTable id="filterList" width="100%" value="#{TableBean.filterList}" var="filter" cellspacing="2" cellpadding="2" border="0">
							<h:column>
								<h:selectBooleanCheckbox id="filterSelectCheckbox" value="#{filter.active}" onclick="filterSelection(event, this)" />
							</h:column>
							<h:column>
								<h:outputText styleClass="plaintext" value="#{filter.name}" />
							</h:column>
							<h:column>
								<h:inputHidden id="filterOriginalValue1" value="#{filter.originalValue1}" />
								<h:inputText id="filterValue1" rendered="#{filter.type=='SIMPLE' || filter.type=='SIMPLERANGE'}"
											 value="#{filter.value1}" styleClass="plaintextt" onblur="this.value=trim(this.value)" />
								<h:selectOneMenu id="filterListValue1" rendered="#{filter.typeString=='LIST' || filter.typeString=='LISTRANGE'}" 
												 value="#{filter.value1}" >
									<f:selectItems value="#{filter.selectOptions}"/>
								</h:selectOneMenu>
								<h:selectManyCheckbox id="filterCheckboxValue" rendered="#{filter.typeString=='CHECKBOX'}" 
												 value="#{filter.multipleValue}" >
									<f:selectItems value="#{filter.selectOptions}"/>
								</h:selectManyCheckbox>
								<h:selectManyListbox id="filterMultipleValue" rendered="#{filter.typeString=='MULTIPLE'}" 
												 value="#{filter.multipleValue}" >
									<f:selectItems value="#{filter.selectOptions}"/>
								</h:selectManyListbox>
								<h:selectOneRadio id="filterRadioValue" rendered="#{filter.typeString=='RADIO'}" 
												 value="#{filter.value1}" >
									<f:selectItems value="#{filter.selectOptions}"/>
								</h:selectOneRadio>
								<t:inputCalendar id="filterDateValue1" rendered="#{filter.typeString=='DATE' || filter.typeString=='DATERANGE'}" 
												 value="#{filter.date1}" size="10" styleClass="plaintext"
												 popupButtonImageUrl="../images/down.gif" renderPopupButtonAsImage="true" 
												 renderAsPopup="true" popupDateFormat="dd-MM-yyyy" helpText="DD-MM-YYYY" onchange="validateDate(this)" />
								
								<h:panelGroup rendered="#{filter.range}" >
									<h:inputHidden id="filterOriginalRangeSelect" value="#{filter.rangeSelect}" />
									<h:inputHidden id="filterOriginalValue2" value="#{filter.originalValue2}" />
									<h:outputText value="&nbsp;&nbsp&nbsp;" escape="false" />
									<h:selectBooleanCheckbox id="filterRangeSelect" value="#{filter.rangeSelect}" onclick="filterRangeSelectClicked(event, this)" />
									<h:outputText value="To:" styleClass="plaintextbold" />
									<h:inputText id="filterValue2" rendered="#{filter.typeString=='SIMPLERANGE'}" 
												 value="#{filter.value2}" styleClass="plaintext" onblur="this.value=trim(this.value)" />
									<h:selectOneMenu id="filterListValue2" rendered="#{filter.typeString=='LISTRANGE'}"
													 value="#{filter.value2}" >
										<f:selectItems value="#{filter.selectOptions}"/>
									</h:selectOneMenu>
									<t:inputCalendar id="filterDateValue2" rendered="#{filter.typeString=='DATERANGE'}" 
													 value="#{filter.date2}" size="10" styleClass="plaintext"
													 popupButtonImageUrl="../images/down.gif" renderPopupButtonAsImage="true" 
													 renderAsPopup="true" popupDateFormat="dd-MM-yyyy" helpText="DD-MM-YYYY" onchange="validateDate(this)" />
								</h:panelGroup>
							</h:column>
						</h:dataTable>
						<h:outputText id="filterMessage" value="&nbsp;" escape="false" styleClass="errorMessage" />
						<h:panelGrid border="0" id="filterActions" width="90%" columns="4" columnClasses="leftAlign,centreAlign,centreAlign,rightAlign" styleClass="centreAlign">
							<h:outputLink onclick="setAllFiltersState(true, event); return false;" styleClass="nav3" >
								<h:outputText value="Select All" />
							</h:outputLink>
							<h:outputLink onclick="setAllFiltersState(false, event); return false;" styleClass="nav3">
								<h:outputText value="Deselect All"  />
							</h:outputLink>
							<h:outputLink onclick="cancelFilter(event); return false;" styleClass="nav3" >
								<h:outputText value="Cancel" />
							</h:outputLink>
							<h:outputLink onclick="applyFilter(event); return false;" styleClass="nav3" >
								<h:outputText value="Apply" />
							</h:outputLink>
						</h:panelGrid>
					</h:panelGrid>
				<h:outputText value="</div>" escape="false"/>
				<h:commandLink id="filterActionLink" action="#{TableBean.applyFilter}" >
					<f:param name="tableOperation" value="applyFilter" />
				</h:commandLink>
			</h:panelGroup>
			
			<%-- ==================================== Columns Add/Remove Section ==================================== --%>
			<h:outputText value="" rendered="#{!TableBean.tableView.dynamicColumns}" />

			<h:panelGroup rendered="#{TableBean.tableView.dynamicColumns}" id="columnSelectionGroup">
				<h:inputHidden id="minColNum" value="#{TableBean.tableView.minColNum}" />
				<h:inputHidden id="maxColNum" value="#{TableBean.tableView.maxColNum}" />
				<h:inputHidden id="visibleColNum" value="#{TableBean.visibleColNum}" />
				<h:inputHidden id="columnSelections" value="#{TableBean.columnSelections}"/>
				<h:inputHidden id="defaultColumns" value="#{TableBean.defaultColumns}"/>
				<h:graphicImage id="columnSelectionsImage" value="../images/ColumnSelection_btn.png" title="Click to add/remove Columns"
								alt="Add/Remove Columns" styleClass="icon" style="cursor:pointer;" onclick="columnSelectionClicked(event)" rendered="#{!TableBean.tableView.inTabPane}" />
				<h:outputText value="<div id='#{TableBean.tableViewName}:columnSelectionPanel' style='position:absolute;z-index:100;height:1px;visibility:hidden;border:1px solid black;' class='columnSelectionPanel' onmouseout='columnSelectionPanelMouseOut(event)'>" escape="false" />
					<h:panelGrid id="columnSelectionTable" width="100%" cellspacing="2" cellpadding="2" styleClass="leftAlign" onmouseout="columnSelectionPanelMouseOut(event)" >
						<h:dataTable id="columnSelectionColumnsList" width="100%" value="#{TableBean.columnList}" var="column" cellspacing="2" cellpadding="2"
									 columnClasses="columnSelection1, columnSelection2" border="0">
							<h:column>
								<h:selectBooleanCheckbox value="#{column[0]}" onclick="columnSelection(event, this)" />
							</h:column>
							<h:column>
								<h:outputText styleClass="plaintext" value="#{column[1]}" />
							</h:column>
						</h:dataTable>
						<h:outputText id="columnMessage" value=" " escape="false" styleClass="nav3" style="color:RED"/>
						<h:panelGrid border="0" id="columnSelectionActions" width="100%" columns="3" columnClasses="leftAlign,centreAlign,rightAlign">
							<h:outputLink onclick="selectAllColumns(event); return false;" styleClass="nav3"
										  rendered="#{!(TableBean.tableView.columnsLimited  && TableBean.tableView.defaultColumns)}" >
								<h:outputText value="All" rendered="#{!TableBean.tableView.columnsLimited}" />
								<h:outputText value="" rendered="#{TableBean.tableView.columnsLimited && !TableBean.tableView.defaultColumns}" />
							</h:outputLink>
							<h:outputLink onclick="selectDefaultColumns(event); return false;" styleClass="nav3" >
								<h:outputText value="Defaults" rendered="#{TableBean.tableView.defaultColumns}" />
								<h:outputText value="" rendered="#{!TableBean.tableView.defaultColumns}" />
							</h:outputLink>
							<h:outputText value="" rendered="#{TableBean.tableView.columnsLimited && TableBean.tableView.defaultColumns}" />
							<h:outputLink onclick="hideColumnSelectionPanel(event); return false;" styleClass="nav3" >
								<h:outputText value="Close" />
							</h:outputLink>
						</h:panelGrid>
					</h:panelGrid>
				<h:outputText value="</div>" escape="false"/>
				<h:commandLink id="columnSelectionActionLink" action="#{TableBean.applyColumnSelection}" >
					<f:param name="tableOperation" value="applyColumnSelection" />
				</h:commandLink>
			</h:panelGroup>

			<h:commandLink id="defaultSort" rendered="#{TableBean.defaultOrderDefined}" 
						   title="Click to sort by default order" action="#{TableBean.sortByDefault}" styleClass="nav3" >
				<h:graphicImage value="../images/DefaultOrder_btn.png" alt=" Default order " styleClass="icon"/>
				<f:param name="tableOperation" value="sortByDefault" />
			</h:commandLink>
			<h:outputText value="" rendered="#{!TableBean.defaultOrderDefined}" />
			<%-- ===================================== Navigation panel Message ===================================== --%>
			<h:outputText value="#{TableBean.tableView.navigationPanelMessage}" styleClass="nav3" escape="false"/>

			<%-- ===================================== Entries Per Page Section ===================================== --%>
			<h:panelGrid columns="3" style="margin-left:auto; margin-right:0" >
				<h:outputText value="Display " styleClass="nav3" />
				<h:selectOneMenu id="resultsPerPage" value="#{TableBean.resultsPerPage}" onchange="clickPerPage(event)" >
					<f:selectItems value="#{TableBean.perPageOptions}"/>
				</h:selectOneMenu>
				<h:outputText value=" entries per page" styleClass="nav3" />
				<h:commandLink id="perPage" action="#{TableBean.changeResultsPerPage}" >
					<f:param name="tableOperation" value="perPage" />
				</h:commandLink>
			</h:panelGrid>

			<%-- ============================================= Help Icon ============================================ --%>

		</h:panelGrid>

		<%-- ==================================== Page Navigation Section ==================================== --%>
		<h:inputHidden id="maxPageNum" value="#{TableBean.maxPageNum}" />
		<h:panelGrid rendered="#{(TableBean.tableView.numPages > 0 || TableBean.tableView.heightFlexible) && TableBean.tableView.displayHeader}"
					 border="0" width="100%" styleClass="stripey" cellpadding="2" cellspacing="2" style="white-space:nowrap" 
					 columns="5" columnClasses="navPageNoCol, centreAlign, centreAlign, centreAlign, navGotoBoxCol, navGotoButtonCol, navScrollCol" >

			<h:outputText rendered="#{TableBean.tableView.table.numRows > 0}" value="#{TableBean.tableView.table.numRows} Row: Page #{TableBean.tableView.currentPage} of #{TableBean.tableView.numPages}" styleClass="plaintext"/>

			<h:panelGroup rendered="#{TableBean.tableView.numPages > 1}">
				<h:commandLink id="first" action="#{TableBean.tableNavigation}" styleClass="nav3" >
					<h:graphicImage value="../images/First_btn.png" title="First Page" alt="first" styleClass="icon" />
					<f:param name="tableOperation" value="firstPage" />
				</h:commandLink>
 				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="prev" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/Previous_btn.png" title="Previous Page" alt="prev" styleClass="icon" />
					<f:param name="tableOperation" value="prevPage" />
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="next" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/Next_btn.png" title="Next Page" alt="next" styleClass="icon" />
					<f:param name="tableOperation" value="nextPage" />
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="last" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/Last_btn.png" title="Last Page" alt="last" styleClass="icon" />
					<f:param name="tableOperation" value="lastPage" />
				</h:commandLink>
			</h:panelGroup>

			<%-- ===================================== Heatmap Section ======================================= --%>
			<h:panelGrid rendered="#{TableBean.tableView.heatmapColumns!=null}" border="0" columns="4" cellpadding="0" cellspacing="0" style="margin:auto;" >
				<h:outputText value="Heatmap" styleClass="plaintext"/>
				<h:selectBooleanCheckbox value="#{TableBean.tableView.displayHeatmap}" onclick="reload(event)" />
				<h:outputText value="&nbsp;Heatmap values" styleClass="plaintext" escape="false"/>
				<h:selectBooleanCheckbox value="#{TableBean.tableView.displayHeatmapValues}" onclick="reload(event)" />
			</h:panelGrid>
			<h:outputText value=" " rendered="#{TableBean.tableView.heatmapColumns==null}" />

			<%-- ==================================== Goto page Section ====================================== --%>
			<h:panelGroup rendered="#{TableBean.tableView.numPages > 1}" >
			                <h:panelGrid columns="4" style="margin-left:auto; margin-right:0" >
				         <h:outputText value="Go to page: " styleClass="nav3" />
				         <h:inputText id="pageNum" value="#{TableBean.pageNum}" size="3"/>
				         <h:outputText value=" " styleClass="nav3" />
				         <h:commandLink id="gotoPage" action="#{TableBean.tableNavigation}" onclick="return validatePageNum(event)" >
					<h:graphicImage value="../images/Go_btn.png" title="Go to page" alt="next" styleClass="icon" />
					<f:param name="tableOperation" value="gotoPage" />
				         </h:commandLink>
													                                </h:panelGrid>
			</h:panelGroup>
			
			<%-- ================================ Expand/Shrink body Section ================================= --%>
<%-- scrolling within a table does not work properly, any browser window has its own scrolling function, no need to have both
			<h:panelGroup rendered="#{TableBean.tableView.heightLimittedFlexible}" >
				<h:graphicImage value="../images/FlexS_Expand_btn.png" title="click to expand scroll area" styleClass="icon" style="cursor:pointer;"
								onclick="expandTableBodyPanel(event)" onmousedown="continuousExpandTableBodyPanel(event)" />
				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				<h:graphicImage value="../images/FlexS_Shrink_btn.png" title="click to shrink scroll area" styleClass="icon" style="cursor:pointer;"
								onclick="shrinkTableBodyPanel(event)" onmousedown="continuousShrinkTableBodyPanel(event)" />
				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="fixedHeight" action="#{TableBean.heightModeChange}" styleClass="nav3" >
					<h:graphicImage value="../images/FlexS_All_btn.png" title="click to display all" alt="all" styleClass="icon" />
					<f:param name="tableOperation" value="unlimittedHeight" />
				</h:commandLink>
				<f:verbatim>&nbsp;</f:verbatim>
			</h:panelGroup>
			<h:commandLink id="flexibleHeight"  rendered="#{TableBean.tableView.heightUnlimittedFlexible}"
							action="#{TableBean.heightModeChange}" styleClass="nav3" title="click to display data in a scrollable frame" >
				<h:graphicImage value="../images/FlexibleScroll_btn.png" alt=" Flexible scroll " styleClass="icon" width="100" height="32" />
				<f:param name="tableOperation" value="limittedHeight" />
			</h:commandLink>
--%>
		</h:panelGrid>
		<%-- ==================================== Table Body Section ==================================== --%>
		<h:inputHidden id="actualRowsPerPage" value="#{TableBean.actualRowsPerPage}" />
		<h:outputText styleClass="plaintext" value="<br/>There is no result to display, modify your filter and try again.<br/><br/>" escape="false" rendered="#{TableBean.filteredTableEmpty}" />
		<h:panelGroup rendered="#{!TableBean.filteredTableEmpty}" >
			<h:outputText escape="false" rendered="#{!TableBean.tableView.heightLimitted}" 
						  value=" <div name='tableBodyPanel' id='tableBodyPanel' class='bodySection' > " />

			<h:outputText escape="false" rendered="#{TableBean.tableView.heightLimitted}" 
						  value=" <div id='tableBodyContainer' class='bodySectionContainer' style='height:#{TableBean.tableView.height}px;'> 
						  		 <div name='tableBodyPanel' id='tableBodyPanel' class='bodySection' style='height:100%;' > " />
			<%-- The actual content is used instead of including jsp file
				<f:subview id="tableBodyLimitted">
					<jsp:include page="table_body.jsp" />
				</f:subview>
			--%>						  		 
				<%-- ======================= Generic Table Body =========================== --%>
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
				
						<%------------------ Complex DataItem that conatins an array of DataItems (80, 81, 82) ----------------%>
						<h:panelGroup rendered="#{TableBean.complexDataItem }" >
							<h:dataTable value="#{TableBean.dataItemComplexValue}" columnClasses="leftAlign" style="#{TableBean.complexDataItemStyle}" >
								<h:column>
									<jsp:include page="table_body_dataItem.jsp" />
								</h:column>
							</h:dataTable>
						</h:panelGroup>
					</t:columns>
				</t:dataTable><f:verbatim></div> </f:verbatim>
			<f:verbatim rendered="#{TableBean.tableView.heightLimitted}" >
				<div id="bodyPanelDumy" class="bodySectionDumy" onscroll="tableBodyPanelOnscroll(this.parentNode);" onmousewheel="tableBodyPanelOnscroll(this.parentNode);" >
					<div id="internalDumy" style="width:1px;" ></div>
				</div>
				</div>
			</f:verbatim>
			
		</h:panelGroup >

		<%-- ==================================== Collection Buttons Section ==================================== --%>
		<h:panelGroup rendered="#{TableBean.collectionBottons>=0}" >
			<f:verbatim>
			<div style="width:100%; float:left">
			</f:verbatim>		
				<h:commandLink id="collectionOperationLink" action="#{TableBean.collectionOperation}" >
					<f:param name="tableOperation" value="" />
				</h:commandLink>
				
				
				<%-- ==================================== Page Navigation Section 2 ==================================== --%>
 				
		    <h:panelGrid rendered="#{(TableBean.tableView.numPages > 1 || TableBean.tableView.heightFlexible) && TableBean.tableView.displayHeader}"
					 border="0" width="100%" styleClass="stripey" cellpadding="2" cellspacing="2" style="white-space:nowrap" 
					 columns="5" columnClasses="navPageNoCol, centreAlign, centreAlign, centreAlign, navGotoBoxCol, navGotoButtonCol, navScrollCol" >

				<h:outputText value="#{TableBean.tableView.table.numRows} Rows: Page #{TableBean.tableView.currentPage} of #{TableBean.tableView.numPages}" styleClass="plaintext"/>
	
				<h:panelGroup rendered="#{TableBean.tableView.numPages > 1}">
					<h:commandLink id="first2" action="#{TableBean.tableNavigation}" styleClass="nav3" >
						<h:graphicImage value="../images/First_btn.png" title="First Page" alt="first" styleClass="icon" />
						<f:param name="tableOperation" value="firstPage" />
					</h:commandLink>
	 				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
					<h:commandLink id="prev2" action="#{TableBean.tableNavigation}" >
						<h:graphicImage value="../images/Previous_btn.png" title="Previous Page" alt="prev" styleClass="icon" />
						<f:param name="tableOperation" value="prevPage" />
					</h:commandLink>
					<f:verbatim>&nbsp;&nbsp;&nbsp;</f:verbatim>
					<h:commandLink id="next2" action="#{TableBean.tableNavigation}" >
						<h:graphicImage value="../images/Next_btn.png" title="Next Page" alt="next" styleClass="icon" />
						<f:param name="tableOperation" value="nextPage" />
					</h:commandLink>
					<f:verbatim>&nbsp;&nbsp;</f:verbatim>
					<h:commandLink id="last2" action="#{TableBean.tableNavigation}" >
						<h:graphicImage value="../images/Last_btn.png" title="Last Page" alt="last" styleClass="icon" />
						<f:param name="tableOperation" value="lastPage" />
					</h:commandLink>
				</h:panelGroup>

			<%-- ==================================== Goto page Section 2 ====================================== --%>
			                <h:panelGroup rendered="#{TableBean.tableView.numPages > 1}" >
			                     <h:panelGrid columns="4" style="margin-left:auto; margin-right:0" >
				         <h:outputText value="Go to page: " styleClass="nav3" />
				         <h:inputText  id="pageNum2" value="#{TableBean.pageNum2}" size="3"/>
				         <h:outputText value=" " styleClass="nav3" />
				         <h:commandLink id="gotoPage2" action="#{TableBean.tableNavigation}" onclick="return validatePageNum2(event)" >
					<h:graphicImage value="../images/Go_btn.png" title="Go to page" alt="next" styleClass="icon" />
					<f:param name="tableOperation" value="gotoPage" />
				         </h:commandLink>
													                                     </h:panelGrid>
			                </h:panelGroup>
			</h:panelGrid>														
			<h:panelGrid columns="3" styleClass="header-stripey" border="0" width="100%"  columnClasses="leftAlign,rightAlign,collectionButtonsCol3" 
						rendered="#{TableBean.multipleCollections && TableBean.collectionBottons!=4}">
				<h:panelGroup>
					<h:outputText value="Items in my " styleClass="plaintext" />
					<h:selectOneMenu value="#{TableBean.selectedCollection}" onchange="reload(event)">
						<f:selectItems value="#{TableBean.collectionsSelectItems}" />
					</h:selectOneMenu>
					<h:outputText value=" : <span class='plaintextbold'>#{TableBean.clipboardItemsNum}</span>" styleClass="plaintext" escape="false" />						
				</h:panelGroup>
			<!-- //NEW COLLECTIONS 1 -->
				<%-- <h:outputLink id="intersectClipboard" styleClass="plaintextbold" onclick="return processSelectionsForLink(event)" 
							  value="clipboardOperation_browse.html?collectionOperation=intersection&collectionType=#{TableBean.selectedCollection}">
					<h:outputText value="Get intersection with my #{TableBean.clipboardName}" />
				</h:outputLink>

				<h:graphicImage url="/images/gu_intersect.gif" alt="intersection"/> --%>
				
			</h:panelGrid>
				
											
			<h:panelGrid columns="3" styleClass="header-stripey" columnClasses="leftAlign,rightAlign,collectionButtonsCol3" 
							 rendered="#{TableBean.collectionBottons!=4}" border="0" width="100%">
				
					<h:outputText value="Items in my #{TableBean.clipboardName}: <span class='plaintextbold'>#{TableBean.clipboardItemsNum}</span>" 
								  styleClass="plaintext" escape="false" rendered="#{!TableBean.multipleCollections}" />
					<%-- <h:panelGrid rendered="#{!TableBean.multipleCollections && (TableBean.collectionBottons==2 || TableBean.collectionBottons==3)}" styleClass="rightAlign" > --%>
					<h:panelGrid rendered="#{!TableBean.multipleCollections && (TableBean.collectionBottons==99)}" styleClass="rightAlign" >
						<h:commandLink id="replaceClipboard" action="#{TableBean.replaceClipboard}" styleClass="plaintextbold" >
							<h:outputText value="Replace my #{TableBean.clipboardName} with this collection" 
											rendered="#{TableBean.collectionBottons==2}" />
							<h:outputText value="Replace my #{TableBean.clipboardName} with all items in this result" 
											rendered="#{TableBean.collectionBottons==3}" />
							<f:param name="tableOperation" value="replaceClipboard" />
						</h:commandLink>
					</h:panelGrid>
				 	
				<!-- NEW COLLECTIONS 2-->
				<%-- <h:panelGroup rendered="#{!TableBean.multipleCollections && !(TableBean.collectionBottons==2 || TableBean.collectionBottons==3)}">
				<h:outputLink id="intersectClipboard2" styleClass="plaintextbold" onclick="return processSelectionsForLink(event)" 
								  value="clipboardOperation_browse.html?collectionOperation=intersection&collectionType=#{TableBean.selectedCollection}">
						<h:outputText value="Get intersection with my #{TableBean.clipboardName}" />
				</h:outputLink>
				</h:panelGroup>
				<h:graphicImage url="/images/gu_intersect.gif" alt="intersection" rendered="#{!TableBean.multipleCollections}"/> --%>
				
				<%-- <h:panelGroup>	 
							
					<h:commandLink id="addClipboard" action="#{TableBean.addToClipboard}" onclick="return saveSelections(event)" >
						<h:graphicImage value="../images/Add_btn.png"		title="Add" alt="next" styleClass="icon" />
						<f:param name="tableOperation" value="addClipboard" />
					</h:commandLink>
					<h:commandLink id="replaceClipboardWithSelected1" action="#{TableBean.replaceClipboardWithSelected}" onclick="return saveSelections(event)" >
						<h:graphicImage value="../images/Replace_btn.png"		title="Replace" alt="next" styleClass="icon" />
						<f:param name="tableOperation" value="replaceClipboardWithSelected" />
					</h:commandLink>
				
				</h:panelGroup>	 --%>
				
				<h:panelGroup rendered="#{TableBean.collectionBottons==1}">	 
							
					<h:commandLink id="addClipboard" action="#{TableBean.addToClipboard}" onclick="return saveSelections(event)" >
						<h:graphicImage value="../images/Add_btn.png"		title="Add" alt="next" styleClass="icon" />
						<f:param name="tableOperation" value="addClipboard" />
					</h:commandLink>
					<h:commandLink id="replaceClipboardWithSelected1" action="#{TableBean.replaceClipboardWithSelected}" onclick="return saveSelections(event)" >
						<h:graphicImage value="../images/Replace_btn.png"		title="Replace" alt="next" styleClass="icon" />
						<f:param name="tableOperation" value="replaceClipboardWithSelected" />
					</h:commandLink>
				
				</h:panelGroup>	
							
					<!-- //NEW COLLECTIONS 3-->
					<%-- <h:outputLink id="differenceCollection" onclick="return processSelectionsForLink(event)" styleClass="plaintextbold" 
								  value="clipboardOperation_browse.html?collectionOperation=difference&collectionType=#{TableBean.selectedCollection}">
						<h:outputText value="Get difference with my #{TableBean.clipboardName}" />
					</h:outputLink>
					<h:graphicImage url="/images/gu_difference.gif" alt="difference"/> --%>
				
					
			    </h:panelGrid>

											
				<h:panelGrid columns="2" styleClass="header-stripey" columnClasses="leftAlign,rightAlign" 
							 rendered="#{TableBean.collectionBottons!=4}" border="0" width="100%">
					 
					<h:outputText value="" rendered="#{TableBean.clipboardItemsNum == 0}"/>					
					<h:outputLink id="ViewClipboard1" styleClass="plaintextbold" rendered="#{TableBean.clipboardItemsNum > 0}"
								  value="collection_browse.html?collectionType=#{TableBean.selectedCollection}">
						<h:outputText value="View my #{TableBean.clipboardName} (or other selections)" />
					</h:outputLink>
					<h:outputLink id="ViewAllCollections1" value="collectionList_browse.html" styleClass="plaintextbold" rendered="#{UserBean.userLoggedIn}" >
						<h:outputText value="View stored collections" />
					</h:outputLink>
					<h:commandLink id="deleteSelectedEntries" action="#{TableBean.deleteSelectedEntries}" onclick="saveSelections(event)"
									rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && TableBean.tableView.table.deletable}" >
						<h:outputText value="Delete Selected Entries" />
						<f:param name="tableOperation" value="deleteSelected" />
					</h:commandLink>
					<h:outputText value="" />
				</h:panelGrid>
				
				<h:panelGrid columns="2" styleClass="header-stripey" border="0" width="100%" columnClasses="leftAlign,rightAlign" 
							 rendered="#{TableBean.collectionBottons==4}" >
				
					<h:outputText value="Items in my #{TableBean.clipboardName}: <span class='plaintextbold'>#{TableBean.clipboardItemsNum}</span>" 
								  styleClass="plaintext" escape="false" />
					<h:outputText value="" />
					<h:commandLink id="replaceClipboardWithSelected2" action="#{TableBean.replaceClipboardWithSelected}" 
									onclick="return saveSelections(event)" styleClass="plaintextbold" >
						<h:outputText value="Replace my #{TableBean.clipboardName}" />
						<f:param name="tableOperation" value="replaceClipboardWithSelected" />
					</h:commandLink>
					<h:commandLink id="replaceClipboard2" action="#{TableBean.replaceClipboard}" styleClass="plaintextbold" >
						<h:outputText value="Replace my #{TableBean.clipboardName} with all items in this result" />
						<f:param name="tableOperation" value="replaceClipboard" />
					</h:commandLink>
					
					<h:outputLink id="ViewClipboard2" styleClass="plaintextbold"
								  value="collection_browse.html?collectionType=#{TableBean.selectedCollection}">
						<h:outputText value="View my #{TableBean.clipboardName} (or other selections)" />
					</h:outputLink>
					<h:outputLink id="ViewAllCollections2" value="collectionList_browse.html" styleClass="plaintextbold" rendered="#{UserBean.userLoggedIn}" >
						<h:outputText value="View stored collections" />
					</h:outputLink>
					<h:commandLink id="deleteSelectedEntries2" action="#{TableBean.deleteSelectedEntries}" onclick="saveSelections(event)"
									rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=5 && TableBean.tableView.table.deletable}" >
						<h:outputText value="Delete Selected Entries" />
						<f:param name="tableOperation" value="deleteSelected" />
					</h:commandLink>
				</h:panelGrid>
				
			<f:verbatim></div></f:verbatim>		
		</h:panelGroup>		
	</h:panelGroup>

</h:form>
</h:panelGrid>
