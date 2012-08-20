<div>
    <g:if test="${matches.size() > 0}">
        <table class="table table-striped cursorpointer fixed-columns" id="matches">
            <thead>
            <tr>
                <th style="width: 7%">#</th>
                <th style="width: 13%">Date</th>
                <th style="width: 40%; text-align:center" colspan="2">Players</th>
                <th style="width: 40%">Description</th>
            </tr>
            </thead>
            <tbody>
            <g:each var="match" in="${matches}">
                <g:render template="matchRow" model="${[match: match]}" />
            </g:each>
            </tbody>
        </table>
    </g:if>
    <g:else>
        <div class="alert alert-info">No matches played in this ladder so far.</div>
    </g:else>
</div>

<div class="pagination">
    <cw:remotePaginate total="${matchesTotal}" update="matches_pane" action="ajaxFetchMatches" maxsteps="3" max="10"/>
</div>