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
        <td><b>WISH expression data Interpretion</b></td>
      </tr>
      <tr class="header-stripey">
        <td class="plaintext">
<p>There are several technical limitations to the whole-mount in situ hybridization (WISH) approach that may effect accurate scoring of a gene's detailed expression pattern leading to either false negative or positive scores. These limitations should be borne in mind when reviewing this data. For example, strong superficial reaction within a tissue may obscure internal expression domains resulting in false negative or false positive assignments particularly where a gene is expressed broadly within superficial cell populations. </p>

<p>Further, low-level ubiquitous expression is difficult to distinguish from background activity inherent in the methodology after long periods of detection (typically greater than 24 hours). </p>

<p>Finally, trapping of probe in luminal compartments (for example the ureter) may lead to erroneous signals in these structures (typically greater than 24 hours). The scoring attempts to be as accurate as possible within the boundaries of the technical limits of WISH. Other approaches such as section in situ hybridization (SISH) can overcome some of these limitations and are an important complement to WISH.</p>
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
