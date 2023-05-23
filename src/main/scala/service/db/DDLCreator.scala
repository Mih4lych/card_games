package service.db

import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

object DDLCreator {
  def genDDl: String = {
    val tables = Seq(CardTable, PlayerTable, ResultTable, TeamTable, GameTable)
    val ddl: PostgresProfile.DDL = tables.map(_.schema).reduce(_ ++ _)

    ddl.createIfNotExistsStatements.mkString(";\n")
  }
}
