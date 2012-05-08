<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
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

    function showGenePanel(e) {
		    document.getElementById(document.forms['geneQForm'].id+":genePanel").style.visibility = 'visible';
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'visible';
    }
      
	function hideGenePanel(e) {
		document.getElementById(document.forms['geneQForm'].id+":genePanel").style.visibility = 'hidden';
			//document.getElementById('headersubview:loginForm:loginPanel').style.visibility = 'hidden';
	}
</script>
</head>

<f:view>
  <jsp:include page="/includes/header.jsp" />
  
  <div class="border_r">
  <div class="border_l">
  <div class="border_b">
  <div class="border_t">
  <div class="border_bl">
  <div class="border_br">
  <div class="border_tl">
  <div class="border_tr">
  
  
  <div class="boxContent">
  
    <div class="navTree">
      <a href="/portal/index.html" class="plaintext">Kidney Atlas Data Portal</a>
      <span class="plaintext">&nbsp;&nbsp;&gt;&nbsp;&nbsp;Gene Expression Home</span>
    </div>
    
    <div class="box1">
      <div class="subBox">
        <div class="border_r">
  		<div class="border_l">
  		<div class="border_b">
  		<div class="border_t">
  		<div class="border_bl">
  		<div class="border_br">
  		<div class="border_tl">
  		<div class="border_tr">
  		
  		
  		<div class="boxContent">
  		  <%-- <div class="miniHelp"><span class="plaintext">Help</div>--%>
  		  <div class="innerBHeader"><span class="plaintextbold">Find Entries for Specific Genes</span></div>
  		  <div>
  		    <h:form>
  		      <input type="hidden" name="query" value="Gene" />
  		      <h:panelGrid columns="1" cellpadding="2" cellspacing="2">
  		      <h:panelGroup>
  		        <h:outputText value="Gene " styleClass="plaintext" />
  		        <h:selectOneMenu id="geneWildcard" value="#{DatabaseHomepageBean.geneWildcard}">
				  <f:selectItems value="#{DatabaseHomepageBean.widecardItems}" />
			    </h:selectOneMenu>
			  </h:panelGroup>
			  <h:outputText styleClass="plaintext" value="(Please enter one identifier, e.g. cldn8; claudin 8; MGI:1859286; ENSMUSG00000050520, per line)" />
			  <h:inputTextarea value="#{DatabaseHomepageBean.geneInput}" rows="4" cols="50"/>
              <h:commandButton image="../images/gu_go.gif" styleClass="icon" alt="Go" action="#{DatabaseHomepageBean.search}" />
              </h:panelGrid>
              <%--<h:panelGrid columns="2" cellpadding="2" cellspacing="2">
                <h:outputText value="Assay Type" styleClass="plaintext" />
                <h:selectOneMenu value="">
                <f:selectItem itemLabel="any" itemValue="any"/>
                <f:selectItem itemLabel="RNA in situ hybridisation" itemValue="equals"/>
                <f:selectItem itemLabel="immunohistochemistry" itemValue="begins"/>
                <f:selectItem itemLabel="microarray" itemValue="begins"/>
              </h:selectOneMenu>
              </h:panelGrid>--%>
  		    </h:form>
  		    <h:form id="uploadForm" enctype="multipart/form-data">
	          <h:inputHidden id="uploadedGenes" value="#{DatabaseHomepageBean.uploadedGenes}"/>
	          <h:outputText styleClass="plaintext" value="Or upload a gene batch query file: " />
			  <t:inputFileUpload id="myFileId" value="#{DatabaseHomepageBean.myFile}" storage="file"
								   accept="txt/txt,txt/csv" required="false" />
			  <h:commandButton value="Upload"	action="#{DatabaseHomepageBean.processMyFile}" />
            </h:form>
  		    <%--<h:outputLink value="focus_gene_index_browse.html" styleClass="plaintext">
  		      <h:outputText value="Browse Genes" />
  		    </h:outputLink>--%>
  		  </div>
  		</div>
  		
  		
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
      </div>
      <div class="breaker">&nbsp;</div>
      <div class="subBox">
        <div class="border_r">
  		<div class="border_l">
  		<div class="border_b">
  		<div class="border_t">
  		<div class="border_bl">
  		<div class="border_br">
  		<div class="border_tl">
  		<div class="border_tr">
  		
  		
  		<div class="boxContent">
  		  <%-- <div class="miniHelp"><span class="plaintext">Help</div>--%>
  		  <div class="innerBHeader"><span class="plaintextbold">EuReGene Entries</span></div>
  		  <div>
  		    <div style="padding:2px;">
  		      <span class="plaintext">In situ&nbsp;&nbsp;&gt;&nbsp;&nbsp;</span>
  		        <a href="ish_browse.html?browseId=browseAll" class="plaintext">Browse in situ data</a>
  		    </div>
  		    <%--<div style="padding:2px;">
  		      <span class="plaintext">Microarray&nbsp;&nbsp;&gt;&nbsp;&nbsp;</span>
  		        <a href="series_browse.html" class="plaintext">Browse experiments</a>
  		      <span class="plaintext">&nbsp;/&nbsp</span>
  		      <a href="focus_mic_browse.html" class="plaintext">Browse samples</a>
  		    </div> --%>
  		      <%--<div style="padding:2px;">
  		        <span class="plaintext">All&nbsp;&nbsp;&gt;&nbsp;&nbsp;</span>
  		        <a href="" class="plaintext">Browse in situ and microarray data</a>
  		      </div>--%>
  		      
  		  </div>
  		  
  		</div>
  		
  		
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
      </div>
      
    </div>
    
    <div class="box2">
      <div class="subBox">
        <div class="border_r">
  		<div class="border_l">
  		<div class="border_b">
  		<div class="border_t">
  		<div class="border_bl">
  		<div class="border_br">
  		<div class="border_tl">
  		<div class="border_tr">
  		
  		
  		<div class="boxContent">
  		  <%-- <div class="miniHelp"><span class="plaintext">Help</div>--%>
  		  <div class="innerBHeader"><span class="plaintextbold">Database Summary</span></div>
  		  <div>
  		    <span class="plaintext">ISH:</span>
  		    <h:outputLink value="ish_browse.html" styleClass="plaintext">
  		      <f:param name="browseId" value="browseAll" />
  		      <h:outputText value="#{EntryPageBean.numPubISHSubmissions}" />
  		    </h:outputLink>
  		    &nbsp;&nbsp;&nbsp;&nbsp;
  		    <span class="plaintext">Microarray:</span>
  		    <h:outputText value="#{EntryPageBean.numPubArraySubmissions}" styleClass="plaintext" />
  		  </div>
  		</div>
  		
  		
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
      </div>
      <div class="breaker">&nbsp;</div>
      <div class="subBox">
        <div class="border_r">
  		<div class="border_l">
  		<div class="border_b">
  		<div class="border_t">
  		<div class="border_bl">
  		<div class="border_br">
  		<div class="border_tl">
  		<div class="border_tr">
  		
  		
  		<div class="boxContent">
  		  <%-- <div class="miniHelp"><span class="plaintext">Help</div>--%>
  		  <div class="innerBHeader"><span class="plaintextbold">View Gene Expression in Anatomical Structures</span></div>
  		  <div>
  		    <div id="autosuggest"><ul></ul></div>
  		    <h:form id="anatomyForm">
  		      <input type="hidden" name="query" value="Anatomy" />
              <h:panelGrid columns="1">
                <h:outputText styleClass="plaintext" value="(Please enter one anatomical structure per line)" />
                <h:inputTextarea id="anatomyInput" value="#{DatabaseHomepageBean.anatomyInput}" cols="50" rows="3"  />                
                <h:commandButton image="../images/gu_go.gif" styleClass="icon" alt="Go" action="#{DatabaseHomepageBean.search}" />
              </h:panelGrid>
              
  		    </h:form>
  		    <script language="Javascript">
  		      var ontology = new Array(<h:outputText escape="false" value="#{anatomyBean.ontologyTerms}" />);
  		      new AutoSuggest(document.getElementById('anatomyForm:anatomyInput'),ontology);
		    </script>
  		    <p>
  		      <a class="plaintext" href="anatomy_tree.html">Search for present/not detected/possible expression in anatomical structures</a><br />
  		      <a class="plaintext" href="/atlas/pages/section_viewer.html?project=euregene_atlas&stack=fullResKidney_substack">View Kidney Structures / Search Gene Expression Data</a>
  		    </p>
  		    
  		  </div>
  		</div>
  		
  		
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
  		</div>
      </div>
      
    </div>
    
    
    
    <div style="clear:both;padding-top:5px;padding-bottom:5px;"></div>
    
  </div>
  
  
  
  
  </div>
  </div>
  </div>
  </div>
  </div>
  </div>
  </div>  
  </div>	
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>