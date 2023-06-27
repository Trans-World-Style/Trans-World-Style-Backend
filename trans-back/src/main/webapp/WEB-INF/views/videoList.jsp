<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
	<c:forEach items="${list}" var="bag">
		<a href="videoDetail?video_id=${bag.video_id}">
			<div>${bag.video_link}</div>
		</a>
	</c:forEach>
</body>
</html>