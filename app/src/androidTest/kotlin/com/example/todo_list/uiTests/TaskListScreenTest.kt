package com.example.todo_list.uiTests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.todo_list.MainActivity
import com.example.todo_list.helpers.ComposeTestHolder
import com.example.todo_list.helpers.assertLabelText
import com.example.todo_list.ui.TaskDetailScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskListScreenTest : TestCase(kaspressoBuilder = Kaspresso.Builder.simple()) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        ComposeTestHolder.composeTestRule = composeTestRule
    }


    // Inner-класс для экрана
    inner class TaskListScreen : ComposeScreen<TaskListScreen>(composeTestRule) {
        // Статические ноды (остаются без изменений)
        val taskInput: KNode = child { hasTestTag("TaskInput") }
        val taskInputLabel: KNode = child { hasTestTag("TaskInputLabel") }

        //        val taskInputLabel: KNode = child { hasText("Enter new task") }
        val addTaskButton: KNode = child { hasTestTag("AddTaskButton") }

        // Динамические ноды: добавляем optional block для поддержки DSL
        fun taskTitle(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskRow_$taskId") }
            node.invoke(block)
            return node
        }

        fun deleteTaskButton(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("DeleteTaskButton_$taskId") }
            node.invoke(block)  // Применяем блок (если передан)
            return node  // Возвращаем ноду
        }
    }

    // Inner-класс для экрана
    inner class TaskDetailScreen : ComposeScreen<TaskDetailScreen>(composeTestRule) {
        fun taskTitle(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskTitleInput_$taskId") }
            node.invoke(block)
            return node
        }
    }

    private val taskListScreen = TaskListScreen()
    private val taskDetailScreen = TaskDetailScreen()

    @Test
    fun testTaskListScreenUI() = run {
        step("Check TaskListScreen is displayed") {
            taskListScreen {
                taskInput { assertIsDisplayed() }
                addTaskButton { assertIsDisplayed() }
                addTaskButton { assertIsEnabled() }
                taskInputLabel {
                    flakySafely(10_000) {
                        assertLabelText("Enter new task")
                    }
                }
            }
        }
    }

    @Test
    fun testAddAndDeleteTask() = run {
        step("Add a new task") {
            taskListScreen {
                taskInput { performTextInput("Test Task") }
                addTaskButton { performClick() }
            }
        }
//        composeTestRule.waitUntil(1000000) { false }
        step("Check task is added") {
            taskListScreen {
//                composeTestRule.onRoot().printToLog("DEBUG")
                taskTitle(1) {
                    flakySafely(10_000) {
                        assertIsDisplayed()
                        assertTextEquals("Test Task")
                    }
                }
            }
        }
        step("Delete the task") {
            taskListScreen {
                deleteTaskButton(1) { performClick() }
            }
        }
        step("Check task is removed") {
            taskListScreen {
                taskTitle(1) { assertDoesNotExist() }
            }
        }
    }
}