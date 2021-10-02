import java.net.Socket
import java.io.{DataInputStream, DataOutputStream}

object TCPClient extends App {
    
    val socket = new Socket("localhost", 8000)

    //return input stream object from server socket, plays a role as encoder 
    val is = new DataInputStream(socket.getInputStream() )
    val os = new DataOutputStream(socket.getOutputStream() )

    println("Menu\n1. iPhone 12 Pro Max\n2. iPhone 12 Pro\n3. iPhone 12\n4. iPhone 12 Mini")
    println("Enter the item name to check for price")
    
    os.writeBytes(s"${scala.io.StdIn.readLine("Enter item name: ")}\n")

    //read a line from is
    var line = is.readLine()

    println(s"result: $line")

    socket.close

}