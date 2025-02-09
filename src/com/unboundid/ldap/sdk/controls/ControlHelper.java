/*
 * Copyright 2007-2025 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2007-2025 Ping Identity Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2007-2025 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk.controls;



import com.unboundid.ldap.sdk.Control;
import com.unboundid.util.InternalUseOnly;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * This class provides a helper class that may be used to ensure that all of the
 * "out-of-the-box" response controls supported by this SDK are properly
 * registered so that they can be properly instantiated when received in a
 * response from the directory server.
 */
@InternalUseOnly()
@ThreadSafety(level=ThreadSafetyLevel.NOT_THREADSAFE)
public final class ControlHelper
{
  /**
   * Prevent this class from being instantiated.
   */
  private ControlHelper()
  {
    // No implementation is required.
  }



  /**
   * Registers all "out-of-the-box" response controls provided with this SDK so
   * that they may be properly decoded if they are received in an LDAP response.
   * This method is intended only for internal use only and should not be called
   * by external applications.
   */
  @InternalUseOnly()
  public static void registerDefaultResponseControls()
  {
    final String packageNameDot = "com.unboundid.ldap.sdk.controls.";
    Control.registerDecodeableControl("2.16.840.1.113730.3.4.15",
         packageNameDot + "AuthorizationIdentityResponseControl");

    Control.registerDecodeableControl("1.3.6.1.4.1.4203.1.9.1.3",
         packageNameDot + "ContentSyncDoneControl");

    Control.registerDecodeableControl("1.3.6.1.4.1.4203.1.9.1.2",
         packageNameDot + "ContentSyncStateControl");

    Control.registerDecodeableControl("2.16.840.1.113730.3.4.7",
         packageNameDot + "EntryChangeNotificationControl");

    Control.registerDecodeableControl("2.16.840.1.113730.3.4.4",
         packageNameDot + "PasswordExpiredControl");

    Control.registerDecodeableControl("2.16.840.1.113730.3.4.5",
         packageNameDot + "PasswordExpiringControl");

    Control.registerDecodeableControl("1.3.6.1.1.13.2",
         packageNameDot + "PostReadResponseControl");

    Control.registerDecodeableControl("1.3.6.1.1.13.1",
         packageNameDot + "PreReadResponseControl");

    Control.registerDecodeableControl("1.2.840.113556.1.4.474",
         packageNameDot + "ServerSideSortResponseControl");

    Control.registerDecodeableControl("1.2.840.113556.1.4.319",
         packageNameDot + "SimplePagedResultsControl");

    Control.registerDecodeableControl("2.16.840.1.113730.3.4.10",
         packageNameDot + "VirtualListViewResponseControl");
  }
}
