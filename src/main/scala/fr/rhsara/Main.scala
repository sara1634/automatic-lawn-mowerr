package fr.rhsara

import grizzled.slf4j.Logger
import java.io._

import fr.rhsara.model.Garden
import fr.rhsara.parser.{Parser, PrintParser}

import scala.io.Source
import scala.io.Source.fromInputStream
import scala.util.{Failure, Success, Try}




object Main  extends App {


  val logger = Logger("Main Log")
  val filePath = if (!args.isEmpty) args(0) else "resources/mowers.txt"

  val tryReadFile = Try(Source.fromFile(filePath)) match {
    case Success(f) => f.getLines().toList
    case Failure(exception) =>  val sw = new StringWriter()
      exception.printStackTrace(new PrintWriter(sw))
      logger.error(sw.toString)
      System.exit(1)
  }


  val lines = tryReadFile.asInstanceOf[List[String]]

  if (lines.isEmpty) {
    logger.error("No line inside the file. Exiting the program")
    System.exit(2)
  }

  val gardenTest = Parser.parseGarden(lines(0).split(" ").toList) match {
    case Some(x) => x
    case _ => logger.error("First line describing the garden is not valid. Exiting the program")
      System.exit(3)
  }

  val garden = gardenTest.asInstanceOf[Garden]
  logger.info("The garden has been created")

  logger.info("Processing the lines for the mowers")
  val mowerLines = if (lines.length % 2 == 0) lines.drop(1).dropRight(1) else lines.drop(1)

  val linesToProcess = mowerLines.sliding(2, 2).toList.map(l => (l(0).split(" ").toList, l(1).toList.map(s => s.toString)))

  val endGarden = Parser.processMowerList(linesToProcess, garden, 1)

  val gardenString = PrintParser.print(endGarden)
  logger.info("\n" + gardenString)


  // write down in the file result
  /*
  val file = new File("resources/Result.txt")
  val bw = new BufferedWriter(new FileWriter(file))
  bw.write(gardenString)
  bw.close()
  */




}
