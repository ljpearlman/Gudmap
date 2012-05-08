<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<head>
    <link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
<!--     <link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
 -->
 <script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
</head>
<body class="stripey" >
<f:view>
<f:verbatim>  
    	<table border="0" width="100%" cellspacing="0" cellpadding="0"> 
        <tr class="header-stripey">        
        </f:verbatim>
        <h:form>
        	
        
		<t:dataList id="labs1"
               styleClass="plaintextbold"
               var="stage"
               value="#{imageMatrixBean.stages}"
               layout="simple"
               rowCountVar="count"
               rowIndexVar="index">
               <f:verbatim>
               <td>
               </f:verbatim>
					<h:outputText value="#{stage}"/>
			   <f:verbatim>
               </td>
               </f:verbatim>
         </t:dataList>
        <f:verbatim>
               </tr>
               
        </f:verbatim> 
  		<t:dataList id="labs2"
               styleClass="plaintextbold"
               var="image"
               value="#{imageMatrixBean.images}"
               layout="simple"
               rowCountVar="count2"
               rowIndexVar="index2">
               	<f:verbatim>
        			<tr class="header-stripey">        
        		</f:verbatim>      
           <t:dataList id="labs3"
               styleClass="plaintextbold"
               var="cell"
               value="#{image}"
               layout="simple"
               rowCountVar="count3"
               rowIndexVar="index3">
               <f:verbatim>
        			<td  valign="top" align="left"> 
        			     
        	   </f:verbatim>  
        	   <h:panelGrid columns="1"> 
        	   		<h:selectBooleanCheckbox value="#{cell[4]}" rendered="#{cell[0]}"/>
               		<h:outputLink rendered="#{cell[0]}" id="thumbnail" value="#" onclick="openZoomViewer('#{cell[2]}', 'desktop1', '1');return false;">
							<h:graphicImage styleClass="icon" value="#{cell[1]}" width="200"/>
					</h:outputLink>					
			   <h:outputLink rendered="#{cell[0]}" id="id" value="#" onclick="window.open('ish_submission.html?id=#{cell[2]}');return false;">
			   		<h:outputText value="#{cell[2]}" />
			   </h:outputLink>	
<%-- 
			   <h:outputLink rendered="#{cell[0]}" id="stage" value="#" onclick="window.open('http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/#{cell[6]}/');return false;">	
--%>
			   <h:outputLink rendered="#{cell[0]}" id="stage" value="#" onclick="window.open('http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/#{cell[6]}/definition.html');return false;">	
			   		<h:outputText value="#{cell[3]}" />
			   </h:outputLink>
			   </h:panelGrid>
			   <f:verbatim>
        			</td>   
        			 
        	   </f:verbatim>   		
			</t:dataList>
			
			<f:verbatim>
        		</tr>
        		<tr>        
            </f:verbatim>   	
        </t:dataList>
        <f:verbatim>
        		</tr>
        		<tr>        
            </f:verbatim>
        <h:commandLink id="addCollectionButton" action="#{imageMatrixBean.addToCollection}" >
        <h:outputText styleClass="plaintext" value="Add to image collection"/>														
	    </h:commandLink>
	    </h:form>
        <f:verbatim>
        </tr>
        </table>
        </f:verbatim> 
</f:view> 
</body>       