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
    <ul class="nav nav-tabs">
        <li class="active"><a href="#all" data-toggle="tab">Show All</a></li>
        <li><a href="#byPerson" data-toggle="tab">Filter By Person</a></li>
        <li><a href="#byTime" data-toggle="tab">Filter By Time</a></li>
    </ul>
    <div id="myTabContent" class="tab-content" style="height: 50px">
        <div class="tab-pane active" id="all">
            <p>All documents are shown</p>
        </div>
        <div class="tab-pane" id="byPerson">
            <form>
                <input type="text" placeholder="Type something…">
            </form>
        </div>
        <div class="tab-pane" id="byTime">
            <form>
                <input type="datetime" placeholder="From...">
                <input type="datetime" placeholder="To...">
            </form>
        </div>
    </div>
</section>

<section id="documents_view">
</section>

<section>
    <fieldset>
        <legend></legend>
        <input id="upload_file" type="file" style="display: none" />
        <button id="upload_btn" type="button" class="btn btn-primary btn-small" ><i class="fa fa-cloud-upload"></i><spring:message code="button.upload" /></button> <span id="upload_progress"></span>
    </fieldset>
</section>

<util:js value="/resources/js/pages/documents.js" />


