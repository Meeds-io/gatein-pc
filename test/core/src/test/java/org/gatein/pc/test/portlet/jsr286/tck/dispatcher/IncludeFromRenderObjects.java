/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.gatein.pc.test.portlet.jsr286.tck.dispatcher;

import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.web.AbstractUniversalTestPortlet;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.unit.actions.ServletServiceTestAction;
import org.gatein.pc.test.unit.web.UTP1;
import org.gatein.pc.test.unit.web.UTS1;
import org.gatein.pc.test.unit.protocol.response.Response;
import org.gatein.pc.test.unit.protocol.response.EndTestResponse;
import static org.gatein.pc.test.unit.Assert.assertNotNull;
import static org.gatein.pc.test.unit.Assert.assertNull;
import static org.gatein.pc.test.unit.Assert.assertEquals;
import static org.gatein.pc.test.unit.Assert.assertTrue;
import static org.gatein.pc.test.unit.Assert.assertFalse;
import org.gatein.pc.test.unit.protocol.response.InvokeGetResponse;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Locale;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author <a href="mailto:boleslaw dot dawidowicz at redhat anotherdot com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */

@TestCase({
   Assertion.JSR286_104,
   Assertion.JSR286_105,
   Assertion.JSR286_106,
   Assertion.JSR286_107,
   Assertion.JSR286_108,
   Assertion.JSR286_109,
   Assertion.JSR286_110,
   Assertion.JSR286_111,
   //Assertion.JSR286_112,
   Assertion.JSR286_113,
   Assertion.JSR286_114,
   Assertion.JSR286_115,
   Assertion.JSR286_116,
   Assertion.JSR286_117,
   Assertion.JSR286_118
   })
public class IncludeFromRenderObjects
{
   
   public IncludeFromRenderObjects(final PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            // Invoke render with header
            InvokeGetResponse render = new InvokeGetResponse(response.createRenderURL().toString());
            render.addHeader("myheader", "render-value");

            return render;
         }
      });

      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            //we dispatch to servlet and assertions will be done there
            String queryString = "?key1=k1value1&key2=k2value1";
            PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getRequestDispatcher("/universalServletA" + queryString);
            response.setContentType("text/html");
            dispatcher.include(request, response);
            return null;
         }
      });


      seq.bindAction(1, UTS1.SERVICE_JOIN_POINT, new ServletServiceTestAction()
      {
         protected Response run(Servlet servlet, HttpServletRequest request, HttpServletResponse response, PortletTestContext context) throws ServletException, IOException
         {
            //we get this object to assert some of it's method compare wieth http request methods
            RenderRequest portletRequest = (RenderRequest)request.getAttribute("javax.portlet.request");
            RenderResponse portletResponse = (RenderResponse)request.getAttribute("javax.portlet.response");

            assertNotNull(portletRequest);
            assertNotNull(portletResponse);

            //SPEC:117
            //Those should do nothing - some of the getters will be checked later
            response.setContentLength(22);
            response.setLocale(Locale.TRADITIONAL_CHINESE);
            response.addCookie(new Cookie("lolo", "bobo"));
            response.sendError(404,"bobo");
            response.sendRedirect("http://www.jboss.org");
            response.setDateHeader("bobo",11);
            response.addDateHeader("bobo",21);
            response.setHeader("bobo", "toto");
            response.addHeader("bobo", "bubu");
            response.setIntHeader("toto",13);
            response.addIntHeader("lala",25);
            response.setStatus(505);
            

            //SPEC:104
            assertNull(request.getRemoteAddr());
            assertNull(request.getRemoteHost());
            assertNull(request.getRealPath("toto"));
            assertNull(request.getLocalAddr());
            assertNull(request.getLocalName());
            assertNull(request.getRequestURL());

            //SPEC:105
            assertEquals(0, request.getRemotePort());
            assertEquals(0, request.getLocalPort());

            //SPEC:106
            assertEquals(null, request.getPathInfo());
            //TODO:NYI
            //assertEquals("",request.getPathTranslated());
            assertEquals("key1=k1value1&key2=k2value1", request.getQueryString());

            assertEquals(seq.getContext().getContextPath() + "/universalServletA",request.getRequestURI());
            assertEquals("/universalServletA", request.getServletPath());


            //SPEC:107
            assertEquals(portletRequest.getScheme(), request.getScheme());
            assertEquals(portletRequest.getServerName(), request.getServerName());
            assertEquals(portletRequest.getServerPort(), request.getServerPort());
            // getAttributeNames, setAttribute, getAttribute, removeAttribute
            request.setAttribute("key1", "k1atrr1");
            request.setAttribute("key2", "k2attr2");
            List attrNames = new LinkedList();
            Enumeration attrEnum = request.getAttributeNames();
            while (attrEnum.hasMoreElements())
            {
               attrNames.add(attrEnum.nextElement());
            }
            assertNotNull(request.getAttribute("key1"));
            assertNotNull(request.getAttribute("key2"));

            assertTrue(attrNames.contains("key1"));
            assertTrue(attrNames.contains("key2"));

            request.removeAttribute("key1");
            assertNull(request.getAttribute("key1"));
            assertNotNull(request.getAttribute("key2"));

            //getLocale, getLocales
            assertEquals(portletRequest.getLocale(), request.getLocale());

            List portletLocales = new LinkedList();
            List servletLocales = new LinkedList();
            Enumeration pl = portletRequest.getLocales();
            while (pl.hasMoreElements())
            {
               portletLocales.add(pl.nextElement());
            }
            Enumeration sl = request.getLocales();
            while (sl.hasMoreElements())
            {
               servletLocales.add(sl.nextElement());
            }

            assertTrue(portletLocales.equals(servletLocales));

            assertEquals(portletRequest.isSecure(), request.isSecure());
            assertEquals(portletRequest.getAuthType(), request.getAuthType());
            assertEquals(portletRequest.getContextPath(), request.getContextPath());
            assertEquals(portletRequest.getRemoteUser(), request.getRemoteUser());
            assertEquals(portletRequest.getUserPrincipal(), request.getUserPrincipal());
            assertEquals(portletRequest.getRequestedSessionId(), request.getRequestedSessionId());
            assertEquals(portletRequest.isRequestedSessionIdValid(), request.isRequestedSessionIdValid());

            assertEquals(portletRequest.getCookies(), request.getCookies());

            //SPEC:108
            //in this assertions we use parameters passed in query string of dispatcher
            List paramNames = new LinkedList();
            Enumeration paramEnum = request.getParameterNames();
            while (paramEnum.hasMoreElements())
            {
               paramNames.add(paramEnum.nextElement());
            }
            assertTrue(paramNames.contains("key1"));
            assertTrue(paramNames.contains("key2"));

            assertEquals("k1value1", request.getParameter("key1"));
            assertEquals(new String[]{"k1value1"}, request.getParameterValues("key1"));

            Map paramNamesMap = request.getParameterMap();
            assertTrue(paramNamesMap.containsKey("key1"));
            assertTrue(paramNamesMap.containsKey("key2"));

            //SPEC:109
            request.setCharacterEncoding("utf8");
            assertNull(request.getCharacterEncoding());
            assertNull(request.getContentType());
            assertNull(request.getInputStream());
            assertNull(request.getReader());

            //SPEC:110
            assertEquals(0, request.getContentLength());

            //SPEC:111
            assertEquals(portletRequest.getProperty("myheader"), request.getHeader("myheader"));
            List rheaders = Collections.list(request.getHeaders("myheader"));
            List pheaders = Collections.list(portletRequest.getProperties("myheader"));

            for (Iterator iterator = pheaders.iterator(); iterator.hasNext();)
            {
               Object o = iterator.next();
               assertTrue(rheaders.contains(o));
            }

            List headerNames = Collections.list(request.getHeaderNames());
            Enumeration propertyNamesE = portletRequest.getPropertyNames();
            while (propertyNamesE.hasMoreElements())
            {
               Object o = propertyNamesE.nextElement();
               assertTrue(headerNames.contains(o));
            }
            assertTrue(headerNames.contains("myheader"));

            //TODO: seems to not be implemented
            //assertEquals("?", request.getDateHeader("myheader"));
            //assertEquals("?", request.getIntHeader("myheader"));

            //SPEC:112
            //TODO:
            //TODO: The following methods of the HttpServletRequest must provide the
            //TODO: functionality defined by the Servlet Specification: getRequestDispatcher, isUserInRole, getSession,
            //TODO: isRequestedSessionIdFromCookie, isRequestedSessionIdFromURL and isRequestedSessionIdFromUrl.

            //SPEC:113
            assertEquals("GET", request.getMethod());

            //SPEC:114
            assertEquals("HTTP/1.1", request.getProtocol());

            //SPEC:115
            assertNull(response.encodeRedirectURL("lolo"));
            assertNull(response.encodeRedirectUrl("blah"));

            //SPEC:116
            assertEquals(portletResponse.getCharacterEncoding(), response.getCharacterEncoding());
            //TODO: setBufferSize, flushBuffer, resetBuffer, reset
            assertEquals(portletResponse.getBufferSize(), response.getBufferSize());
            assertEquals(portletResponse.isCommitted(), response.isCommitted());
            //TODO: How to check if its the same behaviour as in PR? Its not the same object.
            assertNotNull(response.getOutputStream());
            //Cannot invoke both writer and outputstream in single request
            //assertEquals(portletResponse.getWriter(), response.getWriter());
            assertEquals(portletResponse.encodeURL("http://www.jboss.com/"), response.encodeURL("http://www.jboss.com/"));
            assertEquals(portletResponse.encodeURL("http://www.jboss.com/"), response.encodeUrl("http://www.jboss.com/"));


            //SPEC:118
            assertEquals(false, response.containsHeader("blah"));

            //SPEC:119
            assertEquals(portletResponse.getLocale(), response.getLocale());

            return new EndTestResponse();
         }
      });
   }
}
