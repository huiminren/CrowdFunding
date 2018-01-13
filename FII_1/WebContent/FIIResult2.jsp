<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="bean.DataBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="FiiR2.css" rel="stylesheet" type="text/css" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />
<meta name="format-detection" content="telephone=no" />
<meta name="apple-touch-fullscreen" content="YES" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<title>众筹评分结果app版</title>
<script type="text/javascript">
	
</script>
</head>
<body background="长背景.png" style="background-repeat: no-repeat;">

	<%
		DataBean dbean = (DataBean) request.getSession(true).getAttribute(
				"dbean");
	%>

	<p class="TopBar">RESULT</p>
	<div class="Choice">

		<table id="tblSample">

			<%
				for (int i = 1; i <= dbean.getAmount(); i++) {
			%>
			<tr>
				<td class="head">Rank:<%= i %></td>
				<td class="head">Score:<%= dbean.getScore(i) %></td>
				<td class="head">Level:<%= dbean.getRank(i) %></td>
			</tr>
			<tr class="demo">
				<td>
					<p class="top_text" style="text-align: center;">
						<a href="<%= dbean.getUrl(i) %>"
							class="content3"><%= dbean.getUrl(i) %></a>
					</p>
				</td>
			</tr>
			<%
				}
			%>
		</table>

	</div>

</body>
</html>