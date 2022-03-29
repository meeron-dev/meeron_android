package fourtune.merron.data.di

import forutune.meeron.domain.model.MeeronError
import retrofit2.*
import java.lang.reflect.Type

object ErrorHandlingCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val actual = retrofit.nextCallAdapter(this, returnType, annotations) as CallAdapter<Any, Any>? ?: return null
        return object : CallAdapter<Any, Any> by actual {
            override fun adapt(call: Call<Any>): Any {
                val adapt = actual.adapt(call)
                return if (adapt is Call<*>) {
                    adapt.revise(retrofit)
                } else {
                    adapt
                }
            }
        }

    }
}


private fun <T> Call<T>.revise(retrofit: Retrofit): Call<T> {
    val actual = this

    return object : Call<T> by actual {
        override fun enqueue(callback: Callback<T>) {
            actual.enqueue(callback.revise(retrofit))
        }

        override fun execute(): Response<T> = actual.execute().also { response ->
            // prepare MeeronError if available
            response.getMeeronError(retrofit)
        }
    }
}

private fun <T> Response<T>.getMeeronError(retrofit: Retrofit): MeeronError? = runCatching {
    val errorBody = errorBody() ?: return null
    val converter = retrofit.responseBodyConverter<MeeronError>(MeeronError::class.java, emptyArray())
    val errorRes = converter.convert(errorBody)

    errorRes
}.getOrNull()

private fun <T> Callback<T>.revise(retrofit: Retrofit): Callback<T> {
    val actual = this

    return object : Callback<T> by actual {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val neroError = response.getMeeronError(retrofit)

            if (neroError == null)
                actual.onResponse(call, response)
            else
                actual.onFailure(call, neroError)
        }
    }
}

