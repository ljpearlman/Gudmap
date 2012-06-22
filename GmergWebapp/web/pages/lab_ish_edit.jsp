<!-- Author: Mehran Sharghi					 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%-- 
<header>
<script type="text/javascript">
	var w;
	function openDesktop1(obj) {
		//window.name="lab_ish_edit_window" 
		openZoomViewer(obj, 'desktop1', '1');
		w=window.open('ish_edit_expression.html?id='+obj,'desktop0','left=800,resizable=1,toolbar=0,scrollbars=1,width=600,height=700');
		w.focus();
	} 
	function closeImageViewer() {
		w.close();
		if (zoomViewerWindow != null)
			zoomViewerWindow.close();
	}		
</script>
</header>
--%>
<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText value="" rendered="#{LabISHEditBean.dumy}" />
	<f:subview id="labIshEdit">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
