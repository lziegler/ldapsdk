/*
 * Copyright 2015-2025 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2015-2025 Ping Identity Corporation
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
 * Copyright (C) 2015-2025 Ping Identity Corporation
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
package com.unboundid.ldap.sdk.unboundidds.controls;



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

import static com.unboundid.ldap.sdk.unboundidds.controls.ControlMessages.*;



/**
 * This class provides an implementation of the name with entryUUID request
 * control.  It may be included in an add request to indicate that the server
 * should replace the provided RDN with the server-generated entryUUID value.
 * It will also cause the server to include a
 * {@link com.unboundid.ldap.sdk.controls.PostReadResponseControl} in
 * the add result to make the generated DN available to the client.  If the
 * request already includes a
 * {@link com.unboundid.ldap.sdk.controls.PostReadRequestControl}, then the
 * attributes included in the post-read response control will be generated from
 * that request control.  Otherwise, the server will behave as if the request
 * had included a post-read request control requesting only the entryUUID
 * attribute.
 * <BR>
 * <BLOCKQUOTE>
 *   <B>NOTE:</B>  This class, and other classes within the
 *   {@code com.unboundid.ldap.sdk.unboundidds} package structure, are only
 *   supported for use against Ping Identity, UnboundID, and
 *   Nokia/Alcatel-Lucent 8661 server products.  These classes provide support
 *   for proprietary functionality or for external specifications that are not
 *   considered stable or mature enough to be guaranteed to work in an
 *   interoperable way with other types of LDAP servers.
 * </BLOCKQUOTE>
 * <BR>
 * This control has an OID of "1.3.6.1.4.1.30221.2.5.44".  It is recommended
 * that it be used with a criticality of {@code true}.  It does not take a
 * value.
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class NameWithEntryUUIDRequestControl
       extends Control
{
  /**
   * The OID (1.3.6.1.4.1.30221.2.5.44) for the name with entryUUID request
   * control.
   */
  @NotNull public static final String NAME_WITH_ENTRY_UUID_REQUEST_OID =
       "1.3.6.1.4.1.30221.2.5.44";



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = -1083494935823253033L;



  /**
   * Creates a new name with entryUUID request control.  It will be marked
   * critical.
   */
  public NameWithEntryUUIDRequestControl()
  {
    this(true);
  }



  /**
   * Creates a new name with entryUUID request control with the specified
   * criticality.
   *
   * @param  isCritical  Indicates whether this control should be marked
   *                     critical.
   */
  public NameWithEntryUUIDRequestControl(final boolean isCritical)
  {
    super(NAME_WITH_ENTRY_UUID_REQUEST_OID, isCritical, null);
  }



  /**
   * Creates a new name with entryUUID request control which is decoded from the
   * provided generic control.
   *
   * @param  control  The generic control to be decoded as a name with entryUUID
   *                  request control.
   *
   * @throws LDAPException  If the provided control cannot be decoded as a name
   *                         with entryUUID request control.
   */
  public NameWithEntryUUIDRequestControl(@NotNull final Control control)
       throws LDAPException
  {
    super(control);

    if (control.hasValue())
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_NAME_WITH_ENTRY_UUID_REQUEST_HAS_VALUE.get());
    }
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NotNull()
  public String getControlName()
  {
    return INFO_CONTROL_NAME_WITH_ENTRY_UUID_REQUEST.get();
  }



  /**
   * Retrieves a representation of this name with entryUUID request control as a
   * JSON object.  The JSON object uses the following fields (note that since
   * this control does not have a value, neither the {@code value-base64} nor
   * {@code value-json} fields may be present):
   * <UL>
   *   <LI>
   *     {@code oid} -- A mandatory string field whose value is the object
   *     identifier for this control.  For the name with entryUUID request
   *     control, the OID is "1.3.6.1.4.1.30221.2.5.44".
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
              NAME_WITH_ENTRY_UUID_REQUEST_OID),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CONTROL_NAME,
              INFO_CONTROL_NAME_WITH_ENTRY_UUID_REQUEST.get()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CRITICALITY,
              isCritical()));
  }



  /**
   * Attempts to decode the provided object as a JSON representation of a name
   * with entryUUID request control.
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
   * @return  The name with entryUUID request control that was decoded from
   *          the provided JSON object.
   *
   * @throws  LDAPException  If the provided JSON object cannot be parsed as a
   *                         valid name with entryUUID request control.
   */
  @NotNull()
  public static NameWithEntryUUIDRequestControl decodeJSONControl(
              @NotNull final JSONObject controlObject,
              final boolean strict)
         throws LDAPException
  {
    final JSONControlDecodeHelper jsonControl = new JSONControlDecodeHelper(
         controlObject, strict, false, false);

    return new NameWithEntryUUIDRequestControl(
         jsonControl.getCriticality());
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NotNull final StringBuilder buffer)
  {
    buffer.append("NameWithEntryUUIDRequestControl(isCritical=");
    buffer.append(isCritical());
    buffer.append(')');
  }
}
