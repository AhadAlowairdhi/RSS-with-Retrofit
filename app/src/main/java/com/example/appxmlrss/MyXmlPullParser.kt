package com.example.appxmlrss

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class MyXmlPullParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Movie> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): List<Movie> {

        val movie = mutableListOf<Movie>()

        parser.require(XmlPullParser.START_TAG, ns, "rss")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "channel") {
                parser.require(XmlPullParser.START_TAG, ns, "channel")
                var title: String? = null
                var link: String? = null
                var description: String? = null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "title" -> title = readTitle(parser)
                        "link" -> link = readLink(parser)
                        "description" -> description = readDescription(parser)
                        else -> skip(parser)
                    }
                }
                movie.add(Movie(title,link,description))
            } else {
                skip(parser)
            }
        }
        return movie
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:name")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:name")
        return summary
    }
    private fun readDescription(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:name")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:name")
        return summary
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}