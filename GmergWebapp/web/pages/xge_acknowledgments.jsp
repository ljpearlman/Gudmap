<!doctype html public "-//w3c//dtd html 4.01 transitional//en">
<!-- Microarray analysis & Visualisation jsp interface				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<head>
	<style>
		.leftAlign{
		text-align: left;
		}
		.rightAlign{
			text-align: right;
		}
		.topAlign30{
			vertical-align: top;
			width:30%;
		}
		.topAlign55{
			vertical-align: top;
			width:55%;
		}
		.topAlign{
			vertical-align: top;
		}
                .topAlign50{
                    vertical-align: top;
                    width:50%;
                }
	</style>
        
</head>

<f:view>
  <jsp:include page="/includes/header.jsp" />
	
  

  <h:panelGrid styleClass="header-stripey" style="border:1px solid #6BADC5" columns="2" width="100%" cellpadding="2" cellspacing="2">
    <h:panelGrid style="border:2px solid #6BADC5;background-color:#ffffff;padding:2px;vertical-align:top;">
      <h:panelGroup>
      <h:outputText styleClass="plaintext" value="We thank Peter Vize and Erik Segerdell at Xenbase ("/>
      <h:outputLink value="http://www.xenbase.org/" styleClass="plaintext">
        <h:outputText value="www.xenbase.org" />
      </h:outputLink>
      <h:outputText escape="false" styleClass="plaintext" value=") for providing access to the Xenopus anatomy ontology; Julie Moss (MRC Human Genetics Unit, Edinburgh, UK) for generating movies of Xenopus embryos stained for slc12a1 expression; and Monika Hebeisen (Institute of Pharmaceutical Sciences, ETH Z&uuml;rich) for assistance with sequencing of Xenopus EST clones"/>
      </h:panelGroup>
    </h:panelGrid>
  </h:panelGrid>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>