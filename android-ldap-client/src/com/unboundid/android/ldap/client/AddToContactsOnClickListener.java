/*
 * Copyright 2009-2025 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright 2009-2025 Ping Identity Corporation
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
 * Copyright (C) 2009-2025 Ping Identity Corporation
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
package com.unboundid.android.ldap.client;



import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.Contacts;
import android.view.View;
import android.view.View.OnClickListener;

import com.unboundid.ldap.sdk.Entry;

import static com.unboundid.android.ldap.client.Logger.*;
import static com.unboundid.util.StaticUtils.*;



/**
 * This class provides an on-click listener that is meant to add a user to the
 * phone's address book when the associated view is clicked.
 */
final class AddToContactsOnClickListener
      implements OnClickListener
{
  /**
   * The tag that will be used for log messages generated by this class.
   */
  private static final String LOG_TAG = "AddToContactsListener";



  // The activity that created this on-click listener.
  private final Activity activity;

  // The information about the person to add.
  private final String fax;
  private final String homeAddress;
  private final String homeEMail;
  private final String homePhone;
  private final String mobile;
  private final String name;
  private final String pager;
  private final String workAddress;
  private final String workEMail;
  private final String workPhone;



  /**
   * Creates a new phone number on-click listener that will dial the provided
   * telephone number when the associated view is clicked.
   *
   * @param  activity  The activity that created this on-click listener.
   * @param  entry     The entry for the user to add.
   */
  AddToContactsOnClickListener(final Activity activity, final Entry entry)
  {
    logEnter(LOG_TAG, "<init>", activity, entry);

    this.activity = activity;

    name        = entry.getAttributeValue(AttributeMapper.ATTR_FULL_NAME);
    workPhone   = entry.getAttributeValue(AttributeMapper.ATTR_PRIMARY_PHONE);
    homePhone   = entry.getAttributeValue(AttributeMapper.ATTR_HOME_PHONE);
    mobile      = entry.getAttributeValue(AttributeMapper.ATTR_MOBILE_PHONE);
    pager       = entry.getAttributeValue(AttributeMapper.ATTR_PAGER);
    fax         = entry.getAttributeValue(AttributeMapper.ATTR_FAX);
    workEMail   = entry.getAttributeValue(AttributeMapper.ATTR_PRIMARY_MAIL);
    homeEMail   = entry.getAttributeValue(AttributeMapper.ATTR_ALTERNATE_MAIL);
    workAddress = entry.getAttributeValue(AttributeMapper.ATTR_PRIMARY_ADDRESS);
    homeAddress = entry.getAttributeValue(AttributeMapper.ATTR_HOME_ADDRESS);
  }



  /**
   * Indicates that the associated view was clicked and that the associated
   * entry should be added to the contacts.
   *
   * @param  view  The view that was clicked.
   */
  public void onClick(final View view)
  {
    logEnter(LOG_TAG, "onClick", view);

    final ContentValues values = new ContentValues();
    values.put(Contacts.PeopleColumns.NAME, name);
    values.put(Contacts.PeopleColumns.STARRED, 0);

    final Uri contactURI = Contacts.People.createPersonInMyContactsGroup(
         activity.getContentResolver(), values);
    if (contactURI == null)
    {
      final Intent i = new Intent(activity, PopUp.class);
      i.putExtra(PopUp.BUNDLE_FIELD_TITLE,
           activity.getString(R.string.add_to_contacts_popup_title_error));
      i.putExtra(PopUp.BUNDLE_FIELD_TEXT,
           activity.getString(R.string.add_to_contacts_popup_text_error, name));
      activity.startActivity(i);
    }
    else
    {
      if (workPhone != null)
      {
        addPhoneNumber(workPhone, Contacts.PhonesColumns.TYPE_WORK, contactURI);
      }

      if (homePhone != null)
      {
        addPhoneNumber(homePhone, Contacts.PhonesColumns.TYPE_HOME, contactURI);
      }

      if (mobile != null)
      {
        addPhoneNumber(mobile, Contacts.PhonesColumns.TYPE_MOBILE, contactURI);
      }

      if (pager != null)
      {
        addPhoneNumber(pager, Contacts.PhonesColumns.TYPE_PAGER, contactURI);
      }

      if (fax != null)
      {
        addPhoneNumber(fax, Contacts.PhonesColumns.TYPE_FAX_WORK, contactURI);
      }

      if (workEMail != null)
      {
        addEMailAddress(workEMail,
             Contacts.ContactMethodsColumns.TYPE_WORK, contactURI);
      }

      if (homeEMail != null)
      {
        addEMailAddress(homeEMail,
             Contacts.ContactMethodsColumns.TYPE_HOME, contactURI);
      }

      if (workAddress != null)
      {
        addPostalAddress(workAddress,
             Contacts.ContactMethodsColumns.TYPE_WORK, contactURI);
      }

      if (homeAddress != null)
      {
        addPostalAddress(homeAddress,
             Contacts.ContactMethodsColumns.TYPE_HOME, contactURI);
      }

      final Intent i = new Intent(Intent.ACTION_VIEW, contactURI);
      activity.startActivity(i);
    }
  }



  /**
   * Adds the provided phone number to the contact.
   *
   * @param  number  The number to add.
   * @param  type    The type of number to add.
   * @param  uri     The base URI for the contact.
   *
   * @return  {@code true} if the update was successful, or {@code false} if
   *          not.
   */
  private boolean addPhoneNumber(final String number, final int type,
                                 final Uri uri)
  {
    logEnter(LOG_TAG, "addPhoneNumber", number, type, uri);

    final Uri phoneURI = Uri.withAppendedPath(uri,
         Contacts.People.Phones.CONTENT_DIRECTORY);

    final ContentValues values = new ContentValues();
    values.put(Contacts.PhonesColumns.TYPE, type);
    values.put(Contacts.PhonesColumns.NUMBER,  number);

    return logReturn(LOG_TAG, "addPhoneNumber",
         (activity.getContentResolver().insert(phoneURI, values) != null));
  }



  /**
   * Adds the provided e-mail address to the contact.
   *
   * @param  address  The e-mail address to add.
   * @param  type     The type of address to add.
   * @param  uri      The base URI for the contact.
   *
   * @return  {@code true} if the update was successful, or {@code false} if
   *          not.
   */
  private boolean addEMailAddress(final String address, final int type,
                                  final Uri uri)
  {
    logEnter(LOG_TAG, "addEMailAddress", address, type, uri);

    final Uri emailURI = Uri.withAppendedPath(uri,
         Contacts.People.ContactMethods.CONTENT_DIRECTORY);

    final ContentValues values = new ContentValues();
    values.put(Contacts.ContactMethodsColumns.KIND, Contacts.KIND_EMAIL);
    values.put(Contacts.ContactMethodsColumns.DATA, address);
    values.put(Contacts.ContactMethodsColumns.TYPE, type);

    return logReturn(LOG_TAG, "addEMailAddress",
         (activity.getContentResolver().insert(emailURI, values) != null));
  }



  /**
   * Adds the provided postal address to the contact.
   *
   * @param  address  The postal address to add.
   * @param  type     The type of address to add.
   * @param  uri      The base URI for the contact.
   *
   * @return  {@code true} if the update was successful, or {@code false} if
   *          not.
   */
  private boolean addPostalAddress(final String address, final int type,
                                   final Uri uri)
  {
    logEnter(LOG_TAG, "addPostalAddress", address, type, uri);

    final StringBuilder addr = new StringBuilder();
    final StringTokenizer tokenizer = new StringTokenizer(address, "$");
    while (tokenizer.hasMoreTokens())
    {
      addr.append(tokenizer.nextToken().trim());
      if (tokenizer.hasMoreTokens())
      {
        addr.append(EOL);
      }
    }

    final Uri postalURI = Uri.withAppendedPath(uri,
         Contacts.People.ContactMethods.CONTENT_DIRECTORY);

    final ContentValues values = new ContentValues();
    values.put(Contacts.ContactMethodsColumns.KIND, Contacts.KIND_POSTAL);
    values.put(Contacts.ContactMethodsColumns.DATA, addr.toString());
    values.put(Contacts.ContactMethodsColumns.TYPE, type);

    return logReturn(LOG_TAG, "addPostalAddress",
         (activity.getContentResolver().insert(postalURI, values) != null));
  }
}
