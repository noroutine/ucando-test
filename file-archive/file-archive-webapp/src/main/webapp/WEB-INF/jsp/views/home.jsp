<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="tb" uri="/WEB-INF/taglib/taglib.tld" %>

<util:js value="/resources/js/pages/home.js"/>

<h1>File Archive</h1>

<p>
    Project consists of four modules: two libraries and two webapps:
</p>

<p>
    Web application is communicating to File Service through Java SDK which works on top of JAX-RS. File Service is
    using JPA for persistence of metadata and Amazon S3 for content storage.
    File content is stored as BLOBs on S3 or disk, depending on the deployment configuration.
</p>

<p>
    Web application and service were designed to handle files up to 10Gb.
</p>

<p>
    WebApp and File Service use both separate database for authenticating users.
</p>

<p>
    For WebApp I used my other project Tobacco, which uses Backbone and Twitter Bootstrap for frontend, Spring, Spring
    Security and Tiles on backend.
</p>

<h3>This is how it should look like</h3>
<img src="<spring:url value="/resources/images/file-archive-webapp-shot.png" />" width="80%"/>
