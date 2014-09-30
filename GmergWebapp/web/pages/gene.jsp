<!-- Author: Mehran Sharghi																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
	<jsp:include page="/includes/header.jsp" />
<head>
<style>
.columnWidthgp1 {
    width:80px;
    text-align:left;
}
.columnWidthgp2 {
    width:30px;
    text-align:left;
}
.columnWidthgp3 {
    width:80px;
    text-align:left;
}
.columnWidthgp4 {
    width:180px;
    text-align:left;
}

</style>
</head>
<body>
	<h3>Gene Details</h3>
	<h:outputText value="There is no available data on the selected gene." styleClass="plaintext" rendered="#{GeneInfoBean.gene == null}" />
	<h:panelGrid columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene != null}">
		<h:outputText value="Symbol" />
		<h:outputText styleClass="plaintextbold" value="#{GeneInfoBean.gene.symbol}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Name" />
		<h:outputText styleClass="datatext" value="#{GeneInfoBean.gene.name}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Synonyms" />
		<h:outputText styleClass="datatext" value="#{GeneInfoBean.gene.synonyms}" />
		<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="Chromosome"/>
		<h:panelGrid columns="2" columnClasses="plaintext,datatext">
			<h:outputText value="Chromosome:" />
			<h:outputText value="#{GeneInfoBean.gene.xsomeName}" />
			<h:outputText value="Start:" />
			<h:outputText value="#{GeneInfoBean.gene.xsomeStart}" />
			<h:outputText value="End:" />
			<h:outputText value="#{GeneInfoBean.gene.xsomeEnd}" />
			<h:outputText value="Genome Build:" />
			<h:outputText value="#{GeneInfoBean.gene.genomeBuild}" />
		</h:panelGrid>
	</h:panelGrid>

	<f:verbatim>&nbsp;</f:verbatim>
	<f:subview id="geneStripTable" rendered="#{GeneInfoBean.gene!=null}"> 
		<h:outputText value="" rendered="#{GeneInfoBean.setTableViewNameToGeneStrip}" />
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview> 

	<h:panelGrid rendered="#{GeneInfoBean.gene != null && GeneInfoBean.gene.assocProbes != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene != null && GeneInfoBean.gene.assocProbes != null}">
		<h:outputText value="Probes Linked to Gene" />
		<h:dataTable value="#{GeneInfoBean.gene.assocProbes}" var="probe">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{probe[3]}" rendered="#{probe[3] != 'probe.html' && not empty probe[1]}">
					<h:outputText value="#{probe[1]}"/>
				</h:outputLink>
				<h:outputLink styleClass="datatext" value="#{probe[3]}" rendered="#{probe[3] == 'probe.html'}">
					<f:param name="probe" value="#{probe[1]}" />
					<f:param name="maprobe" value="#{probe[2]}" />
					<h:outputText value="#{probe[2]}"/>
				</h:outputLink>
			</h:column>
			<h:column  rendered="#{probe[3] != 'probe.html'}">
				<h:outputLink styleClass="datatext" value="probe.html">
					<f:param name="probe" value="#{probe[1]}" />
					<f:param name="maprobe" value="#{probe[2]}" />
					<h:outputText value="(#{probe[2]})"/>
				</h:outputLink>
			</h:column>			
		</h:dataTable>
	</h:panelGrid>

	<h:panelGrid rendered="#{GeneInfoBean.gene != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene != null}">
		<h:outputText value="Links" />
		<h:panelGrid columns="2" columnClasses="plaintext,datatext">
			<h:outputText value="MGI:"/>
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.mgiURL}" rendered="#{!GeneInfoBean.xenbaseEntryExists}" target="_blank">
				<h:outputText value="#{GeneInfoBean.gene.mgiAccID}" />
			</h:outputLink>

			<h:outputText value="Ensembl:" />
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.ensemblURL}" target="_blank">
				<h:outputText value="#{GeneInfoBean.gene.ensemblID}" />
			</h:outputLink>

			<h:outputText value="UCSC:" />
			<h:outputLink styleClass="plaintext" value="http://genome.ucsc.edu/cgi-bin/hgTracks?db=mm9&hubUrl=http%3A//www.gudmap.org/Gudmap/ngsData/gudmap_ucsc_hub/hub.txt&position=#{GeneInfoBean.gene.symbol}" target="_blank">
				<h:outputText value="View sequencing data for this gene in UCSC Genome Browser" />
			</h:outputLink>
			
			<h:outputText value="OMIM:" />
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.omimURL}" target="_blank">
				<h:outputText value="Search OMIM" />
			</h:outputLink>
			
			<h:outputText value="Entrez:" />
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.entrezURL}" target="_blank">
				<h:outputText rendered="#{GeneInfoBean.entrezIdExists}" value="#{GeneInfoBean.gene.entrezID}" />
				<h:outputText rendered="#{!GeneInfoBean.entrezIdExists}" value="Search entrez" />
			</h:outputLink>
			
			<h:outputText value="Genecards:"/>
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.geneCardURL}" target="_blank">
				<h:outputText value="Search GeneCards" />
			</h:outputLink>
			
			<h:outputText value="GO:" />
			<h:outputLink styleClass="plaintext" value="#{GeneInfoBean.gene.goURL}"  target="_blank">
				<h:outputText value="MGI GO Annotations" />
			</h:outputLink>
		</h:panelGrid>
	</h:panelGrid>

	<h:panelGrid rendered="#{GeneInfoBean.gene. iuphar_db_URL != null || GeneInfoBean.gene. iuphar_guide_URL != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene. iuphar_db_URL != null || GeneInfoBean.gene. iuphar_guide_URL != null}">
		<h:outputText value="Translational Links" />
		<h:panelGrid columns="2" columnClasses="plaintext,datatext">
			<h:outputText  rendered="#{GeneInfoBean.gene. iuphar_db_URL != null}" value="IUPHAR-DB:" />
			<h:outputLink  rendered="#{GeneInfoBean.gene. iuphar_db_URL != null}" styleClass="plaintext"  value="#{GeneInfoBean.gene. iuphar_db_URL}" target="_blank">
				<h:outputText value="View pharmacological information for #{GeneInfoBean.gene.symbol}" />
			</h:outputLink>
			<h:outputText  rendered="#{GeneInfoBean.gene. iuphar_guide_URL != null}" value="Guide to PHARMACOLOGY:" />
			<h:outputLink  rendered="#{GeneInfoBean.gene. iuphar_guide_URL != null}" styleClass="plaintext" value="#{GeneInfoBean.gene. iuphar_guide_URL}" target="_blank">
				<h:outputText value="View pharmacological information for #{GeneInfoBean.gene.symbol}" />
			</h:outputLink>
		</h:panelGrid>
	</h:panelGrid>

	<h:panelGrid rendered="#{GeneInfoBean.gene.ishSubmissions != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.ishSubmissions == null}">
		  <h:outputText value="ISH Data" />
		  <h:outputText value="no data" />
	</h:panelGrid>	
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.ishSubmissions != null}">
		        <h:outputText value="ISH Data" />
		        <h:dataTable cellspacing="5" value="#{GeneInfoBean.gene.ishSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
			</h:column>
			<h:column>
				<h:outputText value="#{sub[2]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[5]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[8]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[3]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] != 'probe.html'}">
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] == 'probe.html'}">
					<f:param name="probe" value="#{sub[6]}" />
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
			</h:column>
			<h:column>
				<h:outputLink styleClass="datatext" value="probe.html">
					<f:param name="probe" value="#{sub[6]}" />
					<f:param name="maprobe" value="#{sub[9]}" />
					<h:outputText value=" (#{sub[9]})" rendered="#{sub[9] != ''}"/>
				</h:outputLink>
			</h:column>
		</h:dataTable>
	</h:panelGrid>
	
	<h:panelGrid rendered="#{GeneInfoBean.gene.ihcSubmissions != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>

	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.ihcSubmissions == null}">
		 <h:outputText value="IHC Data" />
		 <h:outputText value="no data" />	
	</h:panelGrid>
	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.ihcSubmissions != null}">
		 <h:outputText value="IHC Data" />
		    <h:dataTable cellspacing="5" value="#{GeneInfoBean.gene.ihcSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
			</h:column>
			<h:column>
				<h:outputText value="#{sub[2]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[5]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[8]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[3]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] != 'probe.html'}">
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
				<h:outputLink styleClass="datatext" value="#{sub[10]}" rendered="#{sub[10] == 'probe.html'}">
					<f:param name="probe" value="#{sub[6]}" />
					<h:outputText value="#{sub[6]}" />
				</h:outputLink>				
			</h:column>
			<h:column>
				<h:outputLink styleClass="datatext" value="probe.html">
					<f:param name="probe" value="#{sub[6]}" />
					<f:param name="maprobe" value="#{sub[9]}" />
					<h:outputText value=" (#{sub[9]})" rendered="#{sub[9] != ''}"/>
				</h:outputLink>
			</h:column>
		</h:dataTable>
	</h:panelGrid>

	<h:panelGrid rendered="#{GeneInfoBean.gene.tgSubmissions != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>

	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.tgSubmissions == null}">
		        <h:outputText value="Transgenic Data" />
		        <h:outputText value="no data" />
	</h:panelGrid>

	<h:panelGrid columns="2" rowClasses="stripey" columnClasses="leftCol,rightCol" width="100%" rendered="#{GeneInfoBean.gene.tgSubmissions != null}">
		        <h:outputText value="Transgenic Data" />
		        <h:dataTable cellspacing="5" value="#{GeneInfoBean.gene.tgSubmissions}" var="sub">
			<h:column>
				<h:outputLink styleClass="datatext" value="#{sub[1]}">
					<f:param value="#{sub[0]}" name="id" />
					<h:outputText value="#{sub[0]}" />
				</h:outputLink>			
			</h:column>
			<h:column>
				<h:outputText value="#{sub[2]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[5]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[8]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[3]}" styleClass="datatext" />
			</h:column>
			<h:column>
				<h:outputText value="#{sub[11]}" styleClass="datatext" />
			</h:column>
		</h:dataTable>
	</h:panelGrid>
	
<!--
	<h:panelGrid rendered="#{GeneInfoBean.gene != null && GeneInfoBean.gene.numMicArrays != null}">
      	                <f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	<h:panelGrid columns="1" rowClasses="stripey" columnClasses="leftCol" width="100%" cellpadding="0" cellspacing="0"  rendered="#{GeneInfoBean.gene != null && GeneInfoBean.gene.numMicArrays != null}">
		<h:outputText value="Microarrays" />
	</h:panelGrid>
  	<f:subview id="GeneArrayTable" rendered="#{GeneInfoBean.gene != null && GeneInfoBean.gene.numMicArrays != null}">
                                <h:outputText value="" rendered="#{GeneInfoBean.setTableViewNameToArrayData}" />
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview> 
	<h:outputText value="" rendered="#{UtilityBean.setActiveTableViewName}" />
--> 

	<jsp:include page="/includes/footer.jsp" />
</body>    
</f:view>

