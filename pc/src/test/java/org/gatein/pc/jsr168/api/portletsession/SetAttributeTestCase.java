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
package org.gatein.pc.jsr168.api.portletsession;

import static org.jboss.unit.api.Assert.fail;

import javax.portlet.Portlet;
import javax.portlet.PortletSession;
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
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.API286_PORTLET_SESSION_14})
public class SetAttributeTestCase
{
   public SetAttributeTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            PortletSession session = request.getPortletSession();

            try
            {
               session.setAttribute(null, new Object());
               fail();
            }
            catch (IllegalArgumentException expected)
            {
            }

            try
            {
               session.setAttribute(null, new Object(), PortletSession.APPLICATION_SCOPE);
               fail();
            }
            catch (IllegalArgumentException expected)
            {
            }

            //before invalidation
            session.setAttribute("name", new Object());
            session.setAttribute("name", new Object(), PortletSession.APPLICATION_SCOPE);

            session.invalidate();

            //after invalidation
            try
            {
               session.setAttribute("name", new Object());
               fail();
            }
            catch (IllegalStateException expected)
            {
            }

            try
            {
               session.setAttribute("name", new Object(), PortletSession.APPLICATION_SCOPE);
               fail();
            }
            catch (IllegalStateException expected)
            {
            }

            return new EndTestResponse();
         }
      });
   }
}
