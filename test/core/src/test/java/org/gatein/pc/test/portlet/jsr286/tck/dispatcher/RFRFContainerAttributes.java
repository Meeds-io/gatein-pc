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
package org.gatein.pc.test.portlet.jsr286.tck.dispatcher;

import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.api.LifeCyclePhase;

import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletContext;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({Assertion.JSR286_239})
public class RFRFContainerAttributes extends DispatchedContainerAttributes
{

   private static Map<String, String> buildAttributes(ServletContext ctx)
   {
      Map<String, String> map = new HashMap<String, String>();
      map.put("jakarta.servlet.include.request_uri", null);
      map.put("jakarta.servlet.include.context_path", null);
      map.put("jakarta.servlet.include.servlet_path", null);
      map.put("jakarta.servlet.include.path_info", null);
      map.put("jakarta.servlet.include.query_string", null);
      map.put("jakarta.servlet.forward.request_uri", ctx.getContextPath() + "/requestForwardHopServlet/pathinfo1");
      map.put("jakarta.servlet.forward.context_path", ctx.getContextPath());
      map.put("jakarta.servlet.forward.servlet_path", "/requestForwardHopServlet");
      map.put("jakarta.servlet.forward.path_info", "/pathinfo1");
      map.put("jakarta.servlet.forward.query_string", "foo1=bar1");
      return Collections.unmodifiableMap(map);
   }

   private static Map<String, String> buildInfos(ServletContext ctx)
   {
      Map<String, String> map = new HashMap<String, String>();
      map.put("request_uri", ctx.getContextPath() + "/requestForwardHopServlet/pathinfo1");
      map.put("context_path", ctx.getContextPath());
      map.put("servlet_path", "/requestForwardHopServlet");
      map.put("path_info", "/pathinfo1");
      map.put("query_string", "foo1=bar1");
      return Collections.unmodifiableMap(map);
   }

   public RFRFContainerAttributes(PortletTestCase seq)
   {
      super(seq, buildAttributes(seq.getContext()), buildInfos(seq.getContext()));
   }

   protected boolean performTest(LifeCyclePhase phase)
   {
      return phase != LifeCyclePhase.ACTION && phase != LifeCyclePhase.EVENT;
   }

   protected void dispatch(PortletRequest request, PortletResponse response, PortletContext portletContext) throws IOException, PortletException
   {
      PortletRequestDispatcher dispatcher = portletContext.getRequestDispatcher("/requestForwardHopServlet/pathinfo1?foo1=bar1");
      dispatcher.forward(request, response);
   }
}