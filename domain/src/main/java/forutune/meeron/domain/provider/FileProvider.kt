package forutune.meeron.domain.provider

interface FileProvider {
    fun getFileName(uriString: String): String
    fun getMediaType(uriString: String): String
    fun getPath(uriString: String): String?
}