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
package org.gatein.pc.test.portlet.jsr286.tck.portletfilter;

import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.unit.actions.PortletActionTestAction;
import org.gatein.pc.test.unit.actions.PortletEventTestAction;
import org.gatein.pc.test.unit.actions.PortletResourceTestAction;
import org.gatein.pc.test.unit.web.UTP1;
import org.gatein.pc.test.unit.web.UTP2;
import org.gatein.pc.test.unit.protocol.response.Response;
import org.gatein.pc.test.unit.protocol.response.EndTestResponse;
import org.gatein.pc.test.unit.protocol.response.InvokeGetResponse;
import static org.gatein.pc.test.unit.Assert.*;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({Assertion.JSR286_298,Assertion.JSR286_300})
public class Wildcard
{
   public Wildcard(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            return new InvokeGetResponse(response.createActionURL().toString());
         }
      });
      seq.bindAction(1, UTP1.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.ACTION));
            assertFalse((TraceFilter.isActive("PartialWildcardFilter", TraceFilter.ACTION)));
            response.setEvent("Event", null);
         }
      });
      seq.bindAction(1, UTP1.EVENT_JOIN_POINT, new PortletEventTestAction()
      {
         protected void run(Portlet portlet, EventRequest request, EventResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.EVENT));
            assertFalse((TraceFilter.isActive("PartialWildcardFilter", TraceFilter.EVENT)));
         }
      });
      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.RENDER));
            assertFalse((TraceFilter.isActive("PartialWildcardFilter", TraceFilter.RENDER)));
            return new InvokeGetResponse(response.createResourceURL().toString());
         }
      });
      seq.bindAction(2, UTP1.RESOURCE_JOIN_POINT, new PortletResourceTestAction()
      {
         protected Response run(Portlet portlet, ResourceRequest request, ResourceResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.RESOURCE));
            assertFalse((TraceFilter.isActive("PartialWildcardFilter", TraceFilter.RESOURCE)));
            return new InvokeGetResponse(response.createRenderURL().toString());
         }
      });
      seq.bindAction(3, UTP2.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            return new InvokeGetResponse(response.createActionURL().toString());
         }
      });
      seq.bindAction(4, UTP2.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.ACTION));
            assertTrue(TraceFilter.isActive("PartialWildcardFilter", TraceFilter.ACTION));
            response.setEvent("Event", null);
         }
      });
      seq.bindAction(4, UTP2.EVENT_JOIN_POINT, new PortletEventTestAction()
      {
         protected void run(Portlet portlet, EventRequest request, EventResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.EVENT));
            assertTrue(TraceFilter.isActive("PartialWildcardFilter", TraceFilter.EVENT));
         }
      });
      seq.bindAction(4, UTP2.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.RENDER));
            assertTrue(TraceFilter.isActive("PartialWildcardFilter", TraceFilter.RENDER));
            return new InvokeGetResponse(response.createResourceURL().toString());
         }
      });
      seq.bindAction(5, UTP2.RESOURCE_JOIN_POINT, new PortletResourceTestAction()
      {
         protected Response run(Portlet portlet, ResourceRequest request, ResourceResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertTrue(TraceFilter.isActive("WildcardFilter", TraceFilter.RESOURCE));
            assertTrue(TraceFilter.isActive("PartialWildcardFilter", TraceFilter.RESOURCE));
            return new EndTestResponse();
         }
      });
   }
}
