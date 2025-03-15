package org.example

import khttp.get
import khttp.responses.Response
import org.json.JSONObject
import java.io.File

fun main() {
    val inicial = File("config.txt")
    var mensaje : Response
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
        val configurar = readln()
        File("config.txt").appendText(configurar)
        println("Guardando...")
    }

    var postal = inicial.readLines().toString()
    var objeto : JSONObject
    while(true) {
        //Hacer pedido a la api
        println(postal)
        mensaje = get(
            url = "http://api.weatherapi.com/v1/forecast.json",
            params = mapOf(
                "key" to "9bcc50a7119f44be93c130505251103",
                "q" to postal
            )
        )
        println(mensaje)


        objeto = mensaje.jsonObject
        println(objeto)

        //File("in.json").writeText(mensaje.jsonObject.toString())
        if(mensaje.statusCode == 200){
            break
        }

        print(
            "\n" +
                    "\n" +
                    "  ______                     _ \n" +
                    " |  ____|                   | |\n" +
                    " | |__   _ __ _ __ ___  _ __| |\n" +
                    " |  __| | '__| '__/ _ \\| '__| |\n" +
                    " | |____| |  | | | (_) | |  |_|\n" +
                    " |______|_|  |_|  \\___/|_|  (_)\n" +
                    "                               \n" +
                    "                               \n" +
                    "\n"
        )
        println("Ciudad Inválida! Intentalo de nuevo.")
        inicial.delete()
        val configurar = readln()
        File("config.txt").appendText(configurar)
        postal = inicial.readLines().toString()
        println(postal)
    }
    /*mensaje ya tiene el objeto json pero usarlo de un solo sería una terrible idea para mi sanidad mental y para
    la barbaridad que hay que hacer despues.*/

    /*Como la API no lo da en un formato bonito toca hacer esta barbaridad, honestamente no quise usar for porque no supe como.
    Basicamente como la API da objetos de JSON Anidados (uno dentro de otro), lo que
    toca hacer es extraer cada objeto, de ahi el getJSONObject de objeto, que ya era el objeto json base que daba la api.
    Esto es 0 eficiente, pero funciona hasta que quieres usar el objeto de forecast porque son como 5 objetos anidados en uno,
    pero mientras no queramos usar las predicciones no importa, es problema para Luis del futuro.*/

    print("\n" +
            "\n" +
            " ___  __    ___       ___  _____ ______   ________  ___  __    ________  _________   \n" +
            "|\\  \\|\\  \\ |\\  \\     |\\  \\|\\   _ \\  _   \\|\\   __  \\|\\  \\|\\  \\ |\\   __  \\|\\___   ___\\ \n" +
            "\\ \\  \\/  /|\\ \\  \\    \\ \\  \\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\/  /|\\ \\  \\|\\  \\|___ \\  \\_| \n" +
            " \\ \\   ___  \\ \\  \\    \\ \\  \\ \\  \\\\|__| \\  \\ \\   __  \\ \\   ___  \\ \\  \\\\\\  \\   \\ \\  \\  \n" +
            "  \\ \\  \\\\ \\  \\ \\  \\____\\ \\  \\ \\  \\    \\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\\\\\  \\   \\ \\  \\ \n" +
            "   \\ \\__\\\\ \\__\\ \\_______\\ \\__\\ \\__\\    \\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\_______\\   \\ \\__\\\n" +
            "    \\|__| \\|__|\\|_______|\\|__|\\|__|     \\|__|\\|__|\\|__|\\|__| \\|__|\\|_______|    \\|__|\n"+"\n")

    val locacion : JSONObject = objeto.getJSONObject("location")
    val actual : JSONObject = objeto.getJSONObject("current")
    //val futuro : JSONObject = objeto.getJSONObject("forecast")
    println(locacion["country"])
    println(locacion["name"])
    println("La temperatura actual es de: "+actual["temp_c"]+"°C")
    println("La sensación termica es de: "+actual["feelslike_c"]+"°C")
    println("La información fue actualizada el: "+actual["last_updated"])
    //println("Información de debug")
    //println(actual)
    //println(futuro)
}