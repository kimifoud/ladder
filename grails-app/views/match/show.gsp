<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Match</title>
</head>

<body>
<div class="page-header">
    <h1>Match</h1>
</div>

<div class="row">
<div class="span12">
    <g:if test="${match}">
        <p><b>Ladder:</b> ${match.ladder.title}</p>

        <p><b>Players:</b> ${match.player1} <cw:ratingBadge
                rating="${match.player1rating}"/> vs. ${match.player2} <cw:ratingBadge
                rating="${match.player2rating}"/></p>

        <p><b>Winner:</b> ${match.winner}</p>

        <p><b>Played:</b> <g:formatDate format="HH:mm, dd.MM.yyyy" date="${match.played}"/> (<cw:dateFromNow
                date="${match.played}"/>)</p>

        <p><b>Result:</b> <cw:matchResultString p1rc="${match.player1ratingChange}" p1name="${match.player1.name}"
                                                p2rc="${match.player2ratingChange}" p2name="${match.player2.name}"
                                                friendly="${match.friendly}"/></p>

        <p><b>Description:</b> ${match.description.encodeAsHTML()}</p>
        </div>
        <g:if test="${deletable}">
            <div class="span6">
                <div class="form-actions">

                    <p> <g:link action="myMatches"class="btn btn-primary">OK</g:link>
                        <g:link action="remove" class="btn btn-danger" id="${match.id}">Delete</g:link></p>

                    <p><span class="label label-info"
                             style="vertical-align: baseline;">Attention!</span> A match can be deleted for an hour after its creation.
                    </p>
                </div>
            </div>
        </g:if>
    </g:if>
</div>
</body>
</html>