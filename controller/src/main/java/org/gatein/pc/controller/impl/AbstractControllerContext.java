/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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

package org.gatein.pc.controller.impl;

import org.gatein.common.logging.LoggerFactory;
import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.controller.ControllerContext;
import org.gatein.pc.portlet.impl.spi.AbstractClientContext;
import org.gatein.pc.portlet.impl.spi.AbstractRequestContext;
import org.gatein.pc.portlet.impl.spi.AbstractSecurityContext;
import org.gatein.pc.portlet.impl.spi.AbstractServerContext;
import org.gatein.pc.portlet.impl.spi.AbstractUserContext;
import org.gatein.pc.portlet.impl.spi.AbstractWindowContext;
import org.gatein.pc.portlet.impl.spi.AbstractInstanceContext;
import org.gatein.pc.portlet.impl.spi.AbstractPortalContext;
import org.gatein.pc.api.info.PortletInfo;
import org.gatein.pc.api.invocation.ActionInvocation;
import org.gatein.pc.api.invocation.EventInvocation;
import org.gatein.pc.api.invocation.PortletInvocation;
import org.gatein.pc.api.invocation.ResourceInvocation;
import org.gatein.pc.api.invocation.RenderInvocation;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.api.spi.PortalContext;
import org.gatein.common.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractControllerContext implements ControllerContext
{

   /** . */
   private static Logger log = LoggerFactory.getLogger(AbstractControllerContext.class);

   /** . */
   public static final PortalContext PORTAL_CONTEXT = new AbstractPortalContext(Collections.singletonMap("javax.portlet.markup.head.element.support", "true"));

   /** . */
   protected final HttpServletRequest req;

   /** . */
   protected final HttpServletResponse resp;

   public AbstractControllerContext(HttpServletRequest req, HttpServletResponse resp
   )
      throws IOException
   {
      this.req = req;
      this.resp = resp;
   }

   public HttpServletRequest getClientRequest()
   {
      return req;
   }

   public HttpServletResponse getClientResponse()
   {
      return resp;
   }

   public PortletInfo getPortletInfo(String windowId)
   {
      try
      {
         return getPortlet(windowId).getInfo();
      }
      catch (PortletInvokerException e)
      {
         log.error("Could not access portlet invoker for locating window " + windowId, e);

         //
         return null;
      }
   }

   protected abstract Portlet getPortlet(String windowId) throws PortletInvokerException;

   protected abstract PortletInvocationResponse invoke(PortletInvocation invocation) throws PortletInvokerException;

   public PortletInvocationResponse invoke(String windowId, ActionInvocation actionInvocation) throws PortletInvokerException
   {
      //
      Portlet portlet = getPortlet(windowId);

      //
      actionInvocation.setClientContext(new AbstractClientContext(req));
      actionInvocation.setServerContext(new AbstractServerContext(req, resp));
      actionInvocation.setInstanceContext(new AbstractInstanceContext(portlet.getContext().getId()));
      actionInvocation.setUserContext(new AbstractUserContext(req));
      actionInvocation.setWindowContext(new AbstractWindowContext(windowId));
      actionInvocation.setPortalContext(PORTAL_CONTEXT);
      actionInvocation.setSecurityContext(new AbstractSecurityContext(req));
      actionInvocation.setRequestContext(new AbstractRequestContext(req));
      actionInvocation.setTarget(portlet.getContext());

      //
      return invoke(actionInvocation);
   }

   public PortletInvocationResponse invoke(String windowId, List<Cookie> requestCookies, EventInvocation eventInvocation) throws PortletInvokerException
   {
      Portlet portlet = getPortlet(windowId);

      //
      eventInvocation.setClientContext(new AbstractClientContext(req, requestCookies));
      eventInvocation.setServerContext(new AbstractServerContext(req, resp));
      eventInvocation.setInstanceContext(new AbstractInstanceContext(portlet.getContext().getId()));
      eventInvocation.setUserContext(new AbstractUserContext(req));
      eventInvocation.setWindowContext(new AbstractWindowContext(windowId));
      eventInvocation.setPortalContext(PORTAL_CONTEXT);
      eventInvocation.setSecurityContext(new AbstractSecurityContext(req));
      eventInvocation.setTarget(portlet.getContext());

      //
      return invoke(eventInvocation);
   }

   public PortletInvocationResponse invoke(String windowId, List<Cookie> requestCookies, RenderInvocation renderInvocation) throws PortletInvokerException
   {
      Portlet portlet = getPortlet(windowId);

      //
      renderInvocation.setClientContext(new AbstractClientContext(req, requestCookies));
      renderInvocation.setServerContext(new AbstractServerContext(req, resp));
      renderInvocation.setInstanceContext(new AbstractInstanceContext(portlet.getContext().getId()));
      renderInvocation.setUserContext(new AbstractUserContext(req));
      renderInvocation.setWindowContext(new AbstractWindowContext(windowId));
      renderInvocation.setPortalContext(PORTAL_CONTEXT);
      renderInvocation.setSecurityContext(new AbstractSecurityContext(req));
      renderInvocation.setTarget(portlet.getContext());

      //
      return invoke(renderInvocation);
   }

   public PortletInvocationResponse invoke(String windowId, ResourceInvocation resourceInvocation) throws PortletInvokerException
   {
      Portlet portlet = getPortlet(windowId);

      //
      resourceInvocation.setClientContext(new AbstractClientContext(req));
      resourceInvocation.setServerContext(new AbstractServerContext(req, resp));
      resourceInvocation.setInstanceContext(new AbstractInstanceContext(portlet.getContext().getId()));
      resourceInvocation.setUserContext(new AbstractUserContext(req));
      resourceInvocation.setWindowContext(new AbstractWindowContext(windowId));
      resourceInvocation.setPortalContext(PORTAL_CONTEXT);
      resourceInvocation.setSecurityContext(new AbstractSecurityContext(req));
      resourceInvocation.setRequestContext(new AbstractRequestContext(req));
      resourceInvocation.setTarget(portlet.getContext());

      //
      return invoke(resourceInvocation);
   }
}