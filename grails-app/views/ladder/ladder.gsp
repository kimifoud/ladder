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
            $('#matches tr').click(function() {
                var href = $(this).find("a").attr("href");
                if(href) {
                    window.location = href;
                }
            });
            $('#laddertabs a').click(function (e) {
                e.preventDefault();
                $(this).tab('show');
            })
        });
    </g:javascript>
</head>

<body>

<div class="page-header">
    <h1>${ladder.title}</h1>
</div>

<div>
    <ul id="laddertabs" class="nav nav-tabs">
        <li class="active">
            <a href="#leaderboard_pane" data-toggle="tab">Leaderboard</a>
        </li>
        <li><a href="#matches_pane" data-toggle="tab">Matches</a></li>
    </ul>
</div>

<div class="container">
    <div class="tab-content">
        <div class="tab-pane fade in active" id="leaderboard_pane">
            <g:if test="${players.size() > 0}">
                <table class="table table-striped cursorpointer" id="lb">
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
                            <td><g:link controller="player" action="show" id="${player.id}">${player.name}</g:link></td>
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

        <div class="tab-pane fade" id="matches_pane">
            <g:render template="laddermatches" />
        </div>
    </div>
</div>
<g:javascript>
    // Javascript to enable link to tab
    var hash = document.location.hash;
    var prefix = "tab_";
    if (hash) {
        $('.nav-tabs a[href='+hash.replace(prefix,"")+']').tab('show');
    }

    // Change hash for page-reload
    $('.nav-tabs a').on('shown', function (e) {
        window.location.hash = e.target.hash.replace("#", "#" + prefix);
    });
</g:javascript>
</body>
</html>