<!--
This file is part of the Meeds project (https://meeds.io/).
Copyright (C) 2020 Meeds Association
contact@meeds.io
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public License
along with this program; if not, write to the Free Software Foundation,
Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
-->
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects/>

<portlet:renderURL windowState="normal" portletMode="edit" secure="true" copyCurrentRenderParameters="true"
                   escapeXml="true"/>

<test_result_separator/>

<portlet:renderURL windowState="normal" portletMode="edit" secure="true" copyCurrentRenderParameters="true"
                   escapeXml="true" var="testVar"/>
<%
   //Put url placed as page variable
   out.print(pageContext.getAttribute("testVar"));
%>

<test_result_separator/>

<portlet:renderURL secure="false" escapeXml="false">
   <portlet:property name="testProperty" value="testPropValue"/>
   <portlet:property name="testProperty" value="testPropValue2"/>
   <portlet:property name="secondProperty" value="testPropValue"/>

   <portlet:param name="testParam" value="testParamValue"/>
   <portlet:param name="testParam" value="testParamValue2"/>
   <portlet:param name="secondParam" value="testParamValue"/>
</portlet:renderURL>