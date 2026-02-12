package org.example.project.core.util

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_disk_full
import kotlinproject.composeapp.generated.resources.error_insufficient_balance
import kotlinproject.composeapp.generated.resources.error_no_internet
import kotlinproject.composeapp.generated.resources.error_request_timeout
import kotlinproject.composeapp.generated.resources.error_serialization
import kotlinproject.composeapp.generated.resources.error_too_many_requests
import kotlinproject.composeapp.generated.resources.error_unknown
import org.example.project.core.domain.DataError
import org.jetbrains.compose.resources.StringResource

/**
 * Extension function to convert a DataError instance into a corresponding StringResource for UI display.
 * This function maps each specific DataError case to a predefined string resource, allowing for user-friendly error messages in the UI.
 *
 * @return A StringResource that corresponds to the specific DataError instance.
 */
fun DataError.toUiText(): StringResource {
    val stringRes = when (this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> Res.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
        DataError.Local.INSUFFICIENT_FUNDS -> Res.string.error_insufficient_balance
    }
    return stringRes
}