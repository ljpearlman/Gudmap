<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  <h:form>
    <table border="0" width="100%">
      <tr class="header-stripey">

        <td><table border ="0">
	  
          <tr>
	  <td class="plaintext" width="20%">Series GEO ID:</td>
          <td class="datatext">
			<h:outputLink value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc=#{MicSeriesBean.series.geoID}" styleClass="plaintext">
            <h:outputText value="#{MicSeriesBean.series.geoID}" />
            </h:outputLink>          
          </td>
	  </tr>
          <tr>
	  <td class="plaintext">Number of Samples:</td>
          <td class="datatext"><h:outputText value="#{MicSeriesBean.series.numSamples}" /> samples</td>
	  </tr>
          <tr>
	  <td class="plaintext">Title:</td>
          <td class="datatext"><h:outputText value="#{MicSeriesBean.series.title}" /></td>
	  </tr>
          <tr>
	  <td class="plaintext" valign=top>Summary:</td>
          <td class="datatext"><h:outputText value="#{MicSeriesBean.series.summary}" /></td>
	  </tr>
          <tr>
	  <td class="plaintext">Type:</td>
          <td class="datatext"><h:outputText value="#{MicSeriesBean.series.type}" /></td>
	  </tr>
          <tr>
	  <td class="plaintext">Overall Design:</td>
          <td class="datatext"><h:outputText value="#{MicSeriesBean.series.design}" /></td>
	  </tr>
	  
        </table>
        </td>
      </tr>
      </table>
      <table border="0" width="100%">
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr>
        <td>

        <h:form>
        <h:dataTable width="100%" value="#{MicSeriesBean.series.summaryResults}" var="series" styleClass="browseTable" rowClasses="table-nostripe,table-stripey" headerClass="table-stripey">
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Samples" />
            </f:facet>
            <h:outputText value="#{series[0]}" styleClass="plaintext"/>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="GUDMAP ID" />
            </f:facet>
            <h:outputLink styleClass="plaintext" id="submissionID" value="mic_submission.html">  
              <h:outputText value="#{series[1]}"/>
              <f:param name="id" value="#{series[1]}"/>
            </h:outputLink>          
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="GEO ID" />
            </f:facet>
			<h:outputLink value="http://www.ncbi.nlm.nih.gov/projects/geo/query/acc.cgi?acc=#{series[2]}" styleClass="plaintext">
            <h:outputText value="#{series[2]}" />
            </h:outputLink>            
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Sample ID" />
            </f:facet>
            <h:outputText value="#{series[3]}" styleClass="plaintext"/>
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText styleClass="plaintextbold" value="Sample Description" />
            </f:facet>
            <h:outputText value="#{series[4]}" styleClass="plaintext"/>
          </h:column>
        </h:dataTable>          
       </h:form>
	</td>
      </tr>
   </table>
   </h:form>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>



