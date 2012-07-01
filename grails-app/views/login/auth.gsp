<html>
<head>
	<meta name='layout' content='main'/>
	<title><g:message code="springSecurity.login.title"/></title>
    <r:require modules="application, login"/>
</head>

<body>
<div id='login'>
	<div class='inner'>
		<div class='fheader'><g:message code="${params.activeLogin ? 'login.header.neat' : 'springSecurity.login.header'}"/></div>

		<g:if test='${flash.message}'>
			<div class='login_message'>${flash.message}</div>
		</g:if>

		<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
			<p>
				<label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
				<input type='text' class='text_' name='j_username' id='username' value="${params.username}"/>
			</p>

			<p>
				<label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
				<input type='password' class='text_' name='j_password' id='password'/>
			</p>

			<p id="remember_me_holder">
				<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
				<label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
			</p>

			<p>
				<input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}' class="btn btn-primary"/>
			</p>

            <p>
                ... or <g:link controller="register">register</g:link>?
            </p>
		</form>
	</div>
</div>
<script type='text/javascript'>
	<!--
	(function() {
        <g:if test="${params.username}">
		document.forms['loginForm'].elements['j_password'].focus();
        </g:if>
        <g:else>
        document.forms['loginForm'].elements['j_username'].focus();
        </g:else>

	})();
	// -->
</script>
</body>
</html>
