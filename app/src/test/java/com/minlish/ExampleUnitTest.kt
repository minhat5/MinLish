package com.minlish

import com.minlish.data.api.model.TraCauResponse
import com.minlish.data.api.model.TraCauEntry
import com.minlish.data.api.model.TraCauFields
import com.minlish.data.api.model.toWordResponse
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun testTraCauParser_hello() {
        val html = """<script>var tcGT = {"name":"hello","type":0,"children":[{"name":"hi","type":2}]}</script><article id="dict_ev" data-tab-name="Anh - Việt" class="tcTab--slide"><div id="ev"><div class="dict--title">Từ điển Anh - Việt</div><div class="dict--content"><table id="definition" border="0"><tr id="pa"><td id="I_C"><font color="#9e9e9e">◘</font></td><td id="C_C" colspan="2"><font color="#9e9e9e">[hə'lou]</font></td></tr><tr id="cvk"><td> </td><td id="C_C" colspan="2"><font color="#9e9e9e">Cách viết khác:</font></td></tr><tr id="cvk_t"><td> </td><td id="C_C" colspan="2"><font color="#1371BB">hallo</font></td></tr><tr id="pak"><td id="I_C"><font color="#9e9e9e">◘</font></td><td id="C_C" colspan="2"><font color="#9e9e9e">[hə'lou]</font></td></tr><tr id="mn"><td> </td><td id="I_C"><font color="#999">■</font></td><td id="C_C">như <font color="#1371BB">hallo</font></td></tr></table><i></i></div></div></article><div id="dict_di"><k>hello</k><br><blockquote><b><cclass> Australian Slang from Babilon  </cclass></b></blockquote><br><blockquote><blockquote>(interjection) interrogative expressing disbelief: 'We were told that if we laminated our citizenship certificate it would become invalid. Hello?'</blockquote></blockquote></div><article id="dict_cn" data-tab-name="Chuyên ngành" class="tcTab--slide"><div id="ce"><div class="dict--title">Chuyên ngành</div><div class="dict--content"><font color="#1a76bf">*</font><font color="#1a76bf"><b> thán từ</b></font><br>  - <font color="#595959">chào anh!, chào chị!</font><br>  - <font color="#595959">này, này</font><br>  - <font color="#595959">ô này! (tỏ ý ngạc nhiên)</font><br><font color="#1a76bf">*</font><font color="#1a76bf"><b> danh từ</b></font><br>  - <font color="#595959">tiếng chào</font><br>  - <font color="#595959">tiếng gọi "này, này" !</font><br>  - <font color="#595959">tiếng kêu ô này "! (tỏ ý ngạc nhiên)</font><br><font color="#1a76bf">*</font><font color="#1a76bf"><b> nội động từ</b></font><br>  - <font color="#595959">chào</font><br>  - <font color="#595959">gọi "này, này"</font><br>  - <font color="#595959">kêu "ô này" (tỏ ý ngạc nhiên)</font></div></div></article>"""

        val response = TraCauResponse(
            language = "eng",
            tratu = listOf(
                TraCauEntry(
                    fields = TraCauFields(
                        fulltext = html,
                        kinds = "ev|di|cn",
                        word = "hello"
                    )
                )
            )
        )

        val wordResponse = response.toWordResponse()
        assertNotNull(wordResponse)
        assertEquals("hello", wordResponse?.word)
        assertEquals("[hə'lou]", wordResponse?.phonetic)
        
        val meanings = wordResponse?.meanings ?: emptyList()
        assertTrue(meanings.isNotEmpty())
        
        // The first meaning should be "thán từ" because the cross-referenced "Từ loại" was sorted to the bottom
        assertEquals("thán từ", meanings[0].partOfSpeech)
        assertEquals("chào anh!, chào chị!", meanings[0].definitions[0].definition)
        assertEquals("này, này", meanings[0].definitions[1].definition)
        
        // "Từ loại: như hallo" should be the last meaning
        assertEquals("Từ loại", meanings.last().partOfSpeech)
        assertEquals("như hallo", meanings.last().definitions[0].definition)
    }
}