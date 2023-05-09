import cats.data.EitherT
import domain.GameError

package object game {
  type ErrorOrT[F[_], A] = EitherT[F, GameError, A]

  def updateElementInSeq[A](seq: Seq[A])(pred: A => Boolean)(updater: A => A): Seq[A] = {
    val (elemForUpdate, restSeq) = seq.partition(pred)

    updater(elemForUpdate.head) +: restSeq
  }
}
