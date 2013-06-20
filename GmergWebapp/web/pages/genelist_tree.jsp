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

</head>


<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	
	<h:form id="mainForm" >
	


<p>GUDMAP gene lists are the product analysis of GUDMAP microarray expression data. They are spilt into those lists that have been included in publications and those that are unpublished.
</br>
Lists are further sub-divided by sample datasets and microarray chip platform and in some cases the developmental stage of the sample on which the analysis was based.
The datasets are groups of samples, spilt by microarray platform (MOE430 or ST1), with further sub-division by anatomy (kidney, lower urinary tract, reproductive system, pelvic ganglion and juxtaglomerular apparatus).
</br>
Please browse the tree for gene lists of interest, the number of microarray probes/genes which comprise each list is displayed. Hover over a list name for more detailed information, or click on a list (bold) to view the lists in heatmap view.
</br>
The majority of the unpublished gene lists have been produced by Dr. Bruce Aronow (CCHMC). The protocol used by the Aronow group to produce the gene lists is on the <a href="http://www.gudmap.org/Help/Analysis_Help.html">help page</a>. For any further assistance with the methodology please contact <a href="mailto:bruce.aronow@cchmc.org">Dr. Bruce Aronow</a> directly.</p>
<p>Protocols for published gene lists can be found in their respective publications:
</br>
<a href="http://www.ncbi.nlm.nih.gov/pubmed/19000842">Brunskill et al. (2008)</a> Pubmed: 19000842</br>
<a href="http://www.ncbi.nlm.nih.gov/pubmed/19501082">Georgas et al. (2009)</a> Pubmed: 19501082</br>
<a href="http://www.ncbi.nlm.nih.gov/pubmed/21386911">Thiagarajan et al. (2011)</a> Pubmed: 21386911</br>
</p>	
	
<%--	<h:outputText styleClass="bigplaintext" value="#{GeneListTreeBean.title}" rendered="true" escape="false" /> --%>
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
