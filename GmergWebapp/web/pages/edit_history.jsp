<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><h3>Edit History Page</h3></td>
    </tr>
    <br/>
    <h:form>
      <table width="100%" class="stripey">
        <tr>
          <td>
            <h:selectOneRadio value="historyLayoutStyle">
              <f:selectItem itemLabel="Order by editing date" itemValue="OrderByDate" />
              <f:selectItem itemLabel="Order by person" itemValue="OrderByPerson" />
            </h:selectOneRadio>
          </td>
          <td align="right">
            <h:commandLink action="#{QueryBean.findGenes}">
              <h:graphicImage url="../images/gu_go.gif" alt="Go" styleClass="icon"/>
            </h:commandLink>
          </td>
        </tr>
      </table>
    </h:form>
    <br/><br/>
    
    <h:dataTable width="100%" rowClasses="header-stripey,header-nostripe" value="#{EditHistoryBean.modifiedSubmissions}" var="history" border="0">
      <h:column>
        <h:panelGrid columns="1">
          <h:outputText styleClass="plaintextbold" value="Modified Date: #{history[0]}" rendered="#{EditHistoryBean.groupBy == 'date'}"/>
          <h:outputText styleClass="plaintextbold" value="Person: #{history[0]}" rendered="#{EditHistoryBean.groupBy == 'person'}"/>
          <h:panelGroup>
            <h:dataTable value="#{history[1]}" var="person">
              <h:column>
                <h:panelGrid columns="1">
                  <h:outputText styleClass="plaintextbold" value="Person: #{person[0]}" rendered="#{EditHistoryBean.groupBy == 'date'}"/>
                  <h:outputText styleClass="plaintextbold" value="Modified Date: #{person[0]}" rendered="#{EditHistoryBean.groupBy == 'person'}"/>
                  <h:panelGroup>
                    <h:dataTable value="#{person[1]}" var="submissions">
                      <h:column>
                        <h:panelGrid columns="1">
                          <h:outputText styleClass="plaintextbold" value="Submission ID: #{submissions[0]}" />
                          <h:panelGroup>
                            <h:dataTable value="#{submissions[1]}" var="section">
                              <h:column>
                                <h:panelGrid columns="1">
                                  <h:outputText styleClass="plaintextbold" value="#{section[0]}" />
                                  <h:panelGroup>
                                    <h:dataTable styleClass="browseTable" rowClasses="table-nostripe,table-stripey" headerClass="align-top-stripey"
                           bgcolor="white" cellpadding="5" value="#{section[1]}" var="sectionItem">
                                      <h:column>
                                        <f:facet name="header">
                                          <h:outputText styleClass="plaintextbold" value="Modified Field" />
                                        </f:facet>
                                        <h:outputText value="#{sectionItem[0]}" />
                                      </h:column>
                                      <h:column>
                                        <f:facet name="header">
                                          <h:outputText styleClass="plaintextbold" value="Old Value" />
                                        </f:facet>
                                        <h:outputText value="#{sectionItem[1]}" />
                                      </h:column>
                                      <h:column>
                                        <f:facet name="header">
                                          <h:outputText styleClass="plaintextbold" value="New Value" />
                                        </f:facet>
                                        <h:outputText value="#{sectionItem[2]}" />
                                      </h:column>
                                    </h:dataTable>
                                  </h:panelGroup>
                                </h:panelGrid>
                              </h:column>
                            </h:dataTable>
                          </h:panelGroup>
                        </h:panelGrid>
                      </h:column>
                    </h:dataTable>
                  </h:panelGroup>
                </h:panelGrid>
              </h:column>
            </h:dataTable>
          </h:panelGroup>
        </h:panelGrid>
      </h:column>
    </h:dataTable>
  </table>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>