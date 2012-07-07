<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>New match</title>
</head>

<body>
<div class="page-header">
    <h1>New match <small>Insert the result of a played match</small></h1>
</div>
<g:form controller="match" action="save">
    <f:with bean="match">
        <f:field property="player2" label="${message(code: 'match.opponent.label')}">
            <g:select
                    optionKey="id" optionValue="name" name="player2" from="${opponents}"
                    noSelection="${['null': 'Select One...']}"
                    onchange="${remoteFunction(
                            controller: 'match',
                            action: 'ajaxGetWinnersSelect',
                            params: '\'id=\' + escape(this.value)',
                            update: [success:'winner'])}"></g:select>
        </f:field>
        <f:field property="winner">
            <g:select name="winner" id="winner" from="['Select opponent first...']"/>
        </f:field>
        <f:field property="played">
            <g:datePicker name="played" precision="minute" relativeYears="[0..0]"/>
        </f:field>
        <f:field property="friendly" class="span3"/>
        <f:field property="description" class="span3"/>
    </f:with>
    <g:submitButton name="submit" value="Submit"/>
</g:form>
</body>

</html>