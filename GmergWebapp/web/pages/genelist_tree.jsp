<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<head>

	<script type="text/javascript" src="../scripts/jstree.gudmap.globalsearch.js"></script> 

	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/_lib/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="../scripts/jstree_pre1.0_fix_1/jquery.jstree.js"></script> 

<script type="text/javascript">

jQuery(document).ready(function(){
	jQuery("#genelist_tree").jstree({ 	
		"json_data" : {
		    "progressive_render" : true,
		    "ajax" : {
		    	"url" : "../scripts/genelist.json"
		    }
		},	
		"types": {
            "types": {
                "Role": {
                },
            }
		},
        themes : {
			theme : "classic",
			dots  : true,
			icons : false,
			url   : "../scripts/jstree_pre1.0_fix_1/themes/gudmap/style.css"
		},	
		plugins : ["types", "themes", "json_data", "ui", "cookies"]												

	})  		
	.delegate("a", "click", function(e, data) {
		//alert("click node");
		var node = jQuery.jstree._focused().get_selected();
		jQuery.jstree._focused( ).save_selected( ) ;
		
		var result = node.attr("id");

		
		jQuery.cookie("save_selected", result);
		if (result <= '0'){
			jQuery.jstree._focused().toggle_node(node);
			return;
		}
		
		var selectedNode =  document.getElementById('mainForm:treeitem');
		selectedNode.value = result;

//		selectedNode =  document.getElementById('mainForm:genelist_crumb');
//		selectedNode.value = node.attr("crumb");
					
		selectedNode =  document.getElementById('mainForm:genelist_mastertableId');
		selectedNode.value = node.attr("table");

		
		var dummyLink = document.getElementById("genelistLink");
		if (document.createEvent)
		{
		    var evObj = document.createEvent('MouseEvents');
		    evObj.initEvent( 'click', true, false );
		    dummyLink.dispatchEvent(evObj);
		}
		else if (document.createEventObject)
		{
		    dummyLink.fireEvent('onclick');
		}

	});
  		
});

function getCrumb(node){
	alert("into crumb");
	
	var path =  jQuery.jstree._focused().get_parent(node, false);
	alert("path = " + path);	
	
}	


</script>	


<script type="text/javascript">
$(document).ready(function(){
	$("#expanderHead").click(function(){
		$("#expanderContent").slideToggle();
		if ($("#expanderSign").text() == "(more...)"){
			$("#expanderSign").html("(less...)")
		}
		else {
			$("#expanderSign").text("(more...)")
		}
	});
});
</script>
</head>


<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	
	<h:form id="mainForm" >
	<h:outputText styleClass="bigplaintext" value="#{GeneListTreeBean.title}" rendered="true" escape="false" />
	<p id="expanderHead" style="cursor:pointer;">GUDMAP gene lists are the products of analyses of the GUDMAP microarray expression data.<span id="expanderSign">(more...)</span><p>
	<div id="expanderContent" style="display:none">
	
	    <p>They are spilt into those lists that have been included in publications and those that are unpublished. Lists are further sub-divided by sample datasets, microarray chip platform and developmental stage. For more info, including protocols, please see the <a href="http://www.gudmap.org/Help/Analysis_Help.html" style="font-size:inherit;">analysis help page</a>.</p>
	    <p>The number of microarray probes/genes in a list is displayed in brackets. Hover over a list name for more detailed information, or click on a list (bold) to view the lists in heatmap view.</p>
	
		<p>Protocols for published gene lists can be found in their respective publications:
		<br/>
		<a href="http://www.ncbi.nlm.nih.gov/pubmed/19000842" style="font-size:inherit;">Brunskill et al. (2008)</a> Pubmed: 19000842<br/>
		<a href="http://www.ncbi.nlm.nih.gov/pubmed/19501082" style="font-size:inherit;">Georgas et al. (2009)</a> Pubmed: 19501082<br/>
		<a href="http://www.ncbi.nlm.nih.gov/pubmed/21386911" style="font-size:inherit;" >Thiagarajan et al. (2011)</a> Pubmed: 21386911<br/>
		</p>
		<span class="collaps2"></span>
		
		
	</div>
	
	<h:outputLink value="genelist_rnaseq_tree.html" >
		<h:outputText value="Link to RNASEQ Analysis"/>
	</h:outputLink>
	<br/><br/>
	
	 <h:inputText id="treeitem" value="#{GeneListTreeBean.selectedItem}" style="display:none; visibility: hidden; "/>
<%--  	 <h:inputText id="genelist_crumb" value="#{GeneListTreeBean.crumb}" style="display:none; visibility: hidden; "/>	--%>
	 <h:inputText id="genelist_mastertableId" value="#{GeneListTreeBean.masterTableId}" style="display:none; visibility: hidden; "/>
	 <f:verbatim>
		<div id="genelist_tree" class="demo" style="overflow: auto; align: left; height: 540px;">
		</div>
	</f:verbatim>
	
	<t:commandLink id="genelistLink" forceId="true" style="display:none; visibility: hidden;" action="#{GeneListTreeBean.findNode}" actionListener="#{GeneListTreeBean.processAction}" >
	</t:commandLink>
	<t:commandLink id="createChildren" forceId="true" style="display:none" action="#{GeneListTreeBean.createChildren}"/> 
		
	</h:form> 
	<jsp:include page="/includes/footer.jsp" />
</f:view>
