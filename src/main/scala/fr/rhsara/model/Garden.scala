package fr.rhsara.model
import fr.rhsara.parser.PrintParser
import grizzled.slf4j.Logger


case class Garden (topRightPosition : Coordinate, mowers : List[Mower]){
  val logger = Logger("Garden Log")

  def isValidCoordinate(coord : Coordinate): Boolean = {
    val isOccupied = this.mowers.exists(m => m.position == coord)
    if (isOccupied) {
      logger.info("Collision detected on position (" + PrintParser.print(coord) + ")")
      false
    }
    else {
      coord.x >= 0 && coord.y >= 0 && coord.x <= this.topRightPosition.x && coord.y <= topRightPosition.y
    }
  }

  def addMower(mower : Either[String, Mower]): Garden = {
    mower match {
      case Right(m)  => Garden(this.topRightPosition, m :: this.mowers)
      case Left(msg) =>
        logger.warn(msg)
        this
    }
  }
}
