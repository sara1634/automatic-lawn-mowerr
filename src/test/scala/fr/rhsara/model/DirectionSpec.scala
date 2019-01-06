package fr.rhsara.model

import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}
import org.scalacheck.Gen

class DirectionSpec extends FlatSpec with Matchers with GeneratorDrivenPropertyChecks  {

  "Direction apply" should "return the correct Direction" in {

    Direction("N") should be(Some(North))
    Direction("E") should be(Some(East))
    Direction("S") should be(Some(South))
    Direction("W") should be(Some(West))


    val validDirections = List("N", "E", "S", "W")
    val invalidDirections = Gen.alphaStr suchThat (s => !validDirections.contains(s))

    forAll(invalidDirections) { x =>
      Direction(x) should be(None)
    }
  }

  "Direction nextFromLeft for North" should "return West" in {
    North.nextFromLeft() should be(West)
  }

  "Direction nextFromRight for North" should "return East" in {
    North.nextFromRight() should be(East)
  }

  "Direction nextFromLeft for East" should "return North" in {
    East.nextFromLeft() should be(North)
  }

  "Direction nextFromRight for East" should "return South" in {
    East.nextFromRight() should be(South)
  }

  "Direction nextFromLeft for West" should "return South" in {
    West.nextFromLeft() should be(South)
  }

  "Direction nextFromRight for West" should "return North" in {
    West.nextFromRight should be(North)
  }

  "Direction nextFromLeft for South" should "return East" in {
    South.nextFromLeft() should be(East)
  }

  "Direction nextFromRight for South" should "return West" in {
    South.nextFromRight() should be(West)
  }



}
