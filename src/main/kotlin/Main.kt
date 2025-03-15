package org.example

import khttp.get
import khttp.responses.Response
import org.json.JSONObject
import java.io.File

fun main() {
    val inicial = File("config.txt")
    //if(inicial.exists())
    val postal = readln()//le pide al usuario una ciudad o codigo postal, probablemente debería crear un manejador de errores.
    //val configuracion = File("config.txt")
    if(inicial.length() == 0L) {
        print("\n" +
                "\n" +
                "   _____             __ _           _____       _      _       _ \n" +
                "  / ____|           / _(_)         |_   _|     (_)    (_)     | |\n" +
                " | |     ___  _ __ | |_ _  __ _      | |  _ __  _  ___ _  __ _| |\n" +
                " | |    / _ \\| '_ \\|  _| |/ _` |     | | | '_ \\| |/ __| |/ _` | |\n" +
                " | |___| (_) | | | | | | | (_| |_   _| |_| | | | | (__| | (_| | |\n" +
                "  \\_____\\___/|_| |_|_| |_|\\__, (_) |_____|_| |_|_|\\___|_|\\__,_|_|\n" +
                "                           __/ |                                 \n" +
                "                          |___/                                  \n" +
                "\n")
        println("Ingrese Ciudad o codigo postal de US")
        File("config.txt").appendText(readln())
    }
    val mensaje : Response = get(
        url = "http://api.weatherapi.com/v1/forecast.json",
        params = mapOf("key" to "9bcc50a7119f44be93c130505251103", "q" to postal)//que buena idea dejar la llave sin obfuscar.
    )//Pedirle el JSON a la API, el cual contiene todos los datos que queremos.
    println(mensaje)//puro debug

    /*mensaje ya tiene el objeto json pero usarlo de un solo sería una terrible idea para mi sanidad mental y para
    la barbaridad que hay que hacer despues.*/
    val objeto : JSONObject = mensaje.jsonObject

    /*Como la API no lo da en un formato bonito toca hacer esta barbaridad, honestamente no quise usar for porque no supe como.
    Basicamente como la API da objetos de JSON Anidados (uno dentro de otro), lo que
    toca hacer es extraer cada objeto, de ahi el getJSONObject de objeto, que ya era el objeto json base que daba la api.
    Esto es 0 eficiente, pero funciona hasta que quieres usar el objeto de forecast porque son como 5 objetos anidados en uno,
    pero mientras no queramos usar las predicciones no importa, es problema para Luis del futuro.*/

    val locacion : JSONObject = objeto.getJSONObject("location")
    val actual : JSONObject = objeto.getJSONObject("current")
    val futuro : JSONObject = objeto.getJSONObject("forecast")
    println(locacion["country"])
    println(locacion["name"])
    println("La temperatura actual es de: "+actual["temp_c"]+"°C")
    println("La sensación termica es de: "+actual["feelslike_c"]+"°C")
    println("La información fue actualizada el: "+actual["last_updated"])
    println("Información de debug")
    println(actual)
    println(futuro)
}