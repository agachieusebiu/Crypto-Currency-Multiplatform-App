package org.example.project.core.domain

/** A sealed interface representing the result of an operation that can either be a success with data of type [D] or an error with type [E].
 *
 * @param D The type of data contained in a successful result.
 * @param E The type of error contained in an error result, which must implement the [Error] interface.
 */
sealed interface Result<out D, out E : Error> {

    /** Represents a successful result containing data of type [D].
     */
    data class Success<out D>(
        val data: D
    ) : Result<D, Nothing>

    /** Represents an error result containing an error of type [E].
     */
    data class Error<out E : org.example.project.core.domain.Error>(
        val error: E
    ) : Result<Nothing, E>
}

/** Maps the successful data of the [Result] to a new type [R] using the provided [map] function.
 * If the [Result] is an error, it returns the error unchanged.
 *
 * @param map A function that takes the successful data of type [T] and returns a new type [R].
 * @return A [Result] containing either the mapped data of type [R] or the original error of type [E].
 */
inline fun <T, E : Error, R> Result<T, E>.map(
    map: (T) -> R
): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/** Converts a [Result] with data of type [T] to an [EmptyResult] by discarding the data.
 *
 * @return An [EmptyResult] containing either success with no data or the original error of type [E].
 */
fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

/** Executes the given [action] if the [Result] is a success, passing the successful data to the action.
 *
 * @param action A function that takes the successful data of type [T] and returns Unit.
 * @return The original [Result], unchanged.
 */
inline fun <T, E : Error> Result<T, E>.onSuccess(
    action: (T) -> Unit
): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

/** Executes the given [action] if the [Result] is an error, passing the error to the action.
 *
 * @param action A function that takes the error of type [E] and returns Unit.
 * @return The original [Result], unchanged.
 */
inline fun <T, E : Error> Result<T, E>.onError(
    action: (E) -> Unit
): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>