<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Admin Feedback Dashboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="card wide">
    <h2>Admin Feedback Dashboard</h2>
    <c:if test="${not empty msg}"><div class="msg ${msgType}"><c:out value="${msg}"/></div></c:if>

    <table>
      <thead>
        <tr><th>ID</th><th>Feedback</th><th>Date</th><th>Actions</th></tr>
      </thead>
      <tbody>
        <c:forEach var="f" items="${feedbackList}">
          <fmt:formatDate value="${f.updatedDate}" pattern="yyyy-MM-dd" var="d"/>
          <tr data-id="${f.id}"
              data-user="<c:out value='${f.username}'/>"
              data-email="<c:out value='${f.email}'/>"
              data-text="<c:out value='${f.text}'/>"
              data-date="${d}">
            <td>${f.id}</td>
            <td><c:out value="${f.text}"/></td>
            <td class="date">${d}</td>
            <td>
              <div class="actions">
                <button type="button" class="js-view">View</button>
                <button type="button" class="js-edit">Edit</button>
                <form method="post" action="${pageContext.request.contextPath}/admin/dashboard"
                      onsubmit="return confirm('Delete this feedback?');">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="${f.id}">
                  <button type="submit">Delete</button>
                </form>
              </div>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty feedbackList}">
          <tr><td colspan="4" style="text-align:center">No feedback yet.</td></tr>
        </c:if>
      </tbody>
    </table>

    <div class="foot"><a href="${pageContext.request.contextPath}/logout">Log Out</a></div>
  </div>

  <!-- View modal -->
  <div class="modal" id="viewModal">
    <div class="card">
      <h2>Feedback Details</h2>
      <p><b>ID:</b> <span id="vId"></span></p>
      <p><b>Username:</b> <span id="vUser"></span></p>
      <p><b>Email:</b> <span id="vEmail"></span></p>
      <p><b>Date:</b> <span id="vDate"></span></p>
      <p><b>Feedback:</b> <span id="vText"></span></p>
      <div class="row"><button type="button" class="js-close">Close</button></div>
    </div>
  </div>

  <!-- Edit modal -->
  <div class="modal" id="editModal">
    <div class="card">
      <h2>Edit Feedback</h2>
      <form method="post" action="${pageContext.request.contextPath}/admin/dashboard">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="id" id="eId">
        <label for="eText">Feedback:</label>
        <textarea name="text" id="eText" maxlength="2000" required></textarea>
        <div class="row">
          <button type="submit">Save</button>
          <button type="button" class="js-close">Cancel</button>
        </div>
      </form>
    </div>
  </div>

  <script>
    function $(id) { return document.getElementById(id); }
    function closeAll() {
      $('viewModal').classList.remove('open');
      $('editModal').classList.remove('open');
    }
    document.querySelectorAll('.js-view').forEach(function (b) {
      b.addEventListener('click', function () {
        var d = b.closest('tr').dataset;
        $('vId').textContent = d.id;
        $('vUser').textContent = d.user;
        $('vEmail').textContent = d.email;
        $('vDate').textContent = d.date;
        $('vText').textContent = d.text;
        $('viewModal').classList.add('open');
      });
    });
    document.querySelectorAll('.js-edit').forEach(function (b) {
      b.addEventListener('click', function () {
        var d = b.closest('tr').dataset;
        $('eId').value = d.id;
        $('eText').value = d.text;
        $('editModal').classList.add('open');
      });
    });
    document.querySelectorAll('.js-close').forEach(function (b) { b.addEventListener('click', closeAll); });
    document.addEventListener('keydown', function (e) { if (e.key === 'Escape') closeAll(); });
  </script>
</body>
</html>
