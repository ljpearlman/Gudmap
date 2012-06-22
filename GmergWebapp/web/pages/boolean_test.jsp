<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
<head>
  <script type="text/javascript">
    function disableFormElements() {
      var queryForm = document.forms['booleanQForm'];
      var qFormId = queryForm.id;
      for(var i=0;i<queryForm.elements.length;i++){
        if(queryForm.elements[i].id != qFormId+':resultFormat') {
          queryForm.elements[i].disabled = true;
        }
        if(queryForm.elements[i].value == 'p') {
          queryForm.elements[i].checked = true;
        }
      }
      var queryForm2 = document.forms['booleanQForm2'];
      var queryBuilder = queryForm2.elements['booleanQForm2:queryBuilder'];
      var qbVal = queryBuilder.value;
      if(qbVal == null && qbVal == "") {
        queryForm2.elements['booleanQForm2:submitQBuilder'].disabled = true;
        queryForm2.elements['booleanQForm2:saveQ'].disabled = true;
      }
      
      
    }
  </script>
   <script type="text/javascript" src="../scripts/prototype.js"></script>
   <script type="text/javascript" src="../scripts/rico.js"></script>
   <script type="text/javascript" src="../scripts/suggest.js"></script>
  <style>
		.leftAlign{
		text-align: left;
		}
		.rightAlign{
			text-align: right;
		}
		.topAlign45{
			vertical-align: top;
			width:45%;
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
--%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  <p><h:message for="stagesCheck" styleClass="plainred" /></p>
  <p class="plaintextbold">
    Search gene expression patterns in selected anatomical components.
  </p>
<%--  
  <h:form>
    <h:inputHidden id="treeType" value="{applicationVar.perspective}" />
    <p class="plaintext">
      Select Stage range:&nbsp;
      <h:selectOneMenu binding="#{BooleanTestBean.startInput}" value="#{BooleanTestBean.startStage}">
        <f:selectItems value="#{BooleanTestBean.availableStages}"/>
      </h:selectOneMenu>
      &nbsp;
      to:
      <h:selectOneMenu binding="#{BooleanTestBean.endInput}" value="#{BooleanTestBean.endStage}">
        <f:selectItems value="#{BooleanTestBean.availableStages}"/>
      </h:selectOneMenu>
      &nbsp;
      <h:inputHidden id="stagesCheck" validator="#{BooleanTestBean.validateStages}" value="required" />
      <h:commandButton value="Display Tree" actionListener="#{BooleanTestBean.displayTree}" />
    </p>
  </h:form>
--%>  
  
  <p><h:outputText styleClass="plaintext" value="Current anatomy display is for stage range #{BooleanTestBean.startStage} to #{BooleanTestBean.endStage}."/></p>
<%--
  <p><h:outputText styleClass="plaintext" value="Find components:" />&nbsp;<input type="text" id="componentSearchField" size="30" />&nbsp;<input type="button" onclick="openNodesMatchingSearchString(document.getElementById('componentSearchField').value)" value="Search" /></p>
--%>
  <h:panelGrid columns="3" cellpadding="2" cellspacing="2">
    <h:inputText id="componentSearchField" size="30" styleClass="suggestGudmapAnatomy" />
    <h:commandButton onclick="openNodesMatchingSearchString(document.getElementById('componentSearchField').value, false)" value="Find Components in Tree" type="button" />
    <f:verbatim>
	    <a href="#Link263891Context" name="Link263891Context" id="Link263891Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID263891', 'Find Components in Tree', 'To find structures in the anatomy tree, expand the tree or type all or part of the name of the structure in the text box and click - \'Find components in tree\' box.   TS = Theiler Stage.', 'Link263891Context')"> 
	    	<img src="../images/focus/n_information.gif" alt="information" width="22" height="24" border="0" />
	    </a>
    </f:verbatim>
  </h:panelGrid>
  
  <h:panelGrid columns="2" width="100%" columnClasses="topAlign45,topAlign55">
  <h:panelGroup>
  <f:verbatim><div style="overflow:auto;height:455px;width:100%;"></f:verbatim>

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
              <h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
                <h:outputText value="Javascript Tree Menu" />
              </h:outputLink>
            </h:panelGrid>
            <f:verbatim>
              <script type="text/javascript">
                <c:forEach items="${BooleanTestBean.treeContent}" var="row">
                  <c:out value="${row}" escapeXml="false"/>
                </c:forEach>
                initializeDocument();
              </script>
            </f:verbatim>
          </h:panelGroup>
        </h:panelGrid>
      </h:panelGrid>
    </h:panelGrid>
    <h:panelGrid cellpadding="0" cellspacing="0" border="0" width="100%" columns="1">
      <h:panelGroup rendered="#{siteSpecies != 'Xenopus laevis'}">
        <h:outputText styleClass="plaintextbold" value="G " />
        <h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
      </h:panelGroup>
      <h:panelGroup rendered="#{siteSpecies == 'Xenopus laevis'}">
        <h:outputText styleClass="plaintext" value="We would like to thank " />
        <h:outputLink styleClass="plaintext" value="http://cbrbio.ucalgary.ca/lab/vize.html"><h:outputText value="Peter Vize" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" and " />
        <h:outputLink styleClass="plaintext" value="http://homepages.ucalgary.ca/~esegerde/"><h:outputText value="Erik Segerdell" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" at " />
        <h:outputLink styleClass="plaintext" value="http://xenbase.org/"><h:outputText value="Xenbase" /></h:outputLink>
        <h:outputText styleClass="plaintext" value=" for their help with the " />
        <h:outputLink styleClass="plaintext" value="http://www.obofoundry.org/cgi-bin/detail.cgi?id=xenopus_anatomy"><h:outputText value="anatomy ontology" /></h:outputLink><h:outputText styleClass="plaintext" value="." />
        
      </h:panelGroup>
    </h:panelGrid>
    
  
  <f:verbatim></div></f:verbatim>
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
      <h:panelGrid columns="4" cellpadding="2" cellspacing="2">
        <h:inputText value="#{BooleanTestBean.input}"  onkeyup="checkButtonStatus()" size="70" id="queryBuilder" />
        <h:commandButton value="Run Query" id="submitQBuilder" action="#{BooleanTestBean.goSearch}">	
        </h:commandButton>
        <h:commandButton value="Save Query" id="saveQ" actionListener="#{BooleanTestBean.saveQuery}" />
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