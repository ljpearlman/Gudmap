<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
  <html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
      <title>Gene List</title>
      <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
    <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
    <style type="text/css">
    @import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");
    </style>

    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/formfunctions.js" type="text/javascript"></script>
    <script type="text/javascript">
      function selAll() {
        if (document.getElementById)
	{
	  x = document.getElementById('browseForm');	
	}
	else if (document.all)
	{
	  x = document.all['browseForm'];
	}
	else if (document.layers)
	{
	  x = document.layers['browseForm'];
	}
	for (var i = 0; i < x.elements.length; i++) {
	  if(x.elements[i].type == 'checkbox'){
	    x.elements[i].checked =true;
	  }
	}
      }
		
      function deSelAll() {
	if (document.getElementById)
	{
	  x = document.getElementById('browseForm');	
	}
	else if (document.all)
	{
	  x = document.all['browseForm'];
	}
	else if (document.layers)
	{
	  x = document.layers['browseForm'];
	}
	for (var i = 0; i < x.elements.length; i++) {
	  if(x.elements[i].type == 'checkbox'){
	    x.elements[i].checked =false;
	  }
	}
      }
    </script>
    </head>
    <body>
    <table border="0" width="600" class="header-stripey" cellpadding="2" cellspacing="2">
      <tr>
        <td>
          <h:panelGrid columns="2" width="100%" cellpadding="0" cellspacing="0" border="0" columnClasses="leftAlign,rightAlign">
            <h:panelGroup>
              <h:outputLink styleClass="nav3" onclick="selAll()"  title="Select all entries on current page" value="#">
              <h:outputText value="Select All" />
              </h:outputLink>
              <h:outputText value=" | " />
              <h:outputLink styleClass="nav3" onclick="deSelAll()"  title="Deselect all entries on current page" value="#">
              <h:outputText value="Deselect All" />
              </h:outputLink>
            </h:panelGroup>
            <h:form>
              <h:panelGrid columns="3">
              <h:outputText styleClass="nav3" value="Find a specific gene in the list: " />
              <h:inputText value="#{MicroarraySingleSubmissionBean.geneSymbol}" />
              <h:commandButton actionListener="#{MicroarraySingleSubmissionBean.findPageNumberForGeneSymbol}" image="../images/gu_go.gif" />
              </h:panelGrid>
            </h:form>
          </h:panelGrid>
        </td>
      </tr>
      <tr>
      <td>
        <h:panelGrid columns="3" styleClass="header-stripey" cellpadding="2" cellspacing="0" width="100%">
          <h:outputText value="Page #{MicroarraySingleSubmissionBean.geneListPageNum} of #{MicroarraySingleSubmissionBean.numGeneListPages}" styleClass="nav3" />
          <h:panelGrid columns="4">
            <h:outputLink value="gene_list.html">
              <h:graphicImage url="/images/first.png" alt="" title="First Page" styleClass="icon" />
              <f:param value="1" name="pgeNum" />
            </h:outputLink>
            <h:outputLink value="gene_list.html">
              <h:graphicImage url="../images/previous.png" alt="" title="Previous Page" styleClass="icon" />
              <f:param value="#{MicroarraySingleSubmissionBean.geneListPageNum-1}" name="pgeNum" />
            </h:outputLink>
            <h:outputLink value="gene_list.html">
              <h:graphicImage url="../images/next.png" alt="" title="Next Page" styleClass="icon" />
              <f:param value="#{MicroarraySingleSubmissionBean.geneListPageNum+1}" name="pgeNum" />
            </h:outputLink>
            <h:outputLink value="gene_list.html">
              <h:graphicImage url="../images/last.png" alt="" title="Last Page" styleClass="icon" />
              <f:param value="#{MicroarraySingleSubmissionBean.numGeneListPages}" name="pgeNum" />
            </h:outputLink>
          </h:panelGrid>
          <h:panelGroup>
            <h:form>
              <h:panelGrid columns="3">
                <h:outputLabel styleClass="nav3" for="pgeNum" value="Go to page: " /><h:inputText value="#{MicroarraySingleSubmissionBean.geneListPageNum}" id="pgeNum" size="3" />
                <h:commandButton image="../images/gu_go.gif" />
              </h:panelGrid>  
            </h:form>
          </h:panelGroup>
        </h:panelGrid>  
      </td>
      </tr>
      </table>
      
      <h:form id="browseForm" target="_self">
        <h:dataTable cellpadding="2" cellspacing="2" border="0" styleClass="browseTable" rowClasses="table-stripey,table-nostripe" headerClass="table-stripey" width="600"
                     value="#{MicroarraySingleSubmissionBean.geneList}" var="geneListRow">
          <h:column>
           <f:facet name="header">
              <h:outputText value="Select"/>
            </f:facet>
            <h:selectBooleanCheckbox  value="#{geneListRow.selected}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:commandLink target="_self" actionListener="#{MicroarraySingleSubmissionBean.sortByAny}" styleClass="plaintextbold"> 
                <f:attribute value="byGene" name="sortBy" />
                <h:outputText value="Gene Symbol" />
              </h:commandLink>
            </f:facet>
            <h:outputLink styleClass="datatext"  value="#" onclick="window.open('gene.html?gene=#{geneListRow.geneSymbol}&probeset=#{geneListRow.probeId}','','toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes,width=800,height=600');return false" target="_blank">  
              <h:outputText value="#{geneListRow.geneSymbol}"/>
            </h:outputLink>
          </h:column>
          <h:column>
           <f:facet name="header">
              <h:commandLink target="_self" actionListener="#{MicroarraySingleSubmissionBean.sortByAny}" styleClass="plaintextbold"> 
                <f:attribute value="byProbeID" name="sortBy" />
                <h:outputText value="Probe ID" />
              </h:commandLink>
            </f:facet>
            <h:outputText value="#{geneListRow.probeId}" styleClass="plaintext"/>
          </h:column>
          <h:column>
           <f:facet name="header">
              <h:commandLink target="_self" actionListener="#{MicroarraySingleSubmissionBean.sortByAny}" styleClass="plaintextbold"> 
                <f:attribute value="bySignal" name="sortBy" />
                <h:outputText value="Signal" />
              </h:commandLink>
            </f:facet>
            <h:outputText value="#{geneListRow.signal}" styleClass="plaintext"/>
          </h:column>
<%--
          <h:column>
           <f:facet name="header">
              <h:commandLink target="_self" actionListener="#{MicroarraySingleSubmissionBean.sortByAny}" styleClass="plaintextbold"> 
                <f:attribute value="byDetection" name="sortBy" />
                <h:outputText value="Detection" />
              </h:commandLink>
            </f:facet>
            <h:outputText value="#{geneListRow.detection}" styleClass="plaintext"/>
          </h:column>
--%>          
          <h:column>
           <f:facet name="header">
              <h:commandLink target="_self" actionListener="#{MicroarraySingleSubmissionBean.sortByAny}" styleClass="plaintextbold"> 
                <f:attribute value="byPValue" name="sortBy" />
                <h:outputText value="P-Value" />
              </h:commandLink>
            </f:facet>
            <h:outputText value="#{geneListRow.pvalue}" styleClass="plaintext"/>
          </h:column>
        </h:dataTable>  
        <h:panelGrid columns="2" border="0" width="605" styleClass="table-stripey" rendered="false">
          <h:panelGroup>
            <h:panelGrid columns="4">
            <h:selectOneMenu>
              <f:selectItem itemLabel="Add To" itemValue="add"/>
              <f:selectItem itemLabel="Replace" itemValue="replace"/>
              <f:selectItem itemLabel="Get Intersect With"
                            itemValue="intersect"/>
              <f:selectItem itemLabel="Get Difference With"
                            itemValue="difference"/>
            </h:selectOneMenu>
            <h:outputText value="my collection with selected " />
            <h:selectOneMenu>
              <f:selectItem itemLabel="Gene" itemValue="gene"/>
            </h:selectOneMenu>
            <h:commandButton image="../images/gu_go.gif" />
            </h:panelGrid>
          </h:panelGroup>
          <h:commandButton action="{GeneCollectionBean.collection}" value="View My Gene Collection"/>
        </h:panelGrid>
      </h:form>
    </body>
  </html>
</f:view>
