import cats.data.EitherT
import domain.GameError

package object util {
  type ErrorOrT[F[_], A] = EitherT[F, GameError, A]

  def updateElementInSeq[A](seq: Seq[A])(pred: A => Boolean)(updater: A => A): Seq[A] = {
    val (elemForUpdate, restSeq) = seq.partition(pred)

    elemForUpdate.headOption.map(updater) match {
      case Some(value) => value +: restSeq
      case None        => restSeq
    }

    /*seq.map {
      case a if pred(a) => updater(a)
      case a            => a
    }*/
  }
}
