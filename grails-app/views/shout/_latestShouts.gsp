<div class="scrollableShouts">
    <g:each in="${shouts}" var="shout">
        <div><p><b>(<g:formatDate date="${shout.dateCreated}" format="HH:mm" />) ${shout.shouter}:</b> ${shout.shout}</p></div>
    </g:each>
</div>