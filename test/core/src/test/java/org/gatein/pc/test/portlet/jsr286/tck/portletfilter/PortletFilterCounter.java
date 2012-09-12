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

import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.gatein.pc.test.portlet.jsr286.common.AbstractRenderFilter;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletFilterCounter extends AbstractRenderFilter
{

   /** . */
   private static WeakHashMap<ClassLoader, AtomicInteger> counters = new WeakHashMap<ClassLoader, AtomicInteger>();

   public static int getCounter()
   {
      ClassLoader key = Thread.currentThread().getContextClassLoader();
      AtomicInteger counter = counters.get(key);
      return counter != null ? counter.get() : 0;
   }

   public PortletFilterCounter()
   {
      synchronized (PortletFilterCounter.class)
      {
         ClassLoader key = Thread.currentThread().getContextClassLoader();
         AtomicInteger counter = counters.get(key);
         if (counter == null)
         {
            counters.put(key, counter = new AtomicInteger());
         }
         counter.incrementAndGet();
      }
   }
}
