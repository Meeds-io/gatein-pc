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

package org.gatein.pc.portlet.impl.jsr286.taglib;

import org.gatein.pc.portlet.impl.jsr168.taglib.GenerateURLTagTEI;

import jakarta.servlet.jsp.tagext.TagData;

/**
 * @author Boleslaw Dawidowicz
 * @version : 0.1 $
 */
public class GenerateURL286TagTEI extends GenerateURLTagTEI
{

   public boolean isValid(TagData data)
   {
      return true;
//      if (super.isValid(data) &&
//         isEscapeXmlValid(data) &&
//         isCopyCurrentRenderParameters(data))
//      {
//         return true;
//      }
      //return false;
   }

   public boolean isEscapeXmlValid(TagData data)
   {
      Object o = data.getAttribute("escapeXml");
      if (o != null && o != TagData.REQUEST_TIME_VALUE)
      {
         String s = (String)o;
         if (s.toLowerCase().equals("true") ||
            s.toLowerCase().equals("false"))
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return true;
      }
   }

   public boolean isCopyCurrentRenderParameters(TagData data)
   {
      Object o = data.getAttribute("copyCurrentRenderParameters");
      if (o != null && o != TagData.REQUEST_TIME_VALUE)
      {
         String s = (String)o;
         if (s.toLowerCase().equals("true") ||
            s.toLowerCase().equals("false"))
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return true;
      }
   }


}
