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
package org.gatein.pc.jsr168.ext.session;

import static org.jboss.unit.api.Assert.assertEquals;
import static org.jboss.unit.api.Assert.assertNotNull;
import static org.jboss.unit.api.Assert.assertTrue;

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletSessionUtil;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.gatein.pc.framework.UTP1;
import org.gatein.pc.unit.Assertion;
import org.gatein.pc.unit.PortletTestCase;
import org.gatein.pc.unit.PortletTestContext;
import org.gatein.pc.unit.actions.PortletRenderTestAction;
import org.gatein.pc.unit.annotations.TestCase;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({
   Assertion.EXT_SESSION_4
   })
@SuppressWarnings("unchecked")
public class PortletScopedAttributesAreApplicationScopedAttributesTestCase
{

   public PortletScopedAttributesAreApplicationScopedAttributesTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            PortletSession session = request.getPortletSession();
            session.setAttribute("foo", "bar");
            Enumeration e = session.getAttributeNames(PortletSession.APPLICATION_SCOPE);
            assertNotNull(e);
            assertTrue(e.hasMoreElements());
            String scopedFoo = (String)e.nextElement();
            assertNotNull(scopedFoo);
            assertEquals("foo", PortletSessionUtil.decodeAttributeName(scopedFoo));
            assertEquals(PortletSession.PORTLET_SCOPE, PortletSessionUtil.decodeScope(scopedFoo));
            Object bar = session.getAttribute(scopedFoo, PortletSession.APPLICATION_SCOPE);
            assertEquals("bar", bar);
            return new EndTestResponse();
         }
      });
   }
}
