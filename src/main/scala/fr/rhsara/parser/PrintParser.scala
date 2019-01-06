package fr.rhsara.parser
import fr.rhsara.model._


object PrintParser {


  trait Show[T] {
    def show(t: T): String
  }

  object Show {

    implicit val CoordinateShow = new Show[Coordinate] {
      def show(c: Coordinate): String = c.x.toString + " " + c.y.toString
    }


    implicit val DirectionShow = new Show[Direction] {
      def show(d: Direction): String = d match {
        case North => "North"
        case East => "East"
        case South => "South"
        case _ => "West"
      }
    }

    implicit val MowerShow = new Show[Mower] {
      def show(mower: Mower): String = PrintParser.print(mower.position) + " - " + PrintParser.print(mower.direction)
    }

    implicit val GardenShow = new Show[Garden] {
      def show(garden: Garden): String = {
        val firstLine = PrintParser.print(garden.topRightPosition) + "\n"
        val mowersStr = garden.mowers.reverse.map(mower => PrintParser.print(mower)).mkString("\n")
        firstLine + mowersStr
      }

    }

  }

  def print[T](t : T)(implicit ev : Show[T]) : String = ev.show(t)
}