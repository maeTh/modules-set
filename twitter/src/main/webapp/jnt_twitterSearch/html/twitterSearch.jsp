<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<template:addResources type="javascript" resources="jquery.min.js"/>
<template:addResources type="javascript" resources="jquery.juitter.js"/>
<template:addResources type="css" resources="twitter.css"/>
<jcr:nodeProperty node="${currentNode}" name="lang" var="lang"/>
<script type="text/javascript">
    $(function() {
        if(!$("#myJuitterContainer").length){
            var container = $('<div></div>').attr('id','myJuitterContainer');
            $('#juitterSearch').append(container);
        }

        var search = "${currentNode.properties.search.string}";
        $.Juitter.start({
            searchType:"searchWord", // needed, you can use "searchWord", "fromUser", "toUser"
            searchObject: search.replace(" ", ","), // needed, you can insert a username here or a word to be searched for, if you wish multiple search, separate the words by comma.
            <c:if test="${not empty lang}">lang:"${currentNode.properties.lang.string}",</c:if> // restricts the search by the given language
            live:"live-${currentNode.properties.timeUpdate.long}", // the number after "live-" indicates the time in seconds to wait before request the Twitter API for updates.
            placeHolder:"myJuitterContainer", // Set a place holder DIV which will receive the list of tweets example <div id="juitterContainer"></div>
            loadMSG: "Loading messages...", // Loading message, if you want to show an image, fill it with "image/gif" and go to the next variable to set which image you want to use on
            imgName: "loader.gif", // Loading image, to enable it, go to the loadMSG var above and change it to "image/gif"
            total: ${currentNode.properties.numberOfTweets.string}, // number of tweets to be show - max 100
            readMore: "Read it on Twitter", // read more message to be show after the tweet content
            nameUser:"image", // insert "image" to show avatar of "text" to show the name of the user that sent the tweet
            openExternalLinks:"newWindow", // here you can choose how to open link to external websites, "newWindow" or "sameWindow"
            filter:"sex->*BAD word*,porn->*BAD word*,fuck->*BAD word*,shit->*BAD word*"  // insert the words you want to hide from the tweets followed by what you want to show instead example: "sex->censured" or "porn->BLOCKED WORD" you can define as many as you want, if you don't want to replace the word, simply remove it, just add the words you want separated like this "porn,sex,fuck"... Be aware that the tweets will still be showed, only the bad words will be removed
        });
         $("#mySearch").submit(function() {
             var mySearch = $(".mySearch").val();
            $.Juitter.start({
                <c:if test="${not empty lang}">lang:"${currentNode.properties.lang.string}",</c:if>
                searchType:"searchWord",
                placeHolder:"myJuitterContainer",
                searchObject:mySearch.replace(" ", ","),
                live:"live-${currentNode.properties.timeUpdate.long}",
                filter:"sex->*BAD word*,porn->*BAD word*,fuck->*BAD word*,shit->*BAD word*"
            });
            return false;
        });

        $(".mySearch").blur(function() {
            if ($(this).val() == "") $(this).val("Type a word and press enter");
        });
        $(".mySearch").click(function() {
            if ($(this).val() == "Type a word and press enter") $(this).val("");
        });
    });
</script>

<div id="myContentSearch">
    <div id="juitterSearch">
        <form action="" method="post" id="mySearch">
            <p><fmt:message key="jnt_twitterSearch"/>: <input type="text" class="mySearch" value="<fmt:message key='jnt_twitterSearch.typeAWordAndPressEnter'/>"/></p>
        </form>
    </div>
</div>