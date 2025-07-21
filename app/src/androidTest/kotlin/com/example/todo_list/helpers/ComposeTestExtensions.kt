package com.example.todo_list.helpers

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import io.github.kakaocup.compose.node.element.KNode

fun KNode.assertText(expectedText: String) {
    assert(SemanticsMatcher("Text or Label contains '$expectedText'") { node ->
        val text = node.config.getOrNull(SemanticsProperties.Text)
        text?.any { it.text == expectedText } == true
    })
}

fun KNode.assertContentDescription(expectedText: String) {
    assert(SemanticsMatcher("Text or Label contains '$expectedText'") { node ->
        val contentDescription = node.config.getOrNull(SemanticsProperties.ContentDescription)
        contentDescription?.any { it == expectedText } == true
    })
}

fun KNode.assertEditableText(expectedText: String) {
    assert(SemanticsMatcher("Text or Label contains '$expectedText'") { node ->
        val editableText = node.config.getOrNull(SemanticsProperties.EditableText)
        editableText?.text == expectedText
    })
}