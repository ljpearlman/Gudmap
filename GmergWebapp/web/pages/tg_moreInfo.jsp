<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>Explanatory Note</title>
    <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
    
    <style type="text/css">
    @import("<c:out value="${pageContext.request.contextPath}" />/css/ie51.css");
    </style>  
  </head>
  <body class="evenstripey">
    <h2 class="titletext">GUDMAP Gene Expression Database</h2>
    <table border="0" height="35">
      <tr align="center"></tr>
    </table>
    <table border="0" cellpadding="0" cellspacing="0">
      <tr class="header-stripey">
        <td><b>TG mouse marker strain interpretion</b></td>
      </tr>
      <tr class="header-stripey">
        <td class="plaintext">
<p>Mouse marker strain entries are unique GUDMAP entries that can contain data for multiple probes at multiple stages of development. The gene expression indicated on the annotation tree represents reporter expression visualised by a number of different techniques including direct reporter expression and indirect conditional reporter expression. The Theiler stage allocated to the Mouse marker strain entry is the stage upon which reporter expression in the annotation tree is based.</p>

	</td>
      </tr>
    </table>
    <table height="35">
      <tr align="center" class="stripey"></tr>
    </table>
    <table>
      <tr>
        <td>
          <input type="button" onclick="self.close();" value="Close">
        </td>
      </tr>
    </table>
  </body>
</html>
