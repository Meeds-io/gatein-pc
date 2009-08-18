/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.pc.jsr168.tck.portletresponses;

import static org.jboss.unit.api.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gatein.pc.framework.UTP1;
import org.gatein.pc.framework.UTS1;
import org.gatein.pc.unit.Assertion;
import org.gatein.pc.unit.PortletTestCase;
import org.gatein.pc.unit.PortletTestContext;
import org.gatein.pc.unit.actions.PortletActionTestAction;
import org.gatein.pc.unit.actions.PortletRenderTestAction;
import org.gatein.pc.unit.actions.ServletServiceTestAction;
import org.gatein.pc.unit.annotations.TestCase;
import org.jboss.unit.Failure;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import org.jboss.unit.driver.response.FailureResponse;
import org.jboss.unit.remote.driver.handler.http.response.InvokeGetResponse;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_74, Assertion.JSR168_75})
public class ParametersSetAfterSendRedirectTestCase
{
   public ParametersSetAfterSendRedirectTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            PortletURL url = response.createActionURL();
            return new InvokeGetResponse(url.toString());
         }
      });

      seq.bindAction(1, UTP1.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws IOException
         {
            response.sendRedirect(request.getContextPath() + "/universalServletA");

            try
            {
               response.setRenderParameter("key", "value");
               fail();
            }
            catch (IllegalStateException e)
            {
               //expected
            }

            try
            {
               Map<String, String[]> map = new HashMap<String, String[]>();
               map.put("key", new String[]{"value"});
               response.setRenderParameters(map);
               fail();
            }
            catch (IllegalStateException e)
            {
               //expected
            }
         }
      });

      seq.bindAction(1, UTS1.SERVICE_JOIN_POINT, new ServletServiceTestAction()
      {
         protected DriverResponse run(Servlet servlet, HttpServletRequest request, HttpServletResponse response, PortletTestContext context) throws ServletException, IOException
         {
            return new EndTestResponse();
         }
      });

      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            return new FailureResponse(Failure.createAssertionFailure("Render wasn't expected"));
         }
      });
   }
}
