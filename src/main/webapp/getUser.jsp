<%@ page import="ru.appline.logic.Model" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users_HW</title>
</head>
<body>
Введите id пользователя (0 - для вывода всего списка пользователей)
<br/>

Доступно: <%
    Model model = Model.getInstance();
    out.print(model.getModel().size());
%>
<form method="get" action="get">
    <label>ID:
        <input type="text" name="id"><br/>
    </label>
    <button type="submit">Поиск</button>
</form>
<a href="index.jsp">Домой</a>
</body>
</html>