

function toggleAll() {
  for (var i = 0; i < document.browseForm.elements.length; i++) {
    if(document.browseForm.elements[i].type == 'checkbox'){
      document.browseForm.elements[i].checked = !(document.browseForm.elements[i].checked);
    }
  }
}

function changeRPP() {
  document.rppForm.submit();
}

function openNewWindow() {
  var numBoxes = browseForm.allEntriesBox.length
  var textlnk = "gudmapdb?query=showSel&name=0&accID="
  for  (i=0; i < browseForm.allEntriesBox.length; i++)
  {
    if (browseForm.allEntriesBox[i].checked)
    {
      textlnk += browseForm.allEntriesBox[i].value + "+"
    }
  }
  var numchars = textlnk.length
  textlnk = textlnk.substr(0,numchars-1)
  windowOpener(textlnk,'0','toolbar=yes,menubar=yes,directories=yes,resizable=yes,scrollbars=yes,width=900,height=600')
  return false
}

function openNewWindow(windowname) {
  var textlnk = "gudmapdb?query=showSel&name=" + windowname + "&accID="
  //alert(windowname)
  for (var i = 0; i < document.browseForm.elements.length; i++) {
    if(document.browseForm.elements[i].type == 'checkbox'){
    if (document.browseForm.elements[i].checked)
    {
      textlnk += document.browseForm.elements[i].value + "+"
    }
    }
  }
  var numchars = textlnk.length
  textlnk = textlnk.substr(0,numchars-1)

  windowOpener(textlnk,windowname,'toolbar=yes,menubar=yes,directories=yes,resizable=yes,scrollbars=yes,width=900,height=600')
  return false
}

function windowOpener(url, name, args) {
  if (typeof(popupWin) != "object"){
    popupWin = window.open(url,name,args);
  }
  else {
    if (!popupWin.closed){
      popupWin.location.href = url;
    }
    else {
      popupWin = window.open(url, name,args);
    }
  }
  popupWin.focus();
}

function clickButton(e, buttonid){
  var bt = document.getElementById(buttonid);
  if (typeof bt == 'object'){
    if(navigator.appName.indexOf("Netscape")>(-1)){
      if (e.keyCode == 13){
	bt.click();
	return false;
      }
    }
    if (navigator.appName.indexOf("Microsoft Internet Explorer")>(-1)){
      if (event.keyCode == 13){
	bt.click();
	return false;
      }
    }
  }
}

function pageScroll() {
  if (navigator.appName.indexOf("Netscape")>(-1)) {
    winW = window.innerWidth;
  }
  if (navigator.appName.indexOf("Microsoft")!=-1) {
    winW = document.body.offsetWidth;
  }
  window.scroll(160-winW, 110);
//    	scrolldelay = setTimeout('pageScroll()',100);
}

function setFocus(){
	document.browseForm.searchValue.focus();
}

function display(){
	pageScroll();
	setFocus();
}

// geneSymbol ----- arg1
// TheilerStage ----- arg2
// Stage ----- arg3
// AssayType ----- arg4
// specimenType ----- arg5
// ImageName ----- arg6
function openImage(url, name, arg1, arg2, arg3, arg4, arg5, arg6){
  imgWin = window.open(url,'imagePopup', 'toolbar=no,resizable=yes,scrollbars=yes')
  with (imgWin.document){
  writeln('<html><head><title>Image '+arg6+' From '+name+'</title>    <style type="text/css">');
  writeln('body { font-family: verdana, arial, helvetica, sans-serif;');
  writeln('font-size: smaller; }');
  writeln('h1 { font-size: large; }');
  writeln('tr { font-size: smaller; }');
  writeln('th, td { border-left: 1px solid #000000;');
  writeln('border-bottom: 1px solid #000000; padding: 2px; }');
  writeln('th { color: #000000; background: #e9e9f2; }');
  writeln('table { padding: 0px;  margin: 0px;');
  writeln('border-top: 1px solid #000000;');
  writeln('border-right: 1px solid #000000; }');
  writeln('table.info { color: #000000; background: #e9e9f2; }');
  writeln('table.data { color: #000000; background: #ffffff; }');
  writeln('td.label { text-align: right; }');
  writeln('td.value { font-weight: bold; }');
  writeln('tr.even { color: #000000; background: #ffffff; }');
  writeln('tr.odd { color: #000000; background: #eeeeee; }');
  writeln('</style></head><body><table class="data" cellspacing="0"><tr class="odd"><td>ID:</td><td>'+arg1+'</td></tr>'+
	  '<tr class="even"><td>Gene Symbol:</td><td>'+arg2+'</td></tr>'+'<tr class="odd"><td>Theiler Stage:</td><td>TS'+arg3+'</td></tr>'+'<tr class="even"><td>Stage Given:</td><td>'+arg4+'</td></tr><tr class="odd"><td>Assay Type:</td><td>'+arg5+
		'</td></tr><tr class="even"><td>Specimen Type:</td><td>'+arg6+'</td></tr></table><a><img border="0" id="detail" src="' + url + '"/></a></body></html>');
		close();
  }
	//if(imgWin.document.getElementById('detail').width<500 || imgWin.document.getElementById('detail').height<500) {
		//alert(imgWin.document.getElementById('detail').width+","+imgWin.document.getElementById('detail').height)
  imgWin.resizeTo(800,600)
	//} else {
		//imgWin.resizeTo(imgWin.document.getElementById('detail').width, imgWin.document.getElementById('detail').height)
	//}
  return false
}


function selAllForOperationTwo() {
  for (var i = 0; i < document.browseFormTwo.elements.length; i++) {
    if(document.browseFormTwo.elements[i].type == 'checkbox'){
      document.browseFormTwo.elements[i].checked =true;
    }
  }
}

function deSelAllForOperationTwo() {
  for (var i = 0; i < document.browseFormTwo.elements.length; i++) {
    if(document.browseFormTwo.elements[i].type == 'checkbox'){
      document.browseFormTwo.elements[i].checked =false;
    }
  }
}

//functions 'ShowMenu' and 'hideMenu' used to hide and display additional menu items.
var Menu,TO;
var browseMenuItems = "<a class='plaintextbold' href='/gudmap/gudmapdb?query=browse'>Browse ISH Data</a><br /><a class='plaintextbold' href='/gudmap/gudmapdbarray?query=browse'>Browse Microarray Data</a>";
function ShowMenu(id, content){
Menu=document.getElementById(id);
Menu.style.visibility = 'visible';
//Menu.innerHTML = content;
clearTimeout(TO);

}

function hideMenu() {
//TO=setTimeout('Menu.innerHTML="";',500);
TO=setTimeout('Menu.style.visibility=\'hidden\';',500);
}


function writit(text,id)
{	
	if (document.getElementById)
	{
		x = document.getElementById(id);
		if(navigator.userAgent.indexOf("Firefox") > -1)
		{
			x.textContent = 'Selected substructure: ' + text;
		} else {
			x.innerHTML = '';
			x.innerHTML = 'Selected substructure: ' + text;
		}	
		eval(x);	
	}
	else if (document.all)
	{
		x = document.all[id];
		if(navigator.userAgent.indexOf("Firefox") > -1)
		{
			x.textContent = 'Selected substructure: ' + text;
		} else {
			x.innerHTML = 'Selected substructure: ' + text;
		}
		eval(x);
	}
	else if (document.layers)
	{
		x = document.layers[id];
		text2 = '<P>' + 'Selected substructure: ' + text + '</P>';
		x.document.open();
		x.document.write(text2);
		x.document.close();
	}
}
