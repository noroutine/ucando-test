<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="tb" uri="/WEB-INF/taglib/taglib.tld" %>


<section>
<h1>Files</h1>

</section>

<section id="documents_view">
</section>

<section>
    <fieldset>
        <legend></legend>
        <input id="upload_file" type="file" style="display: none" />
        <button id="upload_btn" type="button" class="button" ><i class="fa fa-cloud-upload"></i><spring:message code="button.upload" /></button> <span id="upload_progress"></span>
    </fieldset>
</section>

<util:js value="/resources/js/pages/documents.js" />


