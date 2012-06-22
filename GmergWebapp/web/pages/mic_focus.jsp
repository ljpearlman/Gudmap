<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/tlds/components.tld" prefix="d" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />   


  <h:form id="form1">
  <table border="0" cellspacing="0" cellpadding="0"  class="table-stripey">
	<tr align="left">
		<td width="50%" valign="top" align="left">				
		
	    <h:panelGroup>
  	    <t:selectOneRadio id="radio1" layout="spread" forceId="true" forceIdIndex="false" value="#{ImageMap.selectedOrgan}">
    	    <f:selectItems value="#{ImageMap.organRadioList}" />
	      </t:selectOneRadio>

        <t:dataTable value="#{ImageMap.submissions}" var="submission" rowIndexVar="index" headerClass="plaintextboldleft">
          <h:column>
            <f:facet name="header">
                <h:outputText value="Step 1: Click on an organ to see tutorial and list of results."/>             
            </f:facet>
            <h:outputLink styleClass="plaintext" value="processed_genelists.jsf" rendered="#{submission[4]}">
              <h:outputText value="#{submission[0]}"/>
              <f:param name="component" value="#{submission[1]}"/>
              <f:param name="lab" value="#{submission[3]}"/>
            </h:outputLink>
            <h:outputText styleClass="plaintext" value="#{submission[0]}" rendered="#{!submission[4]}"/>
            <h:outputLink styleClass="plaintext" value="organ_description.jsf">
              <h:outputText value="(Summary)"/>
              <f:param name="component" value="#{submission[1]}"/>
            </h:outputLink>            
          </h:column>
          <h:column>
            <f:facet name="header">
                <h:outputText value="Build an anatomy collection for display and analysis."/>             
            </f:facet>
          	<t:radio for=":form1:radio1" index="#{index}" />
          </h:column>
        </t:dataTable>
  	  </h:panelGroup>
    </td>

        <td width="50%" align="right" valign="top">
        
        <table>
          <tr>
            <td align="right">
            <h:outputText value="My Anatomy Collection" styleClass="plaintextbold"/>
            </td>
          </tr>
          <tr>
            <td align="right">  
		        <h:dataTable
		            value="#{CollectionAnatomyBean.collection}" var="collection">
		          <h:column>
		            <f:facet name="header">
		                <h:outputText value="" styleClass="plaintextbold"/>             
		            </f:facet>
		            <h:selectBooleanCheckbox id="checkbox1" value="#{collection.selected}"/>
		          </h:column>
		          <h:column>
		            <f:facet name="header">
		                <h:outputText value="" styleClass="plaintextbold"/>             
		            </f:facet>
		            <h:outputText value="#{collection.organName}" styleClass="plaintextbold"/> 
		          </h:column>		
                  <h:column>
		            <f:facet name="header">
		                <h:outputText value="" styleClass="plaintextbold"/>             
		            </f:facet>
		            <h:outputLink styleClass="plaintextbold" value="processed_genelists.jsf" rendered="#{collection.rendered}">
		              <h:outputText value="(View)"/>
		              <f:param name="component" value="#{collection.organName}"/>
		              <f:param name="lab" value="#{collection.view}"/>
		            </h:outputLink>
		          </h:column>
                  <h:column>
		            <f:facet name="header">
		                <h:outputText value="" styleClass="plaintextbold"/>             
		            </f:facet>
                    <h:outputLink styleClass="plaintextbold" value="#{collection.download}">
		              <h:outputText value="(Download)"/>
		            </h:outputLink>		            
		          </h:column>		          		                    
		        </h:dataTable>                            
            </td>
          </tr> 
          <tr>
          <td align="right">
            <h:commandButton value="Delete From Collection" action="#{ImageMap.deleteCollection}"/>
     		<h:commandButton value="  Empty My Collection   " action="#{ImageMap.emptyCollection}"/> 
     	  </td>
     	  </tr>
     	  
        </table> 
         
        </td>
     </tr>
	      <tr>
	     	<td align="left"  class="table-stripey">
	     		<h:selectOneMenu value="#{ImageMap.operation}">
	              <f:selectItem itemLabel="Select substructure from diagram" itemValue="showDiagram" /> 
	              <f:selectItem itemLabel="Add to My Anatomy Collection" itemValue="addToCollection" /> 
	            </h:selectOneMenu> 
	        <h:commandButton value="GO" action="#{ImageMap.chooseOrgan}"/>        		     		
	     	</td>
	     	<td class="table-stripey" align="left">
	     	
	     	</td>
	     	
	      </tr>
  </table>
  
  <table>
    <tr>
    	<td align="center" width="20%">			
  			<h:graphicImage id="downarrow" height="50"
                      url="../images/focus/downarrow.gif"/>
     	</td>
        <td align="center" width="20%">			
     	</td>     	
     </tr>
  </table>


  <table class="table-stripey">
    <tr><td align="left">
      <h:outputText value="Step 2: Select substructure from diagram:" styleClass="plaintextbold"/>
    </td></tr>
    <tr><td align="left">
      <h:outputText id="welcomeLabel" 
                                    value="Selected substructure: #{ImageMap.emapId}" />
    </td></tr>

    <tr><td>
      <h:panelGroup rendered="#{ImageMap.diagram}">
      </h:panelGroup>
      <h:graphicImage id="mapImage"
                      url="#{ImageMap.diagramURL}"
                   usemap="#worldMap"/>
      
      <d:map           id="worldMap"
        actionListener="#{ImageMap.processAreaSelected}" 
                immediate="false">
        <d:area        id="area1"
                 value="#{ImageMap.areaList[0]}"
              onmouseover="#{ImageMap.diagramURL}"
               onmouseout="#{ImageMap.diagramURL}"
               targetImage="form1:mapImage"/>

        <d:area        id="area2"
                 value="#{ImageMap.areaList[1]}"
              onmouseover="#{ImageMap.diagramURL}"
               onmouseout="#{ImageMap.diagramURL}"
               targetImage="form1:mapImage"/>
        <d:area        id="area3"
                 value="#{ImageMap.areaList[2]}"
              onmouseover="#{ImageMap.diagramURL}"
               onmouseout="#{ImageMap.diagramURL}"
               targetImage="form1:mapImage"/>                      
      </d:map>
      
    </td></tr>

  </table>
</h:form>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>