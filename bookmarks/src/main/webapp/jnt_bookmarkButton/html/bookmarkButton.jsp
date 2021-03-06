<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<template:addResources type="javascript" resources="jquery.min.js"/>
<template:addResources type="javascript" resources="jquery.form.js"/>

<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<c:if test="${renderContext.loggedIn}" >
<form action="<c:url value='${url.baseLive}${renderContext.user.localPath}.add.do'/>" method="post" name="bookmark" id="bookmarkForm">
    <p>
        <jcr:node path="${renderContext.user.localPath}" var="user" />

        <input type="hidden" name="jcr:title" value=""/>
        <input type="hidden" name="jcrRedirectTo" value="<c:url value='${url.base}${renderContext.mainResource.node.path}'/>">
        <input type="hidden" name="jcrNodeType" value="jnt:bookmark">
        <input type="hidden" name="url" value="">
        <input class="button" id="bookmark"  type="submit" value="<fmt:message key="bookmark.add"/>"/>
        <script type="text/javascript">
            document.forms['bookmark'].elements['jcr:title'].value = document.title;
            document.forms['bookmark'].elements['url'].value = document.location;
            var options = {
                success: function() { $('#bookmarkList${user.identifier}').load('<c:url value="${url.baseLive}${user.path}.bookmarks.html.ajax"/>'); }
            }
            $(document).ready(function() {
                // bind 'myForm' and provide a simple callback function
                $('#bookmarkForm').ajaxForm(options);
            });
        </script>
    </p>
</form>
</c:if>