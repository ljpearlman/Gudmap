var topMenuItems = new Array('Browse All', 'Focus', 'Query', 'Source Summary');
var indexItems = topMenuItems.length-1;
var subMenuItems = new Array();
subMenuItems[0] = new Array('1','0px','Browse ISH Data','Browse Microarray Data');
subMenuItems[1] = new Array('1','187px','ISH Focus','Microarray Focus');
subMenuItems[2] = new Array('0');
subMenuItems[3] = new Array('0');
subMenuItems[4] = new Array('0');
subMenuItems[5] = new Array('0');
subMenuItems[6] = new Array('0');
subMenuItems[7] = new Array('0');

var subMenItemVals = new Array();
subMenItemVals[0] = new Array('0','4','5');
subMenItemVals[1] = new Array('-1');
subMenItemVals[2] = new Array('-1');
subMenItemVals[3] = new Array('-1');
subMenItemVals[4] = new Array('-1');
subMenItemVals[5] = new Array('-1');

var htmlLinks = new Array('0','0','../query_db.html','../lab_summaries.html','../ish_browse_all.html','../mic_browse_all.html', '../focus.html','../mic_focus.html');
var hexcolours = new Array('#ffffff','#5C5C5C','#E6E8FA','#2A4870')
mevents = 'onmouseover="movrevent(this.id)" onmouseout="moutevent(this.id)"';

function writeMenu() {
  //create the top level of the navigation menu bar
  menuBar = '<div id="navbar" style="position:relative;background-color:'+hexcolours[2]+'"><table width="100%" cellpadding="2" cellspacing="0" id="navtbl"><tr id="navRow">'
  textInCell = "";
  for(i=0;i<topMenuItems.length;i++) {
    menuBar+='<td align="center" width="25%" id="navItem'+i+'" '+mevents+'>';
    if(htmlLinks[i] !="0"){
      menuBar +='<a class="navbartext" id="textItem'+i+'" href="'+htmlLinks[i]+'">'
    }
    else {
      menuBar +='<span class="navbartext" id="textItem'+i+'">'
    }
    menuBar+='<img border="0" src="images/spacet.gif" alt="" width="25px" height="0px" />'+topMenuItems[i]+'<img border="0" src="images/spacet.gif" alt="" width="25px" height="0px" />';
    if(htmlLinks[i] !="0"){
      menuBar+='</a>';
    }
    else {
      menuBar+='</span>';
    }
    menuBar+='</td>';
  }
  menuBar+='</tr></table></div>';
  for(i=1;i<subMenuItems.length;i++) {
    menuBar+='<div id="subnav'+(i-1)+'" style="position:absolute;width:160px;visibility:hidden;z-index:1000"><div id="subnavchild'+(i-1)+'" style="position:relative;left:'+subMenuItems[i-1][1]+';background-color:'+hexcolours[2]+';visibility:inherit">';
    for(j=2;j<subMenuItems[i-1].length;j++) {
      indexItems++;
      menuBar+='<div id="navItem'+indexItems+'" style="visibility:inherit;position:relative" '+mevents+'><a class="navbartext" id="textItem'+indexItems+'" href="'+htmlLinks[indexItems]+'"><img border="0" src="../images/spacet.gif" alt="" width="5px" height="0px" />'+subMenuItems[i-1][j]+'<img border="0" src="../images/spacet.gif" alt="" height="0px" width="5px" /></a></div>';
    }
    menuBar+='</div></div>';
  }
  document.write(menuBar);
}
