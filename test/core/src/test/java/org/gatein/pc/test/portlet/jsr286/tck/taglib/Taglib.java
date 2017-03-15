/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

package org.gatein.pc.test.portlet.jsr286.tck.taglib;

import org.gatein.pc.api.ParametersStateString;
import org.gatein.pc.api.invocation.PortletInvocation;
import org.gatein.pc.portlet.aspects.ContextDispatcherInterceptor;
import org.gatein.pc.test.unit.PortletTestContext;

import javax.portlet.*;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.gatein.pc.test.unit.Assert.assertEquals;


/**
 * @author <a href="mailto:boleslaw dot dawidowicz at redhat anotherdot com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
public abstract class Taglib
{

   protected String startTag;
   protected String endTag;
   protected String expectedResult;
   protected String[] expectedResults;


   protected void include(PortletRequestDispatcher dispatcher, RenderRequest request, RenderResponse response) throws IOException, PortletException
   {
      startTag = "<div id=" + response.getNamespace() + ">";
      endTag = "</div>";

      Writer writer = response.getWriter();

      writer.write(startTag);
      dispatcher.include(request, response);
      writer.write(endTag);
   }

   protected void assertResult(PortletTestContext context)
   {
      String prevResponse = new String(context.getResponseBody());

      String portletResp = prevResponse.substring(prevResponse.indexOf(startTag) + startTag.length(), prevResponse.indexOf(endTag));

      assertEquals(expectedResult, portletResp.trim());
   }

   protected void assertResults(PortletTestContext context) {
      String prevResponse = new String(context.getResponseBody());

      String portletResp = prevResponse.substring(prevResponse.indexOf(startTag) + startTag.length(), prevResponse.indexOf(endTag));

      String[] results = portletResp.split("<test_result_separator/>");

      for (int i = 0; i < results.length; i++) {
         // Trim all results
         results[i] = results[i].trim();
         // check base url equality
         int indexExpectedUrl = expectedResults[i].indexOf("?");
         String expectedBaseUrl = expectedResults[i].substring(0, indexExpectedUrl);
         int indexResultUrl = results[i].indexOf("?");
         String resultBaseUrl = results[i].substring(0, indexResultUrl);
         assertEquals(expectedBaseUrl, resultBaseUrl);
         // Convert query params as maps
         Map<String, String> expectedParams = extractParams(expectedResults[i].substring(indexExpectedUrl));
         Map<String, String> resultParams = extractParams(results[i].substring(indexResultUrl));
         assertEquals(expectedParams.size(), resultParams.size());
         // Compare each query params
         for (String key : resultParams.keySet()) {
            if (key.equals("interactionstate")) {
               // for the param interactionstate, the value must be decoded to get the list of paramaters
               // and the maps must be compared. Paramaters are not necessary in the same order in the map,
               // so the string values can be different whereas the maps are equal
               Map<String, String[]> expectedInteractionParams = ((ParametersStateString) ParametersStateString.create(expectedParams.get(key))).getParameters();
               Map<String, String[]> resultInteractionParams = ((ParametersStateString) ParametersStateString.create(resultParams.get(key))).getParameters();
               assertEquals(expectedInteractionParams.size(), resultInteractionParams.size());
               expectedInteractionParams.keySet().stream().forEach(
                       expectedKey -> Arrays.equals(expectedInteractionParams.get(expectedKey), resultInteractionParams.get(expectedKey))
               );
            } else {
               assertEquals(expectedParams.get(key), resultParams.get(key));
            }
         }
      }
   }

   private Map<String, String> extractParams(String queryParams) {
      Map<String, String> paramsMap = new HashMap<>();
      String[] params = queryParams.split("&amp;");
      if(params.length == 1) {
         params = queryParams.split("&");
      }
      for(String param : params) {
         String[] splittedParam = param.split("=");
         paramsMap.put(splittedParam[0], splittedParam[1]);
      }
      return paramsMap;
   }

   protected PortletInvocation getInvocation(PortletRequest req)
   {
      // Get the invocation
      return (PortletInvocation)req.getAttribute(ContextDispatcherInterceptor.REQ_ATT_COMPONENT_INVOCATION);
   }
}
