<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText styleClass="plaintextbold" value="There are no series entries in the database matching the specified submission id" rendered="#{SeriesBean.series == null}"/>
	<h:panelGrid columns="2" styleClass="header-stripey" columnClasses="leftCol,rightCol" cellpadding="3" cellspacing="3" rendered="#{SeriesBean.series != null}">
		<h:outputText value="Series GEO ID:" styleClass="plaintext" />
		<h:outputLink value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc=#{SeriesBean.series.geoID}" target="gmerg_external" styleClass="plaintext">
			<h:outputText value="#{SeriesBean.series.geoID}" />
		</h:outputLink>

		<h:outputText value="Archive/Batch ID:" styleClass="plaintext" />
		<%-- <h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{SeriesBean.series.archiveId}" styleClass="plaintext">
			<h:outputText value="#{SeriesBean.series.archiveId}" />
		</h:outputLink> --%>
		<h:panelGrid columns="3">
				<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{SeriesBean.series.archiveId}" styleClass="plaintext" rendered="#{SeriesBean.series.archiveId > 0}">
					<h:outputText value="#{SeriesBean.series.archiveId}"  rendered="#{SeriesBean.series.archiveId > 0}"/>
				</h:outputLink>
				<h:outputText value="/"/>
				<h:outputLink value="/gudmap/pages/focus_mic_browse.html?batchId=#{SeriesBean.series.batchId}" styleClass="plaintext" rendered="#{SeriesBean.series.batchId > 0}">
					<h:outputText value="#{SeriesBean.series.batchId}"  rendered="#{SeriesBean.series.batchId > 0}"/>
				</h:outputLink>
			</h:panelGrid>

		<h:outputText styleClass="plaintext" value="Number of Samples:" />
		<h:outputText styleClass="datatext" value="#{SeriesBean.series.numSamples} samples" />
	
		<h:outputText styleClass="plaintext" value="Title:" />
		<h:outputText styleClass="datatext" value="#{SeriesBean.series.title}" />
	
		<h:outputText styleClass="plaintext" value="Summary:" />
		<h:outputText styleClass="datatext" value="#{SeriesBean.series.summary}" />
	
		<h:outputText styleClass="plaintext" value="Type:" />
		<h:outputText styleClass="datatext" value="#{SeriesBean.series.type}" />
	
		<h:outputText styleClass="plaintext" value="Overall Design:" />
		<h:outputText styleClass="datatext" value="#{SeriesBean.series.design}" />
	</h:panelGrid>

	<h:outputText value="<br/>" escape="false" />

	<f:subview id="SeriesSamplesTable" rendered="#{SeriesBean.series != null}">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<jsp:include page="/includes/footer.jsp" />
</f:view>