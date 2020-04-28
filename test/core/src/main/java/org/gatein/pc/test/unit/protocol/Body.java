/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.gatein.pc.test.unit.protocol;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public abstract class Body implements Serializable
{
   public static class Raw extends Body
   {

      /** . */
      private byte[] bytes;

      public byte[] getBytes()
      {
         return bytes;
      }

      public void setBytes(byte[] bytes)
      {
         this.bytes = bytes;
      }
   }

   public static class Form extends Body
   {

      /** . */
      private Map<String, String[]> parameters = new HashMap<String, String[]>();

      public void addParameter(String name, String[] values)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         if (values == null)
         {
            throw new IllegalStateException();
         }
         for (String value : values)
         {
            if (value == null)
            {
               throw new IllegalStateException();
            }
         }
         parameters.put(name, values.clone());
      }

      public void removeParameter(String name)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         parameters.remove(name);
      }

      public Set getParameterNames()
      {
         return Collections.unmodifiableSet(parameters.keySet());
      }

      public String[] getParameterValues(String name)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         String[] values = parameters.get(name);
         return values != null ? values.clone() : null;
      }
   }
}
