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

package org.gatein.pc.embed.setpublicrenderparameter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class SetPublicRenderParameterPortlet1 extends GenericPortlet
{

   /** . */
   static String[] foo;

   @Override
   public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException
   {
      response.setRenderParameter("foo", "foo_value");
   }

   @Override
   protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException
   {
      foo = request.getParameterValues("foo");
      PortletURL url = response.createActionURL();
      response.setContentType("text/html");
      PrintWriter writer = response.getWriter();
      writer.print("<a href='" + url + "' id='url'>render</a>");
      writer.close();
   }
}
