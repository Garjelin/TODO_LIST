import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Assert.assertEquals

object ComposeTestHolder {
    lateinit var composeTestRule: ComposeTestRule
}

fun KNode.assertLabelText(expectedText: String) {
    try {
        // Fetch the node with the expected text
        val semanticsNode = ComposeTestHolder.composeTestRule
            .onNodeWithText(expectedText, useUnmergedTree = true)
            .fetchSemanticsNode()

        // Get the text from the semantics node
        val textList = semanticsNode.config.getOrNull(SemanticsProperties.Text)
        if (textList.isNullOrEmpty()) {
            throw AssertionError("No text found in the node. Expected: '$expectedText'")
        } else {
            val actualText = textList[0].text
            assertEquals("Expected text '$expectedText' but found '$actualText'", expectedText, actualText)
        }
    } catch (e: AssertionError) {
        // If the node wasn't found, collect available text from all nodes for a better error message
        val allNodes = ComposeTestHolder.composeTestRule
            .onAllNodesWithText("", useUnmergedTree = true, substring = true, ignoreCase = true)
            .fetchSemanticsNodes()

        val actualTexts = allNodes.mapNotNull { node ->
            node.config.getOrNull(SemanticsProperties.Text)?.getOrNull(0)?.text
        }.joinToString(", ")

        throw AssertionError(
            """
            Failed: assertLabelText.
            Reason: Expected exactly '1' node with text '$expectedText' but could not find any node.
            Actual texts found in UI: [$actualTexts]
            """.trimIndent()
        )
    }
}