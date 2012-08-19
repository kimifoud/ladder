<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <gvisualization:apiImport/>
    <title>${player?.name ?: "Player"}</title>
</head>

<body>
<div class="page-header">
    <h1>${player?.name}</h1>
</div>
<g:if test="${matchesTotal > 0}">
    <div id="statsHolder">
        <g:render template="/player/stats" model="${[matches, matchesTotal, matchesOfficial, matchesFriendly, victories, winningPercent]}" />
    </div>
    <div id="charts">
    </div>
    <div id="matchHolder">
        <g:render template="/player/matches" model="${matches}" />
    </div>

</g:if>
<g:else>
    <g:if test="${player}"><div class="alert alert-info">${player?.name ?: 'UNKNOWN_PLAYER'} has not played any matches so far.</div></g:if>
</g:else>
<g:javascript>
    $(document).ready(function() {
        var width = $("#charts").width();
        $.ajax({
            url:"${createLink(controller: 'chart', action: 'renderPlayerChart')}",
            data: {id: ${params.id}, width: width},
            dataType: 'html',
            success:function (data) {
                $("#charts").html(data);
            }
        });
    })
</g:javascript>
</body>
</html>