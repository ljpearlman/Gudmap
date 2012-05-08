<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
  <jsp:include page="/includes/header.jsp" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><h3>Database Query Page</h3></td>
      <td align="right"></td>
    </tr>
      <table border="0" width="100%">
      <tr class="header-stripey">
        <td width="85%">
          <span class="plaintextbold">
            <h:form>
              Search expression in components using 
              <h:commandLink action="#{anatomyBean.anatomyQueryPage}" styleClass="plaintext">
                <h:outputText value="anatomy ontology" />
              </h:commandLink>
              tree structure
            </h:form>
          </span>
        </td>
        <td align="center" width="15%">
          <h:form>
            <h:commandLink action="#{anatomyBean.anatomyQueryPage}">
              <h:graphicImage url="../images/gu_go.gif" alt="Go" styleClass="icon"/>
            </h:commandLink>
          </h:form>
        </td>
      </tr>
      </table>
      <br/><br/>
      <h:form>
      <table border="0" width="100%">
      <tr>
        <td colspan="4" align="left" class="plaintext">Gene Query</td>
      </tr>
      <tr class="header-stripey">
        <td width=85% align="left">
        &nbsp;<h:message for="geneSymbol" styleClass="plainred"/><br/>
        <span class="plaintextbold">Search DB for
              <h:selectOneMenu id="inputType" value="#{queryBean.inputType}">
                <f:selectItem itemLabel="Gene Symbol" itemValue="symbol"/>
                <f:selectItem itemLabel="Gene Name" itemValue="name"/>
                <f:selectItem itemLabel="Gene Synonym" itemValue="synonym"/>
                <f:selectItem itemLabel="All" itemValue="all"/>
              </h:selectOneMenu> which 
              <h:selectOneMenu id="criteria" value="#{queryBean.criteria}">
                <f:selectItem itemLabel="contains" itemValue="wildcard"/>
                <f:selectItem itemLabel="equals" itemValue="equals"/>
                <f:selectItem itemLabel="starts with" itemValue="begins"/>
              </h:selectOneMenu>
              <h:inputText id="geneSymbol" value="#{queryBean.geneSymbol}" /> in
              </span>
          <span class="plaintextbold">
<%-- 
          <h:outputLink styleClass="plaintextbold" value="http://genex.hgu.mrc.ac.uk/Databases/Anatomy/MAstaging.html" rendered="#{siteSpecies == 'mouse'}">
--%>
          <h:outputLink styleClass="plaintextbold" value="http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/stagecriteria.html" rendered="#{siteSpecies == 'mouse'}">
            <h:outputText value="#{stageSeriesMedium} Stage" styleClass="plaintextbold" />
          </h:outputLink>
          <h:outputLink styleClass="plaintext" value="http://xenbase.org/xenbase/original/atlas/NF/NF-all.html"  rendered="#{siteSpecies == 'Xenopus laevis'}">
            <h:outputText value="#{stageSeriesMedium} Stage" styleClass="plaintextbold" />
          </h:outputLink>
              <h:selectOneMenu id="stage" value="#{queryBean.stage}">
                <f:selectItems value="#{anatomyBean.availableStagesForQuery}"/>
              </h:selectOneMenu></span><span class="plaintext">
            </span>
            
                  <span class="plaintextbold">
                  <h:selectOneMenu id="output" value="#{queryBean.output}">
	                <f:selectItem itemLabel="List all entries" itemValue="gene"/>
	                <f:selectItem itemLabel="List annotated entries only" itemValue="anatomy"/>
                  </h:selectOneMenu>
                  </span>
        </td>
        <td width=15% align="center"><h:commandButton image="../images/gu_go.gif" alt="Go" styleClass="icon" action="#{queryBean.findGenes}" /></td>
      </tr>
      </table>
      </h:form>
      <br/><br/>
      <h:form>
      <h:outputText styleClass="plaintext" value="Which genes are expressed in a component?" rendered="#{siteSpecies != 'Xenopus laevis'}" />
      <h:panelGrid border="0" width="100%" columns="2" columnClasses="queryLeft,queryRight" rendered="#{siteSpecies != 'Xenopus laevis'}">
        <h:panelGroup>
          <h:message for="component" styleClass="plainred"/><f:verbatim><br /></f:verbatim>
          <h:outputText styleClass="plaintextbold" value="Enter Component ID (e.g. EMAP:2220):" />
          <h:inputText id="component" value="#{queryBean.componentID}" />
        </h:panelGroup>
        <h:commandLink action="#{queryBean.findComponent}">        
          <h:graphicImage url="../images/gu_go.gif" alt="Go"  styleClass="icon"/>
          <f:param value="query" name="component" />
        </h:commandLink>
      </h:panelGrid>
      
      </h:form>
      <br/><br/>
      <h:form id="SubDetail">
      <table border="0" width="100%">
      <tr>
        <td align="left" class="plaintext" colspan="4">Find a specific submission</td>
      </tr>
      <tr class="header-stripey">
        <td align="left" colspan="3" width=85%>
        &nbsp;<h:message for="subid" styleClass="plainred"/><br />
        <span class="plaintextbold">
          Enter Submission ID:
          <h:inputText id="subid" value="#{queryBean.submissionID}" />
        </span>
        </td>
        <td align="center" width=15%>
        <h:commandButton image="../images/gu_go.gif" styleClass="icon" alt="Go" action="#{queryBean.submissionInfo}" />
        </td>
      </tr>
      </table>
      </h:form>
    </table>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>