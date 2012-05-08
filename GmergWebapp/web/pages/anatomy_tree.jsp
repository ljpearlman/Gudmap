<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  <p><h:message for="stagesCheck" styleClass="plainred" /></p>
  <p class="plaintext">
    Use the tree to search gene expression patterns in selected
    <h:outputLink rendered="#{siteSpecies != 'Xenopus laevis'}" styleClass="plaintextbold" value="#" onclick="var w=window.open('ontologydescription.html','ontologyPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=600');w.focus();return false;">
     <h:outputText styleClass="plaintextbold" value="anatomy ontology" />
    </h:outputLink>
    <h:outputText styleClass="plaintext" value="anatomy ontology" rendered="#{siteSpecies == 'Xenopus laevis'}" />
    components.
  </p>
  <h:form>
    <h:inputHidden id="treeType" value="{applicationVar.perspective}" />
    <p class="plaintext">
      View
      <h:outputText value="#{applicationScope.perspective}" /> anatomy over developmental stage range:&nbsp; 
      <h:selectOneMenu binding="#{anatomyBean.startInput}" value="#{anatomyBean.startStage}">
        <f:selectItems value="#{anatomyBean.availableStages}"/>
      </h:selectOneMenu>
      &nbsp;
      to:
      <h:selectOneMenu binding="#{anatomyBean.endInput}" value="#{anatomyBean.endStage}">
        <f:selectItems value="#{anatomyBean.availableStages}"/>
      </h:selectOneMenu>
      &nbsp;
      <h:inputHidden id="stagesCheck" validator="#{anatomyBean.validateStages}" value="required" />
      <h:commandButton value="Display Tree" actionListener="#{anatomyBean.displayTree}" />
    </p>
  </h:form>
  <p class="plaintext"><h:outputText rendered="#{anatomyBean.stagesSelected}" value="Current anatomy display is for stage range #{anatomyBean.startStage} to #{anatomyBean.endStage}."/></p>
  <h:form rendered="#{anatomyBean.stagesSelected}" id="anatomyTreeForm">
    
    <h:inputHidden id="start" value="#{anatomyBean.startStage}" required="true" />
    <h:inputHidden id="end" value="#{anatomyBean.endStage}" required="true" />
    
    <h:outputText value="Query: Search for gene expression in selected anatomical components" styleClass="plaintextbold" />
    <br /><br/><h:message for="searchCriteriaCheck" styleClass="plainred" /><br />
    <h:panelGrid columns="2" width="100%">
    <h:panelGroup>
    <h:outputText value="Expression occurs in " styleClass="plaintext" />
    <h:selectOneMenu id="criteria" value="#{anatomyBean.criteria}" required="true" binding="#{anatomyBean.criteriaInput}">
      <f:selectItem itemLabel="all" itemValue="all"/>
      <f:selectItem itemLabel="any" itemValue="any"/>
    </h:selectOneMenu>
    <h:outputText value=" selected components" styleClass="plaintext" />
    </h:panelGroup>
    <h:commandButton value="Search" id="submitbutton" action="#{anatomyBean.annotatedSubmissions}" />
    </h:panelGrid>
    
    <h:panelGrid columns="2">
    <h:outputText value="Expression is:" styleClass="plaintext" />
    <h:selectManyCheckbox id="annotTypes" required="true" value="#{anatomyBean.expressionTypes}" styleClass="plaintext" layout="lineDirection" binding="#{anatomyBean.expressionInput}" >
      <f:selectItems value="#{anatomyBean.annotationVals}" />
    </h:selectManyCheckbox>
    </h:panelGrid>
    
    
    <h:inputHidden id="searchCriteriaCheck" validator="#{anatomyBean.validateSearchCriteria}" value="required" />
    <h:panelGrid cellpadding="0" cellspacing="0" border="0" width="100%" columns="2" rendered="#{anatomyBean.stagesSelected}" columnClasses="treeCol,componentCol">
      <h:panelGrid columns="1" cellpadding="4" cellspacing="0" width="100%" border="0">
        <h:panelGrid columns="1" cellpadding="2" cellspacing="0" border="0" width="100%">
          <h:panelGroup>
            <h:panelGrid columns="1" border="0">
              <h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
                <h:outputText value="Javascript Tree Menu" />
              </h:outputLink>
            </h:panelGrid>
            <f:verbatim>
              <script type="text/javascript">
                <c:forEach items="${anatomyBean.treeContent}" var="row">
                  <c:out value="${row}" escapeXml="false"/>
                </c:forEach>
              
                  queryForm = document.forms['anatomyTreeForm'];
                  queryFormId = queryForm.id;
                  submitbuttonId = queryFormId + ":submitbutton";
                  submitButton = queryForm[submitbuttonId];
                  submitButton.disabled = true;
                  for(i=0;i<queryForm.elements.length;i++){
                    if(queryForm.elements[i].value.indexOf('present') >=0) {
                      queryForm.elements[i].checked = true;
                    }
                  }
                  initializeDocument();
                </script>
              
            </f:verbatim>
          </h:panelGroup>
        </h:panelGrid>
      </h:panelGrid>
      <h:panelGrid columns="1" cellpadding="5" cellspacing="5" border="0" width="100%">
        <f:verbatim>
          <div class="plaintext" id="components">
          </div>
        </f:verbatim>
      </h:panelGrid>
    </h:panelGrid>
    <h:panelGrid cellpadding="0" cellspacing="0" border="0" width="100%" columns="1" rendered="#{anatomyBean.stagesSelected}">
      <h:panelGroup rendered="#{siteSpecies != 'Xenopus laevis'}">
        <h:outputText styleClass="plaintextbold" value="G " />
        <h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
      </h:panelGroup>
      <h:panelGroup rendered="#{siteSpecies == 'Xenopus laevis'}">
        <h:outputText styleClass="plaintext" value="We would like to thank " />
        <h:outputLink styleClass="plaintext" value="http://cbrbio.ucalgary.ca/lab/vize.html"><h:outputText value="Peter Vize" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" and " />
        <h:outputLink styleClass="plaintext" value="http://homepages.ucalgary.ca/~esegerde/"><h:outputText value="Erik Segerdell" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" at " />
        <h:outputLink styleClass="plaintext" value="http://xenbase.org/"><h:outputText value="Xenbase" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" for their help with the " />
        <h:outputLink styleClass="plaintext" value="http://www.obofoundry.org/cgi-bin/detail.cgi?id=xenopus_anatomy"><h:outputText value="anatomy ontology" /></h:outputLink><h:outputText styleClass="plaintext" value="." />
        
      </h:panelGroup>
    </h:panelGrid>
    
  </h:form>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>
