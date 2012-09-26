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

import org.gatein.pc.controller.event.WindowEvent;
import org.gatein.pc.portlet.support.PortletInvokerSupport;
import org.gatein.pc.portlet.support.PortletSupport;
import org.gatein.pc.controller.request.ControllerRequest;
import org.gatein.pc.controller.event.AbstractEventControllerContext;
import org.gatein.pc.controller.response.ControllerResponse;
import org.gatein.pc.controller.response.PageUpdateResponse;
import org.gatein.pc.controller.response.PortletResponse;
import org.gatein.pc.controller.handlers.EventProducerActionHandler;
import org.gatein.pc.controller.handlers.FailingEventHandler;
import org.gatein.pc.controller.handlers.NoOpEventHandler;
import org.gatein.pc.api.PortletInvokerException;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class EventControllerContextFailureTestCase extends junit.framework.TestCase
{

   public static final String DST_WINDOW_ID = "/webappdst.dst";

   /** . */
   private PortletController controller = new PortletController();

   /** . */
   private PortletControllerContextSupport context = new PortletControllerContextSupport();

   /** . */
   private PortletInvokerSupport invoker = context.getInvoker();

   /** . */
   private ControllerRequest request;

   /** . */
   private QName srcName;

   /** . */
   private QName dstName;

   /** . */
   private PortletSupport fooPortlet;

   /** . */
   private PortletSupport barPortlet;

   /** . */
   private boolean called;

   @Override
   protected void setUp() throws Exception
   {
      request = context.createActionRequest(PortletInvokerSupport.FOO_PORTLET_ID);
      srcName = new QName("ns1", "src");
      dstName = new QName("ns2", "dst");
      fooPortlet = invoker.addPortlet(PortletInvokerSupport.FOO_PORTLET_ID);
      barPortlet = invoker.addPortlet(PortletInvokerSupport.BAR_PORTLET_ID);
      called = false;
   }

   public void testEventControllerContextFailsDuringEventConsumedCallback() throws PortletInvokerException
   {
      WiringEventControllerContext ecc = new WiringEventControllerContext()
      {
         public void eventConsumed(EventPhaseContext context, WindowEvent consumedEvent, PortletInvocationResponse consumerResponse)
         {
            called = true;
            throw new RuntimeException();
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, PortletInvokerSupport.BAR_PORTLET_ID);

      // We test that a runtime exception thrown does not interrrupt the flow of the controller
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      barPortlet.addHandler(new NoOpEventHandler());
      ControllerResponse response = controller.process(context, request);
      PageUpdateResponse updateResponse = (PageUpdateResponse)response;
      assertEquals(PortletResponse.DISTRIBUTION_DONE, updateResponse.getEventDistributionStatus());
      assertTrue(called);

      //
      called = false;
      final Error error = new Error();
      ecc = new WiringEventControllerContext()
      {
         public void eventConsumed(EventPhaseContext context, WindowEvent consumedEvent, PortletInvocationResponse consumerResponse)
         {
            called = true;
            throw error;
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, PortletInvokerSupport.BAR_PORTLET_ID);

      //
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      barPortlet.addHandler(new NoOpEventHandler());
      try
      {
         controller.process(context, request);
         fail();
      }
      catch (Error ignore)
      {
         assertSame(error, ignore);
         assertTrue(called);
      }
   }

   public void testEventControllerContextFailsDuringEventProducedCallback() throws PortletInvokerException
   {
      AbstractEventControllerContext ecc = new AbstractEventControllerContext()
      {
         public Iterable<WindowEvent> eventProduced(EventPhaseContext context, WindowEvent producedEvent, WindowEvent sourceEvent)
         {
            called = true;
            throw new RuntimeException();
         }
      };

      // We test that a runtime exception thrown does not interrrupt the flow of the controller
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      ControllerResponse response = controller.process(context, request);
      PageUpdateResponse updateResponse = (PageUpdateResponse)response;
      assertEquals(PortletResponse.DISTRIBUTION_DONE, updateResponse.getEventDistributionStatus());
      assertTrue(called);

      //
      called = false;
      final Error error = new Error();
      ecc = new AbstractEventControllerContext()
      {
         public Iterable<WindowEvent> eventProduced(EventPhaseContext context, WindowEvent producedEvent, WindowEvent sourceEvent)
         {
            called = true;
            throw error;
         }
      };

      // We test than an error thrown interrupts the flow of the controller
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      try
      {
         controller.process(context, request);
         fail();
      }
      catch (Error ignore)
      {
         assertSame(error, ignore);
         assertTrue(called);
      }
   }

   public void testEventControllerContextFailsDuringEventFailedCallback() throws PortletInvokerException
   {
      WiringEventControllerContext ecc = new WiringEventControllerContext()
      {
         public void eventFailed(EventPhaseContext context, WindowEvent failedEvent, Throwable throwable)
         {
            called = true;
            throw new RuntimeException();
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, PortletInvokerSupport.BAR_PORTLET_ID);

      // We test that a runtime exception thrown does not interrrupt the flow of the controller
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      barPortlet.addHandler(new FailingEventHandler(new RuntimeException()));
      ControllerResponse response = controller.process(context, request);
      PageUpdateResponse updateResponse = (PageUpdateResponse)response;
      assertEquals(PortletResponse.DISTRIBUTION_DONE, updateResponse.getEventDistributionStatus());
      assertTrue(called);

      //
      called = false;
      final Error error = new Error();
      ecc = new WiringEventControllerContext()
      {
         public void eventFailed(EventPhaseContext context, WindowEvent failedEvent, Throwable throwable)
         {
            called = true;
            throw error;
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, PortletInvokerSupport.BAR_PORTLET_ID);

      //
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      barPortlet.addHandler(new FailingEventHandler(new RuntimeException()));
      try
      {
         controller.process(context, request);
         fail();
      }
      catch (Error ignore)
      {
         assertSame(error, ignore);
         assertTrue(called);
      }
   }

   public void testEventControllerContextFailsDuringEventDiscardedCallback() throws PortletInvokerException
   {
      WiringEventControllerContext ecc = new WiringEventControllerContext()
      {
         public void eventDiscarded(EventPhaseContext context, WindowEvent discardedEvent, int cause)
         {
            assertEquals(EVENT_CONSUMER_INFO_NOT_AVAILABLE, cause);
            called = true;
            throw new RuntimeException();
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, DST_WINDOW_ID);

      // We test that a runtime exception thrown does not interrrupt the flow of the controller
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      ControllerResponse response = controller.process(context, request);
      PageUpdateResponse updateResponse = (PageUpdateResponse)response;
      assertEquals(PortletResponse.DISTRIBUTION_DONE, updateResponse.getEventDistributionStatus());
      assertTrue(called);

      //
      called = false;
      final Error error = new Error();
      ecc = new WiringEventControllerContext()
      {
         public void eventDiscarded(EventPhaseContext context, WindowEvent discardedEvent, int cause)
         {
            assertEquals(EVENT_CONSUMER_INFO_NOT_AVAILABLE, cause);
            called = true;
            throw error;
         }
      };
      ecc.createWire(srcName, PortletInvokerSupport.FOO_PORTLET_ID, dstName, DST_WINDOW_ID);

      //
      context.setEventControllerContext(ecc);
      fooPortlet.addHandler(new EventProducerActionHandler(srcName));
      try
      {
         controller.process(context, request);
         fail();
      }
      catch (Error ignore)
      {
         assertSame(error, ignore);
         assertTrue(called);
      }
   }
}
