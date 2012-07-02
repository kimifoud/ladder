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

<div class="navbar">
    <g:link class="btn" action="newMatch"><i class="icon-plus-sign"></i> <g:message code="match.new.label"
                                                                                    default="New match"/></g:link>
</div>
<g:if test="${matchesTotal > 0}">
    <div>
        <table class="table table-striped ">
            <thead>
            <tr>
                <th>#</th>
                <th>Date</th>
                <th>Players</th>
                <th>Winner</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <g:each var="match" in="${matches}">
                <tr>
                    <td>${match.id}</td>
                    <td><g:formatDate format="HH:mm dd.MM.yyyy" date="${match.played}"/></td>
                    <td>${match.player1}(${match.player1rating} <cw:ratingChangeBadge ratingChange="${match.player1ratingChange}" />) vs. ${match.player2}(${match.player2rating} <cw:ratingChangeBadge ratingChange="${match.player2ratingChange}" />)</td>
                    <td>${match.winner} </td>
                    <td><i>${match.description}</i></td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="pagination">
    <g:paginate maxSteps="2" max="5" total="${matchesTotal}" controller="match" action="myMatches"/>
    </div>

</g:if>
<g:else>
    <div class="alert alert-info">You have no matches played in this ladder so far. Why don't you challenge a friend and get started?</div>
</g:else>
</body>
</html>