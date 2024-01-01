/*
 * Copyright 2018-2024 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2018-2024 Ping Identity Corporation
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
 * Copyright (C) 2018-2024 Ping Identity Corporation
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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.unboundid.asn1.ASN1Element;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.asn1.ASN1Sequence;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.JSONControlDecodeHelper;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Debug;
import com.unboundid.util.NotMutable;
import com.unboundid.util.NotNull;
import com.unboundid.util.Nullable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.Validator;
import com.unboundid.util.json.JSONArray;
import com.unboundid.util.json.JSONField;
import com.unboundid.util.json.JSONObject;
import com.unboundid.util.json.JSONValue;

import static com.unboundid.ldap.sdk.unboundidds.controls.ControlMessages.*;



/**
 * This class provides an implementation of a control that may be included in a
 * search request to override certain default limits that would normally be in
 * place for the operation.  The override behavior is specified using one or
 * more name-value pairs, with property names being case sensitive.
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
 * The control has an OID of 1.3.6.1.4.1.30221.2.5.56, a criticality of either
 * {@code true} or {@code false}, and a value with the provided encoding:
 *
 * that contains a mapping of one or
 * more case-sensitive property-value pairs.  Property names will be treated in
 * a case-sensitive manner.
 * the following encoding:
 * <PRE>
 *   OverrideSearchLimitsRequestValue ::= SEQUENCE OF SEQUENCE {
 *        propertyName      OCTET STRING,
 *        propertyValue     OCTET STRING }
 * </PRE>
 */
@NotMutable()
@ThreadSafety(level= ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class OverrideSearchLimitsRequestControl
       extends Control
{
  /**
   * The OID (1.3.6.1.4.1.30221.2.5.56) for the override search limits request
   * control.
   */
  @NotNull public static final String OVERRIDE_SEARCH_LIMITS_REQUEST_OID =
       "1.3.6.1.4.1.30221.2.5.56";



  /**
   * The name of the field used to hold the set of properties in the JSON
   * representation of this control.
   */
  @NotNull private static final String JSON_FIELD_PROPERTIES = "properties";



  /**
   * The name of the field used to hold a property name in the JSON
   * representation of this control.
   */
  @NotNull private static final String JSON_FIELD_PROPERTY_NAME = "name";



  /**
   * The name of the field used to hold a property value in the JSON
   * representation of this control.
   */
  @NotNull private static final String JSON_FIELD_PROPERTY_VALUE = "value";



  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 3685279915414141978L;



  // The set of properties included in this control.
  @NotNull private final Map<String,String> properties;



  /**
   * Creates a new instance of this override search limits request control with
   * the specified property name and value.  It will not be critical.
   *
   * @param  propertyName   The name of the property to set.  It must not be
   *                        {@code null} or empty.
   * @param  propertyValue  The value for the specified property.  It must not
   *                        be {@code null} or empty.
   */
  public OverrideSearchLimitsRequestControl(@NotNull final String propertyName,
                                            @NotNull final String propertyValue)
  {
    this(Collections.singletonMap(propertyName, propertyValue), false);
  }



  /**
   * Creates a new instance of this override search limits request control with
   * the provided set of properties.
   *
   * @param  properties  The map of properties to set in this control.  It must
   *                     not be {@code null} or empty, and none of the keys or
   *                     values inside it may be {@code null} or empty.
   * @param  isCritical  Indicates whether the control should be considered
   *                     critical.
   */
  public OverrideSearchLimitsRequestControl(
              @NotNull final Map<String,String> properties,
              final boolean isCritical)
  {
    super(OVERRIDE_SEARCH_LIMITS_REQUEST_OID, isCritical,
         encodeValue(properties));

    this.properties =
         Collections.unmodifiableMap(new LinkedHashMap<>(properties));
  }



  /**
   * Creates a new instance of this override search limits request control that
   * is decoded from the provided generic control.
   *
   * @param  control  The generic control to decode as an override search limits
   *                  request control.  It must not be {@code null}.
   *
   * @throws  LDAPException  If the provided control cannot be decoded as an
   *                         override search limits request control.
   */
  public OverrideSearchLimitsRequestControl(@NotNull final Control control)
         throws LDAPException
  {
    super(control);

    final ASN1OctetString value = control.getValue();
    if (value == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_NO_VALUE.get());
    }

    final LinkedHashMap<String,String> propertyMap =
         new LinkedHashMap<>(StaticUtils.computeMapCapacity(10));
    try
    {
      for (final ASN1Element valueElement :
           ASN1Sequence.decodeAsSequence(value.getValue()).elements())
      {
        final ASN1Element[] propertyElements =
             ASN1Sequence.decodeAsSequence(valueElement).elements();
        final String propertyName = ASN1OctetString.decodeAsOctetString(
             propertyElements[0]).stringValue();
        final String propertyValue = ASN1OctetString.decodeAsOctetString(
             propertyElements[1]).stringValue();

        if (propertyName.isEmpty())
        {
          throw new LDAPException(ResultCode.DECODING_ERROR,
               ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_EMPTY_PROPERTY_NAME.get());
        }

        if (propertyValue.isEmpty())
        {
          throw new LDAPException(ResultCode.DECODING_ERROR,
               ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_EMPTY_PROPERTY_VALUE.get(
                    propertyName));
        }

        if (propertyMap.containsKey(propertyName))
        {
          throw new LDAPException(ResultCode.DECODING_ERROR,
               ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_DUPLICATE_PROPERTY_NAME.get(
                    propertyName));
        }

        propertyMap.put(propertyName, propertyValue);
      }
    }
    catch (final LDAPException e)
    {
      Debug.debugException(e);
      throw e;
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_CANNOT_DECODE_VALUE.get(
                StaticUtils.getExceptionMessage(e)),
           e);
    }

    if (propertyMap.isEmpty())
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_CONTROL_NO_PROPERTIES.get());
    }

    properties = Collections.unmodifiableMap(propertyMap);
  }



  /**
   * Encodes the provided set of properties into an ASN.1 element suitable for
   * use as the value of this control.
   *
   * @param  properties  The map of properties to set in this control.  It must
   *                     not be {@code null} or empty, and none of the keys or
   *                     values inside it may be {@code null} or empty.
   *
   * @return  The ASN.1 octet string containing the encoded value.
   */
  @NotNull()
  static ASN1OctetString encodeValue(
              @NotNull final Map<String,String> properties)
  {
    Validator.ensureTrue(((properties != null) && (! properties.isEmpty())),
         "OverrideSearchLimitsRequestControl.<init>properties must not be " +
              "null or empty");

    final ArrayList<ASN1Element> propertyElements =
         new ArrayList<>(properties.size());
    for (final Map.Entry<String,String> e : properties.entrySet())
    {
      final String propertyName = e.getKey();
      final String propertyValue = e.getValue();
      Validator.ensureTrue(
           ((propertyName != null) && (! propertyName.isEmpty())),
           "OverrideSearchLimitsRequestControl.<init>properties keys must " +
                "not be null or empty");
      Validator.ensureTrue(
           ((propertyValue != null) && (! propertyValue.isEmpty())),
           "OverrideSearchLimitsRequestControl.<init>properties values must " +
                "not be null or empty");

      propertyElements.add(new ASN1Sequence(
           new ASN1OctetString(propertyName),
           new ASN1OctetString(propertyValue)));
    }

    return new ASN1OctetString(new ASN1Sequence(propertyElements).encode());
  }



  /**
   * Retrieves a map of the properties included in this request control.
   *
   * @return  A map of the properties included in this request control.
   */
  @NotNull()
  public Map<String,String> getProperties()
  {
    return properties;
  }



  /**
   * Retrieves the value of the specified property.
   *
   * @param  propertyName  The name of the property for which to retrieve the
   *                       value.  It must not be {@code null} or empty, and it
   *                       will be treated in a case-sensitive manner.
   *
   * @return  The value of the requested property, or {@code null} if the
   *          property is not set in the control.
   */
  @Nullable()
  public String getProperty(@NotNull final String propertyName)
  {
    Validator.ensureTrue(((propertyName != null) && (! propertyName.isEmpty())),
         "OverrideSearchLimitsRequestControl.getProperty.propertyName must " +
              "not be null or empty.");

    return properties.get(propertyName);
  }



  /**
   * Retrieves the value of the specified property as a {@code Boolean}.
   *
   * @param  propertyName  The name of the property for which to retrieve the
   *                       value.  It must not be {@code null} or empty, and it
   *                       will be treated in a case-sensitive manner.
   * @param  defaultValue  The default value that will be used if the requested
   *                       property is not set or if its value cannot be parsed
   *                       as a {@code Boolean}.  It may be {@code null} if the
   *                       default value should be {@code null}.
   *
   * @return  The Boolean value of the requested property, or the provided
   *          default value if the property is not set or if its value cannot be
   *          parsed as a {@code Boolean}.
   */
  @Nullable()
  public Boolean getPropertyAsBoolean(@NotNull final String propertyName,
                                      @Nullable final Boolean defaultValue)
  {
    final String propertyValue = getProperty(propertyName);
    if (propertyValue == null)
    {
      return defaultValue;
    }

    switch (StaticUtils.toLowerCase(propertyValue))
    {
      case "true":
      case "t":
      case "yes":
      case "y":
      case "on":
      case "1":
        return Boolean.TRUE;
      case "false":
      case "f":
      case "no":
      case "n":
      case "off":
      case "0":
        return Boolean.FALSE;
      default:
        return defaultValue;
    }
  }



  /**
   * Retrieves the value of the specified property as an {@code Integer}.
   *
   * @param  propertyName  The name of the property for which to retrieve the
   *                       value.  It must not be {@code null} or empty, and it
   *                       will be treated in a case-sensitive manner.
   * @param  defaultValue  The default value that will be used if the requested
   *                       property is not set or if its value cannot be parsed
   *                       as an {@code Integer}.  It may be {@code null} if the
   *                       default value should be {@code null}.
   *
   * @return  The integer value of the requested property, or the provided
   *          default value if the property is not set or if its value cannot be
   *          parsed as an {@code Integer}.
   */
  @Nullable()
  public Integer getPropertyAsInteger(@NotNull final String propertyName,
                                      @Nullable final Integer defaultValue)
  {
    final String propertyValue = getProperty(propertyName);
    if (propertyValue == null)
    {
      return defaultValue;
    }

    try
    {
      return Integer.parseInt(propertyValue);
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      return defaultValue;
    }
  }



  /**
   * Retrieves the value of the specified property as a {@code Long}.
   *
   * @param  propertyName  The name of the property for which to retrieve the
   *                       value.  It must not be {@code null} or empty, and it
   *                       will be treated in a case-sensitive manner.
   * @param  defaultValue  The default value that will be used if the requested
   *                       property is not set or if its value cannot be parsed
   *                       as an {@code Long}.  It may be {@code null} if the
   *                       default value should be {@code null}.
   *
   * @return  The long value of the requested property, or the provided default
   *          value if the property is not set or if its value cannot be parsed
   *          as a {@code Long}.
   */
  @Nullable()
  public Long getPropertyAsLong(@NotNull final String propertyName,
                                @Nullable final Long defaultValue)
  {
    final String propertyValue = getProperty(propertyName);
    if (propertyValue == null)
    {
      return defaultValue;
    }

    try
    {
      return Long.parseLong(propertyValue);
    }
    catch (final Exception e)
    {
      Debug.debugException(e);
      return defaultValue;
    }
  }



  /**
   * Retrieves the user-friendly name for this control, if available.  If no
   * user-friendly name has been defined, then the OID will be returned.
   *
   * @return  The user-friendly name for this control, or the OID if no
   *          user-friendly name is available.
   */
  @Override()
  @NotNull()
  public String getControlName()
  {
    return INFO_OVERRIDE_SEARCH_LIMITS_REQUEST_CONTROL_NAME.get();
  }



  /**
   * Retrieves a representation of this override search limits request control
   * as a JSON object.  The JSON object uses the following fields:
   * <UL>
   *   <LI>
   *     {@code oid} -- A mandatory string field whose value is the object
   *     identifier for this control.  For the override search limits request
   *     control, the OID is "1.3.6.1.4.1.30221.2.5.56".
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
   *     base64-encoded representation of the raw value for this override search
   *     limits request control.  Exactly one of the {@code value-base64} and
   *     {@code value-json} fields must be present.
   *   </LI>
   *   <LI>
   *     {@code value-json} -- An optional JSON object field whose value is a
   *     user-friendly representation of the value for this override search
   *     limits request control.  Exactly one of the {@code value-base64} and
   *     {@code value-json} fields must be present, and if the
   *     {@code value-json} field is used, then it will use the following
   *     fields:
   *     <UL>
   *       <LI>
   *         {@code properties} -- A mandatory array field whose values are
   *         JSON objects with the properties to use for this control.  Each of
   *         these JSON objects uses the following fields:
   *         <UL>
   *           <LI>
   *             {@code name} -- A mandatory string field whose value is the
   *             property name.
   *           </LI>
   *           <LI>
   *             {@code value} -- A mandatory string field whose value is the
   *             property value.
   *           </LI>
   *         </UL>
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
    final List<JSONValue> propertiesValues = new ArrayList<>(properties.size());
    for (final Map.Entry<String,String> e : properties.entrySet())
    {
      propertiesValues.add(new JSONObject(
           new JSONField(JSON_FIELD_PROPERTY_NAME, e.getKey()),
           new JSONField(JSON_FIELD_PROPERTY_VALUE, e.getValue())));
    }

    return new JSONObject(
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_OID,
              OVERRIDE_SEARCH_LIMITS_REQUEST_OID),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CONTROL_NAME,
              INFO_OVERRIDE_SEARCH_LIMITS_REQUEST_CONTROL_NAME.get()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_CRITICALITY,
              isCritical()),
         new JSONField(JSONControlDecodeHelper.JSON_FIELD_VALUE_JSON,
              new JSONObject(
                   new JSONField(JSON_FIELD_PROPERTIES,
                        new JSONArray(propertiesValues)))));
  }



  /**
   * Attempts to decode the provided object as a JSON representation of an
   * override search limits request control.
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
   * @return  The override search limits request control that was decoded from
   *          the provided JSON object.
   *
   * @throws  LDAPException  If the provided JSON object cannot be parsed as a
   *                         valid override search limits request control.
   */
  @NotNull()
  public static OverrideSearchLimitsRequestControl decodeJSONControl(
              @NotNull final JSONObject controlObject,
              final boolean strict)
         throws LDAPException
  {
    final JSONControlDecodeHelper jsonControl = new JSONControlDecodeHelper(
         controlObject, strict, true, true);

    final ASN1OctetString rawValue = jsonControl.getRawValue();
    if (rawValue != null)
    {
      return new OverrideSearchLimitsRequestControl(new Control(
           jsonControl.getOID(), jsonControl.getCriticality(), rawValue));
    }


    final JSONObject valueObject = jsonControl.getValueObject();

    final List<JSONValue> propertiesValues =
         valueObject.getFieldAsArray(JSON_FIELD_PROPERTIES);
    if (propertiesValues == null)
    {
      throw new LDAPException(ResultCode.DECODING_ERROR,
           ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_JSON_MISSING_PROPERTIES.get(
                controlObject.toSingleLineString(), JSON_FIELD_PROPERTIES));
    }

    final Map<String,String> properties = new LinkedHashMap<>();
    for (final JSONValue v : propertiesValues)
    {
      if (v instanceof JSONObject)
      {
        final JSONObject o = (JSONObject) v;

        final String name = o.getFieldAsString(JSON_FIELD_PROPERTY_NAME);
        if (name == null)
        {
          throw new LDAPException(ResultCode.DECODING_ERROR,
               ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_JSON_MISSING_PROP_FIELD.get(
                    controlObject.toSingleLineString(), JSON_FIELD_PROPERTIES,
                    JSON_FIELD_PROPERTY_NAME));
        }

        final String value = o.getFieldAsString(JSON_FIELD_PROPERTY_VALUE);
        if (value == null)
        {
          throw new LDAPException(ResultCode.DECODING_ERROR,
               ERR_OVERRIDE_SEARCH_LIMITS_REQUEST_JSON_MISSING_PROP_FIELD.get(
                    controlObject.toSingleLineString(), JSON_FIELD_PROPERTIES,
                    JSON_FIELD_PROPERTY_VALUE));
        }

        if (strict)
        {
          final List<String> unrecognizedFields =
               JSONControlDecodeHelper.getControlObjectUnexpectedFields(
                    o, JSON_FIELD_PROPERTY_NAME, JSON_FIELD_PROPERTY_VALUE);
          if (! unrecognizedFields.isEmpty())
          {
            throw new LDAPException(ResultCode.DECODING_ERROR,
                 ERR_OVERRIDE_SEARCH_LIMITS_RESPONSE_JSON_UNKNOWN_PROP_FIELD.
                      get(controlObject.toSingleLineString(),
                           JSON_FIELD_PROPERTIES, unrecognizedFields.get(0)));
          }
        }

        properties.put(name, value);
      }
      else
      {
        throw new LDAPException(ResultCode.DECODING_ERROR,
             ERR_OVERRIDE_SEARCH_LIMITS_RESPONSE_JSON_PROP_NOT_OBJECT.get(
                  controlObject.toSingleLineString(), JSON_FIELD_PROPERTIES));
      }
    }


    if (strict)
    {
      final List<String> unrecognizedFields =
           JSONControlDecodeHelper.getControlObjectUnexpectedFields(
                valueObject, JSON_FIELD_PROPERTIES);
      if (! unrecognizedFields.isEmpty())
      {
        throw new LDAPException(ResultCode.DECODING_ERROR,
             ERR_OVERRIDE_SEARCH_LIMITS_RESPONSE_JSON_UNKNOWN_VALUE_FIELD.get(
                  controlObject.toSingleLineString(),
                  unrecognizedFields.get(0)));
      }
    }


    return new OverrideSearchLimitsRequestControl(properties,
         jsonControl.getCriticality());
  }



  /**
   * Appends a string representation of this LDAP control to the provided
   * buffer.
   *
   * @param  buffer  The buffer to which to append the string representation of
   *                 this buffer.
   */
  @Override()
  public void toString(@NotNull final StringBuilder buffer)
  {
    buffer.append("OverrideSearchLimitsRequestControl(oid='");
    buffer.append(getOID());
    buffer.append("', isCritical=");
    buffer.append(isCritical());
    buffer.append(", properties={");

    final Iterator<Map.Entry<String,String>> iterator =
         properties.entrySet().iterator();
    while (iterator.hasNext())
    {
      final Map.Entry<String,String> e = iterator.next();

      buffer.append('\'');
      buffer.append(e.getKey());
      buffer.append("'='");
      buffer.append(e.getValue());
      buffer.append('\'');

      if (iterator.hasNext())
      {
        buffer.append(", ");
      }
    }

    buffer.append("})");
  }
}
