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

package org.gatein.pc.jsr286.tck.taglib;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.gatein.pc.framework.UTP1;
import org.gatein.pc.unit.Assertion;
import org.gatein.pc.unit.PortletTestCase;
import org.gatein.pc.unit.PortletTestContext;
import org.gatein.pc.unit.actions.PortletRenderTestAction;
import org.gatein.pc.unit.actions.PortletResourceTestAction;
import org.gatein.pc.unit.annotations.TestCase;
import org.gatein.pc.unit.base.AbstractUniversalTestPortlet;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import org.jboss.unit.remote.driver.handler.http.response.InvokeGetResponse;

/**
 * @author <a href="mailto:boleslaw dot dawidowicz at redhat anotherdot com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
@TestCase({
   Assertion.JSR286_113
   })
public class DefineObjectsTestCase extends TaglibTestCase
{

   //TODO: doesn't test include from action/event - the actionRequest/actionResponse/eventRequest/eventResponse objects

   public DefineObjectsTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            response.setContentType("text/html");
            PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getRequestDispatcher("/defineObjects.jsp");

            include(dispatcher, request, response);

            expectedResult = "jspDispatch,renderRequest,renderResponse,portletConfig,portletSession,portletSessionScope," +
               "portletPreferences,portletPreferencesValues";


            return new InvokeGetResponse(response.createRenderURL().toString());
         }
      });



      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            assertResult(context);
            
            return new InvokeGetResponse(response.createResourceURL().toString());
         }
      });

      seq.bindAction(2, UTP1.RESOURCE_JOIN_POINT, new PortletResourceTestAction()
      {
         protected DriverResponse run(Portlet portlet, ResourceRequest request, ResourceResponse response, PortletTestContext context) throws IOException, PortletException
         {
            response.setContentType("text/html");
            PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getRequestDispatcher("/defineObjects.jsp");

            startTag = "<div id=" + response.getNamespace() + ">";
            endTag = "</div>";

            Writer writer = response.getWriter();

            writer.write(startTag);
            dispatcher.include(request, response);
            writer.write(endTag);

            expectedResult = "jspDispatch,resourceRequest,resourceResponse,portletConfig,portletSession,portletSessionScope," +
               "portletPreferences,portletPreferencesValues";

            return new InvokeGetResponse(response.createRenderURL().toString());
         }
      });

      seq.bindAction(3, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            assertResult(context);

            return new EndTestResponse();
         }
      });
   }
}
