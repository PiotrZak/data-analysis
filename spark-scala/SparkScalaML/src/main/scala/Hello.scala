object Main {
  def main(args: Array[String]): Unit = {

    val objectives = List("Scala", "C++Basic")

    printTimeInCity("Warsaw")

//    今日の目標を表示 (View Todays Goals)
//    When running scala program from terminal at 16:00
//    Warsaw: Fri Dec 22 16:10:14 CET 2023
//    Tokyo: Sat Dec 23 00:11:42 CET 2023

    for (objective <- objectives) {
      println(objective)
    }

  }

  // 22.12.2023 - this function will work only when I am in Poland
  // cause +8 to Japan and -1 to London
  def printTimeInCity(city: String): Unit = {

    val nowInCurrentLocation = new java.util.Date

    if (city == "Warsaw") {
      println(nowInCurrentLocation)
    } else if (city == "Tokyo") {
      val now = new java.util.Date(nowInCurrentLocation.getTime + 8 * 60 * 60 * 1000)
      println(now)
    } else if (city == "London") {
      val now = new java.util.Date(nowInCurrentLocation.getTime - 1 * 60 * 60 * 1000)
      println(now)
    } else {
      println("Unknown city")
    }
  }

}