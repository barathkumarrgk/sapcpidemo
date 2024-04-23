import com.sap.it.api.mapping.*
import java.text.SimpleDateFormat
import java.util.Date

def String currentDateTime(String input){
      def tz = TimeZone.getTimeZone("UTC")
      def df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'+01:00'")
      df.setTimeZone(tz)
	return df.format(new Date())
}