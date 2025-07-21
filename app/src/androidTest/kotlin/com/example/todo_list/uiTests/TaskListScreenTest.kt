package com.example.todo_list.uiTests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.todo_list.MainActivity
import com.example.todo_list.helpers.assertEditableText
import com.example.todo_list.helpers.assertText
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import org.junit.Rule
import org.junit.Test

class TaskListScreenTest : TestCase(kaspressoBuilder = Kaspresso.Builder.simple()) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // Inner-класс для экрана
    inner class TaskListScreen : ComposeScreen<TaskListScreen>(composeTestRule) {

        val taskInput: KNode = child { hasTestTag("TaskInput") }
        val addTaskButton: KNode = child { hasTestTag("AddTaskButton") }

        fun taskRow(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskRow_$taskId") }
            node.invoke(block)
            return node
        }
        fun deleteTaskButton(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("DeleteTaskButton_$taskId") }
            node.invoke(block)  // Применяем блок (если передан)
            return node  // Возвращаем ноду
        }
        fun taskCheckbox(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskCheckbox_$taskId") }
            node.invoke(block)
            return node
        }
    }

    // Inner-класс для экрана
    inner class TaskDetailScreen : ComposeScreen<TaskDetailScreen>(composeTestRule) {
        val taskCompletedCheckbox: KNode = child { hasTestTag("TaskCompletedCheckbox") }
        val taskCompletedLabel: KNode = child { hasTestTag("TaskCompletedLabel") }
        val saveTaskButton: KNode = child { hasTestTag("SaveTaskButton") }
        val backButton: KNode = child { hasTestTag("BackButton") }

        fun taskTitleInput(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskTitleInput_$taskId") }
            node.invoke(block)
            return node
        }
    }

    private val TaskListScreenClass = TaskListScreen()
    private val TaskDetailScreenClass = TaskDetailScreen()

    @Test
    fun testTaskListScreenUI() = run {
        step("Check TaskListScreen is displayed") {
            composeTestRule.onRoot().printToLog("DEBUG")
            TaskListScreenClass {
                taskInput {
                    assertIsDisplayed()
                    assertText("Enter new task")
                }
                addTaskButton { assertIsDisplayed() }
                addTaskButton { assertIsEnabled() }
            }
        }
    }

    @Test
    fun testAddAndDeleteTask() = run {
        step("Add a new task") {
            TaskListScreenClass {
                taskInput {
                    performTextInput("Test Task")
                }
                addTaskButton {
                    performClick()
                }
            }
        }
//        composeTestRule.waitUntil(1000000) { false }
        step("Check task is added") {
            TaskListScreenClass {
//                composeTestRule.onRoot().printToLog("DEBUG")
                taskRow(1) {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Test Task")
                    }
                }
            }
        }
        step("Delete the task") {
            TaskListScreenClass {
                deleteTaskButton(1) { performClick() }
            }
        }
        step("Check task is removed") {
            TaskListScreenClass {
                taskRow(1) { assertDoesNotExist() }
            }
        }
    }

    @Test
    fun testEditTask() = run {
        step("Add a new task") {
            TaskListScreenClass {
                taskInput {
                    performTextInput("Original Task")
                }
                addTaskButton {
                    performClick()
                }
            }
        }
        step("Navigate to TaskDetailScreen") {
            TaskListScreenClass {
                taskRow(1) {
                    performClick()
                }
            }
        }
//        composeTestRule.waitUntil(1000000) { false }
        step("Check TaskDetailScreen is displayed") {
            TaskDetailScreenClass {
                taskTitleInput(1) {
                    assertIsDisplayed()
                    assertEditableText("Original Task")
                }
                taskCompletedCheckbox {
                    assertIsDisplayed()
                }
                taskCompletedLabel {
                    assertTextEquals("Completed")
                }
                saveTaskButton {
                    assertIsEnabled()
                }
                backButton {
                    assertIsEnabled()
                }
            }
        }
        step("Edit task title and completion status") {
            TaskDetailScreenClass {
                taskTitleInput(1) {
                    performTextReplacement("Updated Task")
//                    performTextInput("Updated Task")
                }
                taskCompletedCheckbox {
                    performClick()
                }
                saveTaskButton {
                    performClick()
                }
            }
        }
        step("Return to TaskListScreen") {
            TaskDetailScreenClass {
                backButton {
                    performClick()
                }
            }
        }
        step("Check task is updated") {
            TaskListScreenClass {
                taskRow(1) {
                    assertTextEquals("Updated Task")
                }
                taskCheckbox(1) {
                    assertIsToggleable()
                    assertIsOn()
                }
            }
        }
    }
}