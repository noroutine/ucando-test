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

<script type="text/javascript">
    window.R || (window.R = {
        registerResources: function (r) {
            r.get = window.R.get;
            window.R = r;
            stopWatch(R.get('build.name') + ' ' + R.get('build.version'));
        },
        get: function(key, args) {
            var msg = R[key];
            if (msg) {
                if (args) {
                    for (var name in args) {
                        //noinspection JSUnfilteredForInLoop
                        msg = msg.replace('{' + name + '}', args[name]);
                    }
                }
                return msg;
            } else {
                return key;
            }
        }
    });
</script>

<%-- see ResourcesController --%>
<util:js value="/resources.json"/>


<%--
<!-- Google Analytics: change UA-XXXXX-X to be your site's ID. -->
<script>
    var _gaq=[['_setAccount','UA-XXXXX-X'],['_trackPageview']];
    (function(d,t){var g=d.createElement(t),s=d.getElementsByTagName(t)[0];
    g.src='//www.google-analytics.com/ga.js';
    s.parentNode.insertBefore(g,s)}(document,'script'));
</script>
--%>