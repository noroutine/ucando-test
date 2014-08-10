<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="tb" uri="/WEB-INF/taglib/taglib.tld" %>

<spring:message code="build.profile" var="build_profile"/>

<c:choose>
    <c:when test="${build_profile eq 'production'}">
        <util:css href="/resources/vendor/css/all.css" />
        <util:css href="/resources/css/all.css" />
    </c:when>
    <c:otherwise>

        <%-- HTML5 boilerplate, see: http://html5boilerplate.com/--%>
        <%--<util:css href="/resources/vendor/css/normalize-2.1.3.css" />--%>
        <%--<util:css href="/resources/vendor/css/boilerplate-4.2.0.css" />--%>

        <%-- Twitter Bootstrap, see: http://getbootstrap.com/ --%>
        <util:css href="/resources/vendor/css/bootstrap-3.2.0.css" />
        <util:css href="/resources/vendor/css/backgrid-0.3.5.css" />

        <util:css href="/resources/css/main.css" />
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${build_profile eq 'production'}">
        <util:js value="/resources/vendor/js/all.js" minify="false"/>
        <util:js value="/resources/js/all.js" minify="false"/>
    </c:when>
    <c:otherwise>
        <%-- should be included in the same order as in pom.xml in yui compression plugin --%>

        <util:js value="/resources/vendor/js/jquery-2.0.3.js"/>
        <util:js value="/resources/vendor/js/jquery.tinypubsub.js"/>
        <util:js value="/resources/vendor/js/jstorage-0.3.1.js"/>

        <%-- Backbone.js --%>
        <util:js value="/resources/vendor/js/lodash-1.3.1.js" />
        <util:js value="/resources/vendor/js/backbone-1.0.0.js" />
        <util:js value="/resources/vendor/js/backgrid-0.3.5.js" />

        <%-- Dust.js --%>
        <util:js value="/resources/vendor/js/dust-full-2.0.2.js" />
        <util:js value="/resources/vendor/js/dust-helpers-1.1.1.js" />

        <%-- Twitter Bootstrap --%>
        <util:js value="/resources/vendor/js/bootstrap-3.2.0.js" />

        <util:js value="/resources/js/compatibility.js"/>
        <util:js value="/resources/js/common.js"/>
    </c:otherwise>
</c:choose>
