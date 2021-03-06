/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2009-2010, Jesse Eichar             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scalax.io

import scala.collection.mutable.Buffer
import Line.Terminators._

/**
 * Creates a Traversable[String] from a Traversable[Char] where each String is a line as indicated by the
 * [[scalax.io.Line.Terminators.Terminator]].
 *
 *
 * @see [[scalax.io.Input]]
 * @see [[scalax.io.ReadChars]]
 */
class LineTraversable(source: Traversable[Char], terminator: Terminator, includeTerminator: Boolean) extends LongTraversable[String] {
    def foreach[U](f: String => U) : Unit = {
        val buffer = source.foldLeft(Buffer[Char]()) {
            case (buffer, nextChar) =>
              terminator.split(buffer :+ nextChar) match {
                case split @ LineSplit(_,_,nextLine) if nextLine.nonEmpty => {
                  f(split toString includeTerminator)
                  Buffer(nextLine:_*)
                }
                case _ =>
                  buffer += nextChar
                  buffer
              }
        }

        if (buffer.nonEmpty) {
          val line = terminator.split(buffer).toString(includeTerminator)
          f (line)
        }
    }
}
