package com.minlish.data.api.model

import com.minlish.domain.model.WordResponse
import com.minlish.domain.model.Meaning
import com.minlish.domain.model.Definition

data class TraCauResponse(
    val language: String?,
    val tratu: List<TraCauEntry>?
)

data class TraCauEntry(
    val fields: TraCauFields?
)

data class TraCauFields(
    val fulltext: String?,
    val kinds: String?,
    val word: String?
)

fun cleanHtml(html: String): String {
    var text = html
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
        .replace("&nbsp;", " ")
        .replace(" ", " ") // non-breaking space
    
    // Remove all HTML tags
    text = text.replace(Regex("<[^>]*>"), "")
    
    // Trim leading/trailing whitespace
    return text.trim()
}

fun extractSectionHtml(html: String, sectionId: String): String? {
    val regex = Regex("""<(article|div)[^>]*id=["']${sectionId}["'][^>]*>(.*?)</\1>""", RegexOption.DOT_MATCHES_ALL)
    return regex.find(html)?.groupValues?.get(2)
}

fun parseTableDictionary(sectionHtml: String): List<Meaning> {
    val meanings = mutableListOf<Meaning>()
    val trRegex = Regex("""<tr[^>]*id=["']([^"']+)["'][^>]*>(.*?)</tr>""", RegexOption.DOT_MATCHES_ALL)
    
    var currentPartOfSpeech = ""
    val currentDefinitions = mutableListOf<Definition>()
    
    var currentDefinitionText = ""
    val currentExamples = mutableListOf<String>()
    
    val flushDefinition = {
        if (currentDefinitionText.isNotEmpty()) {
            currentDefinitions.add(
                Definition(
                    definition = currentDefinitionText,
                    example = currentExamples.firstOrNull()
                )
            )
            currentDefinitionText = ""
            currentExamples.clear()
        }
    }
    
    val flushMeaning = {
        flushDefinition()
        if (currentDefinitions.isNotEmpty()) {
            meanings.add(
                Meaning(
                    partOfSpeech = if (currentPartOfSpeech.isEmpty()) "Từ loại" else currentPartOfSpeech,
                    definitions = currentDefinitions.toList()
                )
            )
            currentDefinitions.clear()
        }
    }
    
    for (match in trRegex.findAll(sectionHtml)) {
        val id = match.groupValues[1]
        val innerHtml = match.groupValues[2]
        val cleanText = cleanHtml(innerHtml)
        
        when (id) {
            "tl" -> {
                flushMeaning()
                currentPartOfSpeech = cleanText
            }
            "mn" -> {
                flushDefinition()
                currentDefinitionText = cleanText
            }
            "mh" -> {
                currentExamples.add(cleanText)
            }
            "mh_n" -> {
                if (currentExamples.isNotEmpty()) {
                    val idx = currentExamples.lastIndex
                    currentExamples[idx] = "${currentExamples[idx]}: $cleanText"
                }
            }
            "tn" -> {
                currentExamples.add(cleanText)
            }
            "tn_n" -> {
                if (currentExamples.isNotEmpty()) {
                    val idx = currentExamples.lastIndex
                    currentExamples[idx] = "${currentExamples[idx]}: $cleanText"
                }
            }
        }
    }
    
    flushMeaning()
    return meanings
}

fun parseListDictionary(sectionHtml: String): List<Meaning> {
    val meanings = mutableListOf<Meaning>()
    val lines = sectionHtml.replace("<br>", "\n").replace("<br/>", "\n").split("\n")
    
    var currentPartOfSpeech = ""
    val currentDefinitions = mutableListOf<Definition>()
    
    val flushMeaning = {
        if (currentDefinitions.isNotEmpty()) {
            meanings.add(
                Meaning(
                    partOfSpeech = if (currentPartOfSpeech.isEmpty()) "Từ loại" else currentPartOfSpeech,
                    definitions = currentDefinitions.toList()
                )
            )
            currentDefinitions.clear()
        }
    }
    
    for (line in lines) {
        val cleanLine = cleanHtml(line)
        if (cleanLine.isEmpty()) continue
        
        if (line.contains("*") || line.contains("<b>") && (line.contains("#1a76bf") || line.contains("#1a76bf"))) {
            flushMeaning()
            currentPartOfSpeech = cleanLine.replace("*", "").trim()
        } else if (cleanLine.startsWith("-")) {
            val definitionText = cleanLine.removePrefix("-").trim()
            if (definitionText.isNotEmpty()) {
                currentDefinitions.add(Definition(definition = definitionText, example = null))
            }
        } else {
            if (currentPartOfSpeech.isNotEmpty()) {
                currentDefinitions.add(Definition(definition = cleanLine, example = null))
            }
        }
    }
    
    flushMeaning()
    return meanings
}

fun parseEnglishDictionary(sectionHtml: String): List<Meaning> {
    val meanings = mutableListOf<Meaning>()
    val lines = sectionHtml.replace("<br>", "\n").replace("<br/>", "\n").split("\n")
    
    var currentPartOfSpeech = ""
    val currentDefinitions = mutableListOf<Definition>()
    
    val flushMeaning = {
        if (currentDefinitions.isNotEmpty()) {
            meanings.add(
                Meaning(
                    partOfSpeech = if (currentPartOfSpeech.isEmpty()) "Definition" else currentPartOfSpeech,
                    definitions = currentDefinitions.toList()
                )
            )
            currentDefinitions.clear()
        }
    }
    
    for (line in lines) {
        val cleanLine = cleanHtml(line)
        if (cleanLine.isEmpty()) continue
        
        if (cleanLine.startsWith("■")) {
            flushMeaning()
            currentPartOfSpeech = cleanLine.replace("■", "").trim()
        } else if (cleanLine.contains("》")) {
            val definitionText = cleanLine.substringAfter("》").trim()
            if (definitionText.isNotEmpty()) {
                currentDefinitions.add(Definition(definition = definitionText, example = null))
            }
        } else if (Regex("""^\d+\.""").containsMatchIn(cleanLine)) {
            val definitionText = cleanLine.replaceFirst(Regex("""^\d+\."""), "").trim()
            if (definitionText.isNotEmpty()) {
                currentDefinitions.add(Definition(definition = definitionText, example = null))
            }
        } else {
            if (cleanLine.startsWith("(") && cleanLine.endsWith(")")) continue
            
            if (currentPartOfSpeech.isNotEmpty()) {
                currentDefinitions.add(Definition(definition = cleanLine, example = null))
            }
        }
    }
    
    flushMeaning()
    return meanings
}

fun TraCauResponse.toWordResponse(): WordResponse? {
    val entry = tratu?.firstOrNull() ?: return null
    val fields = entry.fields ?: return null
    val wordStr = fields.word ?: return null
    val html = fields.fulltext ?: ""
    
    // Extract phonetic
    val paRegex = Regex("""<tr[^>]*id=["']pa[k]?["'][^>]*>.*?(\[[^\]]+\])""", RegexOption.DOT_MATCHES_ALL)
    var phonetic = paRegex.find(html)?.groupValues?.get(1)
    if (phonetic == null) {
        val generalPhoneticRegex = Regex("""\[([a-zA-Z0-9\s'/,.:\-əɪʊæɑɒɔʌɜθðʃʒŋɡ]+)\]""")
        phonetic = generalPhoneticRegex.find(html)?.value
    }
    
    val allMeanings = mutableListOf<Meaning>()
    
    // dict_ev
    val evHtml = extractSectionHtml(html, "dict_ev")
    if (evHtml != null) {
        allMeanings.addAll(parseTableDictionary(evHtml))
    }
    
    // dict_cn
    val cnHtml = extractSectionHtml(html, "dict_cn")
    if (cnHtml != null) {
        allMeanings.addAll(parseListDictionary(cnHtml))
    }
    
    // dict_aa
    val aaHtml = extractSectionHtml(html, "dict_aa")
    if (aaHtml != null) {
        allMeanings.addAll(parseEnglishDictionary(aaHtml))
    }
    
    var meanings = allMeanings.toList()
    
    // Heuristic: Sort/Reorder to push "Từ loại: như ..." definitions to the bottom if there are better ones
    val hasDescriptiveMeaning = meanings.any { 
        it.partOfSpeech != "Từ loại" && !it.definitions.any { d -> d.definition.startsWith("như ") || d.definition.startsWith("xem ") } 
    }
    if (hasDescriptiveMeaning) {
        meanings = meanings.sortedBy { 
            it.partOfSpeech == "Từ loại" || it.definitions.any { d -> d.definition.startsWith("như ") || d.definition.startsWith("xem ") }
        }
    }
    
    if (meanings.isEmpty()) {
        meanings = listOf(
            Meaning(
                partOfSpeech = "Word",
                definitions = listOf(
                    Definition(definition = "No definition found.", example = null)
                )
            )
        )
    }
    
    return WordResponse(
        word = wordStr,
        phonetic = phonetic,
        phonetics = emptyList(),
        meanings = meanings
    )
}
