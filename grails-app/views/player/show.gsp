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
<div id="charts">
</div>
<g:if test="${matchesTotal > 0}">
    <g:render template="/match/matches" model="${matches}" />
    <div class="pagination">
        <g:paginate maxsteps="3" total="${matchesTotal}" controller="player" action="show" id="${params.id}"/>
    </div>

</g:if>
<g:else>
    <div class="alert alert-info">${player?.name ?: 'UNKNOWN_PLAYER'} has not played any matches so far.</div>
</g:else>
<g:javascript>
    $(document).ready(function() {
        var width = $("#charts").width()
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