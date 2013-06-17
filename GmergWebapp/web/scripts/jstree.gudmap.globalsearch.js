/*
 * jstree.gudmap.globalsearch 1.0
 *
 * Bernard Haggarty 19/11/2011
 *
 */


function updateTree(){

	renameNode("genes");
	
	renameNode("insitu");
	
	renameNode("microarray");

	renameNode("genelists");
	
	renameNode("tutorials");

	renameNode("tissuesummarypage");

	renameNode("tgmousestrains");	
}


function renameNode(id){
	// NOTE: adds count to node name by appending to DOM, as using jstree.rename_node causes
	// the status of the tree to be lost when page is refreshed
	//alert("id = " + id);
	//alert("renameNode");
	var nodeId =jQuery("#" + id);
	//alert("nodeId = " + nodeId);
	
	var count = getCount(nodeId);
//	if (count != 0){
		jQuery("#" + id +  " > a > b").remove(); // remove old count	
		var display_count = "<b> [" + count + "]</b>";	// get latest count	
		jQuery("#" + id +  " > a").append(display_count); //append latest count to tree node 
//	}
	
	
//	if (jQuery.jstree._focused().is_open(nodeId))
		renameChildren(nodeId);		
}

function renameChildren(nodeId){	
	var childnodes = jQuery.jstree._focused()._get_children(nodeId);
	
	for(var i=0; i<childnodes.length; i++){		
		var child = jQuery(childnodes[i]);
		//alert("child = " + child.attr("id"));
		renameNode(child.attr("id"));
	}		
}


//
//function updateChildren(node){
//	alert("updateChildren");
//	
//	var childnodes = jQuery.jstree._focused()._get_children(node);
//	
//	for(var i=0; i<childnodes.length; i++){		
//		
//		var child = childnodes[i];
//		alert(jQuery.jstree._focused().get_text(child));	
//		renameNode(child);
//		updateChildren(child);		
//	}		
//}


//	function displayPage(e, data){
//		alert("jgg_displayPage");
//		alert(data.rslt.obj.attr("id"));
//		
//		var node = jQuery.jstree._focused().get_selected();
//		var path =  jQuery.jstree._focused().get_path(node,true);
//		alert("path = " + path);	
//		alert("id = " + node.attr("id"));
//		
//		renameNode(node);
//		
//		
//		//var n = jQuery.jstree._focused().select_node("#insitu");
//		//jQuery.jstree._focused().rename_node(n, "Insitu(10)"); 
//		//jQuery("#insitu").jstree.set_text("fred");
//
//		
//
//		//jQuery.jstree._focused().rename_node(jQuery("#insitu"), "Insitu(10)"); 
//	}
	


// the following functions are for the creation of dynamic tree control

	function checkParents(node, nodeId){

		var parents =  jQuery.jstree._focused().get_path(node,true);

		for(var i=0; i<parents.length; i++){
			
           if (jQuery("#"+parents[i]).attr("id") == nodeId)
               return true;              
    	} 
		return false;
	}	

	function checkChildren(node, nodeId){

		var tree_instance = jQuery.jstree._reference(jQuery("#demo1")); 	
		var childnodes =tree_instance._get_children(node);
		
		for(var i=0; i<childnodes.length; i++){		
			
			if (nodeId == childnodes[i].id)
				return true;

		}		
		return false;
	}


	
	function contextMenu(obj){
		
		alert("node ID = " + obj.attr("id"));
			//alert("Gen)
				
		var tsArray = ["ts17","ts18","ts19","ts20","ts21","ts22","ts23","ts24","ts25","ts26","ts27","ts28"];
		var expArray = ["present","not detected","uncertain"];
		var anatomyArray = ["metanephros","lower urinary tract","male reproductive system","female reproductive system","early genitourinary system"];
		
		if (obj.attr("id") == "genes") { 
			//alert("Genes");
	        var items = {
	        	genestrips : {
	        		label : "Gene Strips",
	        		action : function (obj) { addGeneStrips(obj);}
	        	},
		        genelists : {
	        		label : "Genelists",
	        		action : function (obj) { addGeneLists(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "genestrip") == true || checkParents(obj, "genestrip") == true)
				delete items.genestrips;
			if (checkChildren(obj, "genelist") == true || checkParents(obj, "genelist") == true)
				delete items.genelists;
	        return items;
	    } 

		if (obj.attr("id") == "series") { 
			//alert("Series");
	        var items = {
		        platform : {
	        		label : "Platform",
	        		action : function (obj) { addPlatform(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "platform") == true || checkParents(obj, "platform") == true)
				delete items.platform;
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;
	        return items;
	    }
		
		if (obj.attr("id") == "insitu") { 
			//alert("Insitu");
	        var items = {
	        	expression : {
	        		label : "Expressions",
	        		action : function (obj) { addExpression(obj);}
	        	},
		        stage : {
	        		label : "Theiler Stages",
	        		action : function (obj) { addTheilerStage(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	},
	        	rename : {
	        		label : "Rename Node",
	        		action : function (obj) { renameNode(obj);}
	        	}

	        };
			
			if (checkChildren(obj, "expression") == true || checkParents(obj, "expression") == true)
				delete items.expression;
			if (checkChildren(obj, "stage") == true || checkParents(obj, "stage") == true)
				delete items.stage;
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;

	        return items;
	    } 
	    
		if (obj.attr("id") == "microarray") { 
			//alert("Microarray");
	        var items = {
	        	series : {
	        		label : "Series",
	        		action : function (obj) { addSeries(obj);}
	        	},
		        samples : {
	        		label : "Samples",
	        		action : function (obj) { addSamples(obj);}
	        	}
	        };
			if (checkChildren(obj, "series") == true || checkParents(obj, "series") == true)
				delete items.series;
			if (checkChildren(obj, "samples") == true || checkParents(obj, "samples") == true)
				delete items.samples;
	        return items;
	    }
	    
		if (obj.attr("id") == "samples") { 
			//alert("Samples");
	        var items = {
		        stage : {
	        		label : "Theiler Stages",
	        		action : function (obj) { addTheilerStage(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "stage") == true || checkParents(obj, "stage") == true)
				delete items.stage;
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;
	        return items;
	    }

		if (jQuery.inArray(obj.attr("name"), expArray) > -1) { 
			//alert("expression = " + obj.attr("name"));
	        var items = {
		        stage : {
	        		label : "Theiler Stages",
	        		action : function (obj) { addTheilerStage(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "stage") == true || checkParents(obj, "stage") == true)
				delete items.stage;
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;
	        return items;
	    }


		if (jQuery.inArray(obj.attr("name"), tsArray) > -1) { 
			//alert("ts = " + obj.attr("name"));
	        var items = {
		        expression : {
	        		label : "Expressions",
	        		action : function (obj) { addExpression(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "expression") == true || checkParents(obj, "expression") == true)
				delete items.expression;
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;
	        return items;
	    }
		
		if (jQuery.inArray(obj.attr("name"), anatomyArray) > -1) { 
			//alert("anatomy = " + obj.attr("name"));
	        var items = {
	        	expression : {
	        		label : "Expressions",
	        		action : function (obj) { addExpression(obj);}
	        	},
		        stage : {
	        		label : "Theiler Stages",
	        		action : function (obj) { addTheilerStage(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "expression") == true || checkParents(obj, "expression") == true)
				delete items.expression;
			if (checkChildren(obj, "stage") == true || checkParents(obj, "stage") == true)
				delete items.stage;
	        return items;
	    } 
		
		
		if (obj.attr("name") == "GPL81" || obj.attr("name") == "GPL339" || obj.attr("name") == "GPL1261" || obj.attr("name") == "GPL6426") { 
	        var items = {
		        stage : {
	        		label : "Theiler Stages",
	        		action : function (obj) { addTheilerStage(obj);}
	        	},
	        	anatomy : {
	        		label : "Anatomical Region",
	        		action : function (obj) { addAnatomicalRegion(obj);}
	        	}	        	
	        };
			if (checkChildren(obj, "anatomy") == true || checkParents(obj, "anatomy") == true)
				delete items.anatomy;
			if (checkChildren(obj, "stage") == true || checkParents(obj, "stage") == true)
				delete items.stage;
	        return items;
	    }

		
	}


	function addGeneStrips(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "genestrip", "name" : "GeneStrip"}, "data" : "GeneStrip"});
		jQuery.jstree._focused().open_node(node);
	}

	function addGeneLists(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "genelist", "name" : "GeneList"}, "data" : "GeneList"});
		addGeneListChildren(newNode);
		jQuery.jstree._focused().open_node(node);
	}

	function addGeneListChildren(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "series", "name" : "Series"}, "data" : "Series"});
		jQuery.jstree._focused().open_node(node);
	}
	
	function addExpression(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "expression", "name" : "Expression"}, "data" : "Expression"});
		addExpressionChildren(newNode);
		jQuery.jstree._focused().open_node(node);	
	}
	
	function addExpressionChildren(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "present", "name" : "present"}, "data" : name1});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "not detected", "name" : "not detected"}, "data" : "not detected"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "uncertain", "name" : "uncertain"}, "data" : "uncertain"});
		jQuery.jstree._focused().open_node(node);
	}
	
	function addTheilerStage(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "stage", "name" : "Theiler Stage"}, "data" : "Theiler Stage"});
		addTheilerStageChildren(newNode);
		jQuery.jstree._focused().open_node(node);
	}

	function addTheilerStageChildren(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts17", "name" : "ts17"}, "data" : "ts17"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts18", "name" : "ts18"}, "data" : "ts18"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts19", "name" : "ts19"}, "data" : "ts19"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts20", "name" : "ts20"}, "data" : "ts20"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts21", "name" : "ts21"}, "data" : "ts21"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts22", "name" : "ts22"}, "data" : "ts22"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts23", "name" : "ts23"}, "data" : "ts23"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts24", "name" : "ts24"}, "data" : "ts24"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts25", "name" : "ts25"}, "data" : "ts25"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts26", "name" : "ts26"}, "data" : "ts26"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts27", "name" : "ts27"}, "data" : "ts27"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ts28", "name" : "ts28"}, "data" : "ts28"});
		jQuery.jstree._focused().open_node(node);
	}

	function addAnatomicalRegion(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "anatomy", "name" : "Anatomical Region"}, "data" : "Anatomical Region"});
		addAnatomicalRegionChildren(newNode);
		jQuery.jstree._focused().open_node(node);
	}
	
	function addAnatomicalRegionChildren(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "metanephros", "name" : "metanephros"}, "data" : "metanephros"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "lut", "name" : "lower urinary tract"}, "data" : "lower urinary tract"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "mrs", "name" : "male reproductive system"}, "data" : "male reproductive system"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "frs", "name" : "female reproductive system"}, "data" : "female reproductive system"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "ers", "name" : "early genitourinary system"}, "data" : "early reproductive system"});
		jQuery.jstree._focused().open_node(node);
	}

	function addSeries(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "series", "name" : "Series"}, "data" : "Series"});
		jQuery.jstree._focused().open_node(node);
	}

	function addPlatform(node){
		var newNode = jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "platform", "name" : "Platform"}, "data" : "Platform"});
		addPlatformChildren(newNode);
		jQuery.jstree._focused().open_node(node);
	}
	
	function addPlatformChildren(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "developing_kidney", "name" : "Developing Kidney"}, "data" : "Developing Kidney"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "LUT", "name" : "LUT"}, "data" : "LUT"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "Reproductive_System", "name" : "Reproductive System"}, "data" : "Reproductive System"});
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "GPL6246", "name" : "GPL6246"}, "data" : "GPL6246"});
		jQuery.jstree._focused().open_node(node);
	}
	
	function addSamples(node){
		jQuery.jstree._focused().create_node(node, "last", {"attr" : {"id" : "samples", "name" : "Samples"}, "data" : "Samples"});
		jQuery.jstree._focused().open_node(node);
	}

