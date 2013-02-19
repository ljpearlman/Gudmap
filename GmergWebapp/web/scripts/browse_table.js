
function getTableFormId(e) {
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
	if(node && node.id) {
		var i = node.id.indexOf(':browseTableForm');
		if (i >= 0)
			return node.id.substr(0, i) + ':browseTableForm';
	}
	return getFormId(e);
}

function getTableViewId(e) {
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
	if(node && node.id) {
		var i = node.id.indexOf(':browseTableForm');
		if (i >= 0)
			return node.id.substr(0, i);
	}
	return getSubviewId(e);
}
	
function pageNumSubmition(e) {
	var viewId = getTableViewId(e);
	getById(viewId+':browseTableForm:gotoPage').onclick(e);
	return false;
}

function validatePageNum(e) {
	var viewId = getTableViewId(e);
	var pageNum = getById(viewId+':browseTableForm:pageNum').value;
	var maxPage = getById(viewId+':browseTableForm:maxPageNum').value;
	if (isNaN(pageNum) || pageNum<1 || pageNum>parseInt(maxPage)) {
		alert('Please enter a valid number between ' + 1 + ' and ' + maxPage + ' in \"Go to page\" box.');
		return false;
	}
	return true;
}

function validatePageNum2(e) {
	var viewId = getTableViewId(e);
	var pageNum = getById(viewId+':browseTableForm:pageNum2').value;
	var maxPage = getById(viewId+':browseTableForm:maxPageNum').value;
	if (isNaN(pageNum) || pageNum<1 || pageNum>parseInt(maxPage)) {
		alert('Please enter a valid number between ' + 1 + ' and ' + maxPage + ' in \"Go to page\" box.');
		return false;
	}
	return true;
}

function clickPerPage(e) {
	var viewId = getTableViewId(e);
	getById(viewId+':browseTableForm:perPage').onclick();
	return false;
}

//***************************************************************************************************************
//* select/deselect all

function selAll(e) {
	var formId = getTableFormId(e);
	var form = getById(formId);
	for (var i = 0; i < form.elements.length; i++) {
		if(form.elements[i].type=='checkbox' && form.elements[i].name.indexOf('genericTable')>-1){
			form.elements[i].checked =true;
		}
	}
}
		
function deSelAll(e) {
	var formId = getTableFormId(e);
	var form = getById(formId);
	for (var i = 0; i < form.elements.length; i++) {
		if(form.elements[i].type=='checkbox' && form.elements[i].name.indexOf('genericTable')>-1){
			form.elements[i].checked =false;
		}
	}
}

function toggleSelectAll(e, item, checkBoxId) {	//checkBoxId is optional
	var formId = getTableFormId(e);
	var value = item.checked;
	var form = getById(formId);
	var changed = false;
	var idPattern = (checkBoxId)? checkBoxId : 'genericTable';
	for (var i = 0; i < form.elements.length; i++) {
		if(form.elements[i].type=='checkbox' && form.elements[i].name.indexOf(idPattern)>-1)
			if(form.elements[i].checked != value) {
				form.elements[i].checked = value;
				changed = true;
			}
	}
	return changed;
}

function saveSelections(e) {
	var formId = getTableFormId(e);
	var selections = getSelections(formId, 'genericTable', getById(formId+':cellSelection').value);
	getById(formId+':selections').value = selections;
	if((selections.indexOf('1')>=0) == false) {
		alert("Please make a selection from the list of entries");
		return false;
	}
	return true;
}	

function getSelections(formId, id, numCols, idSuffix) {	
	var form = getById(formId);
	var selections = "";
	var selectionArray = new Array();
	var numRows = getById(formId+':resultsPerPage').value;
	var patternString = ':.*?' + id + '.*?:(\\d+):'; 
	if (idSuffix)
		patternString = '.*?:(\\d+):.*?' + id ; 
	if (!numCols)
		numCols = 0;
	else {
		numCols = parseInt(numCols);
		patternString += '.*?(\\d+):';	// note extra \ is needed because we use a string literal
	}
	var pattern = new RegExp(patternString);
	var totalSelectionsNum = 0;
	for (var i=0, selectionsNum=0; i<form.elements.length; i++) {
		if(form.elements[i].type=='checkbox' && form.elements[i].name.indexOf(id)>=0 && form.elements[i].name.indexOf('toggleSelectAll')<0){
			totalSelectionsNum++;
			if(form.elements[i].checked) {
				var index = form.elements[i].name.match(pattern);
//alert("name="+form.elements[i].name+"\npattern="+pattern+"\nindex="+index+"\nnumCols="+numCols);
				var selectionIndex =  ((numCols == 0)? index[1] : (parseInt(index[1])*numCols+parseInt(index[2])));
				selectionArray[selectionsNum++] = selectionIndex;
			}
		}
	}
	selectionArray.sort(sortNumber);
	
	var size = totalSelectionsNum;
	if (numCols > 0) {
		var numRows = getById(formId+':actualRowsPerPage').value;
		size = numCols * numRows;
	}
	for (var i=0,j=0; i<size; i++) 
		if (j<selectionsNum && i==selectionArray[j]) {
			selections += '1';
			j++;
		}
		else
			selections += '0';
//	alert(selections);
	return selections;
}

function sortNumber(a,b) {
	return a - b;
}
/*
function getSelections(e, id) {
	var formId = getTableFormId(e);
	var form = getById(formId);
	var selections = "";
	for (var i = 0; i < form.elements.length; i++) {
		if(form.elements[i].type=='checkbox' && form.elements[i].name.indexOf(id)>=0){
			if(form.elements[i].checked) {
				selections += '1';
			} else {		
				selections += '0';
			}		
		}
	}
	return selections;
}
*/

//************************************* Access table selections from external form (Begin) **************************************
/*
function selectionsTableViewName(viewId) {
	var form = getById(viewId + ':browseTableForm');
	var selectionsNum = 0;
	var selections = "";
	for (var i=0; i<form.elements.length; i++) 
		if (form.elements[i].type=='checkbox' && form.elements[i].name.indexOf('genericTable')>-1)
			if(form.elements[i].checked) {
				selections += '1';
				selectionsNum++;
			} else 
				selections += '0';
	return [selections, selectionsNum];
	
}
*/

function processSelectionsForAction(e, subviewId) {
//	var selections = getSelectionsFromTable(subviewId);
	var selectionsFormId = subviewId + ':browseTableForm';
	var selections = getSelections(selectionsFormId, 'genericTable', getById(selectionsFormId+':cellSelection').value);
	if (selections.indexOf("1") < 0) {
		alert("Please make a selection from the list of entries");
		return false;
	}
	var tableViewName = document.forms[selectionsFormId]['tableViewName'].value;
	var formId = getFormId(e);
	appendHiddenInput(document.forms[formId], 'selectionsString', selections);
//	appendHiddenInput(document.forms[formId], 'selectionsTableViewName', tableViewName);
	appendHiddenInput(document.forms[formId], 'tableViewName', tableViewName);
	return true;
}

function processSelectionsForLink(e, subviewId) {
	if (subviewId==null)
		subviewId = getSubviewId(e);
	var selectionsFormId = subviewId + ':browseTableForm';
	var selections = getSelections(selectionsFormId, 'genericTable', getById(selectionsFormId+':cellSelection').value);
	if (selections.indexOf("1") < 0) {
		alert("Please make a selection from the list of entries");
		return false;
	}
	var tableViewName = getById(subviewId + ':browseTableForm')['tableViewName'].value;
	var item = getItemFromEvent(e)
	item.href = removeRequestParams(item.href, ['selectionsString', 'tableViewName']);
	if(item.href.indexOf('?')<0)
		item.href += '?';
	else 
		item.href += '&';
	
//	item.href += 'selectionsString=' + selections + '&selectionsTableViewName=' + tableViewName;
	item.href += 'selectionsString=' + selections + '&tableViewName=' + tableViewName;
	return true;
}	

function removeRequestParams(url, params) { //used locally 
	var paramsIndex = url.indexOf('?');
	if (paramsIndex<0) 
		return;
	var allParams = url.substr(paramsIndex+1).split('&');
	var newParams = '';
	for (var i=0; i<allParams.length; i++) {
		var remove = false;
		for(var j=0; j<params.length; j++)
			if (allParams[i].indexOf(params[j]+'=') == 0) {
				remove = true; 
				break;
			}			
		if (!remove)
			newParams += ((i>0)?'&':'') + allParams[i];
	}
	return url.substring(0, paramsIndex+1) + newParams;
}

function passTableViewName(e, subviewId) {
	var tableViewName = getById(subviewId + ':browseTableForm')['tableViewName'].value;
	var formId = getFormId(e);
	appendHiddenInput(document.forms[formId], 'tableViewName', tableViewName);
}

//************************************** Access table selections from external form (End) ***************************************
//***************************************************************************************************************
//* Filter Selection

function setStatus(status, id) {
	var item = getById(id);
	if (item) 
		item.disabled = status;
}

function setStatusMultiple(status, formId, id) {
	var form = getById(formId);
	for (var i = 0; i<form.elements.length; i++) {
		if((form.elements[i].type=='checkbox' || form.elements[i].type=='radio') && form.elements[i].name.indexOf(id)>=0)
			form.elements[i].disabled = status;
	}
}

function setStatusCalendar(status, idPrefix, index){
	calendarButton = getById(idPrefix + 'filterDateValue'+index+'Button');
	if (calendarButton) {
		calendarButton.style.visibility = (status)? 'hidden' : 'visible';
		setStatus(status, idPrefix + 'filterDateValue' + index);
	}
}

function setCalendarIds(formId) {
	var form = getById(formId);
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) {
		
			var idPrefix = item.name.substr(0, item.name.lastIndexOf(':')+1);
			inputCalendar = getById(idPrefix + 'filterDateValue1');
			if (!inputCalendar)
				continue;
			var siblings = inputCalendar.parentNode.childNodes;
			var index = '1';
			for (var j=0; j<siblings.length; j++) {
				if (siblings[j].nodeName == 'IMG' || (siblings[j].nodeName=='INPUT' && siblings[j].type.toLowerCase()=='button')) {
					siblings[j].id = idPrefix + 'filterDateValue' + index + 'Button';
					if (isIE) { // in IE the popup calendar goes under the filter panel so I had to find a workaround (pain in the neck)
						var onclickFunction = siblings[j].onclick.toString();
						var p1 = onclickFunction.indexOf('{');
						var p2 = onclickFunction.lastIndexOf('}');
						var calendarDivId = idPrefix + 'filterDateValue' + index + 'Span_calendarDiv';
						var body= onclickFunction.substring(p1+1, p2-1) + ';adjustCalendarZIndex("'+calendarDivId+'");';
						siblings[j].onclick = new Function('event', body);
					}
					if (index=='2')
						break;
					index = '2';
				}
			}
			if (!item.checked) {
				setStatusCalendar(true, idPrefix, '1');
				setStatusCalendar(true, idPrefix, '2');
			}
		}
	}
}

function adjustCalendarZIndex(calendarDivId) {
	var div = document.getElementById(calendarDivId);
	div.style.visibility = 'hidden';
	divv = div;
	setTimeout('divv.style.zIndex = 1000; divv.style.visibility = "visible";',400);
}
 
function filterSelection(e, selectItem) {
	var disabled = !selectItem.checked;  //When this function is called the check box value is changed because it was clicked
	var formId = getTableFormId(e);
	var idPrefix = selectItem.name.substr(0, selectItem.name.lastIndexOf(':')+1);
	
	setFilterDisabled (disabled, formId, idPrefix);
}

function setFilterDisabled (disabled, formId, idPrefix) {
	setStatus(disabled, idPrefix + 'filterValue1');
	setStatus(disabled, idPrefix + 'filterListValue1');
	setStatus(disabled, idPrefix + 'filterMultipleValue');
	setStatus(disabled, idPrefix + 'filterRangeSelect');
	var rangeSelect = getById(idPrefix + 'filterRangeSelect');
	if (rangeSelect) {
		setStatus(!rangeSelect.checked || disabled, idPrefix + 'filterValue2');
		setStatus(!rangeSelect.checked || disabled, idPrefix + 'filterListValue2');
		setStatusCalendar(!rangeSelect.checked || disabled, idPrefix, '2');
	}
	setStatusMultiple(disabled, formId, idPrefix + 'filterCheckboxValue');
	setStatusMultiple(disabled, formId, idPrefix + 'filterRadioValue');
	setStatusCalendar(disabled, idPrefix, '1');
}

function filterRangeSelectClicked(e, rangeSelectItem) {
	var idPrefix = rangeSelectItem.name.substr(0, rangeSelectItem.name.lastIndexOf(':')+1);
	var disabled = !rangeSelectItem.checked || !getById(idPrefix + 'filterSelectCheckbox').checked;  
	setStatus(disabled, idPrefix + 'filterValue2');
	setStatus(disabled, idPrefix + 'filterListValue2');
	setStatusCalendar(disabled, idPrefix, '2');
}

function disableNonActiveFilters(e, formId) {
	var form = getById(formId);
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) 
			filterSelection(e, item);
	}
}

function enableAllFilters(viewId) {
	var form = getById(viewId + ':browseTableForm');
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.disabled && item.name.indexOf('filterList:') >= 0) 
			item.disabled = false;
	}
}

function setAllFiltersState(state, e) {
	var formId = getTableFormId(e);
	var form = getById(formId);
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) {
			item.checked = state;
			filterSelection(e, item);
		}
	}
}

function filterClicked(e) {
	var viewId = getTableViewId(e);
//	var columnSelectionPanel = getById(viewId+':browseTableForm:columnSelectionPanel');
	var tableViewName = getById(viewId+':browseTableForm:tableViewName').value;
	var filterPanel = getById(tableViewName+':filterPanel');
	var windowSize = getWindowDimensioins();
	filterPanel.style.maxHeight = (windowSize.height - 40) + 'px';

	if (filterPanel.style.visibility == 'hidden') {  // ShowFilterPanel
		var formId = viewId+':browseTableForm';
		filterPanel.style.overflow = 'auto';
		var filterImage = getById(viewId+':browseTableForm:modifyFilterImage');
		if (filterImage) {
			filterImage.src = '../images/ApplyFilter_btn.png';	
			filterImage.alt = 'ApplyFilter';
			filterImage.title = 'Click to apply filter to this result';
		}
		else
			filterImage = getById(viewId+':browseTableForm:applyFilterImage');
		// This is to position the panel under the ImageSelection button (it is really a fix for IE since it already ok in mozilla)
		var pos = findPos(filterImage);
		pos = adjustPosRelativeTomainContent(pos);
//		filterPanel.style.left = pos.left + 'px';  //align left
		filterPanel.style.left = Math.max(0, Math.round(pos.left + (filterImage.offsetWidth - filterPanel.offsetWidth)/2)) + 'px'; //align centre
		filterPanel.style.top = (pos.top + filterImage.offsetHeight - 6) + 'px';
		setPanelMaxHeight(filterPanel);

		var filterSelections = getById(viewId+':browseTableForm:filterSelections');
		filterSelections.value = getSelections(formId, 'filterSelectCheckbox', 0, true);
		
		var firstTime = getById(viewId+':browseTableForm:filterDisplayForFirstTime');
		if (firstTime.value=='yes') {
			setCalendarIds(formId);
			disableNonActiveFilters(e, formId);
			firstTime.value = 'no';
		}
		filterPanel.style.visibility = 'visible'; 
	}
	else
		applyFilter(e);
}

function cancelFilter(e) {
	var viewId = getTableViewId(e);
	var form = getById(viewId + ':browseTableForm');
	var filterSelections = getById(viewId+':browseTableForm:filterSelections').value;
	for (var i=0, f=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) {
			item.checked = filterSelections.charAt(f++)=='1'
			filterSelection(e, item);

			var idPrefix = item.name.substr(0, item.name.lastIndexOf(':')+1);
			var originalValueItem1 = getById(idPrefix + 'filterOriginalValue1');
			var originalValueItem2 = getById(idPrefix + 'filterOriginalValue2');
			var isRangeFilter = getById(idPrefix +  'filterRangeSelect');
			// if it is a multiple checkbox filter 
			var filterValueItem1 = document.getElementsByName(idPrefix+'filterCheckboxValue');
			if (filterValueItem1.length>0) { //getElementsByName always return a not null value even if there is no element with the specified name
				var originalValues = originalValueItem1.value.split(';');
				for(var k=0; k<filterValueItem1.length; k++) {
					filterValueItem1[k].checked = false;
					for (var j=0; j<originalValues.length; j++)
						if (filterValueItem1[k].value == originalValues[j]) {
							filterValueItem1[k].checked = true;
							break;
						}
				}		
			}
			// if it is a multiple value select filter  
			else if (filterValueItem1 = getById(idPrefix+'filterMultipleValue')) {
				var originalValues = originalValueItem1.value.split(';');
				for(var k=0; k<filterValueItem1.options.length; k++) {
					filterValueItem1.options[k].selected = false;
					for (var j=0; j<originalValues.length; j++)
						if (filterValueItem1.options[k].value == originalValues[j]) {
							filterValueItem1.options[k].selected = true;
							break;
						}
				}		
			}
			// if it is a simple value filter 
			else if (filterValueItem1 = getById(idPrefix+'filterValue1')) {
				filterValueItem1.value = originalValueItem1.value;
				if (isRangeFilter) 
					getById(idPrefix+'filterValue2').value = originalValueItem2.value;
			}
			// if it is a list select filter 
			else if (filterValueItem1 = getById(idPrefix+'filterListValue1')) {
				filterValueItem1.value = originalValueItem1.value;
				if (isRangeFilter) 
					getById(idPrefix+'filterListValue2').value = originalValueItem2.value;
			}
			// if it is a radio button select filter 
			else if ((filterValueItem1=document.getElementsByName(idPrefix+'filterRadioValue')).length>0) {
				for (var k=0; k<filterValueItem1.length; k++)
					filterValueItem1[k].checked = (filterValueItem1[k].value == originalValueItem1.value);
			}
			// Check if it is a calendar select date filter 
			else if (filterValueItem1 = getById(idPrefix+'filterDateValue1')) {
				if (originalValueItem1.value == '')
					filterValueItem1.value = 'DD-MM-YYYY';
				else
					filterValueItem1.value = originalValueItem1.value;
				validateDate(filterValueItem1, true);
				if (isRangeFilter) {
					if (originalValueItem2.value == '')
						getById(idPrefix+'filterDateValue2').value = 'DD-MM-YYYY';
					else
						getById(idPrefix+'filterDateValue2').value = originalValueItem2.value;
					validateDate(getById(idPrefix+'filterDateValue2'), true);
				}
			}
		}
	}
	setMessage(viewId + ':browseTableForm:filterMessage', ' ');
	hideFilterPanel(viewId);
}

function applyFilter(e) {
	var viewId = getTableViewId(e);
	
	// *** apply selections
	var filterSelections = getById(viewId+':browseTableForm:filterSelections');
	selections = getSelections(viewId+':browseTableForm', 'filterSelectCheckbox', 0, true);
	if (filterSelections.value!=selections || (selections.indexOf('1')>=0 && isFilterValueChanged(viewId))) {
		if (!validateFilterValues(viewId+':browseTableForm'))
			return;
		filterSelections.value = selections;
		actionLink = getById(viewId+':browseTableForm:filterActionLink');
		getById(viewId+':browseTableForm:filterDisplayForFirstTime').value = 'yes';
		hideFilterPanel(viewId);
		enableAllFilters(viewId);	//javascript disabled inputs should be enabled before submitting
		actionLink.onclick();
	}
	else
		hideFilterPanel(viewId);
}

function isFilterValueChanged(viewId) {
	var form = getById(viewId + ':browseTableForm');
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) {
			if (!item.checked) 
				continue;
			var idPrefix = item.name.substr(0, item.name.lastIndexOf(':')+1);
			var currentValue1 = '';
			var currentValue2 = '';
			var filterValueItem2 = null;
			var originalValueItem1 = getById(idPrefix + 'filterOriginalValue1');
			var originalValueItem2 = getById(idPrefix + 'filterOriginalValue2');
			var filterRangeSelect = getById(idPrefix +  'filterRangeSelect');
			var isRangeFilter = (filterRangeSelect && filterRangeSelect.checked);
			// if range select box changed
			if (filterRangeSelect) {
				var filterOriginalRangeSelectChecked = getById(idPrefix + 'filterOriginalRangeSelect').value=="true";
				if (filterRangeSelect.checked && !filterOriginalRangeSelectChecked || !filterRangeSelect.checked && filterOriginalRangeSelectChecked)
					return true;
			}
			// if it is a multiple checkbox filter 
			var filterValueItem1 = document.getElementsByName(idPrefix+'filterCheckboxValue');
			if (filterValueItem1.length>0) { //getElementsByName always return a not null value even if there is no element with the specified name
				for(var k=0; k<filterValueItem1.length; k++) 
					if (filterValueItem1[k].checked)
						currentValue1 += ((currentValue1=='')? '' : ';') + filterValueItem1[k].value;
			}
			// if it is a multiple value select filter  
			else if (filterValueItem1 = getById(idPrefix+'filterMultipleValue')) {
				for(var k=0; k<filterValueItem1.options.length; k++) 
					if (filterValueItem1.options[k].selected)
						currentValue1 += ((currentValue1=='')? '' : ';') + filterValueItem1.options[k].value;
			}
			// if it is a simple value filter 
			else if (filterValueItem1 = getById(idPrefix+'filterValue1')) {
				currentValue1 = filterValueItem1.value;
				if (isRangeFilter)
					currentValue2 = getById(idPrefix+'filterValue2').value;
			}
			// if it is a list select filter 
			else if (filterValueItem1 = getById(idPrefix+'filterListValue1')) {
				currentValue1 = filterValueItem1.value;
				if (isRangeFilter)
					currentValue2 = getById(idPrefix+'filterListValue2').value;
			}
			// if it is a radio button select filter 
			else if ((filterValueItem1=document.getElementsByName(idPrefix+'filterRadioValue')).length>0) {
				for (var k=0; k<filterValueItem1.length; k++)
					if (filterValueItem1[k].checked) {
						currentValue1 = filterValueItem1[k].value;
						break;
					}
			}
			// Check if it is a calendar select date filter 
			else if (filterValueItem1 = getById(idPrefix+'filterDateValue1')) {
				if (filterValueItem1.value == 'DD-MM-YYYY') {
					if (originalValueItem1.value!='')
						filterValueItem1.value = '';
					currentValue1 = '';
				}
				else
					currentValue1 = filterValueItem1.value;
				if (isRangeFilter) {
					filterValueItem2 = getById(idPrefix+'filterDateValue2');
					if (filterValueItem2.value == 'DD-MM-YYYY') {
						if (originalValueItem2.value!='')
							filterValueItem2.value = '';
						currentValue2 = '';
					}
					else
						currentValue2 = filterValueItem2.value;
				}
			}
			if (currentValue1!=originalValueItem1.value || (isRangeFilter && currentValue2 != originalValueItem2.value)) 
				return true;
			continue;
		}
	}
	return false;
}

function validateFilterValues(formId) {	// this is currently validate date values only
	var form = getById(formId);
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('filterList')>=0 && item.name.indexOf('filterSelectCheckbox')>=0 ) {
			if (!item.checked) 
				continue;
			var idPrefix = item.name.substr(0, item.name.lastIndexOf(':')+1);
			if (!validateDate(getById(idPrefix+'filterDateValue1'), true))
				return false;
			if (!validateDate(getById(idPrefix+'filterDateValue2'), true))
				return false;
		}
	}		
	return true;			
}

function validateDate(dateItem, panelMessage) {
	if (!dateItem)
		return true;
	var date = trim(dateItem.value); //removes leading and trailing white spaces
	date = trim(date);
	if (date=='' || date=='DD-MM-YYYY' || isDateValid(date)) {
		dateItem.value = date.replace(/\/|\./g, "-");
		dateItem.className = 'plaintext';
		return true;
	}
	dateItem.className = 'errorMessage';
	if (panelMessage) {
		var idPrefix = dateItem.name.substr(0, dateItem.name.lastIndexOf(':filterList:')+1);
		setMessage(idPrefix+'filterMessage', 'An invalid date has been entered, use DD-MM-YYYY and try again.');
	}
	else
	    alert ('Please enter valid date as DD-MM-YYYY');
    return false;
}

function hideFilterPanel(viewId) {
	var tableViewName = getById(viewId+':browseTableForm:tableViewName').value;
	filterPanel = getById(tableViewName+':filterPanel');
	filterPanel.style.visibility = 'hidden'; 
	filterPanel.style.overflow = 'hidden'; 
	var filterImage = getById(viewId+':browseTableForm:modifyFilterImage');
	if (filterImage) {
		filterImage.src = '../images/ModifyFilter_btn.png';	
		filterImage.alt = 'ModifyFilter';
		filterImage.title = 'Click to modify your filter for this result';
	}
}

//***************************************************************************************************************
//* Columns Selection

function columnSelection(e, selectItem) {
	var viewId = getTableViewId(e);
	var minColNum = parseInt(getById(viewId+':browseTableForm:minColNum').value);
	var maxColNum = parseInt(getById(viewId+':browseTableForm:maxColNum').value);
	var visibleColNumItem = getById(viewId+':browseTableForm:visibleColNum');
	var message = ' ';

	if (!selectItem.checked) {  //When this function is called the check box value is changed because it was clicked
		if (visibleColNumItem.value > minColNum) {
			visibleColNumItem.value--;
		} else {
			selectItem.checked = true;
			message = 'no more column can be removed';
		}
	}
	else { 
		if (visibleColNumItem.value < maxColNum) {
			visibleColNumItem.value++;
		}
		/* to allow to see maxum !!!
		else {
			selectItem.checked = false;
			message = 'no more column can be added';
		}
		*/
	}
	
	setMessage(viewId+':browseTableForm:columnMessage', message);
}

function columnSelectionClicked(e) {
	var viewId = getTableViewId(e);
//	var columnSelectionPanel = getById(viewId+':browseTableForm:columnSelectionPanel');
	var tableViewName = getById(viewId+':browseTableForm:tableViewName').value;
	var columnSelectionPanel = getById(tableViewName+':columnSelectionPanel');
	

	if (columnSelectionPanel.style.visibility == 'hidden') {  // ShowColumnSelectionPanel
		columnSelectionPanel.style.overflow = 'auto';
		
		var columnSelections = getById(viewId+':browseTableForm:columnSelections');
		columnSelections.value = getSelections(viewId+':browseTableForm', 'columnSelectionColumnsList');
		
		// This is to position the panel under the ImageSelection button (it is really a fix for IE since it already ok in mozilla)
		var columnSelectionsImage = getById(viewId+':browseTableForm:columnSelectionsImage');
		var pos = findPos(columnSelectionsImage);
		pos = adjustPosRelativeTomainContent(pos);
		columnSelectionPanel.style.left = pos.left + 'px';  //align left
//		columnSelectionPanel.style.left = Math.round(pos.left + (columnSelectionsImage.offsetWidth - columnSelectionPanel.offsetWidth)/2) + 'px'; //align centre
		columnSelectionPanel.style.top = (pos.top + columnSelectionsImage.offsetHeight - 6) + 'px';
		
		setPanelMaxHeight(columnSelectionPanel);
	
		columnSelectionPanel.style.visibility = 'visible'; 
		var columnSelections = getById(viewId+':browseTableForm:columnSelections');
		columnSelections.value = getSelections(viewId+':browseTableForm', 'columnSelectionColumnsList');
	}
	else 
		hideColumnSelectionPanel(e);
}

function columnSelectionPanelMouseOut(e) {
	if (!e) { 
		e = window.event;
	}
	var from = e.target || e.srcElement;		
	var target = e.relatedTarget || e.toElement;		
	
	var node=target;
	while (node != null){
		if (node.tagName == 'DIV' || node.tagName == 'TABLE') {
			if (node.id.indexOf('columnSelection') < 0) {
				break;
			}				
			else
				return;
		}
		if (node.tagName == 'FORM' || node.parentNode == null) 
			break;
			
		node=node.parentNode;
	}
	
	if (!node)  // This is a fix for mozilla only to fix onmouseout problem when using flexible scroll
		return;
		
	hideColumnSelectionPanel(e);
}

var viewIdParam;		// Used to pass parameter to the method called in the setTimeouts in the following function
function hideColumnSelectionPanel(e) {
	var viewId = getTableViewId(e);
//	columnSelectionPanel = getById(viewId+':browseTableForm:columnSelectionPanel');
	var tableViewName = getById(viewId+':browseTableForm:tableViewName').value;
	columnSelectionPanel = getById(tableViewName+':columnSelectionPanel');
	
	// *** apply selections
	var columnSelections = getById(viewId+':browseTableForm:columnSelections');
	selections = getSelections(viewId+':browseTableForm', 'columnSelectionColumnsList');
	viewIdParam = viewId;
	if (columnSelections.value != selections ) {
		columnSelections.value = selections;
		actionLink = getById(viewId+':browseTableForm:columnSelectionActionLink');
		setTimeout("columnSelectionPanel.style.visibility='hidden'; columnSelectionPanel.style.overflow='hidden'; setMessage(viewIdParam+':browseTableForm:columnMessage', ' '); actionLink.onclick();", 100);
	}
	else {			
		setTimeout("columnSelectionPanel.style.visibility='hidden'; columnSelectionPanel.style.overflow='hidden'; setMessage(viewIdParam+':browseTableForm:columnMessage', ' '); ", 100);
	}
}

function selectDefaultColumns(e) {
	var viewId = getTableViewId(e);
	var defaultCols = getById(viewId+':browseTableForm:defaultColumns').value;
	var formId = getTableFormId(e);
	var form = getById(formId);
	var k;
	var item;
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if (item.type=='checkbox' && item.name.indexOf('columnSelectionColumnsList')>-1) {
			var matches = item.name.match(/:columnSelectionColumnsList:(\d+)/);
			if (matches && defaultCols.charAt(matches[1])=='1')
				item.checked = true;
			else
				item.checked = false;
		}
	}
	setMessage(viewId+':browseTableForm:columnMessage', ' ');
}

function selectAllColumns(e) {
	var formId = getTableFormId(e);
	var form = getById(formId);
	var visibleColNumItem = getById(formId+':visibleColNum');
	visibleColNumItem.value = 0;
	for (var i=0; i<form.elements.length; i++) {
		var item = form.elements[i];
		if(item.type=='checkbox' && item.name.indexOf('columnSelectionColumnsList')>-1) {
			item.checked = true;
			visibleColNumItem.value++;
		}
	}
}

//***************************************************************************************************************
//* Expand/shrink table body

var isMouseKeyDown = false;
var changeSize = 40;   // The amount in pixels each click of mouse will shrink/expand Table body panel
var continuousChangeSize = 20; // The steps in pixels when holding mouse key down will shrink/expand Table body panel 
var viewId;
var tableBodyContainer;
var tableBodyPanel;
var table;
var tableBody;

function initHeightVars(e, tableForm) {
	if (!tableForm) {
		viewId = getTableViewId(e);
		tableForm = document.forms[viewId+':browseTableForm']
	}
	tableBodyContainer = getChild(tableForm, 'tableBodyContainer');
	tableBodyPanel = getChild(tableBodyContainer, 'tableBodyPanel');
	table = getChild(tableBodyPanel, ':genericTable', false);
	tableBody = table.tBodies[0];
}	

function shrinkTableBodyPanel(e) {
	initHeightVars(e);
	changeTableBodyPanelHeight(-changeSize);
}
	
function expandTableBodyPanel(e) {
	initHeightVars(e);
	changeTableBodyPanelHeight(changeSize);
}
	
function continuousExpandTableBodyPanel(e) {
	initHeightVars(e);
	isMouseKeyDown = true;
	setTimeout('continuousIncreaseTableBodyPanelHeight()', 300);
}

function continuousShrinkTableBodyPanel(e) {
	initHeightVars(e);
	isMouseKeyDown = true;
	setTimeout('continuousDecreaseTableBodyPanelHeight()', 300);
}

function continuousIncreaseTableBodyPanelHeight() {
	if (isMouseKeyDown == false)
		return;
	if (changeTableBodyPanelHeight(continuousChangeSize))
		setTimeout('continuousIncreaseTableBodyPanelHeight(continuousChangeSize)', 50);
}
		
function continuousDecreaseTableBodyPanelHeight() {
	if (isMouseKeyDown == false)
		return;
	if (changeTableBodyPanelHeight(-continuousChangeSize))
		setTimeout('continuousDecreaseTableBodyPanelHeight()', 50);
}
		
function findMaxHeight() {
	var voffset = 0;
	var maxHeight = 0;
	if (isIE) {
		var offset = findOffsets(tableBodyPanel);
		maxHeight = table.offsetHeight + offset.v + parseInt(tableBodyPanel.currentStyle['paddingBottom']) 
		if (table.offsetHeight==0)		// for tab panels that are not displayed
			return 0;
	}
	else {
		var offset = findOffsets(tableBodyPanel, table);
		maxHeight = tableBody.scrollHeight + table.tHead.offsetHeight + offset.v + 6;	// 6 is added because 5 is added to table height for non IE
		if (tableBody.scrollHeight + table.tHead.offsetHeight == 0)	// for tab panels that are not displayed
			return 0;
	}
	return maxHeight;
}

function changeTableBodyPanelHeight(value, form) {
	var height = parseInt(tableBodyContainer.style.height);
	var maxHeight = findMaxHeight();
	if (maxHeight == 0) // for tab panels that are not displayed
		return false;
	
	var minHeight = 100;
	if (!value || ((height>minHeight || value>0)  && (height<maxHeight || value<0))) {
		if (!value)
			value = 0;
		var newHeight = height + value;
		newHeight = Math.min(newHeight, maxHeight);
		newHeight = Math.max(newHeight, minHeight);
		tableBodyContainer.style.height =  newHeight + 'px';
		if (isIE) 
			table.tHead.childNodes[0].style.top = '0';//to fix a layout problem with table headers in IE
//		tableBodyPanel.style.height =  newHeight + 'px';
		initialiseTableBodyPanelScrolling(tableBodyPanel);
		if (newHeight == maxHeight) {	// check special case when expandinding height cause remooval of vertical scroll bar and consequently possible height reduction
			height = parseInt(tableBodyContainer.style.height);
			var newMaxHeight = findMaxHeight();
			if (newMaxHeight < maxHeight) {
				newHeight = maxHeight = newMaxHeight;
				tableBodyContainer.style.height =  newHeight + 'px';
				initialiseTableBodyPanelScrolling(tableBodyPanel);
			}
		}
		if (form)
			getById(form.id+':tableBodyHeight').value = newHeight;
		else
			getById(viewId+':browseTableForm:tableBodyHeight').value = newHeight;
			
		return true;
	}
	return false;
}

//************************************************************************
function reload(e) {
	var viewId = getTableViewId(e);
	getById(viewId+':browseTableForm:reloadActionLink').onclick();
	return false;
}

function getWindowDimensioins () {
	var frameWidth = 0;
	var frameHeight = 0;
	if (self.innerWidth) {
		frameWidth = self.innerWidth;
		frameHeight = self.innerHeight;
	}
	else if (document.documentElement && document.documentElement.clientWidth) {
		frameWidth = document.documentElement.clientWidth;
		frameHeight = document.documentElement.clientHeight;
	} 
	else if (document.body) {
		frameWidth = document.body.clientWidth;
		frameHeight = document.body.clientHeight;
	}

	return {width:frameWidth, height:frameHeight};
}

//************************************************************************
function clickExternalCommandLink(formId, linkId, params) {
	if(params!=null && params!="") {
		appendHiddenInput(document.forms[formId], 'clickedLinkParams', params);
	}
	getById(formId + ':' + linkId).onclick();
}

//************************************************************************
function getTableBodyPanel(form) {
	var tableBodyContainer = getChild(form, 'tableBodyContainer');
	return getChild(tableBodyContainer, 'tableBodyPanel');
}

//************************************************************************
function setPanelMaxHeight(panel) {	// is used to adjust the height of filter and column selection panels so that not to go over the page footer 
		var maincontent = getById("mainContent");
		var footer = getById("footernew");
		var maxHeight = footer.offsetTop - panel.offsetTop - 4;	// -4 is to separate it from the footer
		if (maincontent)
			maxHeight -= maincontent.offsetTop;
		panel.style.height = 'auto';
		height = Math.min(panel.offsetHeight, maxHeight),
		panel.style.height = height + 'px'; 
}

//************************************************************************
document.onmouseup = function() { isMouseKeyDown = false; };

