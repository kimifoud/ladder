<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>User profile</title>
</head>

<body>
<div class="page-header">
    <h1>User profile</h1>
</div>

<g:if test="${flash.message}">
    <div class="alert"><p>${flash.message}</p>
        <g:hasErrors>
            <div class="alert alert-error">
                <g:renderErrors bean="${user}" as="list"/>
            </div></g:hasErrors></div>
</g:if>
<div class="errors">
</div>

<div class="row">
    <div class="span6">
        <g:form action="profile" class="form-horizontal">
            <fieldset>
                <div class="control-group">
                    <label class="control-label" for="username">E-mail <span class="required-indicator">*</span></label>

                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-envelope"></i></span><g:textField name="username"
                                                                                                  value="${user?.username}"/>
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
                        <g:textField name="firstName" value="${user?.firstName}"/></div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="lastName">Last name <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <g:textField name="lastName" value="${user?.lastName}"/></div>
                </div>
            </fieldset>

            <div class="form-actions">
                <g:submitButton name="save"
                                value="${message(code: 'default.button.update.label', default: 'Update')}"
                                class="btn btn-primary"/>
                <g:link controller="home" class="btn"><g:message code="default.button.cancel.label"/></g:link>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>