/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.gatein.pc.embed.eventsetparameter;

import junit.framework.Assert;
import org.gatein.pc.embed.AbstractTestCase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class EventSetParameterTestCase extends AbstractTestCase
{
   @ArquillianResource
   URL deploymentURL;

   @Deployment
   public static WebArchive deployment()
   {
      return deployment(PORTLET_APP_PROLOG +
         "<portlet>" +
         "<portlet-name>" + EventSetParameterPortlet.class.getSimpleName() + "</portlet-name>" +
         "<portlet-class>" + EventSetParameterPortlet.class.getName() + "</portlet-class>" +
         "<supports>\n" +
         "<mime-type>text/html</mime-type>\n" +
         "<portlet-mode>VIEW</portlet-mode>\n" +
         "<portlet-mode>EDIT</portlet-mode>\n" +
         "</supports>\n" +
         "<portlet-info>" +
         "<title>" + EventSetParameterPortlet.class.getSimpleName() + "</title>" +
         "</portlet-info>" +
         "<supported-processing-event>" +
         "<qname>foo</qname>" +
         "</supported-processing-event>" +
         "</portlet>" +
         "<event-definition>" +
         "<qname>foo</qname>" +
         "<value-type>java.lang.String</value-type>" +
         "</event-definition>" +
         PORTLET_APP_EPILOG);
   }

   @Drone
   WebDriver driver;

   @Test
   @RunAsClient
   @InSequence(0)
   public void init()
   {
      Assert.assertNull(EventSetParameterPortlet.foo);
      Assert.assertNull(EventSetParameterPortlet.portletMode);
      Assert.assertNull(EventSetParameterPortlet.windowState);
   }

   @Test
   @RunAsClient
   @InSequence(1)
   public void display() throws Exception
   {
      URL url = renderURL(deploymentURL, EventSetParameterPortlet.class);
      driver.get(url.toString());
   }

   @Test
   @RunAsClient
   @InSequence(2)
   public void testAfterDisplay()
   {
      Assert.assertNull(EventSetParameterPortlet.foo);
      Assert.assertEquals(PortletMode.VIEW, EventSetParameterPortlet.portletMode);
      Assert.assertEquals(WindowState.NORMAL, EventSetParameterPortlet.windowState);
   }

   @Test
   @RunAsClient
   @InSequence(3)
   public void testInteraction() throws Exception
   {
      WebElement link = driver.findElement(By.id("url"));
      link.click();
   }

   @Test
   @RunAsClient
   @InSequence(4)
   public void testInvoked()
   {
      Assert.assertNotNull(EventSetParameterPortlet.foo);
      Assert.assertEquals(Collections.singletonList("foo_value"), Arrays.asList(EventSetParameterPortlet.foo));
      Assert.assertEquals(PortletMode.EDIT, EventSetParameterPortlet.portletMode);
      Assert.assertEquals(WindowState.MAXIMIZED, EventSetParameterPortlet.windowState);
   }
}
