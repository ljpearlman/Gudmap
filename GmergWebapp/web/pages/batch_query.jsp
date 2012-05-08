<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
    
	<h3>Gene Symbol Batch Query</h3>
	<ul class="disc">
		<li class="datatext">
			Accepted data format: tab-delimited or newline-delimited csv file or plain text file, only accept query by gene symbol. 
        </li>
	</ul>

	<h:outputText value="<h3>Upload #{proj} collection from a text file</h3>" escape="false" />
	<ul class="disc">
		<li class="datatext">
			Accepted data format:
			<br>&nbsp; name = your collection name (required)
		    <br>&nbsp; type = "submissions" or "genes" (required)
			<br>&nbsp; description = a description for your collection (optional)
			<br>&nbsp; focus group = a focus group e.g "Metanephros" (optional, default is "none")	
			<br>&nbsp; status = "private" or "public" (optinal; default is "private")
			<br>&nbsp; <h:outputText value='a list of tab/semicolon delimitted #{proj} Ids or gene symbols. This should match the "type"' /> 
		</li>
	</ul>
	
	<h:outputText value="<h3>Upload #{proj} ID Collection</h3>" escape="false" />
	<ul class="disc">
		<li class="datatext">
			Accepted data format: tab-delimited or newline-delimited csv file or plain text file, only accept upload by a list of 
			<h:outputText value="#{proj} IDs." /> 
		</li>
	</ul>
	
    <jsp:include page="/includes/footer.jsp" />
</f:view>