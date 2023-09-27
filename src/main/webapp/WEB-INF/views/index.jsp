<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script type="text/javascript" src="/js/jquery-3.6.4.js"></script>
    <script type="text/javascript">
        $(function() {
            $.ajax({
                url : "videoList",
                // 			data : {
                // 			},

                success : function(x) {
                    $('#result').html(x)
                },//success
                error : function() {
                    alert('error')
                }//error
            })//ajax

            $('#btnInsert').click(function() {

                var form = $('#video')[0].files[0];
                var formData = new FormData();

                formData.append('file', form);

                $.ajax({
                    type : "post",
                    url : "videoInsert",
                    enctype : 'multipart/form-data',
                    processData : false,
                    contentType : false,
                    cache : false,
                    data : formData,
                    success : function(x) {
                        alert('영상 추가 성공');
                    },//success
                    error : function() {
                        alert('실패')
                    }//error
                })//ajax
            })//b0

        })//$
    </script>
</head>
<body>
비디오 리스트


<div id="result"></div>
<br><br>
영상 추가<br>
<div class="col-sm-10">
    <input id="video" type="file" name="file">
</div>
<button id="btnInsert">영상 업로드</button>
</body>
</html>