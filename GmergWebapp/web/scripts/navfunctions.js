var TO, menuVal, targId;

function movrevent(id) {
  if(TO){
    clearTimeout(TO);
  }
  if(targId){
    deSelElement();
  }
  currElem = document.getElementById(id);
  parentEl = currElem.parentNode.parentNode;
  parId = parentEl.id;
  if(parId.indexOf("subnav")>=0){
    menuIndex = parId.substring(6,parId.length);
    highlightSubEl(menuIndex,id);
  }
  else{
    highlightEl(id);
  }
}

function moutevent(id) {
  targId = id;
  TO = setTimeout('deSelElement();',500);

}

function mclkevent(id) {
  if(htmlLinks[menuVal] != ''){
    location.href = htmlLinks[menuVal];
  }
}

function highlightSubEl(menuIndex,id) {
  parentItem = document.getElementById("navItem"+menuIndex);
  currItem = document.getElementById(id);
  highlightEl(parentItem.id);
  highlightEl(currItem.id);
}

function highlightEl(id) {
  currElem = document.getElementById(id);
  idNo = id.substring(7,id.length);
  textElem = document.getElementById("textItem"+idNo)
  textElem.style.color = hexcolours[0];
  currElem.style.backgroundColor = hexcolours[3];
  menuVal = id.substring(7,id.length);
  if(subMenuItems[menuVal][0] == 1) {
    showSubItems();
  }
}

function deSelElement() {
  currElem = document.getElementById(targId);
  parentEl = currElem.parentNode.parentNode;
  parId = parentEl.id;
  curIdNo = targId.substring(7,targId.length);
  curTxtElem = document.getElementById("textItem"+curIdNo)
  parIdNo = parId.substring(7,parId.length);
  if(parId.indexOf("subnav")>=0){
    parentEl.style.visibility = 'hidden';
    parentItem = document.getElementById("navItem"+parId.substring(6,parId.length));
    parTxtItem = document.getElementById("textItem"+parId.substring(6,parId.length));
    parTxtItem.style.color = hexcolours[1];
    parentItem.style.backgroundColor = hexcolours[2];
  }
  curTxtElem.style.color = hexcolours[1];
  currElem.style.backgroundColor = hexcolours[2];
  if(subMenuItems[menuVal][0] == 1) {
    hideSubItems()
  }

}

function showSubItems() {
  childElem = document.getElementById('subnav'+menuVal);
  childElem.style.visibility = 'visible';
}

function hideSubItems() {
  childElem = document.getElementById('subnav'+menuVal);
  childElem.style.visibility = 'hidden';
}

///////////////



//GLOBAL VARIABLES
/*varColors=["#CC3333","#329262","#A87070","#DD4477","#DD5511","#B08B59","#22AA99","#6633CC","#AAAA11","#7783A8","#66AA00","#994499","#109618","#3366CC","#EE8800","#D6AE00","#898951","#336699"];
varTitles=["Limb","Alimentary&nbsp;System","Liver&nbsp;and&nbsp;Biliary&nbsp;System","Renal/Urinary&nbsp;System","Reproductive&nbsp;System","Respiratory&nbsp;System","Ear","Eye","Nose","Cardiovascular&nbsp;System","Spleen","Thymus","Brain","Spinal&nbsp;Cord","Peripheral&nbsp;Nervous&nbsp;System","Cranial&nbsp;Nerves&nbsp;and&nbsp;Ganglia","Skeletal&nbsp;Muscles","Skeleton"];
varShortTitles=["LMB","ALS","LBS","RUS","RPS","RSS","EAR","EYE","NOS","CVS","SPL","THY","BRN","SPI","PNS","CNG","SKM","SKL"];
varEmptyValues="0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0"; //empty values*/

//***** with 'others' category *****/
// xingjun - 22/09/2010 - change 'Lower&nbsp;urinary&nbsp;system' to 'Lower&nbsp;urinary&nbsp;tract'
//varColors=["#CC3333","#329262","#A87070","#DD4477","#DD5511","#B08B59","#22AA99"];
//varTitles=["Mesonephros","Metanephros","Lower&nbsp;urinary&nbsp;tract","Early&nbsp;reproductive&nbsp;system","Male&nbsp;reproductive&nbsp;system","Female&nbsp;reproductive&nbsp;system","Others"];
//varShortTitles=["MES","MET","LUT","EARREP","MAL","FEM","OTH"];
//varEmptyValues="0.0,0.0,0.0,0.0,0.0,0.0,0.0";

//***** without 'others' category
// xingjun - 22/09/2010 - change 'Lower&nbsp;urinary&nbsp;system' to 'Lower&nbsp;urinary&nbsp;tract'
varColors=["#CC3333","#329262","#A87070","#DD4477","#DD5511","#B08B59"];
varTitles=["Mesonephros","Metanephros","Lower&nbsp;urinary&nbsp;tract","Early&nbsp;reproductive&nbsp;system","Male&nbsp;reproductive&nbsp;system","Female&nbsp;reproductive&nbsp;system"];
varShortTitles=["MES","MET","LUT","EARREP","MAL","FEM"];
varEmptyValues="0.0,0.0,0.0,0.0,0.0,0.0";

// PREPARE THE BAR FOR DISPLAY
// modified by xingjun - 16/01/2009 - pass in new params (focus groups)
// modified by xingjun - 03/04/2009 
// - replaced passed in constant param assayID with variable param symbol to make every div have one unique id

function prepareGraph(geneSymbol,values,browseLink,focusGroups) {
	var exprValues=varEmptyValues;
	if(values!="")
		exprValues=values;
	
	a=exprValues.split(",");
	len=a.length;
	varDiv="exprLevelsGraph_"+geneSymbol;

	var f = focusGroups;
	fgs = f.split(",");
	
	// url
	browseLnk = browseLink;
	//alert(browseLnk);
	
	graphDiv=document.getElementById(varDiv);
	
	var varHTML="<div class='exprGraph'><table class='exprGraphTable' border='0'>";
	
	var topRowHTML = "";
	var bottomRowHTML = "";
	var maxValue = 50;
	
	for(i=0; i<len; i++) {
		brsLnk = "self.location.href=\"" + browseLnk + fgs[i] + "\"";
		topRowHTML += "<td class='exprGraphCell' align='center' onClick='" + brsLnk + "'" ;
		bottomRowHTML += "<td class='exprGraphCell' align='center' onClick='" + brsLnk + "'";
		var title = varTitles[i] + "&nbsp;[Expression&nbsp;";
		var value = Math.round(Math.abs(a[i])*100.0/maxValue);
		if(a[i]==0) {
			title += "NotExamined]";
			topRowHTML += " title='" + title + "' >";
			topRowHTML += getSpacerDiv(varShortTitles[i], 100);
			topRowHTML += getBarDiv(varShortTitles[i], 0, title, varColors[i]);
			bottomRowHTML += " title='" + title + "' >"
			bottomRowHTML += getSpacerDiv(varShortTitles[i], 100);
		} 
		else if(a[i]>0) { 
			title += "Present]";
			topRowHTML += ">";
			topRowHTML += getSpacerDiv(varShortTitles[i], 100-value);
			topRowHTML += getBarDiv(varShortTitles[i], value, title, varColors[i]);
			bottomRowHTML += " title='" + title + "' >"
			bottomRowHTML += getSpacerDiv(varShortTitles[i], 100);
		}
		else {
			title += "Not&nbsp;Detected]";
			topRowHTML += " title='" + title + "' >"
			topRowHTML += getSpacerDiv(varShortTitles[i], 100);
			bottomRowHTML += ">";
			bottomRowHTML += getBarDiv(varShortTitles[i], value, title, varColors[i]);
			bottomRowHTML += getSpacerDiv(varShortTitles[i], 100-value);
		} 
		topRowHTML += "</td>";
		bottomRowHTML += "</td>";
	}
	varHTML += "<tr style='border-bottom:1px dotted gray'>" + topRowHTML + "</tr>";
	varHTML += "<tr>" + bottomRowHTML + "</tr>";
		
	varHTML += "</table></div>";
	graphDiv.innerHTML = varHTML;
}

function getSpacerDiv(id, height) {
	if (height<=0)
		return "";
	return "<div id='" + id + "_transparentDivID' style='display: block; height:" + height + "%; background-color: transparent;' class='exprGraphBarSpacer'" + "></div>";
}

function getBarDiv(id, height, title, color) {
	var titleStr = ((title!=null)?" title='"+title+"' ":"" );
	if (height>0)
		return "<div id='" + id + "2' style='height:" + height + "%; background-color: " + color + ";'" + titleStr + "class='exprGraphBar'></div>";
	else
		return "<div id='" + id + "2'" + titleStr + "class='zeroExprGraphBar'></div>";
}

//PREPARE THE HEADER ICONS FOR SORT PURPOSE
function prepareGraphTableHeaders(sortLink, sortBy, sortDir) {
	var chartTableHeaders=document.getElementById("chartTableHeaders");
	var varHTML="<div class='exprGraph'><table class='exprGraphTable' border=0 align=center style='height:15px; padding:0px; border-collapse:collapse;'><tr>"
	varShowSort=false;
	//varPrefix="kst_exr_value_";
	
	if(sortDir=="ASC")
	varDir="&darr;";
	else if(sortDir=="DESC")
	varDir="&uarr;";
	
	//alert("sortDir= " + sortDir + "    varDir= " + varDir);
	
	//if(sortBy.substring(0,14)==varPrefix)
	//varShowSort=true;
	
	//***** with 'others' category *****/
	//for(i=0;i<=6;i++)
	//***** without 'others' category *****/
	for(i=0;i<=5;i++) {
	//srtLnk="\"self.location.href=\'" + sortLink + varPrefix + (i+1) + "\'\"";
	//alert(srtLnk);
	//varHTML=varHTML+"<td align='center' onClick=" + srtLnk + ">";
		varHTML=varHTML+"<td align='center'>";
	
		/*if(varShowSort==true && (varPrefix + (i+1))==sortBy){
			varHTML=varHTML+"<div id='" + varShortTitles[i] + "' class='exprGraphBar' style='height:15px; border-color:#ffffff; background-color: " + varColors[i] + ";text-align:center;vertical-align:middle;' title=Sort&nbsp;by&nbsp;\"" + varTitles[i] + "\" onMouseOver=selectSimilarBars('" + varShortTitles[i] + "') onMouseOut=unselectSimilarBars('" + varShortTitles[i] + "')>" +
			"<span style='color:#ffffff;font-size:11px;font-weight:bold'>" + varDir + "</span>";
		}
		else{*/
			varHTML=varHTML+"<div id='" + varShortTitles[i] + "' class='exprGraphBar' style='height:15px; background-color: " + varColors[i] + ";text-align:center;vertical-align:middle;' title=\"" + varTitles[i] + "\" onMouseOver=selectSimilarBars('" + varShortTitles[i] + "') onMouseOut=unselectSimilarBars('" + varShortTitles[i] + "')>";
		//}
				
		varHTML=varHTML+"</div></td>";
	}
	varHTML=varHTML+"</td></tr></table></div>";
	chartTableHeaders.innerHTML=varHTML;
}

//HIGHLIGHT THE EXPRESSION GRAPH BAR ON MOUSE OVER
function selectSimilarBars(barID) {
	divElements=document.getElementsByTagName("DIV")

	for(i=0;i<=divElements.length;i++)
	{
		if(divElements[i]==null || divElements[i].id==null)
			continue;
		if(divElements[i].id == barID)
			divElements[i].style.border="1px solid #000000"
		if(divElements[i].id == barID+"_transparentDivID")
			divElements[i].style.visibility="visible";
	}
}

//DIM THE EXPRESSION GRAPH BAR ON MOUSE OUT
function unselectSimilarBars(barID)
{
	divElements=document.getElementsByTagName("DIV")

	for(i=0;i<=divElements.length;i++)
	{
		if(divElements[i]==null || divElements[i].id==null)
			continue;
		if(divElements[i].id == barID)
			divElements[i].style.border="1px solid #969696"
		if(divElements[i].id == barID+"_transparentDivID")
			divElements[i].style.visibility="hidden"
	}
}




