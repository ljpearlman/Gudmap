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
      <title>Image&nbsp;<c:out value="${submissionImage.imageName}" /></title>
      <style type="text/css">
      body { font-family: verdana, arial, helvetica, sans-serif; font-size: smaller; }
      hi { font-size: large; }
      tr { font-size: smaller; }
      th, td { border-left: 1px solid #000000;
               border-bottom: 1px solid #000000; padding: 2px; }
      th { color: #000000; background: #e9e9f2; }
      table { padding: 0px;  margin: 0px;
              border-top: 1px solid #000000;
              border-right: 1px solid #000000; }
      table.info { color: #000000; background: #e9e9f2; }
      table.data { color: #000000; background: #ffffff; }
      td.label { text-align: right; }
      td.value { font-weight: bold; }
      tr.even { color: #000000; background: #ffffff; }
      tr.odd { color: #000000; background: #eeeeee; }
    </style>
    </head>
    <body>
      <h:panelGrid columns="2" rowClasses="odd,even">
        <h:outputText value="ID:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.accessionId}" />
        
        <h:outputText value="Gene Symbol:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.geneSymbol}" />
        
        <h:outputText value="Gene Name:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.geneName}" />
        
        <h:outputText value="#{stageSeriesLong} Stage:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.stage}" />
        
        <h:outputText value="Age:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.age}" />
        
        <h:outputText value="Assay Type:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.assayType}" />
        
        <h:outputText value="Specimen Type:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.specimenType}" />
        
        <h:outputText value="Serial no.:" />
        <h:outputText value="#{ImageDetailBean.imageDetail.serialNo}" />
      </h:panelGrid>
      <h:graphicImage alt="" value="#{ImageDetailBean.imageDetail.filePath}#{ImageDetailBean.imageDetail.imageName}" />
    </body>
  </html>
</f:view>