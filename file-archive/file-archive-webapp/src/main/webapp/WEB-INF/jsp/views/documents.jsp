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
        <li><a href="#byPerson" data-toggle="tab">Search By Person</a></li>
        <li><a href="#byDocumentDate" data-toggle="tab">Search By Document Date</a></li>
        <li><a href="#byUploadTime" data-toggle="tab">Search By Upload Time</a></li>
    </ul>
    <div id="myTabContent" class="panel tab-content" style="height: 50px">
        <div class="tab-pane panel-body active" id="all">
            <p>All documents are shown</p>
        </div>
        <div class="tab-pane panel-body" id="byPerson">
            <form class="form-inline" onsubmit="return false;">
                <input id="name_filter" type="text" class="input-sm form-control" placeholder="Type name...">
                <button type="button" id="search_by_name" class="btn btn-primary btn-sm">Search</button>
            </form>
        </div>

        <div class="tab-pane panel-body" id="byDocumentDate">
            <form class="form-inline" onsubmit="return false;">
                <div class='input-group date' id='picker_doc_date_from'>
                    <input type='text' class="input-sm form-control" placeholder="From..."/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                <div class='input-group date' id='picker_doc_date_to'>
                    <input type='text' class="input-sm form-control" placeholder="To..."/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                <button type="button" id="search_by_doc_date" class="btn btn-primary btn-sm">Search</button>
            </form>
        </div>

        <div class="tab-pane panel-body" id="byUploadTime">
            <form class="form-inline" onsubmit="return false;">
                <div class='input-group date' id='picker_upload_time_from'>
                    <input type='text' class="input-sm form-control" placeholder="From..."/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                <div class='input-group date' id='picker_upload_time_to'>
                    <input type='text' class="input-sm form-control" placeholder="To..."/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                <button type="button" id="search_by_upload_time" class="btn btn-primary btn-sm">Search</button>
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
        <button id="upload_btn" type="button" class="btn btn-primary btn-sm" ><i class="fa fa-cloud-upload"></i><spring:message code="button.upload" /></button> <span id="upload_progress"></span>
    </fieldset>
</section>

<util:js value="/resources/js/pages/documents.js" />


