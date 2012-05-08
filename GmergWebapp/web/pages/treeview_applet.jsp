<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<head>
	<script type="text/javascript">
		function appletInitialised(){
//			window.blur();
		}
		
		function appletTerminated() {
			window.close()
		}
	</script>
</head>

<f:view>
	<h:outputText value="<br/><br/>&nbsp;&nbsp;Treeview applet will open in a separete window shortly.<br/><br/>" styleClass="plaintext" escape="false" />
	<%-- ++++++++++++++++++++++++++++++ TreeView Applet +++++++++++++++++++++++++++++++++++++++++ --%>
	<h:outputText value="#{UtilityBean.treeviewAppletCode}" escape="false" />
</f:view>

