<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ladder &raquo; <g:layoutTitle default="Welcome"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <r:require modules="application, bootstrap"/>
    <g:javascript>
        $(function () {
            // Setup drop down menu
            $('.dropdown-toggle').dropdown();

            // Fix input element click problem
            $('.dropdown input, .dropdown label').click(function (e) {
                e.stopPropagation();
            });

        });
        var cntResPolls = 0;</g:javascript>
    <style type="text/css">
    body {
        padding-top: 60px;
        padding-bottom: 40px;
    }
    </style>
    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <g:link controller="public" action="index" class="brand">The Ladder<sup> <g:meta name="app.version"/></sup></g:link>
            <div class="nav-collapse">
                <ul class="nav">
                    <li ${controllerName in ['public', 'home', 'login'] ? 'class="active"' : ''}><g:link
                            controller="public"><i class="icon icon-home icon-white"></i> Home</g:link></li>
                    <sec:ifLoggedIn>
                        <li ${controllerName in ['match'] && actionName == 'newMatch' ? 'class="active"' : ''}><g:link
                                controller="match" action="newMatch"><i class="icon icon-play icon-white"></i> New match</g:link></li>
                        <li><g:link controller="match" action="myMatches"><i class="icon icon-th-list icon-white"></i> My matches</g:link></li>
                        <li ${controllerName in ['ladder'] ? 'class="active"' : ''}><g:link
                                controller="ladder" action="leaderboard"><i class="icon icon-align-justify icon-white"></i> Ladder</g:link></li>
                    </sec:ifLoggedIn>
                    <li ${controllerName in ['about'] ? 'class="active"' : ''}><g:link uri="#"><i class="icon icon-flag icon-white"></i> About</g:link></li>
                </ul>
                <ul class="nav pull-right">
                    <sec:ifLoggedIn>
                        %{-- TODO logic to update reservation status --}%
                        <li id="reservationStatus"></li>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                <i class="icon-user icon-white"></i> <sec:username/>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><g:link controller="user" action="profile"><i class="icon-cog"></i> Profile</g:link></li>
                                <li class="divider"/>
                                <li><g:link controller="logout"><i class="icon-off"></i> Sign Out</g:link></li>
                            </ul>
                        </li>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <li><g:link controller="register">Sign Up</g:link></li>
                        <li class="divider-vertical"></li>
                        <li class="dropdown">
                            <a class="dropdown-toggle" href="#" data-toggle="dropdown">Sign In <span
                                    class="caret"></span></a>

                            <div class="dropdown-menu" style="padding: 15px; padding-bottom: 0px;">
                                <form action="${resource(file: 'j_spring_security_check')}" method="POST">
                                    <input id="username" style="margin-bottom: 15px;" type="text" name="j_username"
                                           size="30" placeholder="Username"/>
                                    <input id="password" style="margin-bottom: 15px;" type="password"
                                           name="j_password" size="30" placeholder="Password"/>
                                    <label for='remember_me' class='checkbox'>
                                    <input type='checkbox' class='checkbox' name='_spring_security_remember_me' id='remember_me' checked='true'/>
                                    <g:message code="springSecurity.login.remember.me.label"/></label>
                                    <g:link controller="requestPassword">Forgot your password?</g:link>
                                    <g:submitButton class="btn btn-primary" name="login"
                                                    style="clear: left; width: 100%; height: 32px; font-size: 13px;"
                                                    value="Sign In"/>
                                </form>
                            </div>
                        </li>
                    </sec:ifNotLoggedIn>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container">
    %{--<div id="spinner" class="spinner pull-left" style="display:none;">--}%
        %{--<img src="${resource(dir:'images',file:'spinner.gif')}" alt="Spinner" />--}%
    %{--</div>--}%
    <g:layoutBody/>
    <hr/>
    <footer>
        <p>&copy; 2012 Ladder</p>
    </footer>
</div>
<r:layoutResources/>
</body>
</html>