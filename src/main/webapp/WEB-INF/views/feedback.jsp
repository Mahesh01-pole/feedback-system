<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Feedback</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="card">
    <h2>Feedback</h2>

    <div class="toggle">
      <button type="button" id="btnAdd">Add Feedback</button>
      <button type="button" id="btnEdit">Edit Feedback</button>
    </div>

    <c:if test="${not empty msg}"><div class="msg ${msgType}"><c:out value="${msg}"/></div></c:if>
    <div class="msg error" id="note" style="display:none"></div>

    <form method="post" action="${pageContext.request.contextPath}/feedback">
      <input type="hidden" name="mode" id="mode" value="add">
      <label for="text">Your Feedback:</label>
      <textarea id="text" name="text" placeholder="Write your feedback here..." maxlength="2000"></textarea>
      <button type="submit" id="submitBtn">Submit</button>
    </form>

    <div class="foot"><a href="${pageContext.request.contextPath}/logout">Log Out</a></div>
  </div>

  <div id="existing" hidden
       data-has="${not empty feedback}"
       data-text="<c:out value='${feedback.text}'/>"></div>

  <script>
    var box = document.getElementById('existing');
    var has = box.dataset.has === 'true';
    var existingText = box.dataset.text || '';
    var ta = document.getElementById('text');
    var mode = document.getElementById('mode');
    var btnAdd = document.getElementById('btnAdd');
    var btnEdit = document.getElementById('btnEdit');
    var note = document.getElementById('note');
    var submitBtn = document.getElementById('submitBtn');

    function setMode(m) {
      mode.value = m;
      btnAdd.classList.toggle('active', m === 'add');
      btnEdit.classList.toggle('active', m === 'edit');
      note.style.display = 'none';
      submitBtn.disabled = false;

      if (m === 'edit') {
        if (has) {
          ta.value = existingText;
        } else {
          ta.value = '';
          note.textContent = 'You have not submitted feedback yet. Use Add Feedback.';
          note.style.display = 'block';
          submitBtn.disabled = true;
        }
      } else {
        if (has) {
          ta.value = '';
          note.textContent = 'You already submitted feedback. Use Edit Feedback to change it.';
          note.style.display = 'block';
          submitBtn.disabled = true;
        }
      }
    }
    btnAdd.addEventListener('click', function () { setMode('add'); });
    btnEdit.addEventListener('click', function () { setMode('edit'); });
    setMode(has ? 'edit' : 'add');
  </script>
</body>
</html>
