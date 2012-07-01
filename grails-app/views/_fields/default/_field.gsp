<%@ page defaultCodec="html" %>
<div class="control-group ${invalid ? 'error' : ''}">
    <label class="control-label" for="${property}${index != null ? index : ""}">${label}</label>
    <div class="controls">
        <%= index == null ? widget : widget . replace('id="' + property + '"', 'id="' + property + index + '"') %>
        <g:if test="${invalid}"><span class="help-inline">${errors.join('<br>')}</span></g:if>
    </div>
</div>