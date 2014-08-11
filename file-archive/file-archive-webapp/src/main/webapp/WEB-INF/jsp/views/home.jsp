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

<h1>File Archive</h1>
<p>
Project consists of four modules: two libraries and two webapps:
</p>
<p>
WebApp is communicating to File Service through Java SDK. File Service is using JPA for persistence and both are using JAX-RS for REST
File content is stored as BLOBs into the database, which doesn't work well with MySQL - expect files above 500Mb to make everything slow and failing.
Webapp and service were designed to handle files up to 1Gb but MySQL is a bad choice here. Storing content on the disk or AWS would raise the limit.
</p>
<p>
WebApp and File Service use another database for authenticating users.
</p>
<p>
There are four users predefined with roles in braces: admin, joe, jack and jill.
Password is <pre>LetMe1n!</pre>
</p>
<p>
    For Service and SDK I used JAX-RS
</p>
    For WebApp I used my other project Tobacco, which uses Backbone and Twitter Bootstrap for frontend, Spring, Spring Security and Tiles on backend.
</p>

<h3>This is how it should look like</h3>
<img src="<spring:url value="/resources/images/file-archive-webapp-shot.png" />" width="80%" />
