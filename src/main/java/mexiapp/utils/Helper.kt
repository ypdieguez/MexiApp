package mexiapp.utils

class Helper {
    companion object Result {
        fun getCode(result: String) =
                result.substringAfter("Cédigo Seguridad: ").substringBefore("\n")
                        .replace(" ", "").toLowerCase()

        fun getToken(result: String) =
                result.substringAfter("Token: ").substringBefore("\n")
                        .replace(" ", "")
    }
}