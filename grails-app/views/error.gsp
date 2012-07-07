<!doctype html>
<html>
<head>
    <title>Grails Runtime Exception</title>
    <meta name="layout" content="main">
    <r:require module="error"/>
</head>

<body>
<div class="alert alert-error">
    <h2>Oh snap, an error occurred! :'(</h2>
</div>
<g:renderException exception="${exception}"/>
</body>
</html>