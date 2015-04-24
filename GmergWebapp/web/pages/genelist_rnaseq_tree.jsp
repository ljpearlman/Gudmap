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
	jQuery("#genelist_seq_tree").jstree({ 	
		"json_data" : {
		    "progressive_render" : true,
		    "ajax" : {
		    	"url" : "../scripts/genelistrnaseq.json"
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
//		alert("click node");
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

		selectedNode =  document.getElementById('mainForm:genelist_htmlfile');
		selectedNode.value = node.attr("file");

		window.open(selectedNode.value);



	});
  		
});

function getCrumb(node){
	alert("into crumb");
	
	var path =  jQuery.jstree._focused().get_parent(node, false);
	alert("path = " + path);	
	
}	


</script>	



</head>


<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	
	<h:form id="mainForm" >
	<h:outputText styleClass="bigplaintext" value="#{GeneListRnaSeqTreeBean.title}" rendered="true" escape="false" />

	 <h:inputText id="treeitem" value="#{GeneListRnaSeqTreeBean.selectedItem}" style="display:none; visibility: hidden; "/>
	 <h:inputText id="genelist_htmlfile" value="#{GeneListRnaSeqTreeBean.htmlFile}" style="display:none; visibility: hidden; "/>
	 <f:verbatim>
		<div id="genelist_seq_tree" class="rnaseq" style="overflow: auto; align: left; height: 540px;">
		</div>
	</f:verbatim>
	
	<t:commandLink id="genelistLink" forceId="true" style="display:none; visibility: hidden;" action="#{GeneListRnaSeqTreeBean.findNode}" actionListener="#{GeneListRnaSeqTreeBean.processAction}" >
	</t:commandLink>


	
	</h:form> 
	<jsp:include page="/includes/footer.jsp" />
</f:view>
