/*
 * Copyright 2019-2024 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2019-2024 Ping Identity Corporation
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
 * Copyright (C) 2019-2024 Ping Identity Corporation
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



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.unboundid.asn1.ASN1Boolean;
import com.unboundid.asn1.ASN1Element;
import com.unboundid.asn1.ASN1Long;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.asn1.ASN1Sequence;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DecodeableControl;
import com.unboundid.ldap.sdk.JSONControlDecodeHelper;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Debug;
import com.unboundid.util.NotMutable;
import com.unboundid.util.NotNull;
import com.unboundid.util.Nullable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.json.JSONBoolean;
import com.unboundid.util.json.JSONField;
import com.unboundid.util.json.JSONNumber;
import com.unboundid.util.json.JSONObject;
import com.unboundid.util.json.JSONString;
import com.unboundid.util.json.JSONValue;

import static com.unboundid.ldap.sdk.unboundidds.controls.ControlMessages.*;



/**
 * This class provides a response control that may be used to convey the
 * password (and other associated information) generated in response to a
 * {@link GeneratePasswordRequestControl}.
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
 * This control has an OID of "1.3.6.1.4.1.30221.2.5.59", a criticality of
 * false, and a value with the following encoding:
 * <PRE>
 *   GeneratePasswordResponse ::= SEQUENCE {
 *        generatedPassword          OCTET STRING,
 *        mustChangePassword         BOOLEAN,
 *        secondsUntilExpiration     [0] INTEGER OPTIONAL,
 *        ... }
 * </PRE>
 *
 * @see  GeneratePasswordRequestControl
 */
@NotMutable()
@ThreadSafety(level=ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class GeneratePasswordResponseControl
       extends Control
       implements DecodeableControl
{
  /**
   * The OID (1.3.6.1.4.1.30221.2.5.59) for the generate password response
   * control.
   */
  @NotNull public static final String GENERATE_PASSWORD_RESPONSE_OID =
       "1.3.6.1.4.1.30221.2.5.59";



  /**
   * The BER type for the {@code secondsUntilExpiration} element.
   */
  private static final byte TYPE_SECONDS_UNTIL_EXPIRATION = (byte) 0x80;



  /**
   * The name of the field used to hold the generated password in the JSON
   * representation of this control.
   */
  @NotNull private static final String JSON_FIELD_GENERATED_PASSWORD =
       "generated-password";



  /**
   * The name of the field used to indicate whether the user must choose a new
   * password in the JSON representation of this control.
   */
  @NotNull private static final String JSON_FIELD_MUST_CHANGE_PASSWORD =
       "must-change-password";



  /**
   * The name of the field used to specify the number of seconds until the
   * password expires in the JSON representation of this control.
   */
  @NotNull private static final String JSON_FIELD_SECONDS_UNTIL_EXPIRATION =
       "seconds-until-expiration";



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 7542512192838228238L;



  // The generated password included in the control.
  @NotNull private final ASN1OctetString generatedPassword;

  // Indicates whether the user will be required to choose a new password the
  // first time they authenticate.
  private final boolean mustChangePassword;

  // The number of seconds until the new password will expire.
  @Nullable private final Long secondsUntilExpiration;



  /**
   * Creates a new empty control instance that is intended to be used only for
   * decoding controls via the {@code DecodeableControl} interface.
   */
  GeneratePasswordResponseControl()
  {
    generatedPassword = null;
    mustChangePassword = false;
    secondsUntilExpiration = null;
  }



  /**
   * Creates a new generate password response control with the provided
   * information.
   *
   * @param  generatedPassword       The password generated by the server.  It
   *                                 must not be {@code null}.
   * @param  mustChangePassword      Indicates whether the user will be required
   *                                 to choose a new password the first time
   *                                 they authenticate.
   * @param  secondsUntilExpiration  The number of seconds until the new
   *                                 password will expire.  It may be
   *                                 {@code null} if the new password will not
   *                                 expire.
   */
  public GeneratePasswordResponseControl(
              @NotNull final String generatedPassword,
              final boolean mustChangePassword,
              @Nullable final Long secondsUntilExpiration)
  {
    this(new ASN1OctetString(generatedPassword), mustChangePassword,
         secondsUntilExpiration);
  }



  /**
   * Creates a new generate password response control with the provided
   * information.
   *
   * @param  generatedPassword       The password generated by the server.  It
   *                                 must not be {@code null}.
   * @param  mustChangePassword      Indicates whether the user will be required
   *                                 to choose a new password the first time
   *                                 they authenticate.
   * @param  secondsUntilExpiration  The number of seconds until the new
   *                                 password will expire.  It may be
   *                                 {@code null} if the new password will not
   *                                 expire.
   */
  public GeneratePasswordResponseControl(
              @NotNull final byte[] generatedPassword,
              final boolean mustChangePassword,
              @Nullable final Long secondsUntilExpiration)
  {
    this(new ASN1OctetString(generatedPassword), mustChangePassword,
         secondsUntilExpiration);
  }



  /**
   * Creates a new generate password response control with the provided
   * information.
   *
   * @param  generatedPassword       The password generated by the server.  It
   *                                 must not be {@code null}.
   * @param  mustChangePassword      Indicates whether the user will be required
   *                                 to choose a new password the first time
   *                                 they authenticate.
   * @param  secondsUntilExpiration  The number of seconds until the new
   *                                 password will expire.  It may be
   *                                 {@code null} if the new password will not
   *                                 expire.
   */
  private GeneratePasswordResponseControl(
               @NotNull final ASN1OctetString generatedPassword,
               final boolean mustChangePassword,
               @Nullable final Long secondsUntilExpiration)
  {
    super(GENERATE_PASSWORD_RESPONSE_OID, false,
         encodeValue(generatedPassword, mustChangePassword,
              secondsUntilExpiration));

    this.generatedPassword = generatedPassword;
    this.mustChangePassword = mustChangePassword;
    this.secondsUntilExpiration = secondsUntilExpiration;
  }



  /**
   * Creates a new generate password response control with the provided
   * information.
   *
   * @param  oid         The OID for the control.
   * @param  isCritical  Indicates whether the control should be marked
   *                     critical.
   * @param  value       The encoded value for the control.  This may be
   *                     {@code null} if no value was provided.
   *
   * @throws  LDAPException  If the provided control cannot be decoded as a
   *                         generate password response control.
   */
  public GeneratePasswordResponseControl(@NotNull final String oid,
                                         final boolean isCritical,
                                         @Nullable final ASN1OctetString value)
         throws LDAPException
  {
    super(oid, isCritical,  value);

    if (value == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GENERATE_PASSWORD_RESPONSE_NO_VALUE.get());
    }

    try
    {
      final ASN1Element valElement = ASN1Element.decode(value.getValue());
      final ASN1Element[] elements =
           ASN1Sequence.decodeAsSequence(valElement).elements();
      generatedPassword = ASN1OctetString.decodeAsOctetString(elements[0]);
      mustChangePassword =
           ASN1Boolean.decodeAsBoolean(elements[1]).booleanValue();

      Long secsUntilExp = null;
      for (int i=2; i < elements.length; i++)
      {
        final ASN1Element e = elements[i];
        switch (e.getType())
        {
          case TYPE_SECONDS_UNTIL_EXPIRATION:
            secsUntilExp = ASN1Long.decodeAsLong(e).longValue();
            break;
          default:
            // This is a field we don't currently recognize but might be defined
            // in the future.
            break;
        }
      }

      secondsUntilExpiration = secsUntilExp;
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GENERATE_PASSWORD_RESPONSE_CANNOT_DECODE_VALUE.get(
                StaticUtils.getExceptionMessage(e)),
           e);
    }
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NotNull()
  public GeneratePasswordResponseControl decodeControl(
              @NotNull final String oid,
              final boolean isCritical,
              @Nullable final ASN1OctetString value)
         throws LDAPException
  {
    return new GeneratePasswordResponseControl(oid, isCritical, value);
  }



  /**
   * Extracts a generate password  response control from the provided result.
   *
   * @param  result  The result from which to retrieve the generate password
   *                 response control.
   *
   * @return  The generate password response control contained in the provided
   *          result, or {@code null} if the result did not contain a generate
   *          password response control.
   *
   * @throws  LDAPException  If a problem is encountered while attempting to
   *                         decode the generate password response control
   *                         contained in the provided result.
   */
  @Nullable()
  public static GeneratePasswordResponseControl get(
                     @NotNull final LDAPResult result)
         throws LDAPException
  {
    final Control c = result.getResponseControl(GENERATE_PASSWORD_RESPONSE_OID);
    if (c == null)
    {
      return null;
    }

    if (c instanceof GeneratePasswordResponseControl)
    {
      return (GeneratePasswordResponseControl) c;
    }
    else
    {
      return new GeneratePasswordResponseControl(c.getOID(), c.isCritical(),
           c.getValue());
    }
  }



  /**
   * Encodes the provided information appropriately for use as the value of this
   * control.
   *
   * @param  generatedPassword        The password generated by the server.  It
   *                                 must not be {@code null}.
   * @param  mustChangePassword      Indicates whether the user will be required
   *                                 to choose a new password the first time
   *                                 they authenticate.
   * @param  secondsUntilExpiration  The number of seconds until the new
   *                                 password will expire.  It may be
   *                                 {@code null} if the new password will not
   *                                 expire.
   *
   * @return  The ASN.1 octet string suitable for use as the control value.
   */
  @NotNull()
  private static ASN1OctetString encodeValue(
                      @NotNull final ASN1OctetString generatedPassword,
                      final boolean mustChangePassword,
                      @Nullable final Long secondsUntilExpiration)
  {
    final ArrayList<ASN1Element> elements = new ArrayList<>(3);
    elements.add(generatedPassword);
    elements.add(mustChangePassword
         ? ASN1Boolean.UNIVERSAL_BOOLEAN_TRUE_ELEMENT
         : ASN1Boolean.UNIVERSAL_BOOLEAN_FALSE_ELEMENT);

    if (secondsUntilExpiration != null)
    {
      elements.add(new ASN1Long(TYPE_SECONDS_UNTIL_EXPIRATION,
           secondsUntilExpiration));
    }

    return new ASN1OctetString(new ASN1Sequence(elements).encode());
  }



  /**
   * Retrieves the password that was generated by the server.
   *
   * @return  The password that was generated by the server.
   */
  @NotNull()
  public ASN1OctetString getGeneratedPassword()
  {
    return generatedPassword;
  }



  /**
   * Retrieves a string representation of the password that was generated by the
   * server.
   *
   * @return  A string representation of the password that was generated by the
   *          server.
   */
  @NotNull()
  public String getGeneratedPasswordString()
  {
    return generatedPassword.stringValue();
  }



  /**
   * Retrieves the bytes that comprise the password that was generated by the
   * server.
   *
   * @return  The bytes that comprise the password that was generated by the
   *          server.
   */
  @NotNull()
  public byte[] getGeneratedPasswordBytes()
  {
    return generatedPassword.getValue();
  }



  /**
   * Indicates whether the user will be required to change their password the
   * first time they authenticate.
   *
   * @return  {@code true} if the user will be required to change their password
   *          the first time they authenticate, or {@code false} if not.
   */
  public boolean mustChangePassword()
  {
    return mustChangePassword;
  }



  /**
   * Retrieves the length of time, in seconds, until the generated password will
   * expire.
   *
   * @return  The length of time, in seconds, until the generated password will
   *          expire, or {@code null} if this is not available (e.g., because
   *          the generated password will not expire).
   */
  @Nullable()
  public Long getSecondsUntilExpiration()
  {
    return secondsUntilExpiration;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NotNull()
  public String getControlName()
  {
    return INFO_CONTROL_NAME_GENERATE_PASSWORD_RESPONSE.get();
  }



  /**
   * Retrieves a representation of this generate password response control as a
   * JSON object.  The JSON object uses the following fields:
   * <UL>
   *   <LI>
   *     {@code oid} -- A mandatory string field whose value is the object
   *     identifier for this control.  For the generate password response
   *     control, the OID is "1.3.6.1.4.1.30221.2.5.59".
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
   *   <LI>
   *     {@code value-base64} -- An optional string field whose value is a
   *     base64-encoded representation of the raw value for this generate
   *     password response control.  Exactly one of the {@code value-base64} and
   *     {@code value-json} fields must be present.
   *   </LI>
   *   <LI>
   *     {@code value-json} -- An optional JSON object field whose value is a
   *     user-friendly representation of the value for this generate password
   *     response control.  Exactly one of the {@code value-base64} and
   *     {@code value-json} fields must be present, and if the
   *     {@code value-json} field is used, then it will use the following
   *     fields:
   *     <UL>
   *       <LI>
   *         {@code generated-password} -- A string field whose value is the
   *         password that was generated for the entry.
   *       </LI>
   *       <LI>
   *         {@code must-change-password} -- A Boolean field that indicates
   *         whether the user must choose a new password before they will be
   *         allowed to request any other operations.
   *       </LI>
   *       <LI>
   *         {@code seconds-until-expiration} -- An optional integer field whose
   *         value is the number of seconds until the generated password
   *         expires.
   *       </LI>
   *     </UL>
   *   </LI>
   * </UL>
   *
   * @return  A JSON object that contains a representation of this control.
   */
  @Override()
  @NotNull()
  public JSONObject toJSONControl()
  {
    final Map<String,JSONValue> valueFields = new LinkedHashMap<>();
    valueFields.put(JSON_FIELD_GENERATED_PASSWORD,
         new JSONString(generatedPassword.stringValue()));
    valueFields.put(JSON_FIELD_MUST_CHANGE_PASSWORD,
         new JSONBoolean(mustChangePassword));

    if (secondsUntilExpiration != null)
    {
      valueFields.put(JSON_FIELD_SECONDS_UNTIL_EXPIRATION,
           new JSONNumber(secondsUntilExpiration));
    }


    return new JSONObject(
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_OID,
              GENERATE_PASSWORD_RESPONSE_OID),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CONTROL_NAME,
              INFO_CONTROL_NAME_GENERATE_PASSWORD_RESPONSE.get()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CRITICALITY,
              isCritical()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_VALUE_JSON,
              new JSONObject(valueFields)));
  }



  /**
   * Attempts to decode the provided object as a JSON representation of a
   * generate password response control.
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
   * @return  The generate password response control that was decoded from the
   *          provided JSON object.
   *
   * @throws  LDAPException  If the provided JSON object cannot be parsed as a
   *                         valid generate password response control.
   */
  @NotNull()
  public static GeneratePasswordResponseControl decodeJSONControl(
              @NotNull final JSONObject controlObject,
              final boolean strict)
         throws LDAPException
  {
    final JSONControlDecodeHelper jsonControl = new JSONControlDecodeHelper(
         controlObject, strict, true, true);

    final ASN1OctetString rawValue = jsonControl.getRawValue();
    if (rawValue != null)
    {
      return new GeneratePasswordResponseControl(jsonControl.getOID(),
           jsonControl.getCriticality(), rawValue);
    }


    final JSONObject valueObject = jsonControl.getValueObject();

    final String generatedPassword =
         valueObject.getFieldAsString(JSON_FIELD_GENERATED_PASSWORD);
    if (generatedPassword == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GENERATE_PASSWORD_RESPONSE_JSON_MISSING_FIELD.get(
                valueObject.toSingleLineString(),
                JSON_FIELD_GENERATED_PASSWORD));
    }

    final Boolean mustChangePassword =
         valueObject.getFieldAsBoolean(JSON_FIELD_MUST_CHANGE_PASSWORD);
    if (mustChangePassword == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_GENERATE_PASSWORD_RESPONSE_JSON_MISSING_FIELD.get(
                valueObject.toSingleLineString(),
                JSON_FIELD_MUST_CHANGE_PASSWORD));
    }

    final Long secondsUntilExpiration =
         valueObject.getFieldAsLong(JSON_FIELD_SECONDS_UNTIL_EXPIRATION);


    if (strict)
    {
      final List<String> unrecognizedFields =
           JSONControlDecodeHelper.getControlObjectUnexpectedFields(
                valueObject, JSON_FIELD_GENERATED_PASSWORD,
                JSON_FIELD_MUST_CHANGE_PASSWORD,
                JSON_FIELD_SECONDS_UNTIL_EXPIRATION);
      if (! unrecognizedFields.isEmpty())
      {
        throw new LDAPException(ResultCode.DECODING_ERROR,
             ERR_GENERATE_PASSWORD_RESPONSE_JSON_CONTROL_UNRECOGNIZED_FIELD.get(
                  controlObject.toSingleLineString(),
                  unrecognizedFields.get(0)));
      }
    }


    return new GeneratePasswordResponseControl(generatedPassword,
         mustChangePassword, secondsUntilExpiration);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NotNull final StringBuilder buffer)
  {
    buffer.append("GeneratePasswordResponseControl(mustChangePassword=");
    buffer.append(mustChangePassword);

    if (secondsUntilExpiration != null)
    {
      buffer.append(", secondsUntilExpiration=");
      buffer.append(secondsUntilExpiration);
    }

    buffer.append(')');
  }
}
