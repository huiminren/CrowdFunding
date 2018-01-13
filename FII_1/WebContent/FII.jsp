<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="FII.css" rel="stylesheet" type="text/css"/>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />
<meta name="format-detection" content="telephone=no"/>
<meta name="apple-touch-fullscreen" content="YES" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>众筹评分</title>
<script type="text/javascript">
function showtext() {
	
	var tbl = document.getElementById('tblSample');
	var lastRow = tbl.rows.length;
	// if there's no header row in the table, then iteration = lastRow + 1

	var iteration = lastRow+1;
	document.getElementById('hidetext').value=iteration;
	var row = tbl.insertRow(lastRow);
	// left cell

	if(iteration<=20)
	{
		
	// right cell
	var cellRight = row.insertCell(0);
	
	var el = document.createElement('input');
	el.type = 'text';
	el.name = 'uName' + iteration;
	el.className = 'top_text' ;

	el.size = 40;
	el.onkeypress = keyPressTest;

	cellRight.appendChild(el);

	}
}
function keyPressTest(e, obj)
{
var validateChkb = document.getElementById('chkValidateOnKeyPress');
if (validateChkb.checked) {
var displayObj = document.getElementById('spanOutput');
var key;
if(window.event) {
key = window.event.keyCode;
}
else if(e.which) {
key = e.which;
}

var objId;
if (obj != null) {
objId = obj.id;
} else {
objId = this.id;
}
displayObj.innerHTML = objId + ' : ' + String.fromCharCode(key);
}
}
</script>
</head>
<body background="长背景.png" style="background-repeat:no-repeat;">

<p class="TopBar">WHICH PROJECT SHOULD I CHOOSE?</p>
<div class="Choice">
<p class="clue">Add Your Project's Link</p>
<form method="get" action="ProcessServlet" enctype="multipart/form-data">
<div class="demo">
<table id="tblSample">
        	<tr><td>
        	
        	<input class="top_text"  type="text" name="uName1" value="" style="text-align:center;">
        	
        	</td></tr>
        	</table>
        	</div>
        	<br /> <input id="hidetext" name="choice_amount" value="1" type="hidden" />
        	
			<input class="register_btn" type="submit" value="GET SCORE" >
			
			<input class="back_btn" type="button" value="ADD PROJECT" onclick="showtext()">
		
		</form>
</div>

</body>
</html>