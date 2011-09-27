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
package org.gatein.pc.portlet.deployment;

import java.util.List;
import java.util.Locale;

import javax.xml.namespace.QName;

import org.gatein.pc.portlet.impl.metadata.PortletApplication20MetaData;
import org.gatein.pc.portlet.impl.metadata.event.EventDefinitionMetaData;
import org.gatein.pc.portlet.impl.metadata.event.EventDefinitionReferenceMetaData;
import org.gatein.pc.portlet.impl.metadata.portlet.PortletMetaData;

/**
 * @author <a href="mailto:emuckenh@redhat.com">Emanuel Muckenhuber</a>
 * @version $Revision$
 */
public class EventTestEverythingTestCase extends AbstractMetaDataTestCase
{

   public void _test01()
   {
      try
      {

         String xmlFile = "metadata/event/portlet-event1.xml";

         unmarshall(xmlFile);
         fail();
      }
      catch (Exception e)
      {
         // ok
      }
   }

   public void test02()
   {
      try
      {

         String xmlFile = "metadata/event/portlet-event2.xml";

         PortletApplication20MetaData md = unmarshall(xmlFile);
         assertNotNull(md);
         assertTrue(md instanceof PortletApplication20MetaData);
         assertEquals("2.0", md.getVersion());

         final List<EventDefinitionMetaData> eventDefinitions = md.getEvents();
         assertEquals(2, eventDefinitions.size());

         EventDefinitionMetaData emd = eventDefinitions.get(0);
         QName qname = emd.getQname();
         assertEquals("eventID", emd.getId());
         assertEquals("http://example.com/testEvents", qname.getNamespaceURI());
         assertEquals("portletEvent", qname.getLocalPart());
         assertEquals("x", qname.getPrefix());
         assertEquals("org.jboss.portal.event.invoke.refill.beer", emd.getValueType());
         assertEquals("descriptionDefaultLanguage", emd.getDescription().getDefaultString());
         assertEquals("descriptionDefaultLanguage", emd.getDescription().getString(new Locale("en"), false));
         assertEquals("Beschreibung in Deutsch", emd.getDescription().getString(new Locale("de"), false));

         EventDefinitionMetaData emd2 = eventDefinitions.get(1);
         
         assertNull(emd2.getQname());
         assertEquals("hellouh", emd2.getName());
         final List<QName> aliases = emd2.getAlias();
         assertEquals(2, aliases.size());
         assertEquals("hello", aliases.get(0).getLocalPart());
         assertEquals(new QName("http://example.com/testEvents", "helloWithNS"), aliases.get(1));

         // portlet event reference testing
         PortletMetaData p1 = md.getPortlet("Portlet2");
         QName referenceQName = new QName("http://example.com/testEvents", "portletEvent");
         assertNotNull(p1);
         assertEquals("org.jboss.portal.test.portlet.TestSessionPortlet", p1.getPortletClass());

         EventDefinitionReferenceMetaData ermd1 = p1.getSupportedProcessingEvent().get(0);
         assertNotNull(ermd1);
         assertEquals(referenceQName, ermd1.getQname());

         EventDefinitionReferenceMetaData ermd2 = p1.getSupportedProcessingEvent().get(1);
         assertNotNull(ermd2);
         assertEquals("hellouh", ermd2.getName());

         EventDefinitionReferenceMetaData ermd3 = p1.getSupportedPublishingEvent().get(0);
         assertNotNull(ermd3);
         assertEquals(referenceQName, ermd3.getQname());

         EventDefinitionReferenceMetaData ermd4 = p1.getSupportedPublishingEvent().get(1);
         assertNotNull(ermd4);
         assertEquals("hellouh", ermd4.getName());
         
      }
      catch (Exception e)
      {
         fail(e);
      }
   }

   // JULIEN : find a way to make that validated somehow
   public void _test021() throws Exception
   {
      // "Should fail: name and qname defined!"
      String xmlFile = "metadata/event/portlet-event2-fail.xml";
      unmarshall(xmlFile, true);
   }

   public void testFoo() throws Exception
   {
      String xmlFile = "metadata/event/portlet-event-resource-bundle.xml";
      PortletApplication20MetaData md = unmarshall(xmlFile);

      //
      assertNotNull(md);
      PortletMetaData portletMD = md.getPortlet("Portlet");
      assertNotNull(portletMD);
      assertEquals("bundle", portletMD.getResourceBundle());

      //
      assertEquals(null, md.getResourceBundle());
      List<EventDefinitionMetaData> events = md.getEvents();
      assertNotNull(events);
      assertEquals(1, events.size());
      EventDefinitionMetaData event = events.get(0);
      assertNotNull(event);
      assertEquals("event", event.getName());
      assertEquals("org.jboss.portal.event.invoke.refill.beer", event.getValueType());
   }
}
