<div class="scrollableShouts">
    <g:each in="${shouts}" var="shout">
            <p><b>(<g:formatDate date="${shout.dateCreated}" format="HH:mm" />) ${shout.shouter.encodeAsHTML()}:</b> ${shout.shout.encodeAsHTML()}</p>
    </g:each>
</div>