<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  
  <h:outputText styleClass="plaintextbold" value="No entries exist in the database based on your current search parameters" rendered="#{!ISHComponentGeneExpressionBean.resultExists}"/>

  <h:panelGrid columns="4" styleClass="header-stripey" cellpadding="2" cellspacing="0" width="100%" rendered="#{ISHComponentGeneExpressionBean.resultExists}">
      <h:outputText value="Page #{ISHComponentGeneExpressionBean.pnum} of #{ISHComponentGeneExpressionBean.numPages}" styleClass="nav3" />
      <h:panelGrid columns="4">
        <h:outputLink rendered="#{ISHComponentGeneExpressionBean.rppAll}" value="ish_comp_gene_exp.html">
          <h:graphicImage url="/images/first.png" alt="" title="First Page" styleClass="icon" />
          <f:param value="1" name="pgeNum" />
        </h:outputLink>
        <h:outputLink rendered="#{ISHComponentGeneExpressionBean.rppAll}" value="ish_comp_gene_exp.html">
          <h:graphicImage url="../images/previous.png" alt="" title="Previous Page" styleClass="icon" />
          <f:param value="#{ISHComponentGeneExpressionBean.pnum-1}" name="pgeNum" />
        </h:outputLink>
        <h:outputLink rendered="#{ISHComponentGeneExpressionBean.rppAll}" value="ish_comp_gene_exp.html">
          <h:graphicImage url="../images/next.png" alt="" title="Next Page" styleClass="icon" />
          <f:param value="#{ISHComponentGeneExpressionBean.pnum+1}" name="pgeNum" />
        </h:outputLink>
        <h:outputLink rendered="#{ISHComponentGeneExpressionBean.rppAll}" value="ish_comp_gene_exp.html">
          <h:graphicImage url="../images/last.png" alt="" title="Last Page" styleClass="icon" />
          <f:param value="#{ISHComponentGeneExpressionBean.numPages}" name="pgeNum" />
        </h:outputLink>
      </h:panelGrid>
      <h:panelGroup>
        <h:form>
          <h:panelGrid columns="3">
            <h:outputLabel styleClass="nav3" rendered="#{ISHComponentGeneExpressionBean.rppAll}" for="pgeNum" value="Go to page: " />
            <h:inputText rendered="#{ISHComponentGeneExpressionBean.rppAll}" value="#{ISHComponentGeneExpressionBean.pnum}" id="pgeNum" size="3" />
            <h:commandButton rendered="#{ISHComponentGeneExpressionBean.rppAll}" image="../images/gu_go.gif" />
          </h:panelGrid>  
        </h:form>
      </h:panelGroup>
    </h:panelGrid>
  
    <h:form id="browseForm" rendered="#{ISHComponentGeneExpressionBean.resultExists}">
      <h:dataTable width="100%" styleClass="browseTable" rowClasses="table-nostripe,table-stripey" headerClass="table-stripey" 
          value="#{ISHComponentGeneExpressionBean.components}" var="submission" rendered="#{ISHComponentGeneExpressionBean.resultExists}">
        <h:column>
          <f:facet name="header">
            <h:commandLink actionListener="#{ISHComponentGeneExpressionBean.sortByAny}" styleClass="plaintextbold"> 
              <f:attribute value="byPath" name="sortBy" />
              <h:outputText value="Component Description"/>
            </h:commandLink>
          </f:facet>
          <h:outputText value="#{submission[0]}"  styleClass="plaintext"/>              
        </h:column>            
        <h:column>
          <f:facet name="header">
            <h:commandLink actionListener="#{ISHComponentGeneExpressionBean.sortByAny}" styleClass="plaintextbold"> 
              <f:attribute value="byCompID" name="sortBy" />
              <h:outputText value="Component ID" />
            </h:commandLink>
          </f:facet>
          <h:outputText value="#{submission[1]}" styleClass="plaintext"/>
        </h:column>
        <h:column>
          <f:facet name="header">
            <h:commandLink styleClass="plaintextbold" actionListener="#{ISHComponentGeneExpressionBean.sortByAny}"> 
              <f:attribute value="byTS" name="sortBy" />
              <h:outputText value="#{stageSeriesLong} Stage" />
            </h:commandLink>
          </f:facet>
          <h:outputText value="#{submission[2]}" styleClass="plaintext"/>
        </h:column>
        <h:column>
          <f:facet name="header">
            <h:commandLink styleClass="plaintextbold" actionListener="#{ISHComponentGeneExpressionBean.sortByAny}"> 
              <f:attribute value="byGene" name="sortBy" />
              <h:outputText value="Gene Symbol" />
            </h:commandLink>
          </f:facet>
          <h:outputLink styleClass="plaintext" value="gene.html">
            <h:outputText value="#{submission[3]}" styleClass="plaintext"/>
            <f:param name="gene" value="#{submission[3]}"/>
          </h:outputLink>
        </h:column>          
        <h:column>
          <f:facet name="header">
            <h:commandLink styleClass="plaintextbold" actionListener="#{ISHComponentGeneExpressionBean.sortByAny}"> 
              <f:attribute value="byGroup" name="sortBy" />
              <h:outputText value="Number of entries" />
            </h:commandLink>
          </f:facet>
          <h:commandLink styleClass="plaintext" action="#{QueryBean.findSubsExpressedInComponent}">
            <f:param value="#{submission[1]}" name="component" />
            <f:param value="#{submission[2]}" name="stage" />
            <f:param value="#{submission[3]}" name="geneSymbol" />
            <h:outputText value="#{submission[4]} entry/entries"/>
          </h:commandLink>
        </h:column>
      </h:dataTable>
    </h:form>
    
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>