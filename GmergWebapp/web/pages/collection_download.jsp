<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<head>
	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
</head>

<body onload="getById('collectionDownloadForm:downloadLink').onclick()">
					 
<f:view>
	<h:form id="collectionDownloadForm">
		<h:commandLink id="downloadLink" action="#{CollectionListBrowseBean.downloadCollection1}">
			<f:param name="actionMethodCalled" value="download" />
			<f:param name="collectionId" value="2" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
