<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:form id="browseForm">
		<h:outputText styleClass="superbigplaintext" value="Gene Index:" />
		<t:dataList id="indexRow" var="index" value="#{FocusGeneIndexBean.index}" layout="simple" styleClass="plaintextbold" >
			<h:outputLink styleClass="superbigplaintext" id="submissionID" value="focus_gene_index_browse.html?index=#{index}&#{Visit.statusParam}">
				<h:outputText styleClass="superbigplaintext" value="#{index}" />
			</h:outputLink>
		</t:dataList>
		
		<h:dataTable width="100%" styleClass="browseTable" columnClasses="geneCol1, geneCol2, geneCol3" rowClasses="table-stripey,table-nostripe" headerClass="align-top-left-stripey" 
					 var="row" binding="#{FocusGeneIndexBean.myDataTable}" >
			<h:column>
				<f:facet name="header">
					<h:outputText styleClass="bigplaintext" value="Gene Symbol" />
				</f:facet>
				<h:outputLink styleClass="plaintext" value="gene.html" rendered='#{row[7] != "" }'>
					<h:outputText styleClass="plaintext" value="#{row[0]}"/>
					<f:param value="#{row[7]}" name="geneId" />
				</h:outputLink>
				<h:outputText styleClass="plaintext" value="#{row[0]}" rendered='#{row[7] == "" }'/>
			</h:column>
			
			<h:column>
				<f:facet name="header">             
					<h:panelGroup> 
						<h:outputText styleClass="bigplaintext" value="In Situ Expression" />
						<h:panelGrid columns="2">
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/DetectedRoundPlus20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Present" />
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/NotDetectedRoundMinus20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Not detected" />
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/PossibleRound20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Uncertain" />
						</h:panelGrid>
					</h:panelGroup>
				</f:facet>
				<h:panelGrid id="IshExpr" columns="3" width="100%" style="text-align:left" columnClasses="geneCol1-1" >
				    <h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/DetectedRoundPlus20x20.gif"/>               
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[1]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[1]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="present" />
							<f:param name="sub" value="ish" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[1]}" rendered="#{row[1]=='0'}"/>
					</h:panelGroup>
					<h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/NotDetectedRoundMinus20x20.gif"/>          	   
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[2]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[2]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="absent" />
							<f:param name="sub" value="ish" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[2]}" rendered="#{row[2]=='0'}"/>
					</h:panelGroup>
					<h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/PossibleRound20x20.gif"/>         	   
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[3]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[3]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="unknown" />
							<f:param name="sub" value="ish" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[3]}" rendered="#{row[3]=='0'}"/>
					</h:panelGroup>
				</h:panelGrid>				
			</h:column>
<%-- 
			<h:column>
	            <f:facet name="header">
					<h:panelGroup> 
						<h:outputText styleClass="bigplaintext" value="Microarray Expression" />
						<h:panelGrid columns="2">
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/DetectedRoundPlus20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Present" />
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/NotDetectedRoundMinus20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Absent" />
							<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/PossibleRound20x20.gif"/>
							<h:outputText styleClass="plaintext" value="Marginal" />
						</h:panelGrid>
		            </h:panelGroup>
	            </f:facet>
				<h:panelGrid id="ArrayExpr" columns="3" width="100%" style="text-align:left" columnClasses="geneCol1-1" >
				    <h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/DetectedRoundPlus20x20.gif"/>
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[4]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[4]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="P" />
							<f:param name="sub" value="mic" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[4]}" rendered="#{row[4]=='0'}"/>
					</h:panelGroup>
					<h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/NotDetectedRoundMinus20x20.gif"/>
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[5]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[5]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="A" />
							<f:param name="sub" value="mic" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[5]}" rendered="#{row[5]=='0'}"/>
					</h:panelGroup>
					<h:panelGroup>
						<h:graphicImage styleClass="icon" height="20" width="20" value="../images/tree/PossibleRound20x20.gif"/>
						<h:commandLink action="#{FocusGeneIndexBean.queryGenes}" styleClass="plaintext" rendered="#{row[6]!='0'}" >
							<h:outputText styleClass="plaintext" value="#{row[6]}"/>
							<f:param name="query" value="GeneSymbol" />
							<f:param name="input" value="#{row[0]}" />
							<f:param name="exp" value="M" />
							<f:param name="sub" value="mic" />
						</h:commandLink>
						<h:outputText styleClass="plaintext" value="#{row[6]}" rendered="#{row[6]=='0'}"/>
					</h:panelGroup>
				</h:panelGrid>				
			</h:column>
--%>						
		</h:dataTable>
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />

</f:view>
