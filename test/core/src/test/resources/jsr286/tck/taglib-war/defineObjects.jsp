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
<%@ page import="org.gatein.pc.test.unit.Assert" %>
<%@ page import="javax.portlet.RenderRequest" %>
<%@ page import="javax.portlet.RenderResponse" %>
<%@ page import="javax.portlet.ActionRequest" %>
<%@ page import="javax.portlet.ActionResponse" %>
<%@ page import="javax.portlet.EventRequest" %>
<%@ page import="javax.portlet.EventResponse" %>
<%@ page import="javax.portlet.ResourceRequest" %>
<%@ page import="javax.portlet.ResourceResponse" %>
<%@ page import="javax.portlet.PortletConfig" %>
<%@ page import="javax.portlet.PortletSession" %>
<%@ page import="java.util.Map" %>
<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects/>
<%
   out.print("jspDispatch");

   if (renderRequest != null && renderRequest instanceof RenderRequest)
   {
      out.print(",renderRequest");
   }
   if (renderResponse != null && renderResponse instanceof RenderResponse)
   {
      out.print(",renderResponse");
   }
   if (actionRequest != null && actionRequest instanceof ActionRequest)
   {
      out.print(",actionRequest");
   }
   if (actionResponse != null && actionResponse instanceof ActionResponse)
   {
      out.print(",actionResponse");
   }
   if (eventRequest != null && eventRequest instanceof EventRequest)
   {
      out.print(",eventRequest");
   }
   if (eventResponse != null && eventResponse instanceof EventResponse)
   {
      out.print(",eventResponse");
   }
   if (resourceRequest != null && resourceRequest instanceof ResourceRequest)
   {
      out.print(",resourceRequest");
   }
   if (resourceResponse != null && resourceResponse instanceof ResourceResponse)
   {
      out.print(",resourceResponse");
   }
   if (portletConfig != null && portletConfig instanceof PortletConfig)
   {
      out.print(",portletConfig");
   }
   if (portletSession != null && portletSession instanceof PortletSession)
   {
      out.print(",portletSession");
   }
   if (portletSessionScope != null && portletSessionScope instanceof Map)
   {
      out.print(",portletSessionScope");
   }
   if (portletPreferences != null && portletPreferences instanceof PortletPreferences)
   {
      out.print(",portletPreferences");
   }
   if (portletPreferencesValues != null && portletPreferencesValues instanceof Map)
   {
      out.print(",portletPreferencesValues");
   }

%>