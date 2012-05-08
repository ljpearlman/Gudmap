<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText styleClass="plaintextbold" value="There are no series entries in the database matching the specified submission id" rendered="#{seriesBean.series == null}"/>
	<h:panelGrid columns="2" styleClass="header-stripey" columnClasses="leftCol,rightCol" cellpadding="3" cellspacing="3" rendered="#{seriesBean.series != null}">
		<h:outputText value="Series GEO ID:" styleClass="plaintext" />
		<h:outputLink value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc=#{seriesBean.series.geoID}" target="gmerg_external" styleClass="plaintext">
			<h:outputText value="#{seriesBean.series.geoID}" />
		</h:outputLink>

		<h:outputText value="Archive ID:" styleClass="plaintext" />
		<h:outputLink value="http://www.gudmap.org/Submission_Archive/index.html##{seriesBean.series.archiveId}" styleClass="plaintext">
			<h:outputText value="#{seriesBean.series.archiveId}" />
		</h:outputLink>

		<h:outputText styleClass="plaintext" value="Number of Samples:" />
		<h:outputText styleClass="datatext" value="#{seriesBean.series.numSamples} samples" />
	
		<h:outputText styleClass="plaintext" value="Title:" />
		<h:outputText styleClass="datatext" value="#{seriesBean.series.title}" />
	
		<h:outputText styleClass="plaintext" value="Summary:" />
		<h:outputText styleClass="datatext" value="#{seriesBean.series.summary}" />
	
		<h:outputText styleClass="plaintext" value="Type:" />
		<h:outputText styleClass="datatext" value="#{seriesBean.series.type}" />
	
		<h:outputText styleClass="plaintext" value="Overall Design:" />
		<h:outputText styleClass="datatext" value="#{seriesBean.series.design}" />
	</h:panelGrid>

	<h:outputText value="<br/>" escape="false" />

	<f:subview id="SeriesSamplesTable" rendered="#{seriesBean.series != null}">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<jsp:include page="/includes/footer.jsp" />
</f:view>