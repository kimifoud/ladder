<tr>
    <td>${match.id}</td>
    <td><g:link controller="match" action="show" id="${match.id}"><g:formatDate
            format="HH:mm dd.MM.yyyy" date="${match.played}"/></g:link></td>
    <g:if test="${match.winner.id.equals(match.player1.id)}">
        <td style="text-align:right"><g:link controller="player" action="show"
                                             id="${match.player1.id}">${match.player1}</g:link> <cw:ratingChangeBadge
                rating="${match.player1rating}" ratingChange="${match.player1ratingChange}"/></td>
        <td><cw:ratingChangeBadge rating="${match.player2rating}"
                                  ratingChange="${match.player2ratingChange}"/> <g:link controller="player"
                                                                                        action="show"
                                                                                        id="${match.player2.id}">${match.player2}</g:link></td>
    </g:if>
    <g:else>
        <td style="text-align:right"><g:link controller="player" action="show"
                                             id="${match.player2.id}">${match.player2}</g:link> <cw:ratingChangeBadge
                rating="${match.player2rating}" ratingChange="${match.player2ratingChange}"/></td>
        <td><cw:ratingChangeBadge rating="${match.player1rating}"
                                  ratingChange="${match.player1ratingChange}"/> <g:link controller="player"
                                                                                        action="show"
                                                                                        id="${match.player1.id}">${match.player1}</g:link></td>
    </g:else>
    <td><i><cw:cutString str="${match.description.encodeAsHTML()}" l="70"/></i></td>
</tr>