/*
 * Copyright 2022-2025 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2022-2025 Ping Identity Corporation
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
 * Copyright (C) 2022-2025 Ping Identity Corporation
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
package com.unboundid.ldap.sdk.unboundidds.logs.v2;



import org.testng.annotations.Test;

import com.unboundid.ldap.sdk.LDAPSDKTestCase;
import com.unboundid.ldap.sdk.unboundidds.logs.v2.syntax.BooleanLogFieldSyntax;



/**
 * This class provides a set of tests for the {@code LogField} class.
 */
public final class LogFieldTestCase
       extends LDAPSDKTestCase
{
  /**
   * Tests the behavior of the log field without a constant name.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testWithoutConstantName()
         throws Exception
  {
    final LogField field = new LogField("the-field-name",
         BooleanLogFieldSyntax.getInstance());

    assertNotNull(field.getFieldName());
    assertEquals(field.getFieldName(), "the-field-name");

    assertNull(field.getConstantName());

    assertNotNull(field.getExpectedSyntax());
    assertTrue(field.getExpectedSyntax() instanceof BooleanLogFieldSyntax);

    assertNotNull(field.toString());
    assertFalse(field.toString().isEmpty());
  }



  /**
   * Tests the behavior of the log field with a constant name.
   *
   * @throws  Exception  If an unexpected problem occurs.
   */
  @Test()
  public void testWithConstantName()
         throws Exception
  {
    final LogField field = new LogField("the-field-name", "the-constant-name",
         BooleanLogFieldSyntax.getInstance());

    assertNotNull(field.getFieldName());
    assertEquals(field.getFieldName(), "the-field-name");

    assertNotNull(field.getConstantName());
    assertEquals(field.getConstantName(), "the-constant-name");

    assertNotNull(field.getExpectedSyntax());
    assertTrue(field.getExpectedSyntax() instanceof BooleanLogFieldSyntax);

    assertNotNull(field.toString());
    assertFalse(field.toString().isEmpty());
  }
}
