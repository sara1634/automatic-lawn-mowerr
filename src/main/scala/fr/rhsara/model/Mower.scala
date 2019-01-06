package fr.rhsara.model
import  grizzled.slf4j.Logger

case class Mower (position: Coordinate, direction: Direction){
  val logger = Logger("Mower Log")

  def rotateRight(): Mower = {
    Mower(this.position, this.direction.nextFromRight())
  }

  def rotateLeft(): Mower = {
    Mower(this.position, this.direction.nextFromLeft())
  }

  def advance(garden: Garden): Mower = {
    val newCoord = this.direction match {
      case North => Coordinate(this.position.x, this.position.y + 1)
      case East => Coordinate(this.position.x + 1, this.position.y)
      case South => Coordinate(this.position.x, this.position.y - 1)
      case _ => Coordinate(this.position.x - 1, this.position.y)
    }
    if (garden.isValidCoordinate(newCoord)) Mower(newCoord, this.direction)
    else this
  }


  def update(action: String, garden: Garden): Mower = {
    action match {
      case "A" => this.advance(garden);
      case "D" => this.rotateRight();
      case "G" => this.rotateLeft();
      case x =>
        logger.warn("Unknown action \" " + x + " \" detected")
        this
    }
  }

}



object Mower {
  def processUpdates(actions: List[String], garden: Garden, accumulator: Mower) : Mower = {
    actions match {
      case x :: xs => processUpdates(xs, garden, accumulator.update(x, garden))
      case _ => accumulator
    }
  }

}
