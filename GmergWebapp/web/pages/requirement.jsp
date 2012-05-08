<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
  <jsp:include page="/includes/header.jsp" />
    <h:panelGrid styleClass="header-stripey" cellpadding="5" cellspacing="5">
      <f:verbatim><h3>System Requirements</h3></f:verbatim>

			<f:verbatim></f:verbatim>

           <h:panelGroup>
                <f:verbatim>
                <span>
				  <ul class="disc">
		    		<li class="datatext">This site has been tested to work on the following platforms and browsers:
						<ul class="disc">
				    		<li class="datatext">Linux (i686) - Firefox (v2.0) and  Mozilla (v1.7.2)</li>
						    <li class="datatext">Macintosh PPC 10.4.11 - Safari(v3.0.4) and Firefox (v2.0)</li>
						    <li class="datatext">Macintosh Intel 10.4.11 - Safari(v3.0.4) and Firefox (v2.0.0.11)</li>
						    <li class="datatext">Windows XP: Internet Explorer (v 6.0) and Firefox (v2.0.0.11)</li>
						    <li class="datatext">Windows Vista: Internet Explorer (v 7.0.6) and Firefox (v2.0.0.8)</li>
						</ul>
						
						
					</li>
				    <li class="datatext">The site may run on other configurations but we cannot guarantee its stabilty.
                     Please contact us if you wish to report any bugs in the suggested configurations or any other configurations which function correctly.</li>
				    <li class="datatext">Javascript must be enabled on your browser for this site to function correctly.</li>
				  </ul>
				</span>      	  
				</f:verbatim>  
			</h:panelGroup>	 
		</h:panelGrid>  
  <f:subview id="footer">
    <jsp:include page="/includes/footer.jsp" />
  </f:subview>
</f:view>