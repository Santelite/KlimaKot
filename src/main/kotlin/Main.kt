package org.example

//import com.sun.org.apache.bcel.internal.generic.IFEQ
import khttp.get
import khttp.responses.Response
import org.json.JSONObject
import java.io.File

fun main() {
    val confi = "config.txt"
    val inicial = File(confi)
    var mensaje : Response
    var objeto : JSONObject
    var dias : String = 0.toString()
    var hora : String = 0.toString()
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
        println("Ingrese Ciudad o Código postal de US")
        File(confi).writeText(readln())
        println("Guardando...")
        }

    while(true) {
        var postal = inicial.readLines().toString()

        while (true) {
            //Hacer pedido a la api
            //debug println(postal)
            mensaje = get(
                url = "http://api.weatherapi.com/v1/forecast.json",
                params = mapOf(
                    "key" to "9bcc50a7119f44be93c130505251103",
                    "q" to postal,
                    "lang" to "es",
                    "days" to dias.toString()
                )
            )
            //debug(debería regresar la como respuesta el codigo 200) println(mensaje)

            objeto = mensaje.jsonObject
            //debug(debería mostrar el json que da la api) println(objeto)

            //File("in.json").writeText(mensaje.jsonObject.toString())

            //si la api regreso algo válido entonces romper el bucle
            if (mensaje.statusCode == 200) {
                break
            }

            //si no regreso algo válido entonces mostrar error y seguir bucle hasta que se resuelva
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
            //inicial.delete()
            File(confi).writeText(readln())
            postal = inicial.readLines().toString()
            //debug println(postal)
        }

        //el titulo asi bien chivo
        print(
            "\n" +
                    "\n" +
                    " ___  __    ___       ___  _____ ______   ________  ___  __    ________  _________   \n" +
                    "|\\  \\|\\  \\ |\\  \\     |\\  \\|\\   _ \\  _   \\|\\   __  \\|\\  \\|\\  \\ |\\   __  \\|\\___   ___\\ \n" +
                    "\\ \\  \\/  /|\\ \\  \\    \\ \\  \\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\/  /|\\ \\  \\|\\  \\|___ \\  \\_| \n" +
                    " \\ \\   ___  \\ \\  \\    \\ \\  \\ \\  \\\\|__| \\  \\ \\   __  \\ \\   ___  \\ \\  \\\\\\  \\   \\ \\  \\  \n" +
                    "  \\ \\  \\\\ \\  \\ \\  \\____\\ \\  \\ \\  \\    \\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\\\\\  \\   \\ \\  \\ \n" +
                    "   \\ \\__\\\\ \\__\\ \\_______\\ \\__\\ \\__\\    \\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\_______\\   \\ \\__\\\n" +
                    "    \\|__| \\|__|\\|_______|\\|__|\\|__|     \\|__|\\|__|\\|__|\\|__| \\|__|\\|_______|    \\|__|\n" + "\n"
        )

        /*Basicamente como la API da objetos de JSON Anidados (uno dentro de otro), lo que
        toca hacer es extraer cada objeto, de ahi el getJSONObject de objeto, que ya era el objeto json base que daba la api.
        Esto funciona hasta que queres usar el objeto de forecast porque son como 5 objetos anidados en uno,
        pero mientras no queramos usar las predicciones no importa, es problema para Luis del futuro.*/

        val locacion: JSONObject = objeto.getJSONObject("location")
        val actual: JSONObject = objeto.getJSONObject("current")
        val futuro : JSONObject = objeto.getJSONObject("forecast")
        if(dias == 0.toString()) {
            println(locacion["country"])
            println(locacion["name"])
            println("La temperatura actual es de: " + actual["temp_c"] + "°C")
            println("La sensación termica es de: " + actual["feelslike_c"] + "°C")
            println("La información fue actualizada el: " + actual["last_updated"])

        } else if(dias.toIntOrNull() != null && hora.toIntOrNull() != null && dias.toInt() <= 14 && hora.toInt() <= 24) { //si el usuario quiere romper el codigo no lo voy a dejar.
            //ya es Luis del futuro pero al menos encontre de hacerlo de una forma menos horrible, es lo mismo pero con variables
            val x = futuro.getJSONArray("forecastday")
            val fechafutura = x.getJSONObject(dias.toInt() - 1)
            val climafuturo = fechafutura.getJSONObject("day")
            val todaHora = fechafutura.getJSONArray("hour")
            val porHora = todaHora.getJSONObject(hora.toInt())

            //presentar la info
            println("La fecha y hora será: " + porHora["time"])
            println("La temperatura Max será de " + climafuturo["maxtemp_c"] + "°C, y la Min de " + climafuturo["mintemp_c"] + "°C")
            println("La probabilidad de lluvia es de " + climafuturo["daily_chance_of_rain"] + "%")
            println("La temperatura a esa hora será de " + porHora["temp_c"] + "°C")
        } else {
            println("Hora o día inválido! Vuelve a intentarlo...") //buen intento bro JKAJKJA
        }
        //println("Información de debug")
        //println(actual)
        //println(futuro)
        print("\n")
        println("Opciones:")
        println("1. Ver predicción del clima")
        println("2. Cambiar Ciudad")
        println("3. Salir")
        when(readln().toIntOrNull()) {
            1 -> {
                    //ya sirve ya lo arregle :)
                    println("Cuantos días en el futuro? (Max. 14 Días)")
                    dias = readln().toString()
                    println("En que horas? (24h)")
                    hora = readln().toString()
            }
            2 -> {
                println("Ingrese Ciudad o Código Postal de US")
                File(confi).writeText(readln())
            }
            3 -> {
                println("Saliendo")
                break
            }
            else -> println("Comando Inválido")
        }
    }
}