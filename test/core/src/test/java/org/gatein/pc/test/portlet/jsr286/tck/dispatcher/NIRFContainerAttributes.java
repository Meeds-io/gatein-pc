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
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({Assertion.JSR286_239})
public class NIRFContainerAttributes extends DispatchedContainerAttributes
{

   private static Map<String, String> buildAttributes()
   {
      Map<String, String> map = new HashMap<String, String>();
      map.put("javax.servlet.include.request_uri", null);
      map.put("javax.servlet.include.context_path", null);
      map.put("javax.servlet.include.servlet_path", null);
      map.put("javax.servlet.include.path_info", null);
      map.put("javax.servlet.include.query_string", null);
      map.put("javax.servlet.forward.request_uri", null);
      map.put("javax.servlet.forward.context_path", null);
      map.put("javax.servlet.forward.servlet_path", null);
      map.put("javax.servlet.forward.path_info", null);
      map.put("javax.servlet.forward.query_string", null);
      return Collections.unmodifiableMap(map);
   }

   private static Map<String, String> buildInfo(ServletContext ctx)
   {
      Map<String, String> map = new HashMap<String, String>();
      map.put("request_uri", null);
      map.put("context_path", ctx.getContextPath());
      map.put("servlet_path", null);
      map.put("path_info", null);
      map.put("query_string", null);
      return Collections.unmodifiableMap(map);
   }

   public NIRFContainerAttributes(PortletTestCase seq)
   {
      super(seq, buildAttributes(), buildInfo(seq.getContext()));
   }

   protected boolean performTest(LifeCyclePhase phase)
   {
      return phase != LifeCyclePhase.ACTION && phase != LifeCyclePhase.EVENT;
   }

   protected void dispatch(PortletRequest request, PortletResponse response, PortletContext portletContext) throws IOException, PortletException
   {
      PortletRequestDispatcher dispatcher = portletContext.getNamedDispatcher("RequestForwardHopServlet");
      dispatcher.include(request, response);
   }
}