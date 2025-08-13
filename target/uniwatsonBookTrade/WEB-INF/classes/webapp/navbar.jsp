<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- 統一載入 Bootstrap（保證所有 include 都有樣式） -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/home.jsp">UniWatson 二手書</a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#nav">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div id="nav" class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link ${param.active=='home'?'active':''}"
             href="${pageContext.request.contextPath}/home.jsp">首頁</a>
        </li>

        <li class="nav-item">
          <a class="nav-link ${param.active=='books'?'active':''}"
             href="${pageContext.request.contextPath}/books">書籍列表</a>
        </li>

        <li class="nav-item">
          <a class="nav-link ${param.active=='courses'?'active':''}"
             href="${pageContext.request.contextPath}/courses">課程用書</a>
        </li>

        <c:if test="${not empty sessionScope.user}">
          <li class="nav-item">
            <a class="nav-link ${param.active=='sell'?'active':''}"
               href="${pageContext.request.contextPath}/sell">我的商品</a>
          </li>
          <li class="nav-item">
            <a class="nav-link ${param.active=='orders'?'active':''}"
               href="${pageContext.request.contextPath}/orders">我的訂單</a>
          </li>
        </c:if>

        <c:if test="${sessionScope.role == 'manager'}">
          <li class="nav-item">
            <a class="nav-link ${param.active=='manager'?'active':''}"
               href="${pageContext.request.contextPath}/manager">管理後台</a>
          </li>
        </c:if>
      </ul>

      <div class="d-flex">
        <c:if test="${not empty sessionScope.user}">
          <span class="navbar-text me-3">歡迎，${sessionScope.user.name}</span>
          <a class="btn btn-outline-light" href="${pageContext.request.contextPath}/logout">登出</a>
        </c:if>
        <c:if test="${empty sessionScope.user}">
          <a class="btn btn-outline-light me-2" href="${pageContext.request.contextPath}/login.jsp">登入</a>
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/register">註冊</a>
        </c:if>
      </div>
    </div>
  </div>
</nav>
