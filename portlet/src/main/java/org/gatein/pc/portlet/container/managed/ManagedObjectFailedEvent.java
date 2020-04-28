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
package org.gatein.pc.portlet.container.managed;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public final class ManagedObjectFailedEvent extends ManagedObjectEvent
{

   /** . */
   private final LifeCycleStatus status;

   public ManagedObjectFailedEvent(ManagedObject managedObject, LifeCycleStatus status)
   {
      super(managedObject);

      //
      this.status = status;
   }

   public LifeCycleStatus getStatus()
   {
      return status;
   }

   @Override
   public String toString()
   {
      return getClass().getSimpleName() + "[status=" + status + ",managed=" + managedObject + "]";
   }
}
