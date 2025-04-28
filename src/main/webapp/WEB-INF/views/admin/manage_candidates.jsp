<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Candidates</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Manage Candidates for ${election.title}</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="manage-candidates">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <a href="${pageContext.request.contextPath}/admin/add-candidate?electionId=${election.id}" class="btn">Add New Candidate</a>
            <a href="${pageContext.request.contextPath}/admin/manage-elections" class="btn">Back to Elections</a>

            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Party</th>
                        <th>Bio</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${candidates}" var="candidate">
                        <tr>
                            <td>${candidate.name}</td>
                            <td>${candidate.party}</td>
                            <td>${candidate.bio}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/delete-candidate?id=${candidate.id}&electionId=${election.id}" class="btn danger">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html>