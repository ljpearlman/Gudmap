<!-- Microarray analysis & Visualisation jsp interface				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<head>
	<style>
		.centreAlign{
			text-align: center;
		}
		.leftAlign27{
			text-align: left;
			width:27%;
		}
		.bottomAlign{
			vertical-align: bottom;
		}
		.message{
		  color: red; 
		  font-size: 11px; 
		  text-align: left;
		}
	</style>
	
	<script type="text/javascript">
	
		/* AJAX Dynamic progress-indicator stuff */
		function waitingSubmitForm(dest) { 
			document.getElementById("analysisClusteringForm:clusterWaiting").value = true;
			try {
				document.getElementById("progress").style.visibility="visible"; 
				setTimeout('document.images["spinner"].src = "../images/spinner.gif"', 200); 
				xmlhttp = window.XMLHttpRequest?new XMLHttpRequest(): new ActiveXObject("Microsoft.XMLHTTP"); 
			} 
			catch (e) { 
				// browser doesn't support ajax. handle however you want  
			} 
//     xmlhttp.onreadystatechange = triggered;   
			xmlhttp.open("GET", dest); 
			xmlhttp.send(null);
			return false;
		} 

    function checkClusterWaiting() {
      if (document.getElementById("analysisClusteringForm:clusterWaiting").value == "true"){
				document.getElementById("progress").style.visibility="visible"; 
				setTimeout('document.images["spinner"].src = "../images/spinner.gif"', 200); 
			}
			else{
			  if (document.getElementById("progress").style.visibility=="visible") {
					document.getElementById("progress").style.visibility="hidden"; 
					setTimeout('document.innerHTML = xmlhttp.responseText',100); 
				}
			}
    }
    
    function appletInitialised() {
				document.getElementById("progress").style.visibility="hidden"; 
    	  document.getElementById("analysisClusteringForm:clusterWaiting").value = "false";
    }
/*		
		function triggered() {
			if ((xmlhttp.readyState == 4) && (xmlhttp.status == 200)) {
				document.getElementById("progress").style.visibility="hidden"; 
				setTimeout('document.innerHTML = xmlhttp.responseText',100); 
			} 
		}
*/

   var okToSubmitCluster = true;

  function validateCluster(geneNo, geneNumLimit)
  {
    var clusterValid = true;       
    if(parseInt(geneNo) > parseInt(geneNumLimit))
      clusterValid = confirm('Number of genes is exceeding the limit. You can press "Cancel" and apply more restrictive filters or press "Ok" to visualise the first ' + geneNumLimit + ' genes.');
    if (clusterValid) {
      okToSubmitCluster = true;
			return waitingSubmitForm(document.getElementById("analysisClusteringForm") );
		}
			
		okToSubmitCluster = false;
    return false;
  }
            
</script>	
	
</head>

<body onload="checkClusterWaiting()">

<f:view>
	<jsp:include page="/includes/header.jsp" />
	<f:verbatim>
		<div id="progress" style="visibility:hidden">
			Clustering data, please wait ...
			</p>
		  <img id="spinner" src="../images/spinner.gif" height="20" align="MIDDLE"/>
		</div>
	</f:verbatim>
	<h:form id="analysisDataSelectionForm" >

		<h:panelGrid columns="2" styleClass="stripey" cellpadding="5" width="100%" columnClasses="align-left, align-right" >
			<h:outputText value="Data Selection:" styleClass="plaintextbold" />
			<h:outputText value="" />
			<h:panelGroup>
				<h:outputLabel for="seriesId" value="Series ID (GEO accession): " />
				<h:selectOneMenu id="seriesId" value="#{AnalysisBean.seriesGeoId}" required="true" binding="#{AnalysisBean.seriesIdInput}" immediate="true">
					<f:selectItems value="#{AnalysisBean.seriesGeoIds}" />
				</h:selectOneMenu>
				<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				<h:commandButton action="#{AnalysisBean.getSamplesWaiting}" value="Get Samples" immediate="true"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:commandButton action="#{AnalysisBean.clearAll}" value="Clear All" rendered="#{AnalysisBean.samplesLoaded}" immediate="true"/>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
			</h:panelGroup>	
		</h:panelGrid>

		<h:panelGrid columns="5" rendered="#{AnalysisBean.samplesLoaded}" styleClass="stripey" cellpadding="5" width="100%" 
																				 columnClasses="leftAlign27, align-left, centreAlign, centreAlign, centreAlign">
			<h:outputText value="Number of samples:" styleClass="plaintextbold" />
			<h:outputText value="#{AnalysisBean.numSamples}" />
			<h:outputText value="" />
			<h:outputText value="" />
			<h:outputText value="" />

			<h:outputText value=" Number of genes per sample:" styleClass="plaintextbold" />
			<h:outputText value="#{AnalysisBean.numGenes}" />
			<h:outputText value="" />
			<h:outputText value="" />
			<h:outputText value="" />

			<h:outputText value="" />
			<h:outputText value="" />
			<h:outputText value="Minimum" styleClass="plaintextbold" />
			<h:outputText value="Maximum" styleClass="plaintextbold" />
			<h:outputText value="Average" styleClass="plaintextbold" />

			<h:outputText value="Signal value:" styleClass="plaintextbold" />
			<h:outputText value="" />
			<h:outputText value="#{AnalysisBean.minSignal}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>
			<h:outputText value="#{AnalysisBean.maxSignal}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>
			<h:outputText value="#{AnalysisBean.avgSignal}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>

			<h:outputText value="Standard deviation:" styleClass="plaintextbold" />
			<h:outputText value="" />
			<h:outputText value="#{AnalysisBean.minStdDev}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>
			<h:outputText value="#{AnalysisBean.maxStdDev}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>
			<h:outputText value="#{AnalysisBean.avgStdDev}">
				<f:convertNumber pattern="####0.00" />
			</h:outputText>
		</h:panelGrid>
	</h:form>
	<br>
	<!-- ++++++++++++++++++++++++++ Filtering +++++++++++++++++++++++++++++++++++++++++++++ -->
	<h:form id="analysisFilteringForm">
		<h:panelGrid id="filteringPanel" columns="2" styleClass="stripey" cellpadding="5" width="100%" columnClasses="align-left, centreAlign">
			<h:outputText value="Filtering:" styleClass="plaintextbold" />
			<h:outputText value="" />

			<h:outputText value="" />
			<h:outputText value="Number passed" styleClass="plaintextbold" />

			<!-- -------------------------------------------------------------------------------- -->

			<h:panelGroup>
				<h:selectBooleanCheckbox id="presenceFilter" value="#{AnalysisBean.presenceFilter}" 
                                 valueChangeListener="#{AnalysisBean.filterListener}" onclick="submit()"/>
        <h:outputText value=" Filter genes that are <em>not present</em> in at least " escape="false"/>
				<h:selectOneMenu id="presenceChoises" value="#{AnalysisBean.presence}" required="true" >
					<f:selectItems value="#{AnalysisBean.presenceChoises}" />
				</h:selectOneMenu>
				<h:outputText value=" of samples" />
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{AnalysisBean.presenceFilterPass}" rendered="#{AnalysisBean.presenceFilterPass>=0}"/>
				<h:outputText value="-" rendered="#{AnalysisBean.presenceFilterPass<0}"/>
			</h:panelGroup>

			<h:outputText value="" rendered="#{AnalysisBean.samplesLoaded}" />
			<h:outputText value="" rendered="#{AnalysisBean.samplesLoaded}" />

			<!-- -------------------------------------------------------------------------------- -->
			<h:panelGroup>
				<h:selectBooleanCheckbox id="stdDevFilter" value="#{AnalysisBean.stdDevFilter}" 
                                 valueChangeListener="#{AnalysisBean.filterListener}" onclick="submit()"/>
        <h:outputText value=" Filter genes with <em>standard deviation</em> less than " escape="false"/>
				<h:inputText id="stdDev" value="#{AnalysisBean.stdDev}" binding="#{AnalysisBean.stdDevInput}" required="true" size="7" >
				  <f:validateDoubleRange />
				</h:inputText>  
				<h:outputText value=" across the samples" />
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{AnalysisBean.stdDevFilterPass}" rendered="#{AnalysisBean.stdDevFilterPass>=0}" />
				<h:outputText value="-" rendered="#{AnalysisBean.stdDevFilterPass<0}" />
			</h:panelGroup>

		  <h:message id="stdDevMessage" for="stdDev" styleClass="message" 
		             rendered="#{AnalysisBean.samplesLoaded && AnalysisBean.stdDevFilter}" />
			<h:outputText value="" rendered="#{AnalysisBean.samplesLoaded && AnalysisBean.stdDevFilter}" />
			
			<!-- -------------------------------------------------------------------------------- -->
			<h:panelGroup>
				<h:selectBooleanCheckbox id="signalFilter" value="#{AnalysisBean.signalFilter}" 
                                 valueChangeListener="#{AnalysisBean.filterListener}" onclick="submit()"/>
        <h:outputText value=" Filter genes that have <em>signal value</em> above " escape="false"/>
				<h:inputText id="signalThreshold" value="#{AnalysisBean.signalThreshold}" 
				             binding="#{AnalysisBean.signalThresholdInput}" required="true" size="7" >
				  <f:validateDoubleRange />
				</h:inputText>  
				<f:verbatim>&nbsp; in at least &nbsp;</f:verbatim>
				<h:selectOneMenu id="signalPresenceChoises" value="#{AnalysisBean.signalPresence}" required="true" >
					<f:selectItems value="#{AnalysisBean.signalPresenceChoises}" />
				</h:selectOneMenu>
				<h:outputText value=" of samples" />
			</h:panelGroup>

			<h:panelGroup>
				<h:outputText value="#{AnalysisBean.signalFilterPass}" rendered="#{AnalysisBean.signalFilterPass>=0}" />
				<h:outputText value="-" rendered="#{AnalysisBean.signalFilterPass<0}" />
			</h:panelGroup>

		  <h:message id="signalMessage" for="signalThreshold" styleClass="message" 
		             rendered="#{AnalysisBean.samplesLoaded && AnalysisBean.signalFilter}" />
			<h:outputText value="" rendered="#{AnalysisBean.samplesLoaded && AnalysisBean.signalFilter}" />

			<!-- -------------------------------------------------------------------------------- -->
  		<h:panelGrid columnClasses="align-right" width="100%" >
	  		<h:outputText styleClass="plaintextbold" value="Total number of genes passed:" />
  		</h:panelGrid>  
			<h:panelGroup>
				<h:outputText value="#{AnalysisBean.totalFilterPass}" rendered="#{AnalysisBean.totalFilterPass>=0}"/>
				<h:outputText value="-" rendered="#{AnalysisBean.totalFilterPass<0}"/>
			</h:panelGroup>

			<h:panelGroup>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
				<h:commandButton id="filter" action="#{AnalysisBean.filter}" value="Filter" immediate="true"/>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
 			  <h:message id="filterMessage" for="filter" styleClass="message" />
			</h:panelGroup>
		</h:panelGrid>
	</h:form>
	<br>
	<!-- ++++++++++++++++++++++++++++++ Clustering +++++++++++++++++++++++++++++++++++++++++ -->
	<h:form id="analysisClusteringForm" onsubmit="return okToSubmitCluster;">
		<h:panelGrid id="clusteringPanel" columns="2" styleClass="stripey" cellpadding="5" width="100%" >
			<h:outputText value="Hierarchical Clustering:" styleClass="plaintextbold" />
			<h:outputText value="" />

			<h:outputText value="" />
			<h:outputText value="" />

			<h:outputText value="&nbsp;&nbsp;Clustering Method: &nbsp;" escape="false" />
			<h:selectOneMenu id="clusteringMethodChoises" value="#{AnalysisBean.clusteringMethod}" required="true" >
				<f:selectItems value="#{AnalysisBean.clusteringMethodChoises}" />
			</h:selectOneMenu>

			<h:outputText value="&nbsp;&nbsp;Distance Measure: &nbsp;" escape="false" />
			<h:selectOneMenu id="distanceChoises" value="#{AnalysisBean.distanceMeasure}" required="true" >
				<f:selectItems value="#{AnalysisBean.distanceMeasureChoises}" />
			</h:selectOneMenu>
		</h:panelGrid>

		<h:panelGrid columns="1" styleClass="stripey" cellpadding="5" width="100%" >
 			<h:panelGroup>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
	 			<h:panelGroup id="cluster">
					<h:commandButton action="#{AnalysisBean.cluster}" value="Cluster" rendered="#{not AnalysisBean.samplesLoaded}" />
					<h:commandButton action="#{AnalysisBean.cluster}" value="Cluster" rendered="#{AnalysisBean.samplesLoaded}" onclick="validateCluster('#{AnalysisBean.totalFilterPass}', '#{AnalysisBean.geneNumLimit}')" />
				</h:panelGroup>
				<f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
		  	<h:message id="clusterMessage" for="cluster" styleClass="message"/>
			</h:panelGroup>
		</h:panelGrid>
		
	  <h:inputHidden id="clusterWaiting" value="#{AnalysisBean.clusterWaiting}" />
	  
	</h:form>

	<!-- ++++++++++++++++++++++++++++++ Applet +++++++++++++++++++++++++++++++++++++++++ -->
	<h:panelGroup rendered="#{AnalysisBean.visualiseResult}">
		<f:verbatim>
			<applet code="edu/stanford/genetics/treeview/applet/GudmapApplet.class" MAYSCRIPT archive="
		</f:verbatim>
			<h:outputText value="#{facesContext.externalContext.requestContextPath}/TreeView.jar," /> 
			<h:outputText value="#{facesContext.externalContext.requestContextPath}/nanoxml-2.2.2.jar," />
			<h:outputText value="#{facesContext.externalContext.requestContextPath}/analysis.jar\" " />
		<f:verbatim>
			 width="1" height="1"
			alt="Your browser is not running the visualisation applet! "  >
			<param name="sessionId" value="${pageContext.session.id}"/>
			<param name="dataSource" value="series"/>
			</applet>
		</f:verbatim>
	</h:panelGroup>

    <f:subview id="footer">
      <jsp:include page="/includes/footer.jsp" />	
    </f:subview>
</f:view>
</body>
