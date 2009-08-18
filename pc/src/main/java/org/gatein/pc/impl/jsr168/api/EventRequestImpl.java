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
package org.gatein.pc.impl.jsr168.api;

import org.gatein.pc.api.invocation.EventInvocation;
import org.gatein.pc.impl.jsr168.PortletContainerImpl;

import javax.portlet.EventRequest;
import javax.portlet.Event;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class EventRequestImpl extends PortletRequestImpl implements EventRequest
{

   /** . */
   private EventImpl event;

   /** . */
   private EventInvocation eventInvocation;

   public EventRequestImpl(PortletContainerImpl container, EventInvocation invocation)
   {
      super(container, invocation);

      //
      this.eventInvocation = invocation;
   }

   public Event getEvent()
   {
      if (event == null)
      {
         event = new EventImpl(eventInvocation);
      }

      //
      return event;
   }

   public String getMethod()
   {
      // Does it make sense in the remote case ?
      return invocation.getDispatchedRequest().getMethod();
   }
}
