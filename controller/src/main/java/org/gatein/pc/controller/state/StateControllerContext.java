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
package org.gatein.pc.controller.state;

import org.gatein.pc.controller.ControllerContext;

import java.util.Map;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public interface StateControllerContext
{

   /**
    * <p>Update the public navigational state of a portlet window. The interpretation of what should be updated is left up
    * to the implementor. An example of implementation would use the mapping between qname and name provided by the referenced
    * portlet info.</p>
    *
    * <p>The update argument values with a length of zero should be treated as removals.</p>
    *
    * @param controller the controller
    * @param page the page
    * @param portletWindowId the portlet window id
    * @param update the updates
    * @throws IllegalArgumentException if an argument is not valid
    * @throws IllegalStateException if the page state is read only
    */
   void updatePublicNavigationalState(
      ControllerContext controller,
      PageNavigationalState page,
      String portletWindowId,
      Map<String, String[]> update);

   /**
    * Obtain the public navigational state of a portlet window. The interpretation of what should be retrieved is left up
    * to the implementor. An example of implementation would use the mapping between qnames and name provided by the
    * referenced portlet info.
    *
    * @param controller the controller
    * @param page the page
    * @param windowId the portlet window id  @return the portlet public navigational state
    * @throws IllegalArgumentException if an argument is not valid
    */
   Map<String, String[]> getPublicWindowNavigationalState(
      ControllerContext controller,
      PageNavigationalState page,
      String windowId);
}
