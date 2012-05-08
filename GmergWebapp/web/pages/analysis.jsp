<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
	<h:outputLink value="array_analysis.html">
		<h:outputText value="Microarray Visualisation" />
	</h:outputLink>
	<br>
	<h:outputLink value="mic_focus.html">
		<h:outputText value="Microarray Tissue Analysis" />
	</h:outputLink>
	<br>
	<h:outputLink value="all_components_genelists.html">
		<h:outputText value="Microarray Components Genelists" />
	</h:outputLink>
	<jsp:include page="/includes/footer.jsp" />
</f:view>