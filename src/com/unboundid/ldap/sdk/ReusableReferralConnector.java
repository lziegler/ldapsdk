/*
 * Copyright 2023-2024 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2023-2024 Ping Identity Corporation
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
 * Copyright (C) 2023-2024 Ping Identity Corporation
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
package com.unboundid.ldap.sdk;



import com.unboundid.util.Extensible;
import com.unboundid.util.NotNull;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * This interface defines an API that may be used to obtain a
 * {@link FullLDAPInterface} (e.g., a connection pool) that may be used for the
 * purpose of following a referral.  When configured with a
 * {@code ReusableReferralConnector} rather than a base
 * {@link ReferralConnector}, one of the methods in this class will be used in
 * preference to the {@link ReferralConnector#getReferralConnection} method.
 */
@Extensible()
@ThreadSafety(level=ThreadSafetyLevel.INTERFACE_THREADSAFE)
public interface ReusableReferralConnector
       extends ReferralConnector
{
  /**
   * Retrieves a {@link FullLDAPInterface} for use in following a referral
   * returned in the provided result.  The caller must not do anything to
   * attempt to leave the interface in an unusable state (e.g., closing a
   * connection or connection pool).
   *
   * @param  referralURL  The LDAP URL for the referral to follow.  It must not
   *                      be {@code null}.
   * @param  connection   The connection on which the referral was received.  It
   *                      will not be {@code null}.
   *
   * @return  A {@link FullLDAPInterface} for use in following a referral with
   *          the given URL.
   *
   * @throws  LDAPException  If a problem occurs while obtaining the
   *                         {@code FullLDAPInterface} to use for following the
   *                         referral.
   */
  @NotNull()
  FullLDAPInterface getReferralInterface(@NotNull LDAPURL referralURL,
                                         @NotNull LDAPConnection connection)
       throws LDAPException;
}
