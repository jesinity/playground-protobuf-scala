package it.jesinity.playground.protobuf

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Files, Paths}

import com.sksamuel.avro4s.{AvroInputStream, AvroOutputStream}
import it.jesinity.playground.protobuf.domain.SIPRegisterRequest
import spray.json.DefaultJsonProtocol
import scala.io.Source

/**
  * an alternative representation
  * @param from
  * @param to
  * @param callId
  * @param maxForward
  * @param via
  * @param callSequence
  * @param contact
  * @param contentLength
  */
case class SIPRegisterRequest2(
                                from: String,
                                to: String,
                                callId: String = "",
                                maxForward: Int = 0,
                                via: Predef.String = "",
                                callSequence: Predef.String = "",
                                contact: Predef.String = "",
                                contentLength: Int = 0
                              )

object MyJsonProtocol extends DefaultJsonProtocol {

  implicit val sipRegisterRequest = jsonFormat8(SIPRegisterRequest2)

}


object CompareProtobufsAvroAndJson {

  val avroSchema = Source
    .fromInputStream(
      this.getClass.getResourceAsStream("sipRegisterRequest.avsc")
    )
    .getLines()
    .mkString("\n")

  def main(args: Array[String]): Unit = {

    val request = SIPRegisterRequest(
      "Bob <sips:bob@biloxi.example.com>;tag=a73kszlfl",
      "Bob <sips:bob@biloxi.example.com>",
      "1j9FpLxk3uxtm8tn@biloxi.example.com",
      70
    )

    val request2 = SIPRegisterRequest2(
      "Bob <sips:bob@biloxi.example.com>;tag=a73kszlfl",
      "Bob <sips:bob@biloxi.example.com>",
      "1j9FpLxk3uxtm8tn@biloxi.example.com",
      70
    )

    val file             = new File("/tmp/sipInAvro.bin")
    val fileOutputStream = new FileOutputStream(file)
    val output           = AvroOutputStream.data[SIPRegisterRequest](fileOutputStream)
    output.write(request)
    output.close()

    val readBackStream               = AvroInputStream.data[SIPRegisterRequest]("/tmp/sipInAvro.bin")
    val readBack: SIPRegisterRequest = readBackStream.iterator.toList.head
    readBackStream.close()

    println(s"what I wrote is what I read? ${readBack.equals(request)}")

    import java.nio.file.Files
    import java.nio.file.Paths
    import MyJsonProtocol._
    import spray.json._

    Files.write(Paths.get("/tmp/sipInJson.bin"), request2.toJson.compactPrint.getBytes)
  }
}


