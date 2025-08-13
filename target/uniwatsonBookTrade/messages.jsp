<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="navbar.jsp"/>

<div class="container mt-4" style="max-width: 760px;">
  <h3 class="mb-3">訂單訊息</h3>

  <ul class="list-group mb-3">
    <c:forEach var="m" items="${messages}">
      <li class="list-group-item">
        <div class="small text-muted">${m.time}　${m.sendoutId} → ${m.receivedId}</div>
        <div>${m.content}</div>
      </li>
    </c:forEach>
    <c:if test="${empty messages}">
      <li class="list-group-item text-muted">目前沒有訊息</li>
    </c:if>
  </ul>

  <form action="${pageContext.request.contextPath}/messages" method="post" class="row g-2">
    <input type="hidden" name="orderId" value="${param.orderId != null ? param.orderId : orderId}"/>
    <div class="col-md-3">
      <input class="form-control" name="to" placeholder="收件人（留空自動對方）">
    </div>
    <div class="col-md-7">
      <input class="form-control" name="content" placeholder="訊息內容" required>
    </div>
    <div class="col-md-2">
      <button class="btn btn-primary w-100" type="submit">送出</button>
    </div>
  </form>

  <a class="btn btn-link mt-3" href="${pageContext.request.contextPath}/orders">返回我的訂單</a>
</div>
