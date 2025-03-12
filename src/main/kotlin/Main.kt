package org.example

import khttp.get
import khttp.responses.Response
import org.json.JSONObject

fun main() {
    println("Ingrese Ciudad o codigo postal de US")
    val postal = readln()
    val mensaje : Response = khttp.get(
        url = "http://api.weatherapi.com/v1/forecast.json",
        params = mapOf("key" to "9bcc50a7119f44be93c130505251103", "q" to postal)
    )
    println(mensaje)
    val objeto = mensaje.jsonObject
    val locacion = objeto.getJSONObject("location")
    val actual = objeto.getJSONObject("current")
    val futuro = objeto.getJSONObject("forecast")
    println(locacion["country"])
    println(locacion["name"])
    println(actual)
    println(futuro)


    //val ciudad = mensaje
    //println(objeto["location"])
    //println(objeto["current"])
    //for (i in objeto.toString())
}