<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body>
<c:forEach items="${it.msgList}" var="msg"> 
<p>reciever:   <c:out value="${msg.SenderEmail}"> </c:out> </p><br>
<p>message:   <c:out value="${msg.Message}"> </c:out> </p><br>

</c:forEach>
</body>
</html>