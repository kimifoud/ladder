<tr>
    <td><g:link controller="match" action="show" id="${it.id}"><cw:dateFromNow
            date="${it.played}" format="short"/></g:link></td>

    <g:if test="${it.winner.id.equals(it.player1.id)}">
        <td style="text-align:right"><g:link controller="player" action="show"
                                             id="${it.player1.id}">${it.player1}</g:link> <cw:ratingChangeBadge
                rating="${it.player1rating}" ratingChange="${it.player1ratingChange}"/></td>
        <td><cw:ratingChangeBadge rating="${it.player2rating}"
                                  ratingChange="${it.player2ratingChange}"/> <g:link controller="player"
                                                                                     action="show"
                                                                                     id="${it.player2.id}">${it.player2}</g:link></td>
    </g:if>
    <g:else>
        <td style="text-align:right"><g:link controller="player" action="show"
                                             id="${it.player2.id}">${it.player2}</g:link> <cw:ratingChangeBadge
                rating="${it.player2rating}" ratingChange="${it.player2ratingChange}"/></td>
        <td><cw:ratingChangeBadge rating="${it.player1rating}"
                                  ratingChange="${it.player1ratingChange}"/> <g:link controller="player"
                                                                                     action="show"
                                                                                     id="${it.player1.id}">${it.player1}</g:link></td>

    </g:else>
</tr>