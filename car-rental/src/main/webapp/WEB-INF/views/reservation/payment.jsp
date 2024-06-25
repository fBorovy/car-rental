<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Płatność</title>
        <script>
            function validateForm() {
                var cardNumber = document.getElementById("card_number").value;
                var cardSurname = document.getElementById("card_surname").value;
                var cardCVV = document.getElementById("card_cvv").value;

                if (!/^\d{26}$/.test(cardNumber)) {
                    alert("Numer karty musi składać się z 26 cyfr.");
                    return false;
                }
                if (!/^[a-zA-Z\s]+$/.test(cardSurname)) {
                    alert("Wprowadzono niewłaściwe nazwisko.");
                    return false;
                }
                if (!/^\d{3}$/.test(cardCVV)) {
                    alert("CVV musi zawierać 3 cyfry.");
                    return false;
                }
                return true;
            }
        </script>
</head>
<body>
    <h2>Opłacanie rezerwacji</h2>
    <form action="${pageContext.request.contextPath}/reservation/payment?id=${reservation.id}" method="post">
        <div>
            <label for="card_number">Numer karty:</label>
            <input type="text" id="card_number" name="card_number" required pattern="\d{26}">
        </div>
        <div>
            <label for="card_surname">Nazwisko na karcie:</label>
            <input type="text" id="card_surname" name="card_surname" required pattern="[a-zA-Z\s]+">
        </div>
        <div>
            <label for="card_cvv">Nr CVV:</label>
            <input type="text" id="card_cvv" name="card_cvv" required pattern="\d{3}">
        </div>
        <div>
            <button type="submit">Zapłać</button>
        </div>
    </form>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
</body>
</html>