package com.example.reminderhabit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.reminderhabit.model.EmailMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmailViewModel : ViewModel() {

        // StateFlow to hold the list of email messages
        private val _messagesState = MutableStateFlow(emptyList<EmailMessage>())
        val messagesState: StateFlow<List<EmailMessage>> = _messagesState.asStateFlow()

        init {
                // Initialize the list of email messages
                // when the ViewModel is created
                _messagesState.update { sampleMessages() }
        }

        /**
         * Refreshes the list of email messages.
         */
        fun refresh() {
                _messagesState.update {
                        sampleMessages()
                }
        }

        /**
         * Removes an email message from the list.
         * @param currentItem The email message to be removed.
         */
        fun removeItem(currentItem: EmailMessage) {
                _messagesState.update {
                        val mutableList = it.toMutableList()
                        mutableList.remove(currentItem)
                        mutableList
                }
        }

        /**
         * Generates a list of sample email messages.
         * @return The list of sample email messages.
         */
        private fun sampleMessages() = listOf(
                EmailMessage("John Doe", "Hello"),
                EmailMessage("Alice", "Hey there! How's it going?"),
                EmailMessage("Bob", "I just discovered a cool new programming language!"),
                EmailMessage("Geek", "Have you seen the latest tech news? It's fascinating!"),
                EmailMessage("Mark", "Let's grab a coffee and talk about coding!"),
                EmailMessage("Cyan", "I need help with a coding problem. Can you assist me?"),
        )
}
