<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText value="Gene: #{StageFocusBrowseBean.gene}" rendered="#{StageFocusBrowseBean.gene != ''}" escape="false" />
	<h:form id="stageBrowseForm">
		<h:dataTable width="100%" styleClass="browseTable" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey" 
		             value="#{StageFocusBrowseBean.submissions}" var="submission">
			<h:column>
				<f:facet name="header">
					<h:outputText styleClass="plaintextbold" value="Stage"/>
				</f:facet>
				<h:outputLink value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/#{submission[4]}definition.html" styleClass="plaintext">
					<h:outputText value="#{submission[0]}"/>
				</h:outputLink>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText styleClass="plaintextbold" value="Alternate Staging" />
				</f:facet>
				<h:outputText styleClass="plaintext" value="#{submission[1]}" />
			</h:column>
			<h:column>
				<f:facet name="header">              
					<h:outputText styleClass="plaintextbold" value="Number of In situ Submissions" />
				</f:facet>
				<h:outputLink value="focus_insitu_browse.html?stage=#{submission[4]}&#{Visit.statusParam}" styleClass="plaintext" rendered="#{submission[2]!='0'}">  
					<h:outputText value="#{submission[2]}" />
				</h:outputLink>
				<h:outputText styleClass="plaintext" value="#{submission[2]}" rendered="#{submission[2]=='0'}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText styleClass="plaintextbold" value="Number of Microarray Submissions" />
				</f:facet>
				<h:outputLink value="focus_mic_browse.html?stage=#{submission[4]}&#{Visit.statusParam}" styleClass="plaintext" rendered="#{submission[3]!='0'}">  
					<h:outputText value="#{submission[3]}" />
				</h:outputLink>
				<h:outputText styleClass="plaintext" value="#{submission[3]}" rendered="#{submission[3]=='0'}"/>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText styleClass="plaintextbold" value="Number of Sequence Submissions" />
				</f:facet>
				<h:outputLink value="focus_ngd_browse.html?stage=#{submission[4]}&#{Visit.statusParam}" styleClass="plaintext" rendered="#{submission[5]!='0'}">  
					<h:outputText value="#{submission[5]}" />
				</h:outputLink>
				<h:outputText styleClass="plaintext" value="#{submission[5]}" rendered="#{submission[5]=='0'}"/>
			</h:column>
		</h:dataTable>
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
