<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText styleClass="plaintextbold" value="There are no series entries in the database matching the specified submission id" rendered="#{NGDSeriesBean.series == null}"/>
	<h:panelGrid columns="2" styleClass="header-stripey" columnClasses="leftCol,rightCol" cellpadding="3" cellspacing="3" rendered="#{NGDSeriesBean.series != null}">
		<h:outputText value="Series GEO ID:" styleClass="plaintext"   rendered="#{not empty NGDSeriesBean.series.geoID}"/>
		<h:outputLink value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc=#{NGDSeriesBean.series.geoID}" target="gmerg_external" styleClass="plaintext" rendered="#{not empty NGDSeriesBean.series.geoID}">
			<h:outputText value="#{NGDSeriesBean.series.geoID}" rendered="#{not empty NGDSeriesBean.series.geoID}"/>
		</h:outputLink>
		
		<h:outputText value="Archive ID:" styleClass="plaintext"     rendered="#{NGDSeriesBean.series.archiveId > 0}"/>
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{NGDSeriesBean.series.archiveId}" styleClass="plaintext" rendered="#{NGDSeriesBean.series.archiveId > 0}">
			<h:outputText value="#{NGDSeriesBean.series.archiveId}"    rendered="#{NGDSeriesBean.series.archiveId > 0}"/>
		</h:outputLink>
		<h:outputText value="Batch ID:" styleClass="plaintext"  rendered="#{NGDSeriesBean.series.batchId > 0}"/>
		<h:outputLink value="/gudmap/pages/focus_ngd_browse.html?batchId=#{NGDSeriesBean.series.batchId}" styleClass="plaintext" rendered="#{NGDSeriesBean.series.batchId > 0}">
			<h:outputText value="#{NGDSeriesBean.series.batchId}"  rendered="#{NGDSeriesBean.series.batchId > 0}"/>
		</h:outputLink>

		<%-- <h:outputText value="Archive/Batch ID:" styleClass="plaintext" />
		<h:panelGrid columns="3" >
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{NGDSeriesBean.series.archiveId}" styleClass="plaintext" rendered="#{NGDSeriesBean.series.archiveId > 0}">
			<h:outputText value="#{NGDSeriesBean.series.archiveId}"    rendered="#{NGDSeriesBean.series.archiveId > 0}"/>
		</h:outputLink>
		<h:outputText value="/"/>
		<h:outputLink value="/gudmap/pages/focus_ngd_browse.html?batchId=#{NGDSeriesBean.series.batchId}" styleClass="plaintext" rendered="#{NGDSeriesBean.series.batchId > 0}">
			<h:outputText value="#{NGDSeriesBean.series.batchId}"  rendered="#{NGDSeriesBean.series.batchId > 0}"/>
		</h:outputLink>
		</h:panelGrid> --%>

		<h:outputText styleClass="plaintext" value="Number of Samples:" />
		<h:outputText styleClass="datatext" value="#{NGDSeriesBean.series.numSamples} samples" />
	
		<h:outputText styleClass="plaintext" value="Title:" />
		<h:outputText styleClass="datatext" value="#{NGDSeriesBean.series.title}" />
	
		<h:outputText styleClass="plaintext" value="Summary:" />
		<h:outputText styleClass="datatext" value="#{NGDSeriesBean.series.summary}" />
	
		<%-- <h:outputText styleClass="plaintext" value="Type:" />
		<h:outputText styleClass="datatext" value="#{NGDSeriesBean.series.type}" /> --%>
	
		<h:outputText styleClass="plaintext" value="Overall Design:" />
		<h:outputText styleClass="datatext" value="#{NGDSeriesBean.series.design}" />
	</h:panelGrid>

	<h:outputText value="<br/>" escape="false" />

	<f:subview id="SeriesSamplesTable" rendered="#{NGDSeriesBean.series != null}">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<jsp:include page="/includes/footer.jsp" />
</f:view>