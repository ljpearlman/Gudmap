<!-- Author: Mehran Sharghi							 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>


<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form id="componentGenelistForm" >	
		<h:commandLink id="treeviewLink" action="#{AllComponentsGenelistsBean.launchTreeView}" >
			<f:param name="genelistId" value="0" />
		</h:commandLink>
		<%-- ++++++++++++++++++++++++++++++ TreeView Applet +++++++++++++++++++++++++++++++++++++++++ --%>
		<h:panelGroup rendered="#{AllComponentsGenelistsBean.displayTreeView}">
			<f:verbatim>
				<applet code="edu/stanford/genetics/treeview/applet/GudmapApplet.class" MAYSCRIPT archive="
			</f:verbatim>
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/TreeView.jar," escape="false" /> 
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/nanoxml-2.2.2.jar," escape="false" />
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/analysis.jar\"" escape="false" />
					<h:outputText value="width='1' height='1'alt='Your browser is not running the visualisation applet! ' >" escape="false" />
					<h:outputText value="<param name='genelistId' value='#{AllComponentsGenelistsBean.genelistId}'/>" escape="false" />
					<h:outputText value="<param name='cdtFile' value='#{AllComponentsGenelistsBean.cdtFile}'/>" escape="false" />
					<h:outputText value="<param name='gtrFile' value='#{AllComponentsGenelistsBean.gtrFile}'/>" escape="false" />
			<f:verbatim>
					<param name="sessionId" value="${pageContext.session.id}"/>
					<param name="dataSource" value="genelist"/>
				</applet>
			</f:verbatim>
		</h:panelGroup>
	</h:form>

	<h:outputText value="List of component based gene lists : " styleClass="plaintextbold" />
	<h:outputText value="" rendered="#{AllComponentsGenelistsBean.dumy}" />
	<f:subview id="allComponentsGenelists">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>


