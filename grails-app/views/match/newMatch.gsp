<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application, jquery-ui"/>
    <title>New match</title>
</head>

<body>
<div class="page-header">
    <h1>New match <small>Insert the result of a played match</small></h1>
</div>

<div class="row">
    <div class="span6">
        <g:if test="${flash.message}">
            <div class="alert alert-error">
                <a class="close" data-dismiss="alert" href="#">×</a>
                <h4 class="alert-heading">Error!</h4>
                ${flash.message}
            </div>
        </g:if>
        <g:form controller="match" action="save">
            <g:hiddenField name="player2.id" />
            <f:with bean="match">
                <f:field property="player2" label="${message(code: 'match.opponent.label')}">
                    <g:textField name="player2auto" style="width: 300px;" placeholder="Start typing...  " />
                </f:field>
                <f:field property="winner">
                    <div id="winner">
                        <g:select name="winner.id" from=""
                              noSelection="${['null': 'Select opponent first...']}"/>
                    </div>
                </f:field>
                <f:field property="friendly" class="span3"/>
                <f:field property="description" class="span3"/>
            </f:with>
            <div class="form-actions">
                <g:submitButton name="submit" value="Submit" class="btn btn-primary"/>
                <cw:cancelButton/>
            </div>
        </g:form>
    </div>
</div>
</body>

<g:javascript>
    $(document).ready(function () {
        $("#player2auto").autocomplete({
            source:function (request, response) {
                $.ajax({
                    url:"${createLink(controller: 'match', action: 'ajaxFindOpponents')}", // remote datasource
                    data: {term: request.term},
                    success:function (data) {
                        response(data); // set the response
                    }
                });
            },
            delay: 200,
            minLength:1, // triggered only after minimum 2 characters have been entered.
            select:function (event, ui) { // event handler when user selects an opponent from the list.
                 var playerId = ui.item.id;
                 $("#player2\\.id").val(playerId); // update the hidden field.
                 $.ajax({
                    url:"${createLink(controller: 'match', action: 'ajaxGetWinnersSelect')}",
                    data: {id: playerId},
                    dataType: 'html',
                    success:function (data) {
                        $("#winner").html(data);
                    }
                 });
            }
        });
    });
</g:javascript>
</html>