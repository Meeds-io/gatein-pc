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
package org.gatein.pc.portlet.container;

import junit.framework.TestCase;
import org.gatein.pc.portlet.impl.container.PortletApplicationLifeCycle;
import org.gatein.pc.portlet.impl.container.PortletContainerLifeCycle;
import org.gatein.pc.portlet.impl.container.LifeCycle;
import org.gatein.pc.portlet.container.managed.LifeCycleStatus;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class LifeCycleReentranceDetectionTestCase extends TestCase
{

   PortletApplicationLifeCycle applicationLF;
   PortletApplicationObjectSupport application;
   PortletContainerLifeCycle containerLF;
   PortletContainerObjectSupport container;

   IllegalStateException ise;

   @Override
   protected void setUp() throws Exception
   {
      this.ise = null;
   }

   private class InvokeLifeCycleCallback implements ObjectSupport.Callback
   {

      /** . */
      private LifeCycle lifeCycle;

      /** . */
      private boolean invokeStart;

      /** . */
      private boolean rethrowISE;

      private InvokeLifeCycleCallback(LifeCycle lifeCycle, boolean invokeStart, boolean rethrowISE)
      {
         this.lifeCycle = lifeCycle;
         this.invokeStart = invokeStart;
         this.rethrowISE = rethrowISE;
      }

      public void execute()
      {
         try
         {
            if (invokeStart)
            {
               lifeCycle.managedStart();
            }
            else
            {
               lifeCycle.managedDestroy();
            }
         }
         catch (IllegalStateException e)
         {
            ise = e;

            //
            if (rethrowISE)
            {
               throw e;
            }
         }
      }
   }

   public void testApplicationReentersApplication1()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      application.startCallback = new InvokeLifeCycleCallback(applicationLF, true, false);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertEquals(LifeCycleStatus.STARTED, applicationLF.getStatus());
   }

   public void testApplicationReentersApplication2()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      application.startCallback = new InvokeLifeCycleCallback(applicationLF, true, true);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertNotNull(applicationLF.getFailure());
   }

   public void testContainerReentersContainer1()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      container = new PortletContainerObjectSupport("container");
      containerLF = applicationLF.addPortletContainer(new PortletContainerContextSupport(), container);
      container.startCallback  = new InvokeLifeCycleCallback(containerLF, true, false);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertEquals(LifeCycleStatus.STARTED, applicationLF.getStatus());
      assertEquals(LifeCycleStatus.STARTED, containerLF.getStatus());
   }

   public void testContainerReentersContainer2()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      container = new PortletContainerObjectSupport("container");
      containerLF = applicationLF.addPortletContainer(new PortletContainerContextSupport(), container);
      container.startCallback  = new InvokeLifeCycleCallback(containerLF, true, true);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertEquals(LifeCycleStatus.STARTED, applicationLF.getStatus());
      assertNotNull(containerLF.getStatus());
   }

   public void testContainerReentersApplication1()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      container = new PortletContainerObjectSupport("container");
      containerLF = applicationLF.addPortletContainer(new PortletContainerContextSupport(), container);
      container.startCallback  = new InvokeLifeCycleCallback(applicationLF, true, false);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertEquals(LifeCycleStatus.STARTED, applicationLF.getStatus());
      assertEquals(LifeCycleStatus.STARTED, containerLF.getStatus());
   }

   public void testContainerReentersApplication2()
   {
      application = new PortletApplicationObjectSupport("application");
      applicationLF = new PortletApplicationLifeCycle(new PortletApplicationContextSupport(), application);
      container = new PortletContainerObjectSupport("container");
      containerLF = applicationLF.addPortletContainer(new PortletContainerContextSupport(), container);
      container.startCallback  = new InvokeLifeCycleCallback(applicationLF, true, true);

      //
      applicationLF.create();

      //
      applicationLF.managedStart();

      //
      assertNotNull(ise);
      assertEquals(LifeCycleStatus.STARTED, applicationLF.getStatus());
      assertNotNull(containerLF.getStatus());
   }
}
