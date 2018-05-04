package it.jesinity.playground.protobuf

import java.io.{File, FileInputStream, FileOutputStream}

import it.jesinity.playground.protobuf.domain.{ProtocolMessage, SIPRegisterRequest}

object EncodeAndDecodeProtobufs {

  val outputFile = new File("/tmp/sipInProtobuf.bin")

  def main(args: Array[String]): Unit = {

    val request = SIPRegisterRequest(
      "Bob <sips:bob@biloxi.example.com>;tag=a73kszlfl",
      "Bob <sips:bob@biloxi.example.com>",
      "1j9FpLxk3uxtm8tn@biloxi.example.com",
      70
    )

    val protocolMessage: ProtocolMessage =
      ProtocolMessage().withSipRegisterRequest(request)

    protocolMessage.writeTo(new FileOutputStream(outputFile))

    val readBackMessage = ProtocolMessage.parseFrom(new FileInputStream(outputFile))

    println(readBackMessage)

    if (readBackMessage.payload.isSipRegisterRequest ) {
      val readBackRequest = readBackMessage.payload.sipRegisterRequest.get
      println(s"what I wrote is what I read? ${readBackRequest.equals(request)}")
    }
  }
}
