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
package org.gatein.pc.portlet.impl.jsr168.api;

import javax.portlet.PortletURLGenerationListener;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import java.util.List;

/**
 * An implementation of the API that chains.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletURLGenerationListenerChain implements PortletURLGenerationListener
{

   /** . */
   private PortletURLGenerationListener[] listeners;

   public PortletURLGenerationListenerChain(List<PortletURLGenerationListener> listeners)
   {
      this.listeners = listeners.toArray(new PortletURLGenerationListener[listeners.size()]);
   }

   public void filterActionURL(PortletURL portletURL)
   {
      for (PortletURLGenerationListener listener : listeners)
      {
         try
         {
            listener.filterActionURL(portletURL);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   public void filterRenderURL(PortletURL portletURL)
   {
      for (PortletURLGenerationListener listener : listeners)
      {
         try
         {
            listener.filterRenderURL(portletURL);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   public void filterResourceURL(ResourceURL resourceURL)
   {
      for (PortletURLGenerationListener listener : listeners)
      {
         try
         {
            listener.filterResourceURL(resourceURL);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
}
