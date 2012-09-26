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
package org.gatein.pc.controller.event;

import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.controller.EventPhaseContext;

import java.util.Collections;

/**
 * An implementation of the event controller context that do no ops.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class AbstractEventControllerContext implements EventControllerContext
{
   public Iterable<WindowEvent> eventProduced(EventPhaseContext context, WindowEvent producedEvent, WindowEvent sourceEvent)
   {
      return Collections.emptyList();
   }

   public void eventConsumed(EventPhaseContext context, WindowEvent consumedEvent, PortletInvocationResponse consumerResponse)
   {
   }

   public void eventFailed(EventPhaseContext context, WindowEvent consumedEvent, Throwable throwable)
   {
   }

   public void eventDiscarded(EventPhaseContext context, WindowEvent discardedEvent, int cause)
   {
   }
}
