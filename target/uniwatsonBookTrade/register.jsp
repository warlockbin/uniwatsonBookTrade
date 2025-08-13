<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="navbar.jsp"/>

<div class="container mt-4" style="max-width: 720px;">
  <div class="card shadow-sm">
    <div class="card-header bg-primary text-white fs-4">註冊</div>
    <div class="card-body">
      <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
      </c:if>

      <!-- 一定要帶 contextPath，避免 404 -->
      <form method="post" action="${pageContext.request.contextPath}/register" accept-charset="UTF-8">
        <div class="mb-3">
          <label class="form-label">帳號 (USER_ID) *</label>
          <input type="text" class="form-control" name="userId" required>
        </div>
        <div class="mb-3">
          <label class="form-label">姓名 *</label>
          <input type="text" class="form-control" name="name" required>
        </div>
        <div class="mb-3">
          <label class="form-label">電話 *</label>
          <input type="text" class="form-control" name="phone" required>
        </div>
        <div class="mb-3">
          <label class="form-label">系所</label>
          <input type="text" class="form-control" name="department">
        </div>
        <div class="mb-3">
          <label class="form-label">密碼 *</label>
          <input type="password" class="form-control" name="password" required>
        </div>
        <div class="d-flex gap-2">
          <button type="submit" class="btn btn-primary">建立帳號</button>
          <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-secondary">已有帳號？去登入</a>
        </div>
      </form>
    </div>
  </div>
</div>
