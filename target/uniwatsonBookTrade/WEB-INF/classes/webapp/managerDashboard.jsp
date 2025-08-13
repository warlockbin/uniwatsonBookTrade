<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="navbar.jsp"/>

<div class="container mt-4">
  <h3>管理後台</h3>

  <div class="card mb-4">
    <div class="card-header">新增違規紀錄</div>
    <div class="card-body">
      <form class="row g-2" action="${pageContext.request.contextPath}/manager" method="post">
        <div class="col-md-3"><input class="form-control" name="userId" placeholder="User_ID" required></div>
        <div class="col-md-7"><input class="form-control" name="reason" placeholder="違規事由" required></div>
        <div class="col-md-2"><button class="btn btn-danger w-100">新增</button></div>
      </form>
    </div>
  </div>

  <div class="card">
    <div class="card-header">違規紀錄</div>
    <div class="card-body">
      <c:if test="${empty violations}">
        <div class="alert alert-info">目前沒有違規紀錄</div>
      </c:if>
      <table class="table table-sm">
        <thead><tr><th>#</th><th>時間</th><th>User_ID</th><th>Manager_ID</th><th>原因</th></tr></thead>
        <tbody>
          <c:forEach var="v" items="${violations}">
            <tr>
              <td>${v.serialNum}</td>
              <td>${v.time}</td>
              <td>${v.userId}</td>
              <td>${v.managerId}</td>
              <td>${v.reason}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>
