<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Request password</title>
</head>

<body>
<div class="page-header">
    <h1>Forgot your password?</h1>
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
        <g:form action="requestPassword" class="form-horizontal">
            <fieldset>
                <div class="control-group">
                    <label class="control-label" for="email">E-mail <span class="required-indicator">*</span></label>

                    <div class="controls">
                        <div class="input-prepend">
                            <span class="add-on"><i class="icon-envelope"></i></span><g:textField name="email" />
                        </div>

                        <p>Enter your e-mail address to start the process of resetting your password.</p></div>
                </div>
            </fieldset>
        </g:form>
    </div>
</div>

</body>
</html>