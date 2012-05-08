<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <html>
    <head>
      <title>Gudmap Edit Window</title>
      <link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
      <style type="text/css">@import("${pageContext.request.contextPath}/css/ie51.css");</style>
    </head>
    <body>
    <h:form>
      <h:outputText styleClass="titletext" value="Person"/>
      <br/>
      <h:panelGrid columns="1" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
        <h:outputText styleClass="plaintextbold" value="Under construction..."/>
      </h:panelGrid>
      
      <h:panelGrid columns="1" rowClasses="header-stripey">
        <h:outputText value="Authors" />
        <h:inputTextarea id="authors" rows="5" cols="20">
          <h:outputText value="authors" />
        </h:inputTextarea>
      </h:panelGrid>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Author Detail" action="alert('Author Details updated')" />
      </h:panelGrid>
      
      <br/>
      <h:outputText value="Principal Investigator" styleClass="plaintextbold"/>
      <h:panelGrid columns="2" rowClasses="header-stripey">
        <h:outputText value="Name" id="piName"/>
        <h:inputText value="Name" />
        
        <h:outputText value="Institute" id="piInstitute"/>
        <h:inputText value="institute" />

        <h:outputText value="Address" id="piAddress" styleClass="top"/>
        <h:inputTextarea cols="20" rows="5">
          <h:outputText value="address"/>
        </h:inputTextarea>
        
        <h:outputText value="City" id="piCity" />
        <h:inputText value="city" />
        
        <h:outputText value="Postcode" id="piPostcode" />
        <h:inputText value="postcode" />
        
        <h:outputText value="Country" id="piCountry" />
        <h:inputText value="country" />
        
        <h:outputText value="Telephone" id="piTelephone" />
        <h:inputText value="telephone" />
        
        <h:outputText value="Fax" id="piFax" />
        <h:inputText value="fax" />
        
        <h:outputText value="Email" id="piEmail" />
        <h:inputText value="email" />
      </h:panelGrid>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update PI Detail" action="alert('PI Detail updated')" />
      </h:panelGrid>

      <br/>
      <h:outputText value="Submitted to GUDMAP by..." styleClass="plaintextbold"/>
      <h:panelGrid columns="2" rowClasses="header-stripey">
        <h:outputText value="Name" id="submitterName"/>
        <h:inputText value="Name" />
        
        <h:outputText value="Institute" id="submitterInstitute"/>
        <h:inputText value="institute" />

        <h:outputText value="Address" id="submitterAddress" />
        <h:inputTextarea cols="20" rows="5">
          <h:outputText value="address"/>
        </h:inputTextarea>
        
        <h:outputText value="City" id="submitterCity" />
        <h:inputText value="city" />
        
        <h:outputText value="Postcode" id="submitterPostcode" />
        <h:inputText value="postcode" />
        
        <h:outputText value="Country" id="submitterCountry" />
        <h:inputText value="country" />
        
        <h:outputText value="Telephone" id="submitterTelephone" />
        <h:inputText value="telephone" />
        
        <h:outputText value="Fax" id="submitterFax" />
        <h:inputText value="fax" />
        
        <h:outputText value="Email" id="submitterEmail" />
        <h:inputText value="email" />
      </h:panelGrid>
      <h:panelGrid columns="2" styleClass="header-stripey" columnClasses="align-left,align-right">
        <h:commandButton value="Close" onclick="self.close()" type="button" />
        <h:commandButton value="Update Submitter Detail" action="alert('Submitter Details updated')" />
      </h:panelGrid>
    </body>
    </h:form>
  </html>
</f:view>
