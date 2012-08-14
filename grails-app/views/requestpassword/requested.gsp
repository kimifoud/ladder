<!doctype html>
<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Password requested</title>
</head>

<body>
<div class="page-header">
    <h1>Hurray!</h1>
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
        A link for resetting your password was sent to the provided e-mail address. Please check your e-mail and follow the instructions.
    </div>
</div>

</body>

</html>