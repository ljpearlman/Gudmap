<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/tlds/components.tld" prefix="d" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<head>
	<script language="JavaScript">
		
		function onMouseOverImage(over) {
		    document.getElementById("form1:mapImage").src = over;
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'visible';
		}
      
		function onMouseOutImage(out) {
		document.getElementById("form1:mapImage").src = out;
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'hidden';
		}		
	</script>
</head>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  
  <table border="0" width="100%">
      <tr>
        <td colspan="2" align="left" width="98%"><h3>Development of the Mouse Urogenital System</h3></td>      
      </tr>
      <tr>
        <td colspan="2" align="left">Written overview of <a href="http://www.gudmap.org/About/Tutorial/">urogenital development</a>.</td>
      </tr>    
  </table>  


  <h:form id="form1">
  <table border="0" cellspacing="0" cellpadding="0"  class="table-stripey">
	<tr align="left">
		<td width="100%" valign="top" align="left">				
		
	    <h:panelGroup>
  	    <t:selectOneRadio id="radio1" layout="spread" forceId="true" forceIdIndex="false" value="#{ImageMap.selectedOrgan}">
    	    <f:selectItems value="#{ImageMap.organRadioList}" />
	      </t:selectOneRadio>

        <t:dataTable value="#{microarrayFocusBean.organs}" var="organ" rowIndexVar="index" headerClass="plaintextboldleft">
          <h:column>
            <f:facet name="header">
                <h:outputText value="Step 1: Click on an organ to see tutorial and list of results."/>             
            </f:facet>

		          <f:verbatim>
				  <table border="0" width="100%">
				      <tr>
				        <td colspan="2" align="left">Graphical overview of </f:verbatim>
				        <h:outputLink value="#{organ[0]}">
						              <h:outputText value="#{organ[2]}"/>
						   </h:outputLink>
				       
				        <f:verbatim>
				         and associated projects:</td>
				      </tr> 
				      <tr>
				        <td colspan="2" align="left">
				        </f:verbatim>
				        <t:dataList id="labs"
				               styleClass="plaintextbold"
				               var="lab"
				               value="#{organ[1]}"
				               layout="unorderedList"
				               rowCountVar="count"
				               rowIndexVar="index1">
				           <h:outputLink styleClass="plaintextbold" value="#{lab[1]}">
						              <h:outputText value="#{lab[0]}"/>
						   </h:outputLink>
				        </t:dataList>
				        <f:verbatim>
				        </td>
				      </tr>   
				  </table>
				  </f:verbatim>
		 
            
 
                       
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

        
        
    
         
        
     </tr>
	      <tr>
	     	<td align="left"  class="table-stripey">
	     		<h:selectOneMenu value="#{ImageMap.operation}">
	              <f:selectItem itemLabel="Select substructure from diagram" itemValue="showDiagram" /> 
	              <f:selectItem itemLabel="Add to My Anatomy Collection" itemValue="addToCollection" /> 
	            </h:selectOneMenu> 
	        <h:commandButton value="GO" action="#{ImageMap.chooseOrgan}"/>        		     		
	     	</td>

	     	
	      </tr>
  </table>
  
  <table>
    <tr>
    	<td align="center" width="20%">			
  			<h:graphicImage id="downarrow" height="50"
                      url="../images/focus/downarrow.gif"/>
     	</td>
        <td align="center" width="80%">			

     	</td>     	
     </tr>
  </table>
  

  
  <table class="table-stripey">
    <tr><td align="left">
      <h:outputText value="Step 2: Select substructure from diagram:" styleClass="plaintextbold"/>
    </td></tr>
    
    <tr><td align="left">
      
      <h:dataTable
		            value="#{ImageMap.termList}" var="collection">
		          <h:column>
		            <f:facet name="header">
		                <h:outputText value="" styleClass="plaintextbold"/>             
		            </f:facet>
		            <h:outputLink styleClass="plaintext" value="organ_description.jsf" onmouseover="onMouseOverImage('#{collection[1]}');return false;" onmouseout="onMouseOutImage('#{collection[2]}');return false;">
		              <h:outputText value="#{collection[0]}"/>
		              <f:param name="component" value="#{collection[0]}"/>
		            </h:outputLink>
		            
		          </h:column>		          		                    
		        </h:dataTable>                              
    </td></tr>

    <tr><td>
      <h:graphicImage id="mapImage"
                      url="#{ImageMap.diagramURLThree}"
                   usemap="#worldMap"/>
      
      <d:map           id="worldMap"
        actionListener="#{ImageMap.processAreaSelected}" 
                immediate="false">
        <d:area        id="area1"
                 value="#{ImageMap.areaList[0]}"
              onmouseover="#{ImageMap.diagramURLThree}"
               onmouseout="#{ImageMap.diagramURLThree}"
               targetImage="form1:mapImage"/>

        <d:area        id="area2"
                 value="#{ImageMap.areaList[1]}"
              onmouseover="#{ImageMap.diagramURLThree}"
               onmouseout="#{ImageMap.diagramURLThree}"
               targetImage="form1:mapImage"/>
        <d:area        id="area3"
                 value="#{ImageMap.areaList[2]}"
              onmouseover="#{ImageMap.diagramURLThree}"
               onmouseout="#{ImageMap.diagramURLThree}"
               targetImage="form1:mapImage"/>                      
      </d:map>
      
    </td></tr>

  </table>
</h:form>
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>