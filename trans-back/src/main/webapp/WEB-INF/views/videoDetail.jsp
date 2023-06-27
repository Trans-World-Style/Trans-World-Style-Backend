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
비디오 id: ${vo.get().video_id}<br>
비디오 links: ${vo.get().video_link}<br>
<video src="<c:url value='https://trans-world-style.s3.ap-northeast-2.amazonaws.com/upload/${vo.get().video_link}' />" controls width="500" height="auto">대체 텍스트</video>

</body>
</html>