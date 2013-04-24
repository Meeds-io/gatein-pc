/*
 * Copyright (C) 2012 eXo Platform SAS.
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

package org.gatein.pc.embed.actionsetparameter;

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
public class ActionSetParameterTestCase extends AbstractTestCase
{

   @Deployment
   public static WebArchive deployment()
   {
      return deployment(PORTLET_APP_PROLOG +
         "<portlet>" +
         "<portlet-name>" + ActionSetParameterPortlet.class.getSimpleName() + "</portlet-name>" +
         "<portlet-class>" + ActionSetParameterPortlet.class.getName() + "</portlet-class>" +
         "<supports>\n" +
         "<mime-type>text/html</mime-type>\n" +
         "<portlet-mode>VIEW</portlet-mode>\n" +
         "<portlet-mode>EDIT</portlet-mode>\n" +
         "</supports>\n" +
         "<portlet-info>" +
         "<title>" + ActionSetParameterPortlet.class.getSimpleName() + "</title>" +
         "</portlet-info>" +
         "</portlet>" +
         PORTLET_APP_EPILOG);
   }

   @Drone
   WebDriver driver;

   @Test
   @InSequence(0)
   public void init()
   {
      Assert.assertNull(ActionSetParameterPortlet.foo);
      Assert.assertNull(ActionSetParameterPortlet.portletMode);
      Assert.assertNull(ActionSetParameterPortlet.windowState);
   }

   @Test
   @RunAsClient
   @InSequence(1)
   public void display(@ArquillianResource URL deploymentURL) throws Exception
   {
      URL url = renderURL(deploymentURL, ActionSetParameterPortlet.class);
      driver.get(url.toString());
   }

   @Test
   @InSequence(2)
   public void testAfterDisplay()
   {
      Assert.assertNull(ActionSetParameterPortlet.foo);
      Assert.assertEquals(PortletMode.VIEW, ActionSetParameterPortlet.portletMode);
      Assert.assertEquals(WindowState.NORMAL, ActionSetParameterPortlet.windowState);
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
   @InSequence(4)
   public void testInvoked()
   {
      Assert.assertNotNull(ActionSetParameterPortlet.foo);
      Assert.assertEquals(Collections.singletonList("foo_value"), Arrays.asList(ActionSetParameterPortlet.foo));
      Assert.assertEquals(PortletMode.EDIT, ActionSetParameterPortlet.portletMode);
      Assert.assertEquals(WindowState.MAXIMIZED, ActionSetParameterPortlet.windowState);
   }
}
