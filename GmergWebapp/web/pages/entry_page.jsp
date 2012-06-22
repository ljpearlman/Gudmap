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
        <script type="text/javascript">
  var numComps = 0;
  function componentList(id, text, tag_id) {

    var components = document.getElementById('compList');
    if(document.getElementById(tag_id)) {
      singlecomp = document.getElementById(tag_id);
      components.removeChild(singlecomp);
      numComps--;
    }
    else{
      components.innerHTML += '<div class="plaintext" id="'+tag_id+'">'+text+'<input type="hidden" name="component" value="'+id+'" /></div>';
      numComps++;
    }

    queryForm = document.forms['pronephQForm']
    queryFormId = queryForm.id;
    submitbuttonId = queryFormId + ":submitbutton";
    submitButton = queryForm[submitbuttonId];

    if(numComps < 1) {
      submitButton.disabled = true;
    }
    else {
      submitButton.disabled = false;
    }
  }

  function displayComponent(comp_name){
    var selectedComp = document.getElementById('selComponent');
	selectedComp.innerHTML = comp_name;

  }

  function hideComponent() {
    var selectedComp = document.getElementById('selComponent');
	selectedComp.innerHTML = "none";
  }
  
  function showGenePanel(e) {
		    document.getElementById(document.forms['geneQForm'].id+":genePanel").style.visibility = 'visible';
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'visible';
		}
      
		function hideGenePanel(e) {
		document.getElementById(document.forms['geneQForm'].id+":genePanel").style.visibility = 'hidden';
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'hidden';
		}
                
   function showPronephrosPanel(e) {
     document.getElementById(document.forms['pronephQForm'].id+":pronephrosPanel").style.visibility = 'visible';
     
   }
      
   function hidePronephrosPanel(e) {
     document.getElementById(document.forms['pronephQForm'].id+":pronephrosPanel").style.visibility = 'hidden';
     
   }
   
   function changeSearchButtonStatus(e) {
     alert("here");
   }
</script>
</head>

<f:view>
  <jsp:include page="/includes/header.jsp" />	
<h:outputText value="#{EntryPageBean.databaseServer}" rendered="#{proj == 'GUDMAP'}"/>
  <h:panelGrid styleClass="header-stripey" style="border:1px solid #6BADC5" columns="2" width="100%" cellpadding="2" cellspacing="2" columnClasses="topAlign50" rendered="#{siteSpecies == 'Xenopus laevis'}">
  
    <h:panelGroup>
    
    <h:panelGrid columns="3" border="0" cellpadding="0" cellspacing="0" columnClasses="centerAlign">
        <h:graphicImage value="../images/query.gif" alt="" />
        <h:graphicImage value="../images/1level_bridge_noa.gif" alt="" />
        <h:panelGrid styleClass="borderedSection">
          <h:form id="geneQForm">
            <h:panelGrid>
              <h:panelGroup><h:message for="geneSymbol" styleClass="plainred" /><f:verbatim>&nbsp;</f:verbatim></h:panelGroup>
              <h:panelGrid columns="4">
                <h:graphicImage value="../images/gene.gif" alt="" />
                  <h:inputText id="geneSymbol" value="#{QueryBean.geneSymbol}" size="12" />
                  <h:panelGroup>
                  <h:outputLink styleClass="plaintext" value="#" onclick="showGenePanel(event);return false;">      
                    <h:outputText value="options"/>								
                  </h:outputLink>
                  <h:panelGrid id="genePanel" width="200" border="1" cellspacing="0" cellpadding="0" bgcolor="white" 
							 style="position:absolute;visibility:hidden;z-index:100;text-align:right">
          
                    <h:panelGrid id="geneTableOuterFrame" width="200" cellspacing="2" cellpadding="2" bgcolor="white">
                      <h:panelGrid cellpadding="2" cellspacing="2">
                        <h:panelGroup>
                          <h:selectOneMenu id="inputType" value="#{QueryBean.inputType}">
                                <f:selectItem itemLabel="Gene Symbol" itemValue="symbol"/>
                                <f:selectItem itemLabel="Gene Name" itemValue="name"/>
                                <f:selectItem itemLabel="Gene Synonym" itemValue="synonym"/>
                                <f:selectItem itemLabel="All" itemValue="all"/>
                              </h:selectOneMenu>
                            </h:panelGroup>
                            <h:panelGroup>
              			  <h:outputText styleClass="plaintext" value="Gene "/>
              			  <h:selectOneMenu id="criteria" value="#{QueryBean.criteria}">
                                    <f:selectItem itemLabel="contains" itemValue="wildcard"/>
                                    <f:selectItem itemLabel="equals" itemValue="equals"/>
                                    <f:selectItem itemLabel="starts with" itemValue="begins"/>
                                  </h:selectOneMenu>
			  </h:panelGroup>
                          <h:panelGroup>
                            <h:outputText styleClass="plaintext" value="NF Stage "/>
              		    <h:selectOneMenu id="stage" value="#{QueryBean.stage}">
                              <f:selectItems value="#{AnatomyBean.availableStagesForQuery}"/>
                            </h:selectOneMenu></span><span class="plaintext">
                          </h:panelGroup>
                          <h:panelGroup>
              		    <h:selectOneMenu id="output" value="#{QueryBean.output}">
	                      <f:selectItem itemLabel="List all entries" itemValue="gene"/>
	                      <f:selectItem itemLabel="List annotated entries only" itemValue="anatomy"/>
                            </h:selectOneMenu>
                          </h:panelGroup>              
                        </h:panelGrid>
                        <h:outputLink value="#" onclick="hideGenePanel(event);return false;" styleClass="nav3" >
	                  <h:outputText value="Close " />
                        </h:outputLink>
                      </h:panelGrid>
                    </h:panelGrid>
                </h:panelGroup>
                <h:commandButton image="../images/go.gif" alt="Go" styleClass="icon" action="#{QueryBean.findGenes}" />
              </h:panelGrid>
            </h:panelGrid>
          </h:form>
          <h:form id="accessionQForm">
            <h:panelGrid>
              <h:panelGroup><h:message for="accession" styleClass="plainred" /><f:verbatim>&nbsp;</f:verbatim></h:panelGroup>
              <h:panelGroup><f:verbatim>&nbsp;</f:verbatim><h:outputText value="Enter ERGX ID:" styleClass="plaintext" /></h:panelGroup>
              <h:panelGrid columns="3">
                <h:graphicImage value="../images/id.gif" alt="" />
                <h:inputText id="accession" value="#{QueryBean.submissionID}"/>
                <h:commandButton image="../images/go.gif" styleClass="icon" alt="Go" action="#{QueryBean.submissionInfo}" />
              </h:panelGrid>
            </h:panelGrid>  
          </h:form>
          <h:form id="pronephQForm">
             <h:panelGrid styleClass="borderedSection">
               <h:outputLink value="#" onclick="showPronephrosPanel(event);return false;" styleClass="plaintext">
                 <h:graphicImage value="../images/pronephros_thumb.gif" alt="" styleClass="icon" /><f:verbatim><br /></f:verbatim><h:outputText value="Browse for Pronephros Gene Expression" />
               </h:outputLink>
               <h:panelGrid id="pronephrosPanel" border="1" cellspacing="0" cellpadding="0" bgcolor="white" 
							 style="position:absolute;top:465px;visibility:hidden;left:300px;z-index:100" >
          
                        <h:panelGrid id="pronephrosTableOuterFrame" cellspacing="2" cellpadding="2" bgcolor="white">
                        
                          <h:panelGrid cellpadding="2" cellspacing="2" width="100%" columns="1">
                            <h:panelGroup>
                              <h:outputText styleClass="plaintextbold" value="Search for gene expression patterns in selected components of the pronephros " />
                              <h:commandButton value="Search" id="submitbutton" action="#{AnatomyBean.annotSubsFromPronephBrowse}" />
                            </h:panelGroup>
                            
                            <h:panelGrid columns="2" columnClasses="topAlign">
                              <h:panelGroup>
                              <h:outputText value="Expression occurs in " styleClass="plaintext" />
                              <h:selectOneMenu value="#{AnatomyBean.criteria}" required="true" binding="#{AnatomyBean.criteriaInput}">
                                <f:selectItem itemLabel="all" itemValue="all"/>
                                <f:selectItem itemLabel="any" itemValue="any"/>
                              </h:selectOneMenu>
                              <h:outputText value=" selected components. " styleClass="plaintext" />
                              <h:outputText value="Expression is:" styleClass="plaintext" />
                              </h:panelGroup>
                              <h:selectManyCheckbox id="annotTypes" required="true" value="#{AnatomyBean.expressionTypes}" styleClass="plaintext" layout="lineDirection" binding="#{AnatomyBean.expressionInput}" >
                                <f:selectItems value="#{AnatomyBean.annotationVals}" />
                              </h:selectManyCheckbox>
                            </h:panelGrid>
                            
                            <h:panelGrid columns="2" columnClasses="topAlign">
                              <h:panelGrid>
                                <h:panelGroup>
                                <h:outputText styleClass="plaintextbold" value="Current Substructure: " />
                                <f:verbatim>
                                  <span class="plainred" id="selComponent">none</span>
                                </f:verbatim>
                                </h:panelGroup>
                                <h:graphicImage styleClass="icon" ismap="true" usemap="#components" value="../images/xenopus_pronephros.png" />
                              </h:panelGrid>
                              <h:panelGrid>
                                <f:verbatim>
                                  <script type="text/javascript">
                                    
                                    queryFormI = document.forms['pronephQForm'];
                                    queryFormId = queryFormI.id;
                                    
                                    submitbuttonId = queryFormId + ":submitbutton";
                                    
                                    submitButton = queryFormI[submitbuttonId];
                                    
                                    submitButton.disabled = true;
                                    
                                    for(i=0;i<queryFormI.elements.length;i++){
                                        if(queryFormI.elements[i].value.indexOf('present') >=0) {
                                        queryFormI.elements[i].checked = true;
                                        }
                                        
                                    }
                                    
                  
                                  </script>
                                  <div id="compList" class="plaintextbold">
                                    Selected Components:<br/><br />
                                  </div>
                                </f:verbatim>
                              </h:panelGrid>
                            </h:panelGrid>
                            
                            
                            
                            
                          
                          
                        </h:panelGrid>
                        <h:outputLink value="#" onclick="hidePronephrosPanel(event);return false;" styleClass="nav3" >
	                  <h:outputText value="Close " />
                        </h:outputLink>
                      </h:panelGrid>
                    </h:panelGrid>
                    <f:verbatim>
                    <map id="components" name="components">
                     <area alt="" shape="poly" coords="335,117,334,136,389,142,438,143,495,134,528,124,548,116,544,100,511,112,455,123,403,126,379,122,362,121,343,119" href="#" onclick="componentList('141', 'connecting tubule', 'comp1');return false;" onmouseover="displayComponent('connecting tubule')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="195,116,194,109,193,97,261,106,334,118,336,126,334,135,327,136,314,133,280,128,225,121" href="#" onclick="componentList('132', 'distal tubule 2', 'comp2');return false;" onmouseover="displayComponent('distal tubule 2')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="192,98,195,108,193,115,156,109,144,111,129,114,116,120,110,101,119,92,135,89,150,87,170,93,180,95" href="#" onclick="componentList('123', 'distal tubule 1', 'comp3');return false;" onmouseover="displayComponent('distal tubule 1')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="109,103,116,123,108,135,100,154,92,162,82,169,70,174,58,174,61,163,62,153,82,147,93,131,101,114" href="#" onclick="componentList('105', 'intermediate tubule 2', 'comp4');return false;" onmouseover="displayComponent('intermediate tubule 2')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="60,152,59,165,56,174,39,166,26,156,16,144,15,131,22,116,33,104,49,100,60,104,64,121,74,121,77,114,91,114,91,124,81,137,64,143,53,139,46,130,41,123,36,133,43,145" href="#" onclick="componentList('96', 'intermediate tubule 1', 'comp5');return false;" onmouseover="displayComponent('intermediate tubule 1')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="95,110,85,113,74,112,66,102,64,88,71,79,82,77,94,81,100,89,97,100" href="#" onclick="componentList('78', 'proximal tubule 3', 'comp6');return false;" onmouseover="displayComponent('proximal tubule 3')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="92,81,84,77,76,78,70,79,83,63,71,58,64,65,59,56,59,50,68,45,75,42,87,46,93,49,100,52,102,55,103,61,99,67,94,73" href="#" onclick="componentList('69', 'proximal tubule 2', 'comp7');return false;" onmouseover="displayComponent('proximal tubule 2')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="63,66,57,74,51,81,44,83,39,76,44,69,49,65,52,60,58,56,62,61" href="#" onclick="componentList('60', 'proximal tubule 1', 'comp8');return false;" onmouseover="displayComponent('proximal tubule 1')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="55,38,62,35,70,42,63,46,57,51,54,44" href="#" onclick="componentList('60', 'proximal tubule 1', 'comp8');return false;" onmouseover="displayComponent('proximal tubbule 1')" onmouseout="hideComponent()" />
                     <area alt="" shape="poly" coords="104,54,115,62,119,70,113,78,108,71,102,68,102,62" href="#" onclick="componentList('60', 'proximal tubule 1', 'comp8');return false;" onmouseover="displayComponent('proximal tubule 1')" onmouseout="hideComponent()" />
                     
                    </map>
                    </f:verbatim>
             </h:panelGrid>
          </h:form>
        </h:panelGrid>
      </h:panelGrid>
      
      <f:verbatim>
        <br />
        <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                  <td rowspan="4" valign="middle"><img src="../images/browse.gif" alt="" /></td>
                  <td rowspan="4"><img src="../images/1level_bridge_noa.gif" alt="" /></td>
                </tr>
                <tr>
                  <td><img src="../images/tree_arrow_top_noa.gif" alt="" /></td>
                  <td><a href="ish_browse.html?browseId=browseAll"><img src="../images/assay.gif" alt="" border="0" /></a></td>
                </tr>
                <tr>
                  <td><img src="../images/tree_arrow_pipe_noa.gif" alt="" height="50" width="50" /></td>
                  <td><img src="../images/spacet.gif" width="1" height="5" alt="" border="0" /></td>
                </tr>
                <tr>
                  <td><img src="../images/tree_arrow_but_noa.gif" alt="" /></td>
                  <td><a href="anatomy_tree.html"><img src="../images/anatomy.gif" alt="" border="0" /></a></td>
                </tr>
              </table>
              <br /><br />
      </f:verbatim>
      
      <h:panelGrid columns="2" style="border:2px solid #6BADC5;background-color:#ffffff;" cellpadding="2" width="100%" cellspacing="2" columnClasses="topAlign">
    
      <h:panelGrid columns="1">
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="Total Genes (in situ): " />
          <h:outputText value="#{EntryPageBean.numPublicGenes}" styleClass="datatext" />
        </h:panelGroup>
        <h:outputText styleClass="plaintext" value="Submissions:" />
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="ISH: " />
          <h:outputText value="#{EntryPageBean.numPubISHSubmissions}" styleClass="datatext" />
        </h:panelGroup>
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="IHC: " />
          <h:outputText value="#{EntryPageBean.numerOfPublicSubmissionsIHC}" styleClass="datatext" />
        </h:panelGroup>
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="Microarray: " />
          <h:outputText value="#{EntryPageBean.numPubArraySubmissions}" styleClass="datatext" />
        </h:panelGroup>
      </h:panelGrid>
    
      <h:panelGrid columns="1">
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="Last Ediorial Update: " />
          <h:outputText value="#{EntryPageBean.lastEditorialUpdate}" styleClass="datatext" />
        </h:panelGroup>
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="Last Software Update: " />
          <h:outputText value="#{EntryPageBean.lastSoftwareUpdate}" styleClass="datatext" />
        </h:panelGroup>
        <h:panelGroup>
          <h:outputText styleClass="plaintext" value="Last Entry Date: " />
          <h:outputText value="#{EntryPageBean.lastEntryDate}" styleClass="datatext" />
        </h:panelGroup>
      </h:panelGrid>
      
    </h:panelGrid>
    
    </h:panelGroup>
    
    <h:panelGroup>
    
    <h:panelGrid columns="3" style="border:2px solid #6BADC5;background-color:#ffffff;text-align:center" cellpadding="3" width="100%" cellspacing="3" columnClasses="topAlign">  
    
        <h:outputLink value="about.html" styleClass="plaintext">
          <h:outputText value="About XGEbase" />
        </h:outputLink>
        
        <h:outputLink value="requirement.html" styleClass="plaintext">
          <h:outputText value="System Requirements" />
        </h:outputLink>
        
        <h:outputLink value="xge_acknowledgments.html" styleClass="plaintext">
          <h:outputText value="Acknowledgements" />
        </h:outputLink>


    </h:panelGrid>
    
    <f:verbatim><br/></f:verbatim>
    
    <h:panelGrid styleClass="borderedSection" width="100%">
      <h:panelGrid style="margin-left:auto;margin-right:auto;text-align:center">
      <h:graphicImage value="#{EntryPageBean.xenopusImg}" style="border: 1px solid #6BADC5" alt="" />
      <h:outputText styleClass="datatext" value="movie of a stage 35/36 X. laevis embryo stained for SLC12A1 by whole-mount in situ hybridization." />
      <h:form>
        <h:commandLink styleClass="plaintext" actionListener="#{EntryPageBean.changeMovieStatus}">
          <f:param value="#{EntryPageBean.xenMovButtonTxt}" name="imgTxt" />
          <h:graphicImage styleClass="icon" value="#{EntryPageBean.xenMovButtonImg}" alt="" />
          <h:outputText value="#{EntryPageBean.xenMovButtonTxt}" />
        </h:commandLink>
      </h:form>
      </h:panelGrid>
      <h:graphicImage value="../images/SLC12A1_35-36_thumb.jpg" style="border: 1px solid #6BADC5" alt="" />
      <h:outputText styleClass="datatext" value="SLC12A1 expression in X.laevis at NF35-36." />
    </h:panelGrid>  
    
    <f:verbatim><br/></f:verbatim>
    
    <h:panelGrid style="text-align:right;border:2px solid #6BADC5;background-color:#ffffff;padding:2px;vertical-align:top;" columns="2" columnClasses="topAlign">
        <h:graphicImage value="../images/FP6.gif" alt="" />
        <h:outputText styleClass="plaintext" value="Integrated Project funded by the European Community, Framework Programme 6" />
      </h:panelGrid>
    
    </h:panelGroup>
    
  </h:panelGrid>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>



