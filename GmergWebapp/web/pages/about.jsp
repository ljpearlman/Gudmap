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
      <h:outputText styleClass="plaintext" value="The EuReGene Xenopus Gene Expression Database (EXGEbase) is an interactive, searchable gene expression database focusing on terminal differentiation markers. It currently contains whole mount in situ hybridization data of 210  solute carrier genes. The pronephric expression patterns are fully annotated in accordance with the model described by Raciti et al (submitted). The database interface allows for browsing per gene (individual level) or per segment of the pronephric kidney (comparative level). Expression patterns in non-renal tissues such as brain, liver, and heart are fully annotated in accordance with the Xenopus Anatomy Ontology (XAO;"/> 
      <h:outputLink value="http://www.obofoundry.org/" styleClass="plaintext">
        <h:outputText value="http://www.obofoundry.org/" />
      </h:outputLink>
      <h:outputText styleClass="plaintext" value=")." />
      </h:panelGroup>
    </h:panelGrid>
  </h:panelGrid>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>