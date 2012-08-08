<html>
<head>
    <meta name='layout' content='main'/>
    <r:require modules="application"/>
    <title>Home</title>
    <g:javascript>
        var cntShouts = 0;
        var cntMatches = 0;
        $(document).ready(function () {
            setTimeout(function () {
                ajaxFetchShouts()
            }, 5000);
            setTimeout(function () {
            	ajaxFetchLatestMatches()
            }, 300000);
        });

        function ajaxFetchShouts() {
            $.ajax({
                url: '${createLink(controller: 'shout', action: 'ajaxFetchLatest')}',
                dataType: 'html',
                success:function (data) {
                    $('#shouts').html(data);
                    if (cntShouts < 60) { // poll for 60*5 seconds = 5 minutes
                        cntShouts++;
                        setTimeout(function () {
                            ajaxFetchShouts()
                        }, 5000);
                    } else if (cntShouts < 83) { // poll for 23*300 seconds = 115 minutes
                        cntShouts++;
                        setTimeout(function () {
                            ajaxFetchShouts()
                        }, 300000);
                    }
                }
            });
        }
        
        function ajaxFetchLatestMatches() {
            $.ajax({
                url: '${createLink(controller: 'match', action: 'ajaxFetchLatestMatches')}',
                dataType: 'html',
                success:function (data) {
                    $('#latestMatches').html(data);
                    if (cntMatches < 24) { // poll for 24*5 minutes = 120 minutes
                        cntMatches++;
                        setTimeout(function () {
                            ajaxFetchLatestMatches()
                        }, 300000);
                    }
                }
            });
        }
    </g:javascript>
</head>

<body>
<div class="hero-unit">
    <h1>Ladder application</h1>
    <p>A <a href="http://www.grails.org">Grails</a> application for recording played pool matches to find out how good the M$ guys really are.</p>
    <ul>
    <li><b>Rules</b>: <a href="http://www.sbil.fi/published_files/dbf1037.pdf">http://www.sbil.fi/published_files/dbf1037.pdf</a> (3. Kasipallo)</li>
    <li><b>Beta</b>: During the beta stage all saved data may (or may not) be deleted at anytime without further notice. The recorded matches may (or may not) be wiped once the application goes live. Please report any bugs you may bump into :></li>
    <li><b>Bug reports</b>: <a href="https://github.com/kimifoud/ladder/issues">https://github.com/kimifoud/ladder/issues</a></li>
    <li><b>English</b>, because the app is <a href="https://github.com/kimifoud/ladder">open source</a> (and i18n is yet to come).</li>
    </ul>
</div>

<div class="row">
    <div class="span6"><div id="latestMatches" class="well"><g:render template="latestMatches" model="latestMatches"/></div></div>

    <div class="span6">
        <div class="well" id="shoutbox">
            <g:formRemote id="shoutForm" name="shoutForm" url="[controller: 'shout', action: 'ajaxSave']" method="POST" update="shouts" before="disableShout()" after="enableShout(); cntShouts = 0" onSuccess="clearForm('#shoutForm')" style="margin-bottom: 5px">
                <input class="span5" name="shout_" id="shout_" size="16" type="text">
                <input type="submit" value="Shout!" id="shoutBtn" />
                <g:hiddenField name="shout" id="shout" />
            </g:formRemote>
            <div id="shouts">
                <g:render template="/shout/latestShouts" model="shouts" />
            </div>
        </div>
    </div>
</div>
</body>
</html>