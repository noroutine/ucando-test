<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="tb" uri="/WEB-INF/taglib/taglib.tld" %>

<util:js value="/resources/js/pages/home.js" />

<h1>API</h1>
<p>
    Project consists of four modules: two libraries and two webapps:
</p>

<p>
    File Service API is generated with free version of <a href="http://www.miredot.com">Miredot</a>. You can <a href="/file-archive-service-apidoc">browse it here</a>
</p>

<section class="embed-responsive embed-responsive-16by9">
<iframe class="embed-responsive-item" src="/file-archive-service-apidoc"></iframe>
</section>
