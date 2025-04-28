<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Voting History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Your Voting History</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/elections">Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/voting-history">Voting History</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="voting-history">
            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th>Election</th>
                        <th>Candidate</th>
                        <th>Voted At</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${votes}" var="vote">
                        <tr>
                            <td>${vote.getAdditionalInfo('electionTitle')}</td>
                            <td>${vote.getAdditionalInfo('candidateName')}</td>
                            <td>${vote.votedAt}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html>