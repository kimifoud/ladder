<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>My matches</title>
</head>

<body>
<div class="page-header">
    <h1>My matches</h1>
</div>
<g:link class="btn" action="newMatch"><g:message code="match.new.label" default="New match"/></g:link>
<ul>
    <g:each var="match" in="${matches}">
        <li>${match.player1}(${match.player1rating}) vs. ${match.player2}(${match.player2rating}) - winner: ${match.winner} - <i>${match.description}</i>
        </li>
    </g:each>
</ul>
</body>
</html>