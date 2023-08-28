<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
		<div class="menu">
		<div class="menu-logo" onclick="url('main')">로고</div>
			<div class="menu-item" onclick="url('multiBoard')">
				<i class="xi-library-books xi-2x"></i>게시판 관리
			</div>
			<div class="menu-item" onclick="url('post?cate=0')">
				<i class="xi-paper xi-2x"></i>게시글 관리
			</div>
			<div class="menu-item" onclick="url('member')">
				<i class="xi-users xi-2x"></i>회원 관리
			</div>
			<div class="menu-item" onclick="url('comment')">
				<i class="xi-comment xi-2x"></i>댓글 관리
			</div>
			<div class="menu-item" onclick="url('message')">
				<i class="xi-forum xi-2x"></i>메시지 관리
			</div>
			<div class="menu-item" onclick="url('mail')">
				<i class="xi-mail xi-2x"></i>메일 보내기
			</div>
			<div class="menu-item" onclick="url('notice')">
				<i class="xi-bell xi-2x"></i>공지사항
			</div>
			<div class="menu-item" onclick="url('logout')">
				<i class="xi-log-out xi-2x"></i>로그아웃
			</div>
		</div>
		<script type="text/javascript">
		function url(url) {
		location.href="./"+url;	
		}
		//괄호안의 url을 +uri로 던져줌
		</script>