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
package org.gatein.pc.api.invocation;

import org.gatein.pc.api.Mode;
import org.gatein.pc.api.PortletContext;
import org.gatein.pc.api.StateString;
import org.gatein.pc.api.WindowState;
import org.gatein.pc.api.invocation.resolver.AttributeResolver;
import org.gatein.pc.api.invocation.resolver.MapAttributeResolver;
import org.gatein.pc.api.spi.ClientContext;
import org.gatein.pc.api.spi.InstanceContext;
import org.gatein.pc.api.spi.PortalContext;
import org.gatein.pc.api.spi.PortletInvocationContext;
import org.gatein.pc.api.spi.SecurityContext;
import org.gatein.pc.api.spi.ServerContext;
import org.gatein.pc.api.spi.UserContext;
import org.gatein.pc.api.spi.WindowContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6765 $
 * @since 2.4
 */
@SuppressWarnings("unchecked")
public abstract class PortletInvocation
{

   /** . */
   protected final AttributeResolver attributes;

   /** . */
   protected StateString navigationalState;

   /** . */
   protected Map<String, String[]> publicNavigationalState;

   /** . */
   protected Mode mode;

   /** . */
   protected WindowState windowState;

   /** The target portlet. */
   protected PortletContext target;

   /** The portlet request attributes. */
   protected Map<String, Object> requestAttributes;

   /** The invocation context. */
   protected PortletInvocationContext ctx;

   /** The portal context. */
   protected PortalContext portalContext;

   /** The client context. */
   protected ClientContext clientContext;

   /** The server context. */
   protected ServerContext serverContext;

   /** The security context. */
   protected SecurityContext securityContext;

   /** The user context. */
   protected UserContext userContext;

   /** The instance context. */
   protected InstanceContext instanceContext;

   /** The window context. */
   protected WindowContext windowContext;

   /** The dispatched request. */
   protected HttpServletRequest req;

   /** The dispatched response. */
   protected HttpServletResponse resp;

   /**
    * Create an invocation to a portlet.
    *
    * @param ctx the portlet invocation context
    * @throws IllegalArgumentException if the provided context is null
    */
   protected PortletInvocation(PortletInvocationContext ctx) throws IllegalArgumentException
   {
      if (ctx == null)
      {
         throw new IllegalArgumentException();
      }

      //
      this.ctx = ctx;
      this.attributes = new MapAttributeResolver();
   }

   public PortletInvocationContext getContext()
   {
      return ctx;
   }

   public PortletContext getTarget()
   {
      return target;
   }

   public void setTarget(PortletContext target)
   {
      this.target = target;
   }

   /**
    * Returns the http servlet request.
    *
    * @return the response
    */
   public HttpServletResponse getResponse()
   {
      return resp;
   }

   /**
    * Set the http servlet request.
    *
    * @param dresp the response
    */
   public void setResponse(HttpServletResponse dresp)
   {
      this.resp = dresp;
   }

   /**
    * Returns the http servlet request.
    *
    * @return the dispatched response
    * @deprecated use {@link #getResponse()}
    */
   public HttpServletResponse getDispatchedResponse()
   {
      return resp;
   }

   /**
    * Set the http servlet request.
    *
    * @param dresp the dispatched response
    * @deprecated use {@link #setResponse(javax.servlet.http.HttpServletResponse)}
    */
   public void setDispatchedResponse(HttpServletResponse dresp)
   {
      this.resp = dresp;
   }

   /**
    * Returns the http servlet request.
    *
    * @return the request
    */
   public HttpServletRequest getRequest()
   {
      return req;
   }

   /**
    * Set the http servlet request.
    *
    * @param dreq the request
    */
   public void setRequest(HttpServletRequest dreq)
   {
      this.req = dreq;
   }

   /**
    * Returns the dispatched http servlet request.
    *
    * @return the dispatched request
    * @deprecated use {@link #getRequest()}
    */
   public HttpServletRequest getDispatchedRequest()
   {
      return req;
   }

   /**
    * Set the dispatched http servlet request.
    *
    * @param dreq the dispatched request
    * @deprecated use {@link #setRequest(javax.servlet.http.HttpServletRequest)}
    */
   public void setDispatchedRequest(HttpServletRequest dreq)
   {
      this.req = dreq;
   }

   public StateString getNavigationalState()
   {
      return navigationalState;
   }

   public void setNavigationalState(StateString navigationalState)
   {
      this.navigationalState = navigationalState;
   }

   public Map<String, String[]> getPublicNavigationalState()
   {
      return publicNavigationalState;
   }

   public void setPublicNavigationalState(Map<String, String[]> publicNavigationalState)
   {
      this.publicNavigationalState = publicNavigationalState;
   }

   public Mode getMode()
   {
      return mode;
   }

   public void setMode(Mode mode)
   {
      this.mode = mode;
   }

   public WindowState getWindowState()
   {
      return windowState;
   }

   public void setWindowState(WindowState windowState)
   {
      this.windowState = windowState;
   }

   public PortalContext getPortalContext()
   {
      return portalContext;
   }

   public void setPortalContext(PortalContext portalContext)
   {
      this.portalContext = portalContext;
   }

   public ClientContext getClientContext()
   {
      return clientContext;
   }

   public void setClientContext(ClientContext clientContext)
   {
      this.clientContext = clientContext;
   }

   public ServerContext getServerContext()
   {
      return serverContext;
   }

   public void setServerContext(ServerContext serverContext)
   {
      this.serverContext = serverContext;
   }

   public SecurityContext getSecurityContext()
   {
      return securityContext;
   }

   public void setSecurityContext(SecurityContext securityContext)
   {
      this.securityContext = securityContext;
   }

   public UserContext getUserContext()
   {
      return userContext;
   }

   public void setUserContext(UserContext userContext)
   {
      this.userContext = userContext;
   }

   public InstanceContext getInstanceContext()
   {
      return instanceContext;
   }

   public void setInstanceContext(InstanceContext instanceContext)
   {
      this.instanceContext = instanceContext;
   }

   public WindowContext getWindowContext()
   {
      return windowContext;
   }

   public void setWindowContext(WindowContext windowContext)
   {
      this.windowContext = windowContext;
   }

   public Map<String, Object> getRequestAttributes()
   {
      return requestAttributes;
   }

   public void setRequestAttributes(Map<String, Object> requestAttributes)
   {
      this.requestAttributes = requestAttributes;
   }

   public void setAttribute(String attrKey, Object attrValue)
   {
      attributes.setAttribute(attrKey, attrValue);
   }

   public Object getAttribute(String attrKey)
   {
      return attributes.getAttribute(attrKey);
   }

   public void removeAttribute(Object attrKey) throws IllegalArgumentException
   {
      attributes.setAttribute(attrKey, null);
   }
}
