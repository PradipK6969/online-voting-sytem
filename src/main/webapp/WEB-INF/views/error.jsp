<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Error Occurred</h1>
        <div class="alert error">
            <p><strong>Error Message:</strong> ${errorMessage}</p>
            <c:if test="${not empty pageContext.exception}">
                <p><strong>Exception:</strong> ${pageContext.exception}</p>
                <p><strong>Stack Trace:</strong></p>
                <pre>${pageContext.exception.stackTrace}</pre>
            </c:if>
            <c:if test="${not empty currentPage}">
                <p><strong>Current Page:</strong> ${currentPage}</p>
            </c:if>
        </div>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn">Return to Dashboard</a>
    </div>
</body>
</html>