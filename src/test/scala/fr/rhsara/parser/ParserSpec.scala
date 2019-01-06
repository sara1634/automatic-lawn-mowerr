package fr.rhsara.parser

import fr.rhsara.model._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.{Gen, Shrink}


class ParserSpec  extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks {


  implicit def noShrink[T]: Shrink[T] = Shrink.shrinkAny


  //Mower parser
  "Parser parserMower" should "return an Option[mower]" in {

    val allCoordinates = for {
      n <- Gen.choose(0, Int.MaxValue)
      m <- Gen.choose(0, Int.MaxValue)
    } yield (n,m)

    val validDirections = Gen.oneOf("N", "E", "S", "W")


    forAll(allCoordinates, validDirections) { (x, d) =>
      val l = List(x._1.toString, x._2.toString, d)
      Parser.parseMower(l) should be(Some(Mower(Coordinate(x._1, x._2), Direction(d).get)))
    }
  }


  "Parser parserMower" should "return None when the direction is invalid" in {
    val validDirections = List("N", "E", "S", "W")
    val invalidDirections = Gen.alphaStr suchThat (s => !validDirections.contains(s))

    forAll(invalidDirections) { direction =>
      Parser.parseMower(List("0","0", direction)) should be(None)
    }
  }


  "Parser parserMower" should "return None when the coordinates are invalid" in {
    forAll { (n : String, m : String) =>
      Parser.parseMower(List(n, m, "N")) should be(None)
    }
  }

  "Parser parserMower" should "return None when the coordinates and the direction are invalid" in {

    val n = Gen.alphaStr
    val m = Gen.alphaStr
    //val invalidDirections = Gen.oneOf("A","B", "C","D","F","G","H","I","J","K","L","M","O","P","Q","R","T","U","V","X","Y","Z")
    val validDirections = List("N", "E", "S", "W")
    val invalidDirections = Gen.alphaStr suchThat (s => !validDirections.contains(s))

    forAll(n, m, invalidDirections) { (n, m, d) =>
      Parser.parseMower(List(n,m, d)) should be(None)
    }
  }


  "Parser parserMower" should "return a None when starting parameters are not complete " in {

    forAll { (n : Int, m : Int) =>
      val l = List(n.toString, m.toString)
      Parser.parseMower(l) should be(None)
    }
  }


  "Parser processMower" should "return a Right[mower]" in {

    val garden = Garden(Coordinate(10, 10), List())
    val startParameters = List("3", "4", "N")
    val actions1 = List("A", "D", "D", "A", "A", "G", "A")
    val actions2 = List("G", "A", "G", "A", "A", "U", "D")


    Parser.processMower(startParameters, actions1, garden) should be(Right(Mower(Coordinate(4, 3), East)))
    Parser.processMower(startParameters, actions2, garden) should be(Right(Mower(Coordinate(2, 2), West)))
  }


  "Parser processMower" should "return a Left when starting parameters are not complete " in {

    val garden = Garden(Coordinate(10, 10), List())
    val startParameters = List("3", "N")
    val actions1 = List("A", "A", "A")

    Parser.processMower(startParameters, actions1, garden) should be(Left("Creation parameters for the mowers are invalid"))
  }


  "Parser processMower" should "return a Left when direction in startingParameters is invalid" in {

    val garden = Garden(Coordinate(10,10), List())
    val actions = List("A")
    val validDirections = List("N", "E", "S", "W")
    val invalidDirections = Gen.alphaStr suchThat (s => !validDirections.contains(s))

    forAll(invalidDirections) { direction =>
      val l = List("0", "0", direction)
      Parser.processMower(l, actions, garden) should be(Left("Creation parameters for the mowers are invalid"))
    }
  }

  "Parser processMower" should "return a Left when coordinates in startingParameters are invalid" in {
    val garden = Garden(Coordinate(10,10), List())
    val actions = List("A")

    val allAlphaStr1 = Gen.alphaStr
    val allAlphaStr2 = Gen.alphaStr

    forAll(allAlphaStr1, allAlphaStr2) { (x, y) =>
      val l = List(x, y, "N")
      Parser.processMower(l, actions, garden) should be(Left("Creation parameters for the mowers are invalid"))

    }
  }

  "Parser processMower" should "return a Left when initialized mower isn't in the garden" in {
    val garden = Garden(Coordinate(100, 100), List())
    val actions = List("A")

    val genCoordinates1 = Gen.choose(Int.MinValue, Int.MaxValue) suchThat (x => x < 0 || x > 100)
    val genCoordinates2 = Gen.choose(Int.MinValue, Int.MaxValue) suchThat (x => x < 0 || x > 100)

    forAll(genCoordinates1, genCoordinates2) { (n, m) =>
      val l = List("0", n.toString, "N")
      Parser.processMower(l, actions, garden) should be(Left("Mower's position is outside of the garden"))

      val ll = List(n.toString, "0", "N")
      Parser.processMower(ll, actions, garden) should be(Left("Mower's position is outside of the garden"))

      val lll = List(n.toString, m.toString, "N")
      Parser.processMower(lll, actions, garden) should be(Left("Mower's position is outside of the garden"))
    }
  }


  "Parser processMowerList" should "return a garden with all the mowers" in {

    val garden = Garden(Coordinate(100, 100), List())

    val l1 = (List("5", "10", "N"), List("A", "D", "A", "A", "D"))
    val l2 = (List("42", "42", "E"), List("A", "A", "G", "G", "A"))
    val l3 = (List("2", "50", "S"), List("A", "D", "A", "D", "D"))

    val list = List(l1, l2, l3)

    val m1 = Mower(Coordinate(7, 11), South)
    val m2 = Mower(Coordinate(43, 42), West)
    val m3 = Mower(Coordinate(1, 49), East)

    val finalGarden = Garden(Coordinate(100, 100), List(m3, m2, m1))

    Parser.processMowerList(list, garden, 1) should be(finalGarden)
  }


  "Parser processMowerList" should "only add Right(Mower) to the garden" in {

    val garden = Garden(Coordinate(100, 100), List())

    val l1 = (List("5", "10"), List("A", "D", "A", "A", "D"))
    val l2 = (List("42", "E"), List("A", "A", "G", "G", "A"))
    val l3 = (List("103", "50", "S"), List("A", "D", "A", "D", "D"))
    val l4 = (List("0", "42", "E"), List("A", "A", "G", "G", "A"))

    val list = List(l1, l2, l3, l4)

    val m = Mower(Coordinate(1, 42), West)

    val finalGarden = Garden(Coordinate(100, 100), List(m))

    Parser.processMowerList(list, garden, 1) should be(finalGarden)
  }


//garen parser

  "Parser parserGarden" should "return Some(Garden)" in {

    val posNum1 = Gen.choose(0, Integer.MAX_VALUE)
    val posNum2 = Gen.choose(0, Integer.MAX_VALUE)

    forAll(posNum1, posNum2) { (x, y) =>
      val l = List(x.toString, y.toString)
      Parser.parseGarden(l) should be(Some(Garden(Coordinate(x, y), List())))
    }
  }


  "Parser parserGarden" should "return None when passing non-numerical strings as coordinates" in {

    val allAlphaStr1 = Gen.alphaStr
    val allAlphaStr2 = Gen.alphaStr

    forAll(allAlphaStr1, allAlphaStr2) { (x, y) =>
      val l = List(x, y)
      Parser.parseGarden(l) should be(None)
    }
  }


  "Parser parserGarden" should "return None when passing negative coordinates" in {

    val negNum1 = Gen.choose(Integer.MIN_VALUE, -1)
    val negNum2 = Gen.choose(Integer.MIN_VALUE, -1)

    forAll(negNum1) { n =>
      val l = List(n.toString, "1")
      Parser.parseGarden(l) should be(None)
    }

    forAll(negNum1, negNum2) { (n, m) =>
      val l = List(n.toString, m.toString)
      Parser.parseGarden(l) should be(None)
    }

    forAll(negNum1) { n =>
      val l = List("1", n.toString)
      Parser.parseGarden(l) should be(None)
    }
  }

}
