<!doctype html public "-//w3c//dtd html 4.01 transitional//en">
<!-- Generic Table JSP page				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- xingjun - 30/07/2010 - not need anymore
<head>
    <link href="/gudmap/css/gudmap_css.css" type="text/css" rel="stylesheet">
<!--     <link href="/gudmap/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
 -->
</head>
--%>

<fmt:bundle basename="configuration">
  <fmt:message key="project" var="proj" />
</fmt:bundle>


<HTML>

	<c:set var="unique" value="<%=System.currentTimeMillis()%>" />

	<c:choose>
    <c:when test="${param.tableSelectable || param.tableNumPages>1}">
      <c:choose>
        <c:when test="${param.tableSelectable && param.numPages>1}">
          <frameset rows="64, *">
        </c:when>  
        <c:otherwise>
          <frameset rows="38, *">
        </c:otherwise>
      </c:choose>
        <frame name="tableNavigation" src="wide_table_navigation.jsf?tableViewName=${param.tableViewName}&unique=${unique}" 
        			 scrolling="no" frameborder="no" marginwidth="1" marginheight="1" noresize >
        <frame name="tableBody" src="wide_table_body.jsf?tableViewName=${param.tableViewName}&unique=${unique}" 
        			 frameborder="no" marginwidth="1" marginheight="1" noresize>
      </frameset>
    </c:when>  

    <c:otherwise>
      <frameset rows="100%">
        <frame name="tableBody" src="wide_table_body.jsf?tableViewName=${param.tableViewName}&unique=${unique}" 
        			 frameborder="no" marginwidth="1" marginheight="1" noresize>
      </frameset>
    </c:otherwise>
  </c:choose>
  
</HTML>









