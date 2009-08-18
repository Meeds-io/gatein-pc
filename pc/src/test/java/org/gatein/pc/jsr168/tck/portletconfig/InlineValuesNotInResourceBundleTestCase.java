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
package org.gatein.pc.jsr168.tck.portletconfig;

import static org.jboss.unit.api.Assert.assertEquals;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.gatein.pc.jsr168.tck.portletconfig.spec.InlineValuesNotInResourceBundlePortlet;
import org.gatein.pc.unit.Assertion;
import org.gatein.pc.unit.PortletTestCase;
import org.gatein.pc.unit.PortletTestContext;
import org.gatein.pc.unit.actions.PortletRenderTestAction;
import org.gatein.pc.unit.annotations.TestCase;
import org.gatein.pc.unit.base.AbstractUniversalTestPortlet;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_24})
public class InlineValuesNotInResourceBundleTestCase
{
   public InlineValuesNotInResourceBundleTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, InlineValuesNotInResourceBundlePortlet.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            PortletConfig cfg = ((AbstractUniversalTestPortlet)portlet).getPortletConfig();

            ResourceBundle bundle_en = cfg.getResourceBundle(Locale.ENGLISH);

            assertEquals(Locale.ENGLISH, bundle_en.getLocale());

            assertEquals("bar", bundle_en.getString("foo"));

            //These are not defined in bundle but inline in portelt.xml
            assertEquals("title", bundle_en.getString("javax.portlet.title"));
            assertEquals("short-title", bundle_en.getString("javax.portlet.short-title"));
            assertEquals("keywords", bundle_en.getString("javax.portlet.keywords"));
            return new EndTestResponse();
         }
      });
   }
}
