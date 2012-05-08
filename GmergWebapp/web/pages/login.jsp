<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<f:view>
<html>
  <head>
    <title>GUDMAP Gene Expression Database - Login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <link href="../css/gudmap_css.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    </br>
    <h3 align="left">Please enter your name and password</h3>
    <h:form>
    <table width="25%">
<%-- 
      <tr>
        <td>
          <h:messages errorClass="plainred" />
        </td>
      </tr>
--%>
      <tr>
        <td>
          <h:outputText styleClass="plaintext" value="Name: "/>
        </td>
        <td>
          <h:inputText id="name" value="#{userBean.userName}" required="true" />
        </td>
        <td>
          <h:message for="name" styleClass="plainred" />
        </td>
      </tr>
      <tr>
        <td>
          <h:outputText styleClass="plaintext" value="Password: "/>
        </td>
        <td>
          <h:inputSecret id="pass" value="#{userBean.password}" required="true" />
        </td>
        <td>
          <h:message for="pass" styleClass="plainred" />
        </td>
      </tr>
<%-- 
      <tr>
        <td>
          <h:inputHidden id="loginCheck" validator="#{userBean.validateLogin}" value="validation" />
        </td>
      </tr>
--%>
      <tr>
        <td>
          <h:commandButton value="Login" action="#{userBean.validateLogin}" />
        </td>
      </tr>
    </table>
    </h:form>
  </body>
</html>
</f:view>