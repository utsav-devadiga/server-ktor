package test

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import test.plugins.*
import kotlin.random.Random

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureMonitoring()
    configureSerialization()
    configureRouting()

    routing {
        val productStorage = Storage()

        // add some test data
        productStorage.save(Product("123", "My product", "A nice description"))
        productStorage.save(Product("456", "My other product", "A better description"))

        // add product routes
        product(productStorage)
    }



    routing {
        get("/api/getRandomFruit") {
            val randomFruits = arrayOf("apple", "banana", "pineapple", "grapes", "oranges", "watermelon")
            val randomNumber = Random.nextInt(6)
            //mapOf("user" to sampleUser)

            call.respond(
                HttpStatusCode.OK,
                """{"fruit": "${randomFruits[randomNumber]}"}""",

            )
        }
    }
}

fun Route.product(storage: Storage) {
    route("/products") {

        // GET /products
        get {
            call.respond(storage.getAll())
        }

        // GET /products/{id}
        get("/{id}") {
            val id = call.parameters["id"]!!
            val product = storage.get(id)
            if (product != null) {
                call.respond(product)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

