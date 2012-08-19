<div>
    <h3>Latest news</h3>
    <g:each var="item" in="${news}">
        <p><b>${item.title}</b> - <i><cw:dateFromNow date="${item.dateCreated}" format="short" /></i><br />
            ${item.body}
        </p>
    </g:each>
</div>