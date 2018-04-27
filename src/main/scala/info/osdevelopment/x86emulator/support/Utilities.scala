package info.osdevelopment.x86emulator.support

object Utilities {

  implicit class BinaryUnitLong(val self: Long) extends AnyVal {

    def Ki: Long = self * 1024L

    def Mi: Long = self * 1024L * 1024L

    def Gi: Long = self * 1024L * 1024L * 1024L

    def Ti: Long = self * 1024L * 1024L * 1024L * 1024L

    def Pi: Long = self * 1024L * 1024L * 1024L * 1024L * 1024L

    def Ei: Long = self * 1024L * 1024L * 1024L * 1024L * 1024L * 1024L

  }

}
