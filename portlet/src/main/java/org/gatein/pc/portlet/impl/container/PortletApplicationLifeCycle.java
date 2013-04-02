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
package org.gatein.pc.portlet.impl.container;

import org.gatein.pc.portlet.container.managed.ManagedObject;
import org.gatein.pc.portlet.container.object.PortletApplicationObject;
import org.gatein.pc.portlet.container.object.PortletContainerObject;
import org.gatein.pc.portlet.container.object.PortletFilterObject;
import org.gatein.pc.portlet.container.managed.LifeCycleStatus;
import org.gatein.pc.portlet.container.managed.ManagedPortletApplication;
import org.gatein.pc.portlet.container.managed.ManagedObjectRegistryEventListener;
import org.gatein.pc.portlet.container.managed.ManagedObjectAddedEvent;
import org.gatein.pc.portlet.container.managed.ManagedObjectRemovedEvent;
import org.gatein.pc.portlet.container.PortletApplicationContext;
import org.gatein.pc.portlet.container.PortletContainerContext;
import org.gatein.pc.portlet.container.PortletFilterContext;
import org.gatein.pc.portlet.container.PortletApplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletApplicationLifeCycle extends LifeCycle implements ManagedPortletApplication
{

   /** . */
   private final PortletApplicationContext portletApplicationContext;

   /** . */
   private final PortletApplicationObject portletApplication;

   /** . */
   private final ArrayList<LifeCycle> lifeCycles;

   /** . */
   private final LinkedHashMap<String, PortletContainerLifeCycle> portletContainerLifeCycles;

   /** . */
   private final LinkedHashMap<String, PortletFilterLifeCycle> portletFilterLifeCycles;

   /** . */
   private final LinkedHashMap<PortletFilterLifeCycle, Set<PortletContainerLifeCycle>> filterToContainerDependencies;

   /** Internal status to know about wiring. */
   private boolean created;

   /** . */
   private ManagedObjectRegistryEventListener listener;

   public PortletApplicationLifeCycle(
      ManagedObjectRegistryEventListener listener,
      PortletApplicationContext portletApplicationContext,
      PortletApplicationObject portletApplication)
   {
      this.listener = listener;
      this.portletApplicationContext = portletApplicationContext;
      this.portletApplication = portletApplication;
      this.lifeCycles = new ArrayList<LifeCycle>();
      this.portletContainerLifeCycles = new LinkedHashMap<String, PortletContainerLifeCycle>();
      this.portletFilterLifeCycles = new LinkedHashMap<String, PortletFilterLifeCycle>();
      this.filterToContainerDependencies = new LinkedHashMap<PortletFilterLifeCycle, Set<PortletContainerLifeCycle>>();
   }

   public PortletApplicationLifeCycle(
      PortletApplicationContext portletApplicationContext,
      PortletApplicationObject portletApplication)
   {
      this(NullManagedObjectRegistryEventListener.getInstance(), portletApplicationContext, portletApplication);
   }

   public void create()
   {
      if (created)
      {
         throw new IllegalStateException();
      }

      //
      portletApplication.setContext(portletApplicationContext);

      //
      for (PortletFilterLifeCycle portletFilterLifeCycle : portletFilterLifeCycles.values())
      {
         portletFilterLifeCycle.portletFilter.setContext(portletFilterLifeCycle.portletFilterContext);
         portletFilterLifeCycle.portletFilter.setPortletApplication(portletApplication);
         portletApplication.addPortletFilter(portletFilterLifeCycle.portletFilter);

         //
         listener.onEvent(new ManagedObjectAddedEvent(portletFilterLifeCycle));
      }

      //
      for (PortletContainerLifeCycle portletContainerLifeCycle : portletContainerLifeCycles.values())
      {
         portletContainerLifeCycle.portletContainer.setContext(portletContainerLifeCycle.portletContainerContext);
         portletContainerLifeCycle.portletContainer.setPortletApplication(portletApplication);
         portletApplication.addPortletContainer(portletContainerLifeCycle.portletContainer);

         //
         listener.onEvent(new ManagedObjectAddedEvent(portletContainerLifeCycle));
      }

      //
      for (Map.Entry<PortletFilterLifeCycle, Set<PortletContainerLifeCycle>> entry : filterToContainerDependencies.entrySet())
      {
         for (PortletContainerLifeCycle portletContainerLifeCycle : entry.getValue())
         {
            portletContainerLifeCycle.portletContainer.addPortletFilter(entry.getKey().portletFilter);
         }
      }

      //
      created = true;
   }

   public void destroy()
   {
      if (getStatus() == LifeCycleStatus.STARTED)
      {
         managedDestroy();
      }

      //
      if (created)
      {
         for (Map.Entry<PortletFilterLifeCycle, Set<PortletContainerLifeCycle>> entry : filterToContainerDependencies.entrySet())
         {
            for (PortletContainerLifeCycle portletContainerLifeCycle : entry.getValue())
            {
               portletContainerLifeCycle.portletContainer.removePortletFilter(entry.getKey().portletFilter);
            }
         }

         for (PortletContainerLifeCycle portletContainerLifeCycle : portletContainerLifeCycles.values())
         {
            listener.onEvent(new ManagedObjectRemovedEvent(portletContainerLifeCycle));

            //
            portletApplication.removePortletContainer(portletContainerLifeCycle.portletContainer);
            portletContainerLifeCycle.portletContainer.setPortletApplication(null);
            portletContainerLifeCycle.portletContainer.setContext(null);
         }

         //
         for (PortletFilterLifeCycle portletFilterLifeCycle : portletFilterLifeCycles.values())
         {
            listener.onEvent(new ManagedObjectRemovedEvent(portletFilterLifeCycle));

            //
            portletApplication.removePortletFilter(portletFilterLifeCycle.portletFilter);
            portletFilterLifeCycle.portletFilter.setPortletApplication(null);
            portletFilterLifeCycle.portletFilter.setContext(null);
         }
      }

      //
      this.created = false;
   }

   public PortletContainerLifeCycle addPortletContainer(PortletContainerContext portletContainerContext, PortletContainerObject portletContainer)
   {
      if (getStatus() != LifeCycleStatus.INITIALIZED)
      {
         throw new IllegalStateException("Cannot add portlet container because not stopped");
      }
      if (portletContainer == null)
      {
         throw new IllegalArgumentException("No null portlet container can be added");
      }

      //
      if (portletContainerLifeCycles.containsKey(portletContainer.getId()))
      {
         throw new IllegalStateException();
      }

      //
      PortletContainerLifeCycle lifeCycle = new PortletContainerLifeCycle(this, portletContainerContext, portletContainer);
      portletContainerLifeCycles.put(portletContainer.getId(), lifeCycle);
      lifeCycles.add(lifeCycle);

      //
      return lifeCycle;
   }

   public PortletFilterLifeCycle addPortletFilter(PortletFilterContext portletFilterContext, PortletFilterObject portletFilter)
   {
      if (getStatus() != LifeCycleStatus.INITIALIZED)
      {
         throw new IllegalStateException("Cannot add portlet container because not stopped");
      }
      if (portletFilter == null)
      {
         throw new IllegalArgumentException("No null portlet filter can be added");
      }

      //
      if (portletFilterLifeCycles.containsKey(portletFilter.getId()))
      {
         throw new IllegalStateException();
      }

      //
      PortletFilterLifeCycle lifeCycle = new PortletFilterLifeCycle(this, portletFilterContext, portletFilter);
      portletFilterLifeCycles.put(portletFilter.getId(), lifeCycle);
      lifeCycles.add(lifeCycle);

      //
      return lifeCycle;
   }

   public void addDependency(PortletFilterLifeCycle portletFilterLifeCycle, PortletContainerLifeCycle portletContainerLifeCycle)
   {
      if (portletFilterLifeCycle == null)
      {
         throw new IllegalArgumentException();
      }
      if (portletContainerLifeCycle == null)
      {
         throw new IllegalArgumentException();
      }

      //
      if (!portletFilterLifeCycles.containsValue(portletFilterLifeCycle))
      {
         throw new IllegalStateException();
      }
      if (!portletContainerLifeCycles.containsValue(portletContainerLifeCycle))
      {
         throw new IllegalStateException();
      }

      //
      Set<PortletContainerLifeCycle> containerDependencies =  filterToContainerDependencies.get(portletFilterLifeCycle);

      //
      if (containerDependencies == null)
      {
         containerDependencies = new HashSet<PortletContainerLifeCycle>();
         filterToContainerDependencies.put(portletFilterLifeCycle, containerDependencies);
      }

      //
      if (containerDependencies.contains(portletContainerLifeCycle))
      {
         throw new IllegalStateException();
      }

      //
      containerDependencies.add(portletContainerLifeCycle);
   }

   public Set<PortletFilterLifeCycle> getDependencies(PortletContainerLifeCycle portletContainerLifeCycle)
   {
      if (portletContainerLifeCycle == null)
      {
         throw new IllegalArgumentException();
      }

      //
      Set<PortletFilterLifeCycle> dependencies = new HashSet<PortletFilterLifeCycle>();

      //
      for (Map.Entry<PortletFilterLifeCycle, Set<PortletContainerLifeCycle>> entry : filterToContainerDependencies.entrySet())
      {
         if (entry.getValue().contains(portletContainerLifeCycle))
         {
            dependencies.add(entry.getKey());
         }
      }

      //
      return dependencies;
   }

   public Set<PortletContainerLifeCycle> getDependencies(PortletFilterLifeCycle portletFilterLifeCycle)
   {
      if (portletFilterLifeCycle == null)
      {
         throw new IllegalArgumentException();
      }

      //
      Set<PortletContainerLifeCycle> dependencies = filterToContainerDependencies.get(portletFilterLifeCycle);

      if (dependencies == null)
      {
         dependencies = new HashSet<PortletContainerLifeCycle>();
      }
      else
      {
         dependencies = new HashSet<PortletContainerLifeCycle>(dependencies);
      }

      //
      return dependencies;
   }

   @Override
   protected void invokeCreate() throws Exception
   {
      //
   }

   protected void invokeStart() throws Exception
   {
      if (!created)
      {
         throw new DependencyNotResolvedException("Application is not wired");
      }

      //
      portletApplication.start();
   }

   @Override
   protected void promoteDependents(LifeCycleStatus to)
   {
      for (PortletFilterLifeCycle portletFilterLifeCycle : portletFilterLifeCycles.values())
      {
         try
         {
            portletFilterLifeCycle.promote(to);
         }
         catch (IllegalStateException ignore)
         {
         }
      }

      //
      for (PortletContainerLifeCycle portletContainerLifeCycle : portletContainerLifeCycles.values())
      {
         try
         {
            portletContainerLifeCycle.promote(to);
         }
         catch (IllegalStateException ignore)
         {
         }
      }
   }

   @Override
   protected void demoteDependents(LifeCycleStatus to)
   {
      for (PortletContainerLifeCycle portletContainerLifeCycle : portletContainerLifeCycles.values())
      {
         portletContainerLifeCycle.demote(to);
      }

      //
      for (PortletFilterLifeCycle portletFilterLifeCycle : portletFilterLifeCycles.values())
      {
         portletFilterLifeCycle.demote(to);
      }
   }

   protected void invokeStop()
   {
      portletApplication.stop();
   }

   @Override
   protected void invokeDestroy() throws Exception
   {
      //
   }

   public String getId()
   {
      return portletApplication.getId();
   }

   @Override
   public Iterable<? extends ManagedObject> getDependencies()
   {
      return lifeCycles;
   }

   public Iterable<? extends PortletContainerLifeCycle> getManagedPortletContainers()
   {
      return portletContainerLifeCycles.values();
   }

   public PortletContainerLifeCycle getManagedPortletContainer(String portletContainerId)
   {
      return portletContainerLifeCycles.get(portletContainerId);
   }

   public Iterable<? extends PortletFilterLifeCycle> getManagedPortletFilters()
   {
      return portletFilterLifeCycles.values();
   }

   public PortletFilterLifeCycle getManagedPortletFilter(String portletFilterId)
   {
      return portletFilterLifeCycles.get(portletFilterId);
   }

   public String toString()
   {
      return getClass().getSimpleName() + "[" + portletApplication.getId() + "]";
   }

   public PortletApplication getPortletApplication()
   {
      return portletApplication;
   }

   protected ManagedObjectRegistryEventListener getListener()
   {
      return listener;
   }
}
