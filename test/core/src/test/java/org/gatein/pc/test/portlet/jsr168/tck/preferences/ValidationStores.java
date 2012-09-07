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
package org.gatein.pc.test.portlet.jsr168.tck.preferences;

import org.gatein.pc.test.unit.PortletTestCase;
import org.gatein.pc.test.unit.PortletTestContext;
import org.gatein.pc.test.unit.actions.PortletRenderTestAction;
import org.gatein.pc.test.unit.actions.PortletActionTestAction;
import org.gatein.pc.test.unit.web.UTP8;
import org.gatein.pc.test.portlet.jsr168.tck.preferences.spec.ValidationStoresValidator;
import org.gatein.pc.test.unit.annotations.TestCase;
import org.gatein.pc.test.unit.Assertion;
import org.gatein.pc.test.unit.protocol.response.Response;
import org.gatein.pc.test.unit.protocol.response.EndTestResponse;
import org.gatein.pc.test.unit.protocol.response.InvokeGetResponse;
import static org.gatein.pc.test.unit.Assert.assertEquals;
import static org.gatein.pc.test.unit.Assert.assertTrue;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.PortletPreferences;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_103, Assertion.JSR168_105})
public class ValidationStores
{
   public ValidationStores(PortletTestCase seq)
   {
      seq.bindAction(0, UTP8.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            return new InvokeGetResponse(response.createActionURL().toString());
         }
      });

      seq.bindAction(1, UTP8.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws ReadOnlyException, IOException, ValidatorException
         {
            // Get prefs
            PortletPreferences prefs = request.getPreferences();

            // Test the initial value is ok
            assertEquals("single_pref_value", prefs.getValue("single_pref", "other"));
            assertEquals(new String[]{"multi_pref_value_1", "multi_pref_value_2"},
               prefs.getValues("multi_pref", new String[]{"other"}));

            // Set with new values
            prefs.setValue("single_pref", "new_single_pref");
            prefs.setValues("multi_pref", new String[]{"new_multi_pref_value_1", "new_multi_pref_value_2"});

            // Test with new values
            assertEquals("new_single_pref", prefs.getValue("single_pref", "other"));
            assertEquals(new String[]{"new_multi_pref_value_1", "new_multi_pref_value_2"}, prefs.getValues("multi_pref", new String[]{"other"}));

            // Trigger store
            prefs.store();

            // Check the validator has been invoked
            System.out.println("--> " +  ValidationStoresValidator.class.getClassLoader());
            assertTrue(ValidationStoresValidator.isValidateInvoked());

            // Test with new values after store
            assertEquals("new_single_pref", prefs.getValue("single_pref", "other"));
            assertEquals(new String[]{"new_multi_pref_value_1", "new_multi_pref_value_2"}, prefs.getValues("multi_pref", new String[]{"other"}));
         }
      });

      seq.bindAction(1, UTP8.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            return new InvokeGetResponse(response.createActionURL().toString());
         }
      });

      seq.bindAction(2, UTP8.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context)
         {
            // Get prefs
            PortletPreferences prefs = request.getPreferences();

            // Test with new values
            assertEquals("new_single_pref", prefs.getValue("single_pref", "other"));
            assertEquals(new String[]{"new_multi_pref_value_1", "new_multi_pref_value_2"}, prefs.getValues("multi_pref", new String[]{"other"}));
         }
      });

      seq.bindAction(2, UTP8.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected Response run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            return new EndTestResponse();
         }
      });
   }
}
