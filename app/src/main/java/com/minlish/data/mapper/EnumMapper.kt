package com.minlish.data.mapper

inline fun <reified T : Enum<T>> parseEnum(value: String?, default: T): T {
    if (value.isNullOrBlank()) {
        return default
    }
    return runCatching { enumValueOf<T>(value) }.getOrDefault(default)
}

