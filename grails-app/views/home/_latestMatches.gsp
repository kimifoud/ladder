<div>
    <h3>Latest matches</h3>
    <g:if test="${latestMatches?.size() > 0}">
        <table class="table table-striped cursorpointer">
            <thead>
            <tr>
                <th>Played</th>
                <th style="text-align:center" colspan="2">Players</th>
            </tr>
            </thead>
            <tbody>
            <g:each var="match" in="${latestMatches}">
                <tr>
                    <td><g:link controller="match" action="show" id="${match.id}"><cw:dateFromNow
                            date="${match.played}" format="short" /></g:link></td>
                    <td style="text-align:right"><g:link controller="player" action="show"
                                                         id="${match.player1.id}">${match.player1}</g:link> <cw:ratingChangeBadge
                            rating="${match.player1rating}" ratingChange="${match.player1ratingChange}"/></td>
                    <td><cw:ratingChangeBadge rating="${match.player2rating}"
                                              ratingChange="${match.player2ratingChange}"/> <g:link controller="player"
                                                                                                    action="show"
                                                                                                    id="${match.player2.id}">${match.player2}</g:link></td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </g:if>
</div>