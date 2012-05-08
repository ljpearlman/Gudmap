<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="/WEB-INF/tlds/components.tld" prefix="d" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<f:view>
  <jsp:include page="/includes/header.jsp" />
  
  <jsp:useBean class="gmerg.entities.focus.components.model.MicOrganDiscriptionBean" id="micOrganDiscriptionBean" scope="request"/>
    <table border="0" width="100%">
      <tr>
        <td colspan="2" align="left" width="98%"><h3><c:out value="${micOrganDiscriptionBean.title}" /></h3></td><td colspan="2" align="right" width="2%"></td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>    
    </table>
    <table border="0" width="100%">
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold" width="20%">Gene List</td>
        <td width="80%">
          <table border="0" cellspacing="2">
	        <tr>
              <td class="plaintextbold" align="center" width=30%></td>
              <td class="plaintextbold" align="center">
                <c:out value="${micOrganDiscriptionBean.genelist[0][0]}" />
              </td>		  
            </tr>	  
		  <c:if test="${micOrganDiscriptionBean.heading[0] != null}">
	        <tr align="center" valign="middle" class="table-nostripe">
              <td class="plaintextbold" align="left" width=30%>
                ${micOrganDiscriptionBean.heading[0]}
              </td>
              <td align="center">
		        <c:forEach items="${micOrganDiscriptionBean.genelist[1]}" var="row">
		          <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		          </c:if>
  		        </c:forEach>
		        <c:forEach items="${micOrganDiscriptionBean.genelist[2]}" var="row">
		          <c:if test="${row[0] != '' && row[1] != ''}">
		            <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		          </c:if>
		        </c:forEach>
              </td>
		    </tr>
		  </c:if>
		  
		  <c:if test="${micOrganDiscriptionBean.heading[1] != null}">
	          <tr>
                  <td class="plaintextbold" align="left" width=30%>
                    ${micOrganDiscriptionBean.heading[1]}
                  </td>
                  <td align="center">
                    <c:forEach items="${micOrganDiscriptionBean.genelist[3]}" var="row">
		    <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		    </c:if>
		  </c:forEach>
		  <c:forEach items="${micOrganDiscriptionBean.genelist[4]}" var="row">
		    <c:if test="${row[0] != '' && row[1] != ''}">
		    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		    </c:if>
		  </c:forEach>
                  </td>
		  </tr>
		  </c:if>
		  
		  <c:if test="${micOrganDiscriptionBean.heading[2] != null}">
	          <tr align="center" valign="middle" class="table-nostripe">
                  <td class="plaintextbold" align="left" width=30%>
                    ${micOrganDiscriptionBean.heading[2]}
                  </td>
                  <td align="center">
                    <c:forEach items="${micOrganDiscriptionBean.genelist[5]}" var="row">
		    <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		    </c:if>
		  </c:forEach>
		  <c:forEach items="${micOrganDiscriptionBean.genelist[6]}" var="row">
		    <c:if test="${row[0] != '' && row[1] != ''}">
		    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		    </c:if>
		  </c:forEach>
                  </td>
		  </tr>
		  </c:if>
		  
		  <c:if test="${micOrganDiscriptionBean.heading[3] != null}">
	          <tr>
                  <td class="plaintextbold" align="left" width=30%>
                    ${micOrganDiscriptionBean.heading[3]}
                  </td>
                  <td align="center">
                    <c:forEach items="${micOrganDiscriptionBean.genelist[7]}" var="row">
		    <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		    </c:if>
		  </c:forEach>
		  <c:forEach items="${micOrganDiscriptionBean.genelist[8]}" var="row">
		    <c:if test="${row[0] != '' && row[1] != ''}">
		    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		    </c:if>
		  </c:forEach>
                  </td>
		  </tr>
		  </c:if>
		  
		  <c:if test="${micOrganDiscriptionBean.heading[4] != null}">
	          <tr align="center" valign="middle" class="table-nostripe">
                  <td class="plaintextbold" align="left" width=30%>
                    ${micOrganDiscriptionBean.heading[4]}
                  </td>
                  <td align="center">
                    <c:forEach items="${micOrganDiscriptionBean.genelist[9]}" var="row">
		    <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		    </c:if>
		  </c:forEach>
		  <c:forEach items="${micOrganDiscriptionBean.genelist[10]}" var="row">
		    <c:if test="${row[0] != '' && row[1] != ''}">
		    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		    </c:if>
		  </c:forEach>
                  </td>
		  </tr>
		  </c:if>
		  
		  <c:if test="${micOrganDiscriptionBean.heading[5] != null}">
	          <tr>
                  <td class="plaintextbold" align="left" width=30%>
                    ${micOrganDiscriptionBean.heading[5]}
                  </td>
                  <td align="center">
                    <c:forEach items="${micOrganDiscriptionBean.genelist[11]}" var="row">
		    <c:if test="${row != ''}">
                    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row}" />">View All</a></span></p>
		    </c:if>
		  </c:forEach>
		  <c:forEach items="${micOrganDiscriptionBean.genelist[12]}" var="row">
		    <c:if test="${row[0] != '' && row[1] != ''}">
		    <p><span class="plaintext"><a class="plaintextbold" href="<c:out value="${row[0]}" />">Download <c:out value="${row[1]}" /></a></span></p>
		    </c:if>
		  </c:forEach>
                  </td>
		  </tr>		
		  </c:if>  		  		  		  		  
          </table>
        </td>
      </tr> 
      
      
              
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>      
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold" width="30%">PI</td>
        <td class="datatext" align="left" width="30%"><c:out value="${micOrganDiscriptionBean.pi}" /></td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold">Title</td>
        <td>
          <table border="0" cellspacing="2">
	          <tr>
                  <td class="datatext" align="left">
                    <c:out value="${micOrganDiscriptionBean.secondTitle}" />
                  </td>
		  </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold">Summary</td>
        <td>
          <table border="0" cellspacing="2">
	          <tr>
                  <td class="datatext" align="left">
		  <c:forEach items="${micOrganDiscriptionBean.summary}" var="row" varStatus="current">
                    <p><c:out value="${row}" /></p>
		  </c:forEach>  
                  </td>
		  </tr>	  
                  <tr>
                  <td align="left">
                    <span class="plaintext">
                    <a class="plaintextbold" href="<c:out value="${micOrganDiscriptionBean.links[0]}" />">Click to see tutorial.</a>
                    </span>
                  </td>
		  </tr>		
		  <c:if test="${micOrganDiscriptionBean.links[2] != null}">
                  <tr>
                  <td align="left">
                    <span class="plaintext"><a class="plaintextbold" href="<c:out value="${micOrganDiscriptionBean.links[2]}" />">Click to see movie.</a></span>
                  </td>
                  </tr>
                  </c:if>  
          </table>
        </td>
      </tr>    
      <tr>
        <td colspan="2" align="left">&nbsp;</td>
      </tr>
      <tr class="table-stripey">
        <td valign="top" align="left" class="plaintextbold">Overall Design</td>
        <td>
          <table border="0" cellspacing="2">
	          <tr>
                  <td class="datatext" align="left">
		  <c:forEach items="${micOrganDiscriptionBean.design}" var="row" varStatus="current">
                    <p><c:out value="${row}" /></p>
		  </c:forEach>  
                  </td>
		  </tr>	  
                  <tr>
                  <td align="left">
                    <span class="plaintext"><a class="plaintextbold" href="<c:out value="${micOrganDiscriptionBean.links[1]}" />">Click to see protocol.</a></span>
                  </td>
		  </tr>			  
          </table>
        </td>
      </tr>                 
    </table>
  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>