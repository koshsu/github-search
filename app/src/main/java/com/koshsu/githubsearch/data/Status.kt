package com.koshsu.githubsearch.data

// Status of Data in Repository
sealed class Status <M> {

    // Status - Loading
    class Loading<M> : Status<M>()

    // Status - Success
    data class Success<M>(val data: M) : Status<M>()

    // Status - Error
    data class Error<M>(val errorMessage: String) : Status<M>()

    companion object {

        // Returns Loading instance
        fun <M> loading() = Loading<M>()

        // Returns Success instance with data to emit
        fun <M> success(data: M) =
            Success(data)

        // Returns Error instance with error message
        fun <M> error(errorMessage: String) =
            Error<M>(errorMessage)

    }

}