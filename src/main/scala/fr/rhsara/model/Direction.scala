package fr.rhsara.model

sealed trait Direction {

   def nextFromRight() : Direction

   def nextFromLeft() : Direction
}

case object North extends Direction {
  override def nextFromLeft(): Direction = West

  override def nextFromRight(): Direction = East
}

case object East extends Direction {
  override def nextFromRight(): Direction = North

  override def nextFromLeft(): Direction = South
}

case object West extends Direction {
  override def nextFromLeft(): Direction = South

  override def nextFromRight(): Direction = North
}

case object South extends Direction {
  override def nextFromLeft(): Direction = East

  override def nextFromRight(): Direction = West
}


object Direction {
  def apply(s:String):Option[Direction] = s match {
    case "N" => Some(North);
    case "E" => Some(East);
    case "S" => Some(South);
    case "W" => Some(West);
    case _ => None
  }
}
