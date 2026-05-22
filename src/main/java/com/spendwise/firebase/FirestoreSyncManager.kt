package com.spendwise.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spendwise.models.Expense
import kotlinx.coroutines.tasks.await

class FirestoreSyncManager(
    private val auth: FirebaseAuth? = runCatching { FirebaseAuth.getInstance() }.getOrNull(),
    private val firestore: FirebaseFirestore? = runCatching { FirebaseFirestore.getInstance() }.getOrNull()
) {
    private fun collection() = firestore
        ?.collection("users")
        ?.document(auth?.currentUser?.uid.orEmpty())
        ?.collection("expenses")

    suspend fun backup(expenses: List<Expense>) {
        val user = auth?.currentUser ?: return
        val firestore = firestore ?: return
        val collection = collection() ?: return
        val batch = firestore.batch()
        expenses.forEach { batch.set(collection.document(it.id.toString()), it.copy(userId = user.uid, synced = true)) }
        batch.commit().await()
    }

    suspend fun restore(): List<Expense> {
        if (auth?.currentUser == null) return emptyList()
        return collection()?.get()?.await()?.toObjects(Expense::class.java).orEmpty()
    }
}
