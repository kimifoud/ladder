<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application, bootstrap-tab"/>
    <title>Leaderboard</title>
    <g:javascript>
        $(document).ready(function () {
            $('#lb tr').click(function () {
                var href = $(this).find("a").attr("href");
                if (href) {
                    window.location = href;
                }
            });
        });
        $('#laddertabs a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        })
    </g:javascript>
</head>

<body>

<div class="page-header">
    <h1>${ladder.title}</h1>
</div>

<div>
    <ul id="laddertabs" class="nav nav-tabs">
        <li class="active">
            <a href="#leaderboard" data-toggle="tab">Leaderboard</a>
        </li>
        <li><a href="#matches" data-toggle="tab">Matches</a></li>
    </ul>
</div>

<div class="container">
    <div class="tab-content">
        <div class="tab-pane active" id="leaderboard">
            <g:if test="${players.size() > 0}">
                <table class="table table-striped" id="lb">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Player</th>
                        <th>Rating</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:set var="counter" value="${1}"/>
                    <g:each var="player" in="${players}">
                        <tr>
                            <td>${counter}</td>
                            <td><g:link controller="player" action="show" id="${player.id}">${player.fullName}</g:link></td>
                            <td>${player.eloRating}</td>
                        </tr>
                        <g:set var="counter" value="${counter + 1}"/>
                    </g:each>
                    </tbody>
                </table>
            </g:if>
            <g:else>
                <div class="alert alert-info">No matches played in this ladder so far.</div>
            </g:else>
        </div>

        <div class="tab-pane" id="matches">
            <g:if test="${matches.size() > 0}">
                <div>
                    <table class="table table-striped">
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
                <!-- TODO: pagination -->
            </g:if>
            <g:else>
                <div class="alert alert-info">No matches played in this ladder so far.</div>
            </g:else>
        </div>
    </div>
</div>
<g:javascript>
    $(function () {
        $('#laddertabs a:first').tab('show');
    })
</g:javascript>
</body>
</html>