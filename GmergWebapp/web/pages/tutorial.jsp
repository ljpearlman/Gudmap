<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/tlds/components.tld" prefix="d" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  
  <jsp:useBean class="gmerg.entities.focus.components.model.MicTutorialBean" id="micTutorialBean" scope="request"/>

    <table border="0" width="100%">
      <tr>
        <td colspan="2" align="left"><h3><c:out value="${MicTutorialBean.title}" /></h3></td><td colspan="2" align="right"></td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold" width="30%">Description</td>
        <td class="datatext" width="30%">
          <table border="0" cellspacing="2">
            
	          <tr>
                  <td align="left">
		  <c:forEach items="${MicTutorialBean.tutorial}" var="row" varStatus="current">
                    <p><c:out value="${row}" /></p>
		  </c:forEach>
                  </td>
		  </tr>
            
          </table>	
	</td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr class="table-stripey">
        <td  valign="top" align="left" class="plaintextbold">Images</td>
        <td>
          <table border="0" cellspacing="2">
            <c:forEach items="${MicTutorialBean.subImages}" var="row" varStatus="current">
	          <tr>
                  <td align="left">
                    <img border="0" width="500" src="<c:out value="${row}" />" />
                  </td>
		  </tr>
            </c:forEach>

          </table>
        </td>
      </tr>
    </table>

  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>