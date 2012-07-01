<%@ page import="org.carniware.ladder.Ladder" %>



<div class="fieldcontain ${hasErrors(bean: ladderInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="ladder.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${ladderInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: ladderInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="ladder.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="description" required="" value="${ladderInstance?.description}"/>
</div>

