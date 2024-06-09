<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wypożyczalnia pojazdów</title>
        <style>.error { color: red; }</style>
    </head>
    <body>
        <h3>Edycja pojazdu</h3>
        <%-- komentarz JSP - nie jest renderowany --%>
        <form method="post">
            <table>
                <tr>
                    <td>Marka:</td>
                    <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zwiera znaczniki - zabezpiecza przez atakiem XSS --%>
                    <td><input type="text" name="brand" value="${fn:escapeXml(brand)}" ></td>
                    <%-- errors zawiera ew. błędy konwersji/walidacji dla poszczególnych pól --%>
                    <td><span class="error">${errors.brand}</span></td>
                </tr>
                <tr>
                    <td>Model:</td>
                    <td><input type="text" name="model" value="${fn:escapeXml(model)}" ></td>
                    <td><span class="error">${errors.model}</span></td>
                </tr>
                <tr>
                    <td>Cena:</td>
                    <td><input type="text" name="price_per_day" value="${fn:escapeXml(price_per_day)}" > </td>
                    <td><span class="error">${errors.price}</span></td>
                </tr>
            </table>
            <input type="submit" value="Save"> 
        </form>
    </body>
</html>

