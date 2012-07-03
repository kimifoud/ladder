<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>My matches</title>
    <g:javascript>
        $(document).ready(function() {
            $('#matches tr').click(function() {
                var href = $(this).find("a").attr("href");
                if(href) {
                    window.location = href;
                }
            });
        });
    </g:javascript>
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
        <table class="table table-striped" id="matches">
            <thead>
            <tr>
                <th>#</th>
                <th>Date</th>
                <th colspan="2" style="text-align:center">Players</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <g:each var="match" in="${matches}">
                <tr>
                    <td>${match.id}</td>
                    <td><g:link controller="match" action="show" id="${match.id}"><g:formatDate format="HH:mm dd.MM.yyyy" date="${match.played}"/></g:link></td>
                    <td style="text-align:right"><g:link controller="player" action="show" id="${match.player1.id}">${match.player1}</g:link> <cw:ratingChangeBadge rating="${match.player1rating}" ratingChange="${match.player1ratingChange}" /></td>
                    <td><cw:ratingChangeBadge rating="${match.player2rating}" ratingChange="${match.player2ratingChange}" /> <g:link controller="player" action="show" id="${match.player2.id}">${match.player2}</g:link></td>
                    <td><i><cw:cutString str="${match.description}" l="70"/></i></td>
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
    <div class="alert alert-info">You have not played any matches in this ladder so far. Why don't you challenge a friend and get started?</div>
</g:else>
</body>
</html>