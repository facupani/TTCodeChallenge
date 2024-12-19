import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.*

@Serializable
data class User(val id: Int, val name: String, val address: Address)

@Serializable
data class Address(val city: String)

fun filterUsersByCity(users: List<User>, city: String): List<User> {
    return users.filter { it.address.city.equals(city, ignoreCase = true) }
}

fun main() = runBlocking {
    val users = getUsers()
    val filteredUsers = filterUsersByCity(users, "South Christy")

    parseJsonString(userString)

    if (filteredUsers.isEmpty()) {
        println("No users found in the specified city.")
    } else {
        filteredUsers.forEach { user ->
            println("Username: ${user.name}, City: ${user.address.city}, UserID: ${user.id}, ")
        }
    }
}

val userString = """{
    "id": 6,
    "name": "Mrs. Dennis Schulist",
    "username": "Leopoldo_Corkery",
    "email": "Karley_Dach@jasper.info",
    "address": {
      "street": "Norberto Crossing",
      "suite": "Apt. 950",
      "city": "South Christy",
      "zipcode": "23505-1337",
      "geo": {
        "lat": "-71.4197",
        "lng": "71.7478"
      }
    },
    "phone": "1-477-935-8478 x6430",
    "website": "ola.org",
    "company": {
      "name": "Considine-Lockman",
      "catchPhrase": "Synchronised bottom-line interface",
      "bs": "e-enable innovative applications"
    }
  }"""

fun parseJsonString(jsonString: String): List<User>? {
    return try {
        Json.decodeFromString<List<User>>(jsonString)
    } catch (e: Exception) {
        println("Error parsing JSON: ${e.message}")
        null
    }
}

suspend fun getUsers(): List<User> {
    val client = HttpClient(CIO)

    return try {
        val response: String = client.get("https://jsonplaceholder.typicode.com/users").toString()
        Json.decodeFromString(response)
    } catch (e: Exception) {
        println("Getting user data from API Failed: ${e.message}")
        emptyList()
    } finally {
        client.close()
    }
}
