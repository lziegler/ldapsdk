/*
 * Copyright 2020-2025 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2020-2025 Ping Identity Corporation
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
 * Copyright (C) 2020-2025 Ping Identity Corporation
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
import com.unboundid.ldap.sdk.JSONControlDecodeHelper;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.NotMutable;
import com.unboundid.util.NotNull;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.json.JSONField;
import com.unboundid.util.json.JSONObject;

import static com.unboundid.ldap.sdk.controls.ControlMessages.*;



/**
 * This class provides an implementation of the LDAP subentries request control
 * as defined in draft-ietf-ldup-subentry.  It may be included in a search
 * request to indicate that the entries with the {@code ldapSubentry} object
 * class should be included in the search results.
 * <BR><BR>
 * Entries containing the {@code ldapSubentry} object class are special in that
 * they are normally excluded from search results, unless the target entry is
 * requested with a base-level search.  They are used to store operational
 * information that controls how the server should behave rather than user data.
 * Because they do not hold user data, it is generally desirable to have them
 * excluded from search results, but for cases in which a client needs to
 * retrieve such an entry, then this subentries request control may be included
 * in the search request.  This control differs from the
 * {@link RFC3672SubentriesRequestControl} in that it will cause only entries
 * with the {@code ldapSubEntry} object class to be returned, while the
 * {@code RFC3672SubentriesRequestControl} may optionally return both regular
 * entries and subentries.
 * <BR><BR>
 * There is no corresponding response control.
 * <BR><BR>
 * <H2>Example</H2>
 * The following example illustrates the use of the subentries request control
 * to retrieve subentries that may not otherwise be returned.
 * <PRE>
 * // First, perform a search to retrieve an entry with a cn of "test subentry"
 * // but without including the subentries request control.  This should not
 * // return any matching entries.
 * SearchRequest searchRequest = new SearchRequest("dc=example,dc=com",
 *      SearchScope.SUB, Filter.createEqualityFilter("cn", "test subentry"));
 * SearchResult resultWithoutControl = connection.search(searchRequest);
 * LDAPTestUtils.assertResultCodeEquals(resultWithoutControl,
 *      ResultCode.SUCCESS);
 * LDAPTestUtils.assertEntriesReturnedEquals(resultWithoutControl, 0);
 *
 * // Update the search request to add a subentries request control so that
 * // subentries should be included in search results.  This should cause the
 * // subentry to be returned.
 * searchRequest.addControl(new DraftLDUPSubentriesRequestControl());
 * SearchResult resultWithControl = connection.search(searchRequest);
 * LDAPTestUtils.assertResultCodeEquals(resultWithControl, ResultCode.SUCCESS);
 * LDAPTestUtils.assertEntriesReturnedEquals(resultWithControl, 1);
 * </PRE>
 *
 * @see  RFC3672SubentriesRequestControl
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class DraftLDUPSubentriesRequestControl
       extends Control
{
  /**
   * The OID (1.3.6.1.4.1.7628.5.101.1) for the LDAP subentries request control.
   */
  @NotNull public static final String SUBENTRIES_REQUEST_OID =
       "1.3.6.1.4.1.7628.5.101.1";



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 4772130172594841481L;



  /**
   * Creates a new subentries request control.  it will not be marked critical.
   */
  public DraftLDUPSubentriesRequestControl()
  {
    this(false);
  }



  /**
   * Creates a new subentries request control with the specified criticality.
   *
   * @param  isCritical  Indicates whether this control should be marked
   *                     critical.
   */
  public DraftLDUPSubentriesRequestControl(final boolean isCritical)
  {
    super(SUBENTRIES_REQUEST_OID, isCritical, null);
  }



  /**
   * Creates a new subentries request control which is decoded from the provided
   * generic control.
   *
   * @param  control  The generic control to be decoded as a subentries request
   *                  control.
   *
   * @throws  LDAPException  If the provided control cannot be decoded as a
   *                         subentries request control.
   */
  public DraftLDUPSubentriesRequestControl(@NotNull final Control control)
         throws LDAPException
  {
    super(control);

    if (control.hasValue())
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
                              ERR_SUBENTRIES_HAS_VALUE.get());
    }
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NotNull()
  public String getControlName()
  {
    return INFO_CONTROL_NAME_SUBENTRIES_REQUEST.get();
  }



  /**
   * Retrieves a representation of this subentries control as a JSON object.
   * The JSON object uses the following fields (note that since this control
   * does not have a value, neither the {@code value-base64} nor
   * {@code value-json} fields may be present):
   * <UL>
   *   <LI>
   *     {@code oid} -- A mandatory string field whose value is the object
   *     identifier for this control.  For the subentries control, the OID is
   *     "1.3.6.1.4.1.7628.5.101.1".
   *   </LI>
   *   <LI>
   *     {@code control-name} -- An optional string field whose value is a
   *     human-readable name for this control.  This field is only intended for
   *     descriptive purposes, and when decoding a control, the {@code oid}
   *     field should be used to identify the type of control.
   *   </LI>
   *   <LI>
   *     {@code criticality} -- A mandatory Boolean field used to indicate
   *     whether this control is considered critical.
   *   </LI>
   * </UL>
   *
   * @return  A JSON object that contains a representation of this control.
   */
  @Override()
  @NotNull()
  public JSONObject toJSONControl()
  {
    return new JSONObject(
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_OID,
              SUBENTRIES_REQUEST_OID),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CONTROL_NAME,
              INFO_CONTROL_NAME_SUBENTRIES_REQUEST.get()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CRITICALITY,
              isCritical()));
  }



  /**
   * Attempts to decode the provided object as a JSON representation of an
   * subentries request control.
   *
   * @param  controlObject  The JSON object to be decoded.  It must not be
   *                        {@code null}.
   * @param  strict         Indicates whether to use strict mode when decoding
   *                        the provided JSON object.  If this is {@code true},
   *                        then this method will throw an exception if the
   *                        provided JSON object contains any unrecognized
   *                        fields.  If this is {@code false}, then unrecognized
   *                        fields will be ignored.
   *
   * @return  The subentries request control that was decoded from the provided
   *          JSON object.
   *
   * @throws  LDAPException  If the provided JSON object cannot be parsed as a
   *                         valid subentries request control.
   */
  @NotNull()
  public static DraftLDUPSubentriesRequestControl decodeJSONControl(
              @NotNull final JSONObject controlObject,
              final boolean strict)
         throws LDAPException
  {
    final JSONControlDecodeHelper jsonControl = new JSONControlDecodeHelper(
         controlObject, strict, false, false);

    return new DraftLDUPSubentriesRequestControl(jsonControl.getCriticality());
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NotNull final StringBuilder buffer)
  {
    buffer.append("DraftLDUPSubentriesRequestControl(isCritical=");
    buffer.append(isCritical());
    buffer.append(')');
  }
}
