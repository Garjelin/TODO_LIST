package com.example.todo_list.uiTests

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.todo_list.MainActivity
import org.junit.Rule
import org.junit.Test

class TaskListScreenComposeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testTaskListScreenUI() {
        composeTestRule.onRoot().printToLog("DEBUG")
//         Проверка TextField
        composeTestRule.onNode(
            hasTestTag("TaskInput"),
            useUnmergedTree = true
        ).assertIsDisplayed()

        // Проверка кнопки Add Task
        composeTestRule.onNode(
            hasTestTag("AddTaskButton")
        ).assertIsDisplayed().assertIsEnabled()

        // Проверка метки TextField
        composeTestRule.onNode(
            hasTestTag("TaskInputLabel"),
            useUnmergedTree = true
        ).assertExists()

//        // Без testTag (альтернатива с ролью и текстом)
//        composeTestRule.onNode(
//            SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button) and hasText("Enter new task")
//        ).assertIsDisplayed()
    }

    @Test
    fun testAddAndDeleteTask() {
        // Добавление задачи
        composeTestRule.onNode(
            hasTestTag("TaskInput"),
            useUnmergedTree = true
        ).performTextInput("Test Task")

        composeTestRule.onNode(
            hasTestTag("AddTaskButton")
        ).performClick()

        // Проверка, что задача добавлена
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodes(hasTestTag("TaskRow_1")).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNode(
            hasTestTag("TaskTitle_1")
        ).assertIsDisplayed().assertTextEquals("Test Task")

        // Удаление задачи
        composeTestRule.onNode(
            hasTestTag("DeleteTaskButton_1")
        ).performClick()

        // Проверка, что задача удалена
        composeTestRule.onNode(
            hasTestTag("TaskRow_1")
        ).assertDoesNotExist()
    }
}