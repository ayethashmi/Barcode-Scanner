package com.devappspros.barcodescanner.domain.library

class DevAppsVEventBuilder {

    companion object{
        private const val BEGIN_VEVENT = "BEGIN:VEVENT"
        private const val END_VEVENT = "END:VEVENT"

        private const val SUMMARY = "SUMMARY:"
        private const val DTSTART = "DTSTART:"
        private const val DTEND = "DTEND:"
        private const val LOCATION = "LOCATION:"
        private const val DESCRIPTION = "DESCRIPTION:"
    }

    private val stringBuilder: StringBuilder = StringBuilder()

    init {
        stringBuilder.clear()

        stringBuilder.append(BEGIN_VEVENT)
        stringBuilder.append("\n")

    }

    fun setSummary(summary: String): DevAppsVEventBuilder {
        if(summary.isNotBlank()) {
            stringBuilder.append(SUMMARY)
            stringBuilder.append(summary)
            stringBuilder.append("\n")
        }
        return this
    }

    fun setDtStart(dtStart: String): DevAppsVEventBuilder {
        if(dtStart.isNotBlank()) {
            stringBuilder.append(DTSTART)
            stringBuilder.append(dtStart)
            stringBuilder.append("\n")
        }
        return this
    }

    fun setDtEnd(dtEnd: String): DevAppsVEventBuilder {
        if(dtEnd.isNotBlank()) {
            stringBuilder.append(DTEND)
            stringBuilder.append(dtEnd)
            stringBuilder.append("\n")
        }
        return this
    }

    fun setLocation(location: String): DevAppsVEventBuilder {
        if(location.isNotBlank()) {
            stringBuilder.append(LOCATION)
            stringBuilder.append(location)
            stringBuilder.append("\n")
        }
        return this
    }

    fun setDescription(description: String): DevAppsVEventBuilder {
        if(description.isNotBlank()) {
            stringBuilder.append(DESCRIPTION)
            stringBuilder.append(description)
            stringBuilder.append("\n")
        }
        return this
    }

    fun build(): String {
        stringBuilder.append(END_VEVENT)
        return stringBuilder.toString()
    }
}