/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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
package org.gatein.pc.controller;

import junit.framework.TestCase;
import org.gatein.pc.controller.state.PageNavigationalState;
import org.gatein.pc.controller.state.WindowNavigationalState;
import org.gatein.pc.portlet.support.PortletInvokerSupport;
import org.gatein.pc.portlet.support.PortletSupport;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.api.invocation.response.UpdateNavigationalStateResponse;
import org.gatein.pc.api.invocation.ActionInvocation;
import org.gatein.pc.api.invocation.ResourceInvocation;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.api.OpaqueStateString;
import org.gatein.pc.api.StateString;
import org.gatein.pc.api.cache.CacheLevel;
import org.gatein.pc.controller.request.PortletActionRequest;
import org.gatein.pc.controller.request.PortletResourceRequest;
import org.gatein.pc.controller.request.PortletRenderRequest;
import org.gatein.common.util.ParameterMap;
import org.gatein.pc.api.Mode;
import org.gatein.pc.api.WindowState;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletControllerRequestTestCase extends TestCase
{

   /** . */
   PortletController controller = new PortletController();

   /** . */
   PortletControllerContextSupport context = new PortletControllerContextSupport();

   /** . */
   PortletInvokerSupport invoker = context.getInvoker();

   /** . */
   PageNavigationalState pageNS = new PageNavigationalState(false);

   /** . */
   StateString portletNS = new OpaqueStateString("abc");

   /** . */
   WindowNavigationalState windowNS = new WindowNavigationalState(portletNS, org.gatein.pc.api.Mode.EDIT, org.gatein.pc.api.WindowState.MAXIMIZED);

   /** . */
   ParameterMap body = new ParameterMap(Collections.singletonMap("param", new String[]{"value"}));

   /** . */
   OpaqueStateString is = new OpaqueStateString("is");

   /** . */
   OpaqueStateString rs = new OpaqueStateString("rs");

   public void testPortletControllerActionRequest() throws PortletInvokerException
   {
      PortletSupport fooPortlet = invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      fooPortlet.addHandler(new PortletSupport.ActionHandler()
      {
         protected PortletInvocationResponse invoke(ActionInvocation action) throws PortletInvokerException
         {
            assertNotNull(action);
            assertEquals(is, action.getInteractionState());
            assertEquals(portletNS, action.getNavigationalState());
            assertEquals(Mode.EDIT, action.getMode());
            assertEquals(org.gatein.pc.api.WindowState.MAXIMIZED, action.getWindowState());
            assertEquals(body, action.getForm());

            //
            return new UpdateNavigationalStateResponse();
         }
      });

      //
      PortletActionRequest action = new PortletActionRequest(PortletInvokerSupport.FOO_PORTLET_ID, is, body, windowNS, pageNS);

      //
      controller.process(context, action);
   }

   public void testPortletControllerRenderRequest() throws PortletInvokerException
   {
      invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      PortletRenderRequest render = new PortletRenderRequest(PortletInvokerSupport.FOO_PORTLET_ID, windowNS, new HashMap<String, String[]>(), pageNS);
      controller.process(context, render);
   }

   public void testPortletControllerResourceRequestFullScope() throws PortletInvokerException
   {
      PortletSupport fooPortlet = invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      ResourceHandlerImpl resourceHandler = new ResourceHandlerImpl();
      resourceHandler.expectedResourceId = "resourceid";
      resourceHandler.expectedRS = rs;
      resourceHandler.expectedCacheability = CacheLevel.FULL;
      resourceHandler.expectedPortletNS = null;
      resourceHandler.expectedBody = body;
      resourceHandler.expectedMode = Mode.VIEW;
      resourceHandler.expectedWindowState = org.gatein.pc.api.WindowState.NORMAL;
      resourceHandler.expectedPublicState = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest fullServing = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, body, new PortletResourceRequest.FullScope());
      controller.process(context, fullServing);

      //
      resourceHandler.expectedBody = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest fullServing2 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, null, new PortletResourceRequest.FullScope());
      controller.process(context, fullServing2);

      //
      resourceHandler.expectedResourceId = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest fullServing3 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, null, rs, null, new PortletResourceRequest.FullScope());
      controller.process(context, fullServing3);
   }

   public void testPortletControllerResourceRequestPortletScope() throws PortletInvokerException
   {
      PortletSupport fooPortlet = invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      ResourceHandlerImpl resourceHandler = new ResourceHandlerImpl();
      resourceHandler.expectedResourceId = "resourceid";
      resourceHandler.expectedRS = rs;
      resourceHandler.expectedBody = body;
      resourceHandler.expectedCacheability = CacheLevel.PORTLET;
      resourceHandler.expectedPortletNS = portletNS;
      resourceHandler.expectedMode = Mode.EDIT;
      resourceHandler.expectedWindowState = org.gatein.pc.api.WindowState.MAXIMIZED;
      resourceHandler.expectedPublicState = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest portletServing = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, body, new PortletResourceRequest.PortletScope(windowNS));
      controller.process(context, portletServing);

      //
      resourceHandler.expectedPortletNS = null;
      resourceHandler.expectedMode = Mode.VIEW;
      resourceHandler.expectedWindowState = org.gatein.pc.api.WindowState.NORMAL;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest portletServing2 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, body, new PortletResourceRequest.PortletScope(null));
      controller.process(context, portletServing2);

      //
      resourceHandler.expectedResourceId = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest portletServing3 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, null, rs, body, new PortletResourceRequest.PortletScope(null));
      controller.process(context, portletServing3);
   }

   public void testPortletControllerResourceRequestPageScope() throws PortletInvokerException
   {
      PortletSupport fooPortlet = invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      ResourceHandlerImpl resourceHandler = new ResourceHandlerImpl();
      resourceHandler.expectedResourceId = "resourceid";
      resourceHandler.expectedRS = rs;
      resourceHandler.expectedBody = body;
      resourceHandler.expectedPortletNS = portletNS;
      resourceHandler.expectedMode = Mode.EDIT;
      resourceHandler.expectedWindowState = WindowState.MAXIMIZED;
      resourceHandler.expectedCacheability = CacheLevel.PAGE;
      resourceHandler.expectedPublicState = new ParameterMap();
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest pageServing = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, body, new PortletResourceRequest.PageScope(windowNS, pageNS));
      controller.process(context, pageServing);

      //
      resourceHandler.expectedPortletNS = null;
      resourceHandler.expectedMode = Mode.VIEW;
      resourceHandler.expectedWindowState = org.gatein.pc.api.WindowState.NORMAL;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest pageServing2 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, body, new PortletResourceRequest.PageScope(null, pageNS));
      controller.process(context, pageServing2);

      //
      resourceHandler.expectedBody = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest pageServing3 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, null, new PortletResourceRequest.PageScope(null, pageNS));
      controller.process(context, pageServing3);

      //
      resourceHandler.expectedPublicState = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest pageServing4 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, "resourceid", rs, null, new PortletResourceRequest.PageScope(null, null));
      controller.process(context, pageServing4);

      //
      resourceHandler.expectedResourceId = null;
      fooPortlet.addHandler(resourceHandler);
      PortletResourceRequest pageServing5 = new PortletResourceRequest(PortletInvokerSupport.FOO_PORTLET_ID, null, rs, null, new PortletResourceRequest.PageScope(null, null));
      controller.process(context, pageServing5);
   }

   private static class ResourceHandlerImpl extends PortletSupport.ResourceHandler
   {

      private String expectedResourceId;
      private StateString expectedRS;
      private StateString expectedPortletNS;
      private ParameterMap expectedBody;
      private CacheLevel expectedCacheability;
      private Mode expectedMode;
      private org.gatein.pc.api.WindowState expectedWindowState;
      private ParameterMap expectedPublicState;

      protected PortletInvocationResponse invoke(ResourceInvocation resServing) throws PortletInvokerException
      {
         assertNotNull(resServing);
         assertEquals(expectedResourceId, resServing.getResourceId());
         assertEquals(expectedRS, resServing.getResourceState());
         assertEquals(expectedCacheability, resServing.getCacheLevel());
         assertEquals(expectedPortletNS, resServing.getNavigationalState());
         assertEquals(expectedMode, resServing.getMode());
         assertEquals(expectedWindowState, resServing.getWindowState());
         assertEquals(expectedBody, resServing.getForm());
         assertEquals(expectedPublicState, resServing.getPublicNavigationalState());

         // add public NS checks

         //
         return new UpdateNavigationalStateResponse();
      }

   }

}
