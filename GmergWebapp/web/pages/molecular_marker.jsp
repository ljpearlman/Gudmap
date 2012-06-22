<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/tlds/components.tld" prefix="d" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />

  <td><h3>Molecular Marker Candidates</h3></td>
  <h:dataTable width="100%" styleClass="browseTable" rowClasses="table-stripey,table-nostripe" headerClass="align-top-stripey" 
            value="#{MolecularMarkerBean.markerList}" var="marker">
	      <h:column>
            <f:facet name="header">
                <h:outputText value="Tissue" />             
            </f:facet>
              <h:outputText value="#{marker[0]}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText value="Gene Symbol" />             
            </f:facet>
            <h:outputLink styleClass="plaintext" value="gene.html">
              <h:outputText value="#{marker[1]}"/>
              <f:param name="gene" value="#{marker[1]}"/>
            </h:outputLink>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText value="Stage Range" />             
            </f:facet>
              <h:outputText value="#{marker[2]}"/>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText value="ISH" />             
            </f:facet>
              <t:dataList id="ishs"
	               styleClass="plaintextbold"
	               var="ish"
	               value="#{marker[3]}"
	               layout="simple"
	               rowCountVar="count"
	               rowIndexVar="index">
	               <h:outputLink styleClass="plaintext" value="ish_submission.html">
		              <h:outputText value="#{ish}"/>
		              <f:param name="id" value="#{ish}"/>
		           </h:outputLink>
	          </t:dataList>
          </h:column><h:column>
            <f:facet name="header">
                <h:outputText value="IHC" />             
            </f:facet>
              <t:dataList id="ihcs"
	               styleClass="plaintextbold"
	               var="ihc"
	               value="#{marker[4]}"
	               layout="simple"
	               rowCountVar="count"
	               rowIndexVar="index">
	               <h:outputLink styleClass="plaintext" value="ish_submission.html">
		              <h:outputText value="#{ihc}"/>
		              <f:param name="id" value="#{ihc}"/>
		           </h:outputLink>
	          </t:dataList>
          </h:column><h:column>
            <f:facet name="header">
                <h:outputText value="Array" />             
            </f:facet>
              <t:dataList id="Arrays"
	               styleClass="plaintextbold"
	               var="array"
	               value="#{marker[5]}"
	               layout="simple"
	               rowCountVar="count"
	               rowIndexVar="index">
	               <h:outputLink styleClass="plaintext" value="mic_submission.html">
		              <h:outputText value="#{array}"/>
		              <f:param name="id" value="#{array}"/>
		           </h:outputLink>	               
	          </t:dataList>
          </h:column>
			  
	  
      
  </h:dataTable>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>