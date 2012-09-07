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
package org.gatein.pc.test.portlet.jsr286.tck.resourceserving;

import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.unit.actions.PortletResourceTestAction;
import org.gatein.pc.test.unit.web.UTP1;
import org.gatein.pc.test.unit.protocol.response.Response;
import org.gatein.pc.test.unit.protocol.response.EndTestResponse;
import org.gatein.pc.test.unit.protocol.response.InvokeGetResponse;
import static org.gatein.pc.test.unit.Assert.*;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.Serializable;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({Assertion.JSR286_123,Assertion.JSR286_125})
public class ResourceServing
{

   private String blah;

   public ResourceServing(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            blah = response.createRenderURL().toString();

            ResourceURL resourceURL = response.createResourceURL();
            resourceURL.setResourceID("blah");
            resourceURL.setParameter("foo", "bar");
            String s = resourceURL.toString();
            return new InvokeGetResponse(s);
         }
      });
      seq.bindAction(1, UTP1.RESOURCE_JOIN_POINT, new PortletResourceTestAction()
      {
         protected Response run(Portlet portlet, ResourceRequest request, ResourceResponse response, PortletTestContext context) throws PortletException, IOException
         {
            assertEquals("blah", request.getResourceID());
            assertEquals("bar", request.getParameter("foo"));
            assertEquals(ResourceURL.PAGE, request.getCacheability());

            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.print("foo");
            writer.close();

            return new InvokeGetResponse(blah);
         }
      });
      seq.bindAction(2, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {

            byte[] bytes = (byte[])context.getPayload().get("http.response.body");
            System.out.println("bytes.length = " + bytes.length);

            for (Map.Entry<String, Serializable> entry : context.getPayload().entrySet())
            {


               System.out.println("entry.getKey() = " + entry.getKey());
               System.out.println("entry.getValue() = " + entry.getValue());
            }

            return new EndTestResponse();
         }
      });
   }

//   entry.getKey() = http.response.body
//   entry.getValue() = [B@bdcd96
//   entry.getKey() = http.response.headers
//   entry.getValue() = Server: Apache-Coyote/1.1
//   Content-Length: 0
//   Date: Tue,22 Jan 2008 01:23:43 GMT
//


}
