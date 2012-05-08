<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

	<jsp:include page="/includes/header.jsp" />
	<table border="0" width="100%" cellspacing="0" cellpadding="0">
    	<tr>
    		<td><h3>Adding annotation page</h3></td>
    		<td align="right"></td>
    	<tr>
    </table>
    <h:outputText value="You have to log in in order to use this page." rendered="#{!userBean.userLoggedIn}"/>
    <t:dataList id="labs" styleClass="plaintextbold" var="lab" 
    			value="#{labSummaryBean.selectedLab}" layout="simple"
    			rowCountVar="count" rowIndexVar="index" 
    			rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=3}">
    	<f:verbatim>
    		<table border="0" width="100%" cellspacing="0" cellpadding="0"> 
    			<tr class="header-stripey">
    				<td>
    	</f:verbatim>
    	<f:verbatim><a name="</f:verbatim><h:outputText value="#{lab.labName}"/><f:verbatim>"></a></f:verbatim>
    	<h:form>
    		<h:panelGrid columns="4">
    			<f:facet name="header">
    				<h:panelGroup styleClass="plaintextbold">
    					<h:outputText  value="#{lab.labName}"/>
    					<h:outputText value="  Date of Latest Entry in Database: #{lab.latestEntryDate}"/>
    					<h:outputLink value="ish_batch_list.html" 
    								rendered="#{userBean.userLoggedIn && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}">
    						<f:param name="labId" value="#{lab.labId}" />
    						<f:param name="labName" value="#{lab.labName}" />
    						<h:outputText value="  View/Create lab batch annotation"/>
    					</h:outputLink>
    				</h:panelGroup>
    			</f:facet>
    		</h:panelGrid>
    	</h:form>
    	<f:verbatim>
    			</tr>
    		</table>
    	</f:verbatim>
		<h:form>
			<h:dataTable width="100%" value="#{lab.summaryResults}" var="summary"  
				headerClass="align-left-header-lab_sum" columnClasses="columnWidth201,columnWidth202,columnWidth203,columnWidth204,columnWidth205" >
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
    				<h:outputLink styleClass="datatext" value="http://www.gudmap.org/Submission_Archive/index.html##{summary[4]}" >
    					<h:outputText value="#{summary[4]}" />
    				</h:outputLink>
    			</h:column>
    			<h:column>
    				<f:facet name="header">
    					<h:outputText styleClass="plaintextbold" value="Number of Submissions" />
    				</f:facet>
    				<%-- ============================== ISH =============================== --%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Public' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="4" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Public' && userBean.user.userPrivilege<6}" styleClass="plaintext"/>
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="5" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>  
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Annotation' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>  
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="19" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>  	           
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Annotator' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>  	           
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="20" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>  	           
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>  	           
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="21" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>     
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>     
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="22" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege<5}" styleClass="plaintext"/>     
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Awaiting Editor QA' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>     
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="23" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege<5}" styleClass="plaintext"/>
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Editor' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="24" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/>          
    				<h:outputLink rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="25" />
    					<f:param name="assayType" value="ISH" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'ISH' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/> 
    				<%-- ============================== IHC =============================== --%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Public' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="4" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Public' && userBean.user.userPrivilege<6}" styleClass="plaintext"/> 	        
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="5" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>   
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Annotation' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>   
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="19" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>    
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Annotator' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>    
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="20" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>   
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>   
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="21" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>        
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>        
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="22" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege<5}" styleClass="plaintext"/>             
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Awaiting Editor QA' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>             
<%-- 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="23" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
<%-- 
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege<5}" styleClass="plaintext"/> 
--%>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Editor' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/> 
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="24" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/>             
    				<h:outputLink rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
    					<h:outputText value="#{summary[1]}" />
    					<f:param name="date" value="#{summary[0]}" />
    					<f:param name="labId" value="#{lab.labId}" />
    					<f:param name="isPublic" value="25" />
    					<f:param name="assayType" value="IHC" />
    					<f:param name="archiveId" value="#{summary[4]}" />
    				</h:outputLink>
    				<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'IHC' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/> 
    				<%-- ============================== TG =============================== --%>
    				<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Public' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="4" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Public' && userBean.user.userPrivilege<6}" styleClass="plaintext"/> 	        
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="5" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Annotation' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>   
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Annotation' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>   
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege>=3 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="19" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Annotator' && userBean.user.userPrivilege<3}" styleClass="plaintext"/>    
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Annotator' && (userBean.user.userPrivilege<3 || userBean.user.userPrivilege>=3 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>    
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="20" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>   
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>   
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege>=4 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="21" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Annotator' && userBean.user.userPrivilege<4}" styleClass="plaintext"/>        
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Annotator' && (userBean.user.userPrivilege<4 || userBean.user.userPrivilege>=4 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>        
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="22" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Editor QA' && userBean.user.userPrivilege<5}" styleClass="plaintext"/>             
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Awaiting Editor QA' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/>             
<%-- 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5}" value="lab_ish_edit.html" styleClass="plaintext">
--%>
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege>=5 && (userBean.user.userType!='EXAMINER' || userBean.user.userType=='EXAMINER' && lab.viewableByExaminer)}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="23" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
<%-- 
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Editor' && userBean.user.userPrivilege<5}" styleClass="plaintext"/> 
--%>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Editor' && (userBean.user.userPrivilege<5 || userBean.user.userPrivilege>=5 && userBean.user.userType=='EXAMINER' && !lab.viewableByExaminer)}" styleClass="plaintext"/> 
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="24" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Completely Annotated by Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/>             
	    			<h:outputLink rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege>=6}" value="lab_ish_edit.html" styleClass="plaintext">
	    				<h:outputText value="#{summary[1]}" />
	    				<f:param name="date" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="isPublic" value="25" />
	    				<f:param name="assayType" value="TG" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:outputLink>
	    			<h:outputText value="#{summary[1]}" rendered="#{summary[2] == 'Tg' && summary[3] == 'Partially Annotated by Sr. Editor' && userBean.user.userPrivilege<6}" styleClass="plaintext"/> 
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
	    			<h:outputText value="#{summary[3]}" styleClass="plaintext"/>
	    		</h:column>
                <%--=========================== Go Public button ===================================--%>
	    		<h:column rendered="#{userBean.userLoggedIn && (userBean.user.userPrivilege>=6)}">
	    			<f:facet name="header">
	    				<h:outputText styleClass="plaintextbold" value="Action" />
	    			</f:facet>
	    			<h:commandLink id="goPublicButton" value="Go Public" action="#{labSummaryBean.makeSubmissionsPublic}"
	    							rendered="#{summary != null && summary[3] != 'Public' && userBean.user.userPrivilege>=6}" styleClass="plaintext" style="white-space:nowrap" >
	    				<f:param name="submissionDate" value="#{summary[0]}" />
	    				<f:param name="labId" value="#{lab.labId}" />
	    				<f:param name="submissionState" value="#{summary[3]}" />
	    				<f:param name="submissionType" value="#{summary[2]}" />
    					<f:param name="archiveId" value="#{summary[4]}" />
	    			</h:commandLink>
	    		</h:column>

			</h:dataTable>
		</h:form>
		<f:verbatim>
			<table border="0" width="100%" cellspacing="0" cellpadding="0">
				<tr class="header-stripey"><td>&nbsp;</td></tr>
			</table>
			<br/>
		</f:verbatim>
	</t:dataList>
	
	<jsp:include page="/includes/footer.jsp" />

</f:view>
