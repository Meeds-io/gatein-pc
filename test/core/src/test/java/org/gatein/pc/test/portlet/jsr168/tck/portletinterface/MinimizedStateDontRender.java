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
package org.gatein.pc.test.portlet.jsr168.tck.portletinterface;

import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.JoinPointType;
import org.gatein.pc.test.unit.JoinPoint;
import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.actions.PortletActionTestAction;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.portlet.jsr168.tck.portletinterface.spec.MinimizedStateDontRenderPortlet;
import org.gatein.pc.test.unit.protocol.response.Response;

import javax.portlet.Portlet;
import javax.portlet.ActionRequest;
import javax.portlet.PortletException;
import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase
public class MinimizedStateDontRender
{
   public MinimizedStateDontRender(PortletTestCase seq)
   {
      seq.bindAction(new JoinPoint(MinimizedStateDontRenderPortlet.NAME, JoinPointType.PORTLET_ACTION), new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws PortletException, IOException
         {
            throw new AssertionError();
         }
      });
      seq.bindAction(new JoinPoint(MinimizedStateDontRenderPortlet.NAME, JoinPointType.PORTLET_RENDER), new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            throw new AssertionError();
         }
      });
   }
}
