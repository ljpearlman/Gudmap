<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>
<html>
  <head>

    <title>EuReGene Gene Expression Database - Login</title>

    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">


    <link href="css/euregene_css.css" rel="stylesheet" type="text/css">
    <link rel="icon" href="../favicon.ico" type="image/ico">
  </head>
  <body>
    <h3>Entry Form for EuReGene Gene Expression Database</h3>
    <h:form>
    <p><h:messages errorClass="plainred" /></p>
    <p><h:outputText styleClass="plaintext" value="Enter username: "/><h:inputText binding="#{authenticationBean.nameInput}" value="#{authenticationBean.userName}" /> </p>
    <p><h:outputText styleClass="plaintext" value="Enter password: "/><h:inputSecret binding="#{authenticationBean.passInput}" value="#{authenticationBean.password}" /></p>
    <h:inputHidden id="loginCheck" validator="#{authenticationBean.validateLogin}" value="required" />
    <p> <h:commandButton value="Login" action="#{authenticationBean.login}" /></p>
    </h:form>
  </body>
</html>
</f:view>