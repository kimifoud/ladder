<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ladder &raquo; <g:layoutTitle default="Welcome"/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <r:require modules="application, bootstrap"/>

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
            <g:link controller="public" action="index" class="brand">The Ladder</g:link>
            <sec:ifLoggedIn>
                <ul class="nav pull-right">
                    <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-user icon-white"></i> <sec:username/>
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><g:link controller="player"><i class="icon-user"></i> Profile</g:link></li>
                        <li class="divider" />
                        <li><g:link controller="logout"><i class="icon-off"></i> Sign Out</g:link></li>
                    </ul>
                    </li>
                </ul>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:link controller="login" params="[activeLogin: 'true']" class="btn btn-info pull-right">Login</g:link>
            </sec:ifNotLoggedIn>
            <div class="nav-collapse">
                <ul class="nav">
                    <li ${controllerName in ['public', 'home', 'login'] ? 'class="active"':''}><g:link controller="public">Home</g:link></li>
                    <li ${controllerName in ['about'] ? 'class="active"':''}><g:link uri="#">About</g:link></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container" style="padding-top: 60px">
    <g:layoutBody/>
    <hr />
    <footer>
    <p>&copy; 2012 Kimi</p>
    </footer>
</div>
<r:layoutResources/>
</body>
</html>