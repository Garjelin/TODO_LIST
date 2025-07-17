package com.example.todo_list.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Assert.assertEquals

object ComposeTestHolder {
    lateinit var composeTestRule: ComposeTestRule
}

fun KNode.assertLabelText(expectedText: String) {
    val semanticsNode = ComposeTestHolder.composeTestRule.onNodeWithText(expectedText).fetchSemanticsNode()
    val textList = semanticsNode.config.getOrNull(SemanticsProperties.Text)
    assertEquals(expectedText, textList!![0].text)
}