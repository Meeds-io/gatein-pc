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

package org.gatein.pc.embed.lifecycle;

import junit.framework.Assert;
import org.gatein.pc.embed.AbstractTestCase;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class LifeCycleTestCase extends AbstractTestCase
{

   @Deployment(name = "app", managed = false)
   public static WebArchive deployment()
   {
      return deployment(LifeCyclePortlet.class);
   }

   @ArquillianResource
   Deployer deployer;

   @Test
   @InSequence(0)
   @RunAsClient
   public void testBefore()
   {
      Assert.assertFalse(LifeCyclePortlet.started);
   }

   @Test
   @InSequence(1)
   @RunAsClient
   public void testDeploy()
   {
      deployer.deploy("app");
   }

   @Test
   @InSequence(2)
   @RunAsClient
   public void testDeployed()
   {
      Assert.assertTrue(LifeCyclePortlet.started);
   }

   @Test
   @InSequence(3)
   @RunAsClient
   public void testUndeploy()
   {
      deployer.undeploy("app");
   }

   @Test
   @InSequence(4)
   @RunAsClient
   public void testUndeployed()
   {
      Assert.assertFalse(LifeCyclePortlet.started);
   }
}
