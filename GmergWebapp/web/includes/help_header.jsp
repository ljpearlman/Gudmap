<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageName" value="${pageContext.request.servletPath}" />
<c:set var="proj" value="${initParam.project}" scope="application"/>
<c:set var="perspective" value="${initParam.perspective}" scope="application"/>
<c:set var="stageSeriesShort" value="${initParam.stage_series_short}" scope="application" />
<c:set var="stageSeriesMedium" value="${initParam.stage_series_medium}" scope="application" />
<c:set var="stageSeriesLong" value="${initParam.stage_series_long}" scope="application" />

<html>
  <head>
    <title>Gudmap Gene Expression Database</title>
    <link href="../../css/gudmap_css.css" type="text/css" rel="stylesheet">
    <link href="../../css/gudmapmain_css.css" type="text/css" rel="stylesheet">
	<link href="../../css/help.css" type="text/css" rel="stylesheet">
    <style type="text/css">
    @import("../../css/ie51.css");
    </style>
    <script src="../../scripts/formfunctions.js" type="text/javascript"></script>
    <script src="../../scripts/navbar_help.js" type="text/javascript"></script>
    <script src="../../scripts/navfunctions.js" type="text/javascript"></script>
    <script type="text/javascript" src="http://www.gudmap.org/javascripts/main.js"></script>
    <script src="../../scripts/ua.js" type="text/javascript"></script>
    <script src="../../scripts/ftiens4.js" type="text/javascript"></script>
    <script type="text/javascript">
      function doPopup(source) {
        linkId = source.id;
        linkLength = linkId.length;
        linkLength = linkLength - 9;
        newId = linkId.substring(0, linkLength);
        //formId = source.parentNode.id;
        id = newId+"id";
        idVal = document.getElementById(id).value;
        serialNo = newId+"serialNo";
        serialVal = document.getElementById(serialNo).value;
        winNme = newId+"windowNme";
        winNmeVal = document.getElementById(winNme).value;
        popup = window.open("image_detail.html?id="+idVal+"&serialNo="+serialVal, winNmeVal, "resizable=1,toolbar=0,scrollbars=1,width=600,height=600");               
        popup.focus();
      }
      function selAll() {
        if (document.getElementById)
	{
	  x = document.getElementById('browseForm');	
	}
	else if (document.all)
	{
	  x = document.all['browseForm'];
	}
	else if (document.layers)
	{
	  x = document.layers['browseForm'];
	}
	for (var i = 0; i < x.elements.length; i++) {
	  if(x.elements[i].type == 'checkbox'){
	    x.elements[i].checked =true;
	  }
	}
      }
		
      function deSelAll() {
	if (document.getElementById)
	{
	  x = document.getElementById('browseForm');	
	}
	else if (document.all)
	{
	  x = document.all['browseForm'];
	}
	else if (document.layers)
	{
	  x = document.layers['browseForm'];
	}
	for (var i = 0; i < x.elements.length; i++) {
	  if(x.elements[i].type == 'checkbox'){
	    x.elements[i].checked =false;
	  }
	}
      }      
    </script>
    <c:if test="${pageName == '/pages/anatomy_tree.jsp' || pageName == '/pages/ish_submission.jsp'}">
    
    <!-- SECTION 1 -->
    <!-- SECTION 2: Replace everything (HTML, JavaScript, etc.) from here until the beginning
                  of SECTION 3 with the pieces of the head section that are needed for your site  -->

    <script type="text/javascript">
    //This script is not related with the tree itself, just used for my example
    function getQueryString(index)
    {
      var paramExpressions;
      var param
      var val
      paramExpressions = window.location.search.substr(1).split("&");
      if (index < paramExpressions.length)
      {
        param = paramExpressions[index];
        if (param.length > 0) {
          return eval(unescape(param));
        }
      }
      return ""
    }
    </script>

    <!-- SECTION 3: These four scripts define the tree, do not remove-->

    <script type="text/javascript">
    
    
    <c:if test="${pageName == '/pages/anatomy_tree.jsp'}">
    <c:forEach items="${AnatomyBean.treeContent}" var="row">
      <c:out value="${row}" escapeXml="false"/>
    </c:forEach>
    </c:if>
    </script>
    </c:if>
    
  </head>
  <body class="database">
    <div id="wrapper">
      <div id="header">
        <h1><a href="http://www.gudmap.org/index.html">GUDMAP</a></h1>
        <div id="menu">
          <ul id="nav">
            <li id="L_About"><a href="http://www.gudmap.org/About/">About&nbsp;GUDMAP</a>
              <ul>
                <li><a href="http://www.gudmap.org/About/Goal.html">Goal</a></li>
                <li><a href="http://www.gudmap.org/About/Projects/">Projects</a></li>
                <li><a href="http://www.gudmap.org/About/Consortium.html">Consortium</a></li>
                <li><a href="http://www.gudmap.org/About/Tutorial/">Tutorial</a></li>
                <li><a href="http://www.gudmap.org/About/News/">News</a></li>
                <li><a href="http://www.gudmap.org/About/Positions.html">Positions</a></li>
                <li><a href="http://www.gudmap.org/About/Usage.html">Website Content Usage</a></li>
              </ul>
            </li>
            <li id="L_Research"><a href="http://www.gudmap.org/Research/">Research</a>
              <ul>
                <li><a href="http://www.gudmap.org/Research/MAGD.html">Molecular Anatomy of Genitourinary Development</a></li>
                <li><a href="http://www.gudmap.org/Research/GGEM.html">Generation of Genetically Engineered Mice</a></li>
                <li><a href="http://www.gudmap.org/Research/Protocols/">Project Protocols</a></li>
                <li><a href="http://www.gudmap.org/Research/Pubs/">Publications</a></li>
              </ul>
            </li>
            <li id="L_Database"><a href="http://www.gudmap.org/gudmap/">Database</a>
              <ul>
                <li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/ish_browse_all.html">Browse All</a></li>
                <li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/focus.html">Focus</a></li>
                <li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/query_db.html">Query</a></li>
                <li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/lab_summaries.html">Lab Summary</a></li>
                <li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/help/index.htm">Help</a></li>
              </ul>
            </li>
            <li id="L_Resources"><a href="http://www.gudmap.org/Resources/">Resources</a>
              <ul>
                <li><a href="http://www.gudmap.org/Resources/Ontologies.html">Ontologies</a></li>
                <li><a href="http://www.gudmap.org/Resources/MouseStrains/">Mouse Strains</a></li>
              </ul>
            </li>
            <li id="L_Links"><a href="http://www.gudmap.org/Links/">Links</a>
              <ul>
                <li><a href="http://www.gudmap.org/Links/Gen_Disease.html">Genitourinary Disease</a></li>
                <li><a href="http://www.gudmap.org/Links/Gen_Development.html">Genitourinary Development</a></li>
                <li><a href="http://www.gudmap.org/Links/Gene_Expression.html">Gene Expression</a></li>
                <li><a href="http://www.gudmap.org/Links/Funding.html">Funding</a></li>
		<li><a href="http://www.gudmap.org/Links/Message_Board.html">Message Board</a></li>
		<li><a href="http://www.gudmap.org/Links/qc.html">ISH Quality Control</a></li>

                <li><a href="http://www.gudmap.org/Links/Events.html">Events</a></li>
              </ul>
            </li>
            <li id="L_Internal"><a href="http://www.gudmap.org/Internal/">Internal</a>
              <ul>
                <li><a href="http://www.gudmap.org/Internal/Calendars/">Calendars</a></li>
                <li><a href="http://www.gudmap.org/Internal/Agendas/">Agendas</a></li>

                <li><a href="http://www.gudmap.org/Internal/Directory.html">Directory</a></li>
              </ul>
            </li>
            <li id="L_Contact"><a href="http://www.gudmap.org/Contact/">Contact</a>
              <ul>
                <li><a href="http://www.gudmap.org/Contact/ISS.html">In Situ Screening</a></li>
                <li><a href="http://www.gudmap.org/Contact/MP.html">MicroArray Profiling</a></li>

                <li><a href="http://www.gudmap.org/Contact/Mice.html">Genetically Modified Mice</a></li>
                <li><a href="http://www.gudmap.org/Contact/DB.html">Database</a></li>
	        <li><a href="http://www.gudmap.org/Contact/NIH.html">NIH</a></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
      <div id="content">
        <h2 class="titletext">GUDMAP Gene Expression Database</h2>
        <table border="0" class="defaultWidth" cellpadding="0" cellspacing="0">
          <tr>
            <td>
          <c:choose>
          <c:when test="${editorLoggedIn != null}">
              <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <tr align="center" class="stripey">
                  <td width="20%"><a class="plaintextbold" href="../ish_browse_all.html">Browse All</a></td>
                  <td width="20%"><a class="plaintextbold" href="../focus.html">Focus</a></td>
                  <td width="20%"><a class="plaintextbold" href="../query_db.html">Query</a></td>
                  <td width="20%"><a class="plaintextbold" href="../lab_summaries.html">Lab Summary</a></td>
                  <td width="20%"><a class="plaintextbold" href="pre-edit.html">Edit</a></td>
                </tr>
              </table>
          </c:when>
          <c:otherwise>
              <script type="text/javascript">writeMenu()</script>
              <noscript>
                <table width="100%" border="0" cellpadding="2" cellspacing="0">
                  <tr align="center" class="stripey">
                    <td width="25%" class="plaintextbold">
                      Browse All (<a href="../ish_browse_all.html" class="plaintextbold">ISH</a> /
                      <a href="../mic_browse_all.html" class="plaintextbold">Microarray)</a>
                    </td>
                    <td width="25%"><a class="plaintextbold" href="../focus.html">Focus</a></td>
                    <td width="25%"><a class="plaintextbold" href="../query_db.html">Query</a></td>
                    <td width="25%"><a class="plaintextbold" href="../lab_summaries.html">Lab Summary</a></td>
                  </tr>
                </table>
              </noscript>
          </c:otherwise>
          </c:choose>
              </td>
            </tr>
          </table>
          <br/>
          <div id="mainclass" class="mainclass">
            <table border="0" class="defaultWidth" cellpadding="0" cellspacing="0">
              <tr>
                <td>
