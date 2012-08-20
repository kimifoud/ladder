<div>
    <table class="table table-striped cursorpointer" id="matches">
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
            <g:render template="matchRow" model="${[playerId: params.id as Long, match: match]}" />
        </g:each>
        </tbody>
    </table>
</div>

<div class="pagination">
    <cw:remotePaginate total="${matchesTotal}" update="matchHolder" action="ajaxFetchMatches" maxsteps="3" max="5" id="${params.id}"/>
</div>