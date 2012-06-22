<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

  <jsp:include page="/includes/header.jsp" />
      
      <t:dataList id="labs"
               styleClass="plaintextbold"
               var="lab"
               value="#{LabSummaryBean.labs}"
               layout="simple"
               rowCountVar="count"
               rowIndexVar="index">
        <f:verbatim>  
    	<table border="0" width="100%" cellspacing="0" cellpadding="0"> 
        <tr class="header-stripey">
        <td>   
        </f:verbatim>
        <f:verbatim><a name="</f:verbatim><h:outputText value="#{lab.labName}"/><f:verbatim>"></a></f:verbatim>
        <h:form>
        <h:panelGrid columns="6" rendered="#{! empty lab.summaryResults}">       
          <f:facet name="header">
            <h:panelGroup styleClass="plaintextbold">
              <h:outputText  value="#{lab.labName}" />
              <h:outputLink value="lab_ish_browse.html">
                <h:outputText value=" (View All In situ) " />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param name="assayType" value="insitu" />
              </h:outputLink>
              <h:outputLink value="lab_array_browse.html">
                <h:outputText value=" (View All Microarray) " />
                <f:param name="labId" value="#{lab.labId}" />
              </h:outputLink>
              <h:outputLink value="lab_tg_browse.html">
                <h:outputText value=" (View All Transgenic) " />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param name="assayType" value="TG" />
              </h:outputLink>
              <h:outputText value="Date of Latest Entry in Database: #{lab.latestEntryDate}" />
            </h:panelGroup>
          </f:facet> 
        </h:panelGrid>
        </h:form> 
        <f:verbatim>
        </tr>
        </table>
        </f:verbatim>
        <h:form>
        <%-- added columnClasses to adjust the layout - now there are 5 columns (ArchiveId added in) - xingjun - 18/04/2011 --%>
        <%-- also changed headerClasses from align-left-header to align-left-header-lab-sum 25% to 20% - xingjun - 18/04/2011 --%>
        <h:dataTable width="100%" value="#{lab.summaryResults}" rendered="#{! empty lab.summaryResults}" var="summary"  headerClass="align-left-header-lab_sum" columnClasses="columnWidth201,columnWidth202,columnWidth203,columnWidth204,columnWidth205" >
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Submission Date" />
            </f:facet>
            <h:outputText value="#{summary[0]}" styleClass="plaintext"/>
          </h:column>
         <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Archive ID" />
            </f:facet>
            <h:outputLink styleClass="datatext"  value="http://www.gudmap.org/Submission_Archive/index.html##{summary[4]}" >
            	<h:outputText value="#{summary[4]}" />
			</h:outputLink>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Number of Submissions" />
            </f:facet>
            
            <h:outputText rendered="#{summary[3] != 'Available'}" value="#{summary[1]}" styleClass="plaintext"/>
            <h:outputLink rendered="#{summary[2] == 'Microarray' && summary[3] == 'Available'}" value="lab_array_browse.html" styleClass="plaintext">
                <h:outputText value="#{summary[1]}" />
                <f:param name="date" value="#{summary[0]}" />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param name="archiveId" value="#{summary[4]}" />
            </h:outputLink>
            <h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Available'}" value="lab_ish_browse.html" styleClass="plaintext">
                <h:outputText value="#{summary[1]}" />
                <f:param name="date" value="#{summary[0]}" />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param value="ISH" name="assayType" />
                <f:param name="archiveId" value="#{summary[4]}" />
            </h:outputLink>            
            <h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Available'}" value="lab_ish_browse.html" styleClass="plaintext">
                <h:outputText value="#{summary[1]}" />
                <f:param name="date" value="#{summary[0]}" />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param value="IHC" name="assayType" />
                <f:param name="archiveId" value="#{summary[4]}" />
            </h:outputLink>
            <h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Available'}" value="lab_tg_browse.html" styleClass="plaintext">
                <h:outputText value="#{summary[1]}" />
                <f:param name="date" value="#{summary[0]}" />
                <f:param name="labId" value="#{lab.labId}" />
                <f:param value="TG" name="assayType" />
                <f:param name="archiveId" value="#{summary[4]}" />
            </h:outputLink>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Assay Type" />
            </f:facet>
            <h:outputText value="#{summary[2]}" styleClass="plaintext"/>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Status" />
            </f:facet>
            <h:outputText value="#{summary[3]}" styleClass="plaintext" />
          </h:column>
        </h:dataTable>   
             
        </h:form> 
        <f:verbatim rendered="#{! empty lab.summaryResults}">
        <table border="0" width="100%" cellspacing="0" cellpadding="0">
          <tr class="header-stripey"><td>&nbsp;</td></tr>
        </table>
        <br/>
        </f:verbatim>
      </t:dataList>
      
      
      <c:if test="${applicationVar.project != 'GUDMAP'}">
        <table border="0" width="100%" cellspacing="0" cellpadding="0">
          <tr class="header-stripey">
            <td align="center">
              <span class="plaintextbold">Visit the <a class="plaintextbold" href="http://www.gudmap.org/Submission_Archive/index.html">Download Site</a> for a detailed list of all files submitted</span>
            </td>
          </tr>
        </table>
      </c:if>
  
    <jsp:include page="/includes/footer.jsp" />
  
</f:view>
