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
package org.gatein.pc.test.portlet.jsr168.tck.dispatcher;

import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.web.AbstractUniversalTestPortlet;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.unit.actions.ServletServiceTestAction;
import org.gatein.pc.test.unit.web.UTP1;
import org.gatein.pc.test.unit.web.UTS1;
import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.protocol.response.Response;
import org.gatein.pc.test.unit.protocol.response.EndTestResponse;
import static org.gatein.pc.test.unit.Assert.assertNotNull;
import static org.gatein.pc.test.unit.Assert.assertEquals;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_124, Assertion.JSR168_125})
public class DispatchedRequest
{
   public DispatchedRequest(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getNamedDispatcher("UniversalServletA");
            assertNotNull(dispatcher);
            try
            {
               dispatcher.include(request, response);
               Object o = UTP1.local.get();
               assertEquals(Boolean.TRUE, o);
            }
            finally
            {
               UTP1.local.set(null);
            }
            return new EndTestResponse();
         }
      });

      seq.bindAction(0, UTS1.SERVICE_JOIN_POINT, new ServletServiceTestAction()
      {
         protected Response run(Servlet servlet, HttpServletRequest request, HttpServletResponse response, PortletTestContext context) throws ServletException, IOException
         {
            StringBuffer buffer = request.getRequestURL();
            UTP1.local.set(buffer == null ? Boolean.TRUE : Boolean.FALSE);
            return null;
         }
      });
   }
}
