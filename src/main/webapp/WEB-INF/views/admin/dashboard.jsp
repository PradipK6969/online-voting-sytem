<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Welcome, Admin ${admin.fullName}</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="dashboard">
            <div class="stats">
                <div class="stat-card">
                    <h2>Total Elections</h2>
                    <p>${electionCount}</p>
                </div>
                <div class="stat-card">
                    <h2>Total Users</h2>
                    <p>${userCount}</p>
                </div>
            </div>

            <div class="quick-actions">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/admin/add-election" class="btn">Create New Election</a>
                    <a href="${pageContext.request.contextPath}/admin/manage-users" class="btn">Verify Users</a>
                </div>
            </div>
        </section>
    </div>
</body>
</html>