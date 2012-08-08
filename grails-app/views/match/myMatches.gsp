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

<div id="alert-area"></div>

<g:if test="${flash.message}">
    <g:javascript>newAlert('success', '${flash.message}');</g:javascript>
</g:if>

<div class="navbar">
    <g:link class="btn" action="newMatch"><i class="icon-plus-sign"></i> <g:message code="match.new.label"
                                                                                    default="New match"/></g:link>
</div>
<g:if test="${matchesTotal > 0}">
    <g:render template="matches" model="${matches}" />
    <div class="pagination">
    <g:paginate maxsteps="3" max="10" total="${matchesTotal}" controller="match" action="myMatches"/>
    </div>

</g:if>
<g:else>
    <div class="alert alert-info">You have not played any matches in this ladder so far. Why don't you challenge a friend and get started?</div>
</g:else>
</body>
</html>