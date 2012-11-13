<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<f:view>
<c:set var="stageSeriesLong" value="${initParam.stage_series_long}" scope="application" />
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<title>Images for submission&nbsp;<h:outputText value="#{ImageDetailBean.imageDetail.accessionId}" /></title>
		
                
                    <link href="<c:out value="${pageContext.request.contextPath}" />/css/gudmap_css.css" type="text/css" rel="stylesheet">
                    
		<style>
			.detailsTable{
				border: solid 1px;
				text-align: center;
			}

			.col1{
				background-color: #E6E8FA;
				text-align: center;
			}
			
		</style>
		<script type="text/javascript">
		
			var isIE = ((navigator.userAgent.toLowerCase().indexOf("msie") != -1) && (navigator.userAgent.toLowerCase().indexOf("opera") == -1));

			var isViewerFrameLoaded =  false;
			var numTries = 0;
			
			function makeSureImageFrameIsLoaded() {
				if(!isIE)   //This is a work around for IE only (when image viewer frame is faild to show). It reloads the page when image viewer frame is faild to show
					return;
				try{
					if(isViewerFrameLoaded || numTries>5) {
						return;
					}
					numTries++;
//					window.location.reload();
					frames['zoomViewer'].location.href = document.getElementById('viewerFrameSrc').value;
					setTimeout('makeSureImageFrameIsLoaded();', 100);
				}
				catch(excep) {
					numTries++;
//					window.location.reload();
					frames['zoomViewer'].location.href = document.getElementById('viewerFrameSrc').value;
					setTimeout('makeSureImageFrameIsLoaded();', 100);
					return true;
				}
			}
			
			function setDomain() {
				if(!isIE) {   // This is a work around for IE only (when image viewer frame is faild to show)
					document.getElementById('zoomViewerSection').style.visibility = 'visible';
					return false;
				}
				window.opener.setZoomViewerLoaded(); // This is to inform oppener window that the main page (not the frame) is loading
				zoomViewerOriginalDomain = document.domain;
				var newDomain = zoomViewerOriginalDomain.substring(zoomViewerOriginalDomain.indexOf('.')+1);
				if (newDomain == zoomViewerOriginalDomain) {
					alert('Warning! You should use full domain name instead of "' + zoomViewerOriginalDomain + '" for this page to work properly in IE'); 
					return false;
//					document.domain = 'hgu.mrc.ac.uk';
				}
				else {
					document.domain = newDomain;
				}
				
				return true;
			}
			
			function setViewerFrameLoaded() {  // This is called by mrciip viewer frame (from its server; this is why domain was changed before)
				isViewerFrameLoaded = true;
				document.getElementById('zoomViewerSection').style.visibility = 'visible';
			}
			
		</script>
	</head>

	<body onload="if (setDomain()) setTimeout('makeSureImageFrameIsLoaded();', 200);">
		<h:panelGrid id="zoomViewerTopTable" columns="7" width="100%" cellspacing="2" cellpadding="2" styleClass="detailsTable" 
					 rowClasses="plaintext, plaintextbold" columnClasses="table-stripey">
			<h:outputText value="ID" />
			<h:outputText value="Gene Symbol" />
			<h:outputText value="#{stageSeriesLong} Stage" />
			<h:outputText value="Gene Name" />
			<h:outputText value="Age" />
			<h:outputText value="Assay Type" />
			<h:outputText value="Specimen Type" />

			<h:outputText value="#{ImageDetailBean.imageDetail.accessionId}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.geneSymbol}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.stage}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.geneName}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.age}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.assayType}" />
			<h:outputText value="#{ImageDetailBean.imageDetail.specimenType}" />
		</h:panelGrid>
		<h:outputText value="<br>"  escape="false" />
		<h:outputText value='<input type="hidden" id="viewerFrameSrc" value="#{ImageDetailBean.viewerFrameSourceName}?project=#{ImageDetailBean.submissionName}&stack=#{ImageDetailBean.imageDir}&x=1&y=1&thumbnail=#{ImageDetailBean.thumbnail}&notes=#{ImageDetailBean.allNotes}&pImgs=#{ImageDetailBean.publicImages}" />' escape="false" />
		<h:outputText value='<div id="zoomViewerSection" style="visibility:hidden"> <iframe id="zoomViewer" name="zoomViewer" src="#{ImageDetailBean.viewerFrameSourceName}?project=#{ImageDetailBean.submissionName}&stack=#{ImageDetailBean.imageDir}&x=1&y=1&thumbnail=#{ImageDetailBean.thumbnail}&notes=#{ImageDetailBean.allNotes}&pImgs=#{ImageDetailBean.publicImages}" height="744" width="100%" frameborder="no" scrolling="no" marginwidth="1"/></div>' escape="false"/>
		
	</body>
</html>
</f:view>