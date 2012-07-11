<%@ page defaultCodec="html" %>
<div class="control-group">
    <label class="checkbox" for="${property}${index != null ? index : ""}">${label}
        <%= index == null ? widget : widget . replace('id="' + property + '"', 'id="' + property + index + '"') %>
        <g:if test="${invalid}"><span class="help-inline">${errors.join('<br>')}</span></g:if>

    </label>
</div>