<!doctype html>
<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Register</title>
</head>

<body>
<div class="page-header">
    <h1><g:message code="register.label"/></h1>
</div>
<g:if test="${flash.message}">
    <div class="alert alert-error"><p>${flash.message}</p>
        <g:hasErrors>
            <g:renderErrors bean="${user}" as="list"/>
        </g:hasErrors></div>
</g:if>
<div class="errors">
</div>

<div class="row">
    <div class="span6">
        <g:form action="save" class="form-horizontal">
            <fieldset>
                <div class="control-group">
                    <label class="control-label" for="username">E-mail <span class="required-indicator">*</span></label>

                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-envelope"></i></span><g:textField name="username"
                                                                                                  value="${params.username}"/>
                        </div>

                        <p>Your e-mail address acts as your username.</p></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="password">Password <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <g:passwordField name="password"/></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="passwordRepeat">Repeat password <span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <g:passwordField name="passwordRepeat"/></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="firstName">First name <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <g:textField name="firstName" value="${params.firstName}"/></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="lastName">Last name <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <g:textField name="lastName" value="${params.lastName}"/></div>
                </div>
            </fieldset>

            <div class="form-actions">
                <g:submitButton name="register"
                                value="${message(code: 'default.button.register.label', default: 'Register')}"
                                class="btn btn-primary"/>
                <g:link controller="home" class="btn"><g:message code="default.button.cancel.label"/></g:link>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>