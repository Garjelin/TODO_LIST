package com.example.todo_list.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import io.github.kakaocup.compose.node.element.KNode

fun KNode.assertLabelText(expectedText: String) {
    assert(SemanticsMatcher("Text or Label contains '$expectedText'") { node ->
        val textList = node.config.getOrNull(SemanticsProperties.Text)
        val editableText = node.config.getOrNull(SemanticsProperties.EditableText)
        val contentDescription = node.config.getOrNull(SemanticsProperties.ContentDescription)
        textList?.any { it.text == expectedText } == true ||
                editableText?.text == expectedText ||
                contentDescription?.any { it == expectedText } == true
    })
}