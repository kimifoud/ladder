<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>New match</title>
</head>

<body>
<div class="page-header">
    <h1>New match <small>Insert the result of a played match</small></h1>
</div>
<g:form controller="match" action="newMatch">
    <f:all bean="match"/>
    <g:submitButton name="submit" value="Submit" />
</g:form>
</body>
</html>