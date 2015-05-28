<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
	<link href="${pageContext.request.contextPath}/scripts/jstree_pre1.0_fix_1/themes/gudmap/my_jstree_style.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/scripts/jstree_pre1.0_fix_1/themes/gudmap/tooltip_style.css" type="text/css" rel="stylesheet" />


	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/jquery.jstree.js"></script> 

<style>
  #exp_key_div a {
    font-size: 8pt;
    color: #7F7F81;
  }
  .expression_btn, .pattern_btn{
    pointer-events: none;
    cursor: default;
  }

  #demo2_view{
    cursor: default;
    background-color: #EEF2FA;
  }

  #demo2_view a{
    cursor: default;
  }
</style>

<script type="text/javascript">

jQuery(document).ready(function(){
    jQuery("#demo2_view").jstree({
        "themes": {
          "theme": "default",
          "line": true,
          "dots" : true,
          "icons": true,
		  "url" : "../scripts/jstree_pre1.0_fix_1/themes/anatomytree/style3.css"
        },
        "ui" : {
          //"select_limit" : -1,
          "select_limit" : 0,
          //"select_multiple_modifier" : "ctrl",
          //"select_range_modifier" :"shift",
          //selected_parent_open
          "selected_parent_close" : "select_parent"
        },
        "json_data" : {
          "progressive_render" : false,
          "selected_parent_open": true,
          "ajax" : {
        	  "data": function (n) { return { id: n.attr ? n.attr("id") : 0} },
          	"url" : "../scripts/annotation_tree_json/abstract.json"
         }
        },
        "search" : {
        	"case_insensitive" : true, 
        	"ajax" : {
        		"url" : "../scripts/annotation_tree_json/abstract.json" 
        	}
        },
        "plugins" : [ "themes", "json_data", "search", "ui", "crrm" ]
    })
    .bind("loaded.jstree", function (e, data) { 	
 	    jQuery("#demo2_view").jstree("open_node", "#0--0");   	 
    })
	.delegate("a", "click", function(e, data) {
	    var node = $(e.target).closest("li");
	    var data = node.data("jstree");
	    
		var ano_public_id = node.attr('ANO_PUBLIC_ID');
		var ano_comp_name = node.attr('ANO_COMPONENT_NAME');
		var apo_sequence = node.attr('APO_SEQUENCE');
		
		toggleParamGroup('',ano_comp_name,apo_sequence);
	});

}); 

function searchTree(v) {
//	alert("searchTree = " + v);
	jQuery("#demo2_view").jstree("search",v);
}

</script>	

</head>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  <p><h:message for="stagesCheck" styleClass="plainred" /></p>
  <p class="plaintextbold">
    Search gene expression patterns in selected anatomical components.
  </p>
  
  <p><h:outputText styleClass="plaintext" value="Current anatomy display is for stage range #{BooleanTestBean.startStage} to #{BooleanTestBean.endStage}."/></p>
  <h:panelGrid columns="3" cellpadding="2" cellspacing="2">
    <h:inputText id="componentSearchField" size="30" />
    <h:commandButton onclick="searchTree(document.getElementById('componentSearchField').value)" value="Find Components in Tree" type="button" />
    <f:verbatim>
	    <a href="#Link263891Context" name="Link263891Context" id="Link263891Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263891', 'Find Components in Tree', 'To find structures in the anatomy tree, expand the tree or type all or part of the name of the structure in the text box and click - \'Find components in tree\' box.   TS = Theiler Stage.', 'Link263891Context')"> 
	    	<img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" />
	    </a>
    </f:verbatim>
  </h:panelGrid>
  
  <h:panelGrid columns="2" width="100%" columnClasses="topAlign45,topAlign55">
  <h:panelGroup>
<%--   <f:verbatim><div style="overflow:auto;height:455px;width:100%;"></f:verbatim>--%>

    <h:message for="searchCriteriaCheck" styleClass="plainred" /><br />

    <h:panelGrid cellpadding="0" cellspacing="0" border="0" width="100%" columns="1" columnClasses="treeCol">
      <h:panelGrid columns="1" cellpadding="4" cellspacing="0" width="100%" border="0">
        <h:panelGrid columns="1" cellpadding="2" cellspacing="0" border="0" width="100%">
          <h:panelGroup>
            <h:panelGrid columns="2" border="0">
              <h:panelGroup>
                <f:verbatim>
                  <a href="#Link263892Context" name="Link263892Context" id="Link263892Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263892', 'Add/Remove Structure(s) to the Search', 'Click on a structure in the tree to add it to the Boolean Query Search. The selected component appears in red in the right-hand Boolean Search section. To correct a mistake, click again on the structure: this will remove it from the search.', 'Link263892Context')"> <img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" /></a>
                </f:verbatim>
              </h:panelGroup>
            </h:panelGrid>
              
<f:verbatim>
	<div id="demo2_view" class="demo" style="overflow: auto; align: left; height: 300px; width : 350px"></div>
</f:verbatim>             

          </h:panelGroup>
        </h:panelGrid>
      </h:panelGrid>
    </h:panelGrid>

  

  </h:panelGroup>
  <h:form id="booleanQForm">
    <h:panelGrid columns="3" cellpadding="2" cellspacing="2">
      <h:outputText value="Search for: " styleClass="plaintext"/>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="resultFormat" value="">
        <f:selectItems value="#{BooleanTestBean.resultFormat}" />
      </h:selectOneMenu>
      <h:panelGroup>
      <f:verbatim>
        <a href="#Link263893Context" name="Link263893Context" id="Link263893Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263893', 'Choose Search Result Option', 'Use the \'Search for\' options box to select either genes or GUDMAP Entries. The default query finds database entries that match the search criteria. The alternative option finds genes that individually match the search criteria. Note that a database entry is for one gene, one developmental stage, one sex.', 'Link263893Context')"> <img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" /></a>
      </f:verbatim>
      </h:panelGroup>
    </h:panelGrid>
    
    <h:panelGrid columns="3" cellpadding="2" cellspacing="2">
      <h:outputText value="Expression is:" styleClass="plaintext" />
      <h:selectManyCheckbox id="annotTypes1" onchange="setQueryBuilderText()" value="#{BooleanTestBean.annotationTypes1}" styleClass="plaintext" layout="lineDirection" >
        <f:selectItems value="#{BooleanTestBean.annotationVals}" />
      </h:selectManyCheckbox>
      <h:panelGroup>
        <f:verbatim>
          <a href="#Link263894Context" name="Link263894Context" id="Link263894Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263894', 'Choose Search Option', 'Select stage range, expression, pattern and location. (Checking 2 boxes for expression will search for e.g. present OR uncertain.). Select a second structure from the tree as before. Select the Boolean operator AND or OR, (the equivalent of a Boolean \'NOT\' can be obtained by selecting AND in conjunction with \'not detected\'). The interactive query builder allows you to build queries with up to 3 terms. In queries with 3 terms (2 operators), precedence is given to the first Boolean operator. Example 1: A AND B OR C = (A AND B) OR C.', 'Link263894Context')"> <img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" /></a>
        </f:verbatim>
      </h:panelGroup>
    </h:panelGrid>
    
    <h:panelGrid columns="7">
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="location1" value="#{BooleanTestBean.location1}">
        <f:selectItems value="#{BooleanTestBean.locationVals}" />
      </h:selectOneMenu>
      <h:panelGroup>
      <f:verbatim><div id="bool1"></f:verbatim>
      <f:verbatim></div></f:verbatim>
      </h:panelGroup>
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="startStage1" value="#{BooleanTestBean.startStage1}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value=" to: " />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="endStage1" value="#{BooleanTestBean.endStage1}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
    </h:panelGrid>
    <h:panelGrid columns="5" style="padding-bottom:10px">
    <f:verbatim>&nbsp;</f:verbatim>
      <h:outputText styleClass="plaintext" value="with" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="pattern1" value="">
        <f:selectItems value="#{BooleanTestBean.patternVals}" />
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value="at" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="locations1" value="">
        <f:selectItems value="#{BooleanTestBean.locationsVals}" />
      </h:selectOneMenu>
    </h:panelGrid>
    <h:selectOneMenu onchange="setQueryBuilderText()" id="operator1" value="#{BooleanTestBean.operator1}">
      <f:selectItem itemValue="AND" itemLabel="AND" />
      <f:selectItem itemValue="OR" itemLabel="OR" />
    </h:selectOneMenu>
    <h:panelGrid columns="2">
    <h:outputText value="Expression is:" styleClass="plaintext" />
    <h:selectManyCheckbox onchange="setQueryBuilderText()" id="annotTypes2" value="#{BooleanTestBean.annotationTypes2}" styleClass="plaintext" layout="lineDirection" >
      <f:selectItems value="#{BooleanTestBean.annotationVals}" />
    </h:selectManyCheckbox>
    </h:panelGrid>
    <h:panelGrid columns="7">
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="location2" value="#{BooleanTestBean.location2}">
        <f:selectItems value="#{BooleanTestBean.locationVals}" />
      </h:selectOneMenu>
      <h:panelGroup>
      <f:verbatim><div id="bool2"></f:verbatim>
      <f:verbatim></div></f:verbatim>
      </h:panelGroup>
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="startStage2" value="#{BooleanTestBean.startStage2}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value=" to: " />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="endStage2" value="#{BooleanTestBean.endStage2}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
    </h:panelGrid>
    <h:panelGrid columns="5" style="padding-bottom:10px">
    <f:verbatim>&nbsp;</f:verbatim>
      <h:outputText styleClass="plaintext" value="with" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="pattern2" value="">
        <f:selectItems value="#{BooleanTestBean.patternVals}" />
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value="at" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="locations2" value="">
        <f:selectItems value="#{BooleanTestBean.locationsVals}" />
      </h:selectOneMenu>
    </h:panelGrid>
    <h:selectOneMenu onchange="setQueryBuilderText()" id="operator2" value="#{BooleanTestBean.operator2}">
      <f:selectItem itemValue="AND" itemLabel="AND" />
      <f:selectItem itemValue="OR" itemLabel="OR" />
    </h:selectOneMenu>
    <h:panelGrid columns="2">
    <h:outputText value="Expression is:" styleClass="plaintext" />
    <h:selectManyCheckbox onchange="setQueryBuilderText()" id="annotTypes3" value="#{BooleanTestBean.annotationTypes3}" styleClass="plaintext" layout="lineDirection" >
      <f:selectItems value="#{BooleanTestBean.annotationVals}" />
    </h:selectManyCheckbox>
    </h:panelGrid>
    <h:panelGrid columns="7">
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="location3" value="#{BooleanTestBean.location3}">
      <f:selectItems value="#{BooleanTestBean.locationVals}" />
      </h:selectOneMenu>
      <h:panelGroup>
      <f:verbatim><div id="bool3"></f:verbatim>
      <f:verbatim></div></f:verbatim>
      </h:panelGroup>
      <f:verbatim>&nbsp;</f:verbatim>
      <h:selectOneMenu onchange="setQueryBuilderText()" id="startStage3" value="#{BooleanTestBean.startStage3}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value=" to: " />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="endStage3" value="#{BooleanTestBean.endStage3}">
        <f:selectItems value="#{BooleanTestBean.rangeOfSelectedStages}"/>
      </h:selectOneMenu>
    </h:panelGrid>
    <h:panelGrid columns="5" style="padding-bottom:10px">
    <f:verbatim>&nbsp;</f:verbatim>
      <h:outputText styleClass="plaintext" value="with" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="pattern3" value="">
        <f:selectItems value="#{BooleanTestBean.patternVals}" />
      </h:selectOneMenu>
      <h:outputText styleClass="plaintext" value="at" />
      <h:selectOneMenu onchange="setQueryBuilderText()" id="locations3" value="">
        <f:selectItems value="#{BooleanTestBean.locationsVals}" />
      </h:selectOneMenu>
    </h:panelGrid>
    <f:verbatim><br /><br /></f:verbatim>
    </h:form>
  </h:panelGrid>
  <h:panelGrid>
    <h:form id="booleanQForm2">
      <h:panelGrid columns="5" cellpadding="2" cellspacing="2">
        <h:inputText value="#{BooleanTestBean.input}"  onkeyup="checkButtonStatus()" size="70" id="queryBuilder" />
        <h:commandButton value="Run Query" id="submitQBuilder" action="#{BooleanTestBean.goSearch}" />	
        <h:commandButton value="Save Query" id="saveQ" actionListener="#{BooleanTestBean.saveQuery}" />
        <h:commandButton value="Clear Query" id="clearQ" action="#{BooleanTestBean.clearQuery}" />       
        <h:panelGroup>
        <f:verbatim>
        <a href="#Link263895Context" name="Link263895Context" id="Link263895Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263895', 'Execute the Built Query', 'Click \'Run Query\' box to execute query or \'Save Query\' box to save. The query can be modified or extended using the same syntax (see &lt;a href=\&quot;http://www.gudmap.org/Help/Boolean_Syntax_Help.html\&quot;&gt;Help&lt;/a&gt;). In queries with more than one term, precedence is given to the first operator and all other operators are treated independently.', 'Link263895Context')"> <img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" /></a>
        </f:verbatim>
        </h:panelGroup>
      </h:panelGrid>
  </h:form>
  </h:panelGrid>
  <script type="text/javascript">
    disableFormElements();
  </script>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>