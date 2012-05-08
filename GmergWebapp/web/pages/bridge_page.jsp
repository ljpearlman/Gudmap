<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<%@ taglib prefix="rich" uri="http://richfaces.ajax4jsf.org/rich" prefix="rich"%>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

  
<h:form id="mainForm">
   
	<h:panelGrid columns="3" cellpadding="2" cellspacing="2">
		<h:outputText styleClass="plaintextbold" value="Search across GUDMAP database" />
		<h:inputText id="globalSearchInput" value="#{GlobalSearchBean.globalSearchInput}" size="60" />
		<h:commandLink id="googleSearchButton" action="#{GlobalSearchBean.search}" styleClass="quick_search" style="width:70" >
			<h:graphicImage value="/images/search.png" title="Click to search" alt="search" styleClass="icon" style="border:0" />
			<f:param name="input" value="#{GlobalSearchBean.globalSearchInput}" />
		</h:commandLink>
	</h:panelGrid>	


	<f:verbatim>&nbsp;</f:verbatim>
	<f:verbatim>&nbsp;</f:verbatim>
	
	<h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe">
		<h:outputText styleClass="plaintextbold" value="Results for: #{BridgePageBean.input}" />
		<f:verbatim>&nbsp;</f:verbatim>
	</h:panelGrid>
	
	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.geneTitle}">
		<rich:tree id="geneTree" ajaxSubmitSelection="true" switchType="client" value="#{BridgePageBean.rootGeneNode}" 
			var="geneNode" nodeSelectListener="#{BridgePageBean.processTreeNodeImplSelection}">
			<rich:treeNode type="gene">
		        <h:outputText value="#{geneNode}"/>
		    </rich:treeNode>
		</rich:tree>
	</rich:simpleTogglePanel>   

	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.probeTitle}">
		<rich:tree id="probeTree" ajaxSubmitSelection="true" switchType="client" value="#{BridgePageBean.rootProbeNode}" 
			var="probeNode" nodeSelectListener="#{BridgePageBean.processTreeNodeImplSelection}">
			<rich:treeNode type="probe">
		        <h:outputText value="#{probeNode}"/>
		    </rich:treeNode>
		</rich:tree> 
	
	</rich:simpleTogglePanel>   
	
	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.insituTitle}">
		<rich:tree id="insituTree" ajaxSubmitSelection="true" switchType="client" value="#{BridgePageBean.rootInsituNode}" 
			var="insituNode" nodeSelectListener="#{BridgePageBean.processTreeNodeImplSelection}">
			<rich:treeNode type="insitu">
		        <h:outputText value="#{insituNode}"/>
		    </rich:treeNode>
		</rich:tree> 
	</rich:simpleTogglePanel> 

	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.microarrayTitle}">
		<rich:tree id="microarrayTree" ajaxSubmitSelection="true" switchType="client" value="#{BridgePageBean.rootMicroarrayNode}" 
			var="microarrayNode" nodeSelectListener="#{BridgePageBean.processTreeNodeImplSelection}">
			<rich:treeNode type="microarray" iconLeaf="..." icon="..."> 
		        <h:outputText value="#{microarrayNode}"/>
		    </rich:treeNode>
		</rich:tree>	
	</rich:simpleTogglePanel>   

	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.gudmapTitle}">
		<rich:tree id="gudmapTree" ajaxSubmitSelection="true" switchType="client" value="#{BridgePageBean.rootGudmapNode}" 
			var="gudmapNode" nodeSelectListener="#{BridgePageBean.processTreeNodeImplSelection}">
			<rich:treeNode type="gudmap" iconLeaf="..." icon="...">
		        <h:outputText value="#{gudmapNode}"/>
		    </rich:treeNode>
		</rich:tree>	 
	</rich:simpleTogglePanel> 

	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.tutorialTitle}">
	</rich:simpleTogglePanel>   

	<rich:simpleTogglePanel width="450px" opened="false" switchType="client" label="#{BridgePageBean.anatomyTitle}">
	</rich:simpleTogglePanel>   
	
	<f:verbatim>&nbsp;</f:verbatim>
	<f:verbatim>&nbsp;</f:verbatim>

	

	
</h:form>
					
	<jsp:include page="/includes/footer.jsp" />
</f:view>