<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

            </td>
          </tr>
        </table>
      </div>
      </div>
      <div id="footer">
        <table class="defaultWidth" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left">
              <a href="../developments.html" class="plaintextbold">Future Developments</a>
            </td>
            <c:choose>
              <c:when test="${editorLoggedIn != null}">
            <td align="right">
	      <span class="small"><strong>Hello, <c:out value="${editorLoggedIn.userName}" />.</strong> (If you're not <c:out value="${editorLoggedIn.userName}" />, <a href="<c:out value="applicationVar.ctxtPath" />/gudmapdbupdate?query=logout">click here</a>.)</span>
	    </td>
              </c:when>
              <c:otherwise>
            <td align="right">
              <a href="../login.html" class="plaintextbold">Editor login</a>
            </td>  
              </c:otherwise>
            </c:choose>  
          </tr>
          <tr>
            <td colspan="2" align="center">
              <span class="plaintextbold">Contact <a href="mailto:gudmap-db@gudmap.org" class="plaintextbold">gudmap-db@gudmap.org</a> for database-related enquiries OR</span>
              <span class="plaintextbold"><a href="mailto:gudmap-editors@gudmap.org" class="plaintextbold">gudmap-editors@gudmap.org</a> for curation enquiries</span>
            </td>
          </tr>
        </table>
        <br />
        <p class="ineffable"><strong>Sponsoring Agencies:</strong></p>
        <ul id="Agencies">
          <li id="DHHS"><a href="http://www.hhs.gov/" title="Department of Health and Human Services">Department of Health and Human Services</a></li>
          <li id="NIH"><a href="http://www.nih.gov/" title="National Institutes of Health">National Institutes of Health</a></li>
          <li id="NIDDK"><a href="http://www.niddk.nih.gov/" title="National Institute of Diabetes and Digestive and Kidney Diseases">National Institute of Diabetes and Digestive and Kidney Diseases</a></li>
          <li id="NICHHD"><a href="http://www.nichd.nih.gov/" title="The National Institute of Child Health and Human Development">The National Institute of Child Health and Human Development</a></li>
        </ul>
        <p id="bottomNav"></p>
        <p id="Copyright">&copy; 2006 GUDMAP</p>
      </div>
    </div>
  </body>
</html>
