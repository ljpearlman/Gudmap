<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form>	
		<h:commandLink value="View gene list in JavaTreeView" id="treeViewActionLink" action="#{GenelistBean.startTreeView}" >
			<f:param name="genelistId" value="#{GenelistBean.genelistId}" />
		</h:commandLink>
		<%-- ++++++++++++++++++++++++++++++ TreeView Applet +++++++++++++++++++++++++++++++++++++++++ --%>
		<h:panelGroup rendered="#{GenelistBean.displayTreeView}">
			<f:verbatim>
				<applet code="edu/stanford/genetics/treeview/applet/GudmapApplet.class" MAYSCRIPT archive="
			</f:verbatim>
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/TreeView.jar," escape="false" /> 
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/nanoxml-2.2.2.jar," escape="false" />
					<h:outputText value="#{facesContext.externalContext.requestContextPath}/analysis.jar\"" escape="false" />
					<h:outputText value="width='1' height='1'alt='Your browser is not running the visualisation applet! ' >" escape="false" />
					<h:outputText value="<param name='genelistId' value='#{GenelistBean.genelistId}'/>" escape="false" />
			<f:verbatim>
					<param name="sessionId" value="${pageContext.session.id}"/>
					<param name="dataSource" value="genelist"/>
				</applet>
			</f:verbatim>
		</h:panelGroup>
	</h:form>

	<h:outputText value="Gene List : " styleClass="plaintextbold" />
	<h:outputText value="#{GenelistBean.genelistName}" styleClass="plaintext" />
	<f:subview id="genelist">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<jsp:include page="/includes/footer.jsp" />
</f:view>

