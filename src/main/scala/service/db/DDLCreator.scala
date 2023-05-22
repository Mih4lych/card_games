package service.db

import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._

object DDLCreator {
  def genDDl: String = {
    val tables = Seq(CardTable.table, PlayerTable.table, ResultTable.table, TeamTable.table)
    val ddl: PostgresProfile.DDL = tables.map(_.schema).reduce(_ ++ _)

    ddl.createIfNotExistsStatements.mkString(";\n")
  }
}
