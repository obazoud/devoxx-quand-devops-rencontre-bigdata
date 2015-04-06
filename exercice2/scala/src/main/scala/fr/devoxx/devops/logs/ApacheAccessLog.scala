package fr.devoxx.devops.logs

import java.util.concurrent.TimeUnit

import com.google.common.cache.CacheBuilder
import net.sf.uadetector.ReadableUserAgent

case class ApacheAccessLog(host: String, ipRange:String, client: String, user: String, dateTime: String, method: String,
path: String, protocol: String, code: Integer, size: Long, referer: String,
agent: String, agentFamily: String) {
}

object ApacheAccessLog {
  import net.sf.uadetector.service._

  val ACCESS_LOG_PATTERN =
  "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+) \"(\\S+)\" \"(.+)\"".r
  val parser = UADetectorServiceFactory.getCachingAndUpdatingParser
  val cache = CacheBuilder.newBuilder.maximumSize(100).expireAfterWrite(2, TimeUnit.HOURS).build[String, ReadableUserAgent];

  def parse(line: String): ApacheAccessLog = {
    val res = ACCESS_LOG_PATTERN.findFirstMatchIn(line)
    if (res.isEmpty) {
      throw new RuntimeException("Error parsing: " + line);
    }
    val m = res.get
    val agent = m.group(11)
    var userAgent = cache.getIfPresent(agent)
    if (userAgent == null) {
      userAgent = parser.parse(agent)
      cache.put(agent, userAgent)
    }
    val host = m.group(1)
    val ipRange = host.substring(0, host.indexOf(".")) + ".x.x.x"
    ApacheAccessLog(host, ipRange, m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7),
      m.group(8).toInt, m.group(9).toLong,
      m.group(10), m.group(11), userAgent.getFamily().getName())
  }
}
