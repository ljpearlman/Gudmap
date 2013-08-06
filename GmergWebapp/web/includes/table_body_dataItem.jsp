<%-- Author: Mehran Sharghi	--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

	<h:panelGroup>
		<%--------------------- Link to new window with no specifications (1,2,3) --------------------------%> 
		<%-- new window for diferent columns --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==1}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="#{TableBean.tableView.name}#{columnHeader.nameEncodedTitle}" title="#{TableBean.dataItem.title}">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<%-- always new window --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==2}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="#{TableBean.dataItem.nameEncodedLink}" title="#{TableBean.dataItem.title}">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<%-- always same new window for this table--%>
		<h:outputLink rendered="#{TableBean.dataItemtype==3}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="#{TableBean.tableView.name}" title="#{TableBean.dataItem.title}">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<!-- always same new window no matter what the link or page-->
		<h:outputLink rendered="#{TableBean.dataItemtype==9}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="gmerg_external" title="#{TableBean.dataItem.title}">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		
		<%----------------------- Link to new window with specificatons (4,5,6,7) ---------------------------%> 
		<%-- new window for diferent columns --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==4}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.tableView.name}#{columnHeader.nameEncodedTitle}','#{TableBean.dataItem.windowParams}');return false" >
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<%-- always new window: using link as name --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==5}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.dataItem.nameEncodedLink}','#{TableBean.dataItem.windowParams}');return false">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		
		<%-- always new window: using value as name --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==6}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.dataItem.nameEncodedValue}','#{TableBean.dataItem.windowParams}');return false">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>

		<%-- always new window: using viewname&value as name --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==7}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('#{TableBean.dataItem.link}',''#{TableBean.tableView.name}#{TableBean.dataItem.nameEncodedValue}','#{TableBean.dataItem.windowParams}');return false">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>

		<%-- always same new window for this table--%>
		<h:outputLink rendered="#{TableBean.dataItemtype==8}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.tableView.name}','#{TableBean.dataItem.windowParams}');return false" >
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		
		<%---------------------------------- Link to the same window (10) -----------------------------------%> 
		<%-- same window link --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==10}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="_top" title="#{TableBean.dataItem.title}" >
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>

		<%------------------------------------ Thumbnails (12, 13, 14) --------------------------------------%> 
		<%-- (12) always new window without controls--%>
		<h:outputLink rendered="#{TableBean.dataItemtype==12}" 
					  value="#" styleClass="plaintext" target="_blank"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.dataItem.nameEncodedTitle}','#{TableBean.dataItem.windowParams}');return false">
			<h:graphicImage styleClass="icon" height="50" value="#{TableBean.dataItem.value}" />
		</h:outputLink>

		<%-- (13) Same window --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==13}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="_top" title="#{TableBean.dataItem.title}">
			<h:graphicImage styleClass="icon" height="50" value="#{TableBean.dataItem.value}" />
		</h:outputLink>
		
		<%-- (14) opens zoom viewer page in a new window --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==14}" 
					  value="#" styleClass="plaintext" target="_blank"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.dataItem.nameEncodedTitle}','#{TableBean.dataItem.windowParams}');return false">
			<h:graphicImage height="200" value="#{TableBean.dataItem.value}" />
		</h:outputLink>
                                     
		<%------------------------------------ Images (15, 16) --------------------------------------%> 
		<%-- (15) Same window --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==15}" 
					  value="#{TableBean.dataItem.link}" styleClass="plaintext" target="_top" title="#{TableBean.dataItem.title}">
			<h:graphicImage styleClass="icon" value="#{TableBean.dataItem.value}" />
		</h:outputLink>
		
		<%-- (16) always new window without controls--%>
		<h:outputLink rendered="#{TableBean.dataItemtype==16}" 
					  value="#" styleClass="plaintext" target="_blank"
					  onclick="window.open('#{TableBean.dataItem.link}','#{TableBean.dataItem.nameEncodedTitle}','#{TableBean.dataItem.windowParams}');return false">
			<h:graphicImage styleClass="icon" value="#{TableBean.dataItem.value}" />
		</h:outputLink>
						
		<%-- (19) Simple image without any link --%> 
		<h:graphicImage rendered="#{TableBean.dataItemtype==19}" 
						styleClass="icon" value="#{TableBean.dataItem.value}" width="#{TableBean.dataItem.width}" height="#{TableBean.dataItem.height}"  />

		<%------------------------------------- check box item (20) -----------------------------------------%>
		<h:column>
			<h:selectBooleanCheckbox  rendered="#{TableBean.dataItemtype==20}"
									  value="" />
		</h:column>

		<%-------------------------------------- Heatmap item (25) ------------------------------------------%>
		<h:column>
		<h:outputLink rendered="#{TableBean.dataItemtype==25}" 
					  value="javascript:showMicroarrayDetails('#{TableBean.tableView.name }','#{TableBean.component }','#{TableBean.dataItem.title }','#{TableBean.currentRow}')" styleClass="plaintext">
			<h:outputFormat rendered="#{TableBean.dataItemtype==25}"
						  value="&nbsp" 
						  title="#{TableBean.heatmapMouseOver}"
						  style="cursor:Crosshair;" escape="false"/>
		</h:outputLink>
		<%-- 
		<h:outputLink rendered="#{TableBean.dataItemtype==25}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('../includes/description.jsp?description=#{TableBean.dataItem.urlEncodedTitle}','#{TableBean.tableView.name}#{columnHeader.nameEncodedTitle}','#{TableBean.dataItem.windowParams}'); return false" >
			<h:outputFormat rendered="#{TableBean.dataItemtype==25}"
						  value="&nbsp" 
						  title="#{TableBean.heatmapMouseOver}"
						  style="cursor:Crosshair;" escape="false"/>
		</h:outputLink>
		--%>
		<%-- 
			<h:outputFormat rendered="#{TableBean.dataItemtype==25}"
						  value="&nbsp" 
						  title="#{TableBean.heatmapMouseOver}"
						  style="cursor:Crosshair;" escape="false"/>
		--%>						  
		</h:column>

<%--title="ProbeID:#{TableBean.dataItemValue} -- Component:#{TableBean.columnHeaderMouseOver} -- Value:#{TableBean.dataItem.title}"--%> 

		<%------------------------ link to samll description window (30,31,32) ------------------------------%> 
		<%-- new window for diferent columns --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==30}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('../includes/description.jsp?description=#{TableBean.dataItem.urlEncodedTitle}','#{TableBean.tableView.name}#{columnHeader.nameEncodedTitle}','#{TableBean.dataItem.windowParams}'); return false" >
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<%-- always new window --%>
		<h:outputLink rendered="#{TableBean.dataItemtype==31}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('../includes/description.jsp?description=#{TableBean.dataItem.urlEncodedTitle}','#{TableBean.tableView.name}#{TableBean.dataItem.nameEncodedValue}','#{TableBean.dataItem.windowParams}'); return false">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>
		<%-- always same new window for this table--%>
		<h:outputLink rendered="#{TableBean.dataItemtype==32}" 
					  value="#" styleClass="plaintext" target="_blank" title="#{TableBean.dataItem.title}"
					  onclick="window.open('../includes/description.jsp?description=#{TableBean.dataItem.urlEncodedTitle}','#{TableBean.tableView.name}','#{TableBean.dataItem.windowParams}'); return false">
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>

		<%------------------------------------- Javascript link (40) ----------------------------------------%>
		<h:outputLink rendered="#{TableBean.dataItemtype==40}"
					  value="" style="plainText" title="#{TableBean.dataItem.title}" onclick="#{TableBean.dataItem.link}; return false;" >
			<h:outputText value="#{TableBean.dataItemValue}" />
		</h:outputLink>

		<%------------------------------- Simple item-vermbatim text (50) -----------------------------------%>
		<h:outputText rendered="#{TableBean.dataItemtype==50}" 
					  value="#{TableBean.dataItemValue}" styleClass="plaintext" escape="false" />
					  
		<%------------------------------------ Simple item (no link) ----------------------------------------%>
		<h:outputText rendered="#{TableBean.dataItemtype==0}" 
					  value="#{TableBean.dataItemValue}" styleClass="plaintext" />

	</h:panelGroup>
