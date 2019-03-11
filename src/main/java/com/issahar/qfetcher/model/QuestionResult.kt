package com.issahar.qfetcher.model


// Data objects that Jackson uses to return the desired response
data class QuestionsResult(val questions: List<QuestionResult>)

data class QuestionResult (val value: String,
                           val source: String)
