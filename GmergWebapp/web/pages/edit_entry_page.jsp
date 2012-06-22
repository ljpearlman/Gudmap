<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  <f:verbatim>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><h3>Edit Query Page</h3></td>
    </tr>
    <br/>
    
    <table width="100%" border="0">
      <tr>
        <td colspan="4" align="left" class="plaintextbold">Modification History Summary</td>
      <tr>
      <tr>
        <td>Last Edited Item:</td>
        <td>Probe: Notes</td>
      </tr>
      <tr>
        <td>Last Editing Date:</td>
        <td>16-May-2007</td>
      </tr>
      <tr>
        <td>Last Editing Person:</td>
        <td>Jane Armstrong</td>
      </tr>
    </table>
    <br/>
    </f:verbatim>
    <h:form><f:verbatim>
    <table width="100%">
    <tr>
      <td colspan="4" align="left" class="plaintextbold">Submission Query</td>
    </tr>
    <tr class="header-stripey">
      <td>
        <span class="plaintextbold">Show me all</f:verbatim>
          <h:selectOneMenu id="publicStatus" value="publicStatus">
            <f:selectItem itemLabel="public" itemValue="public"/>
            <f:selectItem itemLabel="non-public" itemValue="nonPublic"/>
            <f:selectItem itemLabel="all" itemValue="all"/>
          </h:selectOneMenu> submissions with 
          <h:selectOneMenu id="filterItem" value="filterItem">
            <f:selectItem itemLabel="" itemValue="noValue"/>
            <f:selectItem itemLabel="Archive ID" itemValue="archiveId"/>
            <f:selectItem itemLabel="Submission Date" itemValue="submissionDate"/>
            <f:selectItem itemLabel="Lab ID" itemValue="labId"/>
          </h:selectOneMenu> = 
          <h:inputText id="filterItemValue" value="filterItemValue" required="true"/> AND status is 
          <h:selectOneMenu id="dbStatus">
            <f:selectItems value="#{EditorQueryBean.databaseStatus}"/>
          </h:selectOneMenu><f:verbatim>
        </span>
        </td>
        <td align="right"></f:verbatim>
          <h:commandLink action="#{EditorQueryBean.getSubmissions}">
            <h:graphicImage url="../images/gu_go.gif" alt="Go" styleClass="icon"/>
          </h:commandLink><f:verbatim>
        </td>
      </td>
    </tr>
    </table></f:verbatim>
    </h:form>
<f:verbatim>    <br/><br/></f:verbatim>
    <h:form><f:verbatim>
      <table width="100%">
        <tr>
          <td colspan="4" align="left" class="plaintextbold">View Modification History</td>
          <td align="right"></f:verbatim>
            <h:commandLink action="browseEditHistory">
              <h:graphicImage url="../images/gu_go.gif" alt="Go" styleClass="icon"/>
            </h:commandLink><f:verbatim>
          </td>
        </tr>
      </table></f:verbatim>
    </h:form><f:verbatim>
  </table></f:verbatim>

  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>