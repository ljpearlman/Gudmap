<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

  <jsp:include page="/includes/header.jsp" />
    <table border="0" width="100%" cellspacing="0" cellpadding="0">
      <tr>
        <td><h3>Batch ID:<h:outputText value="#{ISHBatchSubmissionBean.batchID}" /></h3></td>
        <td align="right"></td>
      <tr>
    </table>
    <h:form id="newBatchForm">
					<h:dataTable border="1" cellpadding="2" cellspacing="0" id="patternTable" value="#{ISHBatchSubmissionBean.submissions}" var="sub">
						<h:column>
							<f:facet name="header">
								<h:outputText id="select" value="Select" styleClass="plaintext"/>
							</f:facet>
							<h:selectBooleanCheckbox value="#{sub.selected}"/>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Temporary GUDMAP ID" styleClass="plaintext"/>
							</f:facet>
							<h:outputText id="id" styleClass="datatext" value="#{sub.submissionID}"/>
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Edit Annotation Link" styleClass="plaintext"/>
							</f:facet>
<%-- 
							<h:outputLink value="#" styleClass="plaintext" target="_blank" title="Edit Annotation"
--%>
							<h:outputLink value="#" styleClass="plaintext" target="_blank" title="Edit Annotation"
					  				onclick="window.open('batch_annotation_tree.html?id=#{sub.submissionID}','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');return false">
<%-- 
								<h:outputText value="View Annotation" rendered="#{UserBean.userLoggedIn && (UserBean.user.userType=='EXAMINER' || UserBean.user.userType!='EXAMINER' && UserBean.user.userPrivilege>2 && UserBean.user.userPrivilege<6 && ISHBatchSubmissionBean.batchCompleted && sub.dbStatus>2)}" />
--%>
								<h:outputText value="View Annotation" 
									rendered="#{UserBean.userLoggedIn 
												&& (UserBean.user.userType=='EXAMINER' 
													|| UserBean.user.userType!='EXAMINER' 
														&& (UserBean.user.userPrivilege==3 && (sub.dbStatus==3 || sub.dbStatus==4 || sub.dbStatus>19) 
															|| UserBean.user.userPrivilege==4 && (sub.dbStatus==3 || sub.dbStatus==4 || sub.dbStatus>21) 
															|| UserBean.user.userPrivilege==5 && (sub.dbStatus==3 || sub.dbStatus==4 || sub.dbStatus>23)) 
													)}" />
								
								<h:outputText value="Edit Annotation" 
									rendered="#{UserBean.userLoggedIn 
												&& (UserBean.user.userType!='EXAMINER' 
													&& (UserBean.user.userPrivilege==3 && (sub.dbStatus==2 || sub.dbStatus>4 && sub.dbStatus<20)
														|| UserBean.user.userPrivilege==4 && (sub.dbStatus==2 || sub.dbStatus>4 && sub.dbStatus<22) 
														|| UserBean.user.userPrivilege==5 && (sub.dbStatus==2 || sub.dbStatus>4 && sub.dbStatus<24) 
														|| UserBean.user.userPrivilege>5))}" />
							</h:outputLink>	
<%-- 
							<h:outputLink rendered="#{sub.dbStatus<3}" value="#" styleClass="plaintext" target="_blank" title="Edit Annotation"
					  				onclick="window.open('batch_annotation_tree.html?id=#{sub.submissionID}','desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');return false">
								<h:outputText value="Edit Annotation" />
							</h:outputLink>	
							<h:outputText value="Non-editable" rendered="#{sub.dbStatus>=3}"/>
--%>												
						</h:column>   
						<h:column>
							<f:facet name="header">
								<h:outputText value="Lock/Unlock" styleClass="plaintext"/>
							</f:facet>
							<h:panelGrid columns="1">
							<h:outputText value="Lock By:#{sub.lockedBy}" rendered="#{sub.locked}"/>
							<h:commandLink action="#{ISHBatchSubmissionBean.unLockSubmission}" rendered="#{sub.locked && UserBean.userLoggedIn && (sub.lockedByPrivilege < UserBean.user.userPrivilege || UserBean.user.userPrivilege>=5)}">
								<h:outputText value="Unlock"/>
								<f:param name="subId" value="#{sub.submissionID}" />
							</h:commandLink>
							</h:panelGrid>
							<h:outputText value="Available" rendered="#{!sub.locked}"/>					
						</h:column>  
						<h:column>
							<f:facet name="header">
								<h:outputText value="Last Modified By" styleClass="plaintext"/>
							</f:facet>
							
								<h:outputText value="#{sub.lastModifiedBy}" />
					
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Last Modified Date" styleClass="plaintext"/>
							</f:facet>
								<h:outputText value="#{sub.lastModifiedDate}" />
													
						</h:column>         
					</h:dataTable>
					
					<f:verbatim>&nbsp;</f:verbatim>
					
					<f:verbatim>&nbsp;</f:verbatim>
					
					<h:panelGrid columns="1" rendered="#{ISHBatchSubmissionBean.status==1}">
						<h:outputText value="Please make a selection before deleting." styleClass="plainred"/>
					</h:panelGrid>
					<h:panelGrid columns="1" rendered="#{ISHBatchSubmissionBean.status==2}">
						<h:outputText value="Only one submission should be selected." styleClass="plainred"/>
					</h:panelGrid>

					<h:panelGrid columns="4" cellspacing="5">
						<h:commandLink id="deleteSubmissionButton" action="#{ISHBatchSubmissionBean.deleteSubmission}" rendered="#{!ISHBatchSubmissionBean.batchCompleted && UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER'}">
							<h:graphicImage url="../images/focus/delsub.gif" alt="Go" styleClass="icon" />
						</h:commandLink>
						<h:commandLink id="addSubmissionButton" action="#{ISHBatchSubmissionBean.addSubmission}" rendered="#{!ISHBatchSubmissionBean.batchCompleted && UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER'}">
							<h:graphicImage url="../images/focus/newsub.gif" alt="Go" styleClass="icon" />
						</h:commandLink>						
						<h:commandLink id="duplicateButton" action="#{ISHBatchSubmissionBean.duplicateSubmission}" rendered="#{!ISHBatchSubmissionBean.batchCompleted && UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER'}">
							<h:graphicImage url="../images/focus/dupsub.gif" alt="Go" styleClass="icon" />
						</h:commandLink>
<%-- 
						<h:commandLink id="completeButton" action="#{ISHBatchSubmissionBean.completeBatch}">
							<h:graphicImage url="../images/focus/complete.gif" alt="Go" styleClass="icon" />
						</h:commandLink>
--%>
						<h:outputLink id="backButton" value="ish_batch_list.html" >
							<h:graphicImage url="../images/focus/back.gif" alt="Go" styleClass="icon" />
							<f:param name="labId" value="#{ISHBatchSubmissionBean.labID}" />	
						</h:outputLink>
					</h:panelGrid>  
	</h:form>  
    <jsp:include page="/includes/footer.jsp" />
  
</f:view>    
    