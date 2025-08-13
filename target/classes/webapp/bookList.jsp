<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="navbar.jsp">
  <jsp:param name="active" value="books"/>
</jsp:include>

<div class="container mt-4">
  <h2 class="mb-3">書籍列表</h2>

  <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/books">
    <div class="col-sm-9 col-md-6">
      <input class="form-control" name="q" placeholder="輸入 ISBN / 書名 / 作者"
             value="${q}"/>
    </div>
    <div class="col-auto">
      <button class="btn btn-primary">搜尋</button>
    </div>
  </form>

  <c:choose>
    <c:when test="${empty books}">
      <div class="alert alert-secondary">沒有符合的結果</div>
    </c:when>
    <c:otherwise>
      <table class="table table-hover align-middle">
        <thead>
          <tr><th>ISBN</th><th>書名</th><th>作者</th><th style="width:160px">操作</th></tr>
        </thead>
        <tbody>
        <c:forEach var="b" items="${books}">
          <tr>
            <td><c:out value="${b.isbn}"/></td>
            <td><c:out value="${b.name}"/></td>
            <td><c:out value="${b.author}"/></td>
            <td>
              <a class="btn btn-outline-primary btn-sm"
                 href="${pageContext.request.contextPath}/books?action=view&isbn=${b.isbn}">
                查看詳情
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</div>
