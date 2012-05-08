<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

  <jsp:include page="/includes/header.jsp" />
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
        <td><h3>View <h:outputText value="#{iSHBatchListBean.labName}" /> lab batch page</h3></td>
        <td align="right"></td>
      <tr>
    </table>
    <h:form id="newBatchForm">
					<h:dataTable border="1" cellpadding="2" cellspacing="0" id="patternTable" value="#{iSHBatchListBean.batches}" var="bat">
						<h:column>
							<f:facet name="header">
								<h:outputText id="select" value="Select" styleClass="plaintext"/>
							</f:facet>
							<h:selectBooleanCheckbox value="#{bat.selected}" rendered="#{bat.status=='2'}" />
							<h:outputText value="&nbsp" escape="false" rendered="#{bat.status!='2'}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Temporary Batch ID" styleClass="plaintext"/>
							</f:facet>
<%-- 
							<h:outputLink value="ish_new_batch.html" styleClass="plaintext" title="Click to view the specific batch" rendered="#{bat.status == '2' || (bat.status == '3' && userBean.user.userPrivilege>=5)}">
--%>
							<h:outputLink value="ish_new_batch.html" styleClass="plaintext" title="Click to view the specific batch" rendered="#{(bat.status == '2' || bat.status == '3') && userBean.user.userPrivilege>=3}">
								<h:outputText value="#{bat.batchID}" />
								<f:param name="batchID" value="#{bat.batchID}" />
							</h:outputLink>
<%-- 
							<h:outputText value="#{bat.batchID}" rendered="#{bat.status == '3' && userBean.user.userPrivilege<5}"/> 
--%>
						</h:column> 
						<h:column>
							<f:facet name="header">
								<h:outputText value="Last modified date" styleClass="plaintext"/>
							</f:facet>
								<h:outputText value="#{bat.lastModifiedDate}" />	
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Last modified by" styleClass="plaintext"/>
							</f:facet>
								<h:outputText value="#{bat.lastModifiedBy}" />	
						</h:column>  
						<h:column>
							<f:facet name="header">
								<h:outputText value="Status" styleClass="plaintext"/>
							</f:facet>
								<h:outputText value="Batch Submitted" rendered="#{bat.status == '3'}"/>
								<h:outputText value="Incomplete" rendered="#{bat.status == '2'}"/>	
						</h:column>           
					</h:dataTable>
					
					<f:verbatim>&nbsp;</f:verbatim>
					
					<f:verbatim>&nbsp;</f:verbatim>
					
					<h:panelGrid columns="1" rendered="#{iSHBatchListBean.status==1}">
						<h:outputText value="Please make a selection before deleting." styleClass="plainred"/>
					</h:panelGrid>
					<h:panelGrid columns="1" rendered="#{iSHBatchListBean.status==2}">
						<h:outputText value="Only one batch should be selected." styleClass="plainred"/>
					</h:panelGrid>
					<h:panelGrid columns="1" rendered="#{iSHBatchListBean.status==3}">
						<h:outputText value="Can't delete batch with locked submissions." styleClass="plainred"/>
					</h:panelGrid>

					<h:panelGrid columns="3" cellspacing="5" rendered="#{userBean.userLoggedIn && userBean.user.userType!='EXAMINER'}">
						<h:commandLink id="deleteBatchButton" action="#{iSHBatchListBean.deleteBatch}" >
							<h:graphicImage url="../images/focus/delbat.gif" alt="Go" styleClass="icon" />
							<f:param name="labId" value="#{iSHBatchListBean.labID}" />
						</h:commandLink>
						<h:commandLink id="addBatchButton" action="#{iSHBatchListBean.addBatch}" >
							<h:graphicImage url="../images/focus/newbat.gif" alt="Go" styleClass="icon" />
							<f:param name="labId" value="#{iSHBatchListBean.labID}" />
						</h:commandLink>						
						
						<h:commandLink id="completeButton" action="#{iSHBatchListBean.completeBatch}">
							<h:graphicImage url="../images/focus/complete.gif" alt="Go" styleClass="icon" />
							<f:param name="labId" value="#{iSHBatchListBean.labID}" />
						</h:commandLink>
					</h:panelGrid>  
	</h:form>  
    <jsp:include page="/includes/footer.jsp" />
  
</f:view>    
    