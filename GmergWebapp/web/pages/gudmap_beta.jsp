<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>GUDMAP BETA</title>
    <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
    
    <style type="text/css">
    @import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");
    </style>  
  </head>
  <body>

<h:form id="gsForm" >
	
<h1>GUDMAP BETA</h1>

	<p>This beta application demonstrates the new search capabilities provided by Solr indexing.</p>
	<p>Please note:</p>
	<ul> 
		<li style="font-size:small;">It is a work in progress</li>
		<li style="font-size:small;"> The beta application menu-bar links will navigate away from the beta site and back to the main GUDMAP website. You can identify which application you are viewing by checking the URL
			<ul>
				<li style="font-size:small;">Production: starts with www.gudmap.org/gudmap/xxxx or www.gudmap.org/xxxx</li>
				<li style="font-size:small;">Beta: starts with www.gudmap.org/gudmap_beta/xxxx</li>
			</ul>		
		</li>
		<li style="font-size:small;">We encourage any feedback you might have about the new search to be sent to gudmap-db@gudmap.org</li>
		<li style="font-size:small;">Click the OK button to proceed to GUDMAP Beta</li>
		<li style="font-size:small;">Click the Cancel button to stay on the current GUDMAP production site</li>
	</ul>  

    
</h:form>

    <table height="35">
      <tr align="center" class="stripey"></tr>
    </table>
    <table>
      <tr>
        <td>
          <input type="button" value="OK" onclick="var w=window.open('http://www.gudmap.org/gudmap_beta');w.focus();self.close();return false;">
          <input type="button" onclick="self.close();" value="Cancel">
        </td>
      </tr>
    </table>
  </body>
</html>
