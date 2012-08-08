<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Player</title>
</head>

<body>
<div class="page-header">
    <h1>${player?.name}</h1>
</div>
<g:if test="${matchesTotal > 0}">
    <g:render template="/match/matches" model="${matches}" />
    <div class="pagination">
        <g:paginate maxSteps="2" max="5" total="${matchesTotal}" controller="player" action="show" id="${params.id}"/>
    </div>

</g:if>
<g:else>
    <div class="alert alert-info">${player?.name ?: 'Sbörgels:D'} has not played any matches so far.</div>
</g:else>
</body>
</html>