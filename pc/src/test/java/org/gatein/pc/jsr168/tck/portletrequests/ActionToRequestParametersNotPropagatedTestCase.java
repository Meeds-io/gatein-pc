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
package org.gatein.pc.jsr168.tck.portletrequests;

import static org.jboss.unit.api.Assert.assertEquals;
import static org.jboss.unit.api.Assert.assertNull;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.gatein.pc.framework.UTP1;
import org.gatein.pc.unit.Assertion;
import org.gatein.pc.unit.PortletTestCase;
import org.gatein.pc.unit.PortletTestContext;
import org.gatein.pc.unit.actions.PortletActionTestAction;
import org.gatein.pc.unit.actions.PortletRenderTestAction;
import org.gatein.pc.unit.annotations.TestCase;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import org.jboss.unit.remote.driver.handler.http.response.InvokeGetResponse;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase(Assertion.JSR168_50)
public class ActionToRequestParametersNotPropagatedTestCase
{
   public ActionToRequestParametersNotPropagatedTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            PortletURL url = response.createActionURL();
            url.setParameter("key1", "k1value1");
            url.setParameter("key2", new String[]{"k2value1", "k2value2", "k2value3"});
            return new InvokeGetResponse(url.toString());
         }
      });

      seq.bindAction(1, UTP1.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context)
         {
            //assert that we received parameters from render
            assertEquals("k1value1", request.getParameter("key1"));
            assertEquals(new String[]{"k2value1", "k2value2", "k2value3"}, request.getParameterValues("key2"));
         }
      });

      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            //assert that parameters weren't propagated from Action phase
            assertNull(request.getParameter("key1"));
            assertNull(request.getParameter("key2"));
            return new EndTestResponse();
         }
      });
   }
}
