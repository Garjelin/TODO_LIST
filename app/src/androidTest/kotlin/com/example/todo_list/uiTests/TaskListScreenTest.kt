package com.example.todo_list.uiTests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import assertLabelText
import com.example.todo_list.MainActivity
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
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
//        val taskInputLabel: KNode = child { hasTestTag("TaskInputLabel") }
        val taskInputLabel: KNode = child { hasText("Enter new task") }
        val addTaskButton: KNode = child { hasTestTag("AddTaskButton") }

        // Динамические ноды: добавляем optional block для поддержки DSL
        fun taskTitle(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("TaskTitle_$taskId") }
            node.invoke(block)  // Применяем блок (если передан)
            return node  // Возвращаем ноду для цепочек или присваивания
        }

        fun deleteTaskButton(taskId: Int, block: KNode.() -> Unit = {}): KNode {
            val node = child<KNode> { hasTestTag("DeleteTaskButton_$taskId") }
            node.invoke(block)  // Применяем блок (если передан)
            return node  // Возвращаем ноду
        }
    }

    private val taskListScreen = TaskListScreen()

    @Test
    fun testTaskListScreenUI() = run {
        step("Check TaskListScreen is displayed") {
            taskListScreen {
                taskInput { assertIsDisplayed() }
                addTaskButton { assertIsDisplayed() }
                addTaskButton { assertIsEnabled() }
            }
        }
        step("Check task input label has correct text") {
            flakySafely(timeoutMs = 10_000) {  // Повторяет до 10 сек, если flaky
                taskListScreen {
                    taskInputLabel {
                        assertLabelText("Enter new taskq")
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
        step("Check task is added") {
            taskListScreen {
                taskTitle(1) { assertIsDisplayed() }
                taskTitle(1) { assertTextEquals("Test Task") }
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