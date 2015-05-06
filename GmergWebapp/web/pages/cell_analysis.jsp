<%-- 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib prefix="rich" uri="http://richfaces.ajax4jsf.org/rich" %>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>


<f:view>
<jsp:include page="/includes/header.jsp">
	<jsp:param name="headerParam" value="analysispage" />
</jsp:include>

<h:form id="mainForm" >
		<script type="text/javascript">
						function handleInput (inField, e) {
							//alert(e.keyCode);
							if (e.keyCode === 13) {
								if(inField.id=='mainForm:geneInput')
				                	document.getElementById('mainForm:go1').click();

				            }
				            return false;//so that mainForm is not called
				        }
		</script>
		
<h:selectOneRadio layout="pageDirection" value="#{CellAnalysisBean.file}">
	<f:selectItem itemValue="item1" itemLabel="14434_KidneyCompon_RNAseq_normalized"/>
	<f:selectItem itemValue="item2" itemLabel="14434_Mendelsohn_RNAseq_normalized"/>
	<f:selectItem itemValue="item3" itemLabel="28853_ST1_GonadalCellTypes"/>
	<f:selectItem itemValue="item4" itemLabel="28853_ST1_Juxtaglomerular"/>
	<f:selectItem itemValue="item5" itemLabel="28853_ST1_kidneyCellTypes"/>
	<f:selectItem itemValue="item6" itemLabel="28853_ST1_PelvicGanglia"/>				
	<f:selectItem itemValue="item7" itemLabel="28853_ST1_kidneyCellTypesMouseSymbol"/>				
</h:selectOneRadio>      
		
</br>
		
<h:panelGroup style="margin-right:20px">

<h3>Enter Gene Symbol</h3>
	<h:inputText id="geneInput" value="#{CellAnalysisBean.geneInput}" size="18"  onkeypress="handleInput(this,event)"/>
         
	<h:commandButton id="go1" value="analyse" action="#{CellAnalysisBean.search}">
	</h:commandButton>
	
</h:panelGroup>   



</h:form>



<jsp:include page="/includes/footer.jsp" />

</f:view>


