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
package org.gatein.pc.test.unit;

import org.gatein.pc.test.PortletUtilsTestCase;
import org.gatein.pc.test.StringCodec;
import org.gatein.pc.test.StringCodecTestCase;
import org.gatein.pc.test.TestPortletApplicationDeployer;
import org.gatein.pc.test.unit.protocol.Conversation;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;

import java.net.URL;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class AbstractEarTestCase
{

   public static EnterpriseArchive createDeployment(String[]... deployments) throws Exception
   {
      EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class);

      // So it's not empty
      ear.addManifest();

      // Add portal
      ear.addAsModule(AbstractWarTestCase.createDeployment());

      // Add deployments
      for (String[] deployment : deployments)
      {
         WebArchive war = AbstractWarTestCase.createDeployment(deployment[0], deployment[1], deployment[2]);
         ear.addAsModule(war);
      }

      // Add this maven module classes
      JavaArchive classes = ShrinkWrap.create(JavaArchive.class);
      classes.addClasses(
         PortalTestServlet.class,
         PortletTestServlet.class,
         PortletUtilsTestCase.class,
         StringCodec.class,
         StringCodecTestCase.class,
         TestPortletApplicationDeployer.class
      );
      classes.addPackages(
         true,
         "org.gatein.pc.test.bootstrap",
         "org.gatein.pc.test.controller",
         "org.gatein.pc.test.portlet.framework",
         "org.gatein.pc.test.tck",
         "org.gatein.pc.test.unit",
         "org.gatein.pc.test.url",
         "org.jboss.unit");

      //
      ear.addAsLibrary(classes);

      // Add dependencies
      // THIS IS COUPLED TO JBOSS7 BUT IT IS OK FOR NOW
      // AS ONLY JBOSS7 TESTING USES EAR
      ear.addAsLibraries(Maven.resolver().loadPomFromFile("../dependencies/pom.xml")
              .importRuntimeAndTestDependencies().resolve().withTransitivity().asFile());

      //
      return ear;
   }

   /** . */
   private final String version;

   /** . */
   private final String type;

   /** . */
   private final String suite;

   /** . */
   private final String name;

   protected AbstractEarTestCase(String version, String type, String suite, String name)
   {
      this.version = version;
      this.type = type;
      this.suite = suite;
      this.name = name;
   }

   @Test
   public final void test() throws Exception
   {
      new Conversation(getBaseURL(), name).performInteractions();
   }

   /**
    * Implemented to return the base URL of the deployment.
    *
    * @return the base URL
    */
   protected abstract URL getBaseURL();
}
