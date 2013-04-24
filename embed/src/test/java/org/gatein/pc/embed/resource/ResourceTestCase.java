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

package org.gatein.pc.embed.resource;

import junit.framework.Assert;
import org.gatein.common.io.IOTools;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class ResourceTestCase extends AbstractTestCase
{

   @Deployment
   public static WebArchive deployment()
   {
      return deployment(ResourcePortlet.class);
   }

   @Drone
   WebDriver driver;

   @Test
   @RunAsClient
   @InSequence(0)
   public void init(@ArquillianResource URL deploymentURL) throws Exception
   {
      Assert.assertEquals(0, ResourcePortlet.count);
      URL url = deploymentURL.toURI().resolve("embed/ResourcePortlet").toURL();
      driver.get(url.toString());
   }

   @Test
   @RunAsClient
   @InSequence(1)
   public void updateNavigationalState() throws Exception
   {
      Assert.assertEquals(1, ResourcePortlet.count);
      driver.get(ResourcePortlet.navigationURL);
   }

   @Test
   @InSequence(2)
   public void testParams()
   {
      Assert.assertEquals(2, ResourcePortlet.count);
      Assert.assertEquals("foo_value", ResourcePortlet.foo);
      Assert.assertNull(ResourcePortlet.bar);
      Assert.assertNull("id_value", ResourcePortlet.resourceId);
   }

   @Test
   @RunAsClient
   @InSequence(3)
   public void testInteraction() throws Exception
   {
      WebElement link = driver.findElement(By.id("url"));
      URL url = new URL(link.getAttribute("href"));
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.connect();
      Assert.assertEquals(200, conn.getResponseCode());
      String s = new String(IOTools.getBytes(conn.getInputStream()));
      Assert.assertEquals("SERVE_RESOURCE", s);
      Map<String, String> headers = responseHeaders(conn);
      Assert.assertEquals("text/plain;charset=utf-8", headers.get("Content-Type"));
   }

   @Test
   @InSequence(4)
   public void testInvoked()
   {
      Assert.assertEquals(3, ResourcePortlet.count);
      Assert.assertEquals("foo_value", ResourcePortlet.foo);
      Assert.assertEquals("bar_value", ResourcePortlet.bar);
      Assert.assertEquals("id_value", ResourcePortlet.resourceId);
   }
}
