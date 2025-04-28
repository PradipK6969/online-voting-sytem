<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Vote</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Vote in ${election.title}</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/elections">Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/voting-history">Voting History</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="vote">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/user/vote" method="post">
                <input type="hidden" name="electionId" value="${election.id}">

                <h2>Select a Candidate:</h2>
                <div class="candidates">
                    <c:forEach items="${candidates}" var="candidate">
                        <div class="candidate-item">
                            <input type="radio" id="candidate-${candidate.id}" name="candidateId" value="${candidate.id}" required>
                            <label for="candidate-${candidate.id}">
                                <h3>${candidate.name}</h3>
                                <p>${candidate.party}</p>
                                <p>${candidate.bio}</p>
                            </label>
                        </div>
                    </c:forEach>
                </div>

                <button type="submit" class="btn">Submit Vote</button>
            </form>
        </section>
    </div>
</body>
</html>