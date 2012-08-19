<div>
    <h3>Latest matches</h3>
    <g:if test="${latestMatches?.size() > 0}">
        <table class="table table-striped cursorpointer">
            <tbody>
                <g:render template="/home/matchRow" collection="${latestMatches}" />
            </tbody>
        </table>
    </g:if>
</div>