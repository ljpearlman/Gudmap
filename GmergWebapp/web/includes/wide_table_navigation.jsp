<!doctype html public "-//w3c//dtd html 4.01 transitional//en">
<!-- Navigation panel implemented in a frame for GenericTable navigation				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<head>
  <link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">

	<style>
		.firstCol{
			text-align: left;
			width: 28%;
		}
		.secondCol{
			text-align: center;
			width: 44%;
		}
		.thirdCol{
		  text-align:right;
			align: right;
			width: 20%;
		}
		.forthCol{
			text-align: left;
			width: 8%;
		}
	</style>
	
	<script language="JavaScript">
 		function pageNumSubmition()
	  {
		  document.getElementById("tableNavigationForm:gotoPage").onclick();
    	return false;
		}	
	</script>

</head>

<body>

<f:view>
	<h:form id="tableNavigationForm" target="_parent" onsubmit="return pageNumSubmition()">
		<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
		<h:panelGrid border="0" width="100%" styleClass="stripey" cellpadding="2" cellspacing="2" 
	  				 columns="4" columnClasses="firstCol, secondCol, thirdCol, forthCol">

			<h:outputText value="Page #{TableBean.tableView.currentPage} of #{TableBean.tableView.numPages}" styleClass="plaintext"/>

		    <h:panelGroup>
				<h:commandLink id="first" action="#{TableBean.tableNavigation}" styleClass="nav3" >
					<h:graphicImage value="../images/first.png"  title="First Page" alt="first" styleClass="icon" />
					<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
					<f:param name="tableSelectable" value="#{TableBean.tableView.selectable}" />
					<f:param name="tableNumPages" value="#{TableBean.tableView.numPages}" />
					<f:param name="operation" value="firstPage" />
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="prev" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/previous.png"  title="Previous Page" alt="prev" styleClass="icon" />
					<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
					<f:param name="tableSelectable" value="#{TableBean.tableView.selectable}" />
					<f:param name="tableNumPages" value="#{TableBean.tableView.numPages}" />
					<f:param name="operation" value="prevPage" />
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="next" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/next.png"  title="Next Page" alt="next" styleClass="icon" />
					<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
					<f:param name="tableSelectable" value="#{TableBean.tableView.selectable}" />
					<f:param name="tableNumPages" value="#{TableBean.tableView.numPages}" />
					<f:param name="operation" value="nextPage" />
				</h:commandLink>
				<f:verbatim>&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandLink id="last" action="#{TableBean.tableNavigation}" >
					<h:graphicImage value="../images/last.png"  title="Last Page" alt="last" styleClass="icon" />
					<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
					<f:param name="tableSelectable" value="#{TableBean.tableView.selectable}" />
					<f:param name="tableNumPages" value="#{TableBean.tableView.numPages}" />
					<f:param name="operation" value="lastPage" />
				</h:commandLink>
			</h:panelGroup>
				
			<h:panelGroup>
				<h:outputText value="Go to page:"  styleClass="plaintext"/>
				<f:verbatim>&nbsp;</f:verbatim>
				<h:inputText id="pageNum" value="#{TableBean.pageNum}" size="3" style="text-align:center" />
			</h:panelGroup>
				
			<h:commandLink id="gotoPage" action="#{TableBean.tableNavigation}" >
				<h:graphicImage value="../images/gu_go.gif"  title="Next Page" alt="next" styleClass="icon" />
				<f:param name="tableViewName" value="#{TableBean.tableView.name}" />
				<f:param name="tableSelectable" value="#{TableBean.tableView.selectable}" />
				<f:param name="tableNumPages" value="#{TableBean.tableView.numPages}" />
				<f:param name="operation" value="gotoPage" />
			</h:commandLink>
				
		</h:panelGrid>
	</h:form>
</f:view>

</body>

