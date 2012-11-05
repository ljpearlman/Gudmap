
// A global variabl that can be used when testing client browser for IE
var isIE = ((navigator.userAgent.toLowerCase().indexOf("msie") > 0) && (navigator.userAgent.toLowerCase().indexOf("opera") < 0));
//var isSafari = (navigator.userAgent.toLowerCase().indexOf('safari') >= 0);
//var isNetscape = (navigator.appName.toLowerCase() == 'netscape');
//var isMac = (navigator.userAgent.toLowerCase().indexOf('macintosh') >= 0);

var isSafari = Spry.is.safari;
var isMac = Spry.is.mac;
var isMozilla = Spry.is.mozilla;
var isMozillaWindows = isMozilla && Spry.is.windows;
//var fixedTableHeaders = if (userSystem.is.ie || (userSystem.is.mozilla && !userSystem.is.windows);


//******************************************** Zoom Viewer section (Begin) ******************************************************
var zoomViewerLoaded;
		
//This is to be used whenever a zoom viewer window is required
function openZoomViewer(id, name, serialNo, windowParams, numTries) {
	if(!serialNo)
		serialNo = '1';
	if(!numTries) {
		numTries = 0;
		zoomViewerLoaded = false;
   		if (!windowParams)
   			windowParams = "toolbar=no,menubar=no,directories=no,resizable=yes,scrollbars=yes";
		if (windowParams.indexOf('width') < 0) {
			var width = 1150;
			if(isIE) {
				width = 1174;
			}
			windowParams += ', width=' + width;
		}
		if (windowParams.indexOf('height') < 0){
			windowParams += ', height=' + 835;
		}
		if(windowParams.indexOf('top') < 0) {
			windowParams += ', top=' + 50;
		}
		
		if(windowParams.indexOf('left') < 0) {
			windowParams += ', left=' + 50;
		}
	}
	
	var url = 'zoom_viewer.html?id=' + id + '&serialNo=' + serialNo;
	zoomViewerWindow = window.open(url, name, windowParams);
	if (!isIE) //This is a fix only for IE - when page fails to load it retry to load the page again for up to 5 times
		return;
				
	zoomViewerId = id;						//this for the following setTimeout to pass the parameter to the function
	zoomViewerName = name;					//this for the following setTimeout 
	zoomViewerSerialNo = serialNo;			//this for the following setTimeout
   	zoomViewerWindowParams = windowParams;  //this for the following setTimeout 
   	zoomViewerNumTries = numTries;			//this for the following setTimeout
	setTimeout('reloadZoomViewerIfNotLoaded();', 250);
}	
				
function reloadZoomViewerIfNotLoaded() {
	if(zoomViewerLoaded || zoomViewerNumTries>5) {
		return;
	}	
	openZoomViewer(zoomViewerId, zoomViewerName, zoomViewerSerialNo, zoomViewerWindowParams, zoomViewerNumTries+1);
}	

function setZoomViewerLoaded() { // This is called by zoom_viewer onload funtion
	zoomViewerLoaded = true;
}
//******************************************** Zoom Viewer section (End) ********************************************************
  
//******************************************** Login Panel section (Begin) ********************************************************
// This is to prevent Autocomplete displayes login info. autocomplete attribute can be set to 'off' for forms 
// but this is not stanmdard an hence not available in JSF form. This way it is gonna work for any browser that support javascript
function resetLoginForm() {   
	getById('loginForm:userName').value=''; 
	getById('loginForm:password').value='';; 
	getById('loginForm').style.visibility = 'visible';
	getById('loginForm:userName').focus();
}

function findTableSubViewId(doc) {
	var forms = doc.forms;
	for (var i=0; i<forms.length; i++) {
		var formId = forms[i].id;
		if (formId.indexOf('browseTableForm') >=0 ) 
			return formId.substring(0, formId.indexOf(':'));
	}
	return "";
}

//** This section is only for lgoin in a frame implementation
function onLoginClicked() {
	var loginPanel = getById('loginPanel');
	if ( loginPanel.style.visibility == 'visible' )
		loginPanel.style.visibility = 'hidden';
	else 
		loginPanel.style.visibility = 'visible';
}
    
function hideLoginPanel(parent) {
	if (parent)
		parent.document.getElementById('loginPanel').style.visibility = 'hidden';
	else
		getById('loginPanel').style.visibility = 'hidden';
}

function adjustLoginPanelPosition() {
	var displayLoginPanel = getById('headersubview:loginLinkForm:displayLoginPanel').value;
	var loginPanel = getById('loginPanel');
	if (displayLoginPanel == 'false') 
		loginPanel.style.visibility = 'hidden';
	else		
		loginPanel.style.visibility = 'visible';
		
	var loginLink = getById('headersubview:loginLinkForm:loginLink');
	if (loginLink) {
		var pos = findPos(loginLink);
		pos = adjustPosRelativeTomainContent(pos);
		
		loginPanel.style.left = pos.left - 270;  //align left
		loginPanel.style.top = pos.top + loginLink.offsetHeight;
	}
}

function loginFrameOnload() {
	var loginPanel = parent.document.getElementById('loginPanel');
	
	var loginOperation = getById('loginOperation').value;
	if (loginOperation=='cancelled') {
		loginPanel.style.visibility = 'hidden';
		getById('loginPanelConnectorForm:refreshLoginPanelLink').onclick();
		return;
	}
	
	var userLoggedIn = getById('loginPanelConnectorForm:userLoggedIn').value;
	if (userLoggedIn == 'true' && loginPanel.style.visibility == 'visible') {
		loginPanel.style.visibility = 'hidden';
		var tableReloadActionLink = parent.document.getElementById(findTableSubViewId(parent.document) + ':browseTableForm:reloadActionLink');
		if (tableReloadActionLink == null)  // Check to see if there is a table in the page (current mechanisim only able to keep status of the first table if there are multiple tables in the same page)
			parent.location.href = parent.location.href; //Reload the page 
		else 
			tableReloadActionLink.onclick();
	}
}

function logoutClicked() {
	var tableSubViewId = findTableSubViewId(document);
	if (tableSubViewId != "")	//this is only useful if after logout staying in the same page
		getById(tableSubViewId + ':browseTableForm:logoutLink').onclick();
	else
		getById('headersubview:loginLinkForm:logoutLink').onclick();
		
	return false;
}
//** end of - This section is only for lgoin in a frame implementation

//******************************************** Login Panel section (End) ********************************************************


//*********************************************** Database Homepage (Begin) *****************************************************
function processEnterKey(event) {
	if( !event ) 
	    if( window.event ) {
	      //Internet Explorer
	      event = window.event;
	    } else {
	      //total failure, we have no way of referencing the event
	      return;
	    }
	if (event.keyCode == 13  || event.which == 13 || event.charCode == 13) {
	    //DOM || NS 4 compatible || also NS 6+, Mozilla 0.9+
		if (searchLinkId!=null) {
			var searchLink = getById('mainForm:'+searchLinkId);
			searchLinkId=null;
			searchLink.onclick();
		}
	}
}		
   
function showGeneOptionsPanels(e) {
	// close gene function panel
	getById('mainForm:geneFunctionPanel').style.visibility = 'hidden';
	// open gene panel
	var pos = findPos(getById('mainForm:optionlink'));
	pos = adjustPosRelativeTomainContent(pos);
	var geneOptionsPanel = getById('mainForm:genePanel');
	geneOptionsPanel.style.top = pos.top + 20 + 'px';
	geneOptionsPanel.style.left = pos.left + 'px';// xingjun 23/09/2010 - added 'px' or it will not always get the position value
	geneOptionsPanel.style.visibility = 'visible';
	pos = findPos(geneOptionsPanel);
	pos = adjustPosRelativeTomainContent(pos);
	var genesUploadPanel = getById('uploadForm:uploadPanel');
	genesUploadPanel.style.top = pos.top + 110 + 'px';// xingjun 23/09/2010 - added 'px' or it will not always get the position value
	genesUploadPanel.style.left = pos.left + 'px';
	genesUploadPanel.style.visibility = 'visible';
}


function hideGeneOptionsPanels(e) {
//	var geneSearchResultOption = getById(document.forms['mainForm'].id+":geneSearchResultOption").value;
//	alert(geneSearchResultOption);
	getById('mainForm:genePanel').style.visibility = 'hidden';
	getById('uploadForm:uploadPanel').style.visibility = 'hidden';
}

// modified by xingjun - 21/04/2009 - separate the panels for gene and gene function search option
// - the two panels can only be exclusively opened
function showGeneFunctionOptionsPanel(e) {
	// close gene panel
	getById('mainForm:genePanel').style.visibility = 'hidden';
	getById('uploadForm:uploadPanel').style.visibility = 'hidden';
	
	// open gene function panel
	var pos = findPos(getById('mainForm:fnOptionlink'));
	pos = adjustPosRelativeTomainContent(pos);
	var geneFnOptionsPanel = getById('mainForm:geneFunctionPanel');
	geneFnOptionsPanel.style.top = pos.top + 20 + 'px';
	geneFnOptionsPanel.style.left = pos.left + 'px';
	geneFnOptionsPanel.style.visibility = 'visible';
}

// xingjun - 21/04/2009 - separate panels for gene and gene function options
function hideGeneFunctionOptionsPanels(e) {
//	var geneSearchResultOption = getById(document.forms['mainForm'].id+":geneSearchResultOption").value;
//	alert(geneSearchResultOption);
	getById('mainForm:geneFunctionPanel').style.visibility = 'hidden';
}

// xingjun - 21/04/2009 - when user specify expression summaries (genes) as the search option
// - stage drop-down list should be disabled; conversely enable it
function disEnableGeneStageInGenePanel() {
	var geneSearchResultOption = getById('mainForm:geneSearchResultOption').value;
	//alert(geneSearchResultOption);
	if (geneSearchResultOption == 'entry') {
		getById('mainForm:geneStage').disabled = false;
	} else {
		getById('mainForm:geneStage').disabled = true;
	}
	
	getById('uploadForm:uploadResultOption').value = geneSearchResultOption;	// this is to pass the value when upload form is sending the request
}

function disableTheilerStageInGenePanel() {
	var geneSearchResultOption = getById('mainForm:geneSearchResultOption').value;
	//alert(geneSearchResultOption);
	if (geneSearchResultOption == 'genes') {
		getById('mainForm:geneStage').disabled = true;
	} else {
		getById('mainForm:geneStage').disabled = false;
	}
	
	getById('uploadForm:uploadResultOption').value = geneSearchResultOption;	// this is to pass the value when upload form is sending the request
}

// xingjun - 27/05/2010 - set value for tag inputHidden 2 assign value to geneStage of DatabaseHomeBean
function setGeneStageHidden() {
	var geneStage = getById('mainForm:geneStage').value;
	//alert(geneStage);
	getById('mainForm:geneStageHidden').value = geneStage;
}

// xingjun - 21/04/2009 - when user specify expression summaries (genes) as the search option
// - stage drop-down list should be disabled; conversely enable it
function disableTheilerStageInGeneFunctionPanel() {
	var gfSearchResultOption = getById('mainForm:geneFunctionSearchResultOption').value;
//	alert(gfSearchResultOption);
	if (gfSearchResultOption == 'genes') {
		getById('mainForm:geneFunctionStage').disabled = true;
	} else {
		getById('mainForm:geneFunctionStage').disabled = false;
	}
}

//************************************************ Database Homepage (End) ******************************************************


//************************************************* Collections (Begin) *********************************************************
function collectionListOperationClicked(event, subviewId, attributeSelectItemId) {
	if (processSelectionsForLink(event, subviewId)) {
		addCollectionAttribute(getItemFromEvent(event), attributeSelectItemId);
		return true;
	}
	return false;
}

function addCollectionAttribute(item, attributeSelectItemId) {
	var attributeItem = getById(attributeSelectItemId);
	if (attributeItem) 
		item.href += '&collectionAttribute=' + attributeItem.value; 
}

//************************************************** Collections (End) **********************************************************

//************************************************* Tabbed Panel (Begin) ********************************************************
function showTabPane(tabPanelId, newTab, activeHeaderClass, inactiveHeaderClass, firstSubHeaderClass, activeSubHeaderClass, inactiveSubHeaderClass) {
	var currentTabHolder = document.getElementsByName('currentTabHolder');
	var currentTab = currentTabHolder[0].value;
	if (currentTab == newTab) 
		return false;
		
	getById(tabPanelId + 'Header' + newTab).className = activeHeaderClass;
	getById(tabPanelId + 'Header' + currentTab).className = inactiveHeaderClass;
	getById(tabPanelId + 'SubHeader' + newTab).className = activeSubHeaderClass + ((newTab==0)? ' '+firstSubHeaderClass : '');
	getById(tabPanelId + 'SubHeader' + currentTab).className = inactiveSubHeaderClass + ((currentTab==0)? ' '+firstSubHeaderClass : '');
	getById(tabPanelId + 'TabbedPaneContent' + newTab).style.display = 'block';
	getById(tabPanelId + 'TabbedPaneContent' + currentTab).style.display = 'none';
	
	// update current tab value in all forms
	for (var i=0; i<document.forms.length; i++ ) {
		var form = document.forms[i];
		form.elements['currentTabHolder'].value = newTab;
/* not needed for the new template		
		adjustTableBodyForm(form);
*/		
	}

	adjustTableBodyPanels(findTemplateBodyPanel(tabPanelId + 'TabbedPaneContent' + newTab));
	
	return false;
}

function findTemplateBodyPanel(tabDivId) {
	var bodyPanels = document.getElementsByName('tableBodyPanel');
	var tabBodyPanels = new Array();
	for (var i=0, j=0; i<bodyPanels.length; i++) 
		if (hasParent(bodyPanels[i], tabDivId))
			tabBodyPanels[j++] = bodyPanels[i];
	return tabBodyPanels;
}

function hasParent(element, parentId) {
	var parent = element;
	while (parent=parent.parentNode) 
		if (parent.id == parentId)
			return true;
		
	return false;
}



/* not needed for the new template
function adjustTableBodyForm(form) {
	if (!form.id || form.id.indexOf('browseTableForm')<0)
		return;
	var tableBodypanel = getTableBodyPanel(form);
	var styleClass = tableBodypanel.className;
	if (styleClass.indexOf('defaultWidth1')<0) 
		styleClass = styleClass.replace(/defaultWidth/i, "defaultWidth1");
	tableBodypanel.className = styleClass;
}

function adjustInitialCurrentTabWidth(currentTab) {
	if (!currentTab)
		return;
	var forms = document.forms;
	for (var i=0; i<forms.length; i++ ) {
		if (forms[i].id.indexOf('browseTableForm') < 0)
			continue;
		var parent = forms[i];
		while ( parent = parent.parentNode ) {
//			if (parent.nodeType = 'div' && parent.id && parent.id.indexOf('tabbedpaneContent'+currentTab)>=0) {
			if (parent.id && parent.id.indexOf('TabbedPaneContent'+currentTab)>=0) {
				adjustTableBodyForm(forms[i]);
				return;
			}
		}			
	}	
}
*/
//*************************************************** Tabbed Panel (End) ********************************************************

//****************************************************** Misselaneous ***********************************************************
function getItemFromEvent(e) {
	var evnt = e;
	if (!evnt)
		evnt = window.event;
	var node;
	if (evnt.target)
		node = evnt.target;
	else if (evnt.srcElement)
		node = evnt.srcElement;
	return node;
}


function getByIdInForm(form, id) {
	document.forms[0]
	for (var i=0; i<form.elements.length; i++) 
		if (form.elements[i].id==id)
			return form.elements[i];
	return null;
}

function getById(id){
	var x=null;
	if (document.getElementById) {
		x = document.getElementById(id);	
	}
	else if (document.all) {
		x = document.all[id];
	}
	else if (document.layers)	{
		x = document.layers[id];
	}
	return x;
}

function getFormId(e) {
	var evnt = e;
	if (!evnt){
		evnt = window.event;
	}
	var node;
	if (evnt.target) {
		node = evnt.target;
	}
	else if (evnt.srcElement) {
		node = evnt.srcElement;
	}
	if (node.nodeType == 3) // defeat Safari bug
		node = node.parentNode;
	if (node.elements)		// node is a form tag
		return node.id;
	var form = getParentForm(node);
	if (form!=null)
		return form.id;

	return null;
}				

function getParentForm(node) {
	var parent = null;						
	while (parent=node.parentNode) {
		if(!parent) {
			return null;
		}
		if (parent.elements) {		// parent is a form tag
			return parent;
		}
		node = parent;		
	}
	return null;			
}

function getSubviewId(e) {
	var formId = getFormId(e);
	if (formId == null)
		return '';
	var i = formId.lastIndexOf(':');	//assumes that actual form id is the last part of the id (lat prefix after ":") and the rest is view id
//	var i = formId.indexOf(':');	//assumes that view id is the first prefix before ":" and the rest is the actual form id
	if (i == -1) 
		return '';
	return formId.substr(0, i);
}

function getPageURI() {
	return document.URL.substr(document.URL.indexOf('/pages/')+7);
}


/* this has stopped working on firefoxso I replaced it with the following code. Mehran 28/08/09
function clickLink (id) {
	var link = getById(id);
	if (document.createEvent) {
		var evObj = document.createEvent('MouseEvents');
		evObj.initEvent('click', true, false);
		link.dispatchEvent(evObj);
	}
	else 
		if (document.createEventObject) 
			link.click();
//			link.fireEvent('onclick');	//Despite the spec this is not yet implemented even in IE7!!
}
*/

function clickLink(id) {
	var linkobj = getById(id);
	if (linkobj.getAttribute('onclick') == null) 
		if (linkobj.getAttribute('href')) 
			document.location = linkobj.getAttribute('href');
	else 
		linkobj.onclick();
}

function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		if (obj.getBoundingClientRect) { // IE
			var box = obj.getBoundingClientRect();
			var scrollTop = doc.documentElement.scrollTop || doc.body.scrollTop;
			var scrollLeft = doc.documentElement.scrollLeft || doc.body.scrollLeft;
			curleft = box.left + scrollLeft;
			curtop = box.top + scrollTop;
		}
		else {
			curleft = obj.offsetLeft;
			curtop = obj.offsetTop;
			while (obj = obj.offsetParent) {
				curleft += obj.offsetLeft;
				curtop += obj.offsetTop;
			}
		}
	}
	return {left:curleft, top:curtop};
}

// This is developed for new template to adjust pos for items inside the main content
function adjustPosRelativeTomainContent(pos) {
//		if(isIE) { /* if using display:table for mainCointent style then this line and the else part should be uncomented 
		var maincontent = getById("mainContent");
		if (!maincontent)
			return pos;
		pos.left = pos.left - maincontent.offsetLeft;
		pos.top = pos.top - maincontent.offsetTop;
/*		
	}
	else 
		pos.left += getById("sidebar1").offsetWidth;
*/	
	return pos;
}


// Clears JSF hidden variables associated with actionMethos. It is called to fix problem with ajax4jsf when browser back button is pressed
function clearActionMethodsHistory() {
	for (var i=0; i<document.forms.length; i++) {
		var formElements = document.forms[i].elements;
		for (var j=0; j<formElements.length; j++) {
			if (formElements[j].type == 'hidden' && formElements[j].name.indexOf(':_idcl')>=0) 
				formElements[j].value='';
		}	
	}
}

function appendHiddenInput(form, id, value) {
	var input = getByIdInForm(form, id);
	if (!input) {
		input = document.createElement('input');
		if (input) {
			input.type = 'hidden';
			input.id = id;
			input.name = id;
			input.value = value;
			form.appendChild(input);
		}
	}
	else 
		input.value = value;
		
	return input;
}

// this is to enable all items before submitting a form to make sure that input values are submitted in the request
function enableInputComponents(form) {	
	for (var i=0; i<form.elements.length; i++) 
		if (form.elements[i].disabled == true)
			form.elements[i].disabled = false;
}

function isDateValid(date, mode) {
//	Validates a date string. The date must be a real date i.e. 2-30-2000 would not be accepted';
	if (!date || date=='')
		return false;
	//Mode: not passed (default) is DD/MM/YYYY; mode=1 is MM/DD/YYYY; mode=2 is YYYY/MM/DD
	var separator = "\\/|-";   //"\\/|-|\\.";
	var dayPattern1_1 = "(?:31)";
	var dayPattern1_2 = "(?:29|30)";
	var monthPattern1_1 = "(?:0?[13578]|1[02])";
	var monthPattern1_2 = "(?:0?[1,3-9]|1[0-2])";
	var yearPattern1 = "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})";
	var datePattern1 = "(?:(?:"+ dayPattern1_1 +"("+separator+")" + monthPattern1_1 + ")\\1|(?:" + dayPattern1_2 + "("+separator+")" + monthPattern1_2 +"\\2))" + yearPattern1;
	if (mode==1)
		datePattern1 = "(?:(?:"+ monthPattern1_1 + "("+separator+")" + dayPattern1_1 + ")\\1|(?:" + monthPattern1_2 + "("+separator+")" + dayPattern1_2 +"\\2))" + yearPattern1;
	if (mode==2)
		datePattern1 = yearPattern1 + "("+separator+")" + "(?:"+ monthPattern1_1 + "\\1" + dayPattern1_1 + "|" + monthPattern1_2 + "\\1" + dayPattern1_2 +")";
		
	var dayPattern2 = "(?:29)";
	var monthPattern2 = "0?2";
	var yearPattern2 = "(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))";
	var datePattern2 = "(?:" + dayPattern2 + "("+separator+")" + monthPattern2 + "\\3" + yearPattern2 + ")";
	if (mode==1)
		datePattern2 = "(?:" + monthPattern2 + "("+separator+")" + dayPattern2 + "\\3" + yearPattern2 + ")";
	if (mode==2)
		datePattern2 = "(?:" + yearPattern2 + "("+separator+")" + monthPattern2 + "\\2" + dayPattern2 + ")";
	
	var dayPattern3 = "(?:0?[1-9]|1\\d|2[0-8])";
	var monthPattern3 = "(?:(?:0?[1-9])|(?:1[0-2]))";
	var yearPattern3 = "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})";
	var datePattern3 = dayPattern3 + "("+separator+")" + monthPattern3 + "\\4" + yearPattern3;
	if (mode==1)
		datePattern3 = monthPattern3 + "("+separator+")" + dayPattern3 + "\\4" + yearPattern3;
	if (mode==2)
		datePattern3 = yearPattern3 + "("+separator+")" + monthPattern3 + "\\3" + dayPattern3;
	
	var datePattern = "(?:(?:" + datePattern1+ "|" + datePattern2 + "|" + datePattern3 + ")($|\\ (?=\\d)))?";
	var timePattern = "(((0?[1-9]|1[012])(:[0-5]\\d){0,2}(\\ [AP]M))|([01]\\d|2[0-3])(:[0-5]\\d){1,2})?$";
//	var fullDatePattern = "^(?=\\d)" + datePattern + timePattern; // replaced by next line because it won't work in mozilla
	var fullDatePattern = datePattern + timePattern; 

	var dateRegExp = new RegExp(fullDatePattern);
	var matchResult = date.match(dateRegExp);
	var dateMatched = matchResult && matchResult[0] && matchResult[0].length>0;
	return dateMatched;
}

function setMessage(id, message) {
	if (isIE) {
		getById(id).innerText = message;
	} else {	
		getById(id).firstChild.nodeValue = message;
	}
}

//trim removes leading and trailing white spaces from a string
function trim(s) {
	return s.replace(/^\s+/, "").replace(/\s+$/, "");
}

function openTreeviewApplet(param){
	var winX = (document.all)?window.screenLeft:window.screenX;
	var winY = (document.all)?window.screenTop:window.screenY;
	if (winX==0) 
		winX++;
	if (winY==0) 
		winY++;
	var pos = 'left='+winX+',top='+winY+',screenX='+winX+',screenY='+winY;
	appletWin = window.open("treeview_applet.html?"+param,'treeviewApplet','menubar=0,resizable=0,width=400,height=150,'+pos);
	setTimeout("appletWin.blur();", 500);
	setTimeout("window.focus();", 1500);
	return false;
}

//**************************************************** global page initialisation *******************************************************
function initialisePage(statusParams) {				//adds status params, initialise tab pages, apply predictive texts, fix layout problems
	var currentTabItem = getById('currentTabHolder');
	if (typeof(noSmartScroll) == "undefined")
		noSmartScroll = false;
	for (var i=0; i<document.forms.length; i++ ) {
		appendHiddenInput(document.forms[i], 'statusParams', statusParams);
		if (currentTabItem) 
			appendHiddenInput(document.forms[i], 'currentTabHolder', currentTabItem.value);
	}
/* This is not needed for the new template	
	if (currentTabItem) 
		adjustInitialCurrentTabWidth(currentTabItem.value)
*/		

	adjustTableBodyPanels();
}

function adjustTableBodyPanels(bodyPanels) {
	if (noSmartScroll)
		return;
	if (!bodyPanels) 
		bodyPanels = document.getElementsByName('tableBodyPanel');
	var mainContentTable = getById('mainContentInnerTable');
	var headerWidth = getById('headernew').offsetWidth;
	if (isMac || isMozillaWindows) { // this to adjust a layout specificly for Safari & firfox on mac & windows
		for (var i=0; i<bodyPanels.length; i++) 
			bodyPanels[i].style.width = '500px';
		var pos = findPos(mainContentTable);
		var mainContentWidth = headerWidth - pos.left - 10;
		mainContentTable.style.width = mainContentWidth + 'px';
	}
	for (var i=0; i<bodyPanels.length; i++) {
		if (isMac || isMozillaWindows) // this to adjust a layout specificly for Safari & firfox on mac & windows
			bodyPanels[i].style.width = mainContentWidth + 'px';
		if (isIE) { // this to adjust a layout problem in IE
			var pos = findPos(bodyPanels[i]);
			if (bodyPanels[i].parentNode.offsetWidth != mainContentTable.offsetWidth || bodyPanels[i].parentNode.offsetWidth > headerWidth - pos.left -10)
				bodyPanels[i].parentNode.style.width = headerWidth - pos.left -10 + 'px';
//				bodyPanels[i].style.width = headerWidth - pos.left - 10 + 'px';
//				bodyPanels[i].style.width = headerWidth - pos.left + 'px';
			bodyPanels[i].style.paddingBottom = (bodyPanels[i].scrollWidth > bodyPanels[i].offsetWidth)?'12px':'0px'; /* 12px bottom is to avoid overlaping of horizontal scroll bar with the content in IE */
		}
		if (bodyPanels[i].parentNode.id == 'tableBodyContainer') {	// if in flexible scroll mode
			var tableForm = getParentForm(bodyPanels[i]);	//This is to adjust initial panel size to min & max size
			initHeightVars(null, tableForm);	//This is to adjust initial panel size to min & max size
			changeTableBodyPanelHeight(null, tableForm);	// it will also call initialiseTableBodyPanelScrolling()
		}
	}
}

function findOffsets(panel, table) {
	var voffset = 0;
	var hoffset = 0;
	var panelHeight = panel.offsetHeightpanel;
	var scrollHeight = panel.scrollHeight;
	if (table)
		scrollHeight = table.tBodies[0].scrollHeight + table.offsetHeight - table.tBodies[0].offsetHeight;

	if (scrollHeight > panel.offsetHeight) 
		hoffset = (isMozillaWindows)? 17 : 18;
	if ((isIE || isMozilla) && !noSmartScroll) {	// if not Downgraded 
		if (!panel || !panel.parentNode || !panel.parentNode.offsetWidth)	// This is special case that might happen in IE
			return {h:hoffset, v:voffset};
//		tableBodyPanel.style.width = panel.parentNode.offsetWidth - hoffset + 'px';
		panel.style.width = panel.parentNode.offsetWidth - hoffset + 'px';
	}
	if (panel.scrollWidth > panel.offsetWidth) 
		voffset = 15;
	if (hoffset==0 && voffset>0) 
		if (scrollHeight > panel.offsetHeight - voffset) {
			hoffset = (isMozillaWindows)? 17 : 18;
			if ((isIE || isMozilla) && !noSmartScroll) 	// if not Downgraded 
				tableBodyPanel.style.width = panel.parentNode.offsetWidth - hoffset + 'px';
		}
	return {h:hoffset, v:voffset};
}

function initialiseTableBodyPanelScrolling(bodyPanel) {		//This is to keep generic tables header when scrolling
	if (bodyPanel.parentNode.id != 'tableBodyContainer') 	// Not in flexible scroll mode
		return;
	
	if (!isIE && !isMozilla || noSmartScroll) {	// Downgrade if browser is not Firfox/Mozilla or IE or project is EureGene
		bodyPanel.style.overflowY = 'auto';
		return;
	}
	
	var table = getChild(bodyPanel, ':genericTable', false);
	var tableHead=table.tHead;
	var tableBody=table.tBodies[0];
	var bodyPanelDumy = getChild(tableBodyPanel.parentNode, 'bodyPanelDumy');
	var internalDumy = getChild(bodyPanelDumy, 'internalDumy');
	
	bodyPanelDumy.style.width = (isMozillaWindows)? '17px' : '18px';


	var paddingBottom = 5;

	if(isIE) {
//		tableBodyPanel.style.overflowX = 'auto';
		table.borderColor='white';
		tableHead.className='bodySectionTable';

		var offset = findOffsets(tableBodyPanel);
	}
	else {
		tableBody.style.overflow = 'hidden';
		var offset = findOffsets(tableBodyPanel, table);
		tableBody.style.height=bodyPanel.offsetHeight - tableHead.offsetHeight - offset.v - paddingBottom + 'px';
	}
	
	if (offset.h == 0) {
		bodyPanelDumy.style.height = 0;
		internalDumy.style.height = 0;
		tableBody.style.height = '100%';
		return;
	}
	var dumyHeight = bodyPanel.offsetHeight - offset.v;
	var internalDumyHeight = tableBody.scrollHeight + tableHead.offsetHeight + paddingBottom;
	if(isIE) 
		internalDumyHeight = tableBodyPanel.scrollHeight;
	bodyPanelDumy.style.height = dumyHeight + 'px';
	internalDumy.style.height = internalDumyHeight + 'px';
	tableBody.scrollTop = 0;
	bodyPanelDumy.scrollTop = 0;
	bodyPanelDumy.style.visibility = 'visible';
}		

function tableBodyPanelOnscroll(tableBodyContainer) {
//	if(!isIE && (!isNetscape || isSafari) { // It should never be called if this is true
//		return;
	var bodyPanel = getChild(tableBodyContainer, 'tableBodyPanel');
	var bodyPanelDumy = getChild(tableBodyContainer, 'bodyPanelDumy');
	var table = getChild(bodyPanel, ':genericTable', false);
	if (isIE) {
		var tableHeadTr = table.tHead.childNodes[0]
		tableHeadTr.style.top = bodyPanelDumy.scrollTop + 'px';
		bodyPanel.scrollTop = bodyPanelDumy.scrollTop;
	}
	else {
		var tableBody=table.tBodies[0];
		tableBody.scrollTop=bodyPanelDumy.scrollTop;
	}	
	
/*
	var d1 = bodyPanelDumy.scrollHeight - bodyPanelDumy.offsetHeight;
	var d2 = tableBody.scrollHeight - tableBody.offsetHeight;
	var t1 = bodyPanelDumy.scrollTop;
	var t2 = parseInt(1.0*t1*d2/d1+0.5);
	alert("d1="+d1+"\nd2="+d2+'\nt1='+t1+'\nt2='+t2);
	tableBody.scrollTop=t2;
*/
}

function getChild(node, id, exactMatch) {	// exactMatch is optional default value is true
	if (exactMatch == null)
		exactMatch = true;
	var children = node.childNodes;
	for (var i=0; i<children.length; i++) {
		if (!children[i] || !children[i].id)
			continue;
		if (exactMatch) {
			if (children[i].id.indexOf(id) == children[i].id.length-id.length)
				return children[i];
			}
		else {
			if (children[i].id.indexOf(id) >= 0)
				return children[i];
		}
	}
	return null;
}

/***** xingjun - 30/09/2010 - moved from lab_ish_edit.jsp *****/
var w;
function openDesktop1(obj) {
	//window.name="lab_ish_edit_window" 
	openZoomViewer(obj, 'desktop1', '1');
	w=window.open('ish_edit_expression.html?id='+obj,'desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');
	w.focus();
} 
function closeImageViewer() {
	w.close();
	if (zoomViewerWindow != null)
		zoomViewerWindow.close();
}		

/***** moved here from boolean_test.jsp - xingjun - 27/07/2010 - start *****/
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
/*****	moved here from boolean_test.jsp - xingjun - 27/07/2010 - end *****/
	
