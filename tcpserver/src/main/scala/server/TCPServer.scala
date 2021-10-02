package server
//Writing program for server
import java.net.{ServerSocket, Socket}
import java.io.{DataInputStream, DataOutputStream}
//we need threadpool to have concurrency for multithread processes
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.collection.mutable.ArrayBuffer


object TCPServer extends App {
    val Items = new ArrayBuffer[Item]()

    //Create Item
    val A: Item = new Item("iPhone 12 Pro Max", new RM(5299))
    val B: Item = new Item("iPhone 12 Pro", new RM(4899))
    val C: Item = new Item("iPhone 12", new RM(3899))
    val D: Item = new Item("iPhone 12 Mini", new RM(3399))

    //Append all the Item created into Items list
    Items.append(A)
    Items.append(B)
    Items.append(C)
    Items.append(D)

    //function to check the item's price
    def checkPrice(inputName: String): String = {
        
        var price = 0

        for (item <- Items) {
            //if the item's name input by user is same as the item's name in Items list
            if (item.name == inputName) {
                price = item.price.value
                return s"RM $price"
            }
        }
        //if the item's name input by user is not found in the list
        //print to user "There is no such item"
        return "There is no such item"
    }

    //create a port with number 8000 and there is a process link to the in-port of port 8000
    val server = new ServerSocket(8000)
    try {
        //the program will block the main thread until new connection is made 
        //client socket is a different port which is created by the socket server once connection is made       
        val client: Future[Socket] = Future {server.accept()}
        client.foreach(x => {

            process(x)
        })
        def process(client: Socket) {
            val client2: Future[Socket] = Future {server.accept()}
            client2.foreach(x => {
                process(x)
            })
           
            //return input stream object from client socket, plays a role as encoder 
            val is = new DataInputStream( client.getInputStream() ) 
            val os = new DataOutputStream( client.getOutputStream() ) 

            //read a line from is 
            var line: String = is.readLine()
            println(s"Keyword entered by user: $line")
            var price = checkPrice(line)
            
            //send a string to client 
            os.writeBytes(s"${price}\n")
            client.close
        }
        
    } catch {
        case x: Exception =>
            server.close
            
    }
    //prevent mainthread from dying
    scala.io.StdIn.readLine("Enter any key to shut down the server\n")
    server.close
}