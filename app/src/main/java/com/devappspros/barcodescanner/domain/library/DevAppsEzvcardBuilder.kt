package com.devappspros.barcodescanner.domain.library

import android.content.Context
import com.devappspros.barcodescanner.R
import ezvcard.VCard
import ezvcard.parameter.EmailType
import ezvcard.parameter.TelephoneType
import ezvcard.property.Address
import ezvcard.property.Email
import ezvcard.property.FormattedName
import ezvcard.property.Note
import ezvcard.property.Organization
import ezvcard.property.StructuredName
import ezvcard.property.Telephone
import ezvcard.property.Title
import ezvcard.property.Url

class DevAppsEzvcardBuilder {

    private var mStructuredName: StructuredName? = null
    private var mFormattedName: FormattedName? = null
    private var mOrganization: Organization? = null
    private var mJobTitle: Title? = null
    private var mUrl: Url? = null
    private val mEmailList = mutableListOf<Email>()
    private val mPhoneList = mutableListOf<Telephone>()
    private var mAddress: Address? = null
    private var mNote: Note? = null

    fun build(): VCard = VCard().apply {
        if(mStructuredName!=null)
            structuredName = mStructuredName
        if(mFormattedName!=null)
            formattedName=mFormattedName
        if(mOrganization!=null)
            organization = mOrganization
        if(mJobTitle!=null)
            titles.add(mJobTitle)
        if(mUrl!=null)
            urls.add(mUrl)
        if(mEmailList.isNotEmpty())
            emails.addAll(mEmailList)
        if(mPhoneList.isNotEmpty())
            telephoneNumbers.addAll(mPhoneList)
        if(mAddress!=null)
            addAddress(mAddress)
        if(mNote!=null)
            notes.add(mNote)
    }

    fun createStructuredName(name: String, firstName: String, civil: String) {
        if (name.isNotBlank() || firstName.isNotBlank() || civil.isNotBlank()) {
            mStructuredName = StructuredName().apply {

                if (name.isNotBlank())
                    family = name.trim()

                if (firstName.isNotBlank())
                    given = firstName.trim()

                if (civil.isNotBlank())
                    prefixes.add(civil.trim())
            }
        }
        createFormattedName(name, firstName)
    }

    private fun createFormattedName(name: String, firstName: String){
        mFormattedName = FormattedName("$firstName $name".trim())
    }

    fun createOrganization(organization: String) {
        if(organization.isNotBlank()) {
            mOrganization = Organization().apply {
                values.add(organization.trim())
            }
        }
    }

    fun createJobTitle(jobTitle: String) {
        if(jobTitle.isNotBlank()) {
            mJobTitle = Title(jobTitle.trim())
        }
    }

    fun createUrl(url: String) {
        if(url.isNotBlank())
            mUrl = Url(url.trim())
    }

    fun addEmail(context: Context, email: String, emailTypeStr: String) {
        if(email.isNotBlank()) {
            mEmailList.add(
                Email(email.trim()).apply {
                    getEmailType(context, emailTypeStr)?.let { emailType ->
                        types.add(emailType)
                    }
                }
            )
        }
    }

    fun addPhone(context: Context, phone: String, phoneTypeStr: String) {
        if(phone.isNotBlank()) {
            mPhoneList.add(
                Telephone(phone.trim()).apply {
                    getPhoneType(context, phoneTypeStr)?.let { phoneType ->
                        types.add(phoneType)
                    }
                }
            )
        }
    }

    fun createAddress(mStreet: String, mPostalCode: String, mCity: String, mCountry: String, mRegion: String) {

        if (mStreet.isNotBlank() || mPostalCode.isNotBlank() || mCity.isNotBlank() || mCountry.isNotBlank() || mRegion.isNotBlank()) {
            mAddress = Address().apply {
                if (mStreet.isNotBlank())
                    streetAddress = mStreet.trim()
                if (mPostalCode.isNotBlank())
                    postalCode = mPostalCode.trim()
                if (mCity.isNotBlank())
                    locality = mCity.trim()
                if (mCountry.isNotBlank())
                    country = mCountry.trim()
                if (mRegion.isNotBlank())
                    region = mRegion.trim()
            }
        }
    }

    fun createNote(note: String){
        if(note.isNotBlank())
            mNote = Note(note.trim())
    }

    private fun getEmailType(context: Context, type: String): EmailType? {
        return when(type){
            context.getString(R.string.spinner_type_work) -> EmailType.WORK
            context.getString(R.string.spinner_type_home) -> EmailType.HOME
            else -> null
        }
    }

    private fun getPhoneType(context: Context, type: String): TelephoneType? {
        return when(type){
            context.getString(R.string.spinner_type_mobile) -> TelephoneType.CELL
            context.getString(R.string.spinner_type_work) -> TelephoneType.WORK
            context.getString(R.string.spinner_type_home) -> TelephoneType.HOME
            context.getString(R.string.spinner_type_fax) -> TelephoneType.FAX
            else -> null
        }
    }
}