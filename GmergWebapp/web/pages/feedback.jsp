<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText value="#{FeedbackBean.message}" escape="false" styleClass="errorMessage" />
	
	<h:form id="FeedbackForm" rendered="#{!FeedbackBean.submitted}">
		<h:panelGrid columns="2" width="100%" cellspacing="2px" cellpadding="10px" 
					 columnClasses="rightAlign,leftAlign"  styleClass="header-stripey" >
			
			<h:outputText value="Name: " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:inputText id="name" value="#{FeedbackBean.name}" size="50" 
							 validator="#{FeedbackBean.validateName}" 
							 required="true" requiredMessage="Please enter you name" />
				<h:message for="name" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Your Email address: " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:inputText id="emailAddress" value="#{FeedbackBean.email}" size="50" 
							 validator="#{FeedbackBean.validateEmail}" 
							 required="true" requiredMessage="Please supply a valid email address" />
				<h:message for="emailAddress" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Your comment: " styleClass="plaintextbold" />
			<h:panelGrid>
			    <h:inputTextarea id="comment" value="#{FeedbackBean.comment}" rows="8" cols="60" 
			          validator="#{FeedbackBean.validateComment}" 
							 required="true" requiredMessage="Please supply a comment"/>
				<h:message for="comment" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="URL you wish to comment on (optional): " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:inputText id="url" value="#{FeedbackBean.referringURL}" size="50" />
			</h:panelGrid>
			
			<h:outputText value="Which operating system are you using? " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:selectOneMenu id="os" value="#{FeedbackBean.operatingSystem}" >
				    <f:selectItems value="#{FeedbackBean.operatingSystemOptions}"/>
			    </h:selectOneMenu>
			</h:panelGrid>
			
			<h:outputText value="Which web browser are you using? " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:selectOneMenu id="browser" value="#{FeedbackBean.browser}">
				    <f:selectItems value="#{FeedbackBean.browserOptions}"/>
			    </h:selectOneMenu>
			</h:panelGrid>
			
			
			<h:outputText value="Are you trying to access the database from behind a firewall? " styleClass="plaintextbold" />
			<h:panelGrid>
			    <h:selectOneMenu id="firewall" value="#{FeedbackBean.behindFirewall}">
							 <f:selectItem itemValue="Yes" itemLabel="Yes"/>
							 <f:selectItem itemValue="No" itemLabel="No"/>
							 <f:selectItem itemValue="Don't know" itemLabel="Don't know"/>
			    </h:selectOneMenu>
			</h:panelGrid>

		</h:panelGrid>

		<h:panelGrid width="100%" cellpadding="10px" columnClasses="rightAlign,leftAlign" styleClass="table-stripey" >
			<h:commandButton value="Send" actionListener="#{FeedbackBean.emailComment}" />
		</h:panelGrid>
	</h:form> 
	
	<jsp:include page="/includes/footer.jsp" />
	
</f:view>