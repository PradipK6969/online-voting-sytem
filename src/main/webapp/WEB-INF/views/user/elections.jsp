<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Elections</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Elections</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/elections">Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/voting-history">Voting History</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="elections">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <div class="election-list">
                <c:forEach items="${elections}" var="election">
                    <div class="election-item">
                        <h2>${election.title}</h2>
                        <p>${election.description}</p>
                        <p><strong>Status:</strong> ${election.status}</p>
                        <p><strong>Period:</strong> ${election.startDate} to ${election.endDate}</p>

                        <c:if test="${election.status eq 'ACTIVE'}">
                            <a href="${pageContext.request.contextPath}/user/vote?electionId=${election.id}" class="btn">Vote Now</a>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </section>
    </div>
</body>
</html>