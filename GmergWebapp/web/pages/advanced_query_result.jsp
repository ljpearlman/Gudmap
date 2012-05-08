<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>
  <jsp:include page="/includes/header.jsp" />
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><h3>Advanced Database Query Page</h3></td>
      <td align="right"></td>
    </tr>

      <tr>
      <td>
        <h:form id="form1">
        <h:panelGrid width="100%" columns="2" columnClasses="align-top-65, align-top-35">
          <h:panelGrid width="100%" columns="1">
            
              <h:panelGrid width="100%" columns="3">           
                <t:outputText value="Search:"/>   
                <t:selectOneRadio id="radio1" forceId="true" forceIdIndex="false" layout="lineDirection" value="#{advancedQueryBean.selectedStepOne}">
    	          <f:selectItems value="#{advancedQueryBean.stepOne}" />
	        </t:selectOneRadio>      
	        <h:commandButton value="GO" action="#{advancedQueryBean.advancedQuery}" />		        
              </h:panelGrid>
            
            
            
            
            
            
              <h:panelGroup>
              <t:dataList id="boths" styleClass="plaintextbold" var="both" value="#{advancedQueryBean.both}" layout="simple" rowCountVar="count" rowIndexVar="index">
                <h:panelGrid width="50%" columns="2" rendered="#{!advancedQueryBean.renderedMicroarray||!advancedQueryBean.renderedISH}">
                  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{!both[1]}">        
		    <h:graphicImage url="../images/min-box.gif" alt="minus"  styleClass="icon"/>
		    <f:param value="#{both[3]}" name="section" />
		    <f:param value="Y" name="expand" />
		  </h:commandLink>
		  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{both[1]}">        
		    <h:graphicImage url="../images/plus-box.gif" alt="plus"  styleClass="icon"/>
		    <f:param value="#{both[3]}" name="section" />
		    <f:param value="N" name="expand" />
		  </h:commandLink>
		  <t:outputText value="#{both[2]}"/> 
                </h:panelGrid>
                <h:panelGrid width="80%" columns="2" border="1" rules="groups" cellspacing="0" cellpadding="2" rendered="#{!both[1]&&(!advancedQueryBean.renderedMicroarray||!advancedQueryBean.renderedISH)}">
		  <t:dataList id="items" styleClass="plaintextbold" var="item" value="#{both[0]}" layout="simple" rowCountVar="count" rowIndexVar="index">
		    <h:panelGrid width="100%" columns="2" columnClasses="align-left,align-right" rendered="#{!both[1]&&(!advancedQueryBean.renderedISH||!advancedQueryBean.renderedMicroarray)}">
		      <h:outputText value="#{item[0]}" />
                      <h:commandLink action="#{advancedQueryBean.addOption}">
                        <h:outputText value="Add" />
                        <f:param value="#{item[1]}" name="name" />
                      </h:commandLink>
		    </h:panelGrid>
		  </t:dataList>
	        </h:panelGrid>
              </t:dataList>

              <t:dataList id="ishs" styleClass="plaintextbold" var="ish" value="#{advancedQueryBean.ish}" layout="simple" rowCountVar="count" rowIndexVar="index">
                <h:panelGrid width="50%" columns="2" rendered="#{!advancedQueryBean.renderedISH}">
                  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{!ish[1]}">        
		    <h:graphicImage url="../images/min-box.gif" alt="minus"  styleClass="icon"/>
		    <f:param value="#{ish[3]}" name="section" />
		    <f:param value="Y" name="expand" />
		  </h:commandLink>
		  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{ish[1]}">        
		    <h:graphicImage url="../images/plus-box.gif" alt="plus"  styleClass="icon"/>
		    <f:param value="#{ish[3]}" name="section" />
		    <f:param value="N" name="expand" />
		  </h:commandLink>
		  <t:outputText value="#{ish[2]}"/> 
                </h:panelGrid>
                <h:panelGrid width="80%" columns="1" border="1" rules="groups" cellspacing="0" cellpadding="2" rendered="#{!ish[1]&&!advancedQueryBean.renderedISH}">
                  <t:dataList id="items" styleClass="plaintextbold" var="item" value="#{ish[0]}" layout="simple" rowCountVar="count" rowIndexVar="index">
		    <h:panelGrid width="100%" columns="2" columnClasses="align-left,align-right" rendered="#{!ish[1]&&!advancedQueryBean.renderedISH}">
                      <h:outputText value="#{item[0]}" />
                      <h:commandLink action="#{advancedQueryBean.addOption}">        						        
                        <h:outputText value="Add" /> 
                        <f:param value="#{item[1]}" name="name" />						  		
		      </h:commandLink>
		    </h:panelGrid>
		  </t:dataList>	
		</h:panelGrid>	
	      </t:dataList>								
	  	        
              <t:dataList id="mics"  styleClass="plaintextbold"  var="mic"  value="#{advancedQueryBean.mic}" layout="simple" rowCountVar="count" rowIndexVar="index">
                <h:panelGrid width="50%" columns="2" rendered="#{!advancedQueryBean.renderedMicroarray}">
                  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{!mic[1]}">        
		    <h:graphicImage url="../images/min-box.gif" alt="minus"  styleClass="icon"/>
                    <f:param value="#{mic[3]}" name="section" />
		    <f:param value="Y" name="expand" />
		  </h:commandLink>
		  <h:commandLink action="#{advancedQueryBean.changeProbe}" rendered="#{mic[1]}">        
		    <h:graphicImage url="../images/plus-box.gif" alt="plus"  styleClass="icon"/>
		    <f:param value="#{mic[3]}" name="section" />
		    <f:param value="N" name="expand" />
		  </h:commandLink>
		  <t:outputText value="#{mic[2]}"/> 
                </h:panelGrid>
                <h:panelGrid width="80%" columns="2" border="1" rules="groups" cellspacing="0" cellpadding="2" rendered="#{!mic[1]&&!advancedQueryBean.renderedMicroarray}">
		  <t:dataList id="items" styleClass="plaintextbold" var="item" value="#{mic[0]}" layout="simple" rowCountVar="count" rowIndexVar="index">
                    <h:panelGrid width="100%" columns="2" columnClasses="align-left,align-right" rendered="#{!mic[1]&&!advancedQueryBean.renderedMicroarray}">
                      <h:outputText value="#{item[0]}" />						
                      <h:commandLink action="#{advancedQueryBean.addOption}">  
                        <h:outputText value="Add" />       						        
		        <f:param value="#{item[1]}" name="name" />						  		
		      </h:commandLink>												
                    </h:panelGrid>
		  </t:dataList>
                </h:panelGrid>
              </t:dataList>
            </h:panelGroup>
          </h:panelGrid>
          
          
            <h:panelGroup>
              <f:verbatim> 
    	        <table border="1" width="100%" cellspacing="0" cellpadding="2" rules="groups"> 
                  <tr>
                    <td>   
              </f:verbatim>
      	      <h:outputText value="My Query" />
      	      <f:verbatim>
      	            </td>
      	          </tr>
      	      </f:verbatim>
              
      	      <t:dataList id="labs" styleClass="plaintextbold" var="lab" value="#{advancedQueryBean.collection}" layout="simple" rowCountVar="count" rowIndexVar="index">
                <f:verbatim>
	          <tr>
	      	    <td>     
	      	</f:verbatim>	
	      	<h:outputText value="#{lab[0]}" />
                <t:dataList id="ops" styleClass="plaintextbold" var="op" value="#{lab[1]}" layout="simple" rowCountVar="count1" rowIndexVar="index1">
	          <h:commandLink action="#{advancedQueryBean.removeCollection}">  
                    <h:outputText value="Remove" />       						        
                    <f:param value="#{lab[0]}" name="collection" />						  		
		  </h:commandLink>
		  <f:verbatim>
		      </td>
	      	    </tr>
	      	    <tr>
	      	    <td>
	          </f:verbatim>
	          <h:selectOneMenu rendered="#{op[3]}" value="#{op[1]}">
                    <f:selectItems value="#{op[4]}"/>               
	          </h:selectOneMenu>
	          <h:inputText rendered="#{op[2]}" value="#{op[0]}"/>
	          <h:inputTextarea rendered="#{op[5]}" value="#{op[6]}"/>
	          <h:outputText rendered="#{op[3]&&op[2]&&index1<count1-1}" value="AND" />
                </t:dataList> 		   
                <f:verbatim> 
	            </td>
	      	  </tr>
	        </f:verbatim>
	      </t:dataList> 	
	      <f:verbatim>	      	
	        <tr>
	          <td> 
	      </f:verbatim>
	           
	      <h:outputText value="Output" />
	      <t:selectOneRadio id="radio2" forceId="true" forceIdIndex="false" layout="lineDirection" value="#{advancedQueryBean.selectedOutput}">
	        <f:selectItem itemLabel="Gene List" itemValue="gene" />
	      </t:selectOneRadio>
	      <h:commandLink action="#{advancedQueryBean.startSearch}">        		        		  
                <h:outputText value="Search" />
              </h:commandLink>
              <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
              <h:commandLink action="#{advancedQueryBean.removeAll}">        		        		  
                <h:outputText value="Remove All" />
              </h:commandLink>
              <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
              <h:commandLink action="#{advancedQueryBean.clearAllValues}">        		        		  
                <h:outputText value="Clear All" />
              </h:commandLink>
              <f:verbatim>
                    </td>
	          </tr>
      	        </table>
      	      </f:verbatim>
      	    </h:panelGroup>
          
        </h:panelGrid>
        </h:form>
      </td>
    </tr>      
  </table>
<jsp:include page="/includes/footer.jsp" />
</f:view>