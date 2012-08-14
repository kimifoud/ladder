<html>
<head>
    <meta name='layout' content='main'/>
    <title><g:message code="springSecurity.login.title"/></title>
    <r:require modules="application"/>
</head>

<body>

<div class="page-header">
    <h1><g:message code="${params.activeLogin ? 'login.header.neat' : 'springSecurity.login.header'}"/></h1>
</div>

<div class="row-fluid">
    <div class="span12">
        <g:if test="${flash.message}">
            <div class="alert">${flash.message}</div>
        </g:if>
        <form id="loginForm" action="${resource(file: 'j_spring_security_check')}" class="form-horizontal"
              autocomplete="off" method="POST">
            <fieldset>
                <div class="control-group">
                    <label class="control-label" for="username">Username:</label>

                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-envelope"></i></span><g:textField id="username"
                                                                                                  name="j_username"
                                                                                                  value="${params.username}"/>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="password">Password:</label>

                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-lock"></i></span><g:passwordField name="j_password"
                                                                                                      id="password"/>
                        </div>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="remember_me">Remember me:</label>

                    <div class="controls">
                        <g:checkBox name="${rememberMeParameter}" id="remember_me" checked="${hasCookie ? 'true' : 'false'}" class="checkbox"/></div>
                </div>

            </fieldset>

            <div class="form-actions">
                <g:submitButton name="login" id="submit"
                                value="${message(code: "springSecurity.login.button")}"
                                class="btn btn-primary"/>
                <g:link controller="home" class="btn">Cancel</g:link>
                <g:link controller="register" class="btn btn-info">Register</g:link>
                <g:link controller="requestPassword" class="btn btn-info">Request password</g:link>
            </div>
        </form>
    </div>
</div>

<g:javascript>
    (function() {
        <g:if test="${params.username}">
    document.forms['loginForm'].elements['j_password'].focus();
</g:if>
    <g:else>
        document.forms['loginForm'].elements['j_username'].focus();
    </g:else>

    })();
</g:javascript>
</body>
</html>
